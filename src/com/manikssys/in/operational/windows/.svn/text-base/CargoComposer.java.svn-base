/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.operational.windows;

import com.manikssys.in.common.*;
import com.manikssys.in.operational.beans.*;
import com.manikssys.in.operational.business.*;
import com.manikssys.in.security.beans.ScrUserMaster;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import org.hibernate.HibernateException;
import org.zkoss.util.resource.Labels;

/**
 *
 * @author pc
 */
public class CargoComposer extends GenericForwardComposer implements ICommonWindowInterface {

    private Window dryCargoWin;
    private Combobox loadingPort, dischargePort, cmbOwnerCountry, cmbChartererCountry, cmbAgentCountry;
    private Datebox eta, etb, ets, frmDate, toDate;
    private Textbox owners, charterers, agent, txtIGMNo, txtEGMNo, vessel;
    private Checkbox entryCheckbox;
    private Rows cargoDetailsRows;
    private boolean isNewRecord = false;
    private boolean isNewRow = false;
    private String entryType = "0";
    List<OprCargoHdr> cargoHdrList = null;
    ICargoHdr iCargoHdrObj;
    List<OprVesselPosition> searchvesselList = new ArrayList<OprVesselPosition>();
    ScrUserMaster userMaster;
    int currentIndex = -1;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
    ToolbarComposer toolBarComposer;
    OprVesselPosition vpBean = null;
    OprCargoHdr currenCargoHdr = null;

    public void onCreate$dryCargoWin() {
        System.out.println("in dry cargo create");
        Executions.getCurrent().getDesktop().setAttribute("currentWindow", this);
        toolBarComposer = (ToolbarComposer) Executions.getCurrent().getDesktop().getAttribute("commonHBox$composer");
        System.out.println("toolBarComposer      ----> " + toolBarComposer);
        userMaster = ((ScrUserMaster) sessionScope.get("user"));
        iCargoHdrObj = new OprCargoHdrBS();
        CommonOperation.beginTransaction();
        initComponents();
        findEntryType();
        CommonOperation.commitTransaction();
        userMaster = (ScrUserMaster) sessionScope.get("user");
    }

    private void initComponents() {
        //  clearScreeen();
        List<OprPortMaster> portList = iCargoHdrObj.getPortsList();
        List<OprCountryMaster> countryList = iCargoHdrObj.getAllCountries();

        loadPortCombobox(loadingPort, portList, null);
        loadPortCombobox(dischargePort, portList, null);
        loadCountry(cmbOwnerCountry, countryList, null);
        loadCountry(cmbChartererCountry, countryList, null);
        loadCountry(cmbAgentCountry, countryList, null);
        loadingPort.addForward(Events.ON_BLUR, dryCargoWin, "onSelect");
        dischargePort.addForward(Events.ON_BLUR, dryCargoWin, "onSelect");
        loadingPort.setDisabled(true);
        dischargePort.setDisabled(true);
        vessel.addForward(Events.ON_CHANGE, dryCargoWin, "onVesselChange");
        eta.addForward(Events.ON_FOCUS, dryCargoWin, "onGotFocus");
        etb.addForward(Events.ON_FOCUS, dryCargoWin, "onGotFocus");
        ets.addForward(Events.ON_FOCUS, dryCargoWin, "onGotFocus");
        owners.addForward(Events.ON_FOCUS, dryCargoWin, "onGotFocus");
        charterers.addForward(Events.ON_FOCUS, dryCargoWin, "onGotFocus");
        agent.addForward(Events.ON_FOCUS, dryCargoWin, "onGotFocus");
        txtIGMNo.addForward(Events.ON_FOCUS, dryCargoWin, "onGotFocus");
        txtEGMNo.addForward(Events.ON_FOCUS, dryCargoWin, "onGotFocus");
        cmbOwnerCountry.addForward(Events.ON_FOCUS, dryCargoWin, "onGotFocus");
        cmbChartererCountry.addForward(Events.ON_FOCUS, dryCargoWin, "onGotFocus");
        cmbAgentCountry.addForward(Events.ON_FOCUS, dryCargoWin, "onGotFocus");
        frmDate.addForward(Events.ON_FOCUS, dryCargoWin, "onDateChange");
        toDate.addForward(Events.ON_FOCUS, dryCargoWin, "onDateChange");
    }

    public void onVesselChange(ForwardEvent event) throws InterruptedException {
        System.out.println("onVesselChange event......................");
        OprVesselMaster vesselBean = null;
        String lovFile = Labels.getLabel("app.vp.vessellov");
        Window w = (Window) Executions.createComponents(lovFile, null, null);//("WEB-INF"+File.separator+"presentation"+File.separator+"LOV"+File.separator+"CommonVesselLov.zul", null, null);
        w.setAttribute("PARAM_TEXT", vessel.getText());
        w.setAttribute("OBJECT", null);
        w.setAttribute("width", "400px");
        w.setMaximizable(true);
        w.doModal();
        vesselBean = w.getAttribute("OBJECT") != null ? (OprVesselMaster) w.getAttribute("OBJECT") : null;
        if (vesselBean != null) {
            vessel.setText(vesselBean.getVesselName());
            vessel.setAttribute("VESSEL_OBJECT", vesselBean);
        } else {
            vessel.setText("");
            vessel.setAttribute("VESSEL_OBJECT", null);
        }
    }

