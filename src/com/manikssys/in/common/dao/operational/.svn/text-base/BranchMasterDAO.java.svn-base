package com.manikssys.in.common.dao.operational;

import com.manikssys.in.common.dao.GenericHibernateDAO;
import com.manikssys.in.operational.beans.OprBranchMaster;
import com.manikssys.in.security.beans.ScrUserMaster;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;

import java.util.List;

/**
 * User: sandeep
 * Date: Jun 29, 2010
 */
public class BranchMasterDAO extends GenericHibernateDAO<OprBranchMaster, String>{

    public List getBranchDetails(ScrUserMaster userMaster){

        Criteria crit = getSession().createCriteria(OprBranchMaster.class);
        crit.add(Expression.or
        (Expression.eq("branchId","1"),
         Expression.eq("branchId",userMaster.getOprBranchMaster().getBranchId())));
        crit.add(Expression.eq("status", "1"));
        return  crit.list();
    }

    public List findBranchByCode(String branchCode){
        OprBranchMaster branchMaster = new OprBranchMaster();
        branchMaster.setBranchCode(branchCode);
        branchMaster.setStatus("1");
        return findByExample(branchMaster, new String[]{});
    }

    public List<OprBranchMaster> getBranchList() {

        Criteria crit = getSession().createCriteria(OprBranchMaster.class);
        crit.add(Expression.eq("status", "1"));
        return  crit.list();
    }

    public List<OprBranchMaster> findBranch(OprBranchMaster branchMaster){
        Criteria crit = getSession().createCriteria(OprBranchMaster.class);
        Example example = Example.create(branchMaster);
        example.enableLike(MatchMode.ANYWHERE);
        crit.add(example);
        return crit.list();
    }
}
