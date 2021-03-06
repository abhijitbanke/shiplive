/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.operational.windows;

import com.manikssys.in.common.*;
import com.manikssys.in.operational.beans.OprPortMaster;
import com.manikssys.in.operational.beans.OprRemark;
import com.manikssys.in.operational.beans.OprVesselMaster;
import com.manikssys.in.operational.beans.OprVesselPosition;
import com.manikssys.in.operational.business.IVesselPosition;
//import com.manikssys.in.operational.business.VesselPositionBs;
import com.manikssys.in.operational.business.OprVesselPositionBS;
import com.manikssys.in.security.beans.ScrUserMaster;
import java.text.ParseException;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;
import java.util.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Comboitem;

/**
 *
 * @author pc
 */
public class VesselPositionComposer extends GenericForwardComposer implements ICommonWindowInterface {

    private Combobox cmbPort;
    private boolean isNewRow = false;
    private boolean isNewRecord = false;
    private Grid VesselPositionGrid;
    private Rows VesselPositionRows;
    private Window vesselPositionWin;
    private static Logger log;
    private ScrUserMaster loggedUser;
    private IVesselPosition iVPBs;
    int iRowIndex = 0;
    private List<OprPortMaster> portList;
    private List<CommonStatus> statusList, categoryList, opsList;
    private String entryType = "0";
    private Datebox frmDate, toDate;
    private Textbox vesselName;
    private Label vesselLabel;
    private List<OprVesselMaster> vesselList;
    private List<OprVesselPosition> vpList;
    int currentIndex = -1;
    ToolbarComposer toolBarComposer;

    public void onCreate$vesselPositionWin() {
        System.out.println("on create called");
        Executions.getCurrent().getDesktop().setAttribute("currentWindow", this);
        toolBarComposer = (ToolbarComposer) Executions.getCurrent().getDesktop().getAttribute("commonHBox$composer");
        findEntryType();
        System.out.println("entry type  ==" + entryType);
        log = Logger.getLogger(VesselPositionComposer.class);
        loggedUser = (ScrUserMaster) sessionScope.get("user");
        iVPBs = new OprVesselPositionBS();
        cmbPort.addForward(Events.ON_SELECT, vesselPositionWin, "onGotFocus");
        frmDate.addForward(Events.ON_CHANGE, vesselPositionWin, "onChangeComp");
        toDate.addForward(Events.ON_CHANGE, vesselPositionWin, "onChangeComp");
        vesselName.addForward(Events.ON_CHANGING, vesselPositionWin, "onChangingComp");
        CommonOperation.beginTransaction();
        portList = iVPBs.getPortsList(); // Load all Ports
        vesselList = iVPBs.getVesselsList();
        statusList = getStatusList();
        categoryList = getCategoryList();
        opsList = getOPSList();
        fillPorts(portList, null);
        CommonOperation.commitTransaction();
        addEventsListenerToColumns();
    }

    public void onChangingComp(ForwardEvent event) throws InterruptedException, Exception {
        System.out.println("vessel  :" + vesselLabel.getValue());
        retreivePortVpRecords();
    }

    private void addEventsListenerToColumns() {

        List<Column> colList = VesselPositionGrid.getColumns().getChildren();
        for (int i = 0; i < colList.size(); i++) {
            colList.get(i).addForward(Events.ON_CLICK, vesselPositionWin, "onClickColumn");
        }
    }

