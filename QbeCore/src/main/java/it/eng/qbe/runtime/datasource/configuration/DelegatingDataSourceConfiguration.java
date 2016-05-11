/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.runtime.datasource.configuration;

import it.eng.qbe.runtime.datasource.configuration.dao.ICalculatedFieldsDAO;
import it.eng.qbe.runtime.datasource.configuration.dao.IInLineFunctionsDAO;
import it.eng.qbe.runtime.datasource.configuration.dao.IModelI18NPropertiesDAO;
import it.eng.qbe.runtime.datasource.configuration.dao.IModelPropertiesDAO;
import it.eng.qbe.runtime.datasource.configuration.dao.IRelationshipsDAO;
import it.eng.qbe.runtime.datasource.configuration.dao.IViewsDAO;
import it.eng.qbe.runtime.datasource.configuration.dao.fileimpl.InLineFunctionsDAOFileImpl.InLineFunction;
import it.eng.qbe.runtime.model.properties.IModelProperties;
import it.eng.qbe.runtime.model.properties.SimpleModelProperties;
import it.eng.qbe.runtime.model.structure.IModelRelationshipDescriptor;
import it.eng.qbe.runtime.model.structure.IModelViewEntityDescriptor;
import it.eng.qbe.runtime.model.structure.ModelCalculatedField;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DelegatingDataSourceConfiguration extends InMemoryDataSourceConfiguration {

	IModelI18NPropertiesDAO modelLabelsDAOFileImpl;
	IModelPropertiesDAO modelPropertiesDAO;
	ICalculatedFieldsDAO calculatedFieldsDAO;
	IInLineFunctionsDAO functionsDAO;

	IRelationshipsDAO relationshipsDAO;
	IViewsDAO viewsDAO;

	public DelegatingDataSourceConfiguration(String modelName) {
		super(modelName);
	}

	// ====================================================================
	// overrides
	// ====================================================================

	// datasource properties are managed in memory -> no delegation here
	// public Map<String, Object> loadDataSourceProperties() { ...

	@Override
	public IModelProperties loadModelProperties() {
		return modelPropertiesDAO.loadModelProperties();
	}

	@Override
	public IModelProperties loadModelI18NProperties() {
		return loadModelI18NProperties(null);
	}

	@Override
	public IModelProperties loadModelI18NProperties(Locale locale) {
		SimpleModelProperties properties = modelLabelsDAOFileImpl.loadProperties(locale);
		return properties;
	}

	@Override
	public Map loadCalculatedFields() {
		return calculatedFieldsDAO.loadCalculatedFields();
	}

	public void saveCalculatedFields(Map<String, List<ModelCalculatedField>> calculatedFields) {
		calculatedFieldsDAO.saveCalculatedFields(calculatedFields);
	}

	@Override
	public List<IModelRelationshipDescriptor> loadRelationships() {
		return relationshipsDAO.loadModelRelationships();
	}

	@Override
	public List<IModelViewEntityDescriptor> loadViews() {
		return viewsDAO.loadModelViews();
	}

	@Override
	public HashMap<String, InLineFunction> loadInLineFunctions(String dialect) {
		return functionsDAO.loadInLineFunctions(dialect);
	}

	// ====================================================================
	// Accessor methods
	// ====================================================================

	public IModelPropertiesDAO getModelPropertiesDAO() {
		return modelPropertiesDAO;
	}

	public void setModelPropertiesDAO(IModelPropertiesDAO modelPropertiesDAO) {
		this.modelPropertiesDAO = modelPropertiesDAO;
	}

	public ICalculatedFieldsDAO getCalculatedFieldsDAO() {
		return calculatedFieldsDAO;
	}

	public void setCalculatedFieldsDAO(ICalculatedFieldsDAO calculatedFieldsDAO) {
		this.calculatedFieldsDAO = calculatedFieldsDAO;
	}

	public IModelI18NPropertiesDAO getModelLabelsDAOFileImpl() {
		return modelLabelsDAOFileImpl;
	}

	public void setModelLabelsDAOFileImpl(IModelI18NPropertiesDAO modelLabelsDAOFileImpl) {
		this.modelLabelsDAOFileImpl = modelLabelsDAOFileImpl;
	}

	public IInLineFunctionsDAO getFunctionsDAO() {
		return functionsDAO;
	}

	public void setFunctionsDAO(IInLineFunctionsDAO functionsDAO) {
		this.functionsDAO = functionsDAO;
	}

	public IRelationshipsDAO getRelationshipsDAO() {
		return relationshipsDAO;
	}

	public void setRelationshipsDAO(IRelationshipsDAO relationshipsDAO) {
		this.relationshipsDAO = relationshipsDAO;
	}

	public IViewsDAO getViewsDAO() {
		return viewsDAO;
	}

	public void setViewsDAO(IViewsDAO viewsDAO) {
		this.viewsDAO = viewsDAO;
	}
}
