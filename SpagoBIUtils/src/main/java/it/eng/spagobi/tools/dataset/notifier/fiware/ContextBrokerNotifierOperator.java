/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2015 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.tools.dataset.notifier.fiware;

import it.eng.spagobi.tools.dataset.common.datareader.JSONPathDataReader;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.listener.DataSetListenerManager;
import it.eng.spagobi.tools.dataset.listener.DataSetListenerManagerFactory;
import it.eng.spagobi.tools.dataset.notifier.INotifierOperator;
import it.eng.spagobi.utilities.Helper;
import it.eng.spagobi.utilities.assertion.Assert;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ContextBrokerNotifierOperator implements INotifierOperator {
	
	private static final Logger log=Logger.getLogger(ContextBrokerNotifierOperator.class);

	private static final String SUBSCRIPTION_ID = "subscriptionId";
	private final String subscriptionId;
	private final String uuid;
	private final String dataSetLabel;
	private final DataSetListenerManager manager;
	private final JSONPathDataReader reader;

	public ContextBrokerNotifierOperator(String subscriptionId, String uuid, String dataSetLabel, DataSetListenerManager manager, JSONPathDataReader reader) {
		Helper.checkNotNullNotTrimNotEmpty(subscriptionId, "subscriptionId");
		Helper.checkNotNullNotTrimNotEmpty(uuid, "uuid");
		Helper.checkNotNullNotTrimNotEmpty(dataSetLabel, "dataSetLabel");
		Helper.checkNotNull(manager, "manager");
		Helper.checkNotNull(reader, "reader");

		this.subscriptionId = subscriptionId;
		this.uuid = uuid;
		this.dataSetLabel = dataSetLabel;
		this.manager = manager;
		this.reader = reader;

	}

	public ContextBrokerNotifierOperator(String subscriptionId, String uuid, String dataSetLabel, JSONPathDataReader reader) {
		this(subscriptionId, uuid, dataSetLabel, DataSetListenerManagerFactory.getManager(), reader);
	}

	private List<IRecord> getUpdatedOrAddedRecords(HttpServletRequest req, String body) {
		try {
			Helper.checkNotNull(req, "action");

			if (body.isEmpty()) {
				throw new ContextBrokerNotifierException("Request body is empty.");
			}

			JSONObject bodyJSON = getJSON(body);

			if (bodyJSON == null) {
				throw new ContextBrokerNotifierException("Request body is not a valid JSON: " + body);
			}

			if (!bodyJSON.has(SUBSCRIPTION_ID)) {
				throw new ContextBrokerNotifierException("Request body has not " + SUBSCRIPTION_ID + ": " + body);
			}

			String subId = bodyJSON.getString(SUBSCRIPTION_ID);
			if (!subscriptionId.equals(subId)) {
				log.info("Subscription id not equals: "+subId+"!="+subscriptionId+", notifier skipped.");
				return null;
			}

			// updated
			IDataStore store = reader.read(body);
			List<IRecord> records = getRecords(store);
			return records;
		} catch (ContextBrokerNotifierException e) {
			throw e;
		} catch (Exception e) {
			throw new ContextBrokerNotifierException("Error in context broker notification", e);
		}
	}

	private static JSONObject getJSON(String body) throws JSONException {
		try {
			return new JSONObject(body);
		} catch (Exception e) {
			return null;
		}
	}

	

	private static List<IRecord> getRecords(IDataStore store) {
		List<IRecord> res = new ArrayList<IRecord>((int) Math.min(Integer.MAX_VALUE, store.getRecordsCount()));
		for (int i = 0; i < store.getRecordsCount(); i++) {
			res.add(store.getRecordAt(i));
		}
		return res;
	}

	public void notify(HttpServletRequest req, HttpServletResponse resp,String reqBody) {
		log.debug("IN");
		// updated and added (there is no disctinction in the notification)
		List<IRecord> updatedOrAdded = getUpdatedOrAddedRecords(req,reqBody);
		if (updatedOrAdded==null) {
			//different subscription id
			return;
		}
		int idFieldIndex = reader.getIdFieldIndex();
		Assert.assertTrue(idFieldIndex != -1, "idFieldIndex!=-1");
		manager.changedDataSet(uuid, dataSetLabel, updatedOrAdded, idFieldIndex);
	}

}
