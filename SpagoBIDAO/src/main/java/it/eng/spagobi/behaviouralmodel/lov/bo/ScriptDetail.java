/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.behaviouralmodel.lov.bo;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.ObjParuse;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.utilities.GeneralUtilities;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.tools.dataset.bo.ScriptDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStore;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;
import it.eng.spagobi.utilities.objects.Couple;
import it.eng.spagobi.utilities.scripting.SpagoBIScriptManager;

/**
 * Defines the <code>ScriptDetail</code> objects. This object is used to store Script Wizard detail information.
 */
public class ScriptDetail extends DependenciesPostProcessingLov implements ILovDetail {

	static private Logger logger = Logger.getLogger(ScriptDetail.class);

	public static final String SBI_BINDINGS_LANGUAGE = "SBI_LANGUAGE";
	public static final String SBI_BINDINGS_COUNTRY = "SBI_COUNTRY";
	public static final String SBI_BINDINGS_DATE_FORMAT = "SBI_DATE_FORMAT";

	/**
	 * the script
	 */
	private String script = "";
	private String languageScript = "";
	private List visibleColumnNames = null;
	private String valueColumnName = "";
	private String descriptionColumnName = "";
	private List invisibleColumnNames = null;

	// each entry of the list contains the name of the column to be considered as value column as first item, and the name of the column to be considered as
	// description column as second item
	private List<Couple<String, String>> treeLevelsColumns = null;

	private String lovType = "simple";

	/**
	 * constructor.
	 */
	public ScriptDetail() {
	}

	/**
	 * constructor.
	 *
	 * @param dataDefinition
	 *            xml representation of the script lov
	 *
	 * @throws SourceBeanException
	 *             the source bean exception
	 */
	public ScriptDetail(String dataDefinition) throws SourceBeanException {
		loadFromXML(dataDefinition);
	}

