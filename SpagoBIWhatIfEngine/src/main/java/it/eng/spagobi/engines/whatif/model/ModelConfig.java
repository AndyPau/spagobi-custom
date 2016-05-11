/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 */
package it.eng.spagobi.engines.whatif.model;

import it.eng.spagobi.engines.whatif.crossnavigation.SpagoBICrossNavigationConfig;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;
import it.eng.spagobi.writeback4j.SbiAliases;
import it.eng.spagobi.writeback4j.SbiScenario;
import it.eng.spagobi.writeback4j.SbiScenarioVariable;
import it.eng.spagobi.writeback4j.WriteBackEditConfig;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eyeq.pivot4j.PivotModel;
import com.eyeq.pivot4j.transform.NonEmpty;
import com.eyeq.pivot4j.ui.command.DrillDownCommand;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class ModelConfig implements Serializable {

	private static final long serialVersionUID = 2687163910212567575L;
	private String drillType;
	private Boolean showParentMembers;
	private Boolean hideSpans;
	private Boolean showProperties;
	private Boolean suppressEmpty;
	private Integer actualVersion = null;
	private SbiScenario scenario = null;
	private SbiAliases aliases = null;

	private Integer artifactId;
	private String status;
	private String locker;

	private List<String> toolbarVisibleButtons;
	private List<String> toolbarMenuButtons;

	private Map<String, String> dimensionHierarchyMap;

	private SpagoBICrossNavigationConfig crossNavigation;

	public ModelConfig() {
	}

	public ModelConfig(PivotModel pivotModel) {
		drillType = DrillDownCommand.MODE_POSITION;
		showParentMembers = false;
		hideSpans = false;
		showProperties = false;

		NonEmpty transformNonEmpty = pivotModel.getTransform(NonEmpty.class);
		suppressEmpty = transformNonEmpty.isNonEmpty();

		dimensionHierarchyMap = new HashMap<String, String>();
	}

	public Boolean getSuppressEmpty() {
		return suppressEmpty;
	}

	public void setSuppressEmpty(Boolean suppressEmpty) {
		this.suppressEmpty = suppressEmpty;
	}

	public Boolean getShowProperties() {
		return showProperties;
	}

	public void setShowProperties(Boolean showProperties) {
		this.showProperties = showProperties;
	}

	public Boolean getHideSpans() {
		return hideSpans;
	}

	public void setHideSpans(Boolean hideSpans) {
		this.hideSpans = hideSpans;
	}

	public Boolean getShowParentMembers() {
		return showParentMembers;
	}

	public void setShowParentMembers(Boolean showParentMembers) {
		this.showParentMembers = showParentMembers;
	}

	public String getDrillType() {
		return drillType;
	}

	public void setDrillType(String drillType) {
		this.drillType = drillType;
	}

	public Map<String, String> getDimensionHierarchyMap() {
		return dimensionHierarchyMap;
	}

	public void setDimensionHierarchyMap(Map<String, String> dimensionHierarchyMap) {
		this.dimensionHierarchyMap = dimensionHierarchyMap;
	}

	public void setDimensionHierarchy(String dimensionUniqueName, String hierarchyUniqueName) {
		this.dimensionHierarchyMap.put(dimensionUniqueName, hierarchyUniqueName);
	}

	public Integer getActualVersion() {
		// if(actualVersion==null && scenario!=null &&
		// scenario.getWritebackEditConfig()!=null ){
		// return scenario.getWritebackEditConfig().getInitialVersion();
		// }
		return actualVersion;
	}

	public void setActualVersion(Integer actualVersion) {
		this.actualVersion = actualVersion;
	}

	public WriteBackEditConfig getWriteBackConf() {
		if (scenario == null) {
			return null;
		}
		return scenario.getWritebackEditConfig();
	}

	public void setWriteBackConf(WriteBackEditConfig writebackEditConfig) {
		if (scenario != null) {
			scenario.setWritebackEditConfig(writebackEditConfig);
		}
	}

	public void setScenario(SbiScenario scenario) {
		this.scenario = scenario;
	}

	public List<String> getToolbarVisibleButtons() {
		return toolbarVisibleButtons;
	}

	public void setToolbarVisibleButtons(List<String> toolbarVisibleButtons) {
		this.toolbarVisibleButtons = toolbarVisibleButtons;
	}

	public List<String> getToolbarMenuButtons() {
		return toolbarMenuButtons;
	}

	public void setToolbarMenuButtons(List<String> toolbarMenuButtons) {
		this.toolbarMenuButtons = toolbarMenuButtons;
	}

	@JsonIgnore
	public SbiScenario getScenario() {
		return scenario;
	}

	@JsonIgnore
	public Object getVariableValue(String variableName) {
		if (scenario == null) {
			return null;
		}
		SbiScenarioVariable var = scenario.getVariable(variableName);
		if (var != null) {
			String value = var.getValue();
			return var.getType().getTypedType(value);
		} else {
			// if isn't a variable it could be a generic alias
			String value = aliases.getGenericNameFromAlias(variableName);
			if (value != null) {
				return value;
			} else {
				throw new SpagoBIEngineRuntimeException("Cannot calculate Value, Variable or Alias not found: " + variableName);
			}
		}

	}

	public Integer getArtifactId() {
		return artifactId;
	}

	public void setArtifactID(Integer artifactId) {
		this.artifactId = artifactId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLocker() {
		return locker;
	}

	public void setLocker(String locker) {
		this.locker = locker;
	}

	/**
	 * @return the aliases
	 */
	@JsonIgnore
	public SbiAliases getAliases() {
		return aliases;
	}

	/**
	 * @param aliases
	 *            the aliases to set
	 */
	@JsonIgnore
	public void setAliases(SbiAliases aliases) {
		this.aliases = aliases;
	}

	public boolean isWhatIfScenario() {
		return this.scenario != null;
	}

	// for the deserializer
	public void setWhatIfScenario(boolean bool) {
	}

	public SpagoBICrossNavigationConfig getCrossNavigation() {
		return crossNavigation;
	}

	public void setCrossNavigation(SpagoBICrossNavigationConfig crossNavigation) {
		this.crossNavigation = crossNavigation;
	}

	/**
	 * Updates the values of the object coping the values of another configuration.. Not all the modification are copied, id est Scenario and aliases
	 */
	public void update(ModelConfig source) {
		this.drillType = source.drillType;
		this.showParentMembers = source.showParentMembers;
		this.hideSpans = source.hideSpans;
		this.showProperties = source.showProperties;
		this.suppressEmpty = source.suppressEmpty;
		this.actualVersion = source.actualVersion = null;

		this.status = source.status;
		this.locker = source.locker;

		this.toolbarVisibleButtons = source.toolbarVisibleButtons;
		this.toolbarMenuButtons = source.toolbarMenuButtons;

		this.dimensionHierarchyMap = source.dimensionHierarchyMap;
	}

}
