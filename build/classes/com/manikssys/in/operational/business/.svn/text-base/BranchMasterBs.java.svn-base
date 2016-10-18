package com.manikssys.in.operational.business;

import com.manikssys.in.common.dao.operational.BranchMasterDAO;
import com.manikssys.in.operational.beans.OprBranchMaster;
import com.manikssys.in.security.beans.ScrUserMaster;

import java.util.List;

/**
 * User: sandeep
 * Date: Jul 12, 2010
 */
public class BranchMasterBs implements IBranchMaster{

    BranchMasterDAO branchDao=new BranchMasterDAO();

    // Method to get the Head Branch & Logged User Branch
    public List getBranchDetails(ScrUserMaster userMaster){
        return branchDao.getBranchDetails(userMaster);
    }

    public OprBranchMaster saveBranch(OprBranchMaster branchMaster){
        return branchDao.makePersistent(branchMaster);
    }

     public boolean isBranchExist(String branchCode){

        if (branchDao.findBranchByCode(branchCode).size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<OprBranchMaster> findBranch(OprBranchMaster branchMaster){
        return branchDao.findBranch(branchMaster);
    }
}
