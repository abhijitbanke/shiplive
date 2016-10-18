/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.security;

import com.manikssys.in.common.*;
import com.manikssys.in.reports.business.IUserDisplay;
import com.manikssys.in.reports.business.UserDisplayBs;
import com.manikssys.in.security.beans.ScrSmsHdr;
import com.manikssys.in.security.beans.ScrUserMaster;
import com.manikssys.in.security.business.ISMSSchedularBs;
import com.manikssys.in.security.business.IUserManager;
import com.manikssys.in.security.business.SMSSchedularBs;
import com.manikssys.in.security.business.UserManagerBs;
import java.util.*;

class Task extends TimerTask {

    // run is a abstract method that defines task performed at scheduled time.
    public void run() {
        try {
            Calendar cal = new GregorianCalendar();
            List<ScrSmsHdr> hdrList = getSMSHdr();
            ScrSmsHdr bean = hdrList.get(0);
            List<ScrUserMaster> passiveUserList = getPassiveUsers();
            if (!passiveUserList.isEmpty()) {
                StringBuilder mobiles = new StringBuilder("");
                int count = 0;
                for (ScrUserMaster user : passiveUserList) {
                    if (user.getUserMobile() != null && !user.getUserMobile().isEmpty()) {
                        if (count != 0) {
                            mobiles.append(",");
                        }
                        mobiles.append("91").append(user.getUserMobile());
                        count++;
                    }
                }
                System.out.println("Mobiles    =====  >>>> " + mobiles);
//                if (bean.getSTime().getHours() == (cal.getTime().getHours())  && bean.getSTime().getMinutes() == (cal.getTime().getMinutes() - 1)       &&  bean.getSStatus() == Constants.ACTIVE_DAY_SMS) {
//                    System.out.println("Sending SMS ....................................................");
//                    CallSmscApi apiObj = new CallSmscApi();
//                    apiObj.sendSMS(mobiles.toString(), bean.getSMsg());
//                }
            }
        } catch (Exception e) {
            System.out.println("Thread interrupted!");
            e.printStackTrace();
        }
    }

    private List<ScrSmsHdr> getSMSHdr() {
        ISMSSchedularBs smsSchedularBs = new SMSSchedularBs();
        CommonOperation.beginTransaction();
        List<ScrSmsHdr> hdrList = smsSchedularBs.getSMSHdr();
        CommonOperation.commitTransaction();
        return hdrList;
    }

    public List<ScrUserMaster> getPassiveUsers() {
        IUserDisplay userBs;
        IUserManager userManagerBs;
        List<ScrUserMaster> activeUserList;
        List<ScrUserMaster> userList;
        List<ScrUserMaster> passiveUserList = new ArrayList<ScrUserMaster>();
        userBs = new UserDisplayBs();
        userManagerBs = new UserManagerBs();
        CommonOperation.beginTransaction();
        userList = userManagerBs.getUserList();
        activeUserList = userBs.getActiveUserList();
        System.out.println("activeUserList size    ====>>> " + activeUserList.size());
        for (ScrUserMaster scrUserMaster : userList) {
            if (activeUserList.contains(scrUserMaster)) {
            } else {
                passiveUserList.add(scrUserMaster);
            }
        }
        CommonOperation.commitTransaction();
        System.out.println("passiveList size    ====>>> " + passiveUserList.size());
        return passiveUserList;
    }
}
