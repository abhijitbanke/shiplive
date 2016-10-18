/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.operational.business;

import com.manikssys.in.common.dao.operational.VesselMasterDAO;
import com.manikssys.in.operational.beans.OprCountryMaster;
import com.manikssys.in.operational.beans.OprVesselMaster;
import java.util.List;

/**
 *
 * @author pc
 */
public class OprVesselMasterBS implements IOprVesselMaster{
    VesselMasterDAO vesselDao=new VesselMasterDAO();

    public List<OprVesselMaster> fetchVesselList() {       
        return vesselDao.getVesselsList();
    }

    public OprVesselMaster saveVessel(OprVesselMaster vesselBean) {
       return vesselDao.saveVessel(vesselBean);
    }

    public boolean isVesselExist(String vesselName) {
//        if (vesselDao.findVesselByVesselCode(vesselCode).size() > 0) {
         if (vesselDao.findVesselByVesselName(vesselName).size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    public List<OprCountryMaster> getAllCountries(){
         return vesselDao.loadAllCountries();
    }
}
