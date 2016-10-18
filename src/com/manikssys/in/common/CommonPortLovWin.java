/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.common;

import com.manikssys.in.operational.beans.OprPortMaster;
import com.manikssys.in.operational.business.IPortMaster;
import com.manikssys.in.operational.business.PortMasterBs;
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
public class CommonPortLovWin extends Window implements ICommonWindowInterface, org.zkoss.zk.ui.event.EventListener {

    private Map data = null;
    private ArrayList column = null;
    private ArrayList row = null;
    private Row myRow = null;
    private Paging paging = null;
    private int pgno = 0;
    private String radioCheck = "Y";
    private String sWidth;
    private Window thisWindow = null;
    Textbox param;
    SessionFactory sessionFactory = null;
    Configuration cb = null;
    Session session = null;
    //Rows objrows = null;
    private IPortMaster iPMBs;
    private List<OprPortMaster> portList;
    Rows rows = null;

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
        throw new UnsupportedOperationException("Not supported yet.");
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
            Row row = (Row) evtTarget;
            ((Radio) row.getChildren().get(0)).setChecked(true);
            myRow = row;
        }
        if (evtTarget == param) {
            setGridValues();
        }
    }

    public void onLoad() {

        cb = new Configuration().configure();
        sessionFactory = cb.buildSessionFactory();
        session = sessionFactory.openSession();
        rows = (Rows) this.getFellow("myRow");
        thisWindow = this;
        sWidth = (String) thisWindow.getAttribute("width");
        sWidth = sWidth != null ? sWidth : "400px";
        thisWindow.setWidth(sWidth);//width="760px"
        iPMBs = new PortMasterBs();
        param = (Textbox) this.getFellow("param");
//        param.addEventListener( "onChanging", this);
        if (thisWindow.getAttribute("PARAM_TEXT") != null) {
            param.setText(thisWindow.getAttribute("PARAM_TEXT").toString());
        }
        getRecords();
    }

    public void getRecords() {

        if (myRow != null) {
            ((Radio) myRow.getChildren().get(0)).setChecked(false);
        }

//        if (thisWindow.getAttribute("OBJECT") != null) {
//            OprVesselMaster vesselBean = (OprVesselMaster) thisWindow.getAttribute("OBJECT");
        CommonOperation.beginTransaction();
        portList = iPMBs.fetchPortsList();
        System.out.println("vpList size  >>>>>>>>>" + portList.size());
        if (portList.size() > 0) {
            drawPortColumn();
            setGridValues();
            CommonOperation.commitTransaction();
        } else {
            CommonOperation.commitTransaction("app.comman.err.search.recodNotFound");
            thisWindow.setAttribute("OBJECT", null);
            closeThisWindow();
        }
//        } else {
//        }

    }

    public void setGridValues() {

        //objrows = (Rows) this.getFellow("myRow");

        rows.getChildren().clear();
        int count = 0;
        for (OprPortMaster bean : portList) {
            System.out.println("Port Name " + bean.getPortName());
            if (bean.getPortName().toUpperCase().startsWith(param.getText().toUpperCase())) {
                Row row_ = drawPortRow(bean);
                row_.setParent(rows);
                if (count == 0) {
                    myRow = row_;
                }
                count++;
            }
        }
        if (count == 1) {
            assignValue();
        }
    }

    public void closeThisWindow() {
        this.detach();
    }

    private void drawPortColumn() {
        System.out.println("vesselLOV drawColumns() called");
        Columns objColumns = (Columns) this.getFellow("objCol");
        Column objcolumn = null;
        ArrayList listofCol = new ArrayList();
        listofCol.add(0, "Sel");
        listofCol.add(1, "Port");
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

    private Row drawPortRow(OprPortMaster bean) {
        Row row_ = new Row();
        row_.setValue(bean);
        row_.addEventListener("onClick", this);

        Radio radio = new Radio();
        radio.setChecked(false);
        radio.setParent(row_);

        Label vesselLbl = new Label(bean.getPortName());
        vesselLbl.setParent(row_);
        return row_;

    }

    public void assignValue() {
        try {
            if (myRow != null) {
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
}
