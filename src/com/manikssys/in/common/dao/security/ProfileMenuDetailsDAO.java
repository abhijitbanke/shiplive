package com.manikssys.in.common.dao.security;

import com.manikssys.in.common.dao.GenericHibernateDAO;
import com.manikssys.in.security.beans.ScrProfileMenuDetails;
import com.manikssys.in.security.beans.ScrUserProfileMaster;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sandeep
 * Date: Jun 25, 2010
 */
public class ProfileMenuDetailsDAO extends GenericHibernateDAO<ScrProfileMenuDetails, String> {

    // method to find menu/record against profile

    public List<ScrProfileMenuDetails> getMenuList(ScrUserProfileMaster profile_id) {

        List<ScrProfileMenuDetails> menuList = new ArrayList<ScrProfileMenuDetails>();
        Criterion findByProfile = Expression.eq("scrUserProfileMaster", profile_id);
         menuList = super.findByCriteria(findByProfile);
        //System.out.println("menu details list size for "+profile_id+" ==>" + menuList.size());
        return menuList;
    }
}