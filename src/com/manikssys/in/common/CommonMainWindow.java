/*
 * By this class we can handle all window of application - nishesh
 */
package com.manikssys.in.common;

import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
//import org.maniks.slite.common.Constants;
//import org.maniks.slite.security.beans.ButtonStatusBS;
//import org.maniks.slite.security.beans.ParentMenu;
//import org.maniks.slite.security.beans.User;
//import org.maniks.slite.security.business.UserProfileBS;
//import org.maniks.slite.security.business.ValidateUserBs;


import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

public class CommonMainWindow extends Window implements EventListener {

    int counter = 1;
    private int newButton1 = 0;
    private int delete1 = 0;
    private int retrieve1 = 0;
    private int save1 = 0;
    private int firstRecord1 = 0;
    private int preRecord1 = 0;
    private int nextRecord1 = 0;
    private int lastRecord1 = 0;
    private int excel1 = 0;
    private int clear1 = 1;
    private int a = 1;
    private boolean useAddStatus = false;
    private boolean useSaveStatus = false;
    private boolean useDeleteStatus = false;
//    UserProfileBS b1 = new UserProfileBS();
    Tabs subMenuTabs = null;
    Tabpanels subTabPanels = null;
    private Tabbox tbTray;
    private Tabs tbsTray;


    public int getDelete1() {
        return delete1;
    }

    public void setDelete1(int delete1) {
        this.delete1 = delete1;
    }

    public int getNewButton1() {
        return newButton1;
    }

    public void setNewButton1(int newButton1) {
        this.newButton1 = newButton1;
    }

    public int getSave1() {
        return save1;
    }

    public void setSave1(int save1) {
        this.save1 = save1;
    }

    public void tabPanelOnEvent(Tab tb) {
        Tabbox tbboxnw = new Tabbox();
        Tabs tbsnw = new Tabs();
        Tabpanels tbpnlsnw = new Tabpanels();
        tbpnlsnw.getChildren().clear();
        Tabs tbs = (Tabs) tb.getParent();

        Tabbox tbox = (Tabbox) tbs.getParent();
        Tabpanel tbpo = (Tabpanel) tbox.getSelectedPanel();
        if (tbpo.getChildren().size() == 0) {
            tbpo.setVisible(true);
            tabMenu(tbsnw, tbpnlsnw, Integer.parseInt("" + tb.getAttribute("MENUID")));
            tbsnw.setParent(tbboxnw);
            tbpnlsnw.setParent(tbboxnw);
            tbboxnw.setParent(tbpo);
        }
    }

    public void tabPanelTreeOnEvent(Tab tb) {
        Tabs tbs = (Tabs) tb.getParent();

        Tabbox tbox = (Tabbox) tbs.getParent();

        Tabpanel tbpo = (Tabpanel) tbox.getSelectedPanel();
        Tabpanels tbps = (Tabpanels) tbpo.getParent();
        Tree tree = new Tree();
        Treechildren treeChld = new Treechildren();
        Treeitem ti = new Treeitem();
        int menuId = Integer.parseInt("" + tb.getAttribute("MENUID"));

        if (tbpo.getChildren().size() == 0) {
            tbpo.setVisible(true);
            tree(ti, menuId);
            ti.setParent(treeChld);
            treeChld.setParent(tree);
            tree.setParent(tbpo);
        }
    }

