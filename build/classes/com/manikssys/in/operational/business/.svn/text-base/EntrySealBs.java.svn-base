package com.manikssys.in.operational.business;

import com.manikssys.in.common.dao.operational.EntrySealDAO;
import com.manikssys.in.operational.beans.OprEntrySeal;
import com.manikssys.in.security.beans.ScrUserMaster;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

/**
 * User: sandeep
 * Date: Sep 14, 2010
 */
public class EntrySealBs implements IEntrySeal {

    EntrySealDAO entrySealDao=new EntrySealDAO();

    public OprEntrySeal getEntrySeal(ScrUserMaster userMaster){
        HashMap param = new HashMap();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String dateStr = sdf.format(new Date());
//        System.out.println("dateStr   ====>> "+dateStr);
//        Date today=null;
//        try {
//            today = sdf.parse(dateStr);
//        } catch (ParseException ex) {
//            Logger.getLogger(EntrySealBs.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        today = cal.getTime();
        System.out.println("Today   >>> "+today);        
        String query = "from OprEntrySeal ES where ES.id.branchId=:branchId and ES.id.createdDate=:date";
        param.put("branchId", userMaster.getOprBranchMaster().getBranchId());
        param.put("date",today);
            
       
        System.out.println("BEFORE RETRIVE> QUERY PARAM BARNCH : "+userMaster.getOprBranchMaster().getBranchId());
        
        List<OprEntrySeal> findList=entrySealDao.findByHQL(query,param);
        System.out.println("found res > "+findList.size());
        OprEntrySeal entrySealMaster=null;
        if(findList.size() >0){
            entrySealMaster= findList.get(0);
        }
        return entrySealMaster;
    }

    public OprEntrySeal  sealEntry(OprEntrySeal entrySealMaster) {
         return entrySealDao.makePersistent(entrySealMaster);
    }
}
