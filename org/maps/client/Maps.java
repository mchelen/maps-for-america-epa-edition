package org.maps.client;
/*
Copyright 2009, Mike Jacobs

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.maps.client.services.RSEICountyResultsService;
import org.maps.client.services.RSEICountyResultsServiceAsync;
import org.maps.client.services.RSEIStateResultsService;
import org.maps.client.services.RSEIStateResultsServiceAsync;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.IconButtonEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.KeyboardEvents;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.SliderEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.custom.Portal;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.tips.ToolTipConfig;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.Control;
import com.google.gwt.maps.client.control.ControlAnchor;
import com.google.gwt.maps.client.control.ControlPosition;
import com.google.gwt.maps.client.control.SmallMapControl;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.geocode.GeoAddressAccuracy;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LocationCallback;
import com.google.gwt.maps.client.geocode.Placemark;
import com.google.gwt.maps.client.geocode.StatusCodes;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;


public class Maps implements EntryPoint {
	private MapWidget map;
	private Geocoder geocoder = new Geocoder();
	private List<IMapLayer> layers = new ArrayList<IMapLayer>();
	private IMapLayer healthRisksLayer, hazardsLayer, weightLayer;
	private static final String TRI_DATA_SOURCE = "2006 EPA Toxic Release Inventory";
	private static final String APPLICATION_TITLE = "EPA Edition: Explore toxic release information from the EPA";
	private static final String APPLICATION_SUMMARY = "Explore the relative chronic health risks, hazard levels and toxic release volumes across the country as reported by the <a target=new href=http://www.epa.gov/tri/triprogram/tri_program_fact_sheet.htm>EPA Toxic Release Inventory</a>.  Maps for America combines population, toxic chemical release reports, and chemical toxicity into easy to navigate maps.  Select <b>Health Risks</b>, <b>Toxic Hazards</b> or <b>Weight</b> and search, click and zoom in to discover more.  Hover your mouse over the options buttons for more information.  <a href=\"http://www.epa.gov/oppt/rsei/pubs/using_rsei.html\" target=new>How to use this information</a>";
	private static final String HEALTH_TITLE = "Health Risk-related Results";
	private static final String HAZARDS_TITLE = "Hazards-related Results";
	private static final String WEIGHT_TITLE = "Weight-related Results";
	private static final String HEALTH_SUMMARY = "The relative impacts of releases of toxic chemicals by combining estimates of toxicity, exposure level, and the exposed population to provide risk-related comparisons.<br><br>The score is a unitless measure that is not independently meaningful, but is a risk-related estimate that can be compared to other estimates calculated using the same methods.";
	private static final String HAZARDS_SUMMARY = "The relative impacts of releases of toxic chemicals by combining estimates of toxicity and exposure level (without considering population) to provide risk-related comparisons.<br><br>The score is a unitless measure that is not independently meaningful, but is a risk-related estimate that can be compared to other estimates calculated using the same methods.";
	private static final String WEIGHT_SUMMARY = "The weight of toxic chemical releases. While the the EPA requires that all storage and transfers be reported, this category only includes the weight of releases that are deemed toxic by the EPA.";
	private LayoutContainer mainPanel;
	private Portal bottomPortal;
	private static final String MAP_PANEL_WIDTH = "850px";
	private static final int MAP_PANEL_WIDTH_INT = 850;
	private static final String MAP_PANEL_HEIGHT = "600px";
	private static final String MAP_WIDTH = "500px";
	private static final int MAP_WIDTH_INT = 500;
	private static final String MAP_HEIGHT = "300px";
	private static final int NATIONAL_AVERAGES_HEIGHT = 200;
	private static final int STATE_INFO_HEIGHT = 200;
	private static final int SCROLL_BAR_HEIGHT = 40;
	private static final String[] binColors ={"#279500","#279500", "#f7e400","#f88700", "#d8001c", "#6b48c8"};
	
	
	private int portalColumn = 1;
	
	private RSEIStateResultsServiceAsync stateResultsService;
	private RSEICountyResultsServiceAsync countyResultsService;

	private List<RSEIStateResults> stateResults;

	private void addBottomPortal(LayoutContainer aParent){
		ContentPanel cp = new ContentPanel();  
		cp.setBodyBorder(false);   
		cp.setHeading("Results Portal: Click on the map to add results here");
		cp.setIconStyle("/images/silk/table_multiple.png");
		cp.setHeaderVisible(true);
		cp.setLayout(new FitLayout());  
		// cp.setSize(600, 300);
		bottomPortal = new Portal(2);
		//bottomPortal.setTitle("Click on the map and add results here");
		bottomPortal.setBorders(true);
		bottomPortal.setHeight(MAP_HEIGHT);
		bottomPortal.setColumnWidth(0, 0.48);
		bottomPortal.setColumnWidth(1, 0.48);
		bottomPortal.setStyleAttribute("backgroundColor", "white");
		cp.add(bottomPortal);
		BorderLayoutData bottomPortalLayoutData = new BorderLayoutData(LayoutRegion.SOUTH, 300);
		bottomPortalLayoutData.setMargins(new Margins(0,5,5,5));
		aParent.add(cp, bottomPortalLayoutData);
	}

	private void addMap(LayoutContainer mapPanel) {
		setUpMap();
		final Image vscale = new Image("/images/vscale.png");
		ControlPosition imagePosition = new ControlPosition(ControlAnchor.BOTTOM_RIGHT, 10, 20);
		Control.CustomControl customScale = new Control.CustomControl(imagePosition){
			protected Widget initialize(MapWidget map) {
				return vscale;
			}
			public boolean isSelectable() {
				return false;
			}	
		};

		map.addControl(customScale, imagePosition);
		
		BorderLayoutData mapLayoutData = new BorderLayoutData(LayoutRegion.WEST, MAP_WIDTH_INT); 
		mapLayoutData.setMargins(new Margins(5));
		mapLayoutData.setSplit(false);
		mapPanel.add(map, mapLayoutData);
	}
	
	private void addRightPortal(LayoutContainer mapPanel) {
//		rightPortal = new Portal(1);
//		rightPortal.setBorders(true);  
//		rightPortal.setColumnWidth(0, 1.0);
//		rightPortal.setStyleAttribute("backgroundColor", "white");
//		getNationalRankingsFromServer(rightPortal, 0);
		FitLayout fitLayout = new FitLayout();
		ContentPanel rightPanel = new ContentPanel(fitLayout);
		rightPanel.setBodyBorder(false);
		rightPanel.setHeading("National Scores");
		rightPanel.setSize("100%","100%");
		getNationalRankingsFromServer(rightPanel);
		BorderLayoutData rightPortalLayoutData = new BorderLayoutData(LayoutRegion.CENTER,MAP_PANEL_WIDTH_INT - MAP_WIDTH_INT);
		rightPortalLayoutData.setCollapsible(true);
		rightPortalLayoutData.setMargins(new Margins(5,5,5,0));
		mapPanel.add(rightPanel, rightPortalLayoutData);
	}

	
	private void addSearchPanel(ContentPanel contentPanel) {
		HBoxLayout searchPanelLayout = new HBoxLayout();
		searchPanelLayout.setHBoxLayoutAlign(HBoxLayoutAlign.TOP);
		searchPanelLayout.setAdjustForFlexRemainder(true);
		LayoutContainer searchPanel = new LayoutContainer();
		searchPanel.setLayout(searchPanelLayout);
		searchPanel.setSize("100%", "100%");
		
		final TextField<String> addressTextBox = new TextField<String>();
		addressTextBox.setWidth(200);
		KeyListener addressKeyListener = new KeyListener(){
			public void componentKeyPress(ComponentEvent event){
				if(event.getKeyCode() == KeyboardEvents.Enter.getEventCode()){
					performSearch(addressTextBox.getRawValue());
				}
			}
			
		};
		addressTextBox.addKeyListener(addressKeyListener);
		
		searchPanel.add(addressTextBox, new HBoxLayoutData(new Margins(0, 10, 0, 0)));
		SelectionListener<ButtonEvent> searchButtonListener = new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				performSearch(addressTextBox.getRawValue());
			}

		};
		Button searchButton = new Button("Search", searchButtonListener);
		
		searchPanel.add(searchButton, new HBoxLayoutData(new Margins(0, 10, 0, 0)));
		String toggleGroup = "options";
		int dismissDelay = 15000;
		
		final ToggleButton healthToggleButton = new ToggleButton("Health Risks");
		final ToggleButton hazardsToggleButton = new ToggleButton("Toxic Hazards");
		final ToggleButton weightToggleButton = new ToggleButton("Weight");
		
		SelectionListener<ButtonEvent> sharedToggleButtonListener= new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				healthRisksLayer.setEnabled(healthToggleButton.isPressed());
				hazardsLayer.setEnabled(hazardsToggleButton.isPressed());
				weightLayer.setEnabled(weightToggleButton.isPressed());
				refreshLayers();
			}
		};
		
		healthToggleButton.toggle(true);
		healthToggleButton.addSelectionListener(sharedToggleButtonListener);
		ToolTipConfig healthToolTip = new ToolTipConfig(HEALTH_TITLE, HEALTH_SUMMARY);
		healthToolTip.setDismissDelay(dismissDelay);
		healthToggleButton.setToolTip(healthToolTip);
		healthToggleButton.setToggleGroup(toggleGroup);
		searchPanel.add(healthToggleButton, new HBoxLayoutData(new Margins(0, 0, 0, 18)));
		
		ToolTipConfig hazardsToolTip = new ToolTipConfig(HAZARDS_TITLE, HAZARDS_SUMMARY);
		hazardsToolTip.setDismissDelay(dismissDelay);
		hazardsToggleButton.setToolTip(hazardsToolTip);
		hazardsToggleButton.addSelectionListener(sharedToggleButtonListener);
		hazardsToggleButton.setToggleGroup(toggleGroup);
		searchPanel.add(hazardsToggleButton, new HBoxLayoutData(new Margins(0, 8, 0, 8)));
		
		ToolTipConfig weightToolTip = new ToolTipConfig(WEIGHT_TITLE, WEIGHT_SUMMARY);
		weightToolTip.setDismissDelay(dismissDelay);
		weightToggleButton.setToolTip(weightToolTip);
		weightToggleButton.addSelectionListener(sharedToggleButtonListener);
		weightToggleButton.setToggleGroup(toggleGroup);
		searchPanel.add(weightToggleButton, new HBoxLayoutData(new Margins(0, 8, 0, 0)));
		
		Listener<SliderEvent> sliderListener = new Listener<SliderEvent>(){
			public void handleEvent(SliderEvent ev) {
				int newValue = ev.getNewValue();
				double opacity = ((double)newValue)/100.0;
				for(IMapLayer layer : getLayers()){
					layer.setOpacity(opacity);
				}
				map.clearOverlays();
				refreshLayers();
			}
		};
		
		Slider opacitySlider = new Slider();
		opacitySlider.addListener(Events.Change, sliderListener);

		opacitySlider.setTitle("Adjust the darkness of the results");
		opacitySlider.setMessage("{0}% opaque");
		opacitySlider.setIncrement(10);
		opacitySlider.setValue(50, true);
		opacitySlider.setWidth(100);
		
		searchPanel.add(opacitySlider, new HBoxLayoutData(new Margins(0, 8, 0, 40)));	
		contentPanel.add(searchPanel,new VBoxLayoutData(new Margins(10, 0, 0, 0)));
		addressTextBox.focus();
	} 
	   
	private void addStateContent(State state) {
		getStateContentFromServer(state, bottomPortal, nextPortalColumn());
	}
	
	private void createWidgets(){
		setUpLayers();
		TableLayout mainPanelLayout = new TableLayout(1);
		mainPanelLayout.setCellSpacing(10);
		mainPanelLayout.setWidth(MAP_WIDTH);
		
		mainPanel = new LayoutContainer();
		mainPanel.setLayout(mainPanelLayout);
		mainPanel.setWidth("100%");
		
		VBoxLayout contentPanelLayout = new VBoxLayout();
		Padding contentPanelPadding = new Padding(10);
		contentPanelLayout.setPadding(contentPanelPadding);
		
		ContentPanel contentPanel = new ContentPanel();  
		contentPanel.setHeading(APPLICATION_TITLE);  
		contentPanel.setHeight(120);
		contentPanel.setFrame(false);
		contentPanel.setHeaderVisible(true);
		contentPanel.setBorders(false);
		contentPanel.setLayout(contentPanelLayout);
		contentPanel.setStyleAttribute("backgroundColor", "white"); 
		
		contentPanel.setBodyStyle("font-size: 11px; font-family:sans-serif; padding-right: 10px;");
		contentPanel.addText(APPLICATION_SUMMARY);
		
		addSearchPanel(contentPanel);
		mainPanel.add(contentPanel);
		
		BorderLayout mapPanelLayout = new BorderLayout();
		mapPanel = new LayoutContainer();
		mapPanel.setSize(MAP_PANEL_WIDTH, MAP_PANEL_HEIGHT);
		mapPanel.setLayout(mapPanelLayout);
		addMap(mapPanel);
		addRightPortal(mapPanel);
		
		addBottomPortal(mapPanel);
		mainPanel.add(mapPanel);
		
		RootPanel.get("application").add(mainPanel);
		mainPanelLayout.layout();
	}
	private LayoutContainer mapPanel;
	private synchronized RSEICountyResultsServiceAsync getCountyResultsService() {
		if(countyResultsService == null){
			countyResultsService = GWT.create(RSEICountyResultsService.class);
		}
		return countyResultsService;
	}
	
	private Html getEPAResultsVisualization(int binNumber){
		String style = binColors[binNumber];
		Html h = new Html("<span style='background-color:" + style
				+ "'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		return h;
	}
	
	private List<IMapLayer> getLayers() {
		return layers;
	}
	
	private String getMessage(int statusCode){
		String answer = "Unknown reason";
		if(statusCode == StatusCodes.API_ERROR){
			answer = "API Error";
		}
		else if(statusCode == StatusCodes.BAD_KEY){
			answer = "Bad Key";
		}
		else if(statusCode == StatusCodes.BAD_REQUEST){
			answer = "Bad Request";
		}
		else if(statusCode == StatusCodes.MISSING_ADDRESS){
			answer = "Missing Address";
		}
		else if(statusCode == StatusCodes.SERVER_ERROR){
			answer = "Google Server Error";
		}
		else if(statusCode == StatusCodes.MISSING_QUERY){
			answer = "Missing Queury";
		}
		else if(statusCode == StatusCodes.SUCCESS){
			answer = "Success";
		}
		else if(statusCode == StatusCodes.TOO_MANY_QUERIES){
			answer = "Too Many Queries";
		}
		else if(statusCode == StatusCodes.UNAVAILABLE_ADDRESS){
			answer = "Unavailable Address";
		}
		else if(statusCode == StatusCodes.UNKNOWN_ADDRESS){
			answer = "Unknown Address";
		}
		else if(statusCode == StatusCodes.UNKNOWN_DIRECTIONS){
			answer = "Unknown Directions";
		}
		return answer;
	}
	private Grid<BaseModelData> getNationalRankings(ListStore<BaseModelData> store){
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		RowNumberer r = new RowNumberer();
		configs.add(r);
		ColumnConfig column = new ColumnConfig();
		column.setId("shortName");
		column.setHeader("State");
		column.setWidth(40);
		configs.add(column);
		
		GridCellRenderer<BaseModelData> riskCellRenderer = new GridCellRenderer<BaseModelData>() {
			public Object render(BaseModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BaseModelData> store, Grid<BaseModelData> grid) {
				Integer binIndex = (Integer) model.get(property+"Bin");
				if (binIndex != null) {
					String style = binColors[binIndex];
					return "<span style='background-color:" + style
							+ "'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+ "</span>";
				}
				else{
					return "missing";
				}
			}
		};
		
		
		column = new ColumnConfig(); 
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setId("fullModel");  
		column.setHeader("Health Risks");
		column.setWidth(80);
		column.setRenderer(riskCellRenderer);
		configs.add(column);
		
		column = new ColumnConfig(); 
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setId("triPoundsWToxTox");  
		column.setHeader("Toxic Hazards");
		column.setWidth(80);
		column.setRenderer(riskCellRenderer);
		configs.add(column);
		
		column = new ColumnConfig(); 
		column.setAlignment(HorizontalAlignment.LEFT);
		column.setId("triPoundsWTox");  
		column.setHeader("Weight");
		column.setWidth(80);
		column.setRenderer(riskCellRenderer);
		configs.add(column);
		
		ColumnModel cm = new ColumnModel(configs);
		
		SelectionChangedListener<BaseModelData> selectionChangedListener = new SelectionChangedListener<BaseModelData>(){
			@Override
			public void selectionChanged(SelectionChangedEvent<BaseModelData> se) {
				BaseModelData selected = se.getSelectedItem();
				String shortName = selected.get("shortName");
				String longName = State.getByShortName(shortName).getLongName();
				performSearch(longName+", US");
			}
		};
		

		Grid<BaseModelData> grid = new Grid<BaseModelData>(store, cm);
		grid.getSelectionModel().addSelectionChangedListener(selectionChangedListener);
		grid.setStyleAttribute("borderTop", "none");
		grid.setBorders(true);

		grid.setHeight(NATIONAL_AVERAGES_HEIGHT);
		grid.addPlugin(r);
		return grid;
	}
	
	private void getNationalRankingsFromServer(final ContentPanel aParent){
		AsyncCallback<List<RSEIStateResults>> cb = new AsyncCallback<List<RSEIStateResults>>(){
			public void onFailure(Throwable caught) {
				handleFailure(caught.getMessage());
			}
			public void onSuccess(List<RSEIStateResults> results) {
				stateResults = results;
				ListStore<BaseModelData> store = new ListStore<BaseModelData>();
				for (RSEIStateResults result : results) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("shortName", result.getShortName());
					map.put("fullModel", result.getFullModel());
					map.put("fullModelBin", result.getFullModelBin());
					map.put("triPoundsWToxTox", result.getTriPoundsWToxTox());
					map.put("triPoundsWToxToxBin", result
							.getTriPoundsWToxToxBin());
					map.put("triPoundsWTox", result.getTriPoundsWTox());
					map.put("triPoundsWToxBin", result.getTriPoundsWToxBin());
					BaseModelData bmd = new BaseModelData(map);
					store.add(bmd);
				}
				store.sort("fullModel", SortDir.DESC);
				Grid<BaseModelData> grid = getNationalRankings(store);
				grid.setHeight(NATIONAL_AVERAGES_HEIGHT);
				aParent.add(grid);
				aParent.layout(true);
				aParent.unmask();
			}
		};
		aParent.mask("Loading...");
		getStateResultsService().getResults(cb);
	}
	
//	private void getNationalRankingsFromServer(final Portal aPortal, final int column){
//		AsyncCallback<List<RSEIStateResults>> cb = new AsyncCallback<List<RSEIStateResults>>(){
//			public void onFailure(Throwable caught) {
//				handleFailure(caught.getMessage());
//			}
//			public void onSuccess(List<RSEIStateResults> results) {
//				ListStore<BaseModelData> store = new ListStore<BaseModelData>();
//				for (RSEIStateResults result : results) {
//					Map<String, Object> map = new HashMap<String, Object>();
//					map.put("shortName", result.getShortName());
//					map.put("fullModel", result.getFullModel());
//					map.put("fullModelBin", result.getFullModelBin());
//					map.put("triPoundsWToxTox", result.getTriPoundsWToxTox());
//					map.put("triPoundsWToxToxBin", result
//							.getTriPoundsWToxToxBin());
//					map.put("triPoundsWTox", result.getTriPoundsWTox());
//					map.put("triPoundsWToxBin", result.getTriPoundsWToxBin());
//					BaseModelData bmd = new BaseModelData(map);
//					store.add(bmd);
//				}
//				store.sort("fullModel", SortDir.DESC);
//				Grid<BaseModelData> grid = getNationalRankings(store);
//				grid.setHeight(NATIONAL_AVERAGES_HEIGHT);
//				
//				Portlet national = new Portlet();
//				national.setHeading("National Rankings");  
//				national.setCollapsible(true);
//				national.setAnimCollapse(true);
//				national.setWidth("100%");  
//				national.setHeight(NATIONAL_AVERAGES_HEIGHT + SCROLL_BAR_HEIGHT);
//				national.add(grid);
//				
//				aPortal.add(national, column);
//				aPortal.unmask();
//			}
//		};
//		//RSEIStateResultsServiceAsync service = GWT.create(RSEIStateResultsService.class);
//		aPortal.mask("Loading...");
//		getStateResultsService().getResults(cb);
//	}
	
	private void getStateContentFromServer(final State state, final Portal aPortal, final int column){
		AsyncCallback<List<RSEICountyResults>> cb = new AsyncCallback<List<RSEICountyResults>>(){
			public void onFailure(Throwable caught) {
				handleFailure(caught.getMessage());
			}
			public void onSuccess(List<RSEICountyResults> results) {
				ListStore<BaseModelData> store = new ListStore<BaseModelData>();
				for (RSEICountyResults result : results) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("countyName", result.getCountyName());
					map.put("shortName", result.getShortName());
					map.put("fullModel", result.getFullModel());
					map.put("fullModelBin", result.getFullModelBin());
					map.put("triPoundsWToxTox", result.getTriPoundsWToxTox());
					map.put("triPoundsWToxToxBin", result
							.getTriPoundsWToxToxBin());
					map.put("triPoundsWTox", result.getTriPoundsWTox());
					map.put("triPoundsWToxBin", result.getTriPoundsWToxBin());
					BaseModelData bmd = new BaseModelData(map);
					store.add(bmd);
				}
				store.sort("fullModel", SortDir.DESC);
				Grid<BaseModelData> grid = getStateRankingsGrid(store);
				grid.setHeight(STATE_INFO_HEIGHT - SCROLL_BAR_HEIGHT + 10);
				
				final Portlet porlet = new Portlet();
					SelectionListener<IconButtonEvent> listener = new SelectionListener<IconButtonEvent>() {
						@Override
						public void componentSelected(IconButtonEvent ce) {
							porlet.removeFromParent();
						}
	
					};
				porlet.getHeader().addTool(new ToolButton("x-tool-close", listener));
				
				porlet.setHeading(state.getDisplayName() + " ("+TRI_DATA_SOURCE+")");  
				porlet.setCollapsible(true);
				porlet.setAnimCollapse(true);
				porlet.setWidth("100%");  
				porlet.setHeight(STATE_INFO_HEIGHT + SCROLL_BAR_HEIGHT);
			    
				TabPanel folder = new TabPanel();
				folder.setAutoWidth(true);
				folder.setHeight(STATE_INFO_HEIGHT);
				
				RSEIStateResults stateResults = getStateResults(state);
				Integer facilityCount = 0;
				Integer releaseCount = 0;
				Double fullModel = 0.0;
				Integer fullModelBin = 0;
				Double hazard = 0.0;
				Integer hazardBin = 0;
				Double triPoundsToxic = 0.0;
				Integer triPoundsToxicBin = 0;
				Double fullModelChildrenUnder10 = 0.0;
				Integer fullModelChildrenUnder10Bin = 0;
				Double fullModelChildren10to17 = 0.0;
				Integer fullModelChildren10to17Bin = 0;
				Double fullModelAdults18to44 = 0.0;
				Integer fullModelAdults18to44Bin = 0;
				Double fullModelAdults45to64 = 0.0;
				Integer fullModelAdults45to64Bin = 0;
				Double fullModelAdultsOver65 = 0.0;
				Integer fullModelAdultsOver65Bin = 0;
				
				if(stateResults != null){
					facilityCount = stateResults.getFacilityCount();
					releaseCount = stateResults.getReleaseCount();
					fullModel = stateResults.getFullModel();
					fullModelBin = stateResults.getFullModelBin();
					hazard = stateResults.getTriPoundsWToxTox();
					hazardBin = stateResults.getTriPoundsWToxToxBin();
					triPoundsToxic = stateResults.getTriPoundsWTox();
					triPoundsToxicBin = stateResults.getTriPoundsWToxBin();
					fullModelChildrenUnder10 = stateResults.getFullModelChildrenUnder10();
					fullModelChildrenUnder10Bin = stateResults.getFullModelChildrenUnder10Bin();
					fullModelChildren10to17 = stateResults.getFullModelChildren10to17();
					fullModelChildren10to17Bin = stateResults.getFullModelChildren10to17Bin();
					fullModelAdults18to44 = stateResults.getFullModelAdults18to44();
					fullModelAdults18to44Bin = stateResults.getFullModelAdults18to44Bin();
					fullModelAdults45to64 = stateResults.getFullModelAdults45To64();
					fullModelAdults45to64Bin = stateResults.getFullModelAdults45To64Bin();
					fullModelAdultsOver65 = stateResults.getFullModelAdultsOver65();
					fullModelAdultsOver65Bin = stateResults.getFullModelAdultsOver65Bin();
				}
				
				TabItem summary = new TabItem("Summary");
				TableLayout summaryPanelLayout = new TableLayout(3);
				summaryPanelLayout.setTableStyle("font-size: 11px; font-family:sans-serif; padding-right: 10px;");
				summaryPanelLayout.setCellSpacing(10);
				ContentPanel summaryPanel = new ContentPanel();    
				summaryPanel.setHeight(STATE_INFO_HEIGHT - SCROLL_BAR_HEIGHT);
				summaryPanel.setFrame(false);
				summaryPanel.setHeaderVisible(false);
				summaryPanel.setBorders(false);
				summaryPanel.setLayout(summaryPanelLayout);
				summaryPanel.setStyleAttribute("backgroundColor", "white"); 
				summaryPanel.setBodyStyle("font-size: 11px; font-family:sans-serif; padding-right: 10px;");
				
				summaryPanel.addText("<b>EPA Facilities: </b>");
				summaryPanel.addText(facilityCount.toString());
				summaryPanel.addText("&nbsp;");
				
				summaryPanel.addText("<b>Reported Releases: </b>");
				summaryPanel.addText(releaseCount.toString());
				summaryPanel.addText("&nbsp;");
				
				summaryPanel.addText("<b>Health Risk Score: </b>");
				summaryPanel.addText(fullModel.toString());
				summaryPanel.add(getEPAResultsVisualization(fullModelBin));
				
				summaryPanel.addText("<b>Toxic Hazards Score: </b>");
				summaryPanel.addText(hazard.toString());
				summaryPanel.add(getEPAResultsVisualization(hazardBin));
				
				summaryPanel.addText("<b>Toxic Weight (lbs): </b>");
				summaryPanel.addText(triPoundsToxic.toString());
				summaryPanel.add(getEPAResultsVisualization(triPoundsToxicBin));
				
				String fips = state.getFips();
				Html epaLink = new Html("<a target=new href=http://www.epa.gov/cgi-bin/broker?view=STCO&trilib=TRIQ1&state=" + fips+"&SFS=YES&year=2006&_service=oiaa&_program=xp_tri.sasmacr.tristart.macro>Click to open on new page</a>");
				summaryPanel.addText("<b>EPA State Fact Sheet: </b>");
				summaryPanel.add(epaLink);
				
				summary.add(summaryPanel);
				folder.add(summary);
				
				TabItem demographics = new TabItem("Health Risks by Age");
				demographics.setToolTip("Relative to the same group nationally");
				TableLayout demoPanelLayout = new TableLayout(3);
				demoPanelLayout.setTableStyle("font-size: 11px; font-family:sans-serif; padding-right: 10px;");
				demoPanelLayout.setCellSpacing(10);
				ContentPanel demoPanel = new ContentPanel();    
				demoPanel.setHeight(STATE_INFO_HEIGHT - SCROLL_BAR_HEIGHT);
				demoPanel.setFrame(false);
				demoPanel.setHeaderVisible(false);
				demoPanel.setBorders(false);
				demoPanel.setLayout(demoPanelLayout);
				demoPanel.setStyleAttribute("backgroundColor", "white"); 
				demoPanel.setBodyStyle("font-size: 11px; font-family:sans-serif; padding-right: 10px;");
				
				demoPanel.addText("<b>Children under 10: </b>");
				demoPanel.addText(fullModelChildrenUnder10.toString());
				demoPanel.add(getEPAResultsVisualization(fullModelChildrenUnder10Bin));
				
				demoPanel.addText("<b>Children 10 to 17: </b>");
				demoPanel.addText(fullModelChildren10to17.toString());
				demoPanel.add(getEPAResultsVisualization(fullModelChildren10to17Bin));
				
				demoPanel.addText("<b>Adults 18 to 44: </b>");
				demoPanel.addText(fullModelAdults18to44.toString());
				demoPanel.add(getEPAResultsVisualization(fullModelAdults18to44Bin));
				
				demoPanel.addText("<b>Adults 45 to 64: </b>");
				demoPanel.addText(fullModelAdults45to64.toString());
				demoPanel.add(getEPAResultsVisualization(fullModelAdults45to64Bin));
				
				demoPanel.addText("<b>Adults over 65: </b>");
				demoPanel.addText(fullModelAdultsOver65.toString());
				demoPanel.add(getEPAResultsVisualization(fullModelAdultsOver65Bin));
				
				demographics.add(demoPanel);
				folder.add(demographics);
				
				TabItem countyRankings = new TabItem("County Results");
				countyRankings.setToolTip("Relative to other counties in the same state");
				countyRankings.add(grid);
				folder.add(countyRankings);
				
				porlet.add(folder);
				
				aPortal.add(porlet, column);
				mapPanel.unmask();
				showStateFeedback(state);
			}
		};
		mapPanel.mask("Loading...");
		getCountyResultsService().getResults(state.getShortName(), cb);
	}
	
private Grid<BaseModelData> getStateRankingsGrid(ListStore<BaseModelData> store){
	List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
	RowNumberer r = new RowNumberer();
	configs.add(r);
	ColumnConfig column = new ColumnConfig();
	column.setId("countyName");
	column.setHeader("County");
	column.setWidth(85);
	configs.add(column);
	
	GridCellRenderer<BaseModelData> riskCellRenderer = new GridCellRenderer<BaseModelData>() {
		public Object render(BaseModelData model, String property,
				ColumnData config, int rowIndex, int colIndex,
				ListStore<BaseModelData> store, Grid<BaseModelData> grid) {
			Integer binIndex = (Integer) model.get(property+"Bin");
			if (binIndex != null) {
				String style = binColors[binIndex];
				return "<span style='background-color:" + style
						+ "'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
						+ "</span>";
			}
			else{
				return "missing";
			}
		}
	};
	
	column = new ColumnConfig(); 
	column.setAlignment(HorizontalAlignment.LEFT);
	column.setId("fullModel");  
	column.setHeader("Health Risks");
	column.setWidth(80);
	column.setRenderer(riskCellRenderer);
	configs.add(column);
	
	column = new ColumnConfig(); 
	column.setAlignment(HorizontalAlignment.LEFT);
	column.setId("triPoundsWToxTox");  
	column.setHeader("Toxic Hazards");
	column.setWidth(80);
	column.setRenderer(riskCellRenderer);
	configs.add(column);
	
	column = new ColumnConfig(); 
	column.setAlignment(HorizontalAlignment.LEFT);
	column.setId("triPoundsWTox");  
	column.setHeader("Weight");
	column.setWidth(80);
	column.setRenderer(riskCellRenderer);
	configs.add(column);
	
	SelectionChangedListener<BaseModelData> selectionChangedListener = new SelectionChangedListener<BaseModelData>(){
		@Override
		public void selectionChanged(SelectionChangedEvent<BaseModelData> se) {
			BaseModelData selected = se.getSelectedItem();
			String shortName = selected.get("shortName");
			String countyName = selected.get("countyName");
			String searchString = countyName + " County,"+shortName;
			performSearch(searchString);
		}
	};
	
	ColumnModel cm = new ColumnModel(configs);
	Grid<BaseModelData> grid = new Grid<BaseModelData>(store, cm);
	grid.getSelectionModel().addSelectionChangedListener(selectionChangedListener);
	grid.setStyleAttribute("borderTop", "none");
	grid.setBorders(true);
	//grid.setAutoExpandColumn("countyName");
	grid.setHeight(NATIONAL_AVERAGES_HEIGHT);
	grid.addPlugin(r);
	return grid;
}

private RSEIStateResults getStateResults(State state){
	RSEIStateResults answer = null;
	if(stateResults == null) return null;
	for(RSEIStateResults r : stateResults){
		if(r.getShortName().equals(state.getShortName())){
			answer = r;
			break;
		}
	}
	return answer;
}

private synchronized RSEIStateResultsServiceAsync getStateResultsService() {
	if(stateResultsService == null){
		stateResultsService = GWT.create(RSEIStateResultsService.class);
	}
	return stateResultsService;
}
	
	private int getZoom(int accuracy){
		int answer = 0;
		if(accuracy == GeoAddressAccuracy.ADDRESS){
			answer = 15;
		}
		else if(accuracy == GeoAddressAccuracy.COUNTRY){
			answer = 4;
		}
		else if(accuracy == GeoAddressAccuracy.INTERSECTION){
			answer = 11;
		}
		else if(accuracy == GeoAddressAccuracy.POSTAL_CODE){
			answer = 11;
		}
		else if(accuracy == GeoAddressAccuracy.REGION){
			answer = 5;
		}
		else if(accuracy == GeoAddressAccuracy.SUB_REGION){
			answer = 8;
		}
		else if(accuracy == GeoAddressAccuracy.TOWN){
			answer = 12;
		}
		//System.out.println("zoom: " + answer);
		return answer;
	}
	
	private void handleFailure(String message){
		final Listener<MessageBoxEvent> listener = new Listener<MessageBoxEvent>() {
			public void handleEvent(MessageBoxEvent ce) {
				ce.getMessageBox().close();
			}
		}; 
		MessageBox.alert("We Encountered a Problem", message, listener);
	}
	
	private void handleMapClick(final LatLng point){
		LocationCallback callback = new LocationCallback(){
			public void onFailure(int statusCode) {
				// clicked somewhere off of the US
			}

			public void onSuccess(JsArray<Placemark> locations) {
				Placemark placemark = locations.get(0);
				handleMapClick(point, placemark, map.getZoomLevel());
			}
		};
		geocoder.getLocations(point, callback);
	}

	private void handleMapClick(LatLng point, Placemark placemark, int zoomLevel){
		String shortName = placemark.getState();
		String country = placemark.getCountry();
		if(country.equalsIgnoreCase("US") || country.equalsIgnoreCase("PR")){
			if(shortName == null){
				shortName = "PR";
			}
			State state = State.getByShortName(shortName);
			addStateContent(state);
		}
		
	}

	private int nextPortalColumn(){
		if(portalColumn == 0){
			portalColumn = 1;
		}
		else{
			portalColumn = 0;
		}
		return portalColumn;
	}

	// GWT module entry point method.
	public void onModuleLoad() {
		State.initialize();
		createWidgets();
	}
	
	private void performSearch(final String address) {
		if(address != null && !address.isEmpty()){
			LocationCallback callback = new LocationCallback(){
				public void onFailure(int statusCode) {
					String message = "We could not find " + address + " due to: " + getMessage(statusCode);
					handleFailure(message);
				}

				public void onSuccess(JsArray<Placemark> locations) {
					map.clearOverlays();
					Placemark location = locations.get(0);
					map.setCenter(location.getPoint(), getZoom(location.getAccuracy()));
					refreshLayers();
				}
			};
			
			geocoder.getLocations(address, callback);
		}
	}
	
	private void refreshLayers(){
		//System.out.println("refreshing layers");
		for(IMapLayer layer : getLayers()){
			//System.out.println("Refreshing " + layer.getDisplayName());
			if(layer.isEnabled()){
				layer.show(map);
			}
			else {
				layer.hide(map);
			}
		}
	}
	
	private void setUpLayers(){
		healthRisksLayer = new ImageLayer("Health Risks", "http://indietechnologies.com/maps-for-america/tiles/states/risks/","http://indietechnologies.com/maps-for-america/tiles/counties/risks/");
		healthRisksLayer.setEnabled(true);
		getLayers().add(healthRisksLayer);
		
		hazardsLayer = new ImageLayer("Hazards", "http://indietechnologies.com/maps-for-america/tiles/states/hazards/","http://indietechnologies.com/maps-for-america/tiles/counties/hazards/");
		hazardsLayer.setEnabled(false);
		getLayers().add(hazardsLayer);
		
		weightLayer = new ImageLayer("Weight", "http://indietechnologies.com/maps-for-america/tiles/states/pounds/","http://indietechnologies.com/maps-for-america/tiles/counties/pounds/");
		weightLayer.setEnabled(false);
		getLayers().add(weightLayer);
		
		TRIFacilitiesLayer facilities = new TRIFacilitiesLayer();
		facilities.setDisplayName("Facilities");
		facilities.setEnabled(true);
		facilities.setStartZoomLevel(6);
		getLayers().add(facilities);
	}
	
	private void setUpMap() {
		LatLng cawkerCity = LatLng.newInstance(39.509, -98.434);
		// Open a map centered on Cawker City, KS USA
		MapOptions mapOptions = MapOptions.newInstance();
		mapOptions.setDraggableCursor("default");
		mapOptions.setDraggingCursor("move");
		map = new MapWidget(cawkerCity, 3, mapOptions);
		map.setSize(MAP_WIDTH, MAP_HEIGHT);
		map.setScrollWheelZoomEnabled(true);
		map.setDoubleClickZoom(false);
		// Add some controls for the zoom level
		map.addControl(new SmallMapControl());
		refreshLayers();
		MapClickHandler handler = new MapClickHandler(){
			public void onClick(MapClickEvent event) {
				if(event.getLatLng() != null){
					handleMapClick(event.getLatLng());
				}
			}
		};
		map.addMapClickHandler(handler);
	}

	private void showStateFeedback(State state) {
		String longName = state.getDisplayName();
		Info.display("Information", longName + " was added below", "");
	}

}