    void tree(Treeitem ti, int menuId) {

//        String profid = ((User) Sessions.getCurrent().getAttribute("user")).getProfileId();
//        ValidateUserBs vUser = new ValidateUserBs();
//        ArrayList menuList = vUser.getMenuNodes(profid, menuId);
//
//        if (menuList.size() > 0) {
//            Treechildren tchd = new Treechildren();
//            for (int i = 0; i < menuList.size(); i++) {
//                ParentMenu parentMenu = (ParentMenu) menuList.get(i);
//                Treeitem titm = new Treeitem();
//                Treerow tr = new Treerow();
//                Treecell tc = new Treecell();
//                tc.setImage("/images/arrow.png");
//                tc.setStyle("padding-top:2px; padding-bottom:2px; ");
//                tc.setLabel(parentMenu.getMenuName());
//                tc.setAttribute("action", parentMenu.getMenuAction());
//                tc.setAttribute("MENUID", parentMenu.getMenuId());
//                tc.setAttribute("pageid", parentMenu.getShortName());
//                tc.setAttribute("prop", "3");
//                tc.addEventListener("onClick", this);
//                tr.appendChild(tc);
//                titm.appendChild(tr);
//                tree(titm, Integer.parseInt(parentMenu.getMenuId()));
//                tchd.appendChild(titm);
//                ti.appendChild(tchd);
//            }
//        }
    }

