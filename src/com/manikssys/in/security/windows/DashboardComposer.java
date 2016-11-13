/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.security.windows;

/**
 *
 * @author sandeep
 */
import com.manikssys.in.common.CommonOperation;
import com.manikssys.in.common.ToolbarComposer;
import com.manikssys.in.operational.beans.OprEntrySeal;
import com.manikssys.in.operational.beans.OprEntrySealPK;
import com.manikssys.in.operational.business.EntrySealBs;
import com.manikssys.in.operational.business.IEntrySeal;
import com.manikssys.in.security.beans.ScrMenuMaster;
import com.manikssys.in.security.beans.ScrUserMaster;
import com.manikssys.in.security.business.ILoginBs;
import com.manikssys.in.security.business.LoginBs;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Author : Sandeep Natoo
 * Description : This class is the common class after login which handles dashboard
 */
public class DashboardComposer extends GenericForwardComposer {

    private Window dashboardWin, childWin;
    private Menubar menubar;
    private Include includePage;
    private Label displayFooter, displayDate, lbScreenPath, lblLoggedUser, copyRight, lblLoggedUserBranch;
    private Checkbox chkEntrySeal;
    //Timer timerShow;
    //int decimalStat, displayTime, accounting, solution;
    //String dispdate, timezone, finalDate;
    String strMenuPath;
    ScrUserMaster userMaster;
    //String customPath = "", defaultPath = "";
    //Style styleCss;
    private Combobox langChangeCombo;
    Div toolbarDiv;
    ToolbarComposer toolBarComposer;
    Tabbox tbTray;
    Tabs tbsTray;
    //Toolbarbutton tbbEntryFlag;
    OprEntrySeal entrySealMaster; // for Entry Sealing

