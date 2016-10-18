package com.manikssys.in.security.business;

import com.manikssys.in.security.beans.ScrSmsHdr;
import com.manikssys.in.security.beans.ScrSmsScheduleMaster;
import java.util.List;

/**
 * User: sandeep
 * Date: Jun 25, 2010
 */
public interface ISMSSchedularBs {
    public ScrSmsHdr saveHolidays(ScrSmsHdr smsSchedulBean);
    public List<ScrSmsHdr> getSMSHdr();

    public List<ScrSmsHdr> getHolidayList();

    public List<ScrSmsScheduleMaster> getScheduleForPosition(int position);

    public ScrSmsScheduleMaster saveScheduleData(ScrSmsScheduleMaster scheduleMaster);
}