    public void onEvent(Event event) throws Exception {
        Object object = event.getTarget();
        if (event.getName().equals("onClick")) {
            int pProp = Integer.parseInt((String) event.getTarget().getAttribute("prop"));
            switch (pProp) {
                case 1:
                    if (object.getClass().getName().equalsIgnoreCase("org.zkoss.zul.Tab")) {
                        Tab tb = (Tab) event.getTarget();
                        //tabPanelOnEvent(tb);
                        ArrayList arrmenubutton = new ArrayList();
//                        User user = (User) Sessions.getCurrent().getAttribute("user");
//                        ButtonStatusBS bbsbs = new ButtonStatusBS();
                        String menuid = "" + tb.getAttribute("MENUID");

                        if (subMenuTabs.getChildren().size() >= 0) {
                            subMenuTabs.getChildren().clear();
                            subTabPanels.getChildren().clear();
                        }

                        subMenu(subMenuTabs, subTabPanels, Integer.parseInt(menuid));

                        Sessions.getCurrent().setAttribute("menuid", menuid);
//                        String profid = user.getProfileId();
                        String pageUrl = "" + tb.getAttribute("action");
//                        arrmenubutton = bbsbs.GET_BUTTON_ID_BY_MENU(menuid);
                        String pageId = "" + tb.getAttribute("pageid");
                        tb.getDesktop().setAttribute("activewindow", pageId);
                        Map openedPages = (Map) tb.getDesktop().getAttribute("openedPages");
                        LinkedList sequence = (LinkedList) tb.getDesktop().getAttribute("sequence");
                        if ((openedPages.containsKey(pageId))) {
                            ICommonWindowInterface intf = (ICommonWindowInterface) tb.getDesktop().getPage(pageId).getFellow("win");
//                            intf.getCurrentWindow().setFocus(true);
                            sequence.remove(pageId);
                            sequence.addFirst(pageId);
                            Window win = (Window) tb.getDesktop().getPage("Main").getFellow("parent");
//                            controlToolBar(win, ((User) Sessions.getCurrent().getAttribute("user")).getProfileId() + "");
                            this.getDesktop().setAttribute("activewindow", pageId);
                        } else if (tb.getAttribute("action") != null) {
                            Include sub = (Include) tb.getDesktop().getPage("includedpages").getFellow(pageId);
                            sub.setSrc("");
                            sub.setSrc(pageUrl);
                            Sessions.getCurrent().setAttribute("menuid", menuid);
                            sequence.addFirst(pageId);
                            openedPages.put(pageId, tb.getLabel());
                            Window win = (Window) tb.getDesktop().getPage("Main").getFellow("parent");
                            if (win.getAttribute("MENUID") != null) {
//                                controlToolBar(win, ((User) Sessions.getCurrent().getAttribute("user")).getProfileId() + "");
                            }
                        }
                    }
                    break;
                case 2:
                    if (object.getClass().getName().equalsIgnoreCase("org.zkoss.zul.Tab")) {
                        Tab tb = (Tab) event.getTarget();
                        tabPanelTreeOnEvent(tb);
                    }
                    break;
                case 3:
                    Treecell tc = (Treecell) event.getTarget();
                    ArrayList arrmenubutton = new ArrayList();
//                    User user = (User) Sessions.getCurrent().getAttribute("user");
//                    ButtonStatusBS bbsbs = new ButtonStatusBS();
                    String menuid = "" + tc.getAttribute("MENUID");

                    Sessions.getCurrent().setAttribute("menuid", menuid);
//                    String profid = user.getProfileId();
                    String pageUrl = "" + tc.getAttribute("action");
//                    arrmenubutton = bbsbs.GET_BUTTON_ID_BY_MENU(menuid);
                    String pageId = "" + tc.getAttribute("pageid");
                    tc.getDesktop().setAttribute("activewindow", pageId);
                    Map openedPages = (Map) tc.getDesktop().getAttribute("openedPages");
                    LinkedList sequence = (LinkedList) tc.getDesktop().getAttribute("sequence");
                    if ((openedPages.containsKey(pageId))) {
                        ICommonWindowInterface intf = (ICommonWindowInterface) tc.getDesktop().getPage(pageId).getFellow("win");
//                        intf.getCurrentWindow().setFocus(true);
                        sequence.remove(pageId);
                        sequence.addFirst(pageId);
                        Window win = (Window) tc.getDesktop().getPage("Main").getFellow("parent");
//                        controlToolBar(win, ((User) Sessions.getCurrent().getAttribute("user")).getProfileId() + "");
                        this.getDesktop().setAttribute("activewindow", pageId);
                    } else if (tc.getAttribute("action") != null) {
                        Include sub = (Include) tc.getDesktop().getPage("includedpages").getFellow(pageId);
                        sub.setSrc("");
                        sub.setSrc(pageUrl);
                        Sessions.getCurrent().setAttribute("menuid", menuid);
                        sequence.addFirst(pageId);
                        openedPages.put(pageId, tc.getLabel());
                        Window win = (Window) tc.getDesktop().getPage("Main").getFellow("parent");
                        if (win.getAttribute("MENUID") != null) {
//                            controlToolBar(win, ((User) Sessions.getCurrent().getAttribute("user")).getProfileId() + "");
                        }
                    }
                    break;
                case 4:
                    Tab tb = (Tab) event.getTarget();
                    //tabPanelOnEvent(tb);
                    arrmenubutton = new ArrayList();
//                    user = (User) Sessions.getCurrent().getAttribute("user");
//                    bbsbs = new ButtonStatusBS();
                    menuid = "" + tb.getAttribute("MENUID");

                    Sessions.getCurrent().setAttribute("menuid", menuid);
//                    profid = user.getProfileId();
                    pageUrl = "" + tb.getAttribute("action");
//                    arrmenubutton = bbsbs.GET_BUTTON_ID_BY_MENU(menuid);
                    pageId = "" + tb.getAttribute("pageid");
                    tb.getDesktop().setAttribute("activewindow", pageId);
                    openedPages = (Map) tb.getDesktop().getAttribute("openedPages");
                    sequence = (LinkedList) tb.getDesktop().getAttribute("sequence");
                    if ((openedPages.containsKey(pageId))) {
                        ICommonWindowInterface intf = (ICommonWindowInterface) tb.getDesktop().getPage(pageId).getFellow("win");
//                        intf.getCurrentWindow().setFocus(true);
                        sequence.remove(pageId);
                        sequence.addFirst(pageId);
                        Window win = (Window) tb.getDesktop().getPage("Main").getFellow("parent");
//                        controlToolBar(win, ((User) Sessions.getCurrent().getAttribute("user")).getProfileId() + "");
                        this.getDesktop().setAttribute("activewindow", pageId);
                    } else if (tb.getAttribute("action") != null) {
                        Include sub = (Include) tb.getDesktop().getPage("includedpages").getFellow(pageId);
                        sub.setSrc("");
                        sub.setSrc(pageUrl);
                        Sessions.getCurrent().setAttribute("menuid", menuid);
                        sequence.addFirst(pageId);
                        openedPages.put(pageId, tb.getLabel());
                        Window win = (Window) tb.getDesktop().getPage("Main").getFellow("parent");
                        if (win.getAttribute("MENUID") != null) {
//                            controlToolBar(win, ((User) Sessions.getCurrent().getAttribute("user")).getProfileId() + "");
                        }
                    }
                    break;
            }
        }
    }

