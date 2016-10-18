package com.manikssys.in.common;

import java.awt.event.KeyListener;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Toolbarbutton;

import com.manikssys.in.security.beans.ScrMenuButtonProfileDetails;


/**
 * User: sandeep
 * Date: Jul 1, 2010
 */
public class ToolbarComposer extends GenericForwardComposer {

    ICommonWindowInterface iWin;
    Hbox commonHBox;
    Toolbarbutton First;
    Toolbarbutton prev;
    Toolbarbutton next;
    Toolbarbutton last;
    Toolbarbutton addrecord;
    Toolbarbutton delete_record;
    Toolbarbutton save;
    Toolbarbutton import_data;
    Toolbarbutton cleanup;

    KeyListener klRetrieve;
    KeyListener klInsert;
    KeyListener klDelete;
    KeyListener klSave;
    KeyListener klClear;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        // TODO Auto-generated method stub
        super.doAfterCompose(comp);
        comp.setAttribute("commonHBox$composer", this);
        Executions.getCurrent().getDesktop().setAttribute("commonHBox$composer", this);
    }



    public void onCreate$ToolbarComposer() {
        setEnableDisable(true);
    }



    public void onClick$First() {
        iWin=(ICommonWindowInterface) Executions.getCurrent().getDesktop().getAttribute("currentWindow");
        if(iWin !=null && !(First.isDisabled()) )
            iWin.getFirstRecord();
    }

    public void onClick$prev() {
        iWin=(ICommonWindowInterface) Executions.getCurrent().getDesktop().getAttribute("currentWindow");
        if(iWin !=null && !(prev.isDisabled()) )
            iWin.getPreviousRecord();
    }

    public void onClick$next() {
        iWin=(ICommonWindowInterface) Executions.getCurrent().getDesktop().getAttribute("currentWindow");
        if(iWin !=null && !(next.isDisabled()) )
            iWin.getNextRecord();
    }

    public void onClick$last() {
        iWin=(ICommonWindowInterface) Executions.getCurrent().getDesktop().getAttribute("currentWindow");
        if(iWin !=null && !(last.isDisabled()) )
            iWin.getLastRecord();
    }

    public void onClick$addrecord() throws Exception {
        setNavigationState(true);
        iWin=(ICommonWindowInterface) Executions.getCurrent().getDesktop().getAttribute("currentWindow");
        if(iWin !=null && !(addrecord.isDisabled()) )
            iWin.addRecord();
    }

    public void onClick$delete_record() throws Exception {
        setNavigationState(true);
        iWin=(ICommonWindowInterface) Executions.getCurrent().getDesktop().getAttribute("currentWindow");
        if(iWin !=null && !(delete_record.isDisabled()) )
            iWin.deleteRecord();
    }

    public void onClick$save() throws Exception {
        setNavigationState(true);
        iWin=(ICommonWindowInterface) Executions.getCurrent().getDesktop().getAttribute("currentWindow");
        if(iWin !=null && !(save.isDisabled()) )
            iWin.saveRecord();
    }

    public void onClick$import_data() {
        setNavigationState(true);
        iWin=(ICommonWindowInterface) Executions.getCurrent().getDesktop().getAttribute("currentWindow");
        if(iWin !=null && !(import_data.isDisabled()) )
            iWin.retrieveRecord();
    }

    public void onClick$cleanup() {
        setNavigationState(true);
        iWin=(ICommonWindowInterface) Executions.getCurrent().getDesktop().getAttribute("currentWindow");
        if(iWin !=null && !(cleanup.isDisabled()) )
            iWin.clearScreeen();
    }

    // Method to set the ToolBar controls
    public void setUserAccessState(List<ScrMenuButtonProfileDetails> arButtonList){
        //System.out.println("GOT IN USer Access Button LIst > "+arButtonList.size());
        boolean addStatus=true;
        boolean deleteStatus=true;
        boolean saveStatus=true;

        for(ScrMenuButtonProfileDetails mbpMaster:arButtonList){
            String buttonName = mbpMaster.getScrButtonMaster().getButtonName();
            //System.out.println("Checking Button NAme > "+buttonName);
            if (buttonName.equalsIgnoreCase("ADD")) {
                addStatus=false;
            } else
            if (buttonName.equalsIgnoreCase("SAVE")) {
                saveStatus=false;
            } else
            if (buttonName.equalsIgnoreCase("DELETE")) {
                deleteStatus=false;
            }
        }

        // Set other buttons Enabled
        addrecord.setDisabled(addStatus);
        save.setDisabled(saveStatus);
        delete_record.setDisabled(deleteStatus);
        /*First.setDisabled(true);
        prev.setDisabled(true);
        next.setDisabled(true);
        last.setDisabled(true);*/
        import_data.setDisabled(false);
        cleanup.setDisabled(false);

        // set the toolbar button status
        /*setToolbarButtonState(First);
        setToolbarButtonState(prev);
        setToolbarButtonState(next);
        setToolbarButtonState(last);*/
        setToolbarButtonState(addrecord);
        setToolbarButtonState(delete_record);
        setToolbarButtonState(save);
        setToolbarButtonState(import_data);
        setToolbarButtonState(cleanup);

        setNavigationState(true);
    }

    public void setToolbarButtonState(Toolbarbutton tbButtion){
        if(tbButtion.isDisabled())
            tbButtion.setImage("/images/btn_" + tbButtion.getId() + "_inact.png");
        else
            tbButtion.setImage("/images/btn_" + tbButtion.getId() + ".png");
    }

    public void setEnableDisable(boolean flag){
        First.setDisabled(flag);
        prev.setDisabled(flag);
        next.setDisabled(flag);
        last.setDisabled(flag);
        import_data.setDisabled(flag);
        cleanup.setDisabled(flag);
        addrecord.setDisabled(flag);
        save.setDisabled(flag);
        delete_record.setDisabled(flag);
    }


    public void setNavigationState(boolean flag){
        setNavigationState(flag,-1,0);
    }

    public void setNavigationState(int iNoOfRecs,int iCurRec){
        setNavigationState(false,iNoOfRecs,iCurRec);
    }

    private void setNavigationState(boolean flag, int iNoOfRecs,int iCurRec){
        // set Enable / Disable accoring to condition
        if(!flag){
            if(iCurRec == 1 && iCurRec < iNoOfRecs){
                First.setDisabled(true);
                prev.setDisabled(true);
                next.setDisabled(false);
                last.setDisabled(false);
            }else if(iCurRec == iNoOfRecs && iCurRec >1){
                First.setDisabled(false);
                prev.setDisabled(false);
                next.setDisabled(true);
                last.setDisabled(true);
            }else if(iCurRec > 0 && iCurRec < iNoOfRecs){
                First.setDisabled(false);
                prev.setDisabled(false);
                next.setDisabled(false);
                last.setDisabled(false);
            }
            else{
                First.setDisabled(true);
                prev.setDisabled(true);
                next.setDisabled(true);
                last.setDisabled(true);
            }
        }else{
            First.setDisabled(true);
            prev.setDisabled(true);
            next.setDisabled(true);
            last.setDisabled(true);
        }
        // Set the Status
        setToolbarButtonState(First);
        setToolbarButtonState(prev);
        setToolbarButtonState(next);
        setToolbarButtonState(last);
    }

    // Key Listener methods

    public void onCtrlKey$klRetrieve() {
        try {
            onClick$import_data();
        } catch (Exception e) {e.printStackTrace();}
    }

    public void onCtrlKey$klInsert() {
        try {
            onClick$addrecord();
        } catch (Exception e) {e.printStackTrace();}
    }

    public void onCtrlKey$klDelete() {
        try {
            onClick$delete_record();
        } catch (Exception e) {e.printStackTrace();}
    }

    public void onCtrlKey$klSave() {
        try {
            onClick$save();
        } catch (Exception e) {e.printStackTrace();}
    }

    public void onCtrlKey$klClear() {
        try {
            onClick$cleanup();
        } catch (Exception e) {e.printStackTrace();}
    }


}
