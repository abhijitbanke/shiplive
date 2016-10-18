package com.manikssys.in.operational.business;

import com.manikssys.in.common.dao.operational.BranchMasterDAO;
import com.manikssys.in.common.dao.operational.PortMasterDAO;
import com.manikssys.in.operational.beans.OprBranchMaster;
import com.manikssys.in.operational.beans.OprPortMaster;
import com.manikssys.in.security.beans.ScrUserMaster;
import java.util.ArrayList;

import java.util.List;

/**
 * User: sandeep
 * Date: Jul 1, 2010
 */
public class PortMasterBs implements IPortMaster{

    PortMasterDAO portDao=new PortMasterDAO();

    public List<OprPortMaster> fetchPortsList(){
        return portDao.getPortsList();
    }

    public OprPortMaster savePort(OprPortMaster portBean){
        return portDao.savePort(portBean);
    }

    public boolean isPortExist(String portCode){

        if (portDao.findPortByPortCode(portCode).size() > 0) {
            return true;
        } else {
            return false;
        }
    }

     public List<OprBranchMaster> getBranchList(ScrUserMaster forUser) {

        // Check if logged user is from main branch
        if(forUser.getOprBranchMaster().getBranchRoot().equals("0")){
            BranchMasterDAO branchDao=new BranchMasterDAO();
            return branchDao.getBranchList();
        }
        else
        {
            List arBranchList=new ArrayList();
            arBranchList.add(forUser.getOprBranchMaster());
            return arBranchList;
        }
    }

    public List<OprPortMaster> fetchPortSeqList(OprPortMaster example) {
        return portDao.getPortsSeqList(example);
    }
}
