package com.manikssys.in.security.windows;

/**
 * User: sandeep
 * Date: Jun 25, 2010
 */

import com.manikssys.in.common.CommonOperation;
import com.manikssys.in.common.ComponentValidation;
import com.manikssys.in.common.ICommonWindowInterface;
import com.manikssys.in.security.beans.*;
import com.manikssys.in.security.business.ILoginBs;
import com.manikssys.in.security.business.IUserProfileBs;
import com.manikssys.in.security.business.LoginBs;
import com.manikssys.in.security.business.UserProfileBs;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;

import java.util.*;


public class UserProfileComposer extends GenericForwardComposer implements ICommonWindowInterface{

    private static Logger log = Logger.getLogger(UserProfileComposer.class);
    private IUserProfileBs profileBS = new UserProfileBs();
    private Listbox profile;
    private Textbox profileName;
    private Textbox profileCode;
    private Tree profileTree;
    private Vbox statusBar;

    public void onCreate$userProfileWin() {

        Executions.getCurrent().getDesktop().setAttribute("currentWindow",this);
        ScrUserMaster userMaster = ((ScrUserMaster) sessionScope.get("user"));
        ILoginBs loginBs = new LoginBs();
        CommonOperation.beginTransaction();
        ArrayList arMenuList = loginBs.getMenuList(userMaster.getScrUserProfileMaster());
        List<ScrUserProfileMaster> userProfileList = profileBS.getUserProfileList(userMaster);
        List<ScrProfileMenuDetails> profileMenuDetails =
                profileBS.getProfileMenuDetails(userMaster.getScrUserProfileMaster()); //load menu dtls for profileId
        List<ScrMenuButtonProfileDetails> mbpDetailsList=profileBS.getButtonDetails(userMaster.getScrUserProfileMaster());
        for (ScrUserProfileMaster pbean : userProfileList) {
            Listitem item = new Listitem();
            item.setLabel(pbean.getProfileName());
            item.setValue(pbean);
            profile.appendChild(item);

        }
        profile.setSelectedIndex(0);

        drawTree(arMenuList, profileMenuDetails, mbpDetailsList, profileTree);
        System.out.println("____________________________");
        //initTree(profileTree.getTreechildren());
        CommonOperation.commitTransaction();

    }

    public void onSelect$profile() {
        Listitem item = profile.getSelectedItem();
        if (item != null && item.getValue() != null) {
            ScrUserProfileMaster bean = (ScrUserProfileMaster) item.getValue();
            profileTree.setAttribute("selectedProfile", bean);
            CommonOperation.beginTransaction();
            List<ScrProfileMenuDetails> scrProfileMenuDetails =
                    profileBS.getProfileMenuDetails(bean);
            log.debug("profile menu details count = " + scrProfileMenuDetails.size());
            List<ScrMenuButtonProfileDetails> mbpDetailsList=profileBS.getButtonDetails(bean);
            log.debug("Menu Button Profile details count = " + mbpDetailsList.size());
            profileName.setText(bean.getProfileName());
            profileCode.setText(bean.getProfileCode());
            System.out.println("Proile Id: " + bean.getProfileId());
            List<ScrProfileMenuDetails> list = new ArrayList<ScrProfileMenuDetails>(scrProfileMenuDetails);
            log.info("Populating menu tree of logged in user's profile...");
            populateTree(profileTree.getTreechildren(),list, mbpDetailsList,bean);
            CommonOperation.commitTransaction();
        }
    }


