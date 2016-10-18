/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.common.dao.operational;

import com.manikssys.in.common.Constants;
import com.manikssys.in.common.dao.GenericHibernateDAO;
import com.manikssys.in.operational.beans.OprCargoHdr;
import com.manikssys.in.operational.beans.OprCountryMaster;
import com.manikssys.in.operational.beans.OprVesselPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

/**
 *
 * @author pc
 */
public class VesselPositionDAO extends GenericHibernateDAO<OprVesselPosition, String> {

    public OprVesselPosition saveVp(OprVesselPosition vesselPositionMaster) {

        //System.out.println("the object is"+vesselMaster);
        vesselPositionMaster = makePersistent(vesselPositionMaster);
        return vesselPositionMaster;
    }

    public List<OprVesselPosition> listAllVessels(OprVesselPosition vesselPositionMaster) {
        return findAll(vesselPositionMaster);
    }

    /**
     * Search the vessel by using entry master
     * criteria:- first find out entry withing date range
     *            then check for entryType(cargoType) dry or liquid
     *            alongwith we can specify the vessel name and port id also
     * @param vesselMaster
     * @return list of vessel
     */
    public List<OprVesselPosition> searchVessels(OprVesselPosition vesselPositionMaster) {

        String strQuery = "SELECT * FROM opr_vessel_master as vm join opr_entry_master as em on vm.load_port_id=em.port_id "
                + "where vm.vessel_status='1' and em.entry_status='1' and  vm.cargo_type= :cargo_type and em.entry_type= :entry_type"
                + " and em.created_date between :frmDate and :toDate";
        if (vesselPositionMaster.getVessel() != null && vesselPositionMaster.getVessel().getVesselName().length() > 0) {
            // add vessel to criteria
            strQuery = strQuery + " and vm.vessel_name like '" + vesselPositionMaster.getVessel() + "%'";
        }
        SQLQuery sqlQuery = getSession().createSQLQuery(strQuery);
        sqlQuery.addEntity(OprVesselPosition.class);
        return sqlQuery.list();
    }

    public List<OprCountryMaster> loadAllCountries() {
        Criteria crit = getSession().createCriteria(OprCountryMaster.class);
        return crit.list();
    }

    @Override
    public List<OprVesselPosition> findByExample(OprVesselPosition exampleInstance, String[] excludeProperty) {
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

                crit.add(Expression.or(Expression.between("createdDate", new Date(startDate.getTime()), new Date(endDate.getTime())), Expression.between("updatedDate", new Date(startDate.getTime()), new Date(endDate.getTime()))));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (exampleInstance.getCurrentPort() != null) {
            crit.add(Expression.eq("currentPort", exampleInstance.getCurrentPort()));
        }
//        if(!exampleInstance.getCreatedBy().getOprBranchMaster().getBranchId().equals("1")){  
//            System.out.println("exampleInstance.createdBy().getOprBranchMaster().getBranchId()______________"+exampleInstance.getCreatedBy().getOprBranchMaster().getBranchId());
//            crit.add(Expression.eq("createdBy.getOprBranchMaster()", exampleInstance.getCreatedBy().getOprBranchMaster()));
//        }
        crit.add(Expression.eq("entryStatus", Constants.NON_DELETED));
        crit.add(Expression.eq("vpType", exampleInstance.getVpType()));
        return crit.list();
    }

