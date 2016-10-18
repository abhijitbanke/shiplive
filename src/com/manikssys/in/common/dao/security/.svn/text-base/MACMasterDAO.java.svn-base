/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.common.dao.security;

import com.manikssys.in.common.dao.GenericHibernateDAO;
import com.manikssys.in.security.beans.ScrMacMaster;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sandeep
 */
public class MACMasterDAO extends GenericHibernateDAO<ScrMacMaster, String> {


    public List getMacAddresses() {

        Criteria crit = getSession().createCriteria(ScrMacMaster.class);
        crit.add(Expression.eq("status", "1"));
        return  crit.list();
    }

    public List<ScrMacMaster> getMACList(){
        Criterion findByStatus = Expression.eq("status", "1");
        Criteria crit = getSession().createCriteria(ScrMacMaster.class).add(findByStatus);
        List list=crit.list();
        return list.size()>0 ?list:new ArrayList();
    }

    public ScrMacMaster saveMAC(ScrMacMaster macMaster){
        return makePersistent(macMaster);
    }

    public List findMACById(String macId){
        Criterion findById= Expression.eq("macId", macId);
        Criteria crit = getSession().createCriteria(ScrMacMaster.class).add(findById);
        List list=crit.list();
        return list.size()>0 ?list:new ArrayList();
    }

    public void deleteMAC(ScrMacMaster macMaster){
         makeTransient(macMaster);
    }
}
