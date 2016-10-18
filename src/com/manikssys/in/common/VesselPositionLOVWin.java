/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.common;

import com.manikssys.in.operational.beans.OprCargoHdr;
import com.manikssys.in.operational.beans.OprVesselPosition;
import com.manikssys.in.operational.business.IVesselPosition;
import com.manikssys.in.operational.business.OprVesselPositionBS;
import com.manikssys.in.security.beans.ScrUserMaster;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.*;
import org.zkoss.zul.impl.XulElement;

/**
 *
 * @author pc
 */
public class VesselPositionLOVWin extends Window implements ICommonWindowInterface, org.zkoss.zk.ui.event.EventListener {

    private Map data = null;
    private ArrayList column = null;
    private ArrayList row = null;
    private Row myRow = null;
    private Paging paging = null;
    private int pgno = 0;
    private String radioCheck = "Y";
    private String sWidth;
    private Window thisWindow = null;
    SessionFactory sessionFactory = null;
    Configuration cb = null;
    Session session = null;
    private ScrUserMaster loggedUser;
    private IVesselPosition iVPBs;
    private List<OprVesselPosition> vpList;
    Rows rows = null;
    List<CommonStatus> vpStatusList = getVPStatusList();

    public void clearScreeen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void saveRecord() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addRecord() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void closeScreen() {
        thisWindow.setAttribute("OBJECT", null);
        closeThisWindow();
    }

    public void retrieveRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deleteRecord() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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

    public void onEvent(Event event) throws Exception {
        XulElement evtTarget = (XulElement) event.getTarget();
        if ("org.zkoss.zul.Row".equalsIgnoreCase(evtTarget.getClass().getName())) {
            System.out.println("onRowEvebt..........");
            Row targetRow = (Row) evtTarget;
            ((Radio) targetRow.getChildren().get(0)).setChecked(true);
            myRow = targetRow;
        }
    }

    public void onLoad() throws ParseException {
        System.out.println("OnLoad Called ....................");
        cb = new Configuration().configure();
        sessionFactory = cb.buildSessionFactory();
        session = sessionFactory.openSession();
        rows = (Rows) this.getFellow("myRow");
        thisWindow = this;
        sWidth = (String) thisWindow.getAttribute("width");
        sWidth = sWidth != null ? sWidth : "400px";
        thisWindow.setWidth(sWidth);//width="760px"
        iVPBs = new OprVesselPositionBS();
        loggedUser = (ScrUserMaster) thisWindow.getAttribute("LOGGED_USER");
        getRecords();
    }

    /**
     * Method to get records.
     */
    public void getRecords() throws ParseException {

        if (myRow != null) {
            ((Radio) myRow.getChildren().get(0)).setChecked(false);
        }
        if (thisWindow.getAttribute("OBJECT") != null) {
            OprVesselPosition vpBean = (OprVesselPosition) thisWindow.getAttribute("OBJECT");
            CommonOperation.beginTransaction();
            vpList = iVPBs.getVPListForLimitedDays(vpBean);
            System.out.println("vpList size  >>>>>>>>>" + vpList.size());
            if (vpList.size() > 0) {
                drawVPColumns();
                setGridValues();
                CommonOperation.commitTransaction();

            } else {
                CommonOperation.commitTransaction("app.comman.err.search.recodNotFound");
                thisWindow.setAttribute("OBJECT", null);
                closeThisWindow();
            }
        } else {
        }

    }

    public void setGridValues() {
        rows.getChildren().clear();
        int count = 0;
        if (loggedUser.getOprBranchMaster().getBranchId().equals("1")) {
            for (OprVesselPosition bean : vpList) {
                if (isCargoEntryExist(bean)) {
                    Row row_ = drawVPRow(bean);
                    row_.setParent(rows);
                    if (count == 0) {
                        myRow = row_;
                    }
                    count++;
                }
            }
        } else {
            for (OprVesselPosition bean : vpList) {
                if (loggedUser.getOprBranchMaster().getBranchId().equals(bean.getCreatedBy().getOprBranchMaster().getBranchId()) && isCargoEntryExist(bean)) {

                    Row row_ = drawVPRow(bean);
                    row_.setParent(rows);
                    if (count == 0) {
                        myRow = row_;
                    }
                    count++;
                }
            }

        }
        if (count == 1) {
            assignValue();
        }

    }