	/**
	 * loads the lov from an xml string.
	 *
	 * @param dataDefinition
	 *            the xml definition of the lov
	 *
	 * @throws SourceBeanException
	 *             the source bean exception
	 */
	@Override
	public void loadFromXML(String dataDefinition) throws SourceBeanException {
		dataDefinition.trim();
		// build the sourcebean
		if (dataDefinition.indexOf("<SCRIPT>") != -1) {
			int startInd = dataDefinition.indexOf("<SCRIPT>");
			int endId = dataDefinition.indexOf("</SCRIPT>");
			String script = dataDefinition.substring(startInd + 8, endId);
			script = script.trim();
			if (!script.startsWith("<![CDATA[")) {
				script = "<![CDATA[" + script + "]]>";
				dataDefinition = dataDefinition.substring(0, startInd + 8) + script + dataDefinition.substring(endId);
			}
		}
		SourceBean source = SourceBean.fromXMLString(dataDefinition);
		// get and set the script text
		SourceBean scriptSB = (SourceBean) source.getAttribute("SCRIPT");
		String script = scriptSB.getCharacters();
		setScript(script);
		// get and set value column
		String valueColumn = "";
		SourceBean valCol = (SourceBean) source.getAttribute("VALUE-COLUMN");
		if (valCol != null)
			valueColumn = valCol.getCharacters();
		setValueColumnName(valueColumn);
		// get and set the description column
		String descrColumn = "";
		SourceBean descColSB = (SourceBean) source.getAttribute("DESCRIPTION-COLUMN");
		if (descColSB != null)
			descrColumn = descColSB.getCharacters();
		setDescriptionColumnName(descrColumn);
		// get and set list of visible columns
		List visColNames = new ArrayList();
		SourceBean visColSB = (SourceBean) source.getAttribute("VISIBLE-COLUMNS");
		if (visColSB != null) {
			String visColConc = visColSB.getCharacters();
			if ((visColConc != null) && !visColConc.trim().equalsIgnoreCase("")) {
				String[] visColArr = visColConc.split(",");
				visColNames = Arrays.asList(visColArr);
			}
		}
		setVisibleColumnNames(visColNames);
		// get and set list of invisible columns
		List invisColNames = new ArrayList();
		SourceBean invisColSB = (SourceBean) source.getAttribute("INVISIBLE-COLUMNS");
		if (invisColSB != null) {
			String invisColConc = invisColSB.getCharacters();
			if ((invisColConc != null) && !invisColConc.trim().equalsIgnoreCase("")) {
				String[] invisColArr = invisColConc.split(",");
				invisColNames = Arrays.asList(invisColArr);
			}
		}
		setInvisibleColumnNames(invisColNames);

		SourceBean language = (SourceBean) source.getAttribute("LANGUAGE");
		if (language != null) {
			String lang = language.getCharacters();
			if (lang != null)
				setLanguageScript(lang);
		}

		try {
			SourceBean treeLevelsColumnsBean = (SourceBean) source.getAttribute("TREE-LEVELS-COLUMNS");
			if (treeLevelsColumnsBean != null && treeLevelsColumnsBean.getCharacters() != null && treeLevelsColumnsBean.getCharacters().trim() != "") {
				// compatibility control (versions till 5.1.0 does not have
				// VALUE-COLUMNS and DESCRIPTION-COLUMNS definition)
				String treeLevelsColumnsString = treeLevelsColumnsBean.getCharacters();
				String[] treeLevelsColumnArr = treeLevelsColumnsString.split(",");
				List<Couple<String, String>> levelsMap = new ArrayList<Couple<String, String>>();
				for (int i = 0; i < treeLevelsColumnArr.length; i++) {
					String aValueColumn = treeLevelsColumnArr[i];
					if (i == treeLevelsColumnArr.length - 1) {
						levelsMap.add(new Couple<String, String>(aValueColumn, descrColumn));
					} else {
						levelsMap.add(new Couple<String, String>(aValueColumn, aValueColumn));
					}
				}
				this.treeLevelsColumns = levelsMap;
				this.setValueColumnName(null);
				this.setDescriptionColumnName(null);
			} else {
				SourceBean valuesColumnsBean = (SourceBean) source.getAttribute("VALUE-COLUMNS");
				SourceBean descriptionColumnsBean = (SourceBean) source.getAttribute("DESCRIPTION-COLUMNS");
				if (valuesColumnsBean != null) {

					Assert.assertTrue(descriptionColumnsBean != null, "DESCRIPTION-COLUMNS tag not defined");

					List<Couple<String, String>> levelsMap = new ArrayList<Couple<String, String>>();
					String valuesColumnsStr = valuesColumnsBean.getCharacters();
					logger.debug("VALUE-COLUMNS is [" + valuesColumnsStr + "]");
					String descriptionColumnsStr = descriptionColumnsBean.getCharacters();
					logger.debug("DESCRIPTION-COLUMNS is [" + descriptionColumnsStr + "]");
					String[] valuesColumns = valuesColumnsStr.split(",");
					String[] descriptionColumns = descriptionColumnsStr.split(",");
					List<String> valuesColumnsList = Arrays.asList(valuesColumns);
					List<String> descriptionColumnsList = Arrays.asList(descriptionColumns);

					Assert.assertTrue(valuesColumnsList.size() == descriptionColumnsList.size(),
							"Value columns list and description columns list must have the same length");

					for (int i = 0; i < valuesColumnsList.size(); i++) {
						String aValueColumn = valuesColumnsList.get(i);
						String aDescriptionColumn = descriptionColumnsList.get(i);
						levelsMap.add(new Couple<String, String>(aValueColumn, aDescriptionColumn));
					}
					this.treeLevelsColumns = levelsMap;
				}
			}
		} catch (Exception e) {
			logger.error("Error while reading LOV definition from XML", e);
			throw new SpagoBIRuntimeException("Error while reading LOV definition from XML", e);
		}

		SourceBean lovTypeBean = (SourceBean) source.getAttribute("LOVTYPE");
		String lovType;
		if (lovTypeBean != null) {
			lovType = lovTypeBean.getCharacters();
			this.lovType = lovType;
		}
	}

