/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.security.windows;

import com.manikssys.in.common.CommonOperation;
import com.manikssys.in.common.Constants;
import com.manikssys.in.common.ICommonWindowInterface;
import com.manikssys.in.security.beans.*;
import com.manikssys.in.security.business.ISMSSchedularBs;
import com.manikssys.in.security.business.SMSSchedularBs;
import java.util.*;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.*;

/**
 *
 * @author pc
 */
public class SMSSchedularComposer extends GenericForwardComposer implements ICommonWindowInterface {

    private Checkbox monChkBox, tueChkBox, wedChkBox, thusChkBox, friChkBox, satChkBox, sunChkBox;
    private Combobox cmbPosition;
    private Label characterCount;
    private Textbox messageText;
    private Window SMSSchedularWin;
    private static Logger log;
    private ScrUserMaster loggedUser;
    private Timebox s1UserTime, s2UserTime, s1BranchTime, s2BranchTime, s1HoTime, s2HoTime;   

    public void onCreate$SMSSchedularWin() {
        System.out.println(".............onCreate$SMSSchedularWin.............");
        Executions.getCurrent().getDesktop().setAttribute("currentWindow", this);
        log = Logger.getLogger(MacMasterComposer.class);
        loggedUser = (ScrUserMaster) sessionScope.get("user");

        fillPositionCmb();
        setHolidaysToScreen();
    }

    public void clearScreeen() {
        monChkBox.setChecked(false);
        tueChkBox.setChecked(false);
        wedChkBox.setChecked(false);
        thusChkBox.setChecked(false);
        friChkBox.setChecked(false);
        satChkBox.setChecked(false);
        sunChkBox.setChecked(false);
        messageText.setText("");
    }