    //To display horizontal menu
    public void tabMenu(Tabs menutabs, Tabpanels tbps, int menuId) {
        //To display horizontal menu

//        User user = (User) Sessions.getCurrent().getAttribute("user");
//        String profid = user.getProfileId();
        Tab tab1 = null;
        Tabpanel tbp = null;
//        ValidateUserBs vUser = new ValidateUserBs();
//        ArrayList menuList = vUser.getMenuNodes(profid, menuId);
//        for (int i = 0; i < menuList.size(); i++) {
//            ParentMenu parentMenu = (ParentMenu) menuList.get(i);
//            tab1 = new Tab();
//            tbp = new Tabpanel();
//            if (i == 1) {
//                menuId = Integer.parseInt(parentMenu.getMenuId());
//            }
//            tab1.setLabel(parentMenu.getMenuName());
//            tab1.setAttribute("action", parentMenu.getMenuAction());
//            tab1.setAttribute("MENUID", parentMenu.getMenuId());
//            tab1.setAttribute("pageid", parentMenu.getShortName());
//            tab1.setAttribute("prop", "1");
//            tab1.addEventListener("onClick", this);
//            if (parentMenu.getMenuAction() == null) {
//                String img = parentMenu.getMenuIcon();
//                tab1.setImage(img);
//            } else {
//                tab1.setImage("/images/arrow-icon.png");
//            }
//            tab1.setParent(menutabs);
//            tbp.setWidth("100%");
//            tbp.setParent(tbps);
//        }
    //Tabbox tbox = (Tabbox) subMenuTabs.getParent();
    //subMenu(subMenuTabs, subTabPanels, Integer.parseInt(""+tbox.getSelectedTab().getAttribute("MENUID")));
    }

    //To display horizontal menu
    public void subMenu(Tabs menutabs, Tabpanels tbps, int menuId) {
        //To display horizontal menu

//        User user = (User) Sessions.getCurrent().getAttribute("user");
//        String profid = user.getProfileId();
//        Tab tab1 = null;
//        Tabpanel tbp = null;
//        ValidateUserBs vUser = new ValidateUserBs();
//        ArrayList menuList = vUser.getMenuNodes(profid, menuId);
//        for (int i = 0; i < menuList.size(); i++) {
//            ParentMenu parentMenu = (ParentMenu) menuList.get(i);
//            tab1 = new Tab();
//            tbp = new Tabpanel();
//            if (i == 1) {
//                menuId = Integer.parseInt(parentMenu.getMenuId());
//            }
//            tab1.setLabel(parentMenu.getMenuName());
//            tab1.setAttribute("action", parentMenu.getMenuAction());
//            tab1.setAttribute("MENUID", parentMenu.getMenuId());
//            tab1.setAttribute("pageid", parentMenu.getShortName());
//            if (parentMenu.getMenuAction() == null) {
//                String img = parentMenu.getMenuIcon();
//                tab1.setAttribute("prop", "2");
//                tab1.setImage(img);
//                tab1.setStyle("width:180px");
//            } else {
//                tab1.setImage("/images/arrow-icon.png");
//                tab1.setAttribute("prop", "4");
//                tab1.setStyle("width:180px");
//            }
//            tab1.addEventListener("onClick", this);
//            tab1.setParent(menutabs);
//            tbp.setWidth("100%");
//            tbp.setParent(tbps);
//        }
    }

