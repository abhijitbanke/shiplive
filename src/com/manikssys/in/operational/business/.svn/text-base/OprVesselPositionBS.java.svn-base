/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.operational.business;

import com.manikssys.in.common.dao.operational.PortMasterDAO;
import com.manikssys.in.common.dao.operational.VesselMasterDAO;
import com.manikssys.in.common.dao.operational.VesselPositionDAO;
import com.manikssys.in.operational.beans.OprCountryMaster;
import com.manikssys.in.operational.beans.OprPortMaster;
import com.manikssys.in.operational.beans.OprVesselMaster;
import com.manikssys.in.operational.beans.OprVesselPosition;
import java.util.List;

/**
 *
 * @author pc
 */
public class OprVesselPositionBS implements IVesselPosition{

    PortMasterDAO portDao=new PortMasterDAO();
    VesselMasterDAO vesselDao=new VesselMasterDAO();
    VesselPositionDAO vpDao = new VesselPositionDAO();

    public List<OprPortMaster> getPortsList() {
        return portDao.getPortsList();
    } 
    public List<OprVesselPosition> getVPListByPort(OprVesselPosition vesselPositionMaster) {
        return vpDao.listAllVessels(vesselPositionMaster);
    }

    public OprVesselPosition saveVP(OprVesselPosition vesselPositionMaster) {
        return vpDao.makePersistent(vesselPositionMaster);
    }

    public List findNonSailedVPByPort(OprVesselPosition qVesselPositionMaster) {
        return vpDao.findAll(qVesselPositionMaster);
    }

    public List<OprVesselMaster> getVesselsList() {
        return vesselDao.getVesselsList();
    }

    public List<OprVesselPosition> getVPList(OprVesselPosition vesselPositionMaster) {
        return vpDao.findByExample(vesselPositionMaster, new String[]{});
    }
   
    public List<OprCountryMaster> getAllCountries(){
         return vesselDao.loadAllCountries();
    }
    public List<OprVesselPosition> getVPListForLimitedDays(OprVesselPosition vesselPositionMaster) {
        return vpDao.findVPForCargoHdr(vesselPositionMaster);
    }

}
