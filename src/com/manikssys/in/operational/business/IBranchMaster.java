package com.manikssys.in.operational.business;

import com.manikssys.in.operational.beans.OprBranchMaster;
import com.manikssys.in.security.beans.ScrUserMaster;

import java.util.List;

/**
 * User: sandeep
 * Date: Jul 12, 2010
 */
public interface IBranchMaster {
    public List getBranchDetails(ScrUserMaster userMaster);
    public OprBranchMaster saveBranch(OprBranchMaster branchMaster);
    public boolean isBranchExist(String branchCode);
    public List<OprBranchMaster> findBranch(OprBranchMaster branchMaster);
}