    public void setMap() {
        Tabs menutabs = (Tabs) this.getFellow("tabsCharCodeMnt");
        Tabpanels tbps = (Tabpanels) this.getFellow("tabpanelsCharCodeMnt");

        subMenuTabs = (Tabs) this.getFellow("subMenuTab");
        subTabPanels = (Tabpanels) this.getFellow("subMenuPanel");

        tabMenu(menutabs, tbps, 0);

        Map openedPages = new HashMap();
        LinkedList sequence = new LinkedList();
        openedPages.put("xyz", "pxr");
        this.getDesktop().setAttribute("openedPages", openedPages);
        this.getDesktop().setAttribute("sequence", sequence);
        if (!openedPages.containsKey("ss")) {
            ;
        }

        tbTray=(Tabbox)this.getFellow("tbTray");
        tbsTray=(Tabs)this.getFellow("tbsTray");

    }

    public void exit() {

        //  Client openedPages
        Map Pages = ((Map) this.getDesktop().getAttribute("openedPages"));
        Boolean isValidData = true;
        getDesktop().setAttribute("validData", isValidData);

        Set col = Pages.keySet();
        Object[] array = col.toArray();


        for (int i = 0; i < array.length; i++) {
            String pageid = (String) array[i];
            if (!pageid.equals("xyz")) {
                Window win = (Window) this.getDesktop().getPage(pageid).getFellow("win");
                win.setFocus(true);

                ICommonWindowInterface com = (ICommonWindowInterface) this.getDesktop().getPage(pageid).getFellow("win");
                com.setControls();

                win.onClose();
                isValidData = (Boolean) getDesktop().getAttribute("validData");
                if (!isValidData) {
                    return;
                }

            }
        }
        if (isValidData) {
            Sessions.getCurrent().invalidate();
            Executions.sendRedirect(".."+Executions.getCurrent().getContextPath());
        }

    }
    //closes all opended windows -- added by tushar

    public boolean closeAllOpenedWindows() {
        //  Client openedPages
        boolean isStatus = true;
        Map Pages = ((Map) this.getDesktop().getAttribute("openedPages"));
        Boolean isValidData = true;
        getDesktop().setAttribute("validData", isValidData);
        System.out.println("Pages.size():" + Pages.size());
        if (Pages.size() > 1) {
            try {
                Messagebox.show("All windows will be closed, if any opened.", "Information", Messagebox.OK, Messagebox.INFORMATION);
                isValidData = (Boolean) getDesktop().getAttribute("validData");
                if (!isValidData) {
                    Set col = Pages.keySet();
                    Object[] array = col.toArray();
                    for (int i = 0; i < array.length; i++) {
                        String pageid = (String) array[i];
                        if (!pageid.equals("xyz")) {
                            Window win = (Window) this.getDesktop().getPage(pageid).getFellow("win");
                            win.setFocus(true);

                            ICommonWindowInterface com = (ICommonWindowInterface) this.getDesktop().getPage(pageid).getFellow("win");
                            com.setControls();

                            win.onClose();
                            isValidData = (Boolean) getDesktop().getAttribute("validData");
                            if (!isValidData) {
                                return false;
                            }

                        }
                        isStatus = true;
                    }
                    return isStatus;
                }
            //closeAllOpenedWindows();
            } catch (Exception e) {
            }
        }
        Set col = Pages.keySet();
        Object[] array = col.toArray();
        for (int i = 0; i < array.length; i++) {
            String pageid = (String) array[i];
            if (!pageid.equals("xyz")) {
                Window win = (Window) this.getDesktop().getPage(pageid).getFellow("win");
                win.setFocus(true);

                ICommonWindowInterface com = (ICommonWindowInterface) this.getDesktop().getPage(pageid).getFellow("win");
                com.setControls();

                win.onClose();
                isValidData = (Boolean) getDesktop().getAttribute("validData");
                if (!isValidData) {
                    return false;
                }
                isStatus = true;
            }
        }
        return isStatus;

    }

