package com.manikssys.in.common.dao.security;

import com.manikssys.in.common.dao.GenericHibernateDAO;
import com.manikssys.in.security.beans.ScrUserMaster;
import com.manikssys.in.security.beans.ScrUserProfileMaster;
import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;

import java.util.List;

/**
 *
 * @author maniks
 */
public class UserProfileMasterDAO  extends GenericHibernateDAO<ScrUserProfileMaster, String> {

    public ScrUserProfileMaster findById(String profileId) {
        return findById(profileId, false);
    }

    public List<ScrUserProfileMaster> findAllUserProfiles(ScrUserMaster forUser){
        Criteria crit = getSession().createCriteria(ScrUserProfileMaster.class);
        crit.add(Expression.isNotNull("createdBy"));
        crit.add(Expression.eq("status", "1"));
        return  crit.list();
    }

}