    @Override
    public List<OprVesselPosition> findVPForCargoHdr(OprVesselPosition vesselPositionMaster) {
        System.out.println("in FindVPForCargoHdr.......................................");
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Date startDate = null;
        Date endDate = null;
        try {

            Date today = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(vesselPositionMaster.getFromDate());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            startDate = cal.getTime();
            cal.setTime(vesselPositionMaster.getToDate());
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            endDate = cal.getTime();

            Criterion ctrn1 = Expression.between("createdDate", new Date(startDate.getTime()), new Date(endDate.getTime()));            
            Criterion ctrn2 = Expression.between("updatedDate", new Date(startDate.getTime()), new Date(endDate.getTime()));
            
            crit.add(Expression.or(ctrn1, ctrn2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (vesselPositionMaster.getVessel() != null) {
            Criterion findByVessel = Expression.eq("vessel", vesselPositionMaster.getVessel());
            crit.add(findByVessel);
        }
        if(vesselPositionMaster.getCurrentPort() != null){
            crit.add(  Expression.eq("currentPort", vesselPositionMaster.getCurrentPort())  );
        }              
        if (vesselPositionMaster.getVpType().equalsIgnoreCase(Constants.DRYCARGO_ENTRY)) {
            crit.add(Expression.or(Expression.eq("vpType", Constants.VP_EASTDRY_ENTRY), Expression.eq("vpType", Constants.VP_WESTDRY_ENTRY)));
        } else if(vesselPositionMaster.getVpType().equalsIgnoreCase(Constants.LIQUIDCARGO_ENTRY)){
            crit.add(Expression.or(Expression.eq("vpType", Constants.VP_WESTLIQUID_ENTRY), Expression.eq("vpType", Constants.VP_EASTLIQUID_ENTRY)));
        }
        if(vesselPositionMaster.getOps().equalsIgnoreCase(Constants.LOAD_OPERATION)) {
            crit.add(Expression.or(   Expression.and(Expression.eq("ops", Constants.LOAD_UNLOAD_OPERATION), Expression.eq("vpStatus", Constants.SAILED_VESSEL))                                        ,      Expression.and(Expression.eq("ops", Constants.LOAD_OPERATION), Expression.eq("vpStatus", Constants.SAILED_VESSEL))));
        }else if(vesselPositionMaster.getOps().equalsIgnoreCase(Constants.UNLOAD_OPERATION)){
            crit.add(Expression.or(   Expression.and(Expression.eq("ops", Constants.LOAD_UNLOAD_OPERATION), Expression.eq("vpStatus", Constants.DISCHARGED_VESSEL))                                        ,      Expression.and(Expression.eq("ops", Constants.UNLOAD_OPERATION), Expression.eq("vpStatus", Constants.SAILED_VESSEL))));
        }
        crit.add(Expression.eq("entryStatus", Constants.NON_DELETED));
        return crit.list();
    }

    @Override
    public List<OprVesselPosition> findAll(OprVesselPosition vesselPositionMaster) {
        System.out.println("in findAll FindVPFor  no sailed.......................................");
        Date startDate = null;
        Date endDate = null;
        Criteria crit = getSession().createCriteria(getPersistentClass());
        if (vesselPositionMaster.getFromDate() != null && vesselPositionMaster.getToDate() != null) {
            try {
                //Date today = new Date();
                Calendar cal = new GregorianCalendar();
                cal.setTime(vesselPositionMaster.getFromDate());
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                startDate = cal.getTime();
                cal.setTime(vesselPositionMaster.getToDate());
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                endDate = cal.getTime();
                Criterion ctrn1 = Expression.between("createdDate", new Date(startDate.getTime()), new Date(endDate.getTime()));                
                Criterion ctrn2 = Expression.between("updatedDate", new Date(startDate.getTime()), new Date(endDate.getTime()));                
                crit.add(Expression.or(ctrn1, ctrn2));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (vesselPositionMaster.getCurrentPort() != null) {
            Criterion findByPort = Expression.eq("currentPort", vesselPositionMaster.getCurrentPort());
            crit.add(findByPort);
        }
        if (vesselPositionMaster.getVessel() != null) {
            Criterion findByVessel = Expression.eq("vessel", vesselPositionMaster.getVessel());
            crit.add(findByVessel);
        }
        crit.add(Expression.eq("entryStatus", Constants.NON_DELETED));

        crit.add(Expression.eq("vpType", vesselPositionMaster.getVpType()));

        crit.add(Expression.not(Expression.eq("vpStatus", Constants.SAILED_VESSEL)));

//        crit.addOrder(Order.asc("vesselName"));

        return crit.list();
    }
}
