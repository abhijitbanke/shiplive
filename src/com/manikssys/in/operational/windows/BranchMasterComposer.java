package com.manikssys.in.operational.windows;

import com.manikssys.in.common.*;
import com.manikssys.in.operational.beans.OprBranchMaster;
import com.manikssys.in.operational.business.BranchMasterBs;
import com.manikssys.in.operational.business.IBranchMaster;
import com.manikssys.in.security.beans.ScrUserMaster;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: sandeep
 * Date: Jul 12, 2010
 */
public class BranchMasterComposer extends GenericForwardComposer implements ICommonWindowInterface {

    private Textbox mBranchCode,cBranchCode;
    private Textbox mBranchName,cBranchName;
    private Combobox cHeadBranch;
    private Textbox mBranchAdd1,mBranchAdd2,mBranchAdd3,cBranchAdd1,cBranchAdd2,cBranchAdd3;
    private Textbox mBranchTelephone,cBranchTelephone;
    private Textbox mBranchTelex,cBranchTelex;
    private Textbox mBranchFax,cBranchFax;
    private Textbox mBranchEmail,cBranchEmail;
    private Window branchMasterWin;
    private static Logger log ;
    private ScrUserMaster loggedUser;
    private IBranchMaster iBranchBs;
    OprBranchMaster mainBranch,currentBranch;
    private int iCurrentIndex;
    private List<OprBranchMaster> arBranchList;
    ToolbarComposer toolBarComposer;

    public void onCreate$branchMasterWin() {

        try {

            Executions.getCurrent().getDesktop().setAttribute("currentWindow",this);
            toolBarComposer =(ToolbarComposer)Executions.getCurrent().getDesktop().getAttribute("commonHBox$composer");
            log = Logger.getLogger(BranchMasterComposer.class);
            arBranchList=new ArrayList();
            loggedUser = (ScrUserMaster) sessionScope.get("user");
            iBranchBs =new BranchMasterBs();
            CommonOperation.beginTransaction();
            // Get the Branch List
            List arBranchList=iBranchBs.getBranchDetails(loggedUser);
            if(arBranchList.size() > 0)
            {
                OprBranchMaster branchMaster=(OprBranchMaster)arBranchList.get(0);
                if(branchMaster.getBranchRoot().equals("0")){
                    mainBranch=branchMaster;
                    fillRootDetails();
                    if(arBranchList.size() > 1){
                        currentBranch=(OprBranchMaster)arBranchList.get(1);
                        fillBranchDetails();
                    }else 
                        addRecord();
                }
                else{
                    currentBranch=branchMaster;
                    fillBranchDetails();
                    if(arBranchList.size() > 1){
                        mainBranch=(OprBranchMaster)arBranchList.get(1);
                        fillRootDetails();
                    }
                }

                // Populating Controlling Dept
                Comboitem cmbItem=new Comboitem();
                cmbItem.setLabel(mainBranch.getBranchCode());
                cmbItem.setParent(cHeadBranch);
                cHeadBranch.setSelectedItem(cmbItem);
            }


            CommonOperation.commitTransaction();
        } catch (Exception e) {e.printStackTrace();}
    }

    public void onChange$mBranchCode(){
        ComponentValidation.removeErrorMessage(mBranchCode);
    }

    public void onChange$cBranchCode(){
       ComponentValidation.removeErrorMessage(cBranchCode);
    }

    public void onChange$mBranchName(){
        ComponentValidation.removeErrorMessage(mBranchName);
    }

    public void onChange$cBranchName(){
       ComponentValidation.removeErrorMessage(cBranchName);
    }

    public void fillRootDetails(){

        mBranchCode.setValue(mainBranch.getBranchCode());
        mBranchCode.setAttribute("branchcode",mainBranch.getBranchCode());
        mBranchName.setValue(mainBranch.getBranchName());
        mBranchAdd1.setValue(mainBranch.getUserAddr1());
        mBranchAdd2.setValue(mainBranch.getUserAddr2());
        mBranchAdd3.setValue(mainBranch.getUserAddr3());
        mBranchTelephone.setValue(mainBranch.getBranchTelephone());
        mBranchTelex.setValue(mainBranch.getBranchTelex());
        mBranchFax.setValue(mainBranch.getBranchFax());
        mBranchEmail.setValue(mainBranch.getBranchEmail());

        if(!loggedUser.getOprBranchMaster().getBranchRoot().equals("0"))
            setRootReadOnly(true);
    }