	/**
	 * serialize the lov to an xml string.
	 *
	 * @return the serialized xml string
	 */
	@Override
	public String toXML() {
		String XML = "<SCRIPTLOV>" + "<SCRIPT>" + this.getScript() + "</SCRIPT>" + "<VISIBLE-COLUMNS>"
				+ GeneralUtilities.fromListToString(this.getVisibleColumnNames(), ",") + "</VISIBLE-COLUMNS>" + "<INVISIBLE-COLUMNS>"
				+ GeneralUtilities.fromListToString(this.getInvisibleColumnNames(), ",") + "</INVISIBLE-COLUMNS>" + "<LANGUAGE>" + this.getLanguageScript()
				+ "</LANGUAGE>" + "<LOVTYPE>" + this.getLovType() + "</LOVTYPE>";
		if (this.isSimpleLovType()) {
			XML += "<VALUE-COLUMN>" + valueColumnName + "</VALUE-COLUMN>" + "<DESCRIPTION-COLUMN>" + descriptionColumnName + "</DESCRIPTION-COLUMN>";
		} else {
			XML += "<VALUE-COLUMNS>" + GeneralUtilities.fromListToString(this.getTreeValueColumns(), ",") + "</VALUE-COLUMNS>" + "<DESCRIPTION-COLUMNS>"
					+ GeneralUtilities.fromListToString(this.getTreeDescriptionColumns(), ",") + "</DESCRIPTION-COLUMNS>";
		}
		XML += "</SCRIPTLOV>";
		return XML;
	}

	/**
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#getLovResult(IEngUserProfile profile, List<ObjParuse> dependencies, ExecutionInstance
	 *      executionInstance) throws Exception;
	 */
	@Override
	public String getLovResult(IEngUserProfile profile, List<ObjParuse> dependencies, List<BIObjectParameter> BIObjectParameters, Locale locale)
			throws Exception {
		logger.debug("IN");
		String result = null;
		HashMap attributes = GeneralUtilities.getAllProfileAttributes(profile); // to be cancelled, now substitutution inline
		attributes.putAll(this.getSystemBindings(locale));
		// Substitute profile attributes with their value
		String cleanScript = substituteProfileAttributes(getScript(), attributes);
		setScript(cleanScript);

		List<Object> imports = null;
		String languageScript = this.languageScript;
		if ("groovy".equals(languageScript)) {
			imports = getGroovyImports();
		} else if ("ECMAScript".equals(languageScript) || "rhino-nonjdk".equals(languageScript)) {
			imports = new ArrayList<Object>();
			URL url = Thread.currentThread().getContextClassLoader().getResource("predefinedJavascriptScript.js");
			try {
				logger.debug("predefinedJavascriptScript.js file URL is equal to [" + url + "]");
				imports.add(url);
			} catch (Throwable t) {
				logger.warn("Impossible to load predefinedJavascriptScript.js", t);
			}
		} else if (languageScript == null || languageScript.trim().isEmpty()) {
			logger.warn("Language script is not defined, Groovy will be used");
			languageScript = "groovy";
			imports = getGroovyImports();
		} else {
			logger.warn("There is no predefined script file to import for scripting language [" + languageScript + "]");
		}

		SpagoBIScriptManager scriptManager = new SpagoBIScriptManager();
		result = (String) scriptManager.runScript(getScript(), languageScript, attributes, imports);

		// check if the result must be converted into the right xml sintax
		boolean toconvert = checkSintax(result);
		if (toconvert) {
			result = convertResult(result);
		}
		logger.debug("OUT");
		return result;
	}

	private List<Object> getGroovyImports() {
		List<Object> imports = new ArrayList<Object>();
		URL url = Thread.currentThread().getContextClassLoader().getResource(SpagoBIConstants.PREDEFINED_GROOVY_SCRIPT_FILE_NAME);
		try {
			logger.debug("predefinedGroovyScript.groovy file URL is equal to [" + url + "]");
			imports.add(url);
		} catch (Throwable t) {
			logger.warn("Impossible to load predefinedGroovyScript.groovy", t);
		}
		return imports;
	}

	public DataStore getLovResultAsDataStore(IEngUserProfile profile, List<ObjParuse> dependencies, List<BIObjectParameter> BIObjectParameters, Locale locale)
			throws Exception {
		logger.debug("IN");

		DataStore dataStoreToReturn = null;

		HashMap attributes = GeneralUtilities.getAllProfileAttributes(profile); // to be cancelled, now substitutution inline
		attributes.putAll(this.getSystemBindings(locale));

		ScriptDataSet dataset = new ScriptDataSet();
		dataset.setScript(script);
		dataset.setScriptLanguage(languageScript);
		dataset.setUserProfileAttributes(attributes);
		dataset.loadData();
		dataStoreToReturn = (DataStore) dataset.getDataStore();

		logger.debug("OUT");

		return dataStoreToReturn;
	}

