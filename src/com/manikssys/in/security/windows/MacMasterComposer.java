package com.manikssys.in.security.windows;

import com.manikssys.in.common.CommonOperation;
import com.manikssys.in.common.ComponentValidation;
import com.manikssys.in.common.Constants;
import com.manikssys.in.common.ICommonWindowInterface;
import com.manikssys.in.security.beans.ScrMacMaster;
import com.manikssys.in.security.beans.ScrUserMaster;
import com.manikssys.in.security.business.IMACMaster;
import com.manikssys.in.security.business.MACMasterBs;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: sandeep
 * Date: Jul 9, 2010
 */
public class MacMasterComposer extends GenericForwardComposer implements ICommonWindowInterface {

    private Textbox macId;
    private Textbox machineName;
    private Label macAddressLbl;
    private Grid MACMasterGrid;
    private Rows MACMasterRows;
    private Window MACMasterWin;
    private static Logger log;
    private ScrUserMaster loggedUser;
    private IMACMaster iMACBs;
    int iRowIndex = 0;
    List<ScrMacMaster> macList;

    public void onCreate$MACMasterWin() {

        try {

            Executions.getCurrent().getDesktop().setAttribute("currentWindow", this);
            log = Logger.getLogger(MacMasterComposer.class);
            loggedUser = (ScrUserMaster) sessionScope.get("user");
            macId.addForward(Events.ON_CHANGING, MACMasterWin, "onChangingComp");
            iMACBs = new MACMasterBs();
            CommonOperation.beginTransaction();
            // Get the MAC list
            macList = iMACBs.fetchMACList();
            fillMACListGrid(macList);
            CommonOperation.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onChange$macId() {
        ComponentValidation.removeErrorMessage(macId);
        if (macId.getAttribute("bean") == null) {
            fillMACListGrid(macList);
        }
    }

    public void onChangingComp(ForwardEvent event) throws InterruptedException, Exception {
        System.out.println("...........onChangingComp............");
        if (macId.getAttribute("bean") == null) {
            fillMACListGrid(macList, macAddressLbl.getValue());
        }
    }

    public void onChange$machineName() {
        ComponentValidation.removeErrorMessage(machineName);
    }

    public void fillMACListGrid(List<ScrMacMaster> macList) {
        MACMasterRows.getChildren().clear();
        if (macId.getText().isEmpty()) {
            for (ScrMacMaster macBean : macList) {
                cretaeRow(macBean);
            }
        } else {
            for (ScrMacMaster macBean : macList) {
                if (macBean.getMacId().toUpperCase().startsWith(macId.getText().toUpperCase())) {
                    cretaeRow(macBean);
                }
            }
        }
    }

    public void fillMACListGrid(List<ScrMacMaster> macList, String value) {
        MACMasterRows.getChildren().clear();
        if (value.isEmpty()) {
            for (ScrMacMaster macBean : macList) {
                cretaeRow(macBean);
            }
        } else {
            for (ScrMacMaster macBean : macList) {
                if (macBean.getMacId().toUpperCase().startsWith(value.toUpperCase())) {
                    cretaeRow(macBean);
                }
            }
        }
    }

    public void cretaeRow(ScrMacMaster macBean) {

        Row row = new Row();
        row.setValue(macBean);
        row.setParent(MACMasterRows);

        Div div = new Div();
        //div.setWidth("200px");
        div.setParent(row);

        Label lblSrNo = new Label();
        lblSrNo.setValue("" + (MACMasterRows.getChildren().indexOf(row) + 1));
        lblSrNo.setParent(div);

        Checkbox chkBox = new Checkbox();
        chkBox.setParent(div);

        Toolbarbutton txtMacId = new Toolbarbutton(macBean.getMacId());
        txtMacId.addForward(Events.ON_CLICK, MACMasterWin, "onClickMACId");
        txtMacId.setParent(row);

        Label txtMachineName = new Label();
        txtMachineName.setValue(macBean.getMachineName());
        txtMachineName.setParent(row);
    }

    public void onClickMACId(ForwardEvent event) {
        try {

            Toolbarbutton targetLink = (Toolbarbutton) event.getOrigin().getTarget();
            Row targetRow = (Row) targetLink.getParent();
            ScrMacMaster macBean = (ScrMacMaster) targetRow.getValue();
            iRowIndex = MACMasterRows.getChildren().indexOf(targetRow);
            if (macBean != null) {
                populateFields(macBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateFields(ScrMacMaster macBean) {
        macId.setText(macBean.getMacId());
        macId.setAttribute("bean", macBean);
        macId.setReadonly(true);
        machineName.setText(macBean.getMachineName());
    }

    public void modifyRow(int iRIndex, ScrMacMaster macBean) {
        Row row = (Row) MACMasterRows.getChildren().get(iRIndex);
        row.setValue(macBean);
        Toolbarbutton txtMacId = (Toolbarbutton) row.getChildren().get(1); // Mac Id
        txtMacId.setLabel(macBean.getMacId());
        Label txtMachineName = (Label) row.getChildren().get(2); // Machine Name
        txtMachineName.setValue(macBean.getMachineName());
    }

    // method to generate Serial No for each row in grid
    public void genSerialNumber(int iStartIndex) {
        for (int i = iStartIndex; i < MACMasterRows.getChildren().size(); i++) {
            Row row = (Row) MACMasterRows.getChildren().get(i);
            Div div = (Div) row.getChildren().get(0); // get Sr.No. Field
            Label txtSrNo = (Label) div.getChildren().get(0);
            txtSrNo.setValue((Integer.parseInt(txtSrNo.getValue()) - 1) + "");
        }
    }

    public void resetFields() {
        macId.setText("");
        macId.setAttribute("bean", null);
        macId.setReadonly(false);
        machineName.setText("");
        iRowIndex = 0;
        macList = null;
        CommonOperation.beginTransaction();
        // Get the MAC list
        macList = iMACBs.fetchMACList();
        fillMACListGrid(macList);
        CommonOperation.commitTransaction();

    }
    // Implemented Methods

    public void clearScreeen() {
        resetFields();
    }

    public void saveRecord() throws Exception {
        // validate the fields
        if (validateFields()) {
            boolean isNew = false;
            ScrMacMaster macMaster = null;
            macMaster = (ScrMacMaster) macId.getAttribute("bean");
            if (macMaster == null) {
                isNew = true;
                macMaster = new ScrMacMaster();
            }
            macMaster.setMacId(macId.getText());
            macMaster.setMachineName(machineName.getText());
            macMaster.setCreatedBy(loggedUser);
            macMaster.setCreatedDate(new Date());
            macMaster.setUpdatedBy(loggedUser);
            macMaster.setUpdatedDate(new Date());
            macMaster.setStatus(Constants.NON_DELETED);

            CommonOperation.beginTransaction(); // Here Session transaction begins

            // check wheather kos is already exist or not

            boolean isExist = isNew ? iMACBs.isMACExist(macMaster.getMacId()) : false;

            if (!isExist) {
                // save the kind of solution
                macMaster = iMACBs.saveMAC(macMaster);

                if (isNew) {
                    cretaeRow(macMaster);
                } else {
                    modifyRow(iRowIndex, macMaster);
                }

                log.info("--- Saving MAC ---" + macId.getText());
                CommonOperation.commitTransaction("label.mac.save");
                resetFields();

            } else {
                log.info("--- MAC Already Exist ---" + macId.getText());
                ComponentValidation.showErrorMessage(new WrongValueException(macId, Labels.getLabel("label.mac.exist")));  // throw exception on MAC Id
                CommonOperation.commitTransaction("");
            }
        }
    }

    public boolean validateFields() {
        boolean flag = true;
        WrongValueException e = ComponentValidation.validateManditoryTextbox(macId, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag = false;
        }
        e = ComponentValidation.validateManditoryTextbox(machineName, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag = false;
        }
        return flag;
    }

    public void addRecord() throws Exception {
        resetFields();
    }

    public void closeScreen() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void retrieveRecord() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteRecord() throws Exception {
        int delResult = 0;
        try {
            delResult = Messagebox.show(Labels.getLabel("label.delete.confirmation"), Labels.getLabel("label.common.question"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);

            if (delResult == 16) // if Yes
            {
                int iCount = 0;
                int iIndex = 0;
                boolean deleteFlag = false;
                ArrayList<Row> arList = new ArrayList<Row>();
                for (int i = 0; i < MACMasterRows.getChildren().size(); i++) {
                    Row row = (Row) MACMasterRows.getChildren().get(i);
                    Div div = (Div) row.getChildren().get(0); // get Sr.No. Field
                    Checkbox chkBox = (Checkbox) div.getChildren().get(1);
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
                        ScrMacMaster macBean = (ScrMacMaster) row.getValue();
                        macBean.setStatus(Constants.DELETED);
                        macBean.setUpdatedBy(loggedUser);
                        macBean.setUpdatedDate(new Date());
                        iMACBs.deleteMAC(macBean);
                        MACMasterRows.removeChild(row);
                        macList.remove(macBean);
                    }
                    resetFields();
                    CommonOperation.commitTransaction("label.opr.delete");
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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void getFirstRecord() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void getLastRecord() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void getNextRecord() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void getPreviousRecord() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