    public void fillBranchDetails(){

        cBranchCode.setValue(currentBranch.getBranchCode());
        cBranchCode.setAttribute("branchcode",currentBranch.getBranchCode());
        cBranchName.setValue(currentBranch.getBranchName());
        cBranchAdd1.setValue(currentBranch.getUserAddr1());
        cBranchAdd2.setValue(currentBranch.getUserAddr2());
        cBranchAdd3.setValue(currentBranch.getUserAddr3());
        cBranchTelephone.setValue(currentBranch.getBranchTelephone());
        cBranchTelex.setValue(currentBranch.getBranchTelex());
        cBranchFax.setValue(currentBranch.getBranchFax());
        cBranchEmail.setValue(currentBranch.getBranchEmail());
    }

    public void setRootReadOnly(boolean flag){

        mBranchCode.setReadonly(flag);
        mBranchName.setReadonly(flag);
        mBranchAdd1.setReadonly(flag);
        mBranchAdd2.setReadonly(flag);
        mBranchAdd3.setReadonly(flag);
        mBranchTelephone.setReadonly(flag);
        mBranchTelex.setReadonly(flag);
        mBranchFax.setReadonly(flag);
        mBranchEmail.setReadonly(flag);

    }


    public void resetRootFields(){

        mBranchCode.setText("");
        mBranchName.setText("");
        mBranchAdd1.setText("");
        mBranchAdd2.setText("");
        mBranchAdd3.setText("");
        mBranchTelephone.setText("");
        mBranchTelex.setText("");
        mBranchFax.setText("");
        mBranchEmail.setText("");

        mainBranch=null;
    }

    public void resetBranchFields(){

        cBranchCode.setText("");
        cBranchName.setText("");
        cBranchAdd1.setText("");
        cBranchAdd2.setText("");
        cBranchAdd3.setText("");
        cBranchTelephone.setText("");
        cBranchTelex.setText("");
        cBranchFax.setText("");
        cBranchEmail.setText("");

        currentBranch=null;
    }


    public boolean validateRootFields(){
        boolean flag=true;
        WrongValueException e = ComponentValidation.validateManditoryTextbox(mBranchCode,null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag= false;
        }
        e = ComponentValidation.validateManditoryTextbox(mBranchName,null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag= false;
        }

        e = ComponentValidation.validateManditoryCombo(cHeadBranch,null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag= false;
        }
        return flag;
    }

    public boolean validateBranchFields(){
        boolean flag=true;
        WrongValueException e = ComponentValidation.validateManditoryTextbox(cBranchCode,null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag= false;
        }
        e = ComponentValidation.validateManditoryTextbox(cBranchName,null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            flag= false;
        }
        return flag;
    }

    public void getRootData(){

        mainBranch.setBranchCode(mBranchCode.getValue());
        mainBranch.setBranchName(mBranchName.getValue());
        mainBranch.setUserAddr1(mBranchAdd1.getValue());
        mainBranch.setUserAddr2(mBranchAdd2.getValue());
        mainBranch.setUserAddr3(mBranchAdd3.getValue());
        mainBranch.setBranchTelephone(mBranchTelephone.getValue());
        mainBranch.setBranchTelex(mBranchTelex.getValue());
        mainBranch.setBranchFax(mBranchFax.getValue());
        mainBranch.setBranchEmail(mBranchEmail.getValue());
        mainBranch.setStatus(Constants.NON_DELETED);
        mainBranch.setUpdatedBy(loggedUser);
        mainBranch.setUpdatedDate(new Date());
    }


    public void getBranchData(){

        currentBranch.setBranchCode(cBranchCode.getValue());
        currentBranch.setBranchName(cBranchName.getValue());
        currentBranch.setBranchRoot(mainBranch.getBranchId()); // Set as  the Main Branch Root
        currentBranch.setUserAddr1(cBranchAdd1.getValue());
        currentBranch.setUserAddr2(cBranchAdd2.getValue());
        currentBranch.setUserAddr3(cBranchAdd3.getValue());
        currentBranch.setBranchTelephone(cBranchTelephone.getValue());
        currentBranch.setBranchTelex(cBranchTelex.getValue());
        currentBranch.setBranchFax(cBranchFax.getValue());
        currentBranch.setBranchEmail(cBranchEmail.getValue());
        currentBranch.setStatus(Constants.NON_DELETED);
        currentBranch.setUpdatedBy(loggedUser);
        currentBranch.setUpdatedDate(new Date());
    }


    public void clearScreeen() {
        resetBranchFields();
    }