    public void controlToolBar(Component c1, String pro_id) {

//        String menu_id = (String) c1.getAttribute("MENUID");
//        ArrayList<UserButtonProfileDetail> buttondetails = b1.getUserButtonDetails(pro_id, menu_id);
//
//        Toolbarbutton newRow = (Toolbarbutton) this.getFellow("new");
//        Toolbarbutton delete = (Toolbarbutton) this.getFellow("delete");
//        Toolbarbutton retrieve = (Toolbarbutton) this.getFellow("retrieve");
//        Toolbarbutton save = (Toolbarbutton) this.getFellow("save");
//
//        newRow.setImage("/images/new_d.jpg");
//        delete.setImage("/images/delete_d.jpg");
//        retrieve.setImage("/images/retrieve_d.jpg");
//        save.setImage("/images/save_d.jpg");
//
//
//        newButton1 = Constants.DISABLED;
//        delete1 = Constants.DISABLED;
//        retrieve1 = Constants.ENABLED;
//        save1 = Constants.DISABLED;
//        firstRecord1 = Constants.ENABLED;
//        preRecord1 = Constants.ENABLED;
//        nextRecord1 = Constants.ENABLED;
//        lastRecord1 = Constants.ENABLED;
//        excel1 = Constants.ENABLED;
//        clear1 = Constants.ENABLED;
//
//
//
//        for (UserButtonProfileDetail userButtonProfileDetail : buttondetails) {
//            String buttonName = userButtonProfileDetail.getBUTTON_NAME();
//            if (buttonName.equalsIgnoreCase("ADD")) {
//                newRow.setDisabled(false);
//                newRow.setImage("/images/new_e.jpg");
//                newButton1 = Constants.ENABLED;
//                useAddStatus = true;
//            }
//            if (buttonName.equalsIgnoreCase("UPDATE")) {
//                save.setDisabled(false);
//                save.setImage("/images/save_e.jpg");
//                save1 = Constants.ENABLED;
//            }
//            if (buttonName.equalsIgnoreCase("DELETE")) {
//                delete.setDisabled(false);
//                delete.setImage("/images/delete_e.jpg");
//                delete1 = Constants.ENABLED;
//            }
//            if (buttonName.equalsIgnoreCase("Insert")) {
//                newRow.setDisabled(false);
//                newRow.setImage("/images/new_e.jpg");
//                newButton1 = Constants.ENABLED;
//            }
//            if (buttonName.equalsIgnoreCase("SAVE")) {
//                save.setDisabled(false);
//                save.setImage("/images/save_e.jpg");
//                save1 = Constants.ENABLED;
//            }
//
//
//            if (buttonName.equalsIgnoreCase("RETRIEVE")) {
//                retrieve.setDisabled(false);
//                retrieve.setImage("/images/retrieve_e.jpg");
//                retrieve1 = Constants.ENABLED;
//            }
//            Menuitem item = new Menuitem(buttonName);
//            item.addEventListener("onClick", this);
//            //item.setParent(popup);
//            item.setAttribute("buttonName", buttonName);
//
//        }
//        enableDisableButtons(newButton1, delete1, retrieve1, save1, firstRecord1, preRecord1, nextRecord1, lastRecord1, excel1, clear1);
    }

    public void resetcontrolToolBar() {
//        Toolbarbutton newRow = (Toolbarbutton) this.getFellow("new");
//        Toolbarbutton delete = (Toolbarbutton) this.getFellow("delete");
//        Toolbarbutton retrieve = (Toolbarbutton) this.getFellow("retrieve");
//        Toolbarbutton save = (Toolbarbutton) this.getFellow("save");
//        Toolbarbutton cancel = (Toolbarbutton) this.getFellow("cancel");
//
//        newRow.setImage("/images/new_d.jpg");
//        delete.setImage("/images/delete_d.jpg");
//        retrieve.setImage("/images/retrieve_d.jpg");
//        save.setImage("/images/save_d.jpg");
//        cancel.setImage("/images/cancel_e.jpg");
    }

    public void save(org.zkoss.zul.Menuitem save) throws Exception {

        String activewindow = (String) save.getDesktop().getAttribute("activewindow");
        if (activewindow != null) {
            ICommonWindowInterface intf = (ICommonWindowInterface) save.getDesktop().getPage(activewindow).getFellow("win");
            if (intf != null) {
                intf.saveRecord();
            }

        }
    }

