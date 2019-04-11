/**
 * 
 */
package frc.team1123.logging;

import java.util.ServiceLoader;

import org.slf4j.ILoggerFactory;
import org.slf4j.IMarkerFactory;
import org.slf4j.spi.MDCAdapter;
import org.slf4j.spi.SLF4JServiceProvider;

/**
 * AIM Robotics, Team 1123 <a href="https://www.slf4j.org">slf4j</a> service
 * provider.
 * <p>
 * <b>Robot code should NEVER use this class directly! The
 * <a href="https://www.slf4j.org">slf4j</a> framework loads this class via the
 * {@link ServiceLoader} mechanism.
 * 
 * @author A. Black
 *
 */
public class AIMRoboticsLoggerServiceProvider implements SLF4JServiceProvider {
	private static AIMRoboticsLoggerFactory loggerFactory;
	private static final Object lock = new Object();
	private static final String BINDING_COMPATIBILITY_VERSION = "1.8";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.slf4j.spi.SLF4JServiceProvider#getLoggerFactory()
	 */
	@Override
	public ILoggerFactory getLoggerFactory() {
		synchronized (lock) {
			if (loggerFactory==null)
				loggerFactory = new AIMRoboticsLoggerFactory();
		}
		return loggerFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.slf4j.spi.SLF4JServiceProvider#getMarkerFactory()
	 */
	@Override
	public IMarkerFactory getMarkerFactory() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.slf4j.spi.SLF4JServiceProvider#getMDCAdapter()
	 */
	@Override
	public MDCAdapter getMDCAdapter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.slf4j.spi.SLF4JServiceProvider#getRequesteApiVersion()
	 */
	@Override
	public String getRequesteApiVersion() {
		return BINDING_COMPATIBILITY_VERSION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.slf4j.spi.SLF4JServiceProvider#initialize()
	 */
	@Override
	public void initialize() {
	}

}