    public void saveRecord() throws Exception {
        boolean bothFlag=false;
        if(loggedUser.getOprBranchMaster().getBranchRoot().equals("0")){
            if(validateRootFields())
            {
                getRootData();
                bothFlag=true;
            }
        }

        if (validateBranchFields()) {

            getBranchData();
            CommonOperation.beginTransaction(); // Here Session transaction begins

            // check wheather kos is already exist or not
            boolean isMainExist=false;
            boolean isBranchExist=false;

            // Check whether it is new or not to check duplicate branch code
            String oldBranchCode=mBranchCode.getAttribute("branchcode")==null?"":(String)mBranchCode.getAttribute("branchcode");
            boolean isBranchCodeChanged=mainBranch.getBranchCode().equals(oldBranchCode);
            if(bothFlag && !(isBranchCodeChanged))
                isMainExist=iBranchBs.isBranchExist(mainBranch.getBranchCode());
            oldBranchCode=cBranchCode.getAttribute("branchcode")==null?"":(String)cBranchCode.getAttribute("branchcode");
            isBranchCodeChanged=currentBranch.getBranchCode().equals(oldBranchCode);
            if(!(isBranchCodeChanged))
                isBranchExist=iBranchBs.isBranchExist(currentBranch.getBranchCode());

            if (!(isMainExist) && !(isBranchExist)) {
                // save the kind of solution
                if(bothFlag)
                    mainBranch= iBranchBs.saveBranch(mainBranch);
                currentBranch= iBranchBs.saveBranch(currentBranch);


                log.info("--- Saving Branch ---");
                CommonOperation.commitTransaction("label.opr.branch.save");

            } else {
                log.info("--- Branch Already Exist ---");
                if(isMainExist)
                    ComponentValidation.showErrorMessage(new WrongValueException(mBranchCode, Labels.getLabel("label.branch.exist")));  // throw exception on Main Branch Code
                else
                    ComponentValidation.showErrorMessage(new WrongValueException(cBranchCode, Labels.getLabel("label.branch.exist")));  // throw exception on Branch Code
                CommonOperation.commitTransaction("");
            }
        }

    }

    public void addRecord() throws Exception {
        resetBranchFields();
        currentBranch=new OprBranchMaster();
        currentBranch.setCreatedBy(loggedUser);
        currentBranch.setCreatedDate(new Date());
    }

    public void closeScreen() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void retrieveRecord() {
        OprBranchMaster queryBranchBean=new OprBranchMaster();
        // get the details
        queryBranchBean.setBranchCode(cBranchCode.getText());
        queryBranchBean.setBranchName(cBranchName.getText());
        queryBranchBean.setStatus(Constants.NON_DELETED);

        CommonOperation.beginTransaction();
            arBranchList = iBranchBs.findBranch(queryBranchBean);
            System.out.println("Founeded Size > " + arBranchList.size());
            if (arBranchList.size() > 0) {
                toolBarComposer.setNavigationState(arBranchList.size(),1);
                resetBranchFields();
                iCurrentIndex = 0;
                currentBranch=arBranchList.get(iCurrentIndex);
                fillBranchDetails();
                CommonOperation.commitTransaction();
            } else {
                toolBarComposer.setNavigationState(true);
                clearScreeen();
                CommonOperation.commitTransaction("app.comman.err.search.recodNotFound");
            }
    }

    public void deleteRecord() throws Exception {
        int delResult = Messagebox.show(Labels.getLabel("label.delete.confirmation"), Labels.getLabel("label.common.question"), Messagebox.YES | Messagebox.NO, Messagebox.QUESTION);

        if(delResult == 16) // if Yes
        {
            if(currentBranch != null)  // if any of them is Checked
            {
                CommonOperation.beginTransaction(); // Here Session transaction begins

                currentBranch.setStatus(Constants.DELETED);
                currentBranch.setUpdatedBy(loggedUser);
                currentBranch.setUpdatedDate(new Date());
                iBranchBs.saveBranch(currentBranch);
                resetBranchFields();
            }
            CommonOperation.commitTransaction("label.opr.delete");
        }
        else // If No one is Checked
            Messagebox.show(Labels.getLabel("label.deleteobj.warning"), "Error", Messagebox.OK, Messagebox.ERROR);
    }

    public void setControls() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void getFirstRecord() {
        toolBarComposer.setNavigationState(arBranchList.size(),1);
        iCurrentIndex = 0;
        if (iCurrentIndex < arBranchList.size()) {
            toUI();
        }
    }

    public void getLastRecord() {
        toolBarComposer.setNavigationState(arBranchList.size(),arBranchList.size());
         iCurrentIndex = arBranchList.size()>0?(arBranchList.size() - 1):arBranchList.size();
        if (iCurrentIndex < arBranchList.size()) {
            toUI();
        }
    }

    public void getNextRecord() {
        iCurrentIndex++;
        toolBarComposer.setNavigationState(arBranchList.size(),iCurrentIndex+1);
        if (iCurrentIndex < arBranchList.size()) {
            toUI();
        }
    }

    public void getPreviousRecord() {
        iCurrentIndex--;
        toolBarComposer.setNavigationState(arBranchList.size(),iCurrentIndex+1);
        if (iCurrentIndex < arBranchList.size() && iCurrentIndex >= 0) {
            toUI();
        }
    }
    
     private void toUI(){
         currentBranch=arBranchList.get(iCurrentIndex);
         fillBranchDetails();
     }
}