    public void fillPorts(List<OprPortMaster> portList, OprPortMaster selectedPort) {
        if (portList.isEmpty()) {
            return;
        }
        if (loggedUser.getOprBranchMaster().getBranchId().equals("1")) {
            for (OprPortMaster portMaster : portList) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(portMaster.getPortName());
                cmbItem.setDescription(portMaster.getPortCode());
                cmbItem.setValue(portMaster);
                cmbItem.setParent(cmbPort);
                if (selectedPort != null) {
                    if (portMaster.getPortId().equalsIgnoreCase(selectedPort.getPortId())) {
                        cmbPort.setSelectedItem(cmbItem);
                    }
                }
            }
        } else {
            System.out.println("User Branch    >>>>> " + loggedUser.getOprBranchMaster().getBranchId() + "\n");
            for (OprPortMaster portMaster : portList) {
                if (loggedUser.getOprBranchMaster().getBranchId().equals(portMaster.getBranchId() == null ? null : portMaster.getBranchId().getBranchId())) {
                    Comboitem cmbItem = new Comboitem();
                    cmbItem.setLabel(portMaster.getPortName());
                    cmbItem.setDescription(portMaster.getPortCode());
                    cmbItem.setValue(portMaster);
                    cmbItem.setParent(cmbPort);
                    if (selectedPort != null) {
                        if (portMaster.getPortId().equalsIgnoreCase(selectedPort.getPortId())) {
                            cmbPort.setSelectedItem(cmbItem);
                        }
                    }
                }
            }
        }
    }

    public List<CommonStatus> getStatusList() {
        List<CommonStatus> status_List = new ArrayList<CommonStatus>();
        status_List.add(new CommonStatus(Constants.EXPECTED_VESSEL, "Expected"));
        status_List.add(new CommonStatus(Constants.ARRIVED_VESSEL, "Arrived"));
        status_List.add(new CommonStatus(Constants.WAITING_VESSEL, "Waiting"));
        status_List.add(new CommonStatus(Constants.BERTHED_VESSEL, "Berthed"));
        status_List.add(new CommonStatus(Constants.OCCUPIED_VESSEL, "Occupied"));
        status_List.add(new CommonStatus(Constants.DISCHARGED_VESSEL, "Discharged"));
        status_List.add(new CommonStatus(Constants.SHIFTED_VESSEL, "Shifted"));
        status_List.add(new CommonStatus(Constants.REBERTHING_VESSEL, "Reberthing"));
        status_List.add(new CommonStatus(Constants.SAILED_VESSEL, "Sailed"));
        status_List.add(new CommonStatus(Constants.VACCANT_VESSEL, "Vaccant"));


//        status_List.add(new CommonStatus(Constants.DECOMMISIONED_VESSEL, "Decommisioned"));
        return status_List;
    }

    public List<CommonStatus> getCategoryList() {
        List<CommonStatus> category_List = new ArrayList<CommonStatus>();
        category_List.add(new CommonStatus(Constants.ASPL_CATEGORY, "ASPL"));
        category_List.add(new CommonStatus(Constants.COSTAL_CATEGORY, "Coastal"));
        category_List.add(new CommonStatus(Constants.OPA_CATEGORY, "OPA"));
        category_List.add(new CommonStatus(Constants.NORMAL_CATEGORY, "Normal"));
        return category_List;
    }

    public List<CommonStatus> getOPSList() {
        List<CommonStatus> ops_List = new ArrayList<CommonStatus>();
        ops_List.add(new CommonStatus(Constants.LOAD_OPERATION, "Loading"));
        ops_List.add(new CommonStatus(Constants.UNLOAD_OPERATION, "Unloading"));
        ops_List.add(new CommonStatus(Constants.LOAD_UNLOAD_OPERATION, "Both"));
        ops_List.add(new CommonStatus(Constants.BLANK, "Blank"));

        return ops_List;
    }

    public void findEntryType() {

        try {
            // Getting the Screen Type
            String screen = (String) vesselPositionWin.getDesktop().getAttribute("screen");
            if (screen.contains("VP_WEST_DRY")) {
                entryType = Constants.VP_WESTDRY_ENTRY;
            } else if (screen.contains("VP_WEST_LIQUID")) {
                entryType = Constants.VP_WESTLIQUID_ENTRY;
            } else if (screen.contains("VP_EAST_DRY")) {
                entryType = Constants.VP_EASTDRY_ENTRY;
            } else if (screen.contains("VP_EAST_LIQUID")) {
                entryType = Constants.VP_EASTLIQUID_ENTRY;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onChange$cmbPort() {
        int index = cmbPort.getSelectedIndex();
        if (index >= 0) {
            System.out.println("onChange$cmbPort()___________");
            removeAllErrorMessages();
            resetGrids();
            CommonOperation.beginTransaction();
            retreivePortVpRecords();
            CommonOperation.commitTransaction();

            cmbPort.setSelectedIndex(index);
            vesselName.setReadonly(false);
            vesselLabel.setValue("");
            vesselName.setText("");
        }
    }

    public void onVPStatusChanging(ForwardEvent event) {
        System.out.println("VP Status Changing....");
        Component comp = event.getOrigin().getTarget();
        Combobox vpStatusCmb = (Combobox) comp;
        Row row = (Row) comp.getParent();
        OprVesselPosition bean = (OprVesselPosition) row.getValue();
        if (vpStatusCmb.getSelectedItem().getValue().toString().equalsIgnoreCase(Constants.DISCHARGED_VESSEL)) {
            Combobox cmbOPS = (Combobox) row.getChildren().get(9);
            if (cmbOPS.getSelectedIndex() > -1 && (cmbOPS.getSelectedItem().getValue().toString().equalsIgnoreCase(Constants.LOAD_UNLOAD_OPERATION) || cmbOPS.getSelectedItem().getValue().toString().equalsIgnoreCase(Constants.UNLOAD_OPERATION))) {
            } else {
                    Messagebox.show("This status reserved for Unloading and Loading-Unloading operation");
                    vpStatusCmb.setText("");
                    List<Comboitem> status_List = vpStatusCmb.getChildren();
                    for (Comboitem item : status_List) {
                        System.out.println("Status    ------" + item.getValue());
                        System.out.println("Bean Status    ------" + bean.getVpStatus());
                        if (item.getValue().toString().equals(bean.getVpStatus())) {
                            System.out.println("Status   in condition ------" + item.getValue());
                            vpStatusCmb.setSelectedItem(item);
                            break;
                        }
                    }
            }
        }
    }

    public void onVesselChangeComp(ForwardEvent event) throws SuspendNotAllowedException, InterruptedException {

        OprVesselMaster vessel = null;
        Textbox vesselName = (Textbox) event.getOrigin().getTarget();
        String lovFile = Labels.getLabel("app.vp.vessellov");
        Window w = (Window) Executions.createComponents(lovFile, null, null);//("WEB-INF"+File.separator+"presentation"+File.separator+"LOV"+File.separator+"CommonVesselLov.zul", null, null);
        w.setAttribute("PARAM_TEXT", vesselName.getText());
        w.setAttribute("OBJECT", null);
        w.setAttribute("width", "400px");
        w.setMaximizable(true);
        w.doModal();
        vessel = w.getAttribute("OBJECT") != null ? (OprVesselMaster) w.getAttribute("OBJECT") : null;
//        System.out.println("VesselName  : " + vessel.getVesselName());
        if (vessel != null) {
            vesselName.setText(vessel.getVesselName());
            ((OprVesselPosition) ((Row) vesselName.getParent()).getValue()).setVessel(vessel);
        } //        else if (((OprVesselPosition) ((Row) vesselName.getParent()).getValue()).getVessel() != null) {
        //            vesselName.setText(((OprVesselPosition) ((Row) vesselName.getParent()).getValue()).getVessel().getVesselName());
        //        }
        else {
            vesselName.setText("");
        }

        if (vesselName.getText().toUpperCase().equalsIgnoreCase("VACANT")) {
            setVacantSettings((Row) vesselName.getParent());
        } else {
            setNonVacantSettings((Row) vesselName.getParent());
        }


    }

    public void onPortChangeComp(ForwardEvent event) throws SuspendNotAllowedException, InterruptedException {

        OprPortMaster port = null;
        Textbox portName = (Textbox) event.getOrigin().getTarget();
        String lovFile = Labels.getLabel("app.vp.portlov");
        Window w = (Window) Executions.createComponents(lovFile, null, null);//("WEB-INF"+File.separator+"presentation"+File.separator+"LOV"+File.separator+"CommonPortLov.zul", null, null);
        w.setAttribute("PARAM_TEXT", portName.getText());
        w.setAttribute("OBJECT", null);
        w.setAttribute("width", "400px");
        w.setMaximizable(true);
        w.doModal();
        port = (OprPortMaster) w.getAttribute("OBJECT");
        if (port != null) {
            portName.setText(port.getPortName());
            ((OprVesselPosition) ((Row) portName.getParent()).getValue()).setNpoc(port);
        } 
        else {
            ((OprVesselPosition) ((Row) portName.getParent()).getValue()).setNpoc(null);
            portName.setText("");
        }

    }

    public void onRowClick(ForwardEvent event) {
        System.out.println("..................onRowClick..................");
        Component target = event.getOrigin().getTarget();
        Row targetRow = (Row) target;
        OprVesselPosition bean = (OprVesselPosition) targetRow.getValue();

        selectCurrentPort(bean.getCurrentPort());


    }

    public void onGotFocus(ForwardEvent event) throws InterruptedException, Exception {
        try {
            OprVesselPosition vpBean = null;
            Component comp = event.getOrigin().getTarget();
            System.out.println("..................onGotFocus................" + comp.getClass().getName());
            if (comp == cmbPort) {
                System.out.println("Port Change Event occure");
            }
            if (comp.getClass().getName().equals("org.zkoss.zul.Button")) {
                Button thisButton = (Button) comp;
                Row row = (Row) comp.getParent();
                vpBean = (OprVesselPosition) row.getValue();
                if (row.getAttribute("IS_NEW_RECORD").equals(true)) {
                    int val = Messagebox.show("Do you want to save?", "Information", Messagebox.YES | Messagebox.NO | Messagebox.CANCEL, Messagebox.QUESTION);
                    if (val == 16) {
                        saveRecord();
                    }
                }
                if (vpBean != null) {
                    Listbox lb = new Listbox();
                    lb.setAttribute("VP_BEAN", vpBean);
                    lb.setWidth("370px");
                    populateRemarkListBox(lb, vpBean);
                    Popup popup = new Popup();
                    popup.setWidth("378px");
                    popup.appendChild(lb);
                    vesselPositionWin.appendChild(popup);
                    popup.open(thisButton);
                }

            }
            if (comp.getClass().getName().equals("org.zkoss.zul.Textbox") && comp.getParent().getClass().getName().equals("org.zkoss.zul.Listcell")) {
                System.out.println("Textbox.....");
                Textbox remarkTxt = (Textbox) comp;
                if (!remarkTxt.getText().isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                    vpBean = (OprVesselPosition) comp.getParent().getParent().getParent().getAttribute("VP_BEAN");
                    System.out.println("vpBean  = ===== === " + vpBean.getVessel().getVesselName());
//                    OprRemark remark = (OprRemark) ((Listitem) ((Listcell) remarkTxt.getParent()).getParent()).getValue();
                    if (vpBean.getRemarkCollection() == null) {
                        System.out.println("............If condition for remark init...................");
                        Set set = new HashSet();
                        set.add(getRemarkItem(remarkTxt, vpBean));
                        vpBean.setRemarkCollection(set);
                    } else {
                        System.out.println(".............else condition for remark init...................");
                        vpBean.getRemarkCollection().add(getRemarkItem(remarkTxt, vpBean));
                    }
                }

            }

        } catch (WrongValueException e) {
            e.printStackTrace();
        }
    }

    public void resetFields() {
        cmbPort.setSelectedIndex(-1);
        cmbPort.setAttribute("entry", null);
        resetGrids();
        currentIndex = -1;
        frmDate.setText("");
        toDate.setText("");
        vesselName.setText("");
        vesselName.setReadonly(true);
        removeAllErrorMessages();
    }

    public void resetGrids() {
        if (isNewRecord) {
            isNewRecord = false;
        }
        VesselPositionRows.getChildren().clear();
//        txtAgent.setText("");
    }

    public void addVesselPositionRow(OprVesselPosition vesselPositionMaster) {
        List<Column> colList = VesselPositionGrid.getColumns().getChildren();
//        System.out.println("Vessel Column width " + colList.get(1).getWidth());
//        System.out.println("add VP Row ......");
        Row row = new Row();

        if (vesselPositionMaster == null && isNewRecord) {

            vesselPositionMaster = new OprVesselPosition();
            vesselPositionMaster.setVpType(entryType);
            vesselPositionMaster.setEntryStatus(Constants.NON_DELETED);
            vesselPositionMaster.setCreatedBy(loggedUser);
            vesselPositionMaster.setCreatedDate(new Date());
            vesselPositionMaster.setCurrentPort((OprPortMaster) cmbPort.getSelectedItem().getValue());
        }
//        System.out.println("Is New Row == " + isNewRecord);
        row.setAttribute("IS_NEW_RECORD", isNewRecord);
        row.setValue(vesselPositionMaster);
        row.setParent(VesselPositionRows);

        createCheckbox(row, colList.get(0).getWidth());
//        createVesselCombobox(row, vesselList, vesselPositionMaster.getVessel(), colList.get(1).getWidth()); // Vessel Name
        createVesselTextbox(row, vesselPositionMaster.getVessel() == null ? "" : vesselPositionMaster.getVessel().getVesselName(), colList.get(1).getWidth());
        createTextbox(row, vesselPositionMaster.getAgentName(), colList.get(2).getWidth()); //Added TextBox for Agent
        createTextbox(row, vesselPositionMaster.getJetty(), colList.get(3).getWidth()); // Jetty
        createTextbox(row, vesselPositionMaster.getCargo(), colList.get(4).getWidth()); // Cargo
        createTextbox(row, vesselPositionMaster.getQty() == null ? "" : vesselPositionMaster.getQty() + "", colList.get(5).getWidth()); // Qty
        createDatebox(row, vesselPositionMaster.getArrived() != null ? vesselPositionMaster.getArrived() : vesselPositionMaster.getEta(), colList.get(6).getWidth()); // ETA/Arrived
        createDatebox(row, vesselPositionMaster.getBerthed() != null ? vesselPositionMaster.getBerthed() : vesselPositionMaster.getEtb(), colList.get(7).getWidth()); // ETB/Berthed
        createDatebox(row, vesselPositionMaster.getSailed() != null ? vesselPositionMaster.getSailed() : vesselPositionMaster.getEts(), colList.get(8).getWidth()); // ETS/Sailed
        createStatusCombobox(row, opsList, vesselPositionMaster.getOps(), colList.get(9).getWidth()); // OPS
//        createPortCombobox(row, portList, vesselPositionMaster.getNpoc(), colList.get(10).getWidth()); // NPOC
        createPortTextbox(row, vesselPositionMaster.getNpoc() == null ? "" : vesselPositionMaster.getNpoc().getPortName(), colList.get(10).getWidth());
        createStatusCombobox(row, categoryList, vesselPositionMaster.getCategory() == null ? Constants.NORMAL_CATEGORY : vesselPositionMaster.getCategory(), colList.get(11).getWidth());
        createVPStatusCombobox(row, statusList, vesselPositionMaster.getVpStatus() == null ? Constants.EXPECTED_VESSEL : vesselPositionMaster.getVpStatus(), colList.get(12).getWidth()); // Status
        createRemarkMenu(row, vesselPositionMaster, colList.get(13).getWidth());
        row.addForward(Events.ON_CLICK, vesselPositionWin, "onRowClick");
        System.out.println("Current Status     ---> " + vesselPositionMaster.getVpStatus());
        if (vesselPositionMaster.getVessel() != null) {
            if (vesselPositionMaster.getVessel().getVesselName().toUpperCase().equalsIgnoreCase("VACANT")) {
                setVacantSettings(row);
            } else {
                setNonVacantSettings(row);
            }
        }
    }

    public void createCheckbox(Row row, String width) {
        Checkbox checkBox = new Checkbox();
        checkBox.addForward(Events.ON_FOCUS, vesselPositionWin, "onGotFocus");
        if (isNewRecord) {
            checkBox.setChecked(true);

        }
        checkBox.setWidth(width);
        row.appendChild(checkBox);
    }

    public void createTextbox(Row row, String value, String width) {
        Textbox txtBox = new Textbox();

        if (value != null) {
            txtBox.setValue(value);
        }
        txtBox.setWidth(width);
        row.appendChild(txtBox);
        txtBox.addForward(Events.ON_FOCUS, vesselPositionWin, "onGotFocus");
        txtBox.addForward(Events.ON_CHANGE, vesselPositionWin, "onChangeComp");
    }

    public void createVesselTextbox(Row row, String value, String width) {
        Textbox txtBox = new Textbox();

        if (value != null) {
            txtBox.setValue(value);
        }
        txtBox.setWidth(width);
        row.appendChild(txtBox);

        txtBox.addForward(Events.ON_CHANGE, vesselPositionWin, "onVesselChangeComp");
    }

    public void createPortTextbox(Row row, String value, String width) {
        Textbox txtBox = new Textbox();

        if (value != null) {
            txtBox.setValue(value);
        }
        txtBox.setWidth(width);
        row.appendChild(txtBox);

        txtBox.addForward(Events.ON_CHANGE, vesselPositionWin, "onPortChangeComp");
    }

    public void createDatebox(Row row, Date value, String width) {
        Datebox dtBox = new Datebox();
        if (value != null) {
            dtBox.setValue(value);
            dtBox.setTooltip(dtBox.getText());
        }
        dtBox.setWidth(width);
        row.appendChild(dtBox);
        dtBox.addForward(Events.ON_FOCUS, vesselPositionWin, "onGotFocus");
    }

    public void createPortCombobox(Row row, List<OprPortMaster> list, OprPortMaster value, String width) {
        Combobox cmbBox = new Combobox();
        cmbBox.setAutodrop(true);
        cmbBox.setWidth(width);
        if (list != null) {
            if (loggedUser.getOprBranchMaster().getBranchId().equals("1")) {
                for (OprPortMaster portMaster : list) {

                    Comboitem cmbItem = new Comboitem();
                    cmbItem.setLabel(portMaster.getPortName());
                    cmbItem.setDescription(portMaster.getPortCode());
                    cmbItem.setValue(portMaster);
                    cmbItem.setParent(cmbBox);
                    if (value != null) {
                        if (portMaster.getPortId().equals(value.getPortId())) {
                            cmbBox.setSelectedItem(cmbItem);
                        }
                    }
                }
            } else {
                System.out.println("User Branch    >>>>> " + loggedUser.getOprBranchMaster().getBranchId() + "\n");

                for (OprPortMaster portMaster : list) {
                    System.out.println("Port Branch    >>>>>>>> " + portMaster.getBranchId().getBranchId());
                    if (loggedUser.getOprBranchMaster().getBranchId().equals(portMaster.getBranchId().getBranchId())) {
                        Comboitem cmbItem = new Comboitem();
                        cmbItem.setLabel(portMaster.getPortName());
                        cmbItem.setDescription(portMaster.getPortCode());
                        cmbItem.setValue(portMaster);
                        cmbItem.setParent(cmbBox);
                        if (value != null) {
                            if (portMaster.getPortId().equals(value.getPortId())) {
                                cmbBox.setSelectedItem(cmbItem);
                            }
                        }
                    }
                }

            }
        }
        row.appendChild(cmbBox);
        cmbBox.addForward(Events.ON_FOCUS, vesselPositionWin, "onGotFocus");
    }

    public void createVesselCombobox(Row row, List<OprVesselMaster> list, OprVesselMaster value, String width) {
        Combobox cmbBox = new Combobox();
        cmbBox.setAutodrop(true);
        cmbBox.setWidth(width);
        if (list != null) {
            for (OprVesselMaster vesselMaster : list) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(vesselMaster.getVesselName());
                cmbItem.setDescription(vesselMaster.getVesselCode());
                cmbItem.setValue(vesselMaster);
                cmbItem.setParent(cmbBox);
                if (value != null) {
                    if (vesselMaster.getVesselId().equals(value.getVesselId())) {
                        cmbBox.setSelectedItem(cmbItem);
                    }
                }
            }
        }
        row.appendChild(cmbBox);
        cmbBox.addForward(Events.ON_FOCUS, vesselPositionWin, "onGotFocus");
    }

    public void createStatusCombobox(Row row, List<CommonStatus> list, String value, String width) {
        Combobox cmbBox = new Combobox();
        cmbBox.setAutodrop(true);
        cmbBox.setWidth(width);
        if (list != null) {
            for (CommonStatus statusMaster : list) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(statusMaster.getStatusName());
//                cmbItem.setDescription(portMaster.getPortName());
                cmbItem.setValue(statusMaster.getStatusID());
                cmbItem.setParent(cmbBox);
                if (value != null) {
                    if (statusMaster.getStatusID().equals(value)) {
                        cmbBox.setSelectedItem(cmbItem);
                    }
                }
            }
            if (value == null) {

                cmbBox.setSelectedIndex(-1);

            }
        }
        row.appendChild(cmbBox);
        cmbBox.addForward(Events.ON_FOCUS, vesselPositionWin, "onGotFocus");
    }

    public void createVPStatusCombobox(Row row, List<CommonStatus> list, String value, String width) {
        Combobox cmbBox = new Combobox();
        cmbBox.setAutodrop(true);
        cmbBox.setWidth(width);
        //cmbBox.addForward(Events.ON_CHANGING, vesselPositionWin, "onVPStatusChanging");
        cmbBox.setAttribute("COMBO_NAME", "VP_STATUS");
        if (list != null) {
            for (CommonStatus statusMaster : list) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(statusMaster.getStatusName());
//                cmbItem.setDescription(portMaster.getPortName());
                cmbItem.setValue(statusMaster.getStatusID());
                cmbItem.setParent(cmbBox);

                if (value != null) {
                    if (statusMaster.getStatusID().equals(value)) {
                        cmbBox.setSelectedItem(cmbItem);
                    }
                }
            }
        }
        row.appendChild(cmbBox);
        cmbBox.addForward(Events.ON_CHANGE, vesselPositionWin, "onVPStatusChanging");
    }

    public void clearScreeen() {
        resetFields();
    }

    public void saveRecord() throws Exception {

        if (validateFields()) {

            Date today = new Date();
//            java.util.Calendar cal = new GregorianCalendar();
//            cal.setTime(today);
//            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
//            cal.set(java.util.Calendar.MINUTE, 0);
//            cal.set(java.util.Calendar.SECOND, 1);
//            today = cal.getTime();

            //Date today = df.parse( (new Date()).toString());
            System.out.println("today  " + today);
            List<Row> vpRowsList = VesselPositionRows.getChildren();
            for (Row row : vpRowsList) {
//                if (((Checkbox) row.getChildren().get(0)).isChecked()) {
                String statusValue = ((Combobox) row.getChildren().get(12)).getSelectedItem().getValue().toString();
                //System.out.println(" row Atribute " + row.getAttribute("IS_NEW_RECORD"));
                if (row.getAttribute("IS_NEW_RECORD").equals(true)) {
                    System.out.println("....................Inserting VP..................");
                    System.out.println("date box value      " + ((Datebox) row.getChildren().get(8)).getValue());
                    OprVesselPosition newRecord = (OprVesselPosition) row.getValue();
                    try {
//                            newRecord.setVessel((OprVesselMaster) ((Textbox) row.getChildren().get(1)).getText());
                        newRecord.setAgentName(((Textbox) row.getChildren().get(2)).getText());
                        newRecord.setJetty(((Textbox) row.getChildren().get(3)).getText());
                        newRecord.setCargo(((Textbox) row.getChildren().get(4)).getText());
                        newRecord.setQty(((Textbox) row.getChildren().get(5)).getText());

                        System.out.println("Status updated........to higher");
                        if (statusValue.equalsIgnoreCase(Constants.ARRIVED_VESSEL)) {
                            System.out.println("Arrived date updated");
                            newRecord.setArrived(((Datebox) row.getChildren().get(6)).getValue());
                            newRecord.setBerthed(null);
                            newRecord.setSailed(null);
                        } else if (statusValue.equalsIgnoreCase(Constants.BERTHED_VESSEL)
                                || statusValue.equalsIgnoreCase(Constants.SHIFTED_VESSEL)
                                || statusValue.equalsIgnoreCase(Constants.REBERTHING_VESSEL)) {
                            System.out.println("Berthed date updated");
                            newRecord.setArrived(((Datebox) row.getChildren().get(6)).getValue());
                            newRecord.setBerthed(((Datebox) row.getChildren().get(7)).getValue());


                            newRecord.setSailed(null);
                        } else if (statusValue.equalsIgnoreCase(Constants.SAILED_VESSEL)) {
                            System.out.println("Sailed date updated");
                            newRecord.setArrived(((Datebox) row.getChildren().get(6)).getValue());
                            newRecord.setBerthed(((Datebox) row.getChildren().get(7)).getValue());
                            newRecord.setSailed(((Datebox) row.getChildren().get(8)).getValue());

                        }


                        newRecord.setEta(((Datebox) row.getChildren().get(6)).getValue());
                        newRecord.setEtb(((Datebox) row.getChildren().get(7)).getValue());
                        newRecord.setEts(((Datebox) row.getChildren().get(8)).getValue());


                        newRecord.setOps(((Combobox) row.getChildren().get(9)).getSelectedIndex() < 0 ? null : ((Combobox) row.getChildren().get(9)).getSelectedItem().getValue().toString());
//                        newRecord.setNpoc(((Combobox) row.getChildren().get(10)).getSelectedIndex() < 0 ? null : (OprPortMaster) ((Combobox) row.getChildren().get(10)).getSelectedItem().getValue());
                        newRecord.setCategory(((Combobox) row.getChildren().get(11)).getSelectedIndex() < 0 ? null : ((Combobox) row.getChildren().get(11)).getSelectedItem().getValue().toString());
                        newRecord.setVpStatus(((Combobox) row.getChildren().get(12)).getSelectedIndex() < 0 ? null : ((Combobox) row.getChildren().get(12)).getSelectedItem().getValue().toString());

                        newRecord.setCreatedBy(loggedUser);
                        newRecord.setCreatedDate(today);
                        newRecord.setUpdatedBy(loggedUser);
                        newRecord.setUpdatedDate(today);

                        newRecord.setCurrentPort((OprPortMaster) cmbPort.getSelectedItem().getValue());
                        newRecord.setEntryStatus(Constants.NON_DELETED);

                        newRecord.setVpType(entryType);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (row.getAttribute("IS_NEW_RECORD").equals(false)) {
                    System.out.println("....................Updateting VP..................");
                    OprVesselPosition vpBean = (OprVesselPosition) row.getValue();
//                    vpBean.setVessel((OprVesselMaster) ((Combobox) row.getChildren().get(1)).getSelectedItem().getValue());
                    vpBean.setAgentName(((Textbox) row.getChildren().get(2)).getText());
                    vpBean.setJetty(((Textbox) row.getChildren().get(3)).getText());
                    vpBean.setCargo(((Textbox) row.getChildren().get(4)).getText());
                    vpBean.setQty(((Textbox) row.getChildren().get(5)).getText());

                    vpBean.setOps(((Combobox) row.getChildren().get(9)).getSelectedIndex() < 0 ? null : ((Combobox) row.getChildren().get(9)).getSelectedItem().getValue().toString());
//                    vpBean.setNpoc(((Combobox) row.getChildren().get(10)).getSelectedIndex() < 0 ? null : (OprPortMaster) ((Combobox) row.getChildren().get(10)).getSelectedItem().getValue());
                    vpBean.setCategory(((Combobox) row.getChildren().get(11)).getSelectedIndex() < 0 ? null : ((Combobox) row.getChildren().get(11)).getSelectedItem().getValue().toString());

                    vpBean.setUpdatedBy(loggedUser);
                    vpBean.setUpdatedDate(today);

                    if (statusValue.equalsIgnoreCase(Constants.ARRIVED_VESSEL)) {
                        System.out.println("Arrived date updated");
                        vpBean.setArrived(((Datebox) row.getChildren().get(6)).getValue());
                        vpBean.setEtb(((Datebox) row.getChildren().get(7)).getValue());
                        vpBean.setEts(((Datebox) row.getChildren().get(8)).getValue());
                        vpBean.setBerthed(null);
                        vpBean.setSailed(null);


                    } else if (statusValue.equalsIgnoreCase(Constants.BERTHED_VESSEL) //                                || statusValue.equalsIgnoreCase(Constants.SHIFTED_VESSEL)
                            //                                || statusValue.equalsIgnoreCase(Constants.REBERTHING_VESSEL)
                            ) {
                        System.out.println("Berthed date updated");
                        vpBean.setArrived(((Datebox) row.getChildren().get(6)).getValue());
                        vpBean.setBerthed(((Datebox) row.getChildren().get(7)).getValue());
                        vpBean.setEts(((Datebox) row.getChildren().get(8)).getValue());
                        vpBean.setSailed(null);


                    } else if (statusValue.equalsIgnoreCase(Constants.SAILED_VESSEL)) {
                        System.out.println("Sailed date updated");
                        vpBean.setArrived(((Datebox) row.getChildren().get(6)).getValue());
                        vpBean.setBerthed(((Datebox) row.getChildren().get(7)).getValue());
                        vpBean.setSailed(((Datebox) row.getChildren().get(8)).getValue());

                    }
                    if (statusValue.equalsIgnoreCase(Constants.EXPECTED_VESSEL)) {
                        vpBean.setEta(((Datebox) row.getChildren().get(6)).getValue());
                        vpBean.setEtb(((Datebox) row.getChildren().get(7)).getValue());
                        vpBean.setEts(((Datebox) row.getChildren().get(8)).getValue());
                        vpBean.setArrived(((Datebox) row.getChildren().get(6)).getValue());
                        vpBean.setBerthed(((Datebox) row.getChildren().get(7)).getValue());
                        vpBean.setSailed(((Datebox) row.getChildren().get(8)).getValue());

                    } else {
                        if (vpBean.getEta() == null) {
                            vpBean.setEta(((Datebox) row.getChildren().get(6)).getValue());
                        }
                        if (vpBean.getEtb() == null) {
                            vpBean.setEtb(((Datebox) row.getChildren().get(7)).getValue());
                        }
                        if (vpBean.getEts() == null) {
                            vpBean.setEts(((Datebox) row.getChildren().get(8)).getValue());
                        }
                    }
                    vpBean.setVpStatus(((Combobox) row.getChildren().get(12)).getSelectedItem().getValue().toString());

                }
//                    if (((Checkbox) row.getChildren().get(0)).isChecked()) {
                CommonOperation.beginTransaction();
                iVPBs.saveVP((OprVesselPosition) row.getValue());
                boolean saveStatus = CommonOperation.commitTransaction("label.common.save");
                isNewRecord = false;
                row.setAttribute("IS_NEW_RECORD", isNewRecord);
//                    }
//                }
            }
        }
    }

    public void addRecord() throws Exception {
        System.out.println("Add Record called...");
        if (!isNewRecord && validatePortForNewEntry()) {
            isNewRecord = true;
            addVesselPositionRow(null);
        }

    }

    private boolean validatePortForNewEntry() {
        boolean flag = true;
        WrongValueException e = ComponentValidation.validateManditoryCombo(cmbPort, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag = false;
        }
        return flag;
    }

    public void closeScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void retrieveRecord() {
        try {
            if (validateDates()) {
                OprVesselPosition vpBean = new OprVesselPosition();
                vpBean.setVpType(entryType);
                vpBean.setFromDate(frmDate.getValue());
                System.out.println("frmDate.getValue()  " + frmDate.getValue());
                vpBean.setToDate(toDate.getValue());
                System.out.println("toDate.getValue()  " + toDate.getValue());
                vpBean.setEntryStatus(Constants.NON_DELETED);
                vpBean.setCreatedBy(loggedUser);
                if (cmbPort.getSelectedItem() != null) {
                    OprPortMaster cmbPortMaster = (OprPortMaster) cmbPort.getSelectedItem().getValue();
                    vpBean.setCurrentPort(cmbPortMaster);
                }
                if (VesselPositionRows.getChildren().size() > 0) {
                    OprVesselMaster vessel = ((OprVesselPosition) ((Row) VesselPositionRows.getChildren().get(0)).getValue()).getVessel();
                    String agentName = ((Textbox) ((Row) VesselPositionRows.getChildren().get(0)).getChildren().get(2)).getText();
                    String jetty = ((Textbox) ((Row) VesselPositionRows.getChildren().get(0)).getChildren().get(3)).getText();
                    String cargo = ((Textbox) ((Row) VesselPositionRows.getChildren().get(0)).getChildren().get(4)).getText();
                    if (vessel != null || (jetty.trim().length() > 0) || (cargo.trim().length() > 0)) {
                        vpBean.setVessel(vessel);
                        vpBean.setJetty(jetty);
                        vpBean.setCargo(cargo);
                        vpBean.setAgentName(agentName);
                        vpBean.setVpType(entryType);
                    }
                }
                CommonOperation.beginTransaction();
                vpList = iVPBs.getVPList(vpBean);                           ///Retreive data from database.....
                System.out.println("Founeded Size > " + vpList.size());
                if (vpList.size() > 0) {
                    //                toolBarComposer.setNavigationState(vpList.size(), 1);
                    VesselPositionRows.getChildren().clear();
                    try {
                        toUI(vpList);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(VesselPositionComposer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    CommonOperation.commitTransaction();
                } else {
                    //                toolBarComposer.setNavigationState(true);
                    clearScreeen();
                    CommonOperation.commitTransaction("app.comman.err.search.recodNotFound");
                }
            }
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(VesselPositionComposer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteRecord() throws Exception {
        int delResult = 0;
        try {
            delResult = Messagebox.show(Labels.getLabel("label.delete.confirmation"), Labels.getLabel("label.common.question"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
//            Object obj = vesselPositionWin.getAttribute("modeCompType");
            if (delResult == 16) // if Yes
            {
                int iCount = 0;
                int iIndex = 0;
                boolean deleteFlag = false;
                ArrayList<Row> arList = new ArrayList<Row>();
                CommonOperation.beginTransaction();
                for (int i = 0; i < (VesselPositionRows.getChildren().size()); i++) {
                    Row row = (Row) VesselPositionRows.getChildren().get(i);
                    Checkbox chkBox = (Checkbox) row.getChildren().get(0); // get Sr.No. Field
                    if (chkBox.isChecked()) {
                        arList.add(row);
                        if (iCount == 0) {
                            iIndex = i;
                            iCount++;
                        }
                        deleteFlag = true;
                    }
                }
                if (deleteFlag == true) // if any of them is Checked
                {
                    CommonOperation.beginTransaction(); // Here Session transaction begins
                    for (Row row : arList) {
                        OprVesselPosition vpBean = (OprVesselPosition) row.getValue();
                        vpBean.setEntryStatus(Constants.DELETED);
                        if (row.getAttribute("IS_NEW_RECORD").equals(true)) {
                            isNewRecord = false;
                        }
                        iVPBs.saveVP(vpBean);
                        VesselPositionRows.removeChild(row);
                    }
                    CommonOperation.commitTransaction("com.manikssys.globalbilling.deleteStatus");
                } else // If No one is Checked
                {
                    Messagebox.show(Labels.getLabel("label.delete.warning"), "Error", Messagebox.OK, Messagebox.ERROR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setControls() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getFirstRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getLastRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getNextRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getPreviousRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean validateFields() {

        boolean flag = true;
        WrongValueException e = null;
        if (isNewRecord) {
            e = ComponentValidation.validateManditoryCombo(cmbPort, null);
            if (e != null) {
                ComponentValidation.showErrorMessage(e);
                flag = false;
            }
        }
        List<Row> vpRow_List = VesselPositionRows.getChildren();
        for (Row row : vpRow_List) {
//            OprVesselPosition vesselPositionMaster = (OprVesselPosition) row.getValue();
            Textbox txtVessel = (Textbox) row.getChildren().get(1);
            Textbox txtAgentName = (Textbox) row.getChildren().get(2);
            Textbox txtJetty = (Textbox) row.getChildren().get(3);
            Textbox txtCargo = (Textbox) row.getChildren().get(4);
            Textbox txtQty = (Textbox) row.getChildren().get(5);
            Datebox dtETA = (Datebox) row.getChildren().get(6);
            Datebox dtETB = (Datebox) row.getChildren().get(7);
            Datebox dtETS = (Datebox) row.getChildren().get(8);
            Combobox txtOPS = (Combobox) row.getChildren().get(9);
            Textbox cmbNPOC = (Textbox) row.getChildren().get(10);
            Combobox cmbCatagery = (Combobox) row.getChildren().get(11);
            Combobox cmbStatus = (Combobox) row.getChildren().get(12);
//            if (!validateForNonWaitingVessel(arrivedDt, cmbStatus)) {
//                return false;
//            }
            e = ComponentValidation.validateManditoryTextbox(txtVessel, null);
            if (e != null) {
                ComponentValidation.showErrorMessage(e);
                System.out.println("____________________________1");
                flag = false;
            }
            if (!txtVessel.getText().toUpperCase().equalsIgnoreCase("VACANT")) {
                e = ComponentValidation.validateManditoryTextbox(txtAgentName, null);      //
                if (e != null) {
                    ComponentValidation.showErrorMessage(e);
                    System.out.println("____________________________2");
                    flag = false;
                }
            }
            /*    e = ComponentValidation.validateManditoryTextbox(txtCargo, null);      //
            if (e != null) {
            ComponentValidation.showErrorMessage(e);
            System.out.println("____________________________3");
            flag = false;
            }*/
//            e = ComponentValidation.isValidFloat(txtQty, "Invalid Input");
//            if (e != null) {
//            ComponentValidation.showErrorMessage(e);
//            System.out.println("____________________________4");
//            flag = false;
//            }
            /*
            e = ComponentValidation.validateManditoryCombo(cmbNPOC, null);
            if (e != null) {
            ComponentValidation.showErrorMessage(e);
            System.out.println("____________________________5");
            flag = false;
            }

            e = ComponentValidation.validateManditoryCombo(cmbCatagery, null);
            if (e != null) {
            ComponentValidation.showErrorMessage(e);
            System.out.println("____________________________6");
            flag = false;
            }*/
            e = ComponentValidation.validateManditoryCombo(cmbStatus, null);
            if (e != null) {
                ComponentValidation.showErrorMessage(e);
                System.out.println("____________________________7");
                flag = false;
            }
        }
        System.out.println("VALIDATION FLAG   -->" + flag);
        return flag;
    }

    public boolean validateDates() throws InterruptedException {

        boolean flag = true;

        if (loggedUser.getUserTypeId() != null && loggedUser.getUserTypeId().equalsIgnoreCase("1")) {
            WrongValueException e = ComponentValidation.validateManditoryTextbox(frmDate, null);
            if (e != null) {
                ComponentValidation.showErrorMessage(e);
                flag = false;
            }
            e = ComponentValidation.validateManditoryTextbox(toDate, null);
            if (e != null) {
                ComponentValidation.showErrorMessage(e);
                flag = false;
            }
            return flag;
        } else {
            Messagebox.show("Access restricted for this User");
            return false;
        }
    }

    private BigDecimal getFloatData(Textbox txt) {
        if (txt.getText().length() > 0) {
            try {
                return BigDecimal.valueOf(Float.parseFloat(txt.getText()));
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO;
            } catch (WrongValueException e) {
                return BigDecimal.ZERO;
            }
        } else {
            return BigDecimal.ZERO;
        }

    }

    private void toUI(List<OprVesselPosition> vpList) throws InterruptedException {
        VesselPositionRows.getChildren().clear();
        Iterator itr = vpList.iterator();

        while (itr.hasNext()) {
            OprVesselPosition vesselPosition = (OprVesselPosition) itr.next();
            if (vesselPosition.getVpStatus() != null) {
                System.out.println("vesselPosition.getCreatedBy().getOprBranchMaster()     >>>>>>>>>>>>>>>>>>" + vesselPosition.getCreatedBy().getOprBranchMaster().getBranchId());
                if (loggedUser.getOprBranchMaster().getBranchId().equals("1")
                        || (vesselPosition.getCreatedBy().getOprBranchMaster().getBranchId() == null ? loggedUser.getOprBranchMaster().getBranchId() == null : vesselPosition.getCreatedBy().getOprBranchMaster().getBranchId().equals(loggedUser.getOprBranchMaster().getBranchId()))) {

                    try {
                        addVesselPositionRow(vesselPosition);
                    } catch (Exception ex) {
                        Messagebox.show("Unable to Add vessel on Screen");
                    }
                }
            }
        }
    }

    private void removeAllErrorMessages() {
        ComponentValidation.removeErrorMessage(cmbPort);
        ComponentValidation.removeErrorMessage(frmDate);
        ComponentValidation.removeErrorMessage(toDate);
    }

    private void retreivePortVpRecords() {
        OprVesselPosition vpBean = new OprVesselPosition();
        vpBean.setFromDate(frmDate.getValue() == null ? null : frmDate.getValue());
        vpBean.setToDate(toDate.getValue() == null ? null : toDate.getValue());
        vpBean.setEntryStatus(Constants.NON_DELETED);
        vpBean.setVpType(entryType);
        if (cmbPort.getSelectedItem().getValue() != null) {
            OprPortMaster cmbPortMaster = (OprPortMaster) cmbPort.getSelectedItem().getValue();
            System.out.println("Currrent Port is  = " + cmbPortMaster.getPortName());
            vpBean.setCurrentPort(cmbPortMaster);
        } else {
            return;
        }
        CommonOperation.beginTransaction();
        vpList = iVPBs.findNonSailedVPByPort(vpBean);              ///Retreive BS Function called..
        System.out.println("Founeded Size >----> " + vpList.size());
        if (vpList.size() > 0) {
//            toolBarComposer.setNavigationState(vpList.size(), 1);
            VesselPositionRows.getChildren().clear();
            populatePortVPList(vpList, vesselLabel.getValue()==null?"":vesselLabel.getValue());
            CommonOperation.commitTransaction();
        } else {
//            toolBarComposer.setNavigationState(true);
            clearScreeen();
            CommonOperation.commitTransaction("");
        }
    }

    private void populatePortVPList(List<OprVesselPosition> vpList, String vessel) {
        VesselPositionRows.getChildren().clear();
        Iterator itr = vpList.iterator();
        int vp_status = 0;
        int expected = Integer.parseInt(Constants.EXPECTED_VESSEL);
        int sailed = Integer.parseInt(Constants.SAILED_VESSEL);
        while (itr.hasNext()) {
            OprVesselPosition vesselPosition = (OprVesselPosition) itr.next();
            if (vesselPosition.getVpStatus() != null) {
                vp_status = Integer.parseInt(vesselPosition.getVpStatus());
                if (vp_status != sailed && vessel.isEmpty()) {
                    addVesselPositionRow(vesselPosition);
                } else if (vp_status != sailed && vesselPosition.getVessel().getVesselName().toUpperCase().startsWith(vessel.toUpperCase())) {
                    addVesselPositionRow(vesselPosition);
                }
            }
        }
    }

    private void createRemarkMenu(Row row, OprVesselPosition vpBean, String width) {


        Button remarkBtn = new Button();
        //remark.setAttribute("VP_OBJECT", vpBean);
        remarkBtn.setAttribute("THIS_CONTROL", "REMARK_BUTTON");
        remarkBtn.addForward(Events.ON_CLICK, vesselPositionWin, "onGotFocus");
        remarkBtn.setLabel("Remark");
        remarkBtn.setWidth(width);
        row.appendChild(remarkBtn);

    }

    private void selectCurrentPort(OprPortMaster currentPort) {
        System.out.println("CMB selectCurrentPort called................");
        List<Comboitem> itemList = cmbPort.getChildren();
        for (Comboitem item : itemList) {
            if (((OprPortMaster) item.getValue()).getPortId().equals(currentPort.getPortId())) {
                cmbPort.setSelectedItem(item);
            }
        }
    }

    private void setVacantSettings(Row row) {
        ((Textbox) row.getChildren().get(2)).setDisabled(true);
        ((Textbox) row.getChildren().get(4)).setDisabled(true);
        ((Textbox) row.getChildren().get(5)).setDisabled(true);
        ((Textbox) row.getChildren().get(9)).setDisabled(true);

    }

    private void setNonVacantSettings(Row row) {
        ((Textbox) row.getChildren().get(2)).setDisabled(false);
        ((Textbox) row.getChildren().get(4)).setDisabled(false);
        ((Textbox) row.getChildren().get(5)).setDisabled(false);
        ((Textbox) row.getChildren().get(9)).setDisabled(false);

    }

    private void populateRemarkListBox(Listbox lb, OprVesselPosition vpBean) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Listhead lhead = new Listhead();
        Listheader lheader1 = new Listheader();
        lheader1.setWidth("50px");
        lheader1.setLabel("Sr. No.");
        lheader1.setSort("auto");
        lheader1.setParent(lhead);
        Listheader lheader2 = new Listheader();
        lheader2.setWidth("100px");
        lheader2.setLabel("Date");
        lheader2.setSort("auto");
        lheader2.setParent(lhead);
        Listheader lheader3 = new Listheader();
        lheader3.setWidth("200px");
        lheader3.setLabel("Remark");
        lheader3.setSort("auto");
        lheader3.setParent(lhead);
        lhead.setParent(lb);
        int count = 1;
        try {
            if (vpBean == null || vpBean.getRemarkCollection().isEmpty()) {
//            String[] dateWiseData =
            } else {
                for (OprRemark remarkObj : vpBean.getRemarkCollection()) {
                    Listitem litem = new Listitem();
                    litem.setValue(remarkObj);
                    Listcell lcell1 = new Listcell();
                    lcell1.setLabel("" + count++);
                    lcell1.setParent(litem);
                    Listcell lcell2 = new Listcell();
                    lcell2.setLabel(sdf.format(remarkObj.getRemarkDate()));
                    lcell2.setParent(litem);
                    Listcell lcell3 = new Listcell();
                    Textbox data = new Textbox();
                    data.setWidth("200px");
                    data.setText(remarkObj.getRemarkText());
                    data.setReadonly(true);
                    data.setParent(lcell3);
                    lcell3.setParent(litem);
                    litem.setParent(lb);
                }
            }
        } catch (Exception ex) {
            System.out.println("Exception  = == = " + ex.getMessage());
        }
        Listitem litem = new Listitem();
        litem.setValue(new OprRemark());
        Listcell lcell1 = new Listcell();
        lcell1.setLabel("" + count);
        lcell1.setParent(litem);
        Listcell lcell2 = new Listcell();
        lcell2.setLabel(sdf.format(new Date()));
        lcell2.setParent(litem);
        Listcell lcell3 = new Listcell();
        Textbox data = new Textbox();
        data.setWidth("200px");
        data.setText("");
        data.setAttribute("THIS_CONTROL", "REMARK_TEXTBOX");
        data.addForward(Events.ON_CHANGE, vesselPositionWin, "onGotFocus");
        data.setParent(lcell3);
        lcell3.setParent(litem);
        litem.setParent(lb);
    }

    private OprRemark getRemarkItem(Textbox remarkTxt, OprVesselPosition vpBean) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        OprRemark obj = new OprRemark();
        Listcell cell = (Listcell) remarkTxt.getParent();
        Listitem item = (Listitem) cell.getParent();
        obj.setVesselPosition(vpBean);
        obj.setSrNo(((Listcell) item.getChildren().get(0)).getLabel());
        obj.setRemarkDate(sdf.parse(((Listcell) item.getChildren().get(1)).getLabel()));
        obj.setRemarkText(((Textbox) ((Listcell) item.getChildren().get(2)).getChildren().get(0)).getText());
        return obj;
    }
}
