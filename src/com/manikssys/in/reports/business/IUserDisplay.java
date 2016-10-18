/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.reports.business;

import com.manikssys.in.operational.beans.OprBranchMaster;
import com.manikssys.in.security.beans.ScrUserMaster;

import java.util.List;
/**
 *
 * @author supriyas
 */
public interface IUserDisplay 
{
   List<OprBranchMaster> getBranchList();
    List<ScrUserMaster> getActiveUserList();    
}
