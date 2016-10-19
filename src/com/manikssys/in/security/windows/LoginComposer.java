/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.security.windows;

import com.manikssys.in.common.CommonOperation;
import com.manikssys.in.common.ComponentValidation;
import com.manikssys.in.security.beans.ScrUserMaster;
import com.manikssys.in.security.business.ILoginBs;
import com.manikssys.in.security.business.LoginBs;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import java.util.ArrayList;

/**
 *
 * @author sandeep
 */
public class LoginComposer extends GenericForwardComposer {

    private Textbox uname;
    private Textbox password;
    private Textbox macId;
    private Button submit;
    private Label errmsg;
    private static Logger log = Logger.getLogger(LoginComposer.class);

    public void onCreate$loginIndex() {
        // Clients.evalJavaScript("getMACAddresses("+macId+")");
    }

    public void onChange$uname(Event event) {
        resetTextbox(uname);
        errmsg.setValue("");
    }

    public void onChange$password(Event event) {
        resetTextbox(password);
        errmsg.setValue("");
    }

    public void onOK$password(Event event) {
        try {
            validateUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick$submit(Event event) {
        try {
            validateUser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validateUser() {

        if (validatedFileds()) {

            String macAddresses = (String) sessionScope.get("MAC");
            System.out.println("UName > " + uname.getText() + " PASS> " + password.getText() + " MAC> " + macAddresses);
//            System.out.println("IP "+);
           
//                macAddresses = macAddresses == null ? "" : macAddresses;                //commented by abhijit
            
                macAddresses = macAddresses == null ? "EXAMPLE" : macAddresses;       //Created by Abhijit
                

            String errorMsg = "";
            if (macAddresses.length() > 1) { // Even if it contains no MAC id it returns ';' character so we check its length having more than 1

                CommonOperation.beginTransaction(); // Here Session transaction begins
                ILoginBs valLogObj = new LoginBs();

                // Validate the client's MAC Id
                boolean macStatus = false;
                
//                    macStatus = valLogObj.validateMAC(macAddresses);            //commented by abhijit
                
                    macStatus = true;                                         //Created by Abhijit

                System.out.println("MAC STATUS > " + macStatus);
                if (macStatus) {

                    // if fileds are valid
                    ScrUserMaster userObj = new ScrUserMaster();
                    userObj.setUserName(uname.getText());
                    userObj.setUserPassword(password.getText());
                    // System.out.println("password.getText()"+password.getText());

                    ArrayList<ScrUserMaster> userList = valLogObj.validateUser(userObj);

                    if (userList.size() > 0) {
                        // valid user
                        userObj = userList.get(0);

                        // if valid user save the user object into the session

                        log.info("Logged User is -" + userObj.getUserName());

                        //  System.out.println("the status of user is---" + userObj.getStatus());
                        // if status is 0(Inactive) then show the error message otherwise redirect
                        if (userObj.getStatus().equals("0")) {
                            errorMsg = "app.index.error.inactive.user"; // If Inactive User
                        } else {
                            Executions.sendRedirect("/dashboard.zul");
                            Sessions.getCurrent().setAttribute("user", userObj);
                        }

                    } else {
                        errorMsg = "app.index.error.invalid.user"; // If Invalid User or Password
                    }
                } else {
                    errorMsg = "app.index.error.invalid.mac"; // If Invalid MAC Address
                }
            } else {
                errorMsg = "app.index.error.invalid.jre"; // If JRE or NIC not Found
            }
            password.setValue("");
            uname.setFocus(true);
            errmsg.setValue(org.zkoss.util.resource.Labels.getLabel(errorMsg));

        }

        CommonOperation.commitTransaction(); // Here Sesssion transaction ends .If u r not having msg then just call commitTransaction();
    }

    public boolean validatedFileds() {
        String mandatoryMsg = org.zkoss.util.resource.Labels.getLabel("app.comman.err.mandatory");

        WrongValueException wvexpObj;

        wvexpObj = ComponentValidation.validateManditoryTextbox(uname, mandatoryMsg);
        if (wvexpObj != null) {
            ComponentValidation.showErrorMessage(wvexpObj);
            return false;
        }
        wvexpObj = ComponentValidation.validateManditoryTextbox(password, mandatoryMsg);
        if (wvexpObj != null) {
            ComponentValidation.showErrorMessage(wvexpObj);
            return false;
        }
        return true;
    }

    public void resetTextbox(Textbox txt) {

        ComponentValidation.removeErrorMessage(txt);
    }
}