    public void retrieve(org.zkoss.zul.Menuitem retrive) {
        String activewindow = (String) retrive.getDesktop().getAttribute("activewindow");
        if (activewindow != null) {
            ICommonWindowInterface intf = (ICommonWindowInterface) retrive.getDesktop().getPage(activewindow).getFellow("win");
            if (intf != null) {
                intf.retrieveRecord();
            }

        }

    }

    public void addRow(org.zkoss.zul.Menuitem addRow) throws Exception {
        

    }

    public void delete(org.zkoss.zul.Menuitem delete) throws Exception {
//        String activewindow = (String) delete.getDesktop().getAttribute("activewindow");
//        if (activewindow != null) {
//            org.maniks.slite.common.db.ICommonWindowInterface intf = (org.maniks.slite.common.db.ICommonWindowInterface) delete.getDesktop().getPage(activewindow).getFellow("win");
//            if (intf != null) {
//                intf.delete();
//            }
//
//        }

    }

    public void clear(org.zkoss.zul.Menuitem clear) {


        String activewindow = (String) clear.getDesktop().getAttribute("activewindow");
        if (activewindow != null) {
           ICommonWindowInterface intf = (ICommonWindowInterface) clear.getDesktop().getPage(activewindow).getFellow("win");
            if (intf != null) {
                intf.clearScreeen();
            }

        }

    }

    public void close(org.zkoss.zul.Menuitem close) {
        String activewindow = (String) close.getDesktop().getAttribute("activewindow");
        if (activewindow != null) {
            Page pg = close.getDesktop().getPage(activewindow);
            if (pg != null) {
                ICommonWindowInterface intf = (ICommonWindowInterface) close.getDesktop().getPage(activewindow).getFellow("win");
                if (intf != null) {
                    intf.closeScreen();
                }
                //System.out.println("close>>>>>" + close);
                // close.getDesktop().setAttribute("activewindow", null);
                resetcontrolToolBar();
            }
        }

    }

    public void swapping(KeyListener key) {
        java.util.LinkedList sequence = (LinkedList) this.getDesktop().getAttribute("sequence");
        if (sequence.size() != 0) {
            if (sequence.size() >= 2) {
                if (counter >= sequence.size()) {
                    counter = 1;
                }

                String activewin = (String) sequence.get(counter);
                sequence.remove(activewin);
                sequence.addFirst(activewin);
                Window win = (Window) this.getDesktop().getPage(activewin).getFellow("win");
                win.setFocus(true);
                ICommonWindowInterface com = (ICommonWindowInterface) this.getDesktop().getPage(activewin).getFellow("win");
                com.setControls();
                win.getDesktop().setAttribute("activewindow", activewin);
                counter++;
//                controlToolBar(win, ((User) Sessions.getCurrent().getAttribute("user")).getProfileId() + "");
            } else {
                String activewin = (String) sequence.get(0);
                Window win = (Window) this.getDesktop().getPage(activewin).getFellow("win");
                win.setFocus(true);
                win.getDesktop().setAttribute("activewindow", activewin);
            }
        }


    }

    public void enableDisableButtons(int newRow, int delete, int retrieve, int save, int firstRecord, int previos, int next, int last, int excel, int clear) {

//        Toolbarbutton newRow1 = (Toolbarbutton) this.getFellow("new");
//        Toolbarbutton delete1 = (Toolbarbutton) this.getFellow("delete");
//        Toolbarbutton retrieve1 = (Toolbarbutton) this.getFellow("retrieve");
//        Toolbarbutton save1 = (Toolbarbutton) this.getFellow("save");
//        Toolbarbutton firstR = (Toolbarbutton) this.getFellow("firstRec");
//        Toolbarbutton previousR = (Toolbarbutton) this.getFellow("pre_Recor");
//        Toolbarbutton nextR = (Toolbarbutton) this.getFellow("next_Record");
//        Toolbarbutton lastR = (Toolbarbutton) this.getFellow("last_Record");
//        Toolbarbutton excel1 = (Toolbarbutton) this.getFellow("excel");
//        Toolbarbutton cancel = (Toolbarbutton) this.getFellow("cancel");
//
//
//        controlButton(newRow1, newRow);
//
//        controlButton(delete1, delete);
//
//        controlButton(retrieve1, retrieve);
//
//        controlButton(save1, save);
//
//        controlButton(firstR, firstRecord);
//
//        controlButton(previousR, previos);
//
//        controlButton(nextR, next);
//
//        controlButton(lastR, last);
//
//        controlButton(excel1, excel);
//
//        controlButton(cancel, clear);
    }

