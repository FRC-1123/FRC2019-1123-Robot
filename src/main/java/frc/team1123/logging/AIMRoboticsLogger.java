/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.team1123.logging;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;
import org.yaml.snakeyaml.Yaml;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * AIM Robotics, FRC Team 1123 Logger.
 * <p>
 * <b>Do not instantiate this class directly in robot code! Robot applications
 * should use the <a href="https://www.slf4j.org">slf4j</a> mechanism for
 * obtaining a logger.</b>
 * <p>
 * This class implements an <a href="https://www.slf4j.org">slf4j</a> compliant
 * logger. The current log {@link Level} determines what is seen on the driver
 * station. All logging events are recorded in a log file on the roboRIO
 * regardless of the current log {@link Level} setting.
 * <p>
 * Log files are stored in the location specified by the <code>user.home</code>
 * system property on the roboRIO, usually <b>/home/lvuser</b>.
 * 
 * @author A. Black
 * 
 */
class AIMRoboticsLogger extends MarkerIgnoringBase {
	private static final long serialVersionUID = -6587975532400961716L;
	private static final int MSG_QUEUE_CAPACITY = 300;
	private static final int MAX_LOG_FILES = 12;
	private static final double MAX_SPACE_ALLOCATED = 1.0d / 3.0d;
	private static final Level DEFAULT_ROOT_LOGGER_LEVEL = Level.OFF;
	private static final String LOG_FILE_BASENAME = "run-log";
	private static final String LOG_DIR_NAME = "aim-robotics";
	private static final String LOG_THREAD_NAME = "AIMRoboticsLogger";
	private static final String LOG_CFG_FILENAME = "aim-robotics-logger-config.yaml";

	/**
	 * Log level enumerations.
	 */
	public enum Level {
		TRACE, DEBUG, INFO, WARN, ERROR, OFF
	}

	private static class _Msg {
		public Level level;
		public long threadId;
		public String threadName;
		public String methodName;
		public String loggerName;
		public String text;
		public Throwable throwable;
		public Date date;

		public _Msg(Level level, long threadId, String threadName, String methodName, String loggerName, String text,
				Throwable throwable) {
			this.level = level;
			this.threadId = threadId;
			this.threadName = threadName;
			this.methodName = methodName;
			this.loggerName = loggerName;
			this.text = text;
			this.throwable = throwable;
			this.date = new Date();
		}
	}

	private static final ConcurrentMap<String, Level> logLevelMap;
	private static final ArrayBlockingQueue<_Msg> msgQueue;
	private static final Runnable consumerTask;
	private static final Thread consumer;
	private static final String crlf = System.getProperty("line.separator");
	private static final SimpleDateFormat dfmtr = new SimpleDateFormat("HH:mm:ss");
	private static final StringBuilder buf = new StringBuilder();
	private static FileOutputStream fos;
	private static long logSpaceLimit;
	private static long logSpaceAvailable;
	private static long logBytesWritten = 0L;

