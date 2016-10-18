package com.manikssys.in.common.dao.operational;

import com.manikssys.in.common.dao.GenericHibernateDAO;
import com.manikssys.in.operational.beans.OprPortMaster;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.Order;

/**
 * User: sandeep
 * Date: Jul 1, 2010
 */
public class PortMasterDAO extends GenericHibernateDAO<OprPortMaster, String>{

    public List<OprPortMaster> getPortsList(){
        Criterion findByStatus = Expression.eq("status", "1");
        Criteria crit = getSession().createCriteria(OprPortMaster.class).add(findByStatus);
        crit.addOrder(Order.asc("portName"));
        List list=crit.list();
        return list.size()>0 ?list:new ArrayList();
    }

    public OprPortMaster savePort(OprPortMaster portMaster){
        return (OprPortMaster)makePersistent(portMaster);
    }

    public List findPortByPortCode(String portCode){
        OprPortMaster portMaster = new OprPortMaster();
        portMaster.setPortCode(portCode);
        portMaster.setStatus("1");
        return findByExample(portMaster, new String[]{});
    }

    public List<OprPortMaster> getPortsSeqList(OprPortMaster example) {
        Criterion findByStatus = Expression.eq("status", "1");
        Criteria crit = getSession().createCriteria(OprPortMaster.class).add(findByStatus);
        if(example.getDeSeq() == 3){
            crit.addOrder(Order.asc("deSeq"));
        }else if(example.getLeSeq() == 4){
            crit.addOrder(Order.asc("leSeq"));
        }else if(example.getDwSeq() == 5){
            crit.addOrder(Order.asc("dwSeq"));
        }else if(example.getLwSeq() == 6){
            crit.addOrder(Order.asc("lwSeq"));
        }

        List list=crit.list();
        return list.size()>0 ?list:new ArrayList();
    }


}
