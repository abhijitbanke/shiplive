package com.manikssys.in.security.windows;


import com.manikssys.in.common.CommonOperation;
import com.manikssys.in.common.ComponentValidation;
import com.manikssys.in.common.ICommonWindowInterface;
import com.manikssys.in.common.PasswordEncryption;
import com.manikssys.in.operational.beans.OprBranchMaster;
import com.manikssys.in.security.beans.ScrUserMaster;
import com.manikssys.in.security.beans.ScrUserProfileMaster;
import com.manikssys.in.security.business.IUserManager;
import com.manikssys.in.security.business.IUserProfileBs;
import com.manikssys.in.security.business.UserManagerBs;
import com.manikssys.in.security.business.UserProfileBs;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;

import java.util.Date;
import java.util.List;

/**
 * User: sandeep
 * Date: Jun 28, 2010
 */
public class UserManagerComposer extends GenericForwardComposer implements ICommonWindowInterface {

    private static Logger log = Logger.getLogger(UserManagerComposer.class);
    private IUserProfileBs profileBS = new UserProfileBs();
    private IUserManager userBS = new UserManagerBs();
    private Window userManagerWin;
    private Button save, edit, clear;
    private Button delete;
    private Button restore;
    private Listbox profile;
    private Listbox branch;
    private Textbox userId;
    private Textbox userName;
    private Textbox password;
    private Textbox rePassword;
    private Textbox mobile;
    private Textbox firstName;
    private Textbox lastName;
    private Textbox email;
    private Textbox telephone;
    private Vbox statusBar;
    private Listbox userListbox;
    private Grid userForm;
    //ScrUserMaster user = null;

    public void onClick$save() {

        ScrUserMaster user = (ScrUserMaster) userForm.getAttribute("selectedUser");
        CommonOperation.beginTransaction();
        if (!isValidInput()) {
            CommonOperation.commitTransaction();
            return;
        }

        boolean newUser = false;
        if (user == null) {// new user
            user = new ScrUserMaster();
            newUser = true;
        }
        String usrLastPass = userForm.getAttribute("selectedUser") == null ? "" : user.getUserPassword(); // get the last password stored
        //System.out.println("user id to update -->  "+user.getUserId());
        user.setUserName(userName.getText());
        user.setUserPassword(password.getText());
        user.setUserFullName(firstName.getText() + " " + lastName.getText());
        user.setUserEmail(email.getText());
        user.setUserMobile(mobile.getText());
        user.setUserTelephone(telephone.getText());
        user.setUserEmail(email.getText());
        user.setStatus("1");
        Listitem item = profile.getSelectedItem();
        user.setScrUserProfileMaster(item != null ? (ScrUserProfileMaster) item.getValue() : null);

        Listitem brItem = branch.getSelectedItem();
        user.setOprBranchMaster(brItem != null ? (OprBranchMaster) brItem.getValue() : null);


        ScrUserMaster loggedUser = (ScrUserMaster) sessionScope.get("user");

        if (newUser || usrLastPass != user.getUserPassword()) {  // Check for new user or password has been changed
            PasswordEncryption.encrypt(user); // pass user for password encryption
        }

        if (user.getUserId() == null) {
            user.setCreatedBy(loggedUser.getUserId());
            user.setCreatedDate(new java.sql.Timestamp(new Date().getTime()));
        } else {
            user.setUpdatedBy(loggedUser.getUserId());
            user.setUpdatedDate(new java.sql.Timestamp(new Date().getTime()));
        }


        ScrUserMaster storedUser = userBS.saveUser(user);
        if (storedUser != null && storedUser.getUserId() != null) {
            //  showStatus(true, "info.user.save");
            if (newUser && CommonOperation.commitTransaction("info.user.save")) {
                addUserToList(storedUser, userListbox.getChildren().size()); // add new user to user list
            }
            userForm.setAttribute("selectedUser", storedUser);
            setButtonStatus(true);
        }/* else {
        // showStatus(false, "error.user.nosave");
        }*/
        resetUserForm();
    }