    /**
     * Method to draw columns of grid
     */
    public void drawVPColumns() {
        System.out.println("CargoHDR LOV drawColumns() called");
        Columns objColumns = (Columns) this.getFellow("objCol");
        Column objcolumn = null;
        ArrayList listofCol = new ArrayList();
        listofCol.add(0, "Sel");
        listofCol.add(1, "Vessel");
        listofCol.add(2, "Port");
        listofCol.add(3, "Agent");
        listofCol.add(4, "Berth");
        listofCol.add(5, "Arrived");
        listofCol.add(6, "Berthed");
        listofCol.add(7, "Sailed");
        listofCol.add(8, "Status");
        listofCol.add(9, "OPS");
//        listofCol.add(10, "CargoEntry");
        int i;
        for (i = 0; i < listofCol.size(); i++) {
            objcolumn = new Column();

            objcolumn.setLabel((String) listofCol.get(i));
            objcolumn.setStyle("background:gray;");
            if (i == 0) {
                objcolumn.setStyle("background:gray;");
                objcolumn.setWidth("10px");
            } else {
                String px = ("" + listofCol.get(i)).length() + "0px";
                objcolumn.setWidth(px);
            }
            objColumns.setSizable(true);
            objColumns.appendChild(objcolumn);

        }
    }

    /**
     * Method to draw columns of grid without radio
     */
    public void drawColumnsWORadio() {
        int i;
        Column objcolumn = null;
        Columns objColumns = (Columns) this.getFellow("objCol");
        //System.out.println("ArrayList size " + column.size());
        ArrayList listofCol = ((CommonGetterandSetter) (column.get(0))).getHeader();
        //System.out.println("listofCol ArrayList size " + listofCol.size());
        int colWidth = (listofCol.size() * 2) - 1;
        for (i = 0; i < listofCol.size() + 1; i++) {
            objcolumn = new Column();
            if (i == 0) {
            } else {
                String px = ("" + listofCol.get(i - 1)).length() + "0px";
                ////System.out.println(listofCol.get(i - 1)+":px:"+px);
                objcolumn.setLabel("" + listofCol.get(i - 1));
                objColumns.setSizable(true);
                if (i == 1) {
                    objcolumn.setWidth((90 / colWidth) + "%");
                } else {
                    objcolumn.setWidth(((90 / colWidth) * 2) + "%");
                }
                objColumns.appendChild(objcolumn);
            }

        }

    }

    /**
     *
     * @param result Lsit containing data
     * @param b page number
     * @param e page size
     * @param rows ZK Rows object
     */
    public void redraw(List result, int b, int e, Rows rows) {
        b = b + 1;
        int f = (b * e) - e;
        int g = b * e;
        if (myRow != null) {
            ((Radio) myRow.getChildren().get(0)).setChecked(false);
        }
        rows.getChildren().clear();
        for (int i = f; i < g && i < result.size(); i++) {
            CommonGetterandSetter gettersetter = (CommonGetterandSetter) result.get(i);
            Row r = drawRow(gettersetter);
            rows.appendChild(r);
        }
    }

