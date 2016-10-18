package com.manikssys.in.security.business;

import com.manikssys.in.operational.beans.OprBranchMaster;
import com.manikssys.in.security.beans.ScrMenuButtonProfileDetails;
import com.manikssys.in.security.beans.ScrProfileMenuDetails;
import com.manikssys.in.security.beans.ScrUserMaster;
import com.manikssys.in.security.beans.ScrUserProfileMaster;

import java.util.List;

/**
 * User: sandeep
 * Date: Jun 25, 2010
 */
public interface IUserProfileBs {

    public ScrUserProfileMaster getProfile(String profileID);

    public List<ScrUserProfileMaster> getUserProfileList(ScrUserMaster forMandate);

    public ScrUserProfileMaster saveProfile(ScrUserProfileMaster profile);

    public List<ScrProfileMenuDetails> getProfileMenuDetails(ScrUserProfileMaster profile);

    public void deleteProfileMenuDetail(ScrProfileMenuDetails detail);

    public ScrProfileMenuDetails saveProfileMenuDetail(ScrProfileMenuDetails detail);

    public List<OprBranchMaster> getBranchList(ScrUserMaster forUser);

     public List<ScrMenuButtonProfileDetails> getButtonDetails(ScrUserProfileMaster profile);
}
