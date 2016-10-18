package com.manikssys.in.security.business;

import com.manikssys.in.common.dao.security.MACMasterDAO;
import com.manikssys.in.security.beans.ScrMacMaster;

import java.util.List;

/**
 * User: sandeep
 * Date: Jul 9, 2010
 */
public class MACMasterBs  implements IMACMaster{
     MACMasterDAO macDao =new MACMasterDAO();

    public List<ScrMacMaster> fetchMACList(){
        return macDao.getMACList();
    }

    public ScrMacMaster saveMAC(ScrMacMaster macBean){
        return macDao.saveMAC(macBean);
    }

    public boolean isMACExist(String macId){

        List macList=macDao.findMACById(macId);
        System.out.println("macList >> "+macList.size());
        if (macList != null && macList.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void deleteMAC(ScrMacMaster macBean){
        macDao.deleteMAC(macBean);
    }
}
