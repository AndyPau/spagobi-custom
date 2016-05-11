/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.spagobi.pivot4j.ui;

import it.eng.spagobi.engines.whatif.crossnavigation.CrossNavigationManager;
import it.eng.spagobi.engines.whatif.crossnavigation.SpagoBICrossNavigationConfig;
import it.eng.spagobi.engines.whatif.crossnavigation.TargetClickable;
import it.eng.spagobi.engines.whatif.model.ModelConfig;
import it.eng.spagobi.engines.whatif.model.SpagoBICellWrapper;
import it.eng.spagobi.engines.whatif.model.SpagoBIPivotModel;
import it.eng.spagobi.utilities.engines.SpagoBIEngineRuntimeException;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.olap4j.Axis;
import org.olap4j.OlapException;
import org.olap4j.metadata.Dimension;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;

import com.eyeq.pivot4j.PivotModel;
import com.eyeq.pivot4j.transform.PlaceMembersOnAxes;
import com.eyeq.pivot4j.ui.CellType;
import com.eyeq.pivot4j.ui.RenderContext;
import com.eyeq.pivot4j.ui.command.CellCommand;
import com.eyeq.pivot4j.ui.command.CellParameters;
import com.eyeq.pivot4j.ui.command.DrillDownCommand;
import com.eyeq.pivot4j.ui.html.HtmlRenderer;
import com.eyeq.pivot4j.ui.property.PropertySupport;
import com.eyeq.pivot4j.util.CssWriter;

public class WhatIfHTMLRenderer extends HtmlRenderer {

	private boolean measureOnRows;
	// cache that maps the row/column number and the name of the measure of that
	// row/column (row if measures stay in the rows, column otherwise)
	private Map<Integer, String> positionMeasureMap;
	private boolean initialized = false;
	private String evenColumnStyleClass = "";
	private String oddColumnStyleClass = "";
	// if the member stay in the columns this is the rowPosition of all the occurrence of the member.. Viceversa for the rows
	private Map<Member, Integer> memberPositions;
	private boolean showProperties = false;
	private String styleInLabel;

	public static transient Logger logger = Logger.getLogger(HtmlRenderer.class);

	@Override
	public void render(PivotModel model) {

		super.render(model);
		initialized = false;

	}

	public WhatIfHTMLRenderer(Writer writer, ModelConfig modelConfig) {
		super(writer);
		if (modelConfig.getShowProperties()) {
			memberPositions = new HashMap<Member, Integer>();
			showProperties = true;
		}
	}

	@Override
	public void startCell(RenderContext context, List<CellCommand<?>> commands) {
		boolean header;

		switch (context.getCellType()) {
		case Header:
		case Title:
		case None:
			header = true;
			break;
		default:
			header = false;
			break;
		}

		String name = header ? "th" : "td";

		getWriter().startElement(name, getCellAttributes(context));

		if (commands != null && !commands.isEmpty()) {

			startCommand(context, commands);
		}
	}