    public Row drawRow(CommonGetterandSetter gettersetter) {

        //System.out.println("---" + row.size());
        int i;
        Row objRow = null;
        Textbox lbl = null;
        //Rows objrows = (Rows) this.getFellow("myRow");
        ArrayList listofCol = ((CommonGetterandSetter) (column.get(0))).getHeader();
        //System.out.println("Size of the listofCol.size()" + listofCol.size());

        objRow = new Row();
        objRow.addEventListener("onClick", this);

        for (i = 1; i <= listofCol.size() + 1; i++) {
            //System.out.println("Value---" + gettersetter.getColValue(i));
            if (i == 1) {
                Radio radioButton = new Radio();
                radioButton.setWidth("20px");
                radioButton.addEventListener("onCheck", this);
                radioButton.setParent(objRow);
                //objRow.appendChild(radioButton);
            } else {
                lbl = new Textbox();
                lbl.setReadonly(true);
                if (i == 2) {
                    lbl.setWidth("100%");
                } else if (i == 3) {
                    lbl.setWidth("100%");
                } else {
                    lbl.setWidth("100%");
                }
                if (i == listofCol.size() + 1) {
                    lbl.setStyle("color:#000000; text-align:left;");
                } else {
                    lbl.setStyle("color:#000000; text-align:left;");
                }
                lbl.setValue(gettersetter.getColValue(i - 1));
                lbl.setTooltiptext(gettersetter.getColValue(i - 1));

                objRow.appendChild(lbl);
            }
        }
        objRow.setValue(gettersetter);

        /*Columns objColumns = (Columns) this.getFellow("objCol");
        List collist = objColumns.getChildren();
        int count = collist.size();
        Column col1 = (Column) collist.get(count - 1);
        Column col2 = (Column) collist.get(count - 2);
        col1.setVisible(false);
        col2.setVisible(false);*/

        return objRow;
    }

    public void redrawWORadio(List result, int b, int e, Rows rows) {
        b = b + 1;
        int g = b * e;
        int f = g - e;
        if (myRow != null) {
            ((Radio) myRow.getChildren().get(0)).setChecked(false);
        }
        rows.getChildren().clear();
        for (int i = f; i < g && i < result.size(); i++) {
            CommonGetterandSetter gettersetter = (CommonGetterandSetter) result.get(i);
            Row r = drawRowWORadio(gettersetter);
            rows.appendChild(r);
        }
    }

    public Row drawRowWORadio(CommonGetterandSetter gettersetter) {

        //System.out.println("---" + row.size());
        int i;
        Row objRow = null;
        Textbox lbl = null;
        //Rows objrows = (Rows) this.getFellow("myRow");
        ArrayList listofCol = ((CommonGetterandSetter) (column.get(0))).getHeader();
        //System.out.println("Size of the listofCol.size()" + listofCol.size());

        objRow = new Row();
        objRow.addEventListener("onClick", this);

        for (i = 1; i <= listofCol.size() + 1; i++) {
            //System.out.println("Value---" + gettersetter.getColValue(i));
            if (i == 1) {
//                Radio radioButton = new Radio();
//                radioButton.setWidth("20px");
//                radioButton.addEventListener("onCheck", this);
//                radioButton.setParent(objRow);
                //objRow.appendChild(radioButton);
            } else {
                lbl = new Textbox();
                lbl.setReadonly(true);
                if (i == 2) {
                    lbl.setWidth("100%");
                } else if (i == 3) {
                    lbl.setWidth("100%");
                } else {
                    lbl.setWidth("100%");
                }
                if (i == listofCol.size() + 1) {
                    lbl.setStyle("color:#000000; text-align:left;");
                } else {
                    lbl.setStyle("color:#000000; text-align:left;");
                }

                lbl.setValue(gettersetter.getColValue(i - 1));
                lbl.setTooltiptext(gettersetter.getColValue(i - 1));
                objRow.appendChild(lbl);
            }
        }
        objRow.setValue(gettersetter);
        return objRow;
    }

