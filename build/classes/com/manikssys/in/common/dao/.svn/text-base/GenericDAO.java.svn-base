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
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author maniks
 */
public interface GenericDAO<T, ID extends Serializable> {

    T findById(ID id, boolean lock);

    List<T> findAll();
    public List<T> findAll( OprVesselPosition vesselPositionMaster);

//    public List<T> findAll( OprCargoHdr cargoHdrMaster);

    List<T> findByExample(T exampleInstance, String[] excludeProperty);

    T makePersistent(T entity);
}