	@Override
	protected Map<String, String> getCellAttributes(RenderContext context) {
		String styleClass = null;

		StringWriter writer = new StringWriter();
		CssWriter cssWriter = new CssWriter(writer);

		switch (context.getCellType()) {
		case Header:
			if (context.getAxis() == Axis.COLUMNS) {
				styleClass = getColumnHeaderStyleClass();
			} else {
				styleClass = getRowHeaderStyleClass();

				// if its a property cell no span needed
				if (getRowHeaderLevelPadding() > 0 && !isProperyCell(context)) {

					int padding = getRowHeaderLevelPadding() * context.getMember().getDepth();

					cssWriter.writeStyle("padding-left", padding + "px");
				}
			}
			break;
		case Title:
		case Aggregation:

			if (context.getAxis() == Axis.COLUMNS) {
				styleClass = getColumnTitleStyleClass();
			} else if (context.getAxis() == Axis.ROWS) {
				styleClass = getRowTitleStyleClass();
			}
			break;
		case Value:
			styleClass = getCellStyleClass();

			// add the style to odd and even columns

			int index = context.getColumnIndex() - context.getRowHeaderCount();
			if (index < 0) {
				index = context.getColumnIndex();
			}

			boolean even = index % 2 == 0;

			if (even && evenColumnStyleClass != null) {
				styleClass = styleClass + " " + getEvenColumnStyleClass();
			}

			if (!even && oddColumnStyleClass != null) {
				styleClass = styleClass + " " + getOddColumnStyleClass();
			}

			break;
		case None:
			styleClass = getCornerStyleClass();

			break;
		default:
			assert false;
		}

		Map<String, String> attributes = new TreeMap<String, String>();

		PropertySupport properties = getProperties(context);

		if (properties != null) {
			cssWriter.writeStyle("color", getPropertyValue("fgColor", properties, context));

			String bgColor = getPropertyValue("bgColor", properties, context);
			if (bgColor != null) {
				cssWriter.writeStyle("background-color", bgColor);
				cssWriter.writeStyle("background-image", "none");
			}

			cssWriter.writeStyle("font-family", getPropertyValue("fontFamily", properties, context));
			cssWriter.writeStyle("font-size", getPropertyValue("fontSize", properties, context));

			String fontStyle = getPropertyValue("fontStyle", properties, context);
			if (fontStyle != null) {
				if (fontStyle.contains("bold")) {
					cssWriter.writeStyle("font-weight", "bold");
				}

				if (fontStyle.contains("italic")) {
					cssWriter.writeStyle("font-style", "oblique");
				}
			}

			String styleClassValue = getPropertyValue("styleClass", properties, context);

			if (styleClassValue != null) {
				if (styleClass == null) {
					styleClass = styleClassValue;
				} else {
					styleClass += " " + styleClassValue;
				}
			}
		}

		if (styleClass != null) {
			// adds the proper style (depending if it was collapsed or expanded)
			if (context.getMember() != null && context.getMember().getMemberType() != null
					&& !context.getMember().getMemberType().name().equalsIgnoreCase("Value")) {

				try {
					int childrenNum = context.getMember().getChildMemberCount();

					if (childrenNum > 0) {
						if (getEnableRowDrillDown() || getEnableColumnDrillDown()) {
							styleClass += " " + "collapsed";
						} else {
							styleClass += " " + "expanded";
						}
					}
				} catch (OlapException e) {
					logger.error(e);
				}
			} else if (context.getCellType() == CellType.Title) {
				styleClass = "dimension-title";
			}
			attributes.put("class", styleClass);
		}
		if (context.getCellType() == CellType.Value) {

			initializeInternal(context);

			// need the name of the measure to check if it's editable
			String measureName = getMeasureName(context);
			// attributes.put("contentEditable", "true");
			int colId = context.getColumnIndex();
			int rowId = context.getRowIndex();
			int positionId = context.getCell().getOrdinal();
			// String memberUniqueName = context.getMember().getUniqueName();
			String id = positionId + "!" + rowId + "!" + colId + "!" + System.currentTimeMillis() % 1000;
			attributes.put("ondblclick", "javascript:Sbi.olap.eventManager.makeEditable('" + id + "','" + measureName + "')");
			attributes.put("id", id);
		} else if (context.getCellType() == CellType.Header) {
			String uniqueName = context.getMember().getUniqueName();
			int axis = context.getAxis().axisOrdinal();
			attributes.put("ondblclick", "javascript:Sbi.olap.eventManager.setCalculatedFieldParent('" + uniqueName + "','" + axis + "')");

		}

		writer.flush();
		IOUtils.closeQuietly(writer);

		String style = writer.toString();

		if (StringUtils.isNotEmpty(style)) {
			attributes.put("style", style);
		}

		if (context.getColumnSpan() > 1) {
			attributes.put("colspan", Integer.toString(context.getColumnSpan()));
		}

		if (context.getRowSpan() > 1) {
			attributes.put("rowspan", Integer.toString(context.getRowSpan()));
		}

		return attributes;
	}

	private String getMeasureName(RenderContext context) {
		int coordinate;
		if (this.measureOnRows) {
			coordinate = context.getRowIndex();
		} else {
			coordinate = context.getColumnIndex();
		}
		String measureName = this.positionMeasureMap.get(coordinate);

		if (measureName == null) {
			measureName = ((SpagoBICellWrapper) context.getCell()).getMeasureName();
			this.positionMeasureMap.put(coordinate, measureName);
		}

		return measureName;

	}

	private void initializeInternal(RenderContext context) {
		if (!this.initialized) {
			this.measureOnRows = true;
			this.initialized = true;
			this.positionMeasureMap = new HashMap<Integer, String>();

			// check if the measures are in the rows or in the columns
			List<Member> columnMembers = context.getColumnPosition().getMembers();
			try {
				if (columnMembers != null) {
					for (int i = 0; i < columnMembers.size(); i++) {
						Member member = columnMembers.get(i);
						if (member.getDimension().getDimensionType().equals(Dimension.Type.MEASURE)) {
							this.measureOnRows = false;
						}
					}
				}
			} catch (OlapException e) {
				throw new SpagoBIEngineRuntimeException("Erro getting the measure of a rendered cell ", e);
			}
		}
	}