    public void onCreate$dashboardWin(ForwardEvent event) {
        try {

            //Sessions.getCurrent().setAttribute("style", styleCss);

            // Draw Toolbar
            Hbox tbHBox = (Hbox) execution.createComponents("/toolbar.zul", toolbarDiv, null);
            toolBarComposer = (ToolbarComposer) tbHBox.getAttribute("commonHBox$composer");
            //System.out.println("GOT THE CoMpOSER > "+toolBarComposer);

            //tbbEntryFlag.setContext(getMenuContext(toolbarDiv));

            CommonOperation.beginTransaction(); // Here we start the hibernate transaction
            userMaster = ((ScrUserMaster) sessionScope.get("user"));
            //styleCss.setSrc(userMaster.getUser_theme());
            lblLoggedUser.setValue(userMaster.getUserName() == null ? "" : "Welcome - " + userMaster.getUserName()); // Show the logged in user
            lblLoggedUserBranch.setValue(userMaster.getOprBranchMaster().getBranchName() == null ? "" : userMaster.getOprBranchMaster().getBranchName()); // Show the logged user branch
            ILoginBs loginBs = new LoginBs();
            ArrayList arMenuList = loginBs.getMenuList(userMaster.getScrUserProfileMaster());

            generateMenu(arMenuList, menubar); // Call for draw the menus

            //appendDefaultMenu(menubar); // Append the Default menu for Seal Entry
            getTodaysEntrySeal(); // get the default entry Seal for the current branch

            setFooter();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to generate the Menu & its sub Menus
    public void generateMenu(ArrayList menuList, Component parentComponent) {
        try {
            for (int i = 0; i < menuList.size(); i++) {
                ScrMenuMaster parentMenu = (ScrMenuMaster) menuList.get(i);
                Menu pMenu = new Menu();
                // pMenu.setImage(getIconPath(parentMenu.getMenuCode()));
                pMenu.setLabel((parentMenu.getMenuName()));
                if (parentMenu.getParentMenuId().equals("0")) {
                    // get the real path
                    pMenu.setTooltiptext((parentMenu.getMenuName()));
                    pMenu.setWidth("71px");
                    //pMenu.addForward("onClick", self, "onMenuClick");
                    pMenu.setAttribute("menu", parentMenu);
                    pMenu.setAttribute("menuPath", strMenuPath + (parentMenu.getParentMenuId().equals("0") ? "" : " -> " + (parentMenu.getMenuName())) + " -> " + (parentMenu.getMenuName()));
                    strMenuPath = (parentMenu.getMenuName());  // get the first parent menu name
                } else {
                    pMenu.setLabel((parentMenu.getMenuName()));
                }

                ArrayList childList = parentMenu.getChildList();
                Menupopup menupopup = new Menupopup();
                // Drawing next level
                for (int j = 0; j < childList.size(); j++) {
                    ScrMenuMaster childMenu = (ScrMenuMaster) childList.get(j);
                    ArrayList subChildList = childMenu.getChildList();
                    if (subChildList.size() == 0) //  If the node having no child(s)
                    {
                        Menuitem menuItem = new Menuitem();
                        menuItem.setLabel((childMenu.getMenuName()));
                        menuItem.setAttribute("menu", childMenu);
                        menuItem.setAttribute("menuPath", strMenuPath + (parentMenu.getParentMenuId().equals("0") ? "" : " -> " + (parentMenu.getMenuName())) + " -> " + (childMenu.getMenuName()));

                        menuItem.addForward("onClick", self, "onMenuClick");
                        // menuItem.setLabel(parentMenu.getMenuName());
                        //menuItem.setImage(getIconPath(childMenu.getMenuCode()));
                        menuItem.setParent(menupopup);
                    } else // if node having child(s)
                    {
                        ArrayList tempList = new ArrayList();
                        tempList.add(childMenu);
                        generateMenu(tempList, menupopup);
                    }
                }
                menupopup.setParent(pMenu);
                menupopup.addForward(Events.ON_OPEN, self, "onMenuClick");

                pMenu.setParent(parentComponent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to handle menu click events
    public void onMenuClick(ForwardEvent event) {
        try {
            CommonOperation.commitTransaction(); // Here we close the hibernate transaction on each menu open
            ScrMenuMaster menu = null;
            if (event.getOrigin().getTarget() instanceof Menuitem) {
                menu = (ScrMenuMaster) event.getOrigin().getTarget().getAttribute("menu");
                lbScreenPath.setValue((String) event.getOrigin().getTarget().getAttribute("menuPath"));
                dashboardWin.getDesktop().setAttribute("screen", menu.getMenuCode());
                includePage.setSrc(menu.getMenuAction() == null ? "" : menu.getMenuAction());
                // Set the Access Controls
                if (menu.getMenuAction() != null) {
                    CommonOperation.beginTransaction(); // Here we start the hibernate transaction
                    ILoginBs loginBs = new LoginBs();
                    toolBarComposer.setUserAccessState(loginBs.getAccessButtonList(menu.getMenuId(), userMaster.getScrUserProfileMaster().getProfileId()));
                    CommonOperation.commitTransaction();
                }
            } else {
                //menu = (ScrMenuMaster) event.getOrigin().getTarget().getParent().getAttribute("menu");
                //lbScreenPath.setValue((String) event.getOrigin().getTarget().getParent().getAttribute("menuPath"));
            	includePage.setSrc("");
            }
            
           


          

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFooter() {
        try {
            //timerShow.setDelay(1 * 60 * 1000);
            //timerShow.start();
            //displayTime = 24;
            //dispdate = ""+new Date();
            //TimezoneConverter conDate = new TimezoneConverter();
            //Date utcDate = conDate.convert( new Date(), null);

            //long diff = new Date().getTime() - utcDate.getTime();
            //long min = diff / (1000 * 60);
            // long maxdays = min % 60;
            //long maxdaysRem = diff / (1000 * 60 * 60);
            copyRight.setValue(Labels.getLabel("label.common.copyright"));
            //displayFooter.setValue(utcDate + ",     "  + "(GMT+" + maxdaysRem + ":" + maxdays + "):");
            displayDate.setValue(new SimpleDateFormat("EEE MMM dd zzz yyyy").format(new Date()));
            //displayTime();  // Call for display time
        } catch (WrongValueException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }


    /* // This functionality has been dropped by new change suggestion
    // Method to create the defalut menu (Seal Entry) for make seal on the entry by branch
    private void appendDefaultMenu(Component parentComp){
    Menu defaltMenu=new Menu();
    defaltMenu.setLabel(Labels.getLabel("label.button.seal"));
    defaltMenu.setContext(getMenuContext(defaltMenu));
    defaltMenu.setParent(parentComp);
    }
    
    // Method to create the context for seal entry
    private Menupopup getMenuContext(Component parentComp){
    Menupopup menuPopup=new Menupopup();
    Menuitem menuItem =new Menuitem();
    menuItem.setLabel(Labels.getLabel("label.checkbox.seal"));
    menuItem.setAutocheck(true);
    menuItem.setCheckmark(true);
    menuItem.addForward(Events.ON_CHECK, self, "onSeal");
    menuItem.setParent(menuPopup);
    menuPopup.setParent(parentComp);
    IEntrySeal entrySealBs= new EntrySealBs();
    entrySealMaster=entrySealBs.getEntrySeal(userMaster);
    if(entrySealMaster != null)
    if(entrySealMaster.getStatus().equals("1"))
    menuItem.setChecked(true);
    return menuPopup;
    }
    
     */
    private void getTodaysEntrySeal() {
        IEntrySeal entrySealBs = new EntrySealBs();
        entrySealMaster = entrySealBs.getEntrySeal(userMaster);
        System.out.println("CURRENT ENTRY SEAL >>" + entrySealMaster);
        if (entrySealMaster != null) {
            System.out.println("CURRENT PK SEAL >> " + entrySealMaster.getId());
        }
        if (entrySealMaster != null) {
            if (entrySealMaster.getStatus().equals("1")) {
                chkEntrySeal.setChecked(true);
            }
        }
    }

    // Method to handle the on check event of Seal Entry
    public void onCheck$chkEntrySeal(ForwardEvent fEvt) {
        //Checkbox chkEntrySeal=(Checkbox)fEvt.getOrigin().getTarget();
        Date today = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(today);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        today = cal.getTime();
        System.out.println("Today   >>> "+today);
        boolean flag = false;
        if (chkEntrySeal.isChecked()) {
            flag = true;
        }
        System.out.println("ENTRY SEAL CHECKED ? " + flag);

        if (entrySealMaster == null) {
            entrySealMaster = new OprEntrySeal();
            entrySealMaster.setCreatedBy(userMaster);
        }
        entrySealMaster.setUpdatedBy(userMaster);
        entrySealMaster.setStatus(flag ? "1" : "0");
        OprEntrySealPK priEntrySeal = entrySealMaster.getId();
        System.out.println("priEntrySeal ===" + priEntrySeal);
        if (priEntrySeal == null) {
            priEntrySeal = new OprEntrySealPK();
            priEntrySeal.setBranchId(userMaster.getOprBranchMaster().getBranchId());
            priEntrySeal.setCreatedDate(today);
            entrySealMaster.setId(priEntrySeal);
        }
        System.out.println("entrySealMaster Details " + priEntrySeal.getCreatedDate() + " && " + priEntrySeal.getBranchId());
        CommonOperation.beginTransaction(); // Here we start the hibernate transaction
        IEntrySeal entrySealBs = new EntrySealBs();
        entrySealMaster = entrySealBs.sealEntry(entrySealMaster);
        CommonOperation.commitTransaction();
    }
//    public void onTimer$timerShow() {
//        displayTime(); // Call for display time
//    }
//
//    // Method to display the time on footer bar
//    private void displayTime() {
//        // setFooter();
//        try {
//            if (displayTime == 12) {
//                SimpleDateFormat sm = new SimpleDateFormat(dispdate + " hh:mm:ss");
//                finalDate = sm.format(new Date());
//                displayDate.setValue(finalDate);
//            } else {
//                SimpleDateFormat sm = new SimpleDateFormat(dispdate + " HH:mm:ss");
//                finalDate = sm.format(new Date());
//                displayDate.setValue(finalDate);
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
