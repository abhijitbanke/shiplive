/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.common.dao.operational;

import com.manikssys.in.common.dao.GenericHibernateDAO;
import com.manikssys.in.operational.beans.OprCountryMaster;
import com.manikssys.in.operational.beans.OprVesselMaster;
import java.util.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

/**
 *
 * @author pc
 */
public class VesselMasterDAO extends GenericHibernateDAO<OprVesselMaster, String>{
    public List<OprVesselMaster> getVesselsList(){
        Criterion findByStatus = Expression.eq("entryStatus", "1");
        Criteria crit = getSession().createCriteria(OprVesselMaster.class).add(findByStatus);
        crit.addOrder(Order.asc("vesselName"));
        List list=crit.list();
        return list.size()>0 ?list:new ArrayList();
    }

    public OprVesselMaster saveVessel(OprVesselMaster vesselMaster){
        return (OprVesselMaster)makePersistent(vesselMaster);
    }

    public List findVesselByVesselCode(String vesselCode){
        OprVesselMaster vesselMaster = new OprVesselMaster();
        vesselMaster.setVesselCode(vesselCode);
        vesselMaster.setEntryStatus("1");
        return findByExample(vesselMaster, new String[]{});
    }
    public List findVesselByVesselName(String vesselName){
        OprVesselMaster vesselMaster = new OprVesselMaster();
        vesselMaster.setVesselName(vesselName);
        vesselMaster.setEntryStatus("1");
        return findByExample(vesselMaster, new String[]{});
    }



    public List<OprCountryMaster> loadAllCountries(){
        Criteria crit = getSession().createCriteria(OprCountryMaster.class);
        crit.addOrder(Order.asc("countryName"));
        return crit.list();
    }


}