    public void saveRecord() throws Exception {
        System.out.println("SAVE-----------------------");
        List<ScrSmsHdr> holidayList = new ArrayList<ScrSmsHdr>();
        boolean saveStatus = false;
        for (int i = 1; i < 8; i++) {
            ScrSmsHdr bean = new ScrSmsHdr();
            bean.setId(i);
            setBeanValues(bean);
            holidayList.add(bean);
        }
        ISMSSchedularBs smsBs = new SMSSchedularBs();

        for (ScrSmsHdr bean : holidayList) {
            CommonOperation.beginTransaction();
            bean = smsBs.saveHolidays(bean);
            saveStatus = CommonOperation.commitTransaction();
        }

        if(cmbPosition.getSelectedIndex() > -1 && saveStatus){
            ScrSmsScheduleMaster scheduleMaster = new ScrSmsScheduleMaster();
            scheduleMaster.setId((Integer) cmbPosition.getSelectedItem().getValue());            
            scheduleMaster.setS1UserTime(new Date());
            scheduleMaster.setS2UserTime(new Date());
            scheduleMaster.setS1BranchTime(new Date());
            scheduleMaster.setS2BranchTime(new Date());
            scheduleMaster.setS1HoTime(new Date());
            scheduleMaster.setS2HoTime(new Date());
            scheduleMaster.setTextMsg(messageText.getText());
            System.out.println("scheduleMaster "+scheduleMaster);

            CommonOperation.beginTransaction();
            scheduleMaster = smsBs.saveScheduleData(scheduleMaster);
            saveStatus = CommonOperation.commitTransaction("label.common.save");
        }

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

    private void setBeanValues(ScrSmsHdr bean) {
        int val = bean.getId();
        System.out.println("val >>>> " + val);
        Date date = new Date();
        switch (val) {
            case 1:
                bean.setSStatus(monChkBox.isChecked() ? Constants.ACTIVE_DAY_SMS : Constants.PASSIVE_DAY_SMS);
                break;

            case 2:
                bean.setSStatus(tueChkBox.isChecked() ? Constants.ACTIVE_DAY_SMS : Constants.PASSIVE_DAY_SMS);
                break;

            case 3:
                bean.setSStatus(wedChkBox.isChecked() ? Constants.ACTIVE_DAY_SMS : Constants.PASSIVE_DAY_SMS);
                break;

            case 4:
                bean.setSStatus(thusChkBox.isChecked() ? Constants.ACTIVE_DAY_SMS : Constants.PASSIVE_DAY_SMS);
                break;

            case 5:
                bean.setSStatus(friChkBox.isChecked() ? Constants.ACTIVE_DAY_SMS : Constants.PASSIVE_DAY_SMS);
                break;

            case 6:
                bean.setSStatus(satChkBox.isChecked() ? Constants.ACTIVE_DAY_SMS : Constants.PASSIVE_DAY_SMS);
                break;

            case 7:
                bean.setSStatus(sunChkBox.isChecked() ? Constants.ACTIVE_DAY_SMS : Constants.PASSIVE_DAY_SMS);
                break;
        }

        System.out.println(val + "  Time    >>>>> " + date);

    }

    private void setHolidaysToScreen() {
        System.out.println("*******************setHolidaysToScreen********************");
        ISMSSchedularBs smsBs = new SMSSchedularBs();

        CommonOperation.beginTransaction();
        List<ScrSmsHdr> list = smsBs.getHolidayList();
        CommonOperation.commitTransaction();
        int id = 1;
        for (ScrSmsHdr hdrBean : list) {
            switch (id) {
                case 1:
                    monChkBox.setChecked(hdrBean.getSStatus() == Constants.ACTIVE_DAY_SMS ? true : false);
                    break;

                case 2:
                    tueChkBox.setChecked(hdrBean.getSStatus() == Constants.ACTIVE_DAY_SMS ? true : false);
                    break;

                case 3:
                    wedChkBox.setChecked(hdrBean.getSStatus() == Constants.ACTIVE_DAY_SMS ? true : false);
                    break;

                case 4:
                    thusChkBox.setChecked(hdrBean.getSStatus() == Constants.ACTIVE_DAY_SMS ? true : false);
                    break;

                case 5:
                    friChkBox.setChecked(hdrBean.getSStatus() == Constants.ACTIVE_DAY_SMS ? true : false);
                    break;

                case 6:
                    satChkBox.setChecked(hdrBean.getSStatus() == Constants.ACTIVE_DAY_SMS ? true : false);
                    break;

                case 7:
                    sunChkBox.setChecked(hdrBean.getSStatus() == Constants.ACTIVE_DAY_SMS ? true : false);
                    break;
            }
            id++;
        }

    }

    private void fillPositionCmb() {
        Comboitem cmbItem = new Comboitem();
        cmbItem.setLabel(Labels.getLabel("app.report.title.vpeastdry"));
        cmbItem.setValue(3);
        cmbItem.setParent(cmbPosition);

        Comboitem cmbItem1 = new Comboitem();
        cmbItem1.setLabel(Labels.getLabel("app.report.title.vpeastliquid"));
        cmbItem1.setValue(4);
        cmbItem1.setParent(cmbPosition);

        Comboitem cmbItem2 = new Comboitem();
        cmbItem2.setLabel(Labels.getLabel("app.report.title.vpwestdry"));
        cmbItem2.setValue(5);
        cmbItem2.setParent(cmbPosition);

        Comboitem cmbItem3 = new Comboitem();
        cmbItem3.setLabel(Labels.getLabel("app.report.title.vpwestliquid"));
        cmbItem3.setValue(6);
        cmbItem3.setParent(cmbPosition);
    }

    private void retreivePositionSchedule() {
        clearSchedule();
        ISMSSchedularBs smsBs = new SMSSchedularBs();
        int position = 0;
        position = Integer.parseInt(cmbPosition.getSelectedItem().getValue().toString());
        if(position > 2 && position < 7){
            CommonOperation.beginTransaction();
            List<ScrSmsScheduleMaster> list = smsBs.getScheduleForPosition(position);
            CommonOperation.commitTransaction();
            populateSchedulTime(list.get(0));
        }
    }

    private void clearSchedule() {
        s1UserTime.setValue(null);
        s2UserTime.setValue(null);
        s1BranchTime.setValue(null);
        s2BranchTime.setValue(null);
        s1HoTime.setValue(null);
        s2HoTime.setValue(null);
        messageText.setText("");
    }

    private void populateSchedulTime(ScrSmsScheduleMaster schdulMaster) {
        s1UserTime.setValue(schdulMaster.getS1UserTime());
        s2UserTime.setValue(schdulMaster.getS2UserTime());
        s1BranchTime.setValue(schdulMaster.getS1BranchTime());
        s2BranchTime.setValue(schdulMaster.getS2BranchTime());
        s1HoTime.setValue(schdulMaster.getS1HoTime());
        s2HoTime.setValue(schdulMaster.getS2HoTime());
        messageText.setText("");
    }
   
}