    public void assignValue() {
        System.out.println("Ok Clicked....");
        try {
            if (myRow != null) {
                System.out.println("myRow...." + myRow.getValue());
                thisWindow.setAttribute("OBJECT", myRow.getValue());
                closeThisWindow();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            //System.out.println(exc.getMessage());
        }
    }

    public void cancelButtonClick() {
        thisWindow.setAttribute("OBJECT", null);
        closeThisWindow();
    }

    public void closeThisWindow() {
        this.detach();
    }

    private Row drawVPRow(OprVesselPosition bean) {
        Row newRow = new Row();
        newRow.setValue(bean);
        newRow.addEventListener("onClick", this);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        Radio radio = new Radio();
        radio.setChecked(false);
        radio.setParent(newRow);

        Label vesselLbl = new Label(bean.getVessel().getVesselName());
        vesselLbl.setParent(newRow);

        Label portLbl = new Label(bean.getCurrentPort().getPortName());
        portLbl.setParent(newRow);
        Label agentLbl = new Label(bean.getAgentName() == null ? "" : bean.getAgentName());
        agentLbl.setParent(newRow);

        Label berthLbl = new Label(bean.getJetty() == null ? "" : bean.getJetty());
        berthLbl.setParent(newRow);
        Label arrivedLbl = new Label(bean.getArrived() == null ? "" : sdf.format(bean.getArrived()));
        arrivedLbl.setParent(newRow);
        // arrivedLbl.setWidth(bean.getArrived() == null ? "10px":(sdf.format(bean.getArrived()).length()+100)+"px");
        Label berthedLbl = new Label(bean.getBerthed() == null ? "" : sdf.format(bean.getBerthed()));
        berthedLbl.setParent(newRow);
        Label sailedLbl = new Label(bean.getSailed() == null ? "" : sdf.format(bean.getSailed()));
        sailedLbl.setParent(newRow);
        Label statusLbl = new Label(getBeanVPStatus(bean.getVpStatus()));
        statusLbl.setParent(newRow);
        Label opsLbl = new Label(bean.getOps() == null ? "" : getOpsName(bean.getOps()));
        opsLbl.setParent(newRow);
//        Label cargoStatement = new Label(bean.isIsCargoEntryExist() ? "Entered" : "Not Entered");
//        cargoStatement.setParent(newRow);
        return newRow;

    }

    public List<CommonStatus> getVPStatusList() {
        List<CommonStatus> status_List = new ArrayList<CommonStatus>();
        status_List.add(new CommonStatus(Constants.EXPECTED_VESSEL, "Expected"));
        status_List.add(new CommonStatus(Constants.ARRIVED_VESSEL, "Arrived"));
        status_List.add(new CommonStatus(Constants.WAITING_VESSEL, "Waiting"));
        status_List.add(new CommonStatus(Constants.BERTHED_VESSEL, "Berthed"));
        status_List.add(new CommonStatus(Constants.DISCHARGED_VESSEL, "Discharged"));
        status_List.add(new CommonStatus(Constants.SHIFTED_VESSEL, "Shifted"));
        status_List.add(new CommonStatus(Constants.REBERTHING_VESSEL, "Reberthing"));
        status_List.add(new CommonStatus(Constants.SAILED_VESSEL, "Sailed"));
        status_List.add(new CommonStatus(Constants.VACCANT_VESSEL, "Vaccant"));
        status_List.add(new CommonStatus(Constants.OCCUPIED_VESSEL, "Occupied"));
//        status_List.add(new CommonStatus(Constants.DECOMMISIONED_VESSEL, "Decommisioned"));
        return status_List;
    }

    private String getBeanVPStatus(String status) {
        String statusName = "";
        for (CommonStatus cs : vpStatusList) {
            if (cs.statusID.equals(status)) {
                statusName = cs.getStatusName();
            }
        }

        return statusName;
    }

    private String getOpsName(String opsID) {
        String opsName = "";
        if (opsID.equalsIgnoreCase(Constants.LOAD_OPERATION)) {
            opsName = "L";
        } else if (opsID.equalsIgnoreCase(Constants.LOAD_UNLOAD_OPERATION)) {
            opsName = "L/D";
        } else if (opsID.equalsIgnoreCase(Constants.UNLOAD_OPERATION)) {
            opsName = "D";
        } else {
            opsName = "";
        }
        return opsName;
    }

    private boolean isCargoEntryExist(OprVesselPosition bean) {
        System.out.println("isCargoEntryExist     VP >> " + bean.getVesselPosId());
        boolean flag = false;
        if (bean.getOprCargoHdrCollection() != null && !bean.getOprCargoHdrCollection().isEmpty()) {
            Object[] arrList = bean.getOprCargoHdrCollection().toArray();
            OprCargoHdr hdrBean = (OprCargoHdr) arrList[0];
            System.out.println("__________hdrBean.getStatus()____________" + hdrBean.getStatus());
            return hdrBean.getStatus().equalsIgnoreCase(Constants.CARGO_ENTRY_NOT_COMPLETE);
        } else {
            return true;
        }
    }
}
