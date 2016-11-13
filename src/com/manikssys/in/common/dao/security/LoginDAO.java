/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.common.dao.security;

import com.manikssys.in.security.beans.ScrMenuButtonProfileDetails;
import com.manikssys.in.security.beans.ScrMenuMaster;
import com.manikssys.in.security.beans.ScrUserProfileMaster;
import org.hibernate.Query;
import org.hibernate.Session;
import com.manikssys.in.util.HibernateUtil;

import java.util.ArrayList;
/**
 *
 * @author sandeep
 */
public class LoginDAO {

    public ArrayList<ScrMenuMaster> getMenuList(ScrUserProfileMaster login) {
        ArrayList<ScrMenuMaster> list = new ArrayList<ScrMenuMaster>();
        Session hSession = null;
        try {
            hSession = HibernateUtil.getSessionFactory().getCurrentSession();
            Query q = hSession.createQuery("from ScrMenuMaster menu where menu in (select scrMenuMaster from ScrProfileMenuDetails details where details.scrUserProfileMaster.profileId=:profileId and details.status ='1') and menu.status = '1' order by menu.parentMenuId,menu.menuSequence ");
            q.setParameter("profileId", login.getProfileId());
            list = (ArrayList<ScrMenuMaster>) q.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }

     public ArrayList<ScrMenuButtonProfileDetails> getAccessButtonList(String  menuId,String profileId) {
         ArrayList<ScrMenuButtonProfileDetails> list = new ArrayList<ScrMenuButtonProfileDetails>();
        Session hSession = null;
        try {
            hSession = HibernateUtil.getSessionFactory().getCurrentSession();
            Query q = hSession.createQuery("from ScrMenuButtonProfileDetails mbpDetls where mbpDetls.scrUserProfileMaster .id =:profileId and mbpDetls.scrMenuMaster .id =:menuId and mbpDetls.status ='1'");
            q.setParameter("menuId", menuId);
            q.setParameter("profileId", profileId);

            list = (ArrayList<ScrMenuButtonProfileDetails>) q.list();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
}