	private Map getSystemBindings(Locale locale) {

		if (locale == null) {
			locale = GeneralUtilities.getDefaultLocale();
			logger.debug("Execution instance's locale is null; considering default one: " + locale);
		}
		String dateFormat = GeneralUtilities.getLocaleDateFormat(locale);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(SBI_BINDINGS_LANGUAGE, locale.getLanguage());
		map.put(SBI_BINDINGS_COUNTRY, locale.getCountry());
		map.put(SBI_BINDINGS_DATE_FORMAT, dateFormat);
		return map;
	}

	private String substituteProfileAttributes(String script, HashMap attributes) throws EMFInternalError {
		logger.debug("IN");
		String cleanScript = new String(script);
		int indexSubstitution = 0;
		int profileAttributeStartIndex = script.indexOf("${", indexSubstitution);

		while (profileAttributeStartIndex != -1) {
			int profileAttributeEndIndex = script.indexOf("}", profileAttributeStartIndex);
			String attributeName = script.substring(profileAttributeStartIndex + 2, profileAttributeEndIndex).trim();
			Object attributeValueObj = attributes.get(attributeName);
			if (attributeValueObj == null) {
				logger.error("Profile attribute " + attributeName + " not found");
				attributeValueObj = "undefined";
			}
			cleanScript = cleanScript.replaceAll("\\$\\{" + attributeName + "\\}", attributeValueObj.toString());
			indexSubstitution = profileAttributeEndIndex;
			profileAttributeStartIndex = script.indexOf("${", indexSubstitution);
		}
		logger.debug("OUT");
		return cleanScript;
	}

