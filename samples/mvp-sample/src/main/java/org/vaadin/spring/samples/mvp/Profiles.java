/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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