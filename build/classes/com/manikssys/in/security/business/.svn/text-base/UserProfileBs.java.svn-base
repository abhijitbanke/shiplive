package com.manikssys.in.security.business;

import com.manikssys.in.common.dao.operational.BranchMasterDAO;
import com.manikssys.in.common.dao.security.MenuButtonProfileDAO;
import com.manikssys.in.common.dao.security.ProfileMenuDetailsDAO;
import com.manikssys.in.common.dao.security.UserProfileMasterDAO;
import com.manikssys.in.operational.beans.OprBranchMaster;
import com.manikssys.in.security.beans.ScrMenuButtonProfileDetails;
import com.manikssys.in.security.beans.ScrProfileMenuDetails;
import com.manikssys.in.security.beans.ScrUserMaster;
import com.manikssys.in.security.beans.ScrUserProfileMaster;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sandeep
 * Date: Jun 25, 2010
 */

public class UserProfileBs implements IUserProfileBs {

    private UserProfileMasterDAO profileDao = new UserProfileMasterDAO();
    private ProfileMenuDetailsDAO profileMenuDetailsDao = new ProfileMenuDetailsDAO();
    private MenuButtonProfileDAO mbpDao=new MenuButtonProfileDAO();

    public ScrUserProfileMaster getProfile(String profileID) {
        return null;// profileDao.findById(profileID);
    }

    public List<ScrUserProfileMaster> getUserProfileList(ScrUserMaster forUser) {
        return profileDao.findAllUserProfiles(forUser);
    }

    public ScrUserProfileMaster saveProfile(ScrUserProfileMaster profile) {
        return profileDao.makePersistent(profile);
    }

    public List<ScrProfileMenuDetails> getProfileMenuDetails(ScrUserProfileMaster profile) {
        return profileMenuDetailsDao.getMenuList(profile);
    }

    public void deleteProfileMenuDetail(ScrProfileMenuDetails detail) {
        profileMenuDetailsDao.makeTransient(detail);
    }

    public ScrProfileMenuDetails saveProfileMenuDetail(ScrProfileMenuDetails detail) {
        return profileMenuDetailsDao.makePersistent(detail);
    }

    public List<OprBranchMaster> getBranchList(ScrUserMaster forUser) {

        // Check if logged user is from main branch
        if(forUser.getOprBranchMaster().getBranchRoot().equals("0")){
            BranchMasterDAO branchDao=new BranchMasterDAO();
            return branchDao.getBranchList();
        }
        else
        {
            List arBranchList=new ArrayList();
            arBranchList.add(forUser.getOprBranchMaster());
            return arBranchList;
        }
    }

    public List<ScrMenuButtonProfileDetails> getButtonDetails(ScrUserProfileMaster profile) {
        return mbpDao.getButtonDetails(profile);
    }
}