	/**
	 * checks if the result is formatted in the right xml structure
	 *
	 * @param result
	 *            the result of the lov
	 * @return true if the result is formatted correctly false otherwise
	 */
	public boolean checkSintax(String result) {
		boolean toconvert = false;
		try {
			SourceBean source = SourceBean.fromXMLString(result);
			if (!source.getName().equalsIgnoreCase("ROWS")) {
				toconvert = true;
			} else {
				List rowsList = source.getAttributeAsList(DataRow.ROW_TAG);
				if ((rowsList == null) || (rowsList.size() == 0)) {
					toconvert = true;
				}
			}

		} catch (Exception e) {
			SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), "checkSintax",
					"the result of the lov is not formatted " + "with the right structure so it will be wrapped inside an xml envelope");
			toconvert = true;
		}
		return toconvert;
	}

	/**
	 * Gets the list of names of the profile attributes required.
	 *
	 * @return list of profile attribute names
	 *
	 * @throws Exception
	 *             the exception
	 */
	// public List getProfileAttributeNames() throws Exception {
	// List names = new ArrayList();
	// String script = getScript();
	// while(script.indexOf("getSingleValueProfileAttribute(")!=-1) {
	// int startind = script.indexOf("getSingleValueProfileAttribute(");
	// int endind = startind + 31;
	// int parind = script.indexOf(")", endind);
	// String name = script.substring(endind, parind);
	// script = script.substring(0, startind) + script.substring(parind+1);
	// names.add(name);
	// }
	// while(script.indexOf("getMultiValueProfileAttribute(")!=-1) {
	// int startind = script.indexOf("getMultiValueProfileAttribute(");
	// int endind = startind + 30;
	// int comaind = script.indexOf(",", endind);
	// String name = script.substring(endind, comaind);
	// script = script.substring(0, startind) + script.substring(comaind+1);
	// names.add(name);
	// }
	// return names;
	// }

	/**
	 * Gets the list of names of the profile attributes required.
	 *
	 * @return list of profile attribute names
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public List getProfileAttributeNames() throws Exception {
		List names = new ArrayList();
		String script = getScript();
		while (script.indexOf("${") != -1) {
			int startind = script.indexOf("${");
			int endind = script.indexOf("}", startind);
			String attributeDef = script.substring(startind + 2, endind);
			if (attributeDef.indexOf("(") != -1) {
				int indroundBrack = script.indexOf("(", startind);
				String nameAttr = script.substring(startind + 2, indroundBrack);
				names.add(nameAttr);
			} else {
				names.add(attributeDef);
			}
			script = script.substring(endind);
		}
		return names;
	}

	/**
	 * Checks if the lov requires one or more profile attributes.
	 *
	 * @return true if the lov require one or more profile attributes, false otherwise
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Override
	public boolean requireProfileAttributes() throws Exception {
		boolean contains = false;
		String script = getScript();
		if (script.indexOf("getSingleValueProfileAttribute(") != -1) {
			contains = true;
		}
		if (script.indexOf("getMultiValueProfileAttribute(") != -1) {
			contains = true;
		}
		return contains;
	}

	/**
	 * In case the result of the string is not structured as expected wrap the result into the right xml envelope
	 *
	 * @param result
	 *            the result of the script
	 * @return
	 */
	public String convertResult(String result) {
		StringBuffer sb = new StringBuffer();
		sb.append("<ROWS>");
		sb.append("<ROW VALUE=\"" + result + "\"/>");
		sb.append("</ROWS>");
		descriptionColumnName = "VALUE";
		valueColumnName = "VALUE";
		String[] visibleColumnNamesArray = new String[] { "VALUE" };
		visibleColumnNames = Arrays.asList(visibleColumnNamesArray);
		return sb.toString();
	}

	/**
	 * Get the string of the script.
	 *
	 * @return The string of the script
	 */
	public String getScript() {
		return script;
	}

	/**
	 * Set the string of the script.
	 *
	 * @param script
	 *            the string of the script
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * Splits an XML string by using some <code>SourceBean</code> object methods in order to obtain the source <code>ScriptDetail</code> objects whom XML has
	 * been built.
	 *
	 * @param dataDefinition
	 *            The XML input String
	 *
	 * @return The corrispondent <code>ScriptDetail</code> object
	 *
	 * @throws SourceBeanException
	 *             If a SourceBean Exception occurred
	 */
	public static ScriptDetail fromXML(String dataDefinition) throws SourceBeanException {
		ScriptDetail scriptDet = new ScriptDetail(dataDefinition);
		return scriptDet;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#getDescriptionColumnName()
	 */
	@Override
	public String getDescriptionColumnName() {
		return descriptionColumnName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#setDescriptionColumnName(java.lang.String)
	 */
	@Override
	public void setDescriptionColumnName(String descriptionColumnName) {
		this.descriptionColumnName = descriptionColumnName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#getInvisibleColumnNames()
	 */
	@Override
	public List getInvisibleColumnNames() {
		return invisibleColumnNames;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#setInvisibleColumnNames(java.util.List)
	 */
	@Override
	public void setInvisibleColumnNames(List invisibleColumnNames) {
		this.invisibleColumnNames = invisibleColumnNames;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#getValueColumnName()
	 */
	@Override
	public String getValueColumnName() {
		return valueColumnName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#setValueColumnName(java.lang.String)
	 */
	@Override
	public void setValueColumnName(String valueColumnName) {
		this.valueColumnName = valueColumnName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#getVisibleColumnNames()
	 */
	@Override
	public List getVisibleColumnNames() {
		return visibleColumnNames;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail#setVisibleColumnNames(java.util.List)
	 */
	@Override
	public void setVisibleColumnNames(List visibleColumnNames) {
		this.visibleColumnNames = visibleColumnNames;
	}

	public String getLanguageScript() {
		return languageScript;
	}

	public void setLanguageScript(String languageScript) {
		this.languageScript = languageScript;
	}

	@Override
	public String getLovType() {
		return lovType;
	}

	@Override
	public void setLovType(String lovType) {
		this.lovType = lovType;
	}

	@Override
	public List<Couple<String, String>> getTreeLevelsColumns() {
		return treeLevelsColumns;
	}

	@Override
	public void setTreeLevelsColumns(List<Couple<String, String>> treeLevelsColumns) {
		this.treeLevelsColumns = treeLevelsColumns;
	}

}