	@Override
	public void cellContent(RenderContext context, String label) {

		String link = null;

		PropertySupport properties = getProperties(context);

		if (properties != null) {
			link = getPropertyValue("link", properties, context);
		}

		if (link == null) {
			Map<String, String> attributes = new TreeMap<String, String>();
			String drillMode = this.getDrillDownMode();

			if (!isEmptyNonProperyCell(context)) {

				if (context.getMember() != null && context.getMember().getMemberType() != null
						&& !context.getMember().getMemberType().name().equalsIgnoreCase("Measure")) {

					List<CellCommand<?>> commands = getCommands(context);

					if (commands != null && !commands.isEmpty()) {
						for (CellCommand<?> command : commands) {
							String cmd = command.getName();

							int colIdx = context.getColumnIndex();
							int rowIdx = context.getRowIndex();

							int axis = 0;
							if (context.getAxis() != null) {
								axis = context.getAxis().axisOrdinal();
							}
							int memb = 0;
							if (context.getPosition() != null) {
								memb = context.getPosition().getOrdinal();
							}
							int pos = 0;
							if (context.getAxis() == Axis.COLUMNS) {
								pos = rowIdx;
							} else {
								pos = colIdx;
							}

							String uniqueName = context.getMember().getUniqueName();
							String positionUniqueName = context.getPosition().getMembers().toString();

							if (cmd != null) {
								CellParameters parameters = command.createParameters(context);

								if ((cmd.equalsIgnoreCase("collapsePosition") || cmd.equalsIgnoreCase("drillUp") || cmd.equalsIgnoreCase("collapseMember"))
										&& (!drillMode.equals(DrillDownCommand.MODE_REPLACE))) {
									attributes.put("src", "../img/minus.gif");
									attributes.put("onClick", "javascript:Sbi.olap.eventManager.drillUp(" + axis + " , " + pos + " , " + memb + ",'"
											+ uniqueName + "','" + positionUniqueName + " ')");
									getWriter().startElement("img", attributes);
									getWriter().endElement("img");
								} else if ((cmd.equalsIgnoreCase("expandPosition") || cmd.equalsIgnoreCase("drillDown") || cmd.equalsIgnoreCase("expandMember"))) {
									attributes.put("src", "../img/plus.gif");
									attributes.put("onClick", "javascript:Sbi.olap.eventManager.drillDown(" + axis + " , " + pos + " , " + memb + ",'"
											+ uniqueName + "','" + positionUniqueName + "' )");
									getWriter().startElement("img", attributes);
									getWriter().endElement("img");
								}
							}

						}
					} else {
						if (context.getAxis() == Axis.ROWS && !isProperyCell(context)) {

							attributes.put("src", "../img/nodrill.png");
							attributes.put("style", "padding : 2px");
							getWriter().startElement("img", attributes);
							getWriter().endElement("img");
						}

					}
				}
			}

			if ((context.getCellType() == CellType.Title) && !label.equalsIgnoreCase("Measures")) {

				int colIdx = context.getColumnIndex();
				int rowIdx = context.getRowIndex();

				int axis = 0;
				if (context.getAxis() != null) {
					axis = context.getAxis().axisOrdinal();
				}
				int memb = 0;
				if (context.getPosition() != null) {
					memb = context.getPosition().getOrdinal();
				}
				int pos = 0;
				if (context.getAxis() == Axis.COLUMNS) {
					pos = rowIdx;
				} else {
					pos = colIdx;
				}

				if (drillMode.equals(DrillDownCommand.MODE_REPLACE) && !this.getShowParentMembers()) {
					Hierarchy h = context.getHierarchy();
					PlaceMembersOnAxes pm = context.getModel().getTransform(PlaceMembersOnAxes.class);

					List<Member> visibleMembers = pm.findVisibleMembers(h);
					int d = 0;
					for (Member m : visibleMembers) {
						Level l = m.getLevel();
						d = l.getDepth();
						if (d != 0) {
							break;
						}
					}

					// For drill replace the context.getPosition() and context.getMember are empty.
					String uniqueName = "x";
					String positionUniqueName = "x";

					if (context != null) {
						if (context.getPosition() != null && context.getPosition() != null) {
							positionUniqueName = context.getPosition().getMembers().toString();
						}
						if (context.getMember() != null) {
							uniqueName = context.getMember().getUniqueName();
						}

					}

					if (d != 0) {
						attributes.put("src", "../img/arrow-up.png");
						attributes.put("onClick", "javascript:Sbi.olap.eventManager.drillUp(" + axis + " , " + pos + " , " + memb + ",'" + uniqueName + "','"
								+ positionUniqueName + "' )");
						getWriter().startElement("img", attributes);
						getWriter().endElement("img");
					}
					getWriter().writeContent(label);

				} else if (!drillMode.equals(DrillDownCommand.MODE_REPLACE)) {
					getWriter().writeContent(label);
				}
			} else {

				// start OSMOSIT cross nav button
				SpagoBIPivotModel sbiModel = (SpagoBIPivotModel) context.getModel();
				SpagoBICrossNavigationConfig crossNavigation = sbiModel.getCrossNavigation();
				if (crossNavigation != null && crossNavigation.isButtonClicked()
						&& !crossNavigation.getModelStatus().equalsIgnoreCase(new String("locked_by_other"))
						&& !crossNavigation.getModelStatus().equalsIgnoreCase(new String("locked_by_user")) && context.getCellType() == CellType.Value) {

					int colId = context.getColumnIndex();
					int rowId = context.getRowIndex();
					int positionId = context.getCell().getOrdinal();
					String id = positionId + "!" + rowId + "!" + colId + "!" + System.currentTimeMillis() % 1000;
					attributes.put("src", "../img/cross-navigation.gif");
					attributes.put("onload", "javascript:Sbi.olap.eventManager.createCrossNavigationMenu('" + id + "')");
					attributes.put("id", id);
					getWriter().startElement("img", attributes);
					getWriter().endElement("img");

					setColorInCell(label, attributes);
				} else {
					// TODO: OSMOSIT create member clickable
					List<TargetClickable> targetsClickable = sbiModel.getTargetsClickable();
					if (targetsClickable != null && targetsClickable.size() > 0) {
						Member member = context.getMember();
						if (member != null) {
							String url = CrossNavigationManager.buildClickableUrl(member, targetsClickable);
							if (url != null) {
								attributes.remove("onClick");
								attributes.remove("src");
								attributes.put("href", url);
								getWriter().startElement("a", attributes);
								getWriter().writeContent(label);
								getWriter().endElement("a");
							} else {
								getWriter().writeContent(label);
							}
						} else {

							setColorInCell(label, attributes);
						}
					} else {

						setColorInCell(label, attributes);
					}
					// fine OSMOSIT create member clickable

					// getWriter().writeContent(label);//commentato da osmosit create member clickable
				}
				// fine OSMOSIT cross nav button
				// getWriter().writeContent(label); //commentato da osmosit cross nav button
			}
		} else {
			Map<String, String> attributes = new HashMap<String, String>(1);
			attributes.put("href", link);

			getWriter().startElement("a", attributes);
			getWriter().writeContent(label);
			getWriter().endElement("a");
		}

	}

