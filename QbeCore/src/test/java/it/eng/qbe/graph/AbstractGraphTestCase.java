/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.graph;

import it.eng.qbe.datasource.AbstractDataSourceTestCase;
import it.eng.qbe.jpa.datasource.jpa.JPADriver;
import it.eng.qbe.runtime.datasource.DriverManager;
import it.eng.qbe.runtime.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.runtime.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.runtime.model.structure.IModelEntity;
import it.eng.qbe.runtime.model.structure.IModelStructure;
import it.eng.qbe.runtime.statement.graph.bean.Relationship;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.graph.DirectedMultigraph;
import org.jgrapht.graph.Multigraph;

public class AbstractGraphTestCase extends AbstractDataSourceTestCase {

	IModelStructure modelStructure;
	private static final String QBE_FILE = "test-resources/jpa/jpaImpl/eclipselink/datamart.jar";
	Graph<IModelEntity, Relationship> graph;
	List<IModelEntity> entities;
	Map<IModelEntity, Set<GraphPath<IModelEntity, Relationship>>> mappaths;
	Collection<Relationship> relationShips;

	@Override
	protected void setUpDataSource() {
		IDataSourceConfiguration configuration;

		modelName = "foodmart";

		File file = new File(QBE_FILE);
		configuration = new FileDataSourceConfiguration(modelName, file);
		configuration.loadDataSourceProperties().put("datasource", connection);
		dataSource = DriverManager.getDataSource(JPADriver.DRIVER_ID, configuration);
		relationShips = new ArrayList<Relationship>();
		modelStructure = dataSource.getModelStructure();
		setUpGraph();
	}

	protected void setUpGraph() {
		setUpGraph1();
	}

	/**
	 * Create a graph with 4 entities and relation:
	 *
	 *
	 *
	 * 0 product class 1 promotion 2 customer 3 store
	 *
	 * 0-->1 1-->2 0-->2 2-->3
	 *
	 */
	protected void setUpGraph0() {
		graph = new DirectedMultigraph<IModelEntity, Relationship>(Relationship.class);

		Iterator<IModelEntity> meIter = modelStructure.getRootEntityIterator("foodmart");
		entities = new ArrayList<IModelEntity>();

		for (int i = 0; i < 4; i++) {
			entities.add(meIter.next());
			graph.addVertex(entities.get(i));
		}

		Relationship r1 = new Relationship();
		r1.setSourceFields(entities.get(0).getAllFields());
		r1.setTargetFields(entities.get(1).getAllFields());
		r1.setName("r1");

		Relationship r2 = new Relationship();
		r2.setSourceFields(entities.get(1).getAllFields());
		r2.setTargetFields(entities.get(2).getAllFields());
		r2.setName("r2");

		Relationship r3 = new Relationship();
		r3.setSourceFields(entities.get(0).getAllFields());
		r3.setTargetFields(entities.get(2).getAllFields());
		r3.setName("r3");

		Relationship r4 = new Relationship();
		r4.setSourceFields(entities.get(2).getAllFields());
		r4.setTargetFields(entities.get(3).getAllFields());
		r4.setName("r4");

		graph.addEdge(entities.get(0), entities.get(1), r1);
		graph.addEdge(entities.get(1), entities.get(2), r2);
		graph.addEdge(entities.get(0), entities.get(2), r3);
		graph.addEdge(entities.get(2), entities.get(3), r4);
		relationShips.add(r1);
		relationShips.add(r2);
		relationShips.add(r3);
		relationShips.add(r4);
		mappaths = new HashMap<IModelEntity, Set<GraphPath<IModelEntity, Relationship>>>();

		// pathts from entity 1
		for (int i = 0; i < 4; i++) {
			IModelEntity me = entities.get(i);
			Set<GraphPath<IModelEntity, Relationship>> graphPaths = new HashSet<GraphPath<IModelEntity, Relationship>>();

			KShortestPaths<IModelEntity, Relationship> kshortestPath = new KShortestPaths<IModelEntity, Relationship>(graph, me, 10000);
			for (int j = 0; j < 4; j++) {
				if (j != i) {
					List<GraphPath<IModelEntity, Relationship>> graphPathsTemp = kshortestPath.getPaths(entities.get(j));
					if (graphPathsTemp != null) {
						graphPaths.addAll(graphPathsTemp);// add paths from i to j
						if (mappaths.get(entities.get(j)) == null) {// add paths fro j to i
							mappaths.put(entities.get(j), new HashSet<GraphPath<IModelEntity, Relationship>>());
						}
						mappaths.get(entities.get(j)).addAll(graphPathsTemp);
					}
				}
			}
			if (mappaths.get(me) == null) {// add paths fro j to i
				mappaths.put(me, new HashSet<GraphPath<IModelEntity, Relationship>>());
			}
			mappaths.get(me).addAll(graphPaths);

		}

	}

