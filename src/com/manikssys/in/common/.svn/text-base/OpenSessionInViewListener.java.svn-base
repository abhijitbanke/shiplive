/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.common;

/**
 *
 * @author sandeep
 */

import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.ExecutionInit;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class OpenSessionInViewListener implements ExecutionInit {

    private static final Log log = Log.lookup(OpenSessionInViewListener.class);

    //-- ExecutionInit --//
    public void init(Execution exec, Execution parent) {
        if (parent == null) { //the root execution of a servlet request
            Page dashPage = Executions.getCurrent().getDesktop().getPageIfAny("dashboard");

            if (dashPage != null) {

                Window window = (Window) dashPage.getFellowIfAny("dashboardWin");

                if (window != null) {


                    Label gloablmessage = (Label) window.getFellowIfAny("gloablmessage");

                    if (gloablmessage != null) {
                        gloablmessage.setValue("");
                    }
                }



            }


            // HibernateUtil.currentSession().beginTransaction();
        }
    }
}