    public void onCreate$userManagerWin() {

        Executions.getCurrent().getDesktop().setAttribute("currentWindow", this);
        CommonOperation.beginTransaction();
        ScrUserMaster loggedUser = (ScrUserMaster) sessionScope.get("user");

        for (ScrUserProfileMaster pbean : profileBS.getUserProfileList(loggedUser)) {
            Listitem item = new Listitem();
            item.setLabel(pbean.getProfileName());
            item.setValue(pbean);
            profile.appendChild(item);
        }
        profile.setSelectedIndex(0);

        // Populating the Branchs
        for (OprBranchMaster branchBean : profileBS.getBranchList(loggedUser)) {
            Listitem item = new Listitem();
            item.setLabel(branchBean.getBranchName());
            item.setValue(branchBean);
            branch.appendChild(item);
        }
        branch.setSelectedIndex(0);

        userListbox.addEventListener("onSelect", new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                populateUserForm();
            }
        });
        save.setLabel(Labels.getLabel("button.user.save"));
        loadUserList(loggedUser);
        CommonOperation.commitTransaction();
        setButtonStatus(true);
    }

    private void loadUserList(ScrUserMaster forUser) {

        //System.out.println("finding users for user -- " + forUser.getUserId());
        List<ScrUserMaster> userList = userBS.getUsersList(forUser);
        //System.out.println("found " + userList.size() + " users.");
        for (ScrUserMaster user : userList) {
            if (user.getStatus().equals("1")) {
                addUserToList(user, userList.indexOf(user) + 1);
            }
        }

    }

    private void addUserToList(ScrUserMaster user, int position) {
        String style = user.getStatus().equals("1") ? "none" : "text-decoration:line-through;color:gray";
        Listitem item = new Listitem();
        Listcell srno = new Listcell(String.valueOf(position));
        srno.setStyle(style);
        srno.setParent(item);
        Listcell name = new Listcell(user.getUserName());
        name.setStyle(style);
        name.setParent(item);
        Listcell profileName = new Listcell(user.getScrUserProfileMaster().getProfileName());
        profileName.setStyle(style);
        profileName.setParent(item);
        item.setValue(user);
        userListbox.appendChild(item);
    }

    public void populateUserForm() {

        Listitem item = userListbox.getSelectedItem();
        if (item == null) {
            return;
        }
        ScrUserMaster user = (ScrUserMaster) item.getValue();
        log.debug("selected user - " + user);
        userForm.setAttribute("selectedUser", user);
        userName.setText(user.getUserName());
        password.setText(user.getUserPassword());
        rePassword.setText(user.getUserPassword());
        String[] names = user.getUserFullName().split(" ");
        firstName.setText(names.length > 0 ? names[0] : "");
        email.setText(user.getUserEmail());
        mobile.setText(user.getUserMobile());
        lastName.setText(names.length > 1 ? names[1] : "");
        email.setText(user.getUserEmail());
        telephone.setText(user.getUserTelephone());

        log.debug("user profile - " + user.getScrUserProfileMaster());
        for (Object obj : profile.getChildren()) {
            Listitem itm = (Listitem) obj;
            //System.out.println("list p -- "+ (ScrUserProfileMaster) itm.getValue());
            if (itm.getValue() != null && ((ScrUserProfileMaster) itm.getValue()).equals(user.getScrUserProfileMaster())) {
                itm.setSelected(true);
                break;
            }
        }
        for (Object obj : branch.getChildren()) {
            Listitem itm = (Listitem) obj;
            if (itm.getValue() != null && ((OprBranchMaster) itm.getValue()).equals(user.getOprBranchMaster())) {
                itm.setSelected(true);
                break;
            }
        }
        setButtonStatus(user.getStatus().equals("1") ? null : false);
        //setFields(true); // Disabling all the fields
        statusBar.getChildren().clear();
        log.info("user/employee form populated");
    }

    void setButtonStatus(Boolean status) {
        if (status != null) {
            if (status) { // if true(in case of save)
                restore.setVisible(false);
                //save.setLabel(Labels.getLabel("button.user.edit"));
                edit.setVisible(false);
                save.setVisible(true);
                delete.setVisible(true);
            } else { // if false (In case of restore)
                restore.setVisible(true);
                save.setVisible(false);
                delete.setVisible(false);
                edit.setVisible(false);
            }
        } else { // if null (in case of edit)
            edit.setVisible(true);
            restore.setVisible(false);
            save.setVisible(false);
            delete.setVisible(false);
        }
    }

    public void onClick$delete() {
        Listitem item = userListbox.getSelectedItem();
        ScrUserMaster user = (ScrUserMaster) userForm.getAttribute("selectedUser");
        if (item == null || user == null) {
            return;
        }

        user.setStatus("0");
        CommonOperation.beginTransaction();
        ScrUserMaster deletedUser = userBS.saveUser(user);

        //if(deletedUser != null && deletedUser.getStatus().equals("0")){
        if (CommonOperation.commitTransaction("info.user.delete")) {
            //showStatus(true, "info.user.delete");
            userListbox.removeChild(item); //delete user from user list
            addUserToList(deletedUser, userListbox.getChildren().size()); // add it as deleted entity
            resetUserForm();
        } else {
            //showStatus(false, "error.user.nodelete");
        }
    }

    public void onClick$restore() {
        Listitem item = userListbox.getSelectedItem();
        ScrUserMaster user = (ScrUserMaster) userForm.getAttribute("selectedUser");
        if (item == null || user == null || user.getStatus().equals("1")) {
            return;
        }

        user.setStatus("1");
        CommonOperation.beginTransaction();
        ScrUserMaster restoredUser = userBS.saveUser(user);
        // if(restoredUser != null && restoredUser.getStatus().equals("1")){
        if (CommonOperation.commitTransaction("info.user.restore")) {
            // showStatus(true, "info.user.restore");
            /*userListbox.removeChild(item); // remove delete user entry from user list
            addUserToList(restoredUser, userListbox.getChildren().size()); // add it as normal entity*/
            markListItem(item, false);
            userForm.setAttribute("selectedUser", restoredUser);
            setButtonStatus(true);

        } else {
            //showStatus(false, "error.user.norestore");
        }
    }

    // Method calls on click of edit button
    public void onClick$edit() {
        setFields(false);
        setButtonStatus(true); // Enabling all the fields
    }

    // Method calls on click of clear button
    public void onClick$clear() {
        resetUserForm(); // reset for adding new entry
    }

    private void showStatus(boolean status, String msgKey) {
        Label message = null;
        if (status) {
            message = new Label(Labels.getLabel(msgKey));
            message.setStyle("font-weight:bold; color:green");
            CommonOperation.commitTransaction(null);
        } else {
            message = new Label(Labels.getLabel(msgKey));
            message.setStyle("font-weight:bold; color:red");
            CommonOperation.commitTransaction(msgKey);
        }
        statusBar.getChildren().clear();
        message.setParent(statusBar);
    }

    public void onChange$userName() {
        log.debug("validate username");
        WrongValueException e = ComponentValidation.validateManditoryTextbox(userName, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            return;
        }

        ComponentValidation.removeErrorMessage(userName);
    }

    public void onChange$firstName() {
        log.debug("validate firstname");
        WrongValueException e = ComponentValidation.validateManditoryTextbox(firstName, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            return;
        }

        ComponentValidation.removeErrorMessage(firstName);
    }

    public void onChange$password() {
        log.debug("validate password: " + password.getValue());
        WrongValueException e = ComponentValidation.validateManditoryTextbox(password, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            return;
        }
        ComponentValidation.removeErrorMessage(password);
    }

    public void onChange$rePassword() {
        WrongValueException e = ComponentValidation.validateManditoryTextbox(rePassword, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            return;
        }
        if (!(rePassword.getText().equals(password.getText()))) {
            e = new WrongValueException(rePassword, org.zkoss.util.resource.Labels.getLabel("error.user.wrong.repassword"));
        }
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            return;
        }
        ComponentValidation.removeErrorMessage(rePassword);
    }

    public void onSelect$profile() {
        log.debug("validate profile ");
        WrongValueException e = ComponentValidation.validateManditoryList(profile, Labels.getLabel("error.user.mandatory.profile"));
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            return;
        }
        ComponentValidation.removeErrorMessage(profile);
    }

    public void onSelect$branch() {
        log.debug("validate profile ");
        WrongValueException e = ComponentValidation.validateManditoryList(branch, Labels.getLabel("error.user.mandatory.branch"));
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            return;
        }
        ComponentValidation.removeErrorMessage(branch);
    }

    public void onChange$lastName() {



        WrongValueException e = ComponentValidation.validateManditoryTextbox(lastName, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            return;
        }
        ComponentValidation.removeErrorMessage(lastName);

    }

    public void onChange$telephone() {

        WrongValueException e1 = ComponentValidation.validateManditoryTextbox(telephone, null);
        if (e1 != null) {
            ComponentValidation.showErrorMessage(e1);
            return;
        }
        WrongValueException e = ComponentValidation.isPositiveNumericField(telephone, true);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            return;
        }
        ComponentValidation.removeErrorMessage(telephone);
    }

    public void onChange$email() {


        WrongValueException e = ComponentValidation.isValidEmail(email, null);
        if (e != null) {
            ComponentValidation.showErrorMessage(e);
            return;
        }
        ComponentValidation.removeErrorMessage(email);
    }

    private boolean isValidInput() {
        onChange$userName();
        onChange$password();
        onChange$rePassword();
        onChange$firstName();
        onChange$lastName();
        onSelect$profile();
        onSelect$branch();
        onChange$email();


        return !(ComponentValidation.isErrorToolTip(userName)
                || ComponentValidation.isErrorToolTip(firstName)
                || ComponentValidation.isErrorToolTip(password)
                || ComponentValidation.isErrorToolTip(rePassword)
                || ComponentValidation.isErrorToolTip(email)
                || ComponentValidation.isErrorToolTip(lastName)
                || ComponentValidation.isErrorToolTip(telephone)
                || ComponentValidation.isErrorToolTip(branch)
                || ComponentValidation.isErrorToolTip(profile));
    }

    public void resetUserForm() {
        //System.out.println("reseting user form...");
        userListbox.clearSelection();

        userForm.removeAttribute("selectedUser");
        userName.setText("");
        password.setText("");
        rePassword.setText("");
        firstName.setText("");
        email.setText("");
        mobile.setText("");
        lastName.setText("");
        email.setText("");
        telephone.setText("");
        profile.setSelectedIndex(0);
        branch.setSelectedIndex(0);
        setButtonStatus(true); // Set the buttons in save mode
        setFields(false); // set fields to editable
        // Removing error messages if any
        ComponentValidation.removeErrorMessage(userName);
        ComponentValidation.removeErrorMessage(password);
        ComponentValidation.removeErrorMessage(rePassword);
        ComponentValidation.removeErrorMessage(rePassword);
        ComponentValidation.removeErrorMessage(firstName);
        ComponentValidation.removeErrorMessage(profile);
        ComponentValidation.removeErrorMessage(branch);
    }

    // Method for enable/disable fields on the screen
    public void setFields(boolean isDisable) {
        userName.setReadonly(isDisable);
        password.setReadonly(isDisable);
        rePassword.setReadonly(isDisable);
        firstName.setReadonly(isDisable);
        email.setReadonly(isDisable);
        mobile.setReadonly(isDisable);
        lastName.setReadonly(isDisable);
        email.setReadonly(isDisable);
        telephone.setReadonly(isDisable);
        profile.setDisabled(isDisable);
        branch.setDisabled(isDisable);
    }

    private void markListItem(Listitem item,boolean flag){
        String style=flag?"text-decoration:line-through;color:gray":"text-decoration:none;color:black";
        List<Listcell> cellList=item.getChildren();
        for(Listcell cell:cellList){
            cell.setStyle(style);
        }
        //item.setStyle(style);
    }

    public void clearScreeen() {
        resetUserForm(); // reset for adding new entry
    }

    public void saveRecord() throws Exception {

        ScrUserMaster user = (ScrUserMaster) userForm.getAttribute("selectedUser");
        CommonOperation.beginTransaction();
        if (!isValidInput()) {
            CommonOperation.commitTransaction();
            return;
        }


        boolean newUser = false;
        if (user == null) {// new user
            user = new ScrUserMaster();
            newUser = true;
        }
        String usrLastPass = userForm.getAttribute("selectedUser") == null ? "" : user.getUserPassword(); // get the last password stored
        //System.out.println("user id to update -->  "+user.getUserId());
        user.setUserName(userName.getText());
        user.setUserPassword(password.getText());
        user.setUserFullName(firstName.getText() + " " + lastName.getText());
        user.setUserEmail(email.getText());
        user.setUserMobile(mobile.getText());
        user.setUserTelephone(telephone.getText());
        user.setUserEmail(email.getText());
        user.setStatus("1");
        Listitem item = profile.getSelectedItem();
        user.setScrUserProfileMaster(item != null ? (ScrUserProfileMaster) item.getValue() : null);

        Listitem brItem = branch.getSelectedItem();
        user.setOprBranchMaster(brItem != null ? (OprBranchMaster) brItem.getValue() : null);

        ScrUserMaster loggedUser = (ScrUserMaster) sessionScope.get("user");


        if (newUser || usrLastPass != user.getUserPassword()) {  // Check for new user or password has been changed
            PasswordEncryption.encrypt(user); // pass user for password encryption
        }

        if (user.getUserId() == null) {
            user.setCreatedBy(loggedUser.getUserId());
            user.setCreatedDate(new java.sql.Timestamp(new Date().getTime()));
        } else {
            user.setUpdatedBy(loggedUser.getUserId());
            user.setUpdatedDate(new java.sql.Timestamp(new Date().getTime()));
        }


        ScrUserMaster storedUser = userBS.saveUser(user);
        if (storedUser != null && storedUser.getUserId() != null) {
            //  showStatus(true, "info.user.save");
            if (newUser) {
                addUserToList(storedUser, userListbox.getChildren().size()); // add new user to user list
            }
            userForm.setAttribute("selectedUser", storedUser);
            setButtonStatus(true);
        } else {
            // showStatus(false, "error.user.nosave");
        }
        CommonOperation.commitTransaction("info.user.save");
        resetUserForm();
    }

    public void addRecord() throws Exception {
        resetUserForm(); // reset for adding new entry
    }

    public void closeScreen() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void retrieveRecord() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteRecord() throws Exception {
        Listitem item = userListbox.getSelectedItem();
        ScrUserMaster user = (ScrUserMaster) userForm.getAttribute("selectedUser");
        if (item == null || user == null) {
            return;
        }

        // Validate Self Destroy Option
        ScrUserMaster loggedUser = (ScrUserMaster) sessionScope.get("user");
        if (loggedUser.getUserId().equals(user.getUserId())) {
            CommonOperation.showDashboardMessage(Labels.getLabel("error.user.validation.selfdestroy"), 'F');
            return;
        }

        user.setStatus("0");
        CommonOperation.beginTransaction();
        ScrUserMaster deletedUser = userBS.saveUser(user);

        //if(deletedUser != null && deletedUser.getStatus().equals("0")){
        if (CommonOperation.commitTransaction("info.user.delete")) {
            //showStatus(true, "info.user.delete");
            /*userListbox.removeChild(item); //delete user from user list
            addUserToList(deletedUser, userListbox.getChildren().size()); // add it as deleted entity*/
            markListItem(item, true);
//            removeItemFromList(deletedUser);
            resetUserForm();
        } else {
            //showStatus(false, "error.user.nodelete");
        }
    }

    private void removeItemFromList(ScrUserMaster deletedUser) {       
        List<Listitem> itemList = userListbox.getChildren();
        for(Listitem item : itemList){
            ScrUserMaster user = (ScrUserMaster) item.getValue();
            if(user.getUserId() == null ? deletedUser.getUserId() == null : user.getUserId().equals(deletedUser.getUserId())){
                userListbox.removeChild(item);
            }
        }
    }

    public void setControls() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void getFirstRecord() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void getLastRecord() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void getNextRecord() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void getPreviousRecord() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
