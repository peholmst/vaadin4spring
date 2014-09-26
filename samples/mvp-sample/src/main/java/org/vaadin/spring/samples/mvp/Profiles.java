package org.vaadin.spring.samples.mvp;

/**
 * For use with {@link org.springframework.context.annotation.Profile}.
 * Type-safe interface for all supported profiles.  Use this interface
 * instead of a String literal to reference one or all of an application's
 * supported profiles.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public abstract class Profiles {

	/** Development environment */
	public static final String DEV = "dev";

	public static String[] allProfiles() {
		return new String[] { DEV };
	}
}