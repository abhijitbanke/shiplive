/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.common.dao;

/**
 *
 * @author sandeep
 */
import com.manikssys.in.operational.beans.OprCargoHdr;
import com.manikssys.in.operational.beans.OprVesselPosition;
import com.manikssys.in.security.beans.ScrUserMaster;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import com.manikssys.in.util.HibernateUtil;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.hibernate.criterion.Expression;

/**
 *
 * @author maniks
 */
public abstract class GenericHibernateDAO<T, ID extends Serializable>
        implements GenericDAO<T, ID> {

    private Class<T> persistentClass;
    // private Session session;

    public GenericHibernateDAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Session getSession() {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }

    public Class<T> getPersistentClass() {
        return persistentClass;
    }

    @SuppressWarnings("unchecked")
    public T findById(ID id, boolean lock) {
        T entity;
        if (lock) {
            entity = (T) getSession().load(getPersistentClass(), id, LockMode.UPGRADE);
        } else {
            entity = (T) getSession().load(getPersistentClass(), id);
        }

        return entity;
    }

    /* to get list based on branch : */
    @SuppressWarnings("unchecked")
    public List<T> findByBranchId(ScrUserMaster user) {
        Query q = getSession().createQuery("from " + getPersistentClass().getName() + " obj where obj.createdBy.oprBranchMaster =:branchId and obj.status='1'");
        q.setParameter("branchId", user.getOprBranchMaster());
        return q.list();
    }

    public List<T> findAll() {
        return findByCriteria();
    }

    public List<T> findAll(OprVesselPosition vesselPositionMaster) {
        Query q = getSession().createQuery("from " + getPersistentClass().getName() + " obj where obj.currentPort =:CURRENT_PORT and obj.entryStatus='1' and obj.vpStatus != '7' and obj.vpType =:CURRENT_VPTYPE");
        q.setParameter("CURRENT_PORT", vesselPositionMaster.getCurrentPort());
        q.setParameter("CURRENT_VPTYPE", vesselPositionMaster.getVpType());
        return q.list();
    }

    public List<T> findCargoBetweenDates(OprCargoHdr cargoHdrMaster) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        crit.add(Expression.between("createdDate", new Date(cargoHdrMaster.getFromDate().getTime()), new Date(cargoHdrMaster.getToDate().getTime())));

        return crit.list();
    }

    public List<T> findByExample(T exampleInstance, String[] excludeProperty) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        Example example = Example.create(exampleInstance);
        for (String exclude : excludeProperty) {
            example.excludeProperty(exclude);
        }
        crit.add(example);
        return crit.list();
    }

    public List<T> findVPForCargoHdr(OprVesselPosition vesselPositionMaster) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        if (vesselPositionMaster.getCurrentPort() != null) {
            Criterion findByPort = Expression.eq("currentPort", vesselPositionMaster.getCurrentPort());
            crit.add(findByPort);
        }
        if(vesselPositionMaster.getVessel() != null){
            Criterion findByVessel = Expression.eq("vessel", vesselPositionMaster.getVessel());
            crit.add(findByVessel);
        }
        crit.add(Expression.between("createdDate", new Date(vesselPositionMaster.getFromDate().getTime()), new Date(vesselPositionMaster.getToDate().getTime())));

        //crit.add(   Expression.ilike("vessel.vesselName",vesselPositionMaster.getPartialVesselString(),MatchMode.ANYWHERE)  );

        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public T makePersistent(T entity) {
        getSession().saveOrUpdate(entity);
        return entity;
    }

    public void makeTransient(T entity) {
        getSession().delete(entity);
    }

    public void flush() {
        getSession().flush();
    }

    public void clear() {
        getSession().clear();
    }

    /**
     * Use this inside subclasses as a convenience method.
     */
    @SuppressWarnings("unchecked")
    protected List<T> findByCriteria(Criterion... criterion) {
        Criteria crit = getSession().createCriteria(getPersistentClass());
        for (Criterion c : criterion) {
            crit.add(c);
        }
        return crit.list();
    }

    // Method to fire the dynamic select query ,in this we pass the query text & parameters in map for the query
    public List<T> findByHQL(String hql, HashMap paramMap) {
        Query hQuery = getSession().createQuery(hql);
        Set keys = paramMap.keySet();
        for (Iterator i = keys.iterator(); i.hasNext();) {
            String key = (String) i.next();
            Object value = paramMap.get(key);
            hQuery.setParameter(key, value);
        }
        return hQuery.list();
    }
}
