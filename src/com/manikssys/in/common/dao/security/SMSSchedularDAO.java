/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.common.dao.security;

import com.manikssys.in.common.dao.GenericHibernateDAO;
import com.manikssys.in.security.beans.ScrSmsScheduleMaster;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

/**
 *
 * @author pc
 */
public class SMSSchedularDAO extends GenericHibernateDAO<ScrSmsScheduleMaster, String> {
    

    public List<ScrSmsScheduleMaster> getScheduleForPosition(int position) {
        Criteria criteria = getSession().createCriteria(ScrSmsScheduleMaster.class);
        criteria.addOrder(Order.asc("id"));
        Criterion criterion = Expression.eq("id",position);
        criteria.add(criterion);
        return criteria.list();
    }

    public ScrSmsScheduleMaster saveScheduleData(ScrSmsScheduleMaster scheduleMaster) {
        System.out.println("DAO.saveScheduleData");
        scheduleMaster = makePersistent(scheduleMaster);
        return scheduleMaster;
    }
}
