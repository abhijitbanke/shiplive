package com.manikssys.in.reports.windows;

import com.manikssys.in.common.CommonOperation;
import com.manikssys.in.common.ComponentValidation;
import com.manikssys.in.common.Constants;
import com.manikssys.in.common.ReportGenerator;
import com.manikssys.in.common.ToolbarComposer;
import com.manikssys.in.operational.beans.OprPortMaster;
import com.manikssys.in.operational.beans.OprVesselMaster;
import com.manikssys.in.operational.business.IVesselPosition;
import com.manikssys.in.operational.business.OprVesselPositionBS;
import com.manikssys.in.security.beans.ScrUserMaster;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.zkoss.zk.ui.event.Events;

/**

 * User: sandeep
 * Date: Sep 27, 2010
 */
public class ReportComposer extends GenericForwardComposer {

    private Window winSingleReport;
    private Datebox dbFrom, dbTo;
    private Label reportNameLabel;
    private Rows rwsReportParam;
    private Textbox txtCargo, txtOwner, txtCharterer, txtAgent, txtShipper, txtReceiver, txtVessel;
    private Label lblPort, lblCargo, lblVessel, lblOwner, lblCharterer, lblAgent, lblShipper, lblReceiver, lblDischargePort;
    private Combobox cmbPort, cmbDischargePort;
    private Listbox lBoxDispComp;
    private Iframe iFrameReport;
    private Radiogroup rGpRepState, rGpRepVersion;
    private static Logger log;
    private ScrUserMaster loggedUser;
    private String reportType;
    private boolean isNewReport = true;
    private ToolbarComposer toolBarComposer = null;
    private Groupbox gbReportType;

