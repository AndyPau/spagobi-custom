/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.runtime.query.filters;

import it.eng.qbe.runtime.dataset.QueryTransformer;
import it.eng.qbe.runtime.datasource.IDataSource;
import it.eng.qbe.runtime.model.accessmodality.AbstractModelAccessModality;
import it.eng.qbe.runtime.model.structure.IModelEntity;
import it.eng.qbe.runtime.model.structure.IModelStructure;
import it.eng.qbe.runtime.query.Filter;
import it.eng.qbe.runtime.query.Query;
import it.eng.qbe.runtime.statement.graph.GraphManager;
import it.eng.qbe.runtime.statement.graph.bean.QueryGraph;
import it.eng.qbe.runtime.statement.graph.bean.Relationship;
import it.eng.qbe.runtime.statement.graph.bean.RootEntitiesGraph;
import it.eng.qbe.runtime.statement.graph.cover.ShortestPathsCoverGraph;
import it.eng.qbe.runtime.statement.graph.validator.ConnectionValidator;
import it.eng.spagobi.commons.utilities.SpagoBIUtilities;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jgrapht.Graph;

public class ProfileAttributesModelAccessModality extends AbstractModelAccessModality {

	public static transient Logger logger = Logger.getLogger(ProfileAttributesModelAccessModality.class);

	private List<Filter> filters = null;

	public ProfileAttributesModelAccessModality(List<Filter> filters) {
		this.filters = filters;
	}

	protected List<Filter> getFilters() {
		return filters;
	}

	@Override
	public Query getFilteredStatement(Query query, IDataSource dataSource, Map userProfileAttributes) {
		if (filters == null || filters.isEmpty()) {
			logger.debug("No filters defined, returning input query unchanged");
			return query;
		}
		logger.debug("Some filters are defined, starting evaluation ...");
		Query toReturn = null;
		List<Filter> appliableFilters = this.getAppliableFilters(query, dataSource);
		Map<String, List<String>> filtersMap = getFiltersMap(appliableFilters, userProfileAttributes);
		try {
			toReturn = QueryTransformer.transform(query, dataSource, null, filtersMap);
		} catch (Exception e) {
			throw new SpagoBIEngineRuntimeException("Error while getting filtered query", e);
		}
		toReturn.setQueryGraph(query.getQueryGraph());
		logger.debug("Filtered query : [" + toReturn + "]");
		return toReturn;
	}

	private List<Filter> getAppliableFilters(Query query, IDataSource dataSource) {
		List<Filter> toReturn = new ArrayList<Filter>();
		Iterator<Filter> it = filters.iterator();
		while (it.hasNext()) {
			Filter filter = it.next();
			if (isApplicable(filter, query, dataSource)) {
				toReturn.add(filter);
			}
		}
		return toReturn;
	}

	private boolean isApplicable(Filter filter, Query query, IDataSource dataSource) {
		logger.debug("Evalutaing filter on field [" + filter.getField().getUniqueName() + "] ....");
		Query filteredQuery = applyFilter(filter, query, dataSource);

		IModelStructure modelStructure = dataSource.getModelStructure();
		RootEntitiesGraph rootEntitiesGraph = modelStructure.getRootEntitiesGraph(dataSource.getConfiguration().getModelName(), false);
		Graph<IModelEntity, Relationship> graph = rootEntitiesGraph.getRootEntitiesGraph();

		Set<IModelEntity> entities = filteredQuery.getQueryEntities(dataSource);
		QueryGraph queryGraph = GraphManager.getDefaultCoverGraphInstance(ShortestPathsCoverGraph.class.getName()).getCoverGraph(graph, entities);
		boolean valid = GraphManager.getGraphValidatorInstance(ConnectionValidator.class.getName()).isValid(queryGraph, entities);

		// Map<IModelField, Set<IQueryField>> modelFieldsMap = filteredQuery.getQueryFields(dataSource);
		// Set<IModelField> modelFields = modelFieldsMap.keySet();
		// Set<IModelEntity> modelEntities = Query.getQueryEntities(modelFields);
		// QueryGraph queryGraph = filteredQuery.getQueryGraph();
		// boolean valid = GraphManager.getGraphValidatorInstance(QbeEngineConfig.getInstance().getGraphValidatorImpl()).isValid(queryGraph, modelEntities);
		logger.debug("Filter on field [" + filter.getField().getUniqueName() + "] is " + (valid ? "APPLICABLE" : "NOT APPLICABLE"));
		return valid;
	}

	private Query applyFilter(Filter filter, Query query, IDataSource dataSource) {
		Query toReturn = null;
		try {
			Map<String, List<String>> map = new HashMap<String, List<String>>();
			map.put(filter.getField().getUniqueName(), filter.getValues());
			toReturn = QueryTransformer.transform(query, dataSource, null, map);
		} catch (Exception e) {
			throw new SpagoBIEngineRuntimeException("Error while getting filtered query", e);
		}
		return toReturn;
	}

	private Map<String, List<String>> getFiltersMap(List<Filter> appliableFilters, Map userProfileAttributes) {
		Map<String, List<String>> toReturn = new HashMap<String, List<String>>();
		Iterator<Filter> it = appliableFilters.iterator();
		while (it.hasNext()) {
			Filter filter = it.next();
			// at this stage, the Filter contains just a reference to the user profile attribute, we now get actual values
			List<String> values = null;
			try {
				String profileAttributeName = filter.getValues().get(0);
				if (userProfileAttributes == null || userProfileAttributes.isEmpty() || !userProfileAttributes.containsKey(profileAttributeName)) {
					throw new SpagoBIRuntimeException("User profile attribute " + profileAttributeName + " not found!!");
				}
				String profileAttributeValue = (String) userProfileAttributes.get(profileAttributeName);
				logger.debug("Evaluating user profile attribute: [" + profileAttributeValue + "]");
				String[] valuesArray = SpagoBIUtilities.decodeProfileAttribute(profileAttributeValue);
				logger.debug("User profile attribute retrieved: [" + valuesArray + "]");
				values = Arrays.asList(valuesArray);
			} catch (Exception e) {
				throw new RuntimeException("Error while evaluating user profile attributes", e);
			}
			toReturn.put(filter.getField().getUniqueName(), values);
		}
		return toReturn;
	}
}