    public void onSelect$loadingPort() throws InterruptedException, ParseException {
        System.out.println("loadingPort Got Select Event ....." + loadingPort.getSelectedItem().getValue());
        if(!isValidDatebox()){
            return;
        }

        OprPortMaster selectedPort = (OprPortMaster) loadingPort.getSelectedItem().getValue();
        if (vpBean == null) {
            vpBean = new OprVesselPosition();
            vpBean.setCurrentPort(loadingPort.getSelectedIndex() < 0 ? null : (OprPortMaster) loadingPort.getSelectedItem().getValue());
            vpBean.setVpStatus(Constants.SAILED_VESSEL);
            vpBean.setOps(Constants.LOAD_OPERATION);            
            System.out.println("userMaster.getOprBranchMaster().getBranchId()  " + userMaster.getOprBranchMaster().getBranchId());
            System.out.println("selectedPort.getBranchId()  " + selectedPort.getBranchId());
            if (userMaster.getOprBranchMaster().getBranchId().equalsIgnoreCase("1")) {
                setVPBeanForLOVFilter(Constants.LOAD_OPERATION, (OprPortMaster) loadingPort.getSelectedItem().getValue());
            } else if (selectedPort.getBranchId() != null) {
                if (loadingPort.getSelectedIndex() > -1 && (userMaster.getOprBranchMaster().getBranchId().equalsIgnoreCase(selectedPort.getBranchId().getBranchId()))) {
                    setVPBeanForLOVFilter(Constants.LOAD_OPERATION, (OprPortMaster) loadingPort.getSelectedItem().getValue());
                }
            }
        }

    }

    public void onSelect$dischargePort() throws InterruptedException, ParseException {
        System.out.println("dischargePort Got Select Event ....." + dischargePort.getSelectedItem().getValue());

        OprPortMaster selectedPort = (OprPortMaster) dischargePort.getSelectedItem().getValue();
        if (vpBean == null) {
            vpBean = new OprVesselPosition();
            vpBean.setCurrentPort(dischargePort.getSelectedIndex() < 0 ? null : (OprPortMaster) dischargePort.getSelectedItem().getValue());
            vpBean.setVpStatus(Constants.DISCHARGED_VESSEL);
            vpBean.setOps(Constants.UNLOAD_OPERATION);
            if (dischargePort.getSelectedIndex() > -1 && (userMaster.getOprBranchMaster().getBranchId().equalsIgnoreCase("1") || userMaster.getOprBranchMaster().getBranchId().equalsIgnoreCase(selectedPort.getBranchId().getBranchId()))) {
                setVPBeanForLOVFilter(Constants.UNLOAD_OPERATION, (OprPortMaster) dischargePort.getSelectedItem().getValue());

            }
        }

    }

    private void clearVPComponents() {
        vessel.setText("");
        eta.setValue(null);
        eta.setText("");
        etb.setValue(null);
        etb.setText("");
        ets.setValue(null);
        ets.setText("");
        owners.setText("");
        cmbOwnerCountry.setSelectedItem(null);
        agent.setText(null);
    }

