/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.operational.business;

import com.manikssys.in.common.dao.operational.CargoHdrDAO;
import com.manikssys.in.common.dao.operational.PortMasterDAO;
import com.manikssys.in.common.dao.operational.VesselMasterDAO;
import com.manikssys.in.operational.beans.OprCargoHdr;
import com.manikssys.in.operational.beans.OprCountryMaster;
import com.manikssys.in.operational.beans.OprPortMaster;
import com.manikssys.in.operational.beans.OprVesselMaster;
import com.manikssys.in.operational.beans.OprVesselPosition;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author abhijit
 */
public class OprCargoHdrBS implements ICargoHdr{
      PortMasterDAO portDao=new PortMasterDAO();
      VesselMasterDAO vesselMasterDao = new VesselMasterDAO();
      CargoHdrDAO cargoHdrDAO = new CargoHdrDAO();
    public List<OprPortMaster> getPortsList() {
        return portDao.getPortsList();
    }

    public List<OprCountryMaster> getAllCountries() {
        return vesselMasterDao.loadAllCountries();
    }

    public OprCargoHdr saveCargoHdrData(OprCargoHdr cargoHdr) {

        return cargoHdrDAO.makePersistent(cargoHdr);
    }

    public List<OprCargoHdr> getCargoHdrList(OprCargoHdr cargoHdr ) {
        return cargoHdrDAO.findCargoHdr(cargoHdr);
    }

    public List<OprVesselMaster> getAllVessels() {
        return vesselMasterDao.getVesselsList();
    }    
}