    // EVENTS
    public void onCreate$winSingleReport() {
        System.out.println("onCreate clled....");
        try {
            Executions.getCurrent().getDesktop().setAttribute("currentWindow", this);
            log = Logger.getLogger(ReportComposer.class);
            loggedUser = (ScrUserMaster) sessionScope.get("user");
            toolBarComposer = (ToolbarComposer) Executions.getCurrent().getDesktop().getAttribute("commonHBox$composer");

            initParamComponents();
            IVesselPosition iVPBs = new OprVesselPositionBS();
            CommonOperation.beginTransaction();
            List<OprPortMaster> portsList = iVPBs.getPortsList(); // Load all Ports            
            fillPort(portsList, cmbPort);
            fillPort(portsList, cmbDischargePort);
            CommonOperation.commitTransaction();
            getReportName(); 

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onItemMove(ForwardEvent fEvent) {
        try {

            Listitem draaagedLItem = (Listitem) ((DropEvent) fEvent.getOrigin()).getDragged();
            Listitem droppedLItem = (Listitem) fEvent.getOrigin().getTarget();
            droppedLItem.getParent().insertBefore(draaagedLItem, droppedLItem.getNextSibling());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onChange$dbFrom() {
        ComponentValidation.removeErrorMessage(dbFrom);
    }

    public void onChange$dbTo() {
        ComponentValidation.removeErrorMessage(dbTo);
    }

    public void onClick$btnGenerate() {
        System.out.println("Btn clicked....");
        generateDynaReport();
    }

    // CUSTOM METHODS
    // Method to initilize the param Components
    private void initParamComponents() {

        lblPort = new Label(Labels.getLabel("app.report.reportparam.label.port"));
        lblCargo = new Label(Labels.getLabel("app.report.reportparam.label.cargo"));
        lblVessel = new Label(Labels.getLabel("app.report.reportparam.label.vessel"));
        lblOwner = new Label(Labels.getLabel("app.report.reportparam.label.owner"));
        lblCharterer = new Label(Labels.getLabel("app.report.reportparam.label.charterer"));
        lblAgent = new Label(Labels.getLabel("app.report.reportparam.label.agent"));
        lblShipper = new Label(Labels.getLabel("app.report.reportparam.label.shiper"));
        lblReceiver = new Label(Labels.getLabel("app.report.reportparam.label.receiver"));
        lblDischargePort = new Label(Labels.getLabel("app.report.reportparam.label.dischargeport"));
        cmbPort = new Combobox();
        txtCargo = new Textbox();
//        cmbVessel = new Combobox();
        txtVessel = new Textbox();
        txtOwner = new Textbox();
        txtCharterer = new Textbox();
        txtAgent = new Textbox();
        txtShipper = new Textbox();
        txtReceiver = new Textbox();
        cmbDischargePort = new Combobox();

        txtVessel.addForward(Events.ON_CHANGE, winSingleReport, "onVesselChange");
    }

    public void onVesselChange(ForwardEvent event) throws InterruptedException {

        OprVesselMaster vessel = null;

        String lovFile = Labels.getLabel("app.vp.vessellov");
        Window w = (Window) Executions.createComponents(lovFile, null, null);//("WEB-INF"+File.separator+"presentation"+File.separator+"LOV"+File.separator+"CommonVesselLov.zul", null, null);
        w.setAttribute("PARAM_TEXT", txtVessel.getText());
        w.setAttribute("OBJECT", null);
        w.setAttribute("width", "400px");
        w.setMaximizable(true);
        w.doModal();
        vessel = w.getAttribute("OBJECT") != null ? (OprVesselMaster) w.getAttribute("OBJECT") : null;
//        System.out.println("VesselName  : " + vessel.getVesselName());
        if (vessel != null) {
            txtVessel.setText(vessel.getVesselName());
            txtVessel.setAttribute("VESSEL_OBJECT", vessel);
        } else {
            txtVessel.setText("");
            txtVessel.setAttribute("VESSEL_OBJECT", null);
        }
    }

    // Method to attach the cargo parameter components to screen
    private void generateCargoParams() {

        clearParamFields();

        Row row1 = new Row();
        row1.setStyle("background:none;");
        row1.setParent(rwsReportParam);
        row1.appendChild(lblPort);
        row1.appendChild(cmbPort);
        row1.appendChild(lblVessel);
        row1.appendChild(txtVessel);

        Row row2 = new Row();
        row2.setStyle("background:none;");
        row2.setParent(rwsReportParam);
        row2.appendChild(lblCargo);
        row2.appendChild(txtCargo);
        row2.appendChild(lblAgent);
        row2.appendChild(txtAgent);

        Row row3 = new Row();
        row3.setStyle("background:none;");
        row3.setParent(rwsReportParam);
        row3.appendChild(lblOwner);
        row3.appendChild(txtOwner);
        row3.appendChild(lblCharterer);
        row3.appendChild(txtCharterer);

        Row row4 = new Row();
        row4.setStyle("background:none;");
        row4.setParent(rwsReportParam);
        row4.appendChild(lblShipper);
        row4.appendChild(txtShipper);
        row4.appendChild(lblReceiver);
        row4.appendChild(txtReceiver);

        Row row5 = new Row();
        row5.setStyle("background:none;");
        row5.setParent(rwsReportParam);
        row5.appendChild(lblDischargePort);
        row5.appendChild(cmbDischargePort);
    }

    // Method to attach the cargo parameter components to screen
    private void generateVesselPostionParams() {
        clearParamFields();
        Row row1 = new Row();
        row1.setStyle("background:none;");
        row1.setParent(rwsReportParam);
        row1.appendChild(lblPort);
        row1.appendChild(cmbPort);
        row1.appendChild(lblVessel);
        row1.appendChild(txtVessel);
        Row row2 = new Row();
        row2.setStyle("background:none;");
        row2.setParent(rwsReportParam);
        row2.appendChild(lblCargo);
        row2.appendChild(txtCargo);
        row2.appendChild(lblAgent);
        row2.appendChild(txtAgent);
    }

    // Method to clear the param fields
    private void clearParamFields() {

        cmbPort.setSelectedIndex(-1);
        txtCargo.setText("");
//        cmbVessel.setSelectedIndex(-1);
        txtVessel.setText("");
        txtOwner.setText("");
        txtCharterer.setText("");
        txtAgent.setText("");
        txtShipper.setText("");
        txtReceiver.setText("");
        cmbDischargePort.setSelectedIndex(-1);
        rwsReportParam.getChildren().clear();
    }

    // Method to fill the ports to combobox
    private void fillPort(List<OprPortMaster> portList, Combobox cmbBox) {

        for (OprPortMaster portMaster : portList) {
            Comboitem cmbItem = new Comboitem();
            cmbItem.setLabel(portMaster.getPortName());
            cmbItem.setDescription(portMaster.getPortCode());
            cmbItem.setValue(portMaster);
            cmbItem.setParent(cmbBox);
        }
    }
    // Method to fill the vessels to combobox

    private void fillVessel(List<OprVesselMaster> vesselList, Combobox cmbBox) {

        for (OprVesselMaster vesselMaster : vesselList) {
            Comboitem cmbItem = new Comboitem();
            cmbItem.setLabel(vesselMaster.getVesselName());
            cmbItem.setDescription(vesselMaster.getVesselCode());
            cmbItem.setValue(vesselMaster);
            cmbItem.setParent(cmbBox);
        }
    }

    // Method to generate the display components for cargo
    private void generateCargoDispComp() {

        lBoxDispComp.getItems().clear();
        Map cargoMap = createCargoDispCompMap();
        putLoadedMap(cargoMap);
    }

    // Method to generate the display components for cargo
    private void generateVesselPositionDispComp() {

        lBoxDispComp.getItems().clear();
        Map vpMap = createVesselPositionDispCompMap();
        putLoadedMap(vpMap);
    }

    // Method to create Cargo map for Cargo display components
    private Map createCargoDispCompMap() {

        Map cargoMap = new LinkedHashMap();

        cargoMap.put("app.report.dispcomp.loadport", " (SELECT opm.port_name FROM opr_port_master opm WHERE opm.port_id= och.load_port_id) ");
//        cargoMap.put("app.report.dispcomp.vessel", "vessel_name");
//        cargoMap.put("app.report.dispcomp.commodity", "commodity");
//        cargoMap.put("app.report.dispcomp.agent", "agent");
        cargoMap.put("app.report.dispcomp.disport", " (SELECT opm.port_name FROM opr_port_master opm WHERE opm.port_id= och.dis_port_id) ");
//        cargoMap.put("app.report.dispcomp.owner", "owner");

//        cargoMap.put("app.report.dispcomp.eta", "DATE_FORMAT(eta,'%e-%c')");
//        cargoMap.put("app.report.dispcomp.etb", "DATE_FORMAT(etb,'%e-%c')");
//        cargoMap.put("app.report.dispcomp.ets", "DATE_FORMAT(ets,'%e-%c')");
        cargoMap.put("app.report.dispcomp.charterer", " och.charterers ");
        cargoMap.put("app.report.dispcomp.shiper", " ocd.shipper ");
        cargoMap.put("app.report.dispcomp.receiver", " ocd.receiver ");
        cargoMap.put("app.report.dispcomp.grade", " ocd.grade ");
        cargoMap.put("app.report.dispcomp.manqty", " ocd.man_qty ");
        cargoMap.put("app.report.dispcomp.mechqty", " ocd.mech_qty ");
        cargoMap.put("app.report.dispcomp.anchqty", " ocd.anc_qty ");
        cargoMap.put("app.report.dispcomp.usprice", " ocd.price ");
        cargoMap.put("app.report.dispcomp.fob", " ocd.fob ");
        cargoMap.put("app.report.dispcomp.igm", " och.igm_no ");
        cargoMap.put("app.report.dispcomp.egm", "  och.egm_no ");
        cargoMap.put("app.report.dispcomp.cha", " ocd.cha ");
        return cargoMap;
    }

    // Method to create Vessel Position map for Vessel position display components
    private Map createVesselPositionDispCompMap() {

        Map vpMap = new LinkedHashMap();

        //vpMap.put("app.report.dispcomp.port", "port_code");
        vpMap.put("app.report.dispcomp.jetty", " ovp.jetty");
        vpMap.put("app.report.dispcomp.vessel", " (SELECT ovm.vessel_name FROM opr_vessel_master ovm WHERE ovm.vessel_id= ovp.vessel_id)");
        vpMap.put("app.report.dispcomp.ops", " CASE ovp.ops WHEN '1' THEN 'L' WHEN '2' THEN 'D'  ELSE 'L/D' END");
        vpMap.put("app.report.dispcomp.cargo", "ovp.cargo");
        vpMap.put("app.report.dispcomp.qty", " (ovp.qty)");
        vpMap.put("app.report.dispcomp.eta", " CASE ovp.vp_status WHEN '1' THEN DATE_FORMAT(ovp.eta,'%e-%c') ELSE DATE_FORMAT(ovp.arrived,'%e-%c') END");
        vpMap.put("app.report.dispcomp.etb", " CASE ovp.vp_status WHEN '1' OR '2' THEN DATE_FORMAT(ovp.etb,'%e-%c') ELSE IFNULL(DATE_FORMAT(ovp.berthed,'%e-%c'),DATE_FORMAT(ovp.etb,'%e-%c')) END");
        vpMap.put("app.report.dispcomp.ets", " CASE WHEN ovp.vp_status >= '7' THEN DATE_FORMAT(ovp.sailed,'%e-%c') ELSE DATE_FORMAT(ovp.ets,'%e-%c') END");
        vpMap.put("app.report.dispcomp.npoc", " (SELECT opm.port_name FROM opr_port_master opm WHERE opm.port_id = ovp.npoc)");
        vpMap.put("app.report.dispcomp.agent", " ovp.agent_name");
        vpMap.put("app.report.dispcomp.status", " CASE ovp.vp_status WHEN '1' THEN 'Expected' WHEN '2' THEN  'Arrieved' WHEN '3' THEN 'Waiting' WHEN '4' THEN 'Berthed' WHEN '5' THEN 'Shifted' WHEN '6' THEN 'Reberthed' WHEN '7' THEN 'Sailed' WHEN '8' THEN 'Vaccant' WHEN '9' THEN 'Occupied' WHEN '10' THEN 'Decommisioned' WHEN '11' THEN 'Discharged' END");

//        vpMap.put("app.report.dispcomp.remark", "Remark");
        return vpMap;
    }

    // Method to put the loaded Map key & values
    private void putLoadedMap(Map map) {

        Set keys = map.keySet();
        int count = 0;
        String parameter = "";
        for (Iterator i = keys.iterator(); i.hasNext();) {
            if (count < 9) {
                parameter = "1";
            } else {
                parameter = "2";
            }
            String key = (String) i.next();
            String value = (String) map.get(key);
            createDispCompItem(key, value, parameter);
            count++;
        }
    }

    // Method to create List Item for display components
    private void createDispCompItem(String key, String value, String parameter) {

        Listitem lItem = new Listitem();
        Listcell lCell = new Listcell();
        lCell.setLabel(Labels.getLabel(key));
        lCell.setValue(value);
        lItem.setValue(value);
//        lItem.setAttribute("PARAMETER", parameter);
        lCell.setParent(lItem);
        lItem.setParent(lBoxDispComp);
        lItem.setDraggable("true");
        lItem.setDroppable("true");
        lItem.addForward("onDrop", self, "onItemMove");
    }

    // Method to reurn integer value of reportType string
    private int getReportTypeIntVal() {
        return Integer.parseInt(reportType);
    }

    // Method to validate the required fields
    private boolean validateFields() {
        boolean flag = true;

        WrongValueException e = ComponentValidation.validateManditoryTextbox(dbFrom, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag = false;
        }
        e = ComponentValidation.validateManditoryTextbox(dbTo, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag = false;
        }
        return flag;
    }

    // Method to generate the dyna report
    private void generateDynaReport() {
        if (validateFields()) {// Check for Validation
            Map reportParamMap = new HashMap();
            String strMainQueryText = "";
            isNewReport = rGpRepVersion.getSelectedItem().getValue().toString().contains("New") ? true : false;

            System.out.println("is New report  :" + isNewReport);
            if (reportType.equalsIgnoreCase(Constants.DRYCARGO_ENTRY) && !isNewReport) {   ///Dry cargo Old
                System.out.println("Con   -----1");
                StringBuffer sbuff = new StringBuffer();
                getWhereComponents(sbuff);
                strMainQueryText = sbuff.toString();
            } else if (reportType.equalsIgnoreCase(Constants.LIQUIDCARGO_ENTRY) && !isNewReport) {  ///Liquid cargo Old
                System.out.println("Con   -----2");
                StringBuffer sbuff = new StringBuffer();
                getWhereComponents(sbuff);
                strMainQueryText = sbuff.toString();
            } else if (!reportType.equalsIgnoreCase(Constants.DRYCARGO_ENTRY) && !reportType.equalsIgnoreCase(Constants.LIQUIDCARGO_ENTRY) && isNewReport) {  //// VP new report
                System.out.println("Con   -----3");
                StringBuffer sbuff = new StringBuffer();
                getWhereComponents(sbuff);
                strMainQueryText = sbuff.toString();
                reportParamMap.put("p_SubQuery", createQueryText(reportParamMap));
            } else if (!reportType.equalsIgnoreCase(Constants.DRYCARGO_ENTRY) && !reportType.equalsIgnoreCase(Constants.LIQUIDCARGO_ENTRY) && !isNewReport) {   //// Vp old report
                System.out.println("Con   -----4");
                StringBuffer sbuff = new StringBuffer();
                StringBuffer srbuff = new StringBuffer();
                StringBuffer partialBuffer2 = new StringBuffer();
                getWhereComponents(sbuff);
                strMainQueryText = sbuff.toString();
                getWhereComponentsForSubReport(srbuff);
                getWhereComponentForPartialQry2(partialBuffer2);
                reportParamMap.put("PARTIAL_QUERY_1", srbuff.toString());
                reportParamMap.put("PARTIAL_QUERY_2", partialBuffer2.toString());
            } else if ((reportType.equalsIgnoreCase(Constants.DRYCARGO_ENTRY) || reportType.equalsIgnoreCase(Constants.LIQUIDCARGO_ENTRY)) && isNewReport) {  ///cargo old
                System.out.println("Con   -----5");
                StringBuffer sbuff = new StringBuffer();
                getWhereComponents(sbuff);
                strMainQueryText = sbuff.toString();
                reportParamMap.put("p_SubQuery", createQueryText(reportParamMap));
            } else {                                                                                        /////else
                System.out.println("Con   -----6");
                strMainQueryText = createQueryText(reportParamMap);
            }
            //String strPartialQueryText = createPartialQueryText(reportParamMap);
            String strSubRepPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath(Labels.getLabel("app.report.path"));
            String strImagePath = Executions.getCurrent().getDesktop().getWebApp().getRealPath(Labels.getLabel("app.report.imagepath"));
            reportParamMap.put("PARTIAL_QUERY", strMainQueryText);
            reportParamMap.put("title", reportNameLabel.getValue());
            reportParamMap.put("SUBREPORT_DIR", strSubRepPath + System.getProperty("file.separator"));
            reportParamMap.put("p_image", strImagePath);
            reportParamMap.put("fromDt", dbFrom.getText());
            reportParamMap.put("toDt", dbTo.getText());
            reportParamMap.put("p_vp_type", reportType);
            System.out.println("REPORT PARAM MAP > " + reportParamMap);
            System.out.println("JASPER FILE NAME >>> " + getJasperFileName());
            String repStateVal = rGpRepState.getSelectedItem().getValue();
            //String repVersionVal = rGpRepVersion.getSelectedItem().getValue();
            CommonOperation.beginTransaction();
            iFrameReport.setContent(null);
            if (repStateVal == null || repStateVal.equals("PDF")) {
                iFrameReport.setContent(ReportGenerator.generatePDF(Labels.getLabel("app.report.title.outputfilename") + ".pdf", getJasperFileName(), reportParamMap));
            } //else if(repStateVal.equals("Excel"))
            else {
                iFrameReport.setContent(ReportGenerator.generateExcel(Labels.getLabel("app.report.title.outputfilename") + ".xls", getJasperFileName(), reportParamMap));
            }
            CommonOperation.commitTransaction();
        }
    }

    // Method to generate the Query text depending on condition
    private String createQueryText(Map paramMap) {
        StringBuffer sbQuery = new StringBuffer();
        sbQuery.append("");   // Initialise with 'Select'
        getSelectedComponents(sbQuery, paramMap); // get the Selected Componenents
        getFromComponents(sbQuery);
        getWhereComponents(sbQuery);
        System.out.println("Query String " + sbQuery);
        return sbQuery.toString();
    }

    private void getSelectedComponents(StringBuffer sbQuery, Map paramMap) {
        List<Listitem> dispCompItemList = lBoxDispComp.getItems();
        boolean isDispCompActive = false;
        int fieldCntr = 0;
        int iReportType = getReportTypeIntVal();
        // Object parameter = "";
        if ((iReportType <= 2 && isNewReport) || (isNewReport && iReportType > 2)) { //for VP new Report's
            sbQuery.append("SELECT ");
        }
        for (Listitem lItem : dispCompItemList) {
            if (lItem.isSelected()) {
                isDispCompActive = true;
                sbQuery.append(lItem.getValue()).append(" field").append(++fieldCntr).append(", ");
                paramMap.put("param" + (fieldCntr), lItem.getLabel());
            }
        }
        if (isDispCompActive) { // if components selected manually (Dynamic columns) 
            for (int i = fieldCntr + 1; i < (20); i++) {    // the loop iterate up to 19nd column
                sbQuery.append("'' field").append(i).append(", ");
                paramMap.put("param" + (i), "");
            }
            if (iReportType > 2) {
                sbQuery.append(" ovp.port_id ");
            } else {
                sbQuery.append(" och.cargo_hdr_id ");
            }
        } else { // if components not selected then go for default (static columns)
            if (iReportType <= 2) { // Cargo query
                for (Listitem lItem : dispCompItemList) {
                    sbQuery.append(lItem.getValue()).append(" field").append(++fieldCntr).append(", ");
                    paramMap.put("param" + (fieldCntr), lItem.getLabel());
                }
                sbQuery.append(" och.cargo_hdr_id ");
            } else {
                if (isNewReport) {// VP query
                    for (Listitem lItem : dispCompItemList) {
                        sbQuery.append(lItem.getValue()).append(" field").append(++fieldCntr).append(", ");
                        paramMap.put("param" + (fieldCntr), lItem.getLabel());
                    }
                    sbQuery.append(" ovp.port_id ");
                }
            }
        }
    }

    // Method to get the table names that are used in query
    private void getFromComponents(StringBuffer sbQuery) {
        String partialCargoQuery = " FROM opr_cargo_hdr as och LEFT JOIN opr_cargo_dtl as ocd ON och.cargo_hdr_id = ocd.cargo_hdr_id, opr_vessel_position ovp where ";
        String partialVpQuery = "";
        int iReportType = getReportTypeIntVal();
        if (isNewReport && iReportType > 2) {
            partialVpQuery = "FROM opr_vessel_position ovp where ";
        }
        if (getReportTypeIntVal() <= 2) { // Cargo query
            sbQuery.append(partialCargoQuery);
        } else { // VP query
            sbQuery.append(partialVpQuery);
        }
    }

    // Method to get the criteria by using which fire the query
    public void getWhereComponents(StringBuffer sbQuery) {
        StringBuilder sbWhereTemp = new StringBuilder();
        if (getReportTypeIntVal() <= 2) { // Cargo query
//            sbWhereTemp.append(" och.vessel_pos_id = ovp.vessel_pos_id AND och.entry_status='1' AND ovp.entry_status='1' AND  och.cargo_type='").append(reportType).append("' AND ( (DATE_FORMAT(och.created_date,'%Y-%m-%d') BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("')  OR (DATE_FORMAT(och.updated_date,'%Y-%m-%d') BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("' ))");//AND ((och.load_port_id = "+);
            sbWhereTemp.append(" och.vessel_pos_id = ovp.vessel_pos_id AND och.entry_status='1' AND ovp.entry_status='1' AND  och.cargo_type='").append(reportType).append("' AND ((    DATE(och.created_date) <= '"+getFromDate()+"' AND DATE(och.updated_date) >= '"+getToDate()+"') OR (    DATE(och.created_date) BETWEEN '"+getFromDate()+"' AND '"+getToDate()+"' ))");//AND ((och.load_port_id = "+);
            // Check Port
            if (cmbPort.getSelectedIndex() >= 0) {
                String qPortId = ((OprPortMaster) cmbPort.getSelectedItem().getValue()).getPortId();//
                sbWhereTemp.append(" and (och.load_port_id = '").append(qPortId).append("')");
            }
            // Check Discharge Port
            if (cmbDischargePort.getSelectedIndex() >= 0) {
                String qDisPortId = ((OprPortMaster) cmbDischargePort.getSelectedItem().getValue()).getPortId();
                sbWhereTemp.append(" AND (och.dis_port_id ='").append(qDisPortId).append("' )");
            }
            // Check Vessel
            if (!txtVessel.getText().trim().equals("")) {
                sbWhereTemp.append(" AND (ovp.vessel_id ='").append(((OprVesselMaster) txtVessel.getAttribute("VESSEL_OBJECT")).getVesselId()).append("' )");
            }
            // Check Commodity(Cargo)
            if (txtCargo.getText().trim().length() > 0) {
                sbWhereTemp.append(" AND ovp.cargo  like '").append(txtCargo.getText()).append("%' ");
            }

            // Check Agent
            if (txtAgent.getText().trim().length() > 0) {
                sbWhereTemp.append(" AND ovp.agent_name like '").append(txtAgent.getText()).append("%' ");
            }
            // Check Owner
            if (txtOwner.getText().trim().length() > 0) {
                sbWhereTemp.append(" and ovp.owner like '").append(txtOwner.getText()).append("%' ");
            }
            // Check Charterer
            if (txtCharterer.getText().trim().length() > 0) {
                sbWhereTemp.append(" and och.charterers like '").append(txtCharterer.getText()).append("%' ");
            }
            // Check Shipper
            if (txtShipper.getText().trim().length() > 0) {
                sbWhereTemp.append(" and ocd.shipper like '").append(txtShipper.getText()).append("%' ");
            }
            // Check Receiver
            if (txtReceiver.getText().trim().length() > 0) {
                sbWhereTemp.append(" and ocd.receiver like '").append(txtReceiver.getText()).append("%' ");
            }
            if (!loggedUser.getOprBranchMaster().getBranchId().equalsIgnoreCase("1")) {
                System.out.println("not main Office user");
                sbWhereTemp.append(" AND (SELECT scm.branch_id FROM scr_user_master scm WHERE scm.user_id=och.created_by)='").append(loggedUser.getOprBranchMaster().getBranchId()).append("'");
               
            } else {
                System.out.println("main Office user");
            }
            sbWhereTemp.append(" order by ocd.created_date");
        } else { // VP query                   
//            sbWhereTemp.append(" ovp.entry_status='1'").append(" AND ovp.vp_type='").append(reportType).append("' ").append(" AND ((ovp.updated_date BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("') OR (ovp.created_date BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("') )");
            sbWhereTemp.append(" ovp.entry_status='1'").append(" AND ovp.vp_type='").append(reportType).append("' AND ");
            if (isNewReport) {
                System.out.println("************************");
//                sbWhereTemp.append(" OR (ovp.ets BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("') OR (ovp.eta BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("') OR (ovp.etb BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("'))");
                sbWhereTemp.append(" ((    DATE(ovp.created_date) <= '"+getFromDate()+"' AND DATE(ovp.updated_date) >= '"+getToDate()+"') OR (    DATE(ovp.created_date) BETWEEN '"+getFromDate()+"' AND '"+getToDate()+"' )) ");
            } else {
                System.out.println("___________________________________________2");
//                sbWhereTemp.append(" AND ovp.vp_status NOT IN( '1','3')");
//                sbWhereTemp.append("(ovp.vp_status NOT IN ('1', '3', '7') OR (((ovp.created_date <= '"+getFromDate()+"' AND ovp.updated_date >= '"+getToDate()+"') OR (ovp.created_date BETWEEN '"+getFromDate()+"' AND '"+getToDate()+"' )) AND ovp.vp_status = '7')  )");
                sbWhereTemp.append(" ovp.vp_status NOT IN ('1', '2', '3','5','7') AND  ((  DATE(ovp.created_date) <= '"+getFromDate()+"' AND DATE(ovp.updated_date) >= '"+getToDate()+"') OR (   DATE(ovp.created_date) BETWEEN '"+getFromDate()+"' AND '"+getToDate()+"' )) ");
            }
            if (cmbPort.getSelectedIndex() >= 0) {
                System.out.println("___________________________________________3");
                String qPortId = ((OprPortMaster) cmbPort.getSelectedItem().getValue()).getPortId();
                sbWhereTemp.append(" AND (ovp.port_id ='").append(qPortId).append("')");
            }
            // Check Vessel
            if (!txtVessel.getText().trim().equals("")) {
                sbWhereTemp.append(" AND (ovp.vessel_id ='").append(((OprVesselMaster) txtVessel.getAttribute("VESSEL_OBJECT")).getVesselId()).append("')");
            }
            // Check Commodity(Cargo)
            if (txtCargo.getText().trim().length() > 0) {
                sbWhereTemp.append(" AND    (ovp.cargo  like '").append(txtCargo.getText()).append("%')");
            }
            // Check Agent
            if (txtAgent.getText().trim().length() > 0) {
                sbWhereTemp.append(" AND    (ovp.agent_name like '").append(txtAgent.getText()).append("%')");
            }
            if (!loggedUser.getOprBranchMaster().getBranchId().equalsIgnoreCase("1")) {
                System.out.println("not main Office user");
//                sbWhereTemp.append(" AND (SELECT scm.branch_id FROM scr_user_master scm WHERE scm.user_id=ovp.created_by)='").append(loggedUser.getOprBranchMaster().getBranchId()).append("'");
                 sbWhereTemp.append(" AND ((SELECT scm.branch_id FROM scr_user_master scm WHERE scm.user_id=ovp.created_by)='").append(loggedUser.getOprBranchMaster().getBranchId()).append("' OR ").append("(SELECT scm.branch_id FROM scr_user_master scm WHERE scm.user_id=ovp.updated_by)='").append(loggedUser.getOprBranchMaster().getBranchId()).append("')");
            } else {
                System.out.println("main Office user");
            }
        }
        String replacedStr = sbQuery.toString().replace("$", sbWhereTemp.toString());
        sbQuery.replace(0, sbQuery.length(), replacedStr);

        sbQuery.append(sbWhereTemp.toString());
    }


    public void getWhereComponentForPartialQry2(StringBuffer sbQuery) {
        StringBuilder sbWhereTemp = new StringBuilder();
         // VP query
//            sbWhereTemp.append(" ovp.entry_status='1'").append(" AND ovp.vp_type='").append(reportType).append("' ").append(" AND ((ovp.updated_date BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("') OR (ovp.created_date BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("') )");

                sbWhereTemp.append(" (( DATE(ovp.created_date) <= '"+getFromDate()+"' AND DATE(ovp.updated_date) >= '"+getToDate()+"') OR (   DATE(ovp.created_date) BETWEEN '"+getFromDate()+"' AND '"+getToDate()+"' )) ");



        String replacedStr = sbQuery.toString().replace("$", sbWhereTemp.toString());
        sbQuery.replace(0, sbQuery.length(), replacedStr);

        sbQuery.append(sbWhereTemp.toString());
    }

    private void getWhereComponentsForSubReport(StringBuffer sbQuery) {
        StringBuilder sbWhereTemp = new StringBuilder();

        //sbWhereTemp.append(" ovp.entry_status='1'").append(" AND ovp.vp_type='").append(reportType).append("' ").append(" AND ((ovp.sailed BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("') OR (ovp.arrived BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("') OR (ovp.berthed BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("') ");
//        sbWhereTemp.append(" ovp.entry_status='1'").append(" AND ovp.vp_type='").append(reportType).append("' ").append(" AND ((ovp.updated_date BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("') OR (ovp.created_date BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("') )");

//        sbWhereTemp.append(" ovp.entry_status='1'").append(" AND ovp.vp_type='").append(reportType).append("' ");//.append(" AND ").append("(ovp.vp_status NOT IN( '1','3','7') OR ( (ovp.created_date <= '"+getFromDate()+"' AND    ovp.updated_date >= '"+getToDate()+"') AND ovp.vp_status='7')) ");
//        sbWhereTemp.append(" ovp.entry_status='1'").append(" AND ovp.vp_type='").append(reportType).append("' AND (ovp.vp_status NOT IN ('1', '3', '7') OR (((ovp.created_date <= '"+getFromDate()+"' AND ovp.updated_date >= '"+getToDate()+"') OR (ovp.created_date BETWEEN '"+getFromDate()+"' AND '"+getToDate()+"' )) AND ovp.vp_status = '7')  )");
//        sbWhereTemp.append(" ovp.entry_status='1'").append(" AND ovp.vp_type='").append(reportType).append("' AND ovp.vp_status NOT IN ('1', '3') AND  ((ovp.created_date <= '"+getFromDate()+"' AND ovp.updated_date >= '"+getToDate()+"') OR (ovp.created_date BETWEEN '"+getFromDate()+"' AND '"+getToDate()+"' )) ");
        sbWhereTemp.append(" ovp.entry_status='1'").append(" AND ovp.vp_type='").append(reportType).append("' AND  (( DATE(ovp.created_date) <= '"+getFromDate()+"' AND DATE(ovp.updated_date) >= '"+getToDate()+"') OR (   DATE(ovp.created_date) BETWEEN '"+getFromDate()+"' AND '"+getToDate()+"' )) ");

        System.out.println("________________________________________000");
        if (cmbPort.getSelectedIndex() >= 0) {
            System.out.println("________________________________________111");
            String qPortId = ((OprPortMaster) cmbPort.getSelectedItem().getValue()).getPortId();
            sbWhereTemp.append(" AND (ovp.port_id ='").append(qPortId).append("')");
        }
        // Check Vessel
        if (!txtVessel.getText().trim().equals("")) {
            System.out.println("________________________________________222");
            sbWhereTemp.append(" AND (ovp.vessel_id ='").append(((OprVesselMaster) txtVessel.getAttribute("VESSEL_OBJECT")).getVesselId()).append("')");
        }

        // Check Commodity(Cargo)
        if (txtCargo.getText().trim().length() > 0) {
            System.out.println("________________________________________333");
            sbWhereTemp.append(" AND    (ovp.cargo  like '").append(txtCargo.getText()).append("%')");
        }

        // Check Agent
        if (txtAgent.getText().trim().length() > 0) {
            System.out.println("________________________________________444");
            sbWhereTemp.append(" AND    (ovp.agent_name like '").append(txtAgent.getText()).append("%')");
        }

        if (!loggedUser.getOprBranchMaster().getBranchId().equalsIgnoreCase("1")) {
            System.out.println("________________________________________555");
            System.out.println("not main Office user");
            sbWhereTemp.append(" AND (SELECT scm.branch_id FROM scr_user_master scm WHERE scm.user_id=ovp.created_by)='").append(loggedUser.getOprBranchMaster().getBranchId()).append("' ");
        } else {
            System.out.println("main Office user");
        }

        if (isNewReport) {
            System.out.println("************************");
//                sbWhereTemp.append(" OR (ovp.ets BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("') OR (ovp.eta BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("') OR (ovp.etb BETWEEN '").append(getFromDate()).append("' AND '").append(getToDate()).append("'))");
        } else if (reportType.equalsIgnoreCase(Constants.VP_WESTDRY_ENTRY)) {

            sbWhereTemp.append(" AND ovp.port_id=opm.port_id ORDER BY opm.DW_seq");
        } else if (reportType.equalsIgnoreCase(Constants.VP_EASTDRY_ENTRY)) {

            sbWhereTemp.append(" AND ovp.port_id=opm.port_id ORDER BY opm.DE_seq");
        } else if (reportType.equalsIgnoreCase(Constants.VP_WESTLIQUID_ENTRY)) {

            sbWhereTemp.append(" AND ovp.port_id=opm.port_id ORDER BY opm.LW_seq");
        } else if (reportType.equalsIgnoreCase(Constants.VP_EASTLIQUID_ENTRY)) {

            sbWhereTemp.append(" AND ovp.port_id=opm.port_id ORDER BY opm.LE_seq");
        }

        String replacedStr = sbQuery.toString().replace("$", sbWhereTemp.toString());
        sbQuery.replace(0, sbQuery.length(), replacedStr);

        sbQuery.append(sbWhereTemp.toString());
    }

    // Method to return the from date(String) with default time
    private String getFromDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fromDt = sdf.format(dbFrom.getValue());
//        fromDt = fromDt + " " + "00:00:00";
        return fromDt;
    }

    // Method to return the to date(String) with default time
    private String getToDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String toDt = sdf.format(dbTo.getValue());
//        toDt = toDt + " " + "23:23:59";
        return toDt;
    }

    // Method to get the jasper file according to condition
    private String getJasperFileName() {

        boolean isDispCompActive = false;
        boolean isExcelReport = rGpRepState.getSelectedItem().getValue().toString().equalsIgnoreCase("PDF") ? false : true;
        List<Listitem> dispCompItemList = lBoxDispComp.getItems();
        for (Listitem lItem : dispCompItemList) {
            if (lItem.isSelected()) {
                isDispCompActive = true;
                break;
            }
        }
        int iReportType = getReportTypeIntVal(); // get Report Type
        System.out.println("iReportType   ::::::::::: " + iReportType);
        String strJasperFile = "";
        if ((iReportType <= 2) && isNewReport)                  // Dynamic Cargo
        {
            System.out.println("Report Name Number : 1");
            strJasperFile = Labels.getLabel("app.report.jasperpath.newCargoReport");
        } else if ((iReportType == 1) && !isNewReport)          // Old Dry Cargo
        {
            System.out.println("Report Name Number : 2");
            strJasperFile = Labels.getLabel("app.report.jasperpath.oldCargoDry");
        } else if ((iReportType == 2) && !isNewReport)          // Old Liq Cargo
        {
            System.out.println("Report Name Number : 3");
            strJasperFile = Labels.getLabel("app.report.jasperpath.oldCargoLiq");
        } else if ((iReportType > 2) && isNewReport)            // Dynamic Vessel Position
        {
            System.out.println("Report Name Number : 4");
            strJasperFile = Labels.getLabel("app.report.jasperpath.dynavp");
        } else if ((iReportType > 2) && isExcelReport)          // Static Vessel Position
        {
            System.out.println("Report Name Number : 5");
            strJasperFile = Labels.getLabel("app.report.jasperpath.oldvpxl");
        } else if ((iReportType > 2) && !isExcelReport) {
            System.out.println("Report Name Number : 6");
            strJasperFile = Labels.getLabel("app.report.jasperpath.oldvppdf");
        }

        return strJasperFile;
    }

    private void setReportEnvornmentToScreen() {

        if (reportType.equalsIgnoreCase(Constants.DRYCARGO_ENTRY) || reportType.equalsIgnoreCase(Constants.LIQUIDCARGO_ENTRY)) {
            generateCargoParams();
            generateCargoDispComp();
        } else {
            generateVesselPostionParams();
            generateVesselPositionDispComp();
            isNewReport = rGpRepVersion.getSelectedItem().getValue().toString().contains("New") ? true : false;
        }
    }

    private void getReportName() {
        String screen = (String) winSingleReport.getDesktop().getAttribute("screen");
        System.out.println("Report Type >>>>> " + screen);
        if (screen.contains("GEN_REPORT")) {
            setGeneralReportsFormContaints();
        } else {
            setSingleReportFormContaints(screen);
        }
        setReportEnvornmentToScreen();
    }

    private void setGeneralReportsFormContaints() {
        reportNameLabel.setVisible(false);
        gbReportType.setVisible(true);
        reportType = Constants.DRYCARGO_ENTRY;
        generateCargoParams();
        generateCargoDispComp();

    }

    private void setSingleReportFormContaints(String screen) {
        reportNameLabel.setVisible(true);
        gbReportType.setVisible(false);
        if (screen.contains("DRY_CARGO_REPORT")) {
            reportType = Constants.DRYCARGO_ENTRY;
            reportNameLabel.setValue(Labels.getLabel("app.report.title.drycargoreport"));
        } else if (screen.contains("LIQUID_CARGO_REPORT")) {
            reportType = Constants.LIQUIDCARGO_ENTRY;
            reportNameLabel.setValue(Labels.getLabel("app.report.title.liquidcargoreport"));
        } else if (screen.contains("VP_EAST_DRY_REPORT")) {
            reportType = Constants.VP_EASTDRY_ENTRY;
            reportNameLabel.setValue(Labels.getLabel("app.report.title.vpeastdryreport"));
        } else if (screen.contains("VP_WEST_DRY_REPORT")) {
            reportType = Constants.VP_WESTDRY_ENTRY;
            reportNameLabel.setValue(Labels.getLabel("app.report.title.vpwestdryreport"));
        } else if (screen.contains("VP_EAST_LIQUID_REPORT")) {
            reportType = Constants.VP_EASTLIQUID_ENTRY;
            reportNameLabel.setValue(Labels.getLabel("app.report.title.vpeastliquidreport"));
        } else if (screen.contains("VP_WEST_LIQUID_REPORT")) {
            reportType = Constants.VP_WESTLIQUID_ENTRY;
            reportNameLabel.setValue(Labels.getLabel("app.report.title.vpwestliquidreport"));
        }
    }

    public void onCheck$rdDryCargo() {
        reportType = Constants.DRYCARGO_ENTRY;
        generateCargoParams();
        generateCargoDispComp();

    }

    public void onCheck$rdLiquidCargo() {
        reportType = Constants.LIQUIDCARGO_ENTRY;
        generateCargoParams();
        generateCargoDispComp();

    }

    public void onCheck$rdVpEastDry() {
        reportType = Constants.VP_EASTDRY_ENTRY;
        generateVesselPostionParams();
        generateVesselPositionDispComp();

    }

    public void onCheck$rdVpWestDry() {
        reportType = Constants.VP_WESTDRY_ENTRY;
        generateVesselPostionParams();
        generateVesselPositionDispComp();

    }

    public void onCheck$rdVpEastLiquid() {
        reportType = Constants.VP_EASTLIQUID_ENTRY;
        generateVesselPostionParams();
        generateVesselPositionDispComp();

    }

    public void onCheck$rdVpWestLiquid() {
        reportType = Constants.VP_WESTLIQUID_ENTRY;
        generateVesselPostionParams();
        generateVesselPositionDispComp();

    }
}
