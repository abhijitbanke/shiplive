package com.manikssys.in.common.dao.operational;

import com.manikssys.in.common.dao.GenericHibernateDAO;
import com.manikssys.in.operational.beans.OprEntrySeal;
import com.manikssys.in.security.beans.ScrUserMaster;
import java.util.Calendar;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**

 * User: sandeep
 * Date: Sep 14, 2010

 */

public class EntrySealDAO extends GenericHibernateDAO<OprEntrySeal, String> {

    public List<ScrUserMaster> getUsersOfTodaysEntries() {
        
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        today = cal.getTime();
        System.out.println("Today   >>> "+today);
        Criterion criterion = Expression.eq("id.createdDate", today);
        Criteria criteria = getSession().createCriteria(OprEntrySeal.class);
        criteria.setProjection(Projections.property("updatedBy"));
        criteria.add(criterion).add(Expression.eq("status","1"));

        return criteria.list();
    }

}