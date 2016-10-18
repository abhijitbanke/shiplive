package com.manikssys.in.operational.business;

import com.manikssys.in.operational.beans.OprCargoHdr;
import com.manikssys.in.operational.beans.OprCountryMaster;
import com.manikssys.in.operational.beans.OprPortMaster;
import com.manikssys.in.operational.beans.OprVesselMaster;
import com.manikssys.in.operational.beans.OprVesselPosition;

import java.util.List;

/**
 * User: PC
 * Date: May 2011
 */
public interface IVesselPosition {
    public List<OprPortMaster> getPortsList();
    public List<OprVesselMaster> getVesselsList();
    public List<OprVesselPosition> getVPListByPort(OprVesselPosition vesselPositionMaster);
    public List<OprVesselPosition> getVPList(OprVesselPosition vesselPositionMaster);
    public OprVesselPosition saveVP(OprVesselPosition vesselPositionMaster);
    public List findNonSailedVPByPort(OprVesselPosition qVesselPositionMaster);
    public List<OprCountryMaster> getAllCountries();
    public List<OprVesselPosition> getVPListForLimitedDays(OprVesselPosition vesselPositionMaster);
}
