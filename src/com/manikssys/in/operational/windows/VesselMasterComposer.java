/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.operational.windows;

import com.manikssys.in.common.CommonOperation;
import com.manikssys.in.common.ComponentValidation;
import com.manikssys.in.common.ICommonWindowInterface;
import com.manikssys.in.operational.beans.OprCountryMaster;
import com.manikssys.in.operational.beans.OprVesselMaster;
import com.manikssys.in.operational.business.IOprVesselMaster;
import com.manikssys.in.operational.business.OprVesselMasterBS;
import com.manikssys.in.security.beans.ScrUserMaster;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;

/**
 *
 * @author pc
 */
public class VesselMasterComposer extends GenericForwardComposer implements ICommonWindowInterface {

    private Textbox vesselName;
    private Textbox owner;
    private Combobox ownerCountry;
    private Grid vesselMasterGrid;
    private Rows vesselMasterRows;
    private Window vesselMasterWin;
    private static Logger log;
    private ScrUserMaster loggedUser;
    private IOprVesselMaster iOprVesselMasterBs;
    private Label vesselLabel;
    int iRowIndex = 0;
    private List<OprVesselMaster> vesselList;

    public void onCreate$vesselMasterWin() {
        Executions.getCurrent().getDesktop().setAttribute("currentWindow", this);
        log = Logger.getLogger(PortMasterComposer.class);
        loggedUser = (ScrUserMaster) sessionScope.get("user");
        iOprVesselMasterBs = new OprVesselMasterBS();
        vesselName.addForward(Events.ON_CHANGING, vesselMasterWin, "onChangingComp");
        CommonOperation.beginTransaction();
        List<OprCountryMaster> countryList = iOprVesselMasterBs.getAllCountries();
        loadCountry(ownerCountry, countryList, null);
        vesselList = iOprVesselMasterBs.fetchVesselList();
        fillVesselsListGrid(vesselList);
        CommonOperation.commitTransaction();
    }

