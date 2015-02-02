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
package org.vaadin.spring.samples.mvp.security.dto;

/**
 * Describes the privileges associated with a functional role.
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public interface Role {

	public static final String ADMIN = "Administrator";
	public static final String PUBLIC = "Public";

	String getRoleName();

	boolean isGod();

	boolean canView();

	boolean canAdd();

	boolean canCopy();

	boolean canEdit();

	boolean canDelete();

	boolean canDownload();

	boolean canUpload();

}