package com.manikssys.in.common;

/**
 * User: sandeep
 * Date: Aug 18, 2010
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class SMSSender
{
    boolean debug=true;
    String[] phone;
    String message	="";
    String ppgHost	="http://luna.a2wi.co.in:7501/failsafe/HttpLink";
    //String ppgHost	="http://luna.a2wi.co.in:7501/failsafe/HttpLink?aid=400024&pin=Man12&mnumber=919766038922&message=Hi+chetya!!&signature=MANIKS";
    //String aid="400024";  // Constant
    //String pin="Man12";   // Constant
    String signature="MANIKS";  // Constant
    String userName ="400024";  // Constant
    String password ="Man12";   // Constant


    public String send(String msgText, String[] phone){
        init(msgText,phone);
        String retVal=manipulateSMS();
        return retVal;
    }

    private void init(String msgText, String[] phone){
        message=msgText;
        this.phone=phone;
    }

    private void encodePhone(){
        try
        {
            if(phone != null){

                for(String number:phone){
                    if(number != null && number.length()>10){
                        number=URLEncoder.encode(number, "UTF-8");
                        if(debug)
                            System.out.println("phone------>"+number);
                    }
                }
            }
        }
        catch (UnsupportedEncodingException e)
        {
            System.out.println("Encoding to number not supported");
        }
    }

    private void encodeMessage(){
        try
        {
            if(message != null){

                message=URLEncoder.encode(message, "UTF-8");
                if(debug)
                    System.out.println("message------>"+message);

            }
        }
        catch (UnsupportedEncodingException e)
        {
            System.out.println("Encoding to message not supported");
        }
    }

    private String getURL(String phone){

        String url=ppgHost+"?aid="+userName+"&pin="+password+"&mnumber="+phone+"&message="+message+"&signature="+signature;
        if(debug)
            System.out.println("url string->"+url);
        return url;
    }

    private String sendSMS(String strUrl){

        StringBuffer strReturn=new StringBuffer();

        try
        {
            URL url2=new URL(strUrl);

            HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);

            if(debug)
                System.out.println("Opened Con->"+connection);

            String res=connection.getResponseMessage();

            if(debug)
                System.out.println("Get Resp  ->"+res);

            int code = connection.getResponseCode () ;

            if ( code == HttpURLConnection.HTTP_OK )
            {
                //Get response data.
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String str;
                String space="\r\n";
                while( null != ((str = in.readLine()))) {
                    /*if (str.startsWith("MessageID=")) */{
                        strReturn.append(str);
                        strReturn.append(space);
                        //System.out.println(str);
                    }
                }

                connection.disconnect() ;
            }
        }
        catch(IOException e)
        {
            System.out.println("unable to create new url"+e.getMessage());
        }
        return strReturn.toString();
    }

    private String manipulateSMS(){

        StringBuffer returnVal=new StringBuffer();
        String space="\n\n";
        encodePhone();
        encodeMessage();
        for(String number:phone){
            returnVal.append(sendSMS(getURL(number)));
            returnVal.append(space);
        }
        return returnVal.toString();
    }


    /*public static void main(String args[])
    {
        boolean debug=true;

        String phone	="";
        String message	="";
        String ppgHost	="http://luna.a2wi.co.in:7501/failsafe/HttpLink";
        //String ppgHost	="http://luna.a2wi.co.in:7501/failsafe/HttpLink?aid=400024&pin=Man12&mnumber=919766038922&message=Hi+chetya!!&signature=MANIKS";
        String aid="400024";  // Constant
        String pin="Man12";   // Constant
        String signature="MANIKS";  // Constant
        String username ="";
        String password ="";

        try
        {
            phone=URLEncoder.encode("919766038922", "UTF-8");

            if(debug)
                System.out.println("phone------>"+phone);
            message=URLEncoder.encode("SendMsg via Web", "UTF-8");

            if(debug)
                System.out.println("message---->"+message);
        }
        catch (UnsupportedEncodingException e)
        {
            System.out.println("Encoding not supported");
        }

        String url_str=ppgHost+"?aid="+aid+"&pin="+pin+"&mnumber="+phone+"&message="+message+"&signature="+signature;
        //String url_str=ppgHost;
        // String url_str=ppgHost+"?user="+username+"&password="+password+"&PhoneNumber="+phone+"&Text="+message;

        if(debug)
            System.out.println("url string->"+url_str);


        try
        {
            URL url2=new URL(url_str);

            HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
            connection.setDoOutput(false);
            connection.setDoInput(true);

            if(debug)
                System.out.println("Opened Con->"+connection);

            String res=connection.getResponseMessage();

            if(debug)
                System.out.println("Get Resp  ->"+res);

            int code = connection.getResponseCode () ;

            if ( code == HttpURLConnection.HTTP_OK )
            {
                //Get response data.
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String str,returnstring="";

                while( null != ((str = in.readLine()))) {
                    if (str.startsWith("MessageID=")) {
                        returnstring = returnstring + str + "\r\n";
                        System.out.println(str);
                    }
                }

                connection.disconnect() ;
            }
        }
        catch(IOException e)
        {
            System.out.println("unable to create new url"+e.getMessage());
        }

    } // main*/

    public static void main(String[] args) {
        String msg="This is test message from USIT web";
        //String[] phone={"919049650865","919766038922","919325171493","919822637770","919970174480","919860276868","919890793558","919766656409","919011069094","919372133409","919822732052"};
        String[] phone={"919921498275"};
        SMSSender sender=new SMSSender();
        String msgStat=sender.send(msg,phone);
        System.out.println("msg Status "+msgStat);
    }

} // class