    public void onClickVesselCode(ForwardEvent event) {
        try {
            removeAllErrorMessages();
            Toolbarbutton targetLink = (Toolbarbutton) event.getOrigin().getTarget();
            Row targetRow = (Row) targetLink.getParent();
            OprVesselMaster vesselBean = (OprVesselMaster) targetRow.getValue();
            iRowIndex = vesselMasterRows.getChildren().indexOf(targetRow);
            System.out.println("iRowIndex   >>> "+iRowIndex);
            if (vesselBean != null) {
                populateFields(vesselBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateFields(OprVesselMaster vesselBean) {
        vesselName.setText(vesselBean.getVesselName());
        vesselName.setAttribute("bean", vesselBean);
        owner.setText(vesselBean.getOwner() == null ? "" : "" + vesselBean.getOwner());        
        setSelectedCountry(ownerCountry, vesselBean.getOwnerCountry());
    }
    public void onChangingComp(ForwardEvent event) throws InterruptedException, Exception {
        System.out.println("...........onChangingComp............");
        if(vesselName.getAttribute("bean") == null)
        fillVesselsListGrid(vesselList,vesselLabel.getValue());
    }

    public void onChange$vesselName() {
        ComponentValidation.removeErrorMessage(vesselName);
        if(vesselName.getAttribute("bean") == null)
        fillVesselsListGrid(vesselList);
    }

    public void onChange$owner() {
        ComponentValidation.removeErrorMessage(owner);
    }

    public void onChange$ownerCountry() {
        ComponentValidation.removeErrorMessage(ownerCountry);
    }

    public void clearScreeen() {
        resetFields();
    }

    public void saveRecord() throws Exception {
        // validate the fields
        if (validateFields()) {
            boolean isNew = false;
            OprVesselMaster vesselMaster = null;
            vesselMaster = (OprVesselMaster) vesselName.getAttribute("bean");
            if (vesselMaster == null) {
                isNew = true;
                vesselMaster = new OprVesselMaster();
                vesselMaster.setCreatedBy(loggedUser);
                vesselMaster.setCreatedDate(new Date());
            } else {
                vesselMaster.setUpdatedBy(loggedUser);
                vesselMaster.setUpdatedDate(new Date());
            }
            vesselMaster.setVesselCode(generateVesselCode());
            vesselMaster.setVesselName(vesselName.getText());
            vesselMaster.setOwner(owner.getText());
            vesselMaster.setOwnerCountry((OprCountryMaster) (ownerCountry.getSelectedIndex() > -1 ? ownerCountry.getSelectedItem().getValue() : null));
            vesselMaster.setEntryStatus("1");
            CommonOperation.beginTransaction(); // Here Session transaction begins
            // check wheather kos is already exist or not
            boolean isExist = isNew ? iOprVesselMasterBs.isVesselExist(vesselMaster.getVesselName()) : false;
            if (!isExist) {
                // save the kind of solution
                vesselMaster = iOprVesselMasterBs.saveVessel(vesselMaster);
                if (isNew) {
                    cretaeRow(vesselMaster);
                } else {
                    modifyRow(iRowIndex, vesselMaster);
                }
                
                log.info("--- Saving Port ---" + vesselName.getText());
                CommonOperation.commitTransaction("label.opr.vessel.save");
                resetFields();

            } else {
                log.info("--- Vessel Already Exist ---" + vesselName.getText());
                ComponentValidation.showErrorMessage(new WrongValueException(vesselName, Labels.getLabel("label.opr.vessel.exist")));  // throw exception on Port Code
                CommonOperation.commitTransaction("label.opr.vessel.exist");
            }
        }

    }

    public void modifyRow(int iRIndex, OprVesselMaster vesselBean) {
        Row row = (Row) vesselMasterRows.getChildren().get(iRIndex);
        row.setValue(vesselBean);

        Toolbarbutton txtCode = (Toolbarbutton) row.getChildren().get(1); // Port Code
        txtCode.setLabel(vesselBean.getVesselCode());

        Label txtName = (Label) row.getChildren().get(2); // Port Name
        txtName.setValue(vesselBean.getVesselName());

        Label txtOwner = (Label) row.getChildren().get(3); // Port Name
        txtOwner.setValue(vesselBean.getOwner());

        Label txtOwnerCountry = (Label) row.getChildren().get(4); // Port Name
        txtOwnerCountry.setValue(vesselBean.getOwnerCountry() == null ? "" : vesselBean.getOwnerCountry().getCountryName());
    }

    public void addRecord() throws Exception {
        resetFields();
    }

    public void closeScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void retrieveRecord() {
        fillVesselsListGrid(vesselList);
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
                for (int i = 0; i < vesselMasterRows.getChildren().size(); i++) {
                    Row row = (Row) vesselMasterRows.getChildren().get(i);
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
                        OprVesselMaster vesselBean = (OprVesselMaster) row.getValue();
                        vesselBean.setEntryStatus("0");
                        iOprVesselMasterBs.saveVessel(vesselBean);
                        vesselMasterRows.removeChild(row);
                        vesselList.remove(vesselBean);
                    }
//                    vesselMasterRows.getChildren().clear();
//                   fillVesselsListGrid(iOprVesselMasterBs.fetchVesselList());
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

    public void fillVesselsListGrid(List<OprVesselMaster> vesselList) {
        vesselMasterRows.getChildren().clear();
        if (vesselName.getText().isEmpty()) {
            for (OprVesselMaster vesselBean : vesselList) {
                cretaeRow(vesselBean);
            }
        } else {
            for (OprVesselMaster vesselBean : vesselList) {
                if (vesselBean.getVesselName().toUpperCase().startsWith(vesselName.getText().toUpperCase())) {
                    cretaeRow(vesselBean);
                }
            }
        }
    }
    public void fillVesselsListGrid(List<OprVesselMaster> vesselList,String value) {
        System.out.println("_______________1__"+value);
        vesselMasterRows.getChildren().clear();
        if (value.isEmpty()) {
            for (OprVesselMaster vesselBean : vesselList) {
                cretaeRow(vesselBean);
            }
        } else {
            for (OprVesselMaster vesselBean : vesselList) {
                if (vesselBean.getVesselName().toUpperCase().startsWith(value.toUpperCase())) {
                    cretaeRow(vesselBean);
                }
            }
        }
    }

    public void cretaeRow(OprVesselMaster vesselBean) {

        Row row = new Row();
        row.setValue(vesselBean);
        row.setParent(vesselMasterRows);

        Div div = new Div();
        //div.setWidth("200px");
        div.setParent(row);

        Label lblSrNo = new Label();
        lblSrNo.setValue("" + (vesselMasterRows.getChildren().indexOf(row) + 1));
        lblSrNo.setParent(div);

        Checkbox chkBox = new Checkbox();
        chkBox.setParent(div);

        Toolbarbutton txtVesselCode = new Toolbarbutton(vesselBean.getVesselCode());
        txtVesselCode.addForward(Events.ON_CLICK, vesselMasterWin, "onClickVesselCode");
        txtVesselCode.setParent(row);

        Label txtVesselName = new Label();
        txtVesselName.setValue(vesselBean.getVesselName());
        txtVesselName.setParent(row);

        Label txtOwner = new Label();
        txtOwner.setValue(vesselBean.getOwner());
        txtOwner.setParent(row);

        Label txtOwnerCountry = new Label();
        txtOwnerCountry.setValue(vesselBean.getOwnerCountry() != null ? vesselBean.getOwnerCountry().getCountryName() : "");
        txtOwnerCountry.setParent(row);
    }

    public void resetFields() {
        removeAllErrorMessages();
        vesselName.setText("");
        vesselName.setAttribute("bean", null);
        vesselName.setReadonly(false);
        owner.setText("");
        ownerCountry.setSelectedIndex(-1);
        iRowIndex = 0;
        CommonOperation.beginTransaction();
        vesselList = iOprVesselMasterBs.fetchVesselList();
        fillVesselsListGrid(vesselList);
        CommonOperation.commitTransaction();
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
        }

    }

    public void setSelectedCountry(Combobox cmbBox, OprCountryMaster selectedCountry) {
        System.out.println("selectedCountry  >> "+selectedCountry);
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

    public boolean validateFields() {
        boolean flag = true;
        WrongValueException e = ComponentValidation.validateManditoryTextbox(vesselName, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag = false;
        }
//        e = ComponentValidation.validateManditoryTextbox(owner, null);
//        if (e != null) {
//            ComponentValidation.showErrorMessage(e);
//            flag = false;
//        }
//        e = ComponentValidation.validateManditoryCombo(ownerCountry, null);
//        if (e != null) {
//            ComponentValidation.showErrorMessage(e);
//            flag = false;
//        }
        return flag;
    }

    private String generateVesselCode() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String code = vesselName.getText().toUpperCase().subSequence(0, 3).toString();
        return code + sdf.format(new Date()).replaceAll("[^0-9]", "");

    }

    private void removeAllErrorMessages() {
        ComponentValidation.removeErrorMessage(vesselName);
        ComponentValidation.removeErrorMessage(owner);
        ComponentValidation.removeErrorMessage(ownerCountry);
    }
}
