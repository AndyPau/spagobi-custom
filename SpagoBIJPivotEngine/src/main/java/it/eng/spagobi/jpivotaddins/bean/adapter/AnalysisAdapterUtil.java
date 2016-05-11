/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.spagobi.jpivotaddins.bean.adapter;

import org.apache.log4j.Logger;
import it.eng.spagobi.jpivotaddins.bean.AnalysisBean;

import com.tonbeller.jpivot.chart.ChartComponent;
import com.tonbeller.jpivot.mondrian.MondrianMemento;
import com.tonbeller.jpivot.olap.model.OlapModel;
import com.tonbeller.jpivot.table.TableComponent;
import com.tonbeller.jpivot.table.navi.AxisStyleUI;
import com.tonbeller.wcf.component.ComponentSupport;

public class AnalysisAdapterUtil {
	static private Logger logger = Logger.getLogger(AnalysisAdapterUtil.class);
	/**
     * Constructs an analysis out of jpivot components
     *
     * @param chart
     * @param table
     * @param olapModel
     * @return
     */
    public static AnalysisBean createAnalysisBean(ChartComponent chart,
                                          TableComponent table, OlapModel olapModel) {
    	logger.debug("IN");
        AnalysisBean analysis = new AnalysisBean();
        //analysis.setShowPareto(true);
        analysis.setShowChart(((ComponentSupport)chart).isVisible());
        analysis.setShowTable(((ComponentSupport)table).isVisible());
        analysis.setChartTitle(chart.getChartTitle());
        analysis.setFontName(chart.getFontName());
        analysis.setFontStyle(chart.getFontStyle());
        analysis.setFontSize(chart.getFontSize());
        analysis.setSlicerFontName(chart.getSlicerFontName());
        analysis.setSlicerFontStyle(chart.getSlicerFontStyle());
        analysis.setSlicerFontSize(chart.getSlicerFontSize());
        analysis.setAxisFontName(chart.getAxisFontName());
        analysis.setAxisFontSize(chart.getAxisFontSize());
        analysis.setAxisFontStyle(chart.getAxisFontStyle());
        analysis.setLegendFontName(chart.getLegendFontName());
        analysis.setLegendFontStyle(chart.getLegendFontStyle());
        analysis.setLegendFontSize(chart.getLegendFontSize());
        analysis.setLegendPosition(chart.getLegendPosition());
        analysis.setSlicerPosition(chart.getSlicerPosition());
        analysis.setSlicerAlignment(chart.getSlicerAlignment());
        analysis.setChartType(chart.getChartType());
        analysis.setChartHeight(chart.getChartHeight());
        analysis.setChartWidth(chart.getChartWidth());
        analysis.setHorizAxisLabel(chart.getHorizAxisLabel());
        analysis.setVertAxisLabel(chart.getVertAxisLabel());
        analysis.setAxisTickFontName(chart.getAxisTickFontName());
        analysis.setAxisTickFontSize(chart.getAxisTickFontSize());
        analysis.setAxisTickFontStyle(chart.getAxisTickFontStyle());
        analysis.setShowLegend(chart.getShowLegend());
        analysis.setShowSlicer(chart.isShowSlicer());
        analysis.setDrillThroughEnabled(chart.isDrillThroughEnabled());
        analysis.setTickLabelRotate(chart.getTickLabelRotate());
        //analysis.setForegroundAlpha(chart.getForegroundAlpha());
        //analysis.setUseChartSize(chart.isUseChartSize());
        analysis.setBgColorB(chart.getBgColorB());
        analysis.setBgColorG(chart.getBgColorG());
        analysis.setBgColorR(chart.getBgColorR());

        AxisStyleUI asu = (AxisStyleUI) table.getExtensions().get(AxisStyleUI.ID);
        analysis.setLevelStyle(asu.isLevelStyle());
        analysis.setHideSpans(asu.isHideSpans());

        // only if we have an xmla model
        // logger.info(olapModel.getClass().getName());
        if (olapModel.getBookmarkState(0) instanceof MondrianMemento) {
			MondrianMemento olapMem = (MondrianMemento) olapModel
					.getBookmarkState(0);
            analysis.setMdxQuery(olapMem.getMdxQuery());
            //analysis.setXmlaUri(olapMem.getUri());
            //analysis.setCatalog(olapMem.getCatalog());
        }
        
        logger.debug("OUT");
        return analysis;
    }

}
