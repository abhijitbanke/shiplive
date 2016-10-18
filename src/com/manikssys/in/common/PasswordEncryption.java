/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.common;

/**
 *
 * @author sandeep
 */

import com.manikssys.in.security.beans.ScrUserMaster;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

public class PasswordEncryption {

    public static void encrypt(ScrUserMaster userObj) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA"); // applying SHA Algorithm

            md.update(userObj.getUserPassword().getBytes("UTF-8")); //Convert into byte
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte raw[] = md.digest(); // get encrypted value
        userObj.setUserPassword((new BASE64Encoder()).encode(raw)); // get the digested password value
    }
}
