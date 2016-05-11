/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.events.metadata;

import it.eng.spagobi.commons.metadata.SbiHibernateModel;

import java.util.Date;
import java.util.Set;

/**
 * @author Gioia
 *
 */
public class SbiEventsLog extends SbiHibernateModel {
	private Integer id;
	private String user;
	private Date date;
	private String desc;
	private String params;
	private String handlerClass; 
	private Set roles; 
	
	/**
	 * Instantiates a new sbi events log.
	 */
	public SbiEventsLog() {}
	
	/**
	 * Instantiates a new sbi events log.
	 * 
	 * @param id the id
	 * @param user the user
	 */
	public SbiEventsLog(Integer id, String user) {
		this.id = id;
		this.user = user;
	}
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * Gets the user.
	 * 
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Sets the user.
	 * 
	 * @param user the new user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 * 
	 * @param date the new date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Gets the desc.
	 * 
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Sets the desc.
	 * 
	 * @param desc the new desc
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * Gets the params.
	 * 
	 * @return the params
	 */
	public String getParams() {
		return params;
	}

	/**
	 * Sets the params.
	 * 
	 * @param params the new params
	 */
	public void setParams(String params) {
		this.params = params;
	}
	
	/**
	 * Gets the roles.
	 * 
	 * @return the roles
	 */
	public Set getRoles() {
		return roles;
	}

	/**
	 * Sets the roles.
	 * 
	 * @param roles the new roles
	 */
	public void setRoles(Set roles) {
		this.roles = roles;
	}

	/**
	 * Gets the handler.
	 * 
	 * @return the handler
	 */
	public String getHandlerClass() {
		return handlerClass;
	}

	/**
	 * Sets the handler.
	 * 
	 * @param handler the new handler
	 */
	public void setHandlerClass(String handler) {
		this.handlerClass = handler;
	}
}