    // Method to create Trees based on menu(s)
    public void drawTree(ArrayList menuList, List<ScrProfileMenuDetails> menuDetails,List<ScrMenuButtonProfileDetails> mbpDetails,
                         Component parentComponent) {
        try {
            Treechildren treeChildren = new Treechildren();
            for (int i = 0; i < menuList.size(); i++) {
                ScrMenuMaster parentMenu = (ScrMenuMaster) menuList.get(i);
                Treeitem treeItem = new Treeitem();
                Treerow treeRow = new Treerow();
                Treecell treeCell = new Treecell();
                Label label = new Label();
                label.setValue((parentMenu.getMenuName()));
                Checkbox chkBox = new Checkbox();
                for (ScrProfileMenuDetails scrProfileMenuDetails : menuDetails) {
                    ScrMenuMaster menuMaster = scrProfileMenuDetails.getScrMenuMaster();
                    if (menuMaster.equals(parentMenu)) {

//                        chkBox.setId(scrProfileMenuDetails.getProfileMenuDtlsId());
                        ScrProfileMenuDetails newDetail = new ScrProfileMenuDetails(null, menuMaster, null, "0");
                        log.debug("putting: "+ newDetail);
                        chkBox.setAttribute("menuDetail", newDetail);
                        chkBox.setParent(treeCell);
                        chkBox.addForward("onCheck", self, "onMenuCheck");
                        label.setParent(treeCell);

                        treeCell.setParent(treeRow);
                        treeRow.setParent(treeItem);
                        treeItem.setParent(treeChildren);
                        break;
                    }
                }

                // Draw the Buttons
                for (ScrMenuButtonProfileDetails mbpBean : mbpDetails) {
                    ScrMenuMaster menuMaster = mbpBean.getScrMenuMaster();
                    ScrButtonMaster buttonMaster=mbpBean.getScrButtonMaster();
                    if (menuMaster.equals(parentMenu)) {

                        Treecell trCell=new Treecell();
                        ScrMenuButtonProfileDetails newDetail = new ScrMenuButtonProfileDetails(menuMaster, buttonMaster, null, "0");
                        Checkbox chBox=new Checkbox();
                        chBox.setAttribute("mbpDetail", newDetail);
                        chBox.setParent(trCell);
                        chBox.addForward("onCheck", self, "onMBPMenuCheck");
                        chBox.setLabel(buttonMaster.getButtonName());
                        trCell.setParent(treeRow);
                    }
                }

                // get the childs
                ArrayList childList = parentMenu.getChildList();
                if (childList != null && childList.size() > 0) {
                    chkBox.setAttribute("children", treeChildren);
                    drawTree(childList, menuDetails, mbpDetails,treeItem); // Call for draw next level
                }
            }

            treeChildren.setParent(parentComponent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     

    public void onMenuCheck(ForwardEvent evt) {
        Checkbox cb = (Checkbox) evt.getOrigin().getTarget();
        checkItem(cb, cb.isChecked());
    }

    private void checkItem(Checkbox checkbox , boolean isChecked) {
        checkbox.setChecked(isChecked);
        ScrProfileMenuDetails scrProfileMenuDetails = (ScrProfileMenuDetails) checkbox.getAttribute("menuDetail");
        scrProfileMenuDetails.setStatus(isChecked ? "1" : "0");

        //scrProfileMenuDetails.setProfileMenuDtlsId(null);
        log.debug("Menu Detail:  " + scrProfileMenuDetails);
        Treechildren children = ((Treeitem)checkbox.getParent().getParent().getParent()).getTreechildren();
        if (children != null) {
            for (Object object : children.getChildren()) {
                Treeitem item = (Treeitem) object;
                Treerow row = item.getTreerow();
                Checkbox childCheckbox = (Checkbox) ((Treecell) row.getChildren().get(0)).getChildren().get(0);
                checkItem(childCheckbox, isChecked);
            }
        }
        checkParent(checkbox,isChecked); // call for parent to check
    }

    // Method to check the parent node of tree when checks to any child
    private void checkParent(Checkbox chkBox , boolean isChecked){
        try {

            Treeitem tItem=chkBox.getParent().getParent().getParent().getParent().getParent().getClass().getName().equals("org.zkoss.zul.Treeitem")?(Treeitem)chkBox.getParent().getParent().getParent().getParent().getParent():null;
            if(tItem != null){

                Treechildren tChildren = ((Treechildren)chkBox.getParent().getParent().getParent().getParent());
                int chkCount=0;
                if (tChildren!= null) {
                    for (Object object : tChildren.getChildren()) {
                        Treeitem item = (Treeitem) object;
                        Checkbox adjChkbox = (Checkbox)item.getTreerow().getFirstChild().getFirstChild();
                        if(adjChkbox.isChecked() && !adjChkbox.equals(chkBox)){
                            chkCount++;
                        }
                    }
                }
                if(chkCount < 1){   // Check wheather any adjacent element checked or not
                    Checkbox pChkBox=(Checkbox)tItem.getTreerow().getFirstChild().getFirstChild();
                    pChkBox.setChecked(isChecked);
                    ScrProfileMenuDetails scrProfileMenuDetails = (ScrProfileMenuDetails) pChkBox.getAttribute("menuDetail");
                    scrProfileMenuDetails.setStatus(isChecked ? "1" : "0");
                    checkParent(pChkBox,isChecked); // Call again
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }


    public void onMBPMenuCheck(ForwardEvent evt) {
        Checkbox cb = (Checkbox) evt.getOrigin().getTarget();
        checkMBP(cb, cb.isChecked());
    }

    private void checkMBP(Checkbox checkbox , boolean isChecked) {
        checkbox.setChecked(isChecked);
        ScrMenuButtonProfileDetails mbpBean = (ScrMenuButtonProfileDetails) checkbox.getAttribute("mbpDetail");
        mbpBean.setStatus(isChecked ? "1" : "0");

        //scrProfileMenuDetails.setProfileMenuDtlsId(null);
        log.debug("Menu Detail:  " + mbpBean);
        //checkMBPParent(checkbox,isChecked); // call for parent to check
    }

    private void checkMBPParent(Checkbox chkBox , boolean isChecked){
        try {

        }catch (Exception e){e.printStackTrace();}
    }



    /**
     * Traverse the whole tree and check all menu items that are in selected profile's
     * menu details list.
     *
     * @param children               all tree items of tree
     * @param porfileMenuDetails     selected profile's all menu details
     * @param profile                selected profile
     */
    private void populateTree(Treechildren children, List<ScrProfileMenuDetails> porfileMenuDetails,List<ScrMenuButtonProfileDetails> mbpDetails, ScrUserProfileMaster profile) {

        if (children != null) {
            for (Object object : children.getChildren()) {
                Treeitem item = (Treeitem) object;
                Treerow row = item.getTreerow();
                Checkbox tf = (Checkbox) ((Treecell) row.getChildren().get(0)).getChildren().get(0);
                // Set to default properties
                tf.setChecked(false);
                ScrProfileMenuDetails sPMD = (ScrProfileMenuDetails) tf.getAttribute("menuDetail");
                ScrMenuMaster detail = (sPMD.getScrMenuMaster()); // get menu master
                ScrProfileMenuDetails newDetail = new ScrProfileMenuDetails(null, sPMD.getScrMenuMaster(), null, "0"); // Assign new propereties
                tf.setAttribute("menuDetail", newDetail); // set the new attribute


                for (ScrProfileMenuDetails scrProfileMenuDetails : porfileMenuDetails) {

                    ScrMenuMaster master = scrProfileMenuDetails.getScrMenuMaster();

                    // System.out.println("matching " + master + "detail " + detail);
                    if (detail != null && master.getMenuId().equals(detail.getMenuId())) {
                        // System.out.println("\t\t\t match found ...");

                        boolean isChecked = scrProfileMenuDetails.getStatus().trim().equals("1");
                        tf.setChecked(isChecked);
                        tf.setAttribute("menuDetail", scrProfileMenuDetails);
                        //System.out.println("set profile  = "+scrProfileMenuDetails.getScrUserProfileMaster());
                        //amd.setProfileMenuDtlsId(scrProfileMenuDetails.getProfileMenuDtlsId());
                        log.debug("populated : "+scrProfileMenuDetails);
                        break;
                    }
                }

                for(int i=1;i<row.getChildren().size();i++){
                    Checkbox chBox = (Checkbox) ((Treecell) row.getChildren().get(i)).getChildren().get(0);
                    // Set to default properties
                    chBox.setChecked(false);
                    ScrMenuButtonProfileDetails mbp=(ScrMenuButtonProfileDetails) chBox.getAttribute("mbpDetail");
                    ScrMenuMaster mbpMenuMaster = (mbp.getScrMenuMaster()); // get menu master
                    ScrButtonMaster buttonMaster= mbp.getScrButtonMaster();
                    ScrMenuButtonProfileDetails newmbpDetail = new ScrMenuButtonProfileDetails(mbpMenuMaster, buttonMaster, null, "0"); // Assign new propereties
                    chBox.setAttribute("mbpDetail", newmbpDetail); // set the new attribute
                    for (ScrMenuButtonProfileDetails mbpBean : mbpDetails) {
                        ScrMenuMaster mMaster = mbpBean.getScrMenuMaster();
                        ScrButtonMaster bMaster= mbpBean.getScrButtonMaster();
                        //System.out.println("MENU COMPAIRING "+mMaster.getMenuId()+" <> "+(mbpMenuMaster.getMenuId()));
                        //  System.out.println("BUTTON  COMPAIRING "+bMaster.getButtonId()+" <> "+(buttonMaster.getButtonId()));
                        if ((mbpMenuMaster != null && mMaster.getMenuId().equals(mbpMenuMaster.getMenuId())) && (buttonMaster != null && bMaster.getButtonId().equals(buttonMaster.getButtonId()))) {

                            boolean isChecked = mbpBean.getStatus().trim().equals("1");
                            chBox.setChecked(isChecked);
                            chBox.setAttribute("mbpDetail", mbpBean);
                            //log.debug("GOT MATCH : status "+mbpBean.getStatus()+" id>> "+mbpBean.getMnuButProfDtlsId()+" menu>> "+mbpBean.getScrMenuMaster().getMenuId()+"buttonid>>"+mbpBean.getScrButtonMaster().getButtonId()+"profile>> "+mbpBean.getScrUserProfileMaster().getProfileId());
                            //System.out.println("is Checked > "+chBox.isChecked()+"   "+chBox.getLabel());
                            break;
                        }
                    }
                }
                populateTree(item.getTreechildren(), porfileMenuDetails, mbpDetails,profile);

            }
        }
    }

    public void onClick$submit() {

        ScrUserMaster user = ((ScrUserMaster) sessionScope.get("user"));
        ScrUserProfileMaster bean = null;
        Listitem item = profile.getSelectedItem();
        boolean isNewProfile;
        if (item != null && item.getValue() != null) {
            bean = (ScrUserProfileMaster) item.getValue();
            bean.setUpdatedBy(user);
            bean.setUpdatedDate(new Date());
            isNewProfile = false;
        }    else {
            isNewProfile = true;
            bean = new ScrUserProfileMaster();
            bean.setStatus("1");
            bean.setCreatedBy(user);
            // Check profile code & profile name for duplicate
            Iterator itr=profile.getChildren().iterator();
            while(itr.hasNext()){
                ScrUserProfileMaster pBean=(ScrUserProfileMaster)((Listitem)itr.next()).getValue();
                if(pBean != null){
                    if(pBean.getProfileCode().equalsIgnoreCase(profileCode.getText())){ // if found
                        ComponentValidation.showErrorMessage(new WrongValueException(profileCode,Labels.getLabel("error.profile.profilecode.duplicate")));  // throw exception on profile code
                        return;
                    }
                    if(pBean.getProfileName().trim().equalsIgnoreCase(profileName.getText().trim())){ // if found
                        ComponentValidation.showErrorMessage(new WrongValueException(profileName,Labels.getLabel("error.profile.profilename.duplicate")));  // throw exception on profile name
                        return;
                    }
                }
            }
            bean.setCreatedDate(new Date());
        }

        if(!isValidate())  // validateion fails
            return ;

        bean.setProfileCode(profileCode.getText());
        bean.setProfileName(profileName.getText());

        log.debug("Collecting  selected menues...");
        Set<ScrProfileMenuDetails> selectedProfileMenusDetails = new HashSet<ScrProfileMenuDetails>();
        Set<ScrMenuButtonProfileDetails> selectedMBPDetails = new HashSet<ScrMenuButtonProfileDetails>();
        collectProfileMenuDetails(profileTree.getTreechildren(), selectedProfileMenusDetails, selectedMBPDetails,bean);
        log.debug("total menus selected by user = " + selectedProfileMenusDetails.size());

        // set selected profile menu details
        @SuppressWarnings("unchecked")
        Set<ScrProfileMenuDetails> menuDetails = bean.getScrProfileMenuDetailses();
        menuDetails.clear();

        // add new profile menu details
        menuDetails.addAll(selectedProfileMenusDetails);
        log.debug("save for profile id " + bean.getProfileId());

        log.debug("--------------------- List of collected profile menues ---------------------");
        for (ScrProfileMenuDetails scrProfileMenuDetails : selectedProfileMenusDetails) {
            System.out.println(scrProfileMenuDetails);
        }

        try {
            CommonOperation.beginTransaction();
            ScrUserProfileMaster storedProfile = profileBS.saveProfile(bean);
            if (storedProfile != null) {
                //showStatus(true);
                profileTree.setAttribute("selectedProfile", storedProfile);
                List<ScrProfileMenuDetails> list =  profileBS.getProfileMenuDetails(storedProfile);
                log.info("list menu details saved  = " + list.size());
                if (isNewProfile) {
                    Listitem newProfile = new Listitem();
                    newProfile.setLabel(storedProfile.getProfileName());
                    newProfile.setValue(storedProfile);
                    profile.appendChild(newProfile);
                }
            } else {
                //showStatus(false);
            }
            CommonOperation.commitTransaction("info.profile.save");
        } catch (Exception e) {
            //showStatus(false);
        }

    }

    // Method calls on click of delete button
    public void onClick$delete() {
        try {
            ScrUserMaster user = ((ScrUserMaster) sessionScope.get("user"));
            ScrUserProfileMaster bean = null;
            Listitem item = profile.getSelectedItem();
            if (item != null && item.getValue() != null) {
                bean = (ScrUserProfileMaster) item.getValue();
                bean.setUpdatedBy(user);
                bean.setUpdatedDate(new Date());
                bean.setStatus("0"); // Disable profile

                CommonOperation.beginTransaction();
                profileBS.saveProfile(bean);
                CommonOperation.commitTransaction("info.profile.delete");
                profile.removeChild(item);

                clearTree(false); // call for clear the screen
            }
        } catch (Exception e) {e.printStackTrace();}
    }

    // Method calls on click of Clear button
    public void onClick$clear() {
        clearTree(false);
    }

    /**
     * Collect all menu items which belong to the selected user profile or a new one to be
     * added to the profile. For each menu item set the menu status property to 1/0 (active/inactive)
     * depending upon the checkbox status.
     *
     * @param children                      all tree items of tree
     * @param selectedProfileMenuDetails    into which selected items will be added
     * @param profile                       selected items will belong this profile
     */
    private void collectProfileMenuDetails(Treechildren children,
                                           Set<ScrProfileMenuDetails> selectedProfileMenuDetails, Set<ScrMenuButtonProfileDetails> selectedMBPDetails,ScrUserProfileMaster profile) {

        if (children != null) {
            for (Object object : children.getChildren()) {
                Treeitem item = (Treeitem) object;
                Treerow row = item.getTreerow();
                Checkbox tf = (Checkbox) ((Treecell) row.getChildren().get(0)).getChildren().get(0);
                Label tb = (Label) ((Treecell) row.getChildren().get(0)).getChildren().get(1);
                ScrProfileMenuDetails scrProfileMenuDetails = (ScrProfileMenuDetails) tf.getAttribute("menuDetail");

                if (tf.isChecked() || scrProfileMenuDetails.getProfileMenuDtlsId() != null) {
                    scrProfileMenuDetails.setScrUserProfileMaster(profile);
                    selectedProfileMenuDetails.add(scrProfileMenuDetails);
                    System.out.println("collect: "+scrProfileMenuDetails);
                }
                for(int i=1;i<row.getChildren().size();i++){
                    Checkbox chBox = (Checkbox) ((Treecell) row.getChildren().get(i)).getChildren().get(0);
                    ScrMenuButtonProfileDetails mbpDetails = (ScrMenuButtonProfileDetails) chBox.getAttribute("mbpDetail");
                    if (chBox.isChecked() || mbpDetails.getMnuButProfDtlsId() != null) {
                        mbpDetails.setScrUserProfileMaster(profile);
                        selectedMBPDetails.add(mbpDetails);
                        System.out.println("collect: "+mbpDetails);
                    }
                }

                collectProfileMenuDetails(item.getTreechildren(), selectedProfileMenuDetails, selectedMBPDetails,profile);
            }
        }
    }

    private void showStatus(boolean status) {
        Label message = null;
        if (status) {
            message = new Label(Labels.getLabel("info.profile.save"));
            message.setStyle("font-weight:bold; color:green");
            CommonOperation.commitTransaction("info.profile.save");
        } else {
            message = new Label(Labels.getLabel("error.profile.nosave"));
            message.setStyle("font-weight:bold; color:red");
            CommonOperation.commitTransaction("error.profile.nosave");
        }
        statusBar.getChildren().clear();
        message.setParent(statusBar);
    }

    private boolean isValidate(){
        ArrayList<WrongValueException> errorList = new ArrayList<WrongValueException>();
        WrongValueException e = ComponentValidation.isAlfaNumericField(profileName, true);
        if (e != null) {
            errorList.add(e);
        }else{ComponentValidation.removeErrorMessage(profileName);}
        e = ComponentValidation.isAlfaNumericField(profileCode, true);
        if (e != null) {
            errorList.add(e);
        }else{ComponentValidation.removeErrorMessage(profileCode);}
        if (!errorList.isEmpty()) {
            ComponentValidation.showErrorMesList(errorList);
            return false;
        }
        ComponentValidation.removeErrorMesList(errorList);
        return true;
    }

    // Method to check / uncheck all the tree cells in the tree
    public void clearTree(boolean isChecked){

        Iterator iterator=profileTree.getItems().iterator(); // Get all tree childrens
        while(iterator.hasNext()) // get all checkboxes & set check/uncheck
        {
            Treeitem tItem = (Treeitem)iterator.next();
            Checkbox chkBox=((Checkbox)((Treecell)((Treerow)(tItem).getChildren().get(0)).getChildren().get(0)).getChildren().get(0));
            chkBox.setChecked(isChecked);
            ScrProfileMenuDetails scrProfileMenuDetails = (ScrProfileMenuDetails) chkBox.getAttribute("menuDetail");
            //scrProfileMenuDetails.setStatus(isChecked ? "1" : "0");
            ScrProfileMenuDetails newDetail = new ScrProfileMenuDetails(null, scrProfileMenuDetails.getScrMenuMaster(), null, isChecked ? "1" : "0");
            chkBox.setAttribute("menuDetail", newDetail);

            Treerow row=((Treerow)(tItem).getChildren().get(0));
            for(int i=1;i<row.getChildren().size();i++){
                Checkbox chBox = (Checkbox) ((Treecell) row.getChildren().get(i)).getChildren().get(0);
                chBox.setChecked(isChecked);
                ScrMenuButtonProfileDetails mbpDetails = (ScrMenuButtonProfileDetails) chBox.getAttribute("mbpDetail");
                ScrMenuButtonProfileDetails newMBPDetail = new ScrMenuButtonProfileDetails(mbpDetails.getScrMenuMaster(), mbpDetails.getScrButtonMaster(), null,isChecked ? "1" : "0");
                chBox.setAttribute("mbpDetail", newMBPDetail);
            }
        }
        profile.setSelectedIndex(0); // Set to default
        profileName.setText(""); // Set profile name to blank
        profileCode.setText(""); // Set profile code to blank
    }

    // Method calls on change of profile code
    public void onChange$profileCode(){
        ComponentValidation.removeErrorMessage(profileCode);
    }

    // Method calls on change on profile name
    public void onChange$profileName(){
        ComponentValidation.removeErrorMessage(profileName);
    }

    public void clearScreeen() {
        clearTree(false);
    }

    public void saveRecord() throws Exception {
        ScrUserMaster user = ((ScrUserMaster) sessionScope.get("user"));
        ScrUserProfileMaster bean = null;
        Listitem item = profile.getSelectedItem();
        boolean isNewProfile;
        if (item != null && item.getValue() != null) {
            bean = (ScrUserProfileMaster) item.getValue();
            bean.setUpdatedBy(user);
            bean.setUpdatedDate(new Date());
            isNewProfile = false;
        }    else {
            isNewProfile = true;
            bean = new ScrUserProfileMaster();
            bean.setStatus("1");
            bean.setCreatedBy(user);
            // Check profile code & profile name for duplicate
            Iterator itr=profile.getChildren().iterator();
            while(itr.hasNext()){
                ScrUserProfileMaster pBean=(ScrUserProfileMaster)((Listitem)itr.next()).getValue();
                if(pBean != null){
                    if(pBean.getProfileCode().equalsIgnoreCase(profileCode.getText())){ // if found
                        ComponentValidation.showErrorMessage(new WrongValueException(profileCode,Labels.getLabel("error.profile.profilecode.duplicate")));  // throw exception on profile code
                        return;
                    }
                    if(pBean.getProfileName().trim().equalsIgnoreCase(profileName.getText().trim())){ // if found
                        ComponentValidation.showErrorMessage(new WrongValueException(profileName,Labels.getLabel("error.profile.profilename.duplicate")));  // throw exception on profile name
                        return;
                    }
                }
            }
            bean.setCreatedDate(new Date());
        }

        if(!isValidate())  // validateion fails
            return ;

        bean.setProfileCode(profileCode.getText());
        bean.setProfileName(profileName.getText());

        log.debug("Collecting  selected menues...");
        Set<ScrProfileMenuDetails> selectedProfileMenusDetails = new HashSet<ScrProfileMenuDetails>();
        Set<ScrMenuButtonProfileDetails> selectedMBPDetails = new HashSet<ScrMenuButtonProfileDetails>();
        collectProfileMenuDetails(profileTree.getTreechildren(), selectedProfileMenusDetails,selectedMBPDetails ,bean);
        log.debug("total menus selected by user = " + selectedProfileMenusDetails.size());
        System.out.println("total buttons selected by user = " + selectedMBPDetails.size());
        // set selected profile menu details
        @SuppressWarnings("unchecked")
        Set<ScrProfileMenuDetails> menuDetails = bean.getScrProfileMenuDetailses();
        menuDetails.clear();

        // add new profile menu details
        menuDetails.addAll(selectedProfileMenusDetails);
        log.debug("save for profile id " + bean.getProfileId());

        log.debug("--------------------- List of collected profile menues ---------------------");
        for (ScrProfileMenuDetails scrProfileMenuDetails : selectedProfileMenusDetails) {
            System.out.println(scrProfileMenuDetails);
        }

        // set selected profile menu details
        @SuppressWarnings("unchecked")
        Set<ScrMenuButtonProfileDetails> mbpDetails = bean.getScrMenuButtonProfileDetailses();
        mbpDetails.clear();

        // add new profile menu details
        mbpDetails.addAll(selectedMBPDetails);

        log.debug("--------------------- List of collected Buttons menues ---------------------");
        for (ScrMenuButtonProfileDetails scrProfileMenuDetails : selectedMBPDetails) {
            System.out.println(scrProfileMenuDetails);
        }

        try {
            CommonOperation.beginTransaction();
            ScrUserProfileMaster storedProfile = profileBS.saveProfile(bean);
            if (storedProfile != null) {
                //showStatus(true);
                profileTree.setAttribute("selectedProfile", storedProfile);
                List<ScrProfileMenuDetails> list =  profileBS.getProfileMenuDetails(storedProfile);
                log.info("list menu details saved  = " + list.size());
                if (isNewProfile) {
                    Listitem newProfile = new Listitem();
                    newProfile.setLabel(storedProfile.getProfileName());
                    newProfile.setValue(storedProfile);
                    profile.appendChild(newProfile);
                }
            } else {
                //showStatus(false);
            }
            CommonOperation.commitTransaction("info.profile.save");
        } catch (Exception e) {
            //showStatus(false);
        }
    }

    public void addRecord() throws Exception {
        clearTree(false);
    }

    public void closeScreen() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void retrieveRecord() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteRecord() throws Exception {
        ScrUserMaster user = ((ScrUserMaster) sessionScope.get("user"));
        ScrUserProfileMaster bean = null;
        Listitem item = profile.getSelectedItem();
        if (item != null && item.getValue() != null) {
            bean = (ScrUserProfileMaster) item.getValue();
            bean.setUpdatedBy(user);
            bean.setUpdatedDate(new Date());
            bean.setStatus("0"); // Disable profile

            CommonOperation.beginTransaction();
            profileBS.saveProfile(bean);
            CommonOperation.commitTransaction("info.profile.delete");
            profile.removeChild(item);

            clearTree(false); // call for clear the screen
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