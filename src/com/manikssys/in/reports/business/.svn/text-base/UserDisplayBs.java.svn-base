/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.reports.business;

import com.manikssys.in.common.dao.operational.BranchMasterDAO;
import com.manikssys.in.common.dao.operational.EntrySealDAO;
import com.manikssys.in.operational.beans.OprBranchMaster;
import com.manikssys.in.security.beans.ScrUserMaster;

import java.util.List;

/**
 *
 * @author supriyas
 */
public class UserDisplayBs implements IUserDisplay {

    /*ArrayList branch;
    ArrayList user;
    String id,branchId;*/
   



    public List<OprBranchMaster> getBranchList() {
        BranchMasterDAO branchMasterDAO = new BranchMasterDAO();
       return branchMasterDAO.getBranchList();
    }

    public List<ScrUserMaster> getActiveUserList() {
        /* This functionality is changed so its commented
        EntryMasterDAO entryMasterDAO = new EntryMasterDAO();
        return entryMasterDAO.getUsersOfTodaysEntries();
        */
        EntrySealDAO entrySealDAO = new EntrySealDAO();
        return entrySealDAO.getUsersOfTodaysEntries();        
    }

       
 /*   public void retrive() {
        Connection con = null;
        ResultSet rs, rs1 = null;
        
        branch = new ArrayList();
        user = new ArrayList();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://10.10.16.17:3306/Shiplive", "appUser", "appUser");
            Statement st = con.createStatement();
            rs = st.executeQuery("select branch_id,branch_name from opr_branch_master");
            while (rs.next()) {

                id = rs.getString("branch_id");
                 branch.add(rs.getString("branch_name"));

            }

            rs1 = st.executeQuery("select * from scr_user_master where branch_id=" + "'" + id + "'");
            while (rs1.next()) {
                 branchId=rs1.getString("branch_id");
                user.add(rs1.getString("user_name"));
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    
    
}
