/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.manikssys.in.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CallSmscApi {

    public CallSmscApi() {
    }
    /*
    1	Create a URL.
    2	Retrieve the URLConnection object.
    3	Set output capability on the URLConnection.
    4	Open a connection to the resource.
    5	Get an output stream from the connection.
    6	Write to the output stream.
    7	Close the output stream.
     */

    public void sendSMS(String mobilenumber, String message) throws Exception {

        String retval = "";
        int feedId = 322263;
        String userName = "9833175475";
        String passwd = "admmw";
        String sid = "api_demo";
        String mtype = "N";
        String DR = "Y";

        String data = "http://bulkpush.mytoday.com/BulkSms/SingleMsgApi?feedid=" + feedId + "&username=" + URLEncoder.encode(userName, "UTF-8") + "&password=" + passwd + "&To=" + mobilenumber + "&Text=" + URLEncoder.encode(message, "UTF-8") + "&senderid=" + sid;
        System.out.println("data      === " + data);
        URL url = new URL(data);
        HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
        urlconnection.setRequestMethod("POST");
        urlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlconnection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(urlconnection.getOutputStream());
        out.write(data);
        out.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
        String decodedString;
        while ((decodedString = in.readLine()) != null) {
            retval += decodedString;
        }
        in.close();
        System.out.println(retval);
    }
}
