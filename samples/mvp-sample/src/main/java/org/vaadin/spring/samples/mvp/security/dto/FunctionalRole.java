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
 * <p>
 * Describes a functional role within an application.<br/>
 * Privileges are comprised of a set of boolean calls.
 * </p>
 *
 * @author Chris Phillipson (fastnsilver@gmail.com)
 *
 */
public enum FunctionalRole implements Role {

	// RO = Read-only
	// RW = Read/Write

	ADMIN(Role.ADMIN, true), PUBLIC(Role.PUBLIC, true, false, false, true,
	        false, true, false);

	private String roleName;

	private boolean isAdmin;

	private boolean canView;

	private boolean canAdd;

	private boolean canCopy;

	private boolean canEdit;

	private boolean canDelete;

	private boolean canDownload;

	private boolean canUpload;

	private FunctionalRole(final String roleName, final boolean isAdmin) {
		this.roleName = roleName;
		this.isAdmin = isAdmin;
	}

	private FunctionalRole(final String roleName, final boolean canView, final boolean canAdd, final boolean canCopy,
			final boolean canEdit, final boolean canDelete, final boolean canDownload, final boolean canUpload) {
		this.roleName = roleName;
		this.canView = canView;
		this.canAdd = canAdd;
		this.canCopy = canCopy;
		this.canEdit = canEdit;
		this.canDelete = canDelete;
		this.canDownload = canDownload;
		this.canUpload = canUpload;
	}

	/* (non-Javadoc)
	 * @see org.vaadin.spring.samples.mvp.commons.security.Role#getRoleName()
	 */
	@Override
	public String getRoleName() {
		return roleName;
	}

	@Override
	public boolean isGod() {
		boolean result = false;
		if (isAdmin) {
			result = true;
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.vaadin.spring.samples.mvp.commons.security.Role#canView()
	 */
	@Override
	public boolean canView() {
		return isGod() ? isGod() : canView;
	}

	/* (non-Javadoc)
	 * @see org.vaadin.spring.samples.mvp.commons.security.Role#canAdd()
	 */
	@Override
	public boolean canAdd() {
		return isGod() ? isGod() : canAdd;
	}

	/* (non-Javadoc)
	 * @see org.vaadin.spring.samples.mvp.commons.security.Role#canCopy()
	 */
	@Override
	public boolean canCopy() {
		return isGod() ? isGod() : canCopy;
	}

	/* (non-Javadoc)
	 * @see org.vaadin.spring.samples.mvp.commons.security.Role#canEdit()
	 */
	@Override
	public boolean canEdit() {
		return isGod() ? isGod() : canEdit;
	}

	/* (non-Javadoc)
	 * @see org.vaadin.spring.samples.mvp.commons.security.Role#canDelete()
	 */
	@Override
	public boolean canDelete() {
		return isGod() ? isGod() : canDelete;
	}

	/* (non-Javadoc)
	 * @see org.vaadin.spring.samples.mvp.commons.security.Role#canDownload()
	 */
	@Override
	public boolean canDownload() {
		return isGod() ? isGod() : canDownload;
	}

	/* (non-Javadoc)
	 * @see org.vaadin.spring.samples.mvp.commons.security.Role#canUpload()
	 */
	@Override
	public boolean canUpload() {
		return isGod() ? isGod() : canUpload;
	}

	public static FunctionalRole fromValue(final String roleName) {
		for (FunctionalRole r: FunctionalRole.values()) {
			if (r.roleName.equals(roleName)) {
				return r;
			}
		}
		throw new IllegalArgumentException(roleName);
	}

}