    public void controlButton(Toolbarbutton button, int status) {
        String buttonId = button.getId();

//        switch (status) {
//
//            case Constants.DISABLED:
//                button.setImage("/images/" + buttonId + "_d.jpg");
//                button.setDisabled(true);
//                removeFromPopup(button);
//
//                break;
//
//            case Constants.ENABLED:
//                button.setImage("/images/" + buttonId + "_e.jpg");
//                button.setDisabled(false);
//                addToPopup(button);
//                break;
//
//            case Constants.CLICKED:
//                System.out.println("/images/" + buttonId + "_c.jpg");
//                button.setImage("/images/" + buttonId + "_c.jpg");
//                button.setDisabled(false);
//                addToPopup(button);
//                break;
//
//        }

    }

    public void removeFromPopup(Toolbarbutton button) {
    }

    public void addToPopup(Toolbarbutton button) {

        String buttonId = button.getId();
        if (buttonId.equalsIgnoreCase("new")) {
        }

        if (buttonId.equalsIgnoreCase("delete")) {
        }

        if (buttonId.equalsIgnoreCase("retrieve")) {
        }

        if (buttonId.equalsIgnoreCase("save")) {
        }

    }

    public void onMinimizeWindow(ForwardEvent event){
		         System.out.println("Window MinImized <><><> ");
		         Window minWindow=(Window)event.getOrigin().getTarget();
		         ///ArrayList arMinimizedList=new ArrayList();
		         //if(!arMinimizedList.contains(minWindow))
		         {

		            //arMinimizedList.add(minWindow);
		            removeOldTrayEntries(minWindow);
		            boolean isMinimize=minWindow.getAttribute("doMinimise")==null?true:(Boolean)minWindow.getAttribute("doMinimise");
		            if(isMinimize){
		            Tab tab=new Tab();
		            tab.setLabel(minWindow.getTitle());
		            //tab.setClosable(true);
		            tab.setAttribute("window",minWindow);
		             tab.addForward(Events.ON_CLICK,this,"onTabClicked");
		             tab.setParent(tbsTray);

		         }
		             else{
		                minWindow.setAttribute("doMinimise",true);
		            }
		         }
		         makeTrayVisible();
		     }

		    public void onTabClicked(ForwardEvent event){
		        Tab tab=(Tab)event.getOrigin().getTarget();
		        Window minWindow=(Window)tab.getAttribute("window");
		        minWindow.setAttribute("doMinimise",false);
		        //minWindow.setMaximized(true);
                        minWindow.setFocus(true);
		        minWindow.doOverlapped();
		        tab.setVisible(false);
		        //tbsTray.removeChild(tab);
		        makeTrayVisible();
		    }

		    private void makeTrayVisible(){

		        if(tbsTray.getChildren().size() > 0)
		             tbTray.setVisible(true);
		        else
		            tbTray.setVisible(false);
		    }

		    public void removeOldTrayEntries(Window childWin){
		        ArrayList<Tab> arMinimizedList=new ArrayList<Tab>();
		        for(int i=0;i<tbsTray.getChildren().size();i++){
		            Tab tab=(Tab)tbsTray.getChildren().get(i);
		            Window minWindow=(Window)tab.getAttribute("window");
		            if(childWin .equals(minWindow)){
		                arMinimizedList.add(tab);
		            }
		        }
		        for(Tab tab:arMinimizedList){
		              tbsTray.removeChild(tab);
		        }
    }
}