    private void loadCountry(Combobox cmbBox, List<OprCountryMaster> countryList, OprCountryMaster selectedCountry) {
        if (countryList != null) {
            for (OprCountryMaster countryMaster : countryList) {
                Comboitem cmbItem = new Comboitem();
                cmbItem.setLabel(countryMaster.getCountryName());
                cmbItem.setValue(countryMaster);
                cmbItem.setParent(cmbBox);
            }
        }
        if (selectedCountry != null) {
            boolean flag = false;
            List<Comboitem> comboItemList = cmbBox.getChildren();
            for (Comboitem cmbItem : comboItemList) {
                OprCountryMaster countryMaster = (OprCountryMaster) cmbItem.getValue();
                if (countryMaster.getCountryId().equalsIgnoreCase(selectedCountry.getCountryId())) {
                    cmbBox.setSelectedItem(cmbItem);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                cmbBox.setSelectedIndex(-1);
            }
        } else {
            cmbBox.setSelectedIndex(-1);
        }

    }

    private void loadPortCombobox(Combobox cmb, List<OprPortMaster> portList, OprPortMaster selectedPort) {
        if (portList != null) {
//            if (userMaster.getOprBranchMaster().getBranchId().equals("1")) {
            for (OprPortMaster oprPortMaster : portList) {
                Comboitem ci = new Comboitem();
                ci.setLabel(oprPortMaster.getPortName());
                ci.setDescription(oprPortMaster.getPortCode());
                ci.setValue(oprPortMaster);
                ci.setParent(cmb);
            }
//            } else {
//                for (OprPortMaster oprPortMaster : portList) {
//                    if (userMaster.getOprBranchMaster().getBranchId().equals(oprPortMaster.getBranchId() == null ? null : oprPortMaster.getBranchId().getBranchId())) {
//                        Comboitem ci = new Comboitem();
//                        ci.setLabel(oprPortMaster.getPortName());
//                        ci.setDescription(oprPortMaster.getPortCode());
//                        ci.setValue(oprPortMaster);
//                        ci.setParent(cmb);
//                    }
//                }
//            }
        }
        if (selectedPort != null) {
            boolean flag = false;
            List<Comboitem> comboItemList = cmb.getChildren();
            for (Comboitem cmbItem : comboItemList) {
                OprPortMaster oprPortMaster = (OprPortMaster) cmbItem.getValue();
                if (oprPortMaster.getPortId().equalsIgnoreCase(selectedPort.getPortId())) {
                    cmb.setSelectedItem(cmbItem);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                cmb.setSelectedIndex(-1);
            }
        } else {
            cmb.setSelectedIndex(-1);
        }
    }

    public void clearScreeen() {
        System.out.println("Clear Screen.............");
        vessel.setText("");
        vessel.setReadonly(false);
        currenCargoHdr = null;
        eta.setValue(null);
        etb.setValue(null);
        ets.setValue(null);
        owners.setText("");
        charterers.setText("");
        agent.setText("");
        txtIGMNo.setText("");
        txtEGMNo.setText("");
        loadingPort.setSelectedIndex(-1);
        dischargePort.setSelectedIndex(-1);
        loadingPort.setDisabled(true);
        dischargePort.setDisabled(true);
        cmbOwnerCountry.setSelectedIndex(-1);
        cmbChartererCountry.setSelectedIndex(-1);
        cmbAgentCountry.setSelectedIndex(-1);
        cargoDetailsRows.getChildren().clear();
        //createCargoDetailEmptyRow();
        frmDate.setValue(null);
        toDate.setValue(null);
        currentIndex = -1;
        vpBean = null;
        currenCargoHdr = null;
        txtIGMNo.setAttribute("CARGO_OBJECT", null);
//        searchvesselList = new ArrayList<OprVesselMaster>();
        resetFields();
    }

    public void saveRecord() throws Exception {

        SaveCargo();
    }

    public void addRecord() throws Exception {
        if (vpBean != null && currenCargoHdr == null) {
            int val = Messagebox.show("Do you want to save?", "Information", Messagebox.YES | Messagebox.NO | Messagebox.CANCEL, Messagebox.QUESTION);
            if (val == 16) {
                SaveCargo();
            }
        }

        if (currenCargoHdr != null) {
            isNewRecord = false;
            createCargoDetailEmptyRow();
        } else {
            isNewRecord = true;
            currenCargoHdr = new OprCargoHdr();
            clearScreeen();
        }

    }

    public void closeScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void retrieveRecord() {
        if (!isValidDatebox()) {
            return;
        }
        resetFields();
        OprCargoHdr cargoHdrBean = new OprCargoHdr();
        cargoHdrBean.setFromDate(frmDate.getValue());
        cargoHdrBean.setToDate(toDate.getValue());
        cargoHdrBean.setEntryStatus(Constants.NON_DELETED);
        cargoHdrBean.setCargoType(entryType);
        cargoHdrBean.setOprVesselPosition(vpBean);
        cargoHdrBean.setEgmNo(txtEGMNo.getText().isEmpty() ? null : txtEGMNo.getText());
        cargoHdrBean.setIgmNo(txtIGMNo.getText().isEmpty() ? null : txtIGMNo.getText());
        cargoHdrBean.setLoadPortMaster((OprPortMaster) (loadingPort.getSelectedIndex() < 0 ? null : loadingPort.getSelectedItem().getValue()));
        cargoHdrBean.setDischargePortMaster((OprPortMaster) (dischargePort.getSelectedIndex() < 0 ? null : dischargePort.getSelectedItem().getValue()));
        cargoHdrBean.setCharterers(charterers.getText());
        cargoHdrBean.setUpdatedBy(userMaster);
        OprCargoDtl dtl = (new OprCargoDtl());
        dtl.setEntryStatus(Constants.NON_DELETED);
        Set set = new HashSet();
        set.add(dtl);
        cargoHdrBean.setOprCargoDtlCollection(set);
//            cargoHdrBean.setCharterersCountry((OprCountryMaster) (cmbChartererCountry.getSelectedIndex() < 0 ? null : cmbChartererCountry.getSelectedItem().getValue()));
//            cargoHdrBean.setAgentCountry((OprCountryMaster) (cmbAgentCountry.getSelectedIndex() < 0 ? null : cmbAgentCountry.getSelectedItem().getValue()));
        CommonOperation.beginTransaction();
        try {
            List<OprCargoHdr> allCargoHdrList = iCargoHdrObj.getCargoHdrList(cargoHdrBean);
            if (userMaster.getOprBranchMaster().getBranchId().equalsIgnoreCase("1")) {
                cargoHdrList = allCargoHdrList;
            } else {
                if (cargoHdrList == null) {
                    cargoHdrList = new ArrayList<OprCargoHdr>();
                }
                cargoHdrList.clear();

                for (OprCargoHdr hdrBean : allCargoHdrList) {
                    if (hdrBean.getCreatedBy().getOprBranchMaster().getBranchId().equalsIgnoreCase(userMaster.getOprBranchMaster().getBranchId())) {
                        cargoHdrList.add(hdrBean);
                    }
                }


            }

        } catch (HibernateException ex) {
            ex.printStackTrace();
        }
        System.out.println("Cargo Header List sise ------ >" + cargoHdrList.size());
        if (cargoHdrList.isEmpty()) {
            toolBarComposer.setNavigationState(true);
            clearScreeen();
            CommonOperation.commitTransaction("app.comman.err.search.recodNotFound");
        } else {
            toolBarComposer.setNavigationState(cargoHdrList.size(), 1);
            currentIndex = 0;
            cargoHdrBean = cargoHdrList.get(currentIndex);
            populateHeaderData(cargoHdrBean);
        }
    }

    public void deleteRecord() throws Exception {
        deleteData();
    }

    public void setControls() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getFirstRecord() {
        toolBarComposer.setNavigationState(cargoHdrList.size(), 1);
        currentIndex = 0;
        if (currentIndex < cargoHdrList.size()) {
            populateHeaderData(cargoHdrList.get(currentIndex));
        }
    }

    public void getLastRecord() {
        toolBarComposer.setNavigationState(cargoHdrList.size(), cargoHdrList.size());
        currentIndex = (cargoHdrList.size() - 1);
        if (currentIndex < cargoHdrList.size()) {
            populateHeaderData(cargoHdrList.get(currentIndex));
        }
    }

    public void getNextRecord() {
        currentIndex++;
        toolBarComposer.setNavigationState(cargoHdrList.size(), currentIndex + 1);
        if (currentIndex < cargoHdrList.size()) {
            populateHeaderData(cargoHdrList.get(currentIndex));
        }
    }

    public void getPreviousRecord() {
        currentIndex--;
        toolBarComposer.setNavigationState(cargoHdrList.size(), currentIndex + 1);
        if (currentIndex < cargoHdrList.size() && currentIndex >= 0) {
            populateHeaderData(cargoHdrList.get(currentIndex));
        }
    }

    private OprCargoDtl getCargoObj(Row row, OprCargoDtl cargoDetails) {
        System.out.println("___________getCargoObj  ");
        cargoDetails.setShipper(getTextboxTextOfRow(row, 1));
        cargoDetails.setReceiver(getTextboxTextOfRow(row, 2));
        cargoDetails.setCommodity(getTextboxTextOfRow(row, 3));
        cargoDetails.setGrade(getTextboxTextOfRow(row, 4));
        cargoDetails.setManQty(getFloatData(getTextbox(row, 5)));
        cargoDetails.setMechQty(getFloatData(getTextbox(row, 6)));
        cargoDetails.setAncQty(getFloatData(getTextbox(row, 7)));
        cargoDetails.setPrice(getFloatData(getTextbox(row, 8)));
        cargoDetails.setFob(getIntData(getTextbox(row, 9)));
        cargoDetails.setCha(getTextboxTextOfRow(row, 10));

        // Dummy Inserts
        cargoDetails.setShipQty(BigDecimal.ZERO);
        cargoDetails.setBlQty(BigDecimal.ZERO);

        cargoDetails.setEntryStatus(Constants.NON_DELETED);
//        cargoDetails.setCommodity(entryType);

        return cargoDetails;
    }

    private void createCargoDetailRows(Set<OprCargoDtl> cargoDetails) {
        cargoDetailsRows.getChildren().clear();
        for (OprCargoDtl oprCargoDetail : cargoDetails) {
            if (oprCargoDetail.getEntryStatus().equalsIgnoreCase(Constants.NON_DELETED)) {
                Row row = new Row();
                row.setParent(cargoDetailsRows);
                row.setValue(oprCargoDetail);
                createCheckbox(row);
                createTextbox(row, oprCargoDetail.getShipper());
                createTextbox(row, oprCargoDetail.getReceiver());
                createTextbox(row, oprCargoDetail.getCommodity());
                createTextbox(row, oprCargoDetail.getGrade());
                createTextbox(row, "" + oprCargoDetail.getManQty());
                createTextbox(row, "" + oprCargoDetail.getMechQty());
                createTextbox(row, "" + oprCargoDetail.getAncQty());
                createTextbox(row, "" + oprCargoDetail.getPrice());
                createTextbox(row, "" + oprCargoDetail.getFob());
                createTextbox(row, oprCargoDetail.getCha());
            }
        }
    }

    private void createCargoDetailEmptyRow() {
        Row row = new Row();
        createCheckbox(row);
        row.setParent(cargoDetailsRows);
        row.setValue(null);
        for (int i = 0; i < 4; i++) {
            createTextbox(row, null);
        }
        for (int i = 0; i < 5; i++) {
            createTextbox(row, "" + 0);
        }
        createTextbox(row, null);
    }

    public void createCheckbox(Row row) {
        Checkbox chk = new Checkbox();
        chk.setParent(row);
        chk.addForward(Events.ON_FOCUS, dryCargoWin, "onGotFocus");
        // row.appendChild(chk);
    }

    public String getTextboxTextOfRow(Row row, int index) {
        return ((Textbox) row.getChildren().get(index)).getText();
    }

    public Textbox getTextbox(Row row, int index) {
        return (Textbox) row.getChildren().get(index);
    }

    public void createTextbox(Row row, String val) {

        Textbox txt = new Textbox(val == null ? "" : val);
        txt.setParent(row);
        txt.setWidth("98%");
        txt.addForward(Events.ON_FOCUS, dryCargoWin, "onGotFocus");
        // row.appendChild(txt);
        // txt.setCols(9);
        // txt.addForward(Events.ON_CHANGE, self, "onTextboxChange");
    }

    public void onGotFocus(ForwardEvent event) throws InterruptedException {
        try {
            Component comp = event.getOrigin().getTarget();
            dryCargoWin.setAttribute("modeCompType", comp.getParent().getParent());
        } catch (WrongValueException e) {
            e.printStackTrace();
        }
    }

    public void onDateChange(ForwardEvent event) throws InterruptedException {
        isValidateDatebox();
    }

    public boolean validateFields() {
        boolean flag = true;
        WrongValueException e = ComponentValidation.validateManditoryTextbox(vessel, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag = false;
        }
        if (vpBean == null) {
            flag = false;
        }
        /*
        e = ComponentValidation.validateManditoryCombo(loadingPort, null);
        if (e != null) {
        ComponentValidation.showErrorMessage(e);
        flag = false;
        }
        e = ComponentValidation.validateManditoryCombo(dischargePort, null);
        if (e != null) {
        ComponentValidation.showErrorMessage(e);
        flag = false;
        }
         * 
         */
        /*      e = ComponentValidation.validateManditoryTextbox(owners, null);
        if (e != null) {
        ComponentValidation.showErrorMessage(e);
        flag = false;
        }
        e = ComponentValidation.validateManditoryTextbox(agent, null);
        if (e != null) {
        ComponentValidation.showErrorMessage(e);
        flag = false;
        }*/

        Row row;


        for (int i = 0; i < cargoDetailsRows.getChildren().size(); i++) {
            row = (Row) cargoDetailsRows.getChildren().get(i);

            flag = isValidFloat(getTextbox(row, 5)) == false ? false : flag;
            flag = isValidFloat(getTextbox(row, 6)) == false ? false : flag;
            flag = isValidFloat(getTextbox(row, 7)) == false ? false : flag;
            flag = isValidFloat(getTextbox(row, 8)) == false ? false : flag;
        }

        return flag;
    }

    private int getIntData(Textbox txt) {

        if (txt.getText().length() > 0) {
            try {
                return Integer.parseInt(txt.getText());
            } catch (NumberFormatException e) {
                return 0;
            } catch (WrongValueException e) {
                return 0;
            }
        } else {
            return 0;
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

    private boolean isValidDatebox() {
        boolean flag = true;
        if (frmDate.getValue() == null || toDate.getValue() == null) {
            if (frmDate.getValue() == null) {
                ComponentValidation.showErrorMessage(new WrongValueException(frmDate, org.zkoss.util.resource.Labels.getLabel("app.cargo.err.selectFrmDate")));
                flag = false;
            }
            if (toDate.getValue() == null) {
                ComponentValidation.showErrorMessage(new WrongValueException(toDate, org.zkoss.util.resource.Labels.getLabel("app.cargo.err.selecttoDate")));
                flag = false;
            }
        } else {
            if (ComponentValidation.compareDate(frmDate.getValue(), toDate.getValue()) <= 0) {
            } else {
                ComponentValidation.showErrorMessage(new WrongValueException(frmDate, org.zkoss.util.resource.Labels.getLabel("app.cargo.err.invalidDate")));
                ComponentValidation.showErrorMessage(new WrongValueException(toDate, org.zkoss.util.resource.Labels.getLabel("app.cargo.err.invalidDate")));
                flag = false;
            }
        }
        loadingPort.setDisabled(!flag);
        dischargePort.setDisabled(!flag);
        return flag;
    }
    private boolean isValidateDatebox() {
        boolean flag = true;
        if (frmDate.getValue() != null && toDate.getValue() != null)  {
            if (ComponentValidation.compareDate(frmDate.getValue(), toDate.getValue()) <= 0) {
                ComponentValidation.removeErrorMessage(frmDate);
                ComponentValidation.removeErrorMessage(toDate);     
            } else {
                ComponentValidation.showErrorMessage(new WrongValueException(frmDate, org.zkoss.util.resource.Labels.getLabel("app.cargo.err.invalidDate")));
                ComponentValidation.showErrorMessage(new WrongValueException(toDate, org.zkoss.util.resource.Labels.getLabel("app.cargo.err.invalidDate")));
                flag = false;
            }
        }
        loadingPort.setDisabled(!flag);
        dischargePort.setDisabled(!flag);
        return flag;
    }


    public boolean isValidInteger(Textbox txt) {

        if (txt.getText().length() > 0) {

            WrongValueException wvexpObj;

            wvexpObj = ComponentValidation.isValidInteger(txt, "Invalid Input");
            if (wvexpObj != null) {
                ComponentValidation.showErrorMessage(wvexpObj);
                return false;
            }
        }
        return true;
    }

    public boolean isValidFloat(Textbox txt) {

        if (txt.getText().length() > 0) {

            WrongValueException wvexpObj;

            wvexpObj = ComponentValidation.isValidFloat(txt, "Invalid Input");
            if (wvexpObj != null) {
                ComponentValidation.showErrorMessage(wvexpObj);
                return false;
            }
        }
        return true;
    }

    private void resetFields() {
        ComponentValidation.removeErrorMessage(frmDate);
        ComponentValidation.removeErrorMessage(toDate);
        ComponentValidation.removeErrorMessage(vessel);
        ComponentValidation.removeErrorMessage(loadingPort);
        ComponentValidation.removeErrorMessage(dischargePort);
    }

    private Date getDateBeforeXdays(int prevDays) throws ParseException {
        java.util.Calendar now = java.util.Calendar.getInstance();

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        // add days to current date using Calendar.add method
        now.add(java.util.Calendar.DATE, -prevDays);
        Date date = (Date) formatter.parse(now.get(java.util.Calendar.DATE) + "-" + (now.get(java.util.Calendar.MONTH) + 1) + "-" + now.get(java.util.Calendar.YEAR));
        System.out.println("Date " + date.toString());
        return date;
    }

    private void setVPDataToScreen(OprVesselPosition vpBean) {
        if (vpBean != null) {
//            boolean flag = false;
//            List<Comboitem> comboItemList = vessel.getChildren();
//            for (Comboitem cmbItem : comboItemList) {
//                OprVesselMaster oprVesselMaster = (OprVesselMaster) cmbItem.getValue();
//                if (oprVesselMaster.getVesselId().equalsIgnoreCase(vpBean.getVessel().getVesselId())) {
//                    vessel.setSelectedItem(cmbItem);
//                    flag = true;
//                    break;
//                }
//            }
            vessel.setText(vpBean != null ? vpBean.getVessel().getVesselName() : "");
            vessel.setAttribute("VESSEL_POSITION_OBJECT", vpBean.getVessel());
//            if (!flag) {
//                vessel.setText("");
//            }

            if (vpBean.getVpStatus().equalsIgnoreCase(Constants.DISCHARGED_VESSEL) || vpBean.getVpStatus().equalsIgnoreCase(Constants.SAILED_VESSEL) && vpBean.getOps().equalsIgnoreCase(Constants.UNLOAD_OPERATION)) {
                setPortValue(dischargePort, vpBean.getCurrentPort());
                setPortValue(loadingPort, null);
            } else {
                setPortValue(loadingPort, vpBean.getCurrentPort());
                setPortValue(dischargePort, null);
            }

//            owners.setText(vpBean.getVessel().getOwner());
//            owners.setReadonly(true);
            agent.setText(vpBean.getAgentName());
            agent.setReadonly(true);
            eta.setValue(vpBean.getArrived() == null ? (vpBean.getEta() == null ? null : vpBean.getEta()) : vpBean.getArrived());
            eta.setDisabled(true);
            etb.setValue(vpBean.getBerthed() == null ? (vpBean.getEtb() == null ? null : vpBean.getEtb()) : vpBean.getBerthed());
            etb.setDisabled(true);
            ets.setValue(vpBean.getSailed() == null ? (vpBean.getEts() == null ? null : vpBean.getEts()) : vpBean.getSailed());
            ets.setDisabled(true);

//            setCountryToCombobox(cmbOwnerCountry, vpBean.getVessel().getOwnerCountry());

        }
    }

    private void setCountryToCombobox(Combobox cmb, OprCountryMaster country) {
        if (country != null) {
            boolean flag = false;
            List<Comboitem> comboItemList1 = cmb.getChildren();
            for (Comboitem cmbItem : comboItemList1) {
                OprCountryMaster countryMaster = (OprCountryMaster) cmbItem.getValue();
                if (countryMaster.getCountryId().equalsIgnoreCase(country.getCountryId())) {
                    System.out.println("Combo selected " + countryMaster.getCountryId());
                    cmb.setSelectedItem(cmbItem);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                cmb.setSelectedIndex(-1);
            }
        }
        cmb.setReadonly(true);
    }

    private void SaveCargo() {
        if (validateFields()) {
            currenCargoHdr = (OprCargoHdr) txtIGMNo.getAttribute("CARGO_OBJECT");
            if (currenCargoHdr == null) {
                System.out.println("new Entry to be save...");
                currenCargoHdr = new OprCargoHdr();
                currenCargoHdr.setCreatedBy(userMaster);
                currenCargoHdr.setCreatedDate(new Date());
                currenCargoHdr.setEntryStatus(Constants.NON_DELETED);
                currenCargoHdr.setOprVesselPosition(vpBean);
                currenCargoHdr.setCargoType(entryType);
                isNewRecord = true;
            } else {
                vpBean = currenCargoHdr.getOprVesselPosition();
                System.out.println("Old entry to be update.....");
                currenCargoHdr.setUpdatedBy(userMaster);
                currenCargoHdr.setUpdatedDate(new Date());
                isNewRecord = false;
            }
            currenCargoHdr.setCharterers(charterers.getText());
            currenCargoHdr.setCharterersCountry((OprCountryMaster) (cmbChartererCountry.getSelectedIndex() < 0 ? null : cmbChartererCountry.getSelectedItem().getValue()));

            currenCargoHdr.setAgentCountry((OprCountryMaster) (cmbAgentCountry.getSelectedIndex() < 0 ? null : cmbAgentCountry.getSelectedItem().getValue()));
            currenCargoHdr.setIgmNo(txtIGMNo.getText());
            currenCargoHdr.setEgmNo(txtEGMNo.getText());
            currenCargoHdr.setLoadPortMaster((OprPortMaster) (loadingPort.getSelectedIndex() < 0 ? null : loadingPort.getSelectedItem().getValue()));
            currenCargoHdr.setDischargePortMaster((OprPortMaster) (dischargePort.getSelectedIndex() < 0 ? null : dischargePort.getSelectedItem().getValue()));
            currenCargoHdr.setOwner(owners.getText());
            currenCargoHdr.setOwnerCountry((OprCountryMaster) (cmbOwnerCountry.getSelectedIndex() < 0 ? null : cmbOwnerCountry.getSelectedItem().getValue()));
            currenCargoHdr.setStatus(entryCheckbox.isChecked()?Constants.CARGO_ENTRY_COMPLETE:Constants.CARGO_ENTRY_NOT_COMPLETE);
            Set<OprCargoDtl> cargoDetails = currenCargoHdr.getOprCargoDtlCollection();
            cargoDetails = getCargoDetails();
            currenCargoHdr.setOprCargoDtlCollection(cargoDetails);
            CommonOperation.beginTransaction();
            currenCargoHdr = iCargoHdrObj.saveCargoHdrData(currenCargoHdr);
            boolean saveStatus = CommonOperation.commitTransaction("label.common.save");
            txtIGMNo.setAttribute("CARGO_OBJECT", currenCargoHdr);
            isNewRecord = false;
        }
    }

    private void setVPBeanForLOVFilter(String ops, OprPortMaster port) throws InterruptedException, ParseException {
//        int prevDays = 60;                     // We can put no of days by default value here
//        Date fromDate = frmDate.getValue() == null ? getDateBeforeXdays(prevDays) : frmDate.getValue();
//        Date to_Date = toDate.getValue() == null ? new Date() : toDate.getValue();
//        System.out.println("From Time   " + fromDate.getTime());
//        System.out.println("To Time   " + to_Date.getTime());

        vpBean.setEntryStatus(Constants.NON_DELETED);
        vpBean.setAgentName(agent.getText() == null ? "" : agent.getText());
        vpBean.setArrived(eta.getValue() == null ? null : eta.getValue());
        vpBean.setBerthed(etb.getValue() == null ? null : etb.getValue());
        vpBean.setSailed(ets.getValue() == null ? null : ets.getValue());
        vpBean.setFromDate(frmDate.getValue());
        vpBean.setToDate(toDate.getValue());
        vpBean.setVpType(entryType);
        vpBean.setEntryStatus(Constants.NON_DELETED);

//        vpBean.setVessel(vessel.getSelectedIndex() < 0 ? null : (OprVesselMaster) vessel.getSelectedItem().getValue());
        String lovFile = Labels.getLabel("app.cargo.vplov");
        System.out.println("____________before LOV");
        Window w = (Window) Executions.createComponents(lovFile, null, null);//("WEB-INF/presentation/LOV/Common_Lov.zul", null, null);
        w.setAttribute("OBJECT", vpBean);
        w.setAttribute("width", "600px");
        w.setAttribute("LOGGED_USER", userMaster);
        w.setMaximizable(true);
        w.doModal();
        System.out.println("____________after LOV  +"+w.getAttribute("OBJECT"));
        vpBean = w.getAttribute("OBJECT") != null ? (OprVesselPosition) w.getAttribute("OBJECT") : null;
        if (vpBean != null) {
            clearVPComponents();
            OprCargoHdr bean = new OprCargoHdr();
            bean.setOprVesselPosition(vpBean);
            bean.setEntryStatus(Constants.NON_DELETED);
            bean.setCargoType(entryType);
            if (Constants.UNLOAD_OPERATION.equalsIgnoreCase(ops)) {
                bean.setDischargePortMaster(vpBean.getCurrentPort());
            } else {
                bean.setLoadPortMaster(vpBean.getCurrentPort());
            }
            CommonOperation.beginTransaction();
            cargoHdrList = iCargoHdrObj.getCargoHdrList(bean);
            System.out.println("cargoHdrList.size  >>>>> " + cargoHdrList.size());
            if (cargoHdrList.isEmpty()) {
                System.out.println("_______________________________1");
                setVPDataToScreen(vpBean);
            } else {
                currenCargoHdr = cargoHdrList.get(0);
                System.out.println("_______________________________2");
                populateHeaderData(currenCargoHdr);
            }
            CommonOperation.commitTransaction("");
        } else {
            clearVPComponents();
        }

    }

    private Set<OprCargoDtl> getCargoDetails() {
        Set<OprCargoDtl> cargoList = new HashSet();
        List<Row> cargoRowList = cargoDetailsRows.getChildren();
        for (Row row : cargoRowList) {
            // if (((Checkbox) row.getChildren().get(0)).isChecked()) {
            OprCargoDtl dtlBean = (OprCargoDtl) row.getValue();
            if (dtlBean == null) {
                System.out.println("New Details Entry.........");
                dtlBean = new OprCargoDtl();
                dtlBean.setCreatedBy(userMaster);
                dtlBean.setCreatedDate(new Date());
                dtlBean.setOprCargoHdr(currenCargoHdr);
                row.setValue(dtlBean);
            } else {
                System.out.println("Old Details Entry.........");
                dtlBean.setUpdatedBy(userMaster);
                dtlBean.setUpdatedDate(new Date());
            }
            dtlBean.setEntryStatus(Constants.NON_DELETED);
            getCargoObj(row, dtlBean);
            cargoList.add(dtlBean);
            // }
        }
        return cargoList;
    }

    private void setPortValue(Combobox cmb, OprPortMaster selectedPort) {
        if (selectedPort != null) {
            boolean flag = false;
            List<Comboitem> comboItemList = cmb.getChildren();
            for (Comboitem cmbItem : comboItemList) {
                OprPortMaster oprPortMaster = (OprPortMaster) cmbItem.getValue();
                if (oprPortMaster.getPortId().equalsIgnoreCase(selectedPort.getPortId())) {
                    cmb.setSelectedItem(cmbItem);
                    //cmb.setReadonly(true);
                    //cmb.setDisabled(true);
                    flag = true;
                    break;
                }
            }
//            if (!flag) {
//                cmb.setSelectedIndex(-1);
//                cmb.setReadonly(false);
//            }
        }
//        else {
//            cmb.setSelectedIndex(-1);
//            cmb.setReadonly(false);
//        }
    }

    private void populateHeaderData(OprCargoHdr cargoHdrBean) {
        System.out.println("Populating Cagro Hdr data.....");
//        if(userMaster.getOprBranchMaster().getBranchId().equals("1") || cargoHdrBean.getCreatedBy().getOprBranchMaster().getBranchId() == null ? userMaster.getOprBranchMaster().getBranchId() == null : cargoHdrBean.getCreatedBy().getOprBranchMaster().getBranchId().equals(userMaster.getOprBranchMaster().getBranchId())){
        currenCargoHdr = cargoHdrBean;
        txtIGMNo.setAttribute("CARGO_OBJECT", currenCargoHdr);
        txtIGMNo.setText(cargoHdrBean.getIgmNo());
        txtEGMNo.setText(cargoHdrBean.getEgmNo());
        owners.setText(cargoHdrBean.getOwner());
        //System.out.println("VP Agent " + cargoHdrBean.getOprVesselPosition().getAgentName());
        entryCheckbox.setChecked(cargoHdrBean.getStatus().equalsIgnoreCase(Constants.CARGO_ENTRY_COMPLETE));
        vpBean = cargoHdrBean.getOprVesselPosition();
        setVPDataToScreen(cargoHdrBean.getOprVesselPosition());
        setPortValue(loadingPort, cargoHdrBean.getLoadPortMaster());
        setPortValue(dischargePort, cargoHdrBean.getDischargePortMaster());
        charterers.setText(cargoHdrBean.getCharterers());
        setCountryToCombobox(cmbChartererCountry, cargoHdrBean.getCharterersCountry());
        setCountryToCombobox(cmbOwnerCountry, cargoHdrBean.getOwnerCountry());
        setCountryToCombobox(cmbAgentCountry, cargoHdrBean.getAgentCountry());
        createCargoDetailRows(cargoHdrBean.getOprCargoDtlCollection());
//        }
    }

    private void findEntryType() {
        String screen = (String) dryCargoWin.getDesktop().getAttribute("screen");
        System.out.println("Entry Type   >>>>> " + screen);
        if (screen.contains("DRY_CARGO")) {
            entryType = Constants.DRYCARGO_ENTRY;
        } else if (screen.contains("LIQUID_CARGO")) {
            entryType = Constants.LIQUIDCARGO_ENTRY;
        }
    }

    private void deleteData() throws InterruptedException {
        System.out.println("Delete called......");
        int delResult = 0;
        delResult = Messagebox.show(Labels.getLabel("label.delete.confirmation"), Labels.getLabel("label.common.question"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);
        if (delResult == 16) // if Yes
        {
            int iCount = 0;
            int iIndex = 0;
            boolean deleteFlag = false;
            ArrayList<Row> arList = new ArrayList<Row>();
            Set deleteRecordsSet = new HashSet();
            for (int i = 0; i < (cargoDetailsRows.getChildren().size()); i++) {
                Row row = (Row) cargoDetailsRows.getChildren().get(i);
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
                if (!arList.isEmpty()) {
                    for (Row row : arList) {
                        OprCargoDtl dtlBean = (OprCargoDtl) row.getValue();
                        dtlBean.setEntryStatus(Constants.DELETED);
                        currenCargoHdr.setUpdatedBy(userMaster);
                        currenCargoHdr.setUpdatedDate(new Date());
                        dtlBean.setUpdatedBy(userMaster);
                        dtlBean.setUpdatedDate(new Date());
                        deleteRecordsSet.add(dtlBean);
                        cargoDetailsRows.removeChild(row);
                    }
                    System.out.println("Delete DTL from HDR......");
                    currenCargoHdr.setOprCargoDtlCollection(deleteRecordsSet);
                }
                currenCargoHdr = iCargoHdrObj.saveCargoHdrData(currenCargoHdr);
                CommonOperation.commitTransaction("com.manikssys.globalbilling.deleteStatus");
            } else // If No one is Checked
            {
                if (currenCargoHdr != null) {
                    for (int i = 0; i < (cargoDetailsRows.getChildren().size()); i++) {
                        Row row = (Row) cargoDetailsRows.getChildren().get(i);
                        OprCargoDtl dtlBean = (OprCargoDtl) row.getValue();
                        dtlBean.setEntryStatus(Constants.DELETED);
                        dtlBean.setUpdatedBy(userMaster);
                        dtlBean.setUpdatedDate(new Date());
                        deleteRecordsSet.add(dtlBean);
                    }
                    currenCargoHdr.setOprCargoDtlCollection(deleteRecordsSet);
                    currenCargoHdr.setEntryStatus(Constants.DELETED);
                    currenCargoHdr.setUpdatedBy(userMaster);
                    currenCargoHdr.setUpdatedDate(new Date());
                    CommonOperation.beginTransaction();
                    System.out.println("Delete HDR Data with Details");
                    currenCargoHdr = iCargoHdrObj.saveCargoHdrData(currenCargoHdr);
                    CommonOperation.commitTransaction("com.manikssys.globalbilling.deleteStatus");
                }
                if (!cargoHdrList.isEmpty()) {
                    if (cargoHdrList.size() == 1) {
                        cargoHdrList.remove(0);
                    } else if (currentIndex != (cargoHdrList.size() - 1)) {
                        System.out.println("Current Index 11 >> " + currentIndex);
                        getNextRecord();
                        System.out.println("Current Index 22 >> " + currentIndex);
                        cargoHdrList.remove(currentIndex - 1);
                    } else {

                        getFirstRecord();
                        cargoHdrList.remove(cargoHdrList.size() - 1);
                    }
                }
            }
        }
    }
}
