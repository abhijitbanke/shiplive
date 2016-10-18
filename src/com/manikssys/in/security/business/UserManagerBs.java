package com.manikssys.in.security.business;

import com.manikssys.in.common.dao.security.UserMasterDAO;
import com.manikssys.in.security.beans.ScrUserMaster;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sandeep
 * Date: Jun 28, 2010
 */
public class UserManagerBs implements IUserManager{
    private UserMasterDAO userDao =new UserMasterDAO();

    public ScrUserMaster saveUser(ScrUserMaster user) {
       return userDao.makePersistent(user);
    }

     public List<ScrUserMaster> getUsersList(ScrUserMaster forUser) {
        forUser = userDao.findByExample(forUser, new String[]{}).get(0); // Make persistant so it will refresh
        ArrayList arCondList=new ArrayList();
        arCondList.add("0");
        arCondList.add("1");
        return userDao.getUsersList(forUser);
    }

    public List<ScrUserMaster> getUserList() {
       return userDao.getAllUsersList();
    }
}
