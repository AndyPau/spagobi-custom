/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2015 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.tools.dataset.listener;

import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.utilities.Helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class DataSetListenerManager {

	private static Logger log = Logger.getLogger(DataSetListenerManager.class);

	/**
	 * Operator by DataSet Label by User Unique Identifiers
	 */
	private final Map<String, Map<String, DataStoreListenerOperator>> operatorsyByLabelByUUId = new HashMap<String, Map<String, DataStoreListenerOperator>>();

	public synchronized void addIDataSetListenerIfAbsent(String uuid, String dataSetLabel, IDataSetListener listener,String listenerId) {
		Helper.checkNotNullNotTrimNotEmpty(uuid, "uuid");
		Helper.checkNotNullNotTrimNotEmpty(dataSetLabel, "dataSetLabel");
		Helper.checkNotNull(listener, "listener");
		Helper.checkNotNullNotTrimNotEmpty(listenerId, "listenerId");

		DataStoreListenerOperator op = getOperator(uuid, dataSetLabel);
		op.addIDataSetListenerIfAbsent(listener,listenerId);
	}

	public boolean removeIDataSetListener(String uuid, String dataSetLabel, IDataSetListener listener,String listenerId) {
		Helper.checkNotNullNotTrimNotEmpty(uuid, "uuid");
		Helper.checkNotNullNotTrimNotEmpty(dataSetLabel, "dataSetLabel");
		Helper.checkNotNull(listener, "listener");
		Helper.checkNotNullNotTrimNotEmpty(listenerId, "listenerId");

		DataStoreListenerOperator op = getOperator(uuid, dataSetLabel);
		return op.removeIDataSetListener(listenerId);
	}

	public synchronized boolean removeUser(String uuid) {
		Helper.checkNotNullNotTrimNotEmpty(uuid, "uuid");
		
		return operatorsyByLabelByUUId.remove(uuid) != null;
	}

	public synchronized boolean removeDataSetLabel(String uuid, String dataSetLabel) {
		Helper.checkNotNullNotTrimNotEmpty(uuid, "uuid");
		Helper.checkNotNullNotTrimNotEmpty(dataSetLabel, "dataSetLabel");
		
		Map<String, DataStoreListenerOperator> ops = operatorsyByLabelByUUId.get(uuid);
		if (ops == null) {
			return false;
		}

		return ops.remove(dataSetLabel) != null;
	}

	public void changedDataSet(String uuid, String dataSetLabel, IDataSet currDataSet) {
		Helper.checkNotNullNotTrimNotEmpty(uuid, "uuid");
		Helper.checkNotNullNotTrimNotEmpty(dataSetLabel, "dataSetLabel");
		Helper.checkNotNull(currDataSet, "currDataSet");
		
		DataStoreListenerOperator op = getOperator(uuid, dataSetLabel);
		List<ListenerResult> res = op.changedDataSet(currDataSet);
		manageListenerResult(res);
	}

	private synchronized DataStoreListenerOperator getOperator(String uuid, String dataSetLabel) {
		Map<String, DataStoreListenerOperator> operatorsByLabel = operatorsyByLabelByUUId.get(uuid);
		if (operatorsByLabel == null) {
			operatorsByLabel = new HashMap<String, DataStoreListenerOperator>();
			operatorsyByLabelByUUId.put(uuid, operatorsByLabel);
		}

		DataStoreListenerOperator op = operatorsByLabel.get(dataSetLabel);
		if (op == null) {
			op = new DataStoreListenerOperator();
			operatorsByLabel.put(dataSetLabel, op);
		}
		return op;
	}

	public void changedDataSet(String uuid, String dataSetLabel, List<IRecord> updated) {
		Helper.checkNotNullNotTrimNotEmpty(uuid, "uuid");
		Helper.checkNotNullNotTrimNotEmpty(dataSetLabel, "dataSetLabel");
		Helper.checkNotNull(updated, "updated");
		
		DataStoreListenerOperator op = getOperator(uuid, dataSetLabel);

		List<ListenerResult> res = op.changedDataSet(updated);
		manageListenerResult(res);
		
	}

	private static void manageListenerResult(List<ListenerResult> res) {
		for (ListenerResult lr : res) {
			if (lr.isException()) {
				log.error("Error while calling dataset listener", lr.getException());
			}
		}
	}

	public void changedDataSet(String uuid, String dataSetLabel, List<IRecord> updatedOrAdded, int idFieldIndex) {
		Helper.checkNotNullNotTrimNotEmpty(uuid, "uuid");
		Helper.checkNotNullNotTrimNotEmpty(dataSetLabel, "dataSetLabel");
		Helper.checkNotNull(updatedOrAdded, "updatedOrAdded");
		Helper.checkNotNegative(idFieldIndex, "idFieldIndex");
		
		DataStoreListenerOperator op = getOperator(uuid, dataSetLabel);
		
		List<ListenerResult> res = op.changedDataSetUpdatedOrAdded(updatedOrAdded,idFieldIndex);
		manageListenerResult(res);
	}

	/**
	 * If it's not initialized then it does nothing.
	 * 
	 * @param uuid
	 * @param dataSetLabel
	 * @param listenerId
	 */
	public void addCometListenerIfInitializedAndAbsent(final String uuid, final String dataSetLabel, final String listenerId) {
		if (!CometDInitializerChecker.isCometdInitialized()) {
			return;
		}
		
		IDataSetListener listener= new IDataSetListener() {
			
			public void dataStoreChanged(DataStoreChangedEvent event) throws DataSetListenerException {
				log.debug("uuid = [" + uuid + "], listenerId = [" + listenerId + "]");
				//notify the frontend clients about the dataStore changes
				CometServiceManager manager = CometServiceManagerFactory.getManager();
				manager.dataStoreChanged(uuid, dataSetLabel, event,listenerId);
			}
			
		};
		
		DataStoreListenerOperator op = getOperator(uuid, dataSetLabel);
		op.addIDataSetListenerIfAbsent(listener,listenerId);
	}
	
}