/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.security.business;

import com.manikssys.in.common.dao.security.SMSHolidayDAO;
import com.manikssys.in.common.dao.security.SMSSchedularDAO;
import com.manikssys.in.security.beans.ScrSmsHdr;
import com.manikssys.in.security.beans.ScrSmsScheduleMaster;
import java.util.List;

/**
 *
 * @author pc
 */
public class SMSSchedularBs implements ISMSSchedularBs{
    SMSSchedularDAO smsSchedularDao=new SMSSchedularDAO();
    SMSHolidayDAO smsHolidayDao=new SMSHolidayDAO();

    public ScrSmsHdr saveHolidays(ScrSmsHdr smsSchedulBean) {
        return smsHolidayDao.saveHolidays(smsSchedulBean);
    }

    public List<ScrSmsHdr> getSMSHdr() {
        return smsHolidayDao.getTodaysSMSHdr();
    }

    public List<ScrSmsHdr> getHolidayList() {
        return smsHolidayDao.getHolidayList();
    }

    public List<ScrSmsScheduleMaster> getScheduleForPosition(int position) {
        return smsSchedularDao.getScheduleForPosition(position);
    }

    public ScrSmsScheduleMaster saveScheduleData(ScrSmsScheduleMaster scheduleMaster) {
        return smsSchedularDao.saveScheduleData(scheduleMaster);
    }

}
