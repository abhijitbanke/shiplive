package com.manikssys.in.security.business;

import com.manikssys.in.security.beans.ScrUserMaster;

import java.util.List;

/**
 * User: sandeep
 * Date: Jun 28, 2010
 */
public interface IUserManager {
    public ScrUserMaster saveUser(ScrUserMaster user);
    public List<ScrUserMaster> getUsersList(ScrUserMaster forUser);
    public List<ScrUserMaster> getUserList();
}
