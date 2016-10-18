/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.common.dao.security;

import com.manikssys.in.common.dao.GenericHibernateDAO;
import com.manikssys.in.security.beans.ScrSmsHdr;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

/**
 *
 * @author abhijit
 */
public class SMSHolidayDAO extends GenericHibernateDAO<ScrSmsHdr, String>{
    public ScrSmsHdr saveHolidays(ScrSmsHdr scrSmsHdr) {

        //System.out.println("the object is"+vesselMaster);
        scrSmsHdr = makePersistent(scrSmsHdr);
        return scrSmsHdr;
    }

    public List<ScrSmsHdr> getTodaysSMSHdr() {
        Calendar cal = new GregorianCalendar();
        System.out.println("Today   ___>>> "+cal.getTime().getDay());
        Criterion criterion = Expression.eq("id", cal.getTime().getDay());
        Criteria criteria = getSession().createCriteria(ScrSmsHdr.class);
        criteria.add(criterion);
        return criteria.list();
    }

    public List<ScrSmsHdr> getHolidayList() {
        Criteria criteria = getSession().createCriteria(ScrSmsHdr.class);
        criteria.addOrder(Order.asc("id"));
        return criteria.list();
    }
}
