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