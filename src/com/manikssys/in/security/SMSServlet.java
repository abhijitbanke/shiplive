/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.security;

/**
 *
 * @author pc
 */
import java.io.*;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;

public class SMSServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            System.out.println("_____________DeamonThread for SMS Started______________");
            super.init(config);
            String initial = config.getInitParameter("initial");
            doThreadTask();
            try {
            } catch (NumberFormatException e) {
            }
        } catch (IOException ex) {
            Logger.getLogger(SMSServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doThreadTask()
            throws ServletException, IOException {
        System.out.println("________________in to the doget methode_____________________");
        Timer timer = new Timer();

//        timer.scheduleAtFixedRate(new Task(), 0, 60000);

    }
}
