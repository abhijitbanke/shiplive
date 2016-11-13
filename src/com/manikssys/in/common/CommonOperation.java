/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.common;

/**
 *
 * @author sandeep
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.StaleObjectStateException;
import org.hibernate.Transaction;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Detail;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Window;

import com.manikssys.in.util.HibernateUtil;


public class CommonOperation {

    public static void copyFolder(String srcUrl, String destUrl) {


        File src = new File(srcUrl);
        File dst = new File(destUrl);
        try {
            copyDirectory(src, dst);
        } catch (IOException ex) {
            Logger.getLogger(CommonOperation.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    public static String copyFile(File inputFile, File outputFile) {

        String a = "";
        try {
            FileReader in = new FileReader(inputFile);
            FileWriter out = new FileWriter(outputFile);
            int c;

            while ((c = in.read()) != -1) {
                out.write(c);

            }
            in.close();
            out.close();
        } catch (Exception e) {
        }
        return a;
    }

    public static void copyDirectory(File srcPath, File dstPath)
            throws IOException {

        if (srcPath.isDirectory()) {

            if (!dstPath.exists()) {

                dstPath.mkdir();

            }


            String files[] = srcPath.list();

            for (int i = 0; i < files.length; i++) {
                copyDirectory(new File(srcPath, files[i]),
                        new File(dstPath, files[i]));

            }

        } else {

            if (!srcPath.exists()) {

                System.out.println("File or directory does not exist.");

                System.exit(0);

            } else {

                InputStream in = new FileInputStream(srcPath);
                OutputStream out = new FileOutputStream(dstPath);
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];

                int len;

                while ((len = in.read(buf)) > 0) {

                    out.write(buf, 0, len);

                }

                in.close();

                out.close();

            }

        }



    }

    public static void showDashboardMessage(String Message, char cState) {
        Page dashPage = Executions.getCurrent().getDesktop().getPageIfAny("dashboard");

        if (dashPage != null) {
            Window window = (Window) dashPage.getFellowIfAny("dashboardWin");
            if (window != null) {
                Label gloablmessage = (Label) window.getFellow("gloablmessage");
                if (gloablmessage != null) {
                    if (cState == 'S') // if success message
                    {
                        gloablmessage.setStyle("color:#336600;font-size:12px;font-weight:bold;float:right;");
                    } else // if failure message
                    {
                        gloablmessage.setStyle("color:#FF3333;font-size:12px;font-weight:bold;float:right;");
                    }
                    gloablmessage.setValue(Message);
                }
            }



        }

    }
    // Method to call begin transaction for hibernate session

    public static Transaction beginTransaction() {
        //  This code commented temporary
    	Transaction tx = null;
        try {
			tx = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("BEGIN TRANSACTION>>>>>>>>>>>>>>>>>>>>>>>");
        return tx;
    }

    // method to commit or close the hibernate session
    public static boolean commitTransaction(String key) {

        boolean blnStatus = true;   // Default true
        /* This code commented temporary*/
        try {
        	HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
            if (key != null) { // If message needed
                showDashboardMessage(Labels.getLabel(key), 'S');
            }
        } catch (Exception ex) {
            blnStatus = false; // if having exception
            if (ex instanceof StaleObjectStateException) {
                showDashboardMessage(Labels.getLabel("common.validation.versionidproblem"), 'F');
            }
            try {
                if (HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
                    System.out.println("Trying to rollback database transaction after exception:" + ex);
                    showDashboardMessage(Labels.getLabel("common.validation.errorwhileperform"), 'F');
                    HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
                }
            } catch (Throwable rbEx) {
                System.out.println("Could not rollback transaction after exception! Original Exception:\n" + rbEx);
            }

        } finally {
        	HibernateUtil.getSessionFactory().getCurrentSession().close();
        }
        //System.out.println("COMMIT TRANSACTION>>>>>>>>>>>>>>>>>>>>>>>");
        return blnStatus;
    }

    // Overloading
    public static boolean commitTransaction() {
        return commitTransaction(null); // Call the main method
    }

    /*  Method to fill the combobox with the specified list
    1) is the list of beans 2) property name for label & value 3) the combobox instance
    This method having facilities like setting attribute, seperator, multivalues etc.
    Map values :
    label = set the bean property name
    value = set bean property name or "" for whole bean
    seperator = set to Expression(-,_& etc.) for label having multiple values with concatination else blank
    attribute = set bean property name
    select = set to 'no' if no need for combo item with 'Select'
     */
    public static void comboFill(ArrayList arList, HashMap propMap, Combobox cmbBox) {
        try {

            Class parmTypes[] = {};    // Declare class array
            String charAt = "", newCharAt = "";

            String seperator = propMap.containsKey("seperator") == true ? (String) propMap.get("seperator") : "";  // Get the seperator if any
            String attribute = propMap.containsKey("attribute") == true ? (String) propMap.get("attribute") : "";  // Get the attribute if any

            String label = (String) propMap.get("label");
            String labels[] = label.split(",");
            String value = (String) propMap.get("value");
            if (!value.equals("")) {  // Check wheather value is having any property or not
                charAt = "" + value.charAt(0);
                newCharAt = charAt.toUpperCase();
                value = value.replaceFirst(charAt, newCharAt);
                value = "get" + value;
            }

            // Set the first item as 'Select'
            if (propMap.containsKey("select") == false ? true : propMap.get("select").equals("no") ? false : true) {
                Comboitem itemSelect = new Comboitem();
                itemSelect.setLabel("Select");
                itemSelect.setParent(cmbBox);
            }
            //System.out.println("arList size > "+arList.size());
            for (Object obj : arList) {
                Comboitem cmbItem = new Comboitem();
                Class clss = obj.getClass(); // get the class
                Method method = null;
                String comboLabel = "";

                // Label created with multiple properties
                for (int i = 0; i < labels.length; i++) {
                    charAt = "" + labels[i].charAt(0);
                    newCharAt = charAt.toUpperCase();
                    label = labels[i].replaceFirst(charAt, newCharAt);
                    label = "get" + label;

                    method = clss.getMethod(label, parmTypes); // get method
                    comboLabel = comboLabel + seperator + method.invoke(obj) == null ? "" : "" + method.invoke(obj); // Invoke the method
                    //System.out.println("method > "+method+" >> comboLabel > "+comboLabel);
                }
                cmbItem.setLabel(comboLabel);

                if (!value.equals("")) {  // Check wheather value is having any property or not
                    method = clss.getMethod(value, parmTypes); // get method
                    Object comboValue = method.invoke(obj) == null ? null : method.invoke(obj);   // Invoke the method
                    cmbItem.setValue(comboValue);
                } else // if not then set object bean as value
                {
                    cmbItem.setValue(obj);
                }

                if (!attribute.equals("")) {  // Check wheather value is having any attribute or not
                    method = clss.getMethod(attribute, parmTypes); // get method
                    Object attribObj = method.invoke(obj) == null ? null : method.invoke(obj);   // Invoke the method
                    cmbItem.setAttribute(attribute, attribObj);
                }

                cmbItem.setParent(cmbBox);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // Method to set the specified value of the combobox
    public static void ComboSelected(Combobox cmbBox, String cmbProperty, Object obj, String objProperty) {
        try {
            String charAt = "", newCharAt = "";
            Class parmTypes[] = {};    // Declare class array
            Class clss = null;
            Method method = null;

            if (!cmbProperty.equals("")) {  // Check wheather value is having any property or not
                charAt = "" + cmbProperty.charAt(0);
                newCharAt = charAt.toUpperCase();
                cmbProperty = cmbProperty.replaceFirst(charAt, newCharAt);
                cmbProperty = "get" + cmbProperty;
            }

            if (!objProperty.equals("")) {  // Check wheather value is having any property or not
                charAt = "" + objProperty.charAt(0);
                newCharAt = charAt.toUpperCase();
                objProperty = objProperty.replaceFirst(charAt, newCharAt);
                objProperty = "get" + objProperty;
                clss = obj.getClass(); // get the class
                method = clss.getMethod(objProperty, parmTypes); // get method
                obj = method.invoke(obj);
            }

            for (int i = 0; i < cmbBox.getChildren().size(); i++) {
                Comboitem cmbItem = (Comboitem) cmbBox.getChildren().get(i);
                Object cmbObj = cmbItem.getValue();
                if (!cmbProperty.equals("") && cmbObj != null) {
                    clss = cmbObj.getClass(); // get the class
                    method = clss.getMethod(cmbProperty, parmTypes); // get method
                    cmbObj = method.invoke(cmbObj);
                }

                if ((cmbObj != null) && (cmbObj.equals(obj))) {
                    cmbBox.setSelectedItem(cmbItem);
                    break;
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This method will be used in grid where we have grid with detail componenet
    //to open or close all the detail comonenets on the opened page
    public static void onCheckAllOfDetailGrid(Checkbox checkbox, Grid grid) {



        int start = grid.getActivePage() * grid.getPageSize();
        int end = start + (grid.getPageSize() - 1);
        //    System.out.println("Start"+start +" end"+end+" grid.getPageSize()"+grid.getPageSize());
        if (grid.getRows().getChildren().size() < end) {
            end = grid.getRows().getChildren().size() - 1;
        }
        for (int i = start; i <= end; i++) {
            if (((Row) grid.getRows().getChildren().get(i)).getChildren().get(0) instanceof Detail) {
                Detail detail = (Detail) ((Row) grid.getRows().getChildren().get(i)).getChildren().get(0);
                detail.setOpen(checkbox.isChecked());

                Event event = new Event(Events.ON_OPEN, detail);
                Events.sendEvent(event);
            }
        }

    }

    //This method will be called when  where we have grid with detail componenet
    // check or unckeck  closeall   checkbox
    public static void onPagingOfDetailGrid(Checkbox checkbox, Grid grid) {
        int start = grid.getActivePage() * grid.getPageSize();
        int end = start + (grid.getPageSize() - 1);
        //    System.out.println("Start"+start +" end"+end+" grid.getPageSize()"+grid.getPageSize());
        boolean isClosed = false;
        if (grid.getRows().getChildren().size() < end) {
            end = grid.getRows().getChildren().size() - 1;
        }
        for (int i = start; i <= end; i++) {
            if (((Row) grid.getRows().getChildren().get(i)).getChildren().get(0) instanceof Detail) {
                Detail detail = (Detail) ((Row) grid.getRows().getChildren().get(i)).getChildren().get(0);
                if (!detail.isOpen()) {
                    isClosed = true;
                    break;
                }

            }
        }
        checkbox.setChecked(!isClosed);

    }


}
