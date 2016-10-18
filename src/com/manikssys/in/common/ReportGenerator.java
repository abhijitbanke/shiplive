package com.manikssys.in.common;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JExcelApiExporterParameter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zkplus.hibernate.HibernateUtil;

/**
 * Author : Sandeep Natoo
 * Description : This is Common class utility for generating the jasper reports
 */

public class ReportGenerator
{

    // Method to generate the PDF format report, 1st parameter is file name for generating pdf report,
    // 2nd parameter is jasper file name by using we generate the report & 3rd parameter is parameters to pass jasper file
    public static AMedia generatePDF(String file_name ,String jasper_file,final Map params)
    {
        return generateReport(file_name, jasper_file,params, "pdf", "application/pdf", 1);
    }

    // Method to generate the Excel format report, 1st parameter is file name for generating pdf report,
    // 2nd parameter is jasper file name by using we generate the report & 3rd parameter is parameters to pass jasper file
    public static  AMedia generateExcel(String file_name ,String jasper_file,final Map params)
    {
        return generateReport(file_name, jasper_file,params, "xls", "application/vnd.ms-excel", 2);
    }

    public static AMedia generateXML(String file_name ,String jasper_file,final Map params)
    {
            return generateReport(file_name, jasper_file,params, "xml", "text/xml", 3);
    }

    // Method to generate the Excel format report, 1st parameter is file name for generating pdf report,
    // 2nd parameter is jasper file name by using we generate the report & 3rd parameter is parameters to pass jasper file
    public static  AMedia generateHTML(String file_name ,String jasper_file,final Map params)
    {
        return generateReport(file_name, jasper_file,params, "html", "application/html", 4);
    }

    public static AMedia generateODT(String file_name ,String jasper_file,final Map params)
    {
            return generateReport(file_name, jasper_file,params, "odt", "application/vnd.oasis.opendocument.text", 5);
    }

    public static  AMedia generateCSV(String file_name ,String jasper_file,final Map params)
    {
            return generateReport(file_name, jasper_file,params, "csv", "text/csv", 6);
    }

    public static AMedia generateRTF(String file_name ,String jasper_file,final Map params)
    {
            return generateReport(file_name, jasper_file,params, "rtf", "application/rtf", 8);
    }



    private static AMedia generateReport(String file_name ,String jasper_file,final Map params,String fileExtension,String contentType,int iReportType){
        AMedia aMedia =null;
        try
        {
            aMedia =   new AMedia(file_name, fileExtension, contentType, generateReport(jasper_file,params,iReportType).toByteArray());
        } catch (Exception e) {e.printStackTrace();}
        return aMedia;
    }

   // Method to compile the jrxml file & make the jasper file,first parameter takes jrxml file path & second parameter is jasper file path
    private static void compileJRXML(String jrxmlFile,String jasperFile) throws JRException {
        try
        {
            final Execution exec = Executions.getCurrent();
            JasperCompileManager.compileReportToFile(exec.getDesktop().getWebApp().getRealPath(jrxmlFile), exec.getDesktop().getWebApp().getRealPath(jasperFile));
        } catch (Exception e) {e.printStackTrace();}
    }

    // Method to generate the jrxml file name from jasper file name
    private static String getJRXMLFileName(String jasper_file)
    {
        String jrxml_file="";
        try
        {
            StringTokenizer st=new StringTokenizer(jasper_file,".");
            jrxml_file=st.nextToken();
            jrxml_file=jrxml_file+".jrxml";
        } catch (Exception e) {e.printStackTrace();}
        return jrxml_file;
    }

    // Method to generate the ByteArrayOutputStream for the specific type of report,
    // its having 3 parameters jasper file name, parameters & report type(PDF,Excel etc.)
    private static ByteArrayOutputStream generateReport(String jasper_file,final Map params,int iReportType)
    {
        Connection connection= null;
        InputStream inputStream = null;
        ByteArrayOutputStream arrayOutputStream = null;
        try
        {
            arrayOutputStream = new ByteArrayOutputStream();
            //is = Thread.currentThread().getContextClassLoader().getResourceAsStream(jasper_file);
            final Execution exec = Executions.getCurrent();
            inputStream = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI(jasper_file, false));
            if(inputStream == null) // if jasper doesn't exist then going for compilation
            {
                String jrxml_file=getJRXMLFileName(jasper_file);
                compileJRXML(jrxml_file,jasper_file); // Call for compile the jrxml file
                int iCount=0;
                while(inputStream == null) // pick the stream until not get the jasper file
                {
                    inputStream = exec.getDesktop().getWebApp().getResourceAsStream(exec.toAbsoluteURI(jasper_file, false));
                    if(iCount++==10) // up to 10 rounds not get the jasper file then exit the loop
                        break;
                }
            }

            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
            Session session = sessionFactory.openSession();
            SessionImpl sessionImpl = (SessionImpl) session;
            connection = sessionImpl.connection();
            
            JasperPrint print= JasperFillManager.fillReport(inputStream,params,connection);
            JRExporter exporter = null;

            switch(iReportType)
            {
                case 1 :  // The PDF format
                    exporter=new JRPdfExporter();
                    break;

                case 2 : // The Microsoft Excel format with JExcelApi
                    //exporter=new JRXlsExporter();
                    exporter=new JExcelApiExporter();
                    //exporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.TRUE);
                    //exporter.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);

                    exporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporter.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporter.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporter.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    break;

                case 3 :  // The XML format
                    exporter=new JRXmlExporter();
                    break;

                case 4 : // The HTML format
                    exporter=new JRHtmlExporter();
                    // set IMAGES_MAP parameter to prepare get backward IMAGE_MAP parameter
                    exporter.setParameter(JRHtmlExporterParameter.IMAGES_MAP, new HashMap());
                    exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, params.get("image"));
                    break;

                case 5 :  // The OpenOffice Writer format
                    exporter=new JROdtExporter();
                    break;

                case 6 :  // The CSV (Comma-Separated Values) format
                    exporter=new JRCsvExporter();
                    break;

                case 7 :  // Microsoft Excel format generated with  Apache POI 
                    exporter=new JRXlsExporter();
                    exporter.setParameter(JExcelApiExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporter.setParameter(JExcelApiExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
                    exporter.setParameter(JExcelApiExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporter.setParameter(JExcelApiExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    break;

                case 8 :  // The RTF(Rich Text Format)
                    exporter=new JRRtfExporter();
                    break;

                default : // The PDF format
                    exporter=new JRPdfExporter();
            }

            exporter.setParameter(JRExporterParameter.JASPER_PRINT,	print);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, arrayOutputStream);
            exporter.exportReport();
            arrayOutputStream.close();
            session.close();
        } catch (Exception e) {e.printStackTrace();}
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                    connection.close();
                }catch(Exception e){e.printStackTrace();}
            }
        }
        return arrayOutputStream;
    }
}