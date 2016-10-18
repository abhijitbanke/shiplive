package com.manikssys.in.common.dao.security;

import com.manikssys.in.common.dao.GenericHibernateDAO;
import com.manikssys.in.security.beans.ScrMenuButtonProfileDetails;
import com.manikssys.in.security.beans.ScrUserProfileMaster;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Property;

import java.util.List;

/**
 * User: sandeep
 * Date: Jul 16, 2010
 */
public class MenuButtonProfileDAO extends GenericHibernateDAO<ScrMenuButtonProfileDetails, String> {
     // method to find menu/record against profile

    public List<ScrMenuButtonProfileDetails> getButtonDetails(ScrUserProfileMaster profile_id) {

        //List<ScrMenuButtonProfileDetails> menuList = new ArrayList<ScrMenuButtonProfileDetails>();
        Criterion findByProfile = Expression.eq("scrUserProfileMaster", profile_id);
        //menuList = super.findByCriteria(findByProfile);
        Criteria crit = getSession().createCriteria(getPersistentClass());
        crit.add(findByProfile).createCriteria("scrButtonMaster").addOrder(Property.forName("buttonName").asc());
        return crit.list();
        //return menuList;
    }
}
