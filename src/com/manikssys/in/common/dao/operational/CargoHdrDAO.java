/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.common.dao.operational;

import com.manikssys.in.common.dao.GenericHibernateDAO;
import com.manikssys.in.operational.beans.OprCargoHdr;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;

/**
 *
 * @author abhijit
 */
public class CargoHdrDAO extends GenericHibernateDAO<OprCargoHdr, String> {

    public OprCargoHdr saveVp(OprCargoHdr cargoHdrMaster) {

        //System.out.println("the object is"+vesselMaster);
        cargoHdrMaster = makePersistent(cargoHdrMaster);
        return cargoHdrMaster;
    }

//    public List<OprCargoHdr> findCargoHdr(OprCargoHdr exampleInstance, String[] excludeProperty) {
//        return findByExample(exampleInstance, excludeProperty);
//    }
    public List<OprCargoHdr> findCargoHdr(OprCargoHdr exampleInstance) {
        System.out.println("in find Cargo Hdr .................");
        Criteria crit = getSession().createCriteria(getPersistentClass());
        if (exampleInstance.getFromDate() != null && exampleInstance.getToDate() != null) {
            Date startDate = null;
            Date endDate = null;
            try {

                Date today = new Date();
                Calendar cal = new GregorianCalendar();
                cal.setTime(exampleInstance.getFromDate());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                startDate = cal.getTime();
                cal.setTime(exampleInstance.getToDate());
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                endDate = cal.getTime();
                System.out.println("Start date ------> " + startDate);
                System.out.println("End Date -------->" + endDate);
                crit.add(Expression.or(Expression.between("createdDate", new Date(startDate.getTime()), new Date(endDate.getTime())), Expression.between("updatedDate", new Date(startDate.getTime()), new Date(endDate.getTime()))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (exampleInstance.getLoadPortMaster() != null) {
            System.out.println("Loadport Crit............");
            crit.add(Expression.eq("loadPortMaster", exampleInstance.getLoadPortMaster()));
        }
        else if(exampleInstance.getDischargePortMaster() != null) {
            System.out.println("DischargePort Crit............");
            crit.add(Expression.eq("dischargePortMaster", exampleInstance.getDischargePortMaster()));
        }
        if (exampleInstance.getOprVesselPosition() != null) {
            System.out.println("VP  Crit............");
            Criterion findByVP = Expression.eq("oprVesselPosition", exampleInstance.getOprVesselPosition());
            crit.add(findByVP);
        }
        if (exampleInstance.getEgmNo() != null) {            
            Criterion findByEgmNo = Expression.eq("egmNo", exampleInstance.getEgmNo());
            crit.add(findByEgmNo);
        }
        if (exampleInstance.getIgmNo() != null) {            
            Criterion findByIgmNo = Expression.eq("igmNo", exampleInstance.getIgmNo());
            crit.add(findByIgmNo);
        }
//        System.out.println("___________________exampleInstance.getUpdatedBy().getOprBranchMaster().getBranchId()____"+exampleInstance.getUpdatedBy().getOprBranchMaster().getBranchId());
//        if(exampleInstance.getUpdatedBy().getOprBranchMaster().getBranchId().equals("1")){
//            System.out.println("--------------------------1");
//            crit.add(Expression.eq("createdBy.getOprBranchMaster().getBranchID",exampleInstance.getUpdatedBy().getOprBranchMaster().getBranchId()));
//        }
        crit.add(Expression.eq("cargoType", exampleInstance.getCargoType()));
        crit.add(Expression.eq("entryStatus",exampleInstance.getEntryStatus()));
        return crit.list();
    }

    public List<OprCargoHdr> findByExample(OprCargoHdr exampleInstance, String[] excludeProperty) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }
}
