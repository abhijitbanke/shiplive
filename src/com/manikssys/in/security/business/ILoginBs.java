/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.security.business;

import com.manikssys.in.security.beans.ScrMenuButtonProfileDetails;
import com.manikssys.in.security.beans.ScrUserMaster;
import com.manikssys.in.security.beans.ScrUserProfileMaster;

import java.util.ArrayList;

/**
 *
 * @author sandeep
 */
public interface ILoginBs {
    public boolean validateMAC(String macIds);
    public ArrayList<ScrUserMaster> validateUser(ScrUserMaster userObj);
    public ArrayList getMenuList(ScrUserProfileMaster login);
    public ArrayList<ScrMenuButtonProfileDetails> getAccessButtonList(String  menuId,String profileId);
}