	static {
		logLevelMap = new ConcurrentHashMap<String, AIMRoboticsLogger.Level>();
		msgQueue = new ArrayBlockingQueue<>(MSG_QUEUE_CAPACITY, true);
		consumerTask = new Runnable() {
			@Override
			public void run() {
				if (initialized()) {
					emitLogMessages();
				}
			}
		};
		consumer = new Thread(consumerTask, LOG_THREAD_NAME);
		consumer.setDaemon(true);
		consumer.setPriority(Thread.MIN_PRIORITY);
		consumer.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.err.println("***ERROR IN " + t.getName());
				e.printStackTrace(System.err);
			}
		});
		consumer.start();
	}

	/**
	 * Do logging initialization by setting up the logging thread if it's not
	 * already setup
	 * <p>
	 * 
	 * @return <code>true</code> initialization was successful and
	 *         </code>false</code> otherwise.
	 */
	private static boolean initialized() {
		boolean rtn = true;
		try {
			File userHomeFile = new File(System.getProperty("user.home"));
			File deployDir = new File(userHomeFile, "deploy");
			loadConfiguration(deployDir);
			File loggingDir = new File(userHomeFile, LOG_DIR_NAME);
			if (!loggingDir.exists())
				loggingDir.mkdir();
			FileFilter filter = new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					boolean rtn = pathname.getName().contains(LOG_FILE_BASENAME);
					return rtn;
				}
			};
			long logSpaceUsed = 0L;
			File[] logs = loggingDir.listFiles(filter);
			Arrays.sort(logs, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			int delCnt = logs.length - MAX_LOG_FILES + 1;
			for (int i = logs.length - 1; i > -1; i--) {
				logSpaceUsed += logs[i].length();
				if (i >= delCnt) {
					break;
				}
			}
			if (logs.length >= MAX_LOG_FILES) {
				while (delCnt > 0) {
					delCnt--;
					logs[delCnt].delete();
				}
			}

			long freeSpace = userHomeFile.getFreeSpace();
			long usableSpace = userHomeFile.getUsableSpace();
			long totalSpace = userHomeFile.getTotalSpace();

			double orginSpace = usableSpace + logSpaceUsed;
			logSpaceLimit = Double.valueOf(orginSpace * MAX_SPACE_ALLOCATED).longValue();
			logSpaceAvailable = logSpaceLimit - logSpaceUsed;

			GregorianCalendar gcal = new GregorianCalendar(Locale.getDefault());
			String runLogName = MessageFormat.format("{0}-{1,number,000}-{2,number,00}{3,number,00}{4,number,00}.txt",
					LOG_FILE_BASENAME, gcal.get(Calendar.DAY_OF_YEAR), gcal.get(Calendar.HOUR_OF_DAY),
					gcal.get(Calendar.MINUTE), gcal.get(Calendar.SECOND));
			File runLogFile = new File(loggingDir, runLogName);
			fos = new FileOutputStream(runLogFile);
			SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
			for (int i = 0; i < 80; i++) {
				logBytesWritten++;
				fos.write('*');
			}
			fos.write(crlf.getBytes());
			logBytesWritten += crlf.length();

			_fosWrite(MessageFormat.format("*** Logging Start...... = {0}", df.format(new Date())).getBytes());
			_fosWrite(MessageFormat.format("*** Free Space......... = {0}", freeSpace).getBytes());
			_fosWrite(MessageFormat.format("*** Usable Space....... = {0}", usableSpace).getBytes());
			_fosWrite(MessageFormat.format("*** Total  Space....... = {0}", totalSpace).getBytes());
			_fosWrite(MessageFormat.format("*** Log Space Used..... = {0}", logSpaceUsed).getBytes());
			_fosWrite(MessageFormat.format("*** Log Space Limit.... = {0}", logSpaceLimit).getBytes());
			_fosWrite(MessageFormat.format("*** Log Space Available = {0}", logSpaceAvailable).getBytes());

			for (int i = 0; i < 80; i++) {
				logBytesWritten++;
				fos.write('*');
			}
			fos.write(crlf.getBytes());
			logBytesWritten += crlf.length();

			fos.flush();

		} catch (Throwable t) {
			rtn = false;
			System.err.println("***ERROR IN " + Thread.currentThread().getName());
			t.printStackTrace(System.err);
		}
		return rtn;
	}

	/**
	 * Load logging level configuration from the aim-robotics-logger.json file
	 * located in the deploy directory.
	 * 
	 * @param deployDir the deploy directory {@link File} object.
	 */
	private static void loadConfiguration(File deployDir) {
		//
		// Setup the default root logger level.
		//
		logLevelMap.put("", DEFAULT_ROOT_LOGGER_LEVEL);

		//
		// Check for the existence of the deploy directory. If it doesn't exist then we
		// go with the default root logger level and return.
		//
		String warnMsgPattern = " does not exist. Using default root logger of ";
		if (!deployDir.exists()) {
			StringBuilder b = new StringBuilder();
			b.append('\'').append(deployDir.getAbsolutePath()).append('\'').append(warnMsgPattern).append('\'')
					.append(DEFAULT_ROOT_LOGGER_LEVEL.name()).append("'.");
			System.out.println(b.toString());
			return;
		}

		//
		// See if we can load the logger config file.
		//
		File cfgFile = new File(deployDir, LOG_CFG_FILENAME);
		if (!cfgFile.exists()) {
			StringBuilder b = new StringBuilder();
			b.append('\'').append(cfgFile.getAbsolutePath()).append('\'').append(warnMsgPattern).append('\'')
					.append(DEFAULT_ROOT_LOGGER_LEVEL.name()).append("'.");
			System.out.println(b.toString());
			return;
		}

		try {
			Yaml cfg = new Yaml();
			InputStream is = new FileInputStream(cfgFile);
			Map<String, String> cfgMap = cfg.load(is);
			for (Entry<String, String> cfgEntry : cfgMap.entrySet()) {
				String lvl = cfgEntry.getValue();
				if (lvl != null && !lvl.isBlank()) {
					try {
						String ucLvl = lvl.toUpperCase();
						Level level = Level.valueOf(ucLvl);
						logLevelMap.put(cfgEntry.getKey(), level);
					} catch (IllegalArgumentException e) {
						System.out.println("Logger: Ignoring '" + cfgEntry.getKey() + "' since '" + lvl
								+ "' is an invalid logging level. Level must be in the set {TRACE, DEBUG, INFO, WARN, ERROR, OFF}.");
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage() + " - Using default root logger of " + DEFAULT_ROOT_LOGGER_LEVEL.name());
		}
	}

	/**
	 * Emit the message to the log file and driver station as appropriate.
	 */
	private static void emitLogMessages() {
		while (fos != null) {
			try {
				_Msg msg = msgQueue.take();

				byte[] msgBytes = fmtMsg(msg);

				_fosWrite(msgBytes);

				if (isLogLevelEnabled(msg.loggerName, msg.level)) {
					System.out.write(msgBytes);
					System.out.println();
					System.out.flush();
					try {
						if (msg.level == Level.ERROR) {
							DriverStation.reportError(new String(msgBytes), false);
						} else if (msg.level == Level.WARN) {
							DriverStation.reportWarning(new String(msgBytes), false);
						}
					} catch (Throwable t) {
						// ignore so we don't die on Driver Station errors.
					}
				}

			} catch (Throwable t) {
				t.printStackTrace(System.err);
				break;
			}
		}
	}

	private static void _fosWrite(byte[] msgBytes) throws IOException {
		if (logBytesWritten + msgBytes.length > logSpaceAvailable) {
			throw new RuntimeException("Available logging space exceeded. Logging has been stopped.");
		}
		fos.write(msgBytes);
		fos.write(crlf.getBytes());
		fos.flush();
		logBytesWritten += msgBytes.length + crlf.length();
	}

	/**
	 * Format the message.
	 * 
	 * @param msg the {@link _Msg} object.
	 * @return a formatted message string.
	 */
	private static byte[] fmtMsg(_Msg msg) {
		buf.delete(0, buf.length());
		buf.append(dfmtr.format(msg.date)).append(" [").append(msg.threadId).append(':').append(msg.threadName)
				.append("] [").append(msg.methodName).append("] [").append(msg.level.name()).append("] ")
				.append(msg.text);
		if (msg.throwable != null) {
			buf.append(crlf);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			msg.throwable.printStackTrace(ps);
			buf.append(baos.toString());
		}
		return buf.toString().getBytes();
	}

	/**
	 * Determine the calling method.
	 * 
	 * @return a string of className.methodName where className is the simple class
	 *         name.
	 */
	private static String callingMethod() {
		final int iFrame = 5;
		StackTraceElement[] stackFrames = Thread.currentThread().getStackTrace();
		String rtn = "";
		String className;

		int ndx = stackFrames[iFrame].getClassName().lastIndexOf('.');
		if (ndx == -1)
			className = stackFrames[iFrame].getClassName();
		else
			className = stackFrames[iFrame].getClassName().substring(ndx + 1);
		rtn = className + "." + stackFrames[iFrame].getMethodName();

		return rtn;
	}

	/**
	 * Determine if a logger level is enabled.
	 * 
	 * @param loggerName the name of the logger to check the level on.
	 * @param level      the {@link Level} that is begin checked.
	 * @return <code>true</code> if level is enabled and <code>false</code>
	 *         otherwise
	 */
	private static boolean isLogLevelEnabled(String loggerName, Level level) {
		boolean rtn = false;
		if (loggerName != null) {
			String lgName = loggerName;
			Level cfgLevel = null;
			do {
				cfgLevel = logLevelMap.get(lgName);
				if (cfgLevel == null) {
					int ndx = lgName.lastIndexOf('.');
					if (ndx != -1) {
						lgName = lgName.substring(0, ndx);
					} else {
						cfgLevel = logLevelMap.get("");
					}
				}
			} while (cfgLevel == null);
			rtn = level.ordinal() >= cfgLevel.ordinal();
		}
		return rtn;
	}

	public AIMRoboticsLogger(String name) {
		this.name = name;
	}

	/**
	 * This is our internal implementation for logging regular (non-parameterized)
	 * log messages.
	 *
	 * @param level   One of the LOG_LEVEL_XXX constants defining the log level
	 * @param message The message itself
	 * @param t       The exception whose stack trace should be logged
	 */
	private void log(Level level, String message, Throwable t) {
		_Msg msg = new _Msg(level, Thread.currentThread().getId(), Thread.currentThread().getName(), callingMethod(),
				this.name, message, t);
		AIMRoboticsLogger.msgQueue.add(msg);
	}

	/**
	 * Stub routine used so we can make sure that the calling method is in the same
	 * call stack location when logging.
	 * 
	 * @param level
	 * @param message
	 * @param t
	 */
	private void _log(Level level, String message, Throwable t) {
		log(level, message, t);
	}

	/**
	 * For formatted messages, first substitute arguments and then log.
	 *
	 * @param level
	 * @param format
	 * @param arg1
	 * @param arg2
	 */
	private void formatAndLog(Level level, String format, Object arg1, Object arg2) {
		FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
		log(level, tp.getMessage(), tp.getThrowable());
	}

	/**
	 * For formatted messages, first substitute arguments and then log.
	 *
	 * @param level
	 * @param format
	 * @param arguments a list of 3 ore more arguments
	 */
	private void formatAndLog(Level level, String format, Object... arguments) {
		FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
		log(level, tp.getMessage(), tp.getThrowable());
	}

	// ========================================================================================
	// Public logging methods below this point.
	// ========================================================================================
	/**
	 * We always return true since we want to trap all log events in the current run
	 * log file.
	 */
	@Override
	public boolean isTraceEnabled() {
		return true;
	}

	@Override
	public void trace(String msg) {
		_log(Level.TRACE, msg, null);
	}

	@Override
	public void trace(String format, Object arg) {
		formatAndLog(Level.TRACE, format, arg, null);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		formatAndLog(Level.TRACE, format, arg1, arg2);
	}

	@Override
	public void trace(String format, Object... arguments) {
		formatAndLog(Level.TRACE, format, arguments);
	}

	@Override
	public void trace(String msg, Throwable t) {
		_log(Level.TRACE, msg, t);
	}

	/**
	 * We always return true since we want to trap all log events in the current run
	 * log file.
	 */
	@Override
	public boolean isDebugEnabled() {
		return true;
	}

	@Override
	public void debug(String msg) {
		_log(Level.DEBUG, msg, null);
	}

	@Override
	public void debug(String format, Object arg) {
		formatAndLog(Level.DEBUG, format, arg, null);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		formatAndLog(Level.DEBUG, format, arg1, arg2);
	}

	@Override
	public void debug(String format, Object... arguments) {
		formatAndLog(Level.DEBUG, format, arguments);
	}

	@Override
	public void debug(String msg, Throwable t) {
		_log(Level.DEBUG, msg, t);
	}

	/**
	 * We always return true since we want to trap all log events in the current run
	 * log file.
	 */
	@Override
	public boolean isInfoEnabled() {
		return true;
	}

	@Override
	public void info(String msg) {
		_log(Level.INFO, msg, null);
	}

	@Override
	public void info(String format, Object arg) {
		formatAndLog(Level.INFO, format, arg, null);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		formatAndLog(Level.INFO, format, arg1, arg2);
	}

	@Override
	public void info(String format, Object... arguments) {
		formatAndLog(Level.INFO, format, arguments);
	}

	@Override
	public void info(String msg, Throwable t) {
		_log(Level.INFO, msg, t);
	}

	/**
	 * We always return true since we want to trap all log events in the current run
	 * log file.
	 */
	@Override
	public boolean isWarnEnabled() {
		return true;
	}

	@Override
	public void warn(String msg) {
		_log(Level.WARN, msg, null);
	}

	@Override
	public void warn(String format, Object arg) {
		formatAndLog(Level.WARN, format, arg, null);
	}

	@Override
	public void warn(String format, Object... arguments) {
		formatAndLog(Level.WARN, format, arguments);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		formatAndLog(Level.WARN, format, arg1, arg2);
	}

	@Override
	public void warn(String msg, Throwable t) {
		_log(Level.WARN, msg, t);
	}

	/**
	 * We always return true since we want to trap all log events in the current run
	 * log file.
	 */
	@Override
	public boolean isErrorEnabled() {
		return true;
	}

	@Override
	public void error(String msg) {
		_log(Level.ERROR, msg, null);
	}

	@Override
	public void error(String format, Object arg) {
		formatAndLog(Level.ERROR, format, arg, null);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		formatAndLog(Level.ERROR, format, arg1, arg2);
	}

	@Override
	public void error(String format, Object... arguments) {
		formatAndLog(Level.ERROR, format, arguments);
	}

	@Override
	public void error(String msg, Throwable t) {
		_log(Level.ERROR, msg, t);
	}

}
