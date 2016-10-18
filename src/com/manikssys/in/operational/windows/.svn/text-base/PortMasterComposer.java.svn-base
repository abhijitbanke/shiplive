package com.manikssys.in.operational.windows;

import com.manikssys.in.common.CommonOperation;
import com.manikssys.in.common.ComponentValidation;
import com.manikssys.in.common.ICommonWindowInterface;
import com.manikssys.in.operational.beans.OprBranchMaster;
import com.manikssys.in.operational.beans.OprPortMaster;
import com.manikssys.in.operational.business.IPortMaster;
import com.manikssys.in.operational.business.PortMasterBs;
import com.manikssys.in.security.beans.ScrUserMaster;
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
 * Date: Jul 1, 2010
 */
public class PortMasterComposer extends GenericForwardComposer implements ICommonWindowInterface {

    private Textbox portCode;
    private Textbox portName;
    private Label portNameLbl;
    private Grid portMasterGrid;
    private Rows portMasterRows;
    private Window portMasterWin;
    private static Logger log;
    private ScrUserMaster loggedUser;
    private IPortMaster iPortBs;
    int iRowIndex = 0;
    private Listbox branch;
    private List<OprPortMaster> portList;
    public void onCreate$portMasterWin() {

        try {


            Executions.getCurrent().getDesktop().setAttribute("currentWindow", this);
            log = Logger.getLogger(PortMasterComposer.class);
            loggedUser = (ScrUserMaster) sessionScope.get("user");
            portName.addForward(Events.ON_CHANGING, portMasterWin, "onChangingComp");
            iPortBs = new PortMasterBs();
            CommonOperation.beginTransaction();
            // Get the port list
            for (OprBranchMaster branchBean : iPortBs.getBranchList(loggedUser)) {
                Listitem item = new Listitem();
                item.setLabel(branchBean.getBranchName());
                item.setValue(branchBean);
                branch.appendChild(item);
            }
            branch.setSelectedIndex(0);
            portList = iPortBs.fetchPortsList();
            fillPortsListGrid(portList);
            CommonOperation.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onChange$portCode() {
        ComponentValidation.removeErrorMessage(portCode);
        if(portCode.getAttribute("bean") == null)
            fillPortsListGrid(portList);
    }

    public void onChangingComp(ForwardEvent event) throws InterruptedException, Exception {
        System.out.println("...........onChangingComp............");
        if(portCode.getAttribute("bean") == null)
            fillPortsListGrid(portList,portNameLbl.getValue());
    }
    public void onChange$portName() {
        ComponentValidation.removeErrorMessage(portName);
    }

    public void onChange$branch() {
        ComponentValidation.removeErrorMessage(branch);
    }


    public void fillPortsListGrid(List<OprPortMaster> portList) {
        portMasterRows.getChildren().clear();
        if (portName.getText().isEmpty()) {
            for (OprPortMaster portBean : portList) {
                cretaeRow(portBean);
            }
        } else {
            for (OprPortMaster portBean : portList) {
                if (portBean.getPortName().toUpperCase().startsWith(portName.getText().toUpperCase())) {
                    cretaeRow(portBean);
                }
            }
        }
    }
    public void fillPortsListGrid(List<OprPortMaster> portList,String value) {
        System.out.println("_______________1__"+value);
        portMasterRows.getChildren().clear();
        if (value.isEmpty()) {
            for (OprPortMaster portBean : portList) {
                cretaeRow(portBean);
            }
        } else {
            for (OprPortMaster portBean : portList) {
                if (portBean.getPortName().toUpperCase().startsWith(value.toUpperCase())) {
                    cretaeRow(portBean);
                }
            }
        }
    }

    public void cretaeRow(OprPortMaster portBean) {

        Row row = new Row();
        row.setValue(portBean);
        row.setParent(portMasterRows);

        Div div = new Div();
        //div.setWidth("200px");
        div.setParent(row);

        Label lblSrNo = new Label();
        lblSrNo.setValue("" + (portMasterRows.getChildren().indexOf(row) + 1));
        lblSrNo.setParent(div);

        Checkbox chkBox = new Checkbox();
        chkBox.setParent(div);

        Toolbarbutton txtPortCode = new Toolbarbutton(portBean.getPortCode());
        txtPortCode.addForward(Events.ON_CLICK, portMasterWin, "onClickPortCode");
        txtPortCode.setParent(row);

        Label txtPortName = new Label();
        txtPortName.setValue(portBean.getPortName());
        txtPortName.setParent(row);
    }

    public void onClickPortCode(ForwardEvent event) {
        try {

            Toolbarbutton targetLink = (Toolbarbutton) event.getOrigin().getTarget();
            Row targetRow = (Row) targetLink.getParent();
            OprPortMaster portBean = (OprPortMaster) targetRow.getValue();
            iRowIndex = portMasterRows.getChildren().indexOf(targetRow);
            System.out.println("iRowIndex     ===== "+iRowIndex);
            if (portBean != null) {
                populateFields(portBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateFields(OprPortMaster portBean) {
        portCode.setText(portBean.getPortCode());
        portCode.setAttribute("bean", portBean);
        portCode.setReadonly(false);
        portName.setText(portBean.getPortName());
        if(portBean.getBranchId() != null){
         for (Object obj : branch.getChildren()) {
            Listitem itm = (Listitem) obj;
            if (itm.getValue() != null && ((OprBranchMaster) itm.getValue()).equals(portBean.getBranchId())) {
                itm.setSelected(true);
                break;
            }
        }
        }else{
            System.out.println("Select Null");
            branch.setSelectedIndex(0);
        }
    }

    public void modifyRow(int iRIndex, OprPortMaster portBean) {
        Row row = (Row) portMasterRows.getChildren().get(iRIndex);
        row.setValue(portBean);
        Toolbarbutton txtCode = (Toolbarbutton) row.getChildren().get(1); // Port Code
        txtCode.setLabel(portBean.getPortCode());
        Label txtName = (Label) row.getChildren().get(2); // Port Name
        txtName.setValue(portBean.getPortName());
    }

    // method to generate Serial No for each row in grid
    public void genSerialNumber(int iStartIndex) {
        for (int i = iStartIndex; i < portMasterRows.getChildren().size(); i++) {
            Row row = (Row) portMasterRows.getChildren().get(i);
            Div div = (Div) row.getChildren().get(0); // get Sr.No. Field
            Label txtSrNo = (Label) div.getChildren().get(0);
            txtSrNo.setValue((Integer.parseInt(txtSrNo.getValue()) - 1) + "");
        }
    }

    public void resetFields() {
        portCode.setText("");
        portCode.setAttribute("bean", null);
        portCode.setReadonly(false);
        portName.setText("");
        branch.setSelectedIndex(0);
        iRowIndex = 0;
        CommonOperation.beginTransaction();
            portList = iPortBs.fetchPortsList();
            fillPortsListGrid(portList);
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
            OprPortMaster portMaster = null;
            portMaster = (OprPortMaster) portCode.getAttribute("bean");
            if (portMaster == null) {
                isNew = true;
                portMaster = new OprPortMaster();
                portMaster.setLeSeq(100000);
                portMaster.setLwSeq(100000);
                portMaster.setDeSeq(100000);
                portMaster.setDwSeq(100000);
            }
            portMaster.setPortCode(portCode.getText());
            portMaster.setPortName(portName.getText());
            portMaster.setBranchId((OprBranchMaster) branch.getSelectedItem().getValue());
            portMaster.setCreatedBy(loggedUser);
            portMaster.setCreatedDate(new Date());
            portMaster.setUpdatedBy(loggedUser);
            portMaster.setUpdatedDate(new Date());
            portMaster.setStatus("1");

            CommonOperation.beginTransaction(); // Here Session transaction begins

            // check wheather kos is already exist or not

            boolean isExist = isNew ? iPortBs.isPortExist(portMaster.getPortCode()) : false;

            if (!isExist) {
                // save the kind of solution
                portMaster = iPortBs.savePort(portMaster);

                if (isNew) {
                    cretaeRow(portMaster);
                } else {
                    modifyRow(iRowIndex, portMaster);
                }
                
                log.info("--- Saving Port ---" + portCode.getText());
                CommonOperation.commitTransaction("label.opr.port.save");
                resetFields();

            } else {
                log.info("--- Port Already Exist ---" + portCode.getText());
                ComponentValidation.showErrorMessage(new WrongValueException(portCode, Labels.getLabel("label.opr.port.exist")));  // throw exception on Port Code
                CommonOperation.commitTransaction("");
            }
        }
    }

    public boolean validateFields() {
        boolean flag = true;
        WrongValueException e = ComponentValidation.validateManditoryTextbox(portCode, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag = false;
        }
        e = ComponentValidation.validateManditoryTextbox(portName, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag = false;
        }
        e = ComponentValidation.validateManditoryList(branch, null);
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
        fillPortsListGrid(portList);
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
                for (int i = 0; i < portMasterRows.getChildren().size(); i++) {
                    Row row = (Row) portMasterRows.getChildren().get(i);
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
                        OprPortMaster portBean = (OprPortMaster) row.getValue();
                        portBean.setStatus("0");
                        iPortBs.savePort(portBean);
                        portMasterRows.removeChild(row);
                        portList.remove(portBean);
                    }
                    CommonOperation.commitTransaction("label.opr.delete");
                    resetFields();
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
