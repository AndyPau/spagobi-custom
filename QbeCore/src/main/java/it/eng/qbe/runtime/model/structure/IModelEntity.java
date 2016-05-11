/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.runtime.model.structure;

import java.util.Iterator;
import java.util.List;

public interface IModelEntity extends IModelNode {

	public IModelEntity getRoot();

	public String getType();

	public String getUniqueType();

	public String getRole();

	public List<IModelField> getAllFields();

	public IModelField getField(String fieldUniqueName);

	public IModelField getFieldByName(String fieldName);

	public List<IModelField> getFieldsByType(boolean isKey);

	public List<IModelField> getKeyFields();

	public Iterator<IModelField> getKeyFieldIterator();

	public List<IModelField> getNormalFields();

	public Iterator<IModelField> getNormalFieldIterator();

	public List<ModelCalculatedField> getCalculatedFields();

	public IModelEntity getSubEntity(String entityUniqueName);

	public List<IModelEntity> getSubEntities();

	public List<IModelEntity> getAllSubEntities();

	public List<IModelEntity> getAllSubEntities(String entityName);

	public List<IModelField> getAllFieldOccurencesOnSubEntity(String entityName, String fieldName);

	@Override
	public String toString();

	public String getPath();

	public int getDepth();

	public IModelField addNormalField(String fieldName);

	public IModelField addKeyField(String fieldName);

	public void addField(IModelField field);

	public void addCalculatedField(ModelCalculatedField calculatedField);

	public void deleteCalculatedField(String fieldName);

	public IModelEntity addSubEntity(String subEntityName, String subEntityRole, String subEntityType);

	public void addSubEntity(IModelEntity entity);

	public void setPath(String path);

	public void setRole(String role);

	public void setRoot(IModelEntity root);

	public void setType(String type);

	public IModelEntity clone(IModelEntity newParent, String parentEntity);
}