	private boolean isProperyCell(RenderContext context) {
		if (showProperties && this.getPropertyCollector() != null && context.getLevel() != null && isEmptyNonProperyCell(context)) {
			List<org.olap4j.metadata.Property> propertieds = this.getPropertyCollector().getProperties(context.getLevel());
			return (propertieds != null && propertieds.size() > 0);// check if contains properties..

		}
		return false;
	}

	private boolean isEmptyNonProperyCell(RenderContext context) {

		Member member = context.getMember();

		// if the member stays in the rows, if there is more than one occurrence in the same row, than its a property cell
		if (member != null && showProperties) {

			Integer memberPosition = null;

			if (context.getAxis().axisOrdinal() == (Axis.ROWS.axisOrdinal())) {
				memberPosition = context.getColumnIndex();
			} else {
				memberPosition = context.getRowIndex();
			}

			if (!memberPositions.containsKey(member)) {
				memberPositions.put(member, memberPosition);
			}

			Integer previousPositions = memberPositions.get(member);

			if (previousPositions == memberPosition) {
				return false;
			}
			return true;
		}

		return false;
	}

	@Override
	protected String getPropertyValue(String key, PropertySupport properties, RenderContext context) {
		return super.getPropertyValue(key, properties, context);
	}

	public String getEvenColumnStyleClass() {
		return evenColumnStyleClass;
	}

	public void setEvenColumnStyleClass(String evenColumnStyleClass) {
		this.evenColumnStyleClass = evenColumnStyleClass;
	}

	public String getOddColumnStyleClass() {
		return oddColumnStyleClass;
	}

	public void setOddColumnStyleClass(String oddColumnStyleClass) {
		this.oddColumnStyleClass = oddColumnStyleClass;
	}

	public String getStyleInLabel() {
		return this.styleInLabel;
	}

	public void setStyleInLabel(String styleInLabel) {
		this.styleInLabel = styleInLabel;
	}

	private String[] getCellContent(String label) {
		String[] ss = label.split("style");
		String number = ss[0].substring(1, ss[0].length() - 1);
		String color = ss[1].substring(1);
		ss[0] = number;
		ss[1] = color;
		return ss;
	}

	private void setColorInCell(String label, Map<String, String> attributes) {
		int index = label.indexOf("style");
		if (index != -1) {
			String[] result = getCellContent(label);
			label = result[0];
			this.styleInLabel = result[1];
			attributes.remove("src");
			attributes.remove("onload");
			attributes.remove("id");
			attributes.put("class", "x-grid-cell-inner");
			attributes.put("style", "background-color : " + this.styleInLabel);
			getWriter().startElement("div", attributes);
			getWriter().writeContent(label);
			getWriter().endElement("div");
		} else {
			getWriter().writeContent(label);
		}
	}

}
