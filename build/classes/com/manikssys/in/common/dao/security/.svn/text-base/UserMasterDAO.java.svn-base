/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.common.dao.security;

import com.manikssys.in.common.Constants;
import com.manikssys.in.common.dao.GenericHibernateDAO;
import com.manikssys.in.security.beans.ScrUserMaster;
import com.manikssys.in.operational.beans.OprBranchMaster;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import java.util.ArrayList;

/**
 *
 * @author sandeep
 */
public class UserMasterDAO extends GenericHibernateDAO<ScrUserMaster, String> {

    public boolean isUsernameExists(String userName) {
        ScrUserMaster user = new ScrUserMaster();
        user.setUserName(userName);
        if (findByExample(user, new String[]{}).size() > 0) {
            return true;
        }
        return false;
    }

     public ArrayList<ScrUserMaster> getUsersList(ScrUserMaster userMaster) {
        Criteria cri = getSession().createCriteria(ScrUserMaster.class);
         OprBranchMaster branchMaster=userMaster.getOprBranchMaster();
         if(!branchMaster.getBranchId().equals("1"))
            cri.add(Expression.eq("oprBranchMaster", branchMaster));
        //cri.add(Expression.eq("status", "1"));
        cri.add(Expression.isNotNull("createdBy"));
        return (ArrayList<ScrUserMaster>) cri.list();
    }

     public ArrayList<ScrUserMaster> getAllUsersList() {
        Criteria cri = getSession().createCriteria(ScrUserMaster.class);
        cri.add(Expression.eq("status", Constants.NON_DELETED));
        return (ArrayList<ScrUserMaster>) cri.list();
    }
}
