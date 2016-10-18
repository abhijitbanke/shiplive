package com.manikssys.in.operational.business;

import com.manikssys.in.operational.beans.OprBranchMaster;
import com.manikssys.in.operational.beans.OprPortMaster;
import com.manikssys.in.security.beans.ScrUserMaster;

import java.util.List;

/**
 * User: sandeep
 * Date: Jul 1, 2010
 */
public interface IPortMaster {
    public List<OprPortMaster> fetchPortsList();
    public OprPortMaster savePort(OprPortMaster portBean);
    public boolean isPortExist(String portCode);

    public List<OprBranchMaster> getBranchList(ScrUserMaster forUser);

    public List<OprPortMaster> fetchPortSeqList(OprPortMaster example);
}
