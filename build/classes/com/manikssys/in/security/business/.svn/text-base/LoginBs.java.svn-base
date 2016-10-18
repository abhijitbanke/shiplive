/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.security.business;

import com.manikssys.in.common.PasswordEncryption;
import com.manikssys.in.common.dao.security.LoginDAO;
import com.manikssys.in.common.dao.security.MACMasterDAO;
import com.manikssys.in.common.dao.security.UserMasterDAO;
import com.manikssys.in.security.beans.*;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sandeep
 */
public class LoginBs implements ILoginBs{
    
    public boolean validateMAC(String macIds) {
        macIds=macIds.toUpperCase();
        MACMasterDAO macDao = new MACMasterDAO()  ;
        boolean statusFlag = false;
        List<ScrMacMaster> list = macDao.getMacAddresses();
        for (ScrMacMaster macMasterBean : list) {
            if (macIds.contains(macMasterBean.getMacId().trim())) {
                statusFlag = true;
                break;
            }
        }
        return statusFlag;
    }

    public ArrayList<ScrUserMaster> validateUser(ScrUserMaster userObj) {
        // encrpt the password
        PasswordEncryption.encrypt(userObj);

        UserMasterDAO masterDao = new UserMasterDAO();
        ArrayList<ScrUserMaster> userList = (ArrayList<ScrUserMaster>) masterDao.findByExample(userObj, new String[]{});
        return userList;
    }

    // Method to get the menu list for perticular user
    public ArrayList getMenuList(ScrUserProfileMaster login) {
        ArrayList menuList = null, parentList = null, childList = null;
        try {
            parentList = new ArrayList();
            childList = new ArrayList();
            menuList = new LoginDAO().getMenuList(login);
            if (menuList != null) {
                // Make two different list for parent & childs
                for (int i = 0; i < menuList.size(); i++) {
                    ScrMenuMaster menu = (ScrMenuMaster) menuList.get(i);
                    if (menu.getParentMenuId().equalsIgnoreCase("0")) {
                        System.out.println("Main Menu "+menu.getMenuName());
                        parentList.add(menu);
                    } else {
                        childList.add(menu);
                    }
                }

                // Add the subchilds to its appropriate child
                for (int i = 0; i < childList.size(); i++) {
                    ScrMenuMaster childMenu = (ScrMenuMaster) childList.get(i);
                    ArrayList subChildList = new ArrayList();
                    String menuId = childMenu.getMenuId();
                    for (int j = 0; j < childList.size(); j++) {
                        ScrMenuMaster subChildMenu = (ScrMenuMaster) childList.get(j);
                        String parentId = subChildMenu.getParentMenuId();
                        if (parentId.equalsIgnoreCase(menuId)) {
                            subChildList.add(subChildMenu);
                            //break;
                        }
                    }
                    childMenu.setChildList(subChildList);
                    childList.set(i, childMenu);
                }

                // Add Child Menus to appropriate Parents
                for (int i = 0; i < parentList.size(); i++) {
                    ScrMenuMaster parentMenu = (ScrMenuMaster) parentList.get(i);
                    ArrayList tempChildList = new ArrayList();
                    String menuId = parentMenu.getMenuId();
                    for (int j = 0; j < childList.size(); j++) {
                        ScrMenuMaster childMenu = (ScrMenuMaster) childList.get(j);
                        String parentId = childMenu.getParentMenuId();

                        if (parentId.equalsIgnoreCase(menuId)) {
                            tempChildList.add(childMenu);
                            //break;
                        }
                    }
                    parentMenu.setChildList(tempChildList);
                    parentList.set(i, parentMenu);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parentList;

    }

    public ArrayList<ScrMenuButtonProfileDetails> getAccessButtonList(String  menuId,String profileId){
         LoginDAO loginDao=new LoginDAO();
         return loginDao.getAccessButtonList(menuId,profileId);
    }

    
}