	/**
	 * Create a graph with 4 entities and relation:
	 *
	 *
	 *
	 * 0 product class 1 promotion 2 customer 3 store
	 *
	 * 0-->1 1-->2 0-->2 2-->3
	 *
	 */
	protected void setUpGraph1() {
		graph = new Multigraph<IModelEntity, Relationship>(Relationship.class);

		Iterator<IModelEntity> meIter = modelStructure.getRootEntityIterator("foodmart");
		entities = new ArrayList<IModelEntity>();

		for (int i = 0; i < 4; i++) {
			entities.add(meIter.next());
			graph.addVertex(entities.get(i));
		}

		Relationship r1 = new Relationship();
		r1.setSourceFields(entities.get(0).getAllFields());
		r1.setTargetFields(entities.get(1).getAllFields());
		r1.setName("r1");

		Relationship r2 = new Relationship();
		r2.setSourceFields(entities.get(1).getAllFields());
		r2.setTargetFields(entities.get(2).getAllFields());
		r2.setName("r2");

		Relationship r3 = new Relationship();
		r3.setSourceFields(entities.get(0).getAllFields());
		r3.setTargetFields(entities.get(2).getAllFields());
		r3.setName("r3");

		Relationship r4 = new Relationship();
		r4.setSourceFields(entities.get(2).getAllFields());
		r4.setTargetFields(entities.get(3).getAllFields());
		r4.setName("r4");

		graph.addEdge(entities.get(0), entities.get(1), r1);
		graph.addEdge(entities.get(2), entities.get(1), r2);
		graph.addEdge(entities.get(2), entities.get(3), r3);
		graph.addEdge(entities.get(2), entities.get(0), r4);
		relationShips.add(r1);
		relationShips.add(r2);
		relationShips.add(r3);
		relationShips.add(r4);
		mappaths = new HashMap<IModelEntity, Set<GraphPath<IModelEntity, Relationship>>>();

		// pathts from entity 1
		for (int i = 0; i < 4; i++) {
			IModelEntity me = entities.get(i);
			Set<GraphPath<IModelEntity, Relationship>> graphPaths = new HashSet<GraphPath<IModelEntity, Relationship>>();

			KShortestPaths<IModelEntity, Relationship> kshortestPath = new KShortestPaths<IModelEntity, Relationship>(graph, me, 10000);
			for (int j = 0; j < 4; j++) {
				if (j != i) {
					List<GraphPath<IModelEntity, Relationship>> graphPathsTemp = kshortestPath.getPaths(entities.get(j));
					if (graphPathsTemp != null) {
						graphPaths.addAll(graphPathsTemp);// add paths from i to j
						if (mappaths.get(entities.get(j)) == null) {// add paths fro j to i
							mappaths.put(entities.get(j), new HashSet<GraphPath<IModelEntity, Relationship>>());
						}
						mappaths.get(entities.get(j)).addAll(graphPathsTemp);
					}
				}
			}
			if (mappaths.get(me) == null) {// add paths fro j to i
				mappaths.put(me, new HashSet<GraphPath<IModelEntity, Relationship>>());
			}
			mappaths.get(me).addAll(graphPaths);

		}

	}

}
