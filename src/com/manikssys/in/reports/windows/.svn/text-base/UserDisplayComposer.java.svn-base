/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.reports.windows;

import com.manikssys.in.common.CommonOperation;
import com.manikssys.in.common.ICommonWindowInterface;
import com.manikssys.in.operational.beans.OprBranchMaster;
import com.manikssys.in.reports.business.IUserDisplay;
import com.manikssys.in.reports.business.UserDisplayBs;
import com.manikssys.in.security.beans.ScrUserMaster;
import org.w3c.dom.events.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;

import java.util.List;
import java.util.Set;

/**
 *
 * @author supriyas
 */
public class UserDisplayComposer extends GenericForwardComposer implements ICommonWindowInterface {

    Tree userTree;
    IUserDisplay userBs;

    public void onCreate$userDisplayWin() {
         userBs = new UserDisplayBs();
        CommonOperation.beginTransaction();
        List<OprBranchMaster> branchList = userBs.getBranchList();
        List<ScrUserMaster> userList = userBs.getActiveUserList();
        drawTree(branchList, userList);
        CommonOperation.commitTransaction();
    }

    public void handleEvent(Event evt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void drawTree(List<OprBranchMaster> branchList, List<ScrUserMaster> activeuserList) {

        Treechildren outerTreechildren = new Treechildren();
        outerTreechildren.setParent(userTree);
        try {

            for (OprBranchMaster branchMaster : branchList) {
             //   System.out.println("......branch" + branchMaster.getBranchName());
                Treeitem treeItem = new Treeitem();
                treeItem.setParent(outerTreechildren);
                Treerow treeRow = new Treerow();
                treeRow.setParent(treeItem);
                Treecell treeCell = new Treecell();
                treeCell.setLabel(branchMaster.getBranchName());
                treeCell.setParent(treeRow);

                Treechildren userChildren = new Treechildren();
                userChildren.setParent(treeItem);
                Set<ScrUserMaster> userSet = branchMaster.getScrUserMasters();
                //  System.out.println("the user size is=" + userSet.size());
                for (ScrUserMaster scrUserMaster : userSet) {

                    //   System.out.println(".............user" + scrUserMaster.getUserName());

                    Treeitem userItem = new Treeitem();
                    Treerow userRow = new Treerow();
                    Treecell userCell = new Treecell();
                    userCell.setLabel(scrUserMaster.getUserName());
                    userCell.setParent(userRow);
                    //userCell.setWidth("50%");
                    userRow.setParent(userItem);
                    userItem.setParent(userChildren);


                    System.out.println("tree item......." + treeItem);
                    //treeItem.setParent(treeChildren);
                    treeRow.setParent(treeItem);
                    // treeChildren.setParent(treeItem);

                    Treecell activeUserCell = new Treecell();
                    //activeUserCell.setLabel("Active");
                    activeUserCell.setParent(userRow);
                    Toolbarbutton tButton=new Toolbarbutton();
                    tButton.setStyle("float:left;height:20px;");
                    tButton.setParent(activeUserCell);
                    if (activeuserList.contains(scrUserMaster)) {
                        tButton.setImage("/images/edit.png");
                    }else{
                        tButton.setImage("/images/x-20.png");                        
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* for(int i=0;i<branchList.size();i++)
    {
    
    Treeitem treeItem = new Treeitem();
    Treerow treeRow = new Treerow();
    Treecell treeCell = new Treecell();
    Label label = new Label();
    label.setValue((String) (branchList.get(i)));
    
    treeCell.setLabel((String) (branchList.get(i)));
    treeCell.setParent(treeRow);
    treeRow.setParent(treeItem);
    
    Treechildren userChildren=new Treechildren();
    
    Treeitem userItem = new Treeitem();
    Treerow userRow = new Treerow();
    Treecell userCell = new Treecell();
    Label userLabel = new Label();
    userLabel.setValue((String) (userList.get(i)));
    
    userCell.setLabel((String) (userList.get(i)));
    
    userCell.setParent(userRow);
    userRow.setParent(userItem);
    userItem.setParent(userChildren);
    userChildren.setParent(treeItem);
    
    
    treeItem.setParent(treeChildren);
    treeChildren.setParent(userTree);
    
    
    }*/
    public void clearScreeen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void saveRecord() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addRecord() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void closeScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void retrieveRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deleteRecord() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setControls() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getFirstRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getLastRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getNextRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getPreviousRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
