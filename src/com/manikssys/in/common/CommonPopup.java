/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.common;

import java.sql.ResultSetMetaData;
//import db.ConnectionManager;
//import db.QueryParam;
//import db.QueryProcessor;
import java.sql.Connection;
import java.util.*;
import javax.sql.rowset.CachedRowSet;
//import org.maniks.slite.common.beans.CommonGetterandSetter;




/**
 *
 * @author nishesh
 */
public class CommonPopup {

    /**
     *
     * @param queryname Name of query to execute
     * @param columnName Column name for filter condition
     * @param param value of field to filter
     * @param paramMap Map of key values for query
     * @return Result with column and rows arraylist
     */
    public Map getPopupQueryDetails(String queryname,String columnName, String param, Map paramMap){
        ArrayList arrlist=new ArrayList();
        ArrayList arrcol=new ArrayList();
        ArrayList arrcol1=new ArrayList();
        ResultSetMetaData rsm=null;
        Connection con=null;
        CachedRowSet crstype;
        Map map=new HashMap();

//        try{
//            ArrayList quaryParamArray = new ArrayList();
////            QueryParam field1 = new QueryParam();
//            field1.setId(columnName);
//            field1.setValue("" + param);
//            field1.setType("String");
//            quaryParamArray.add(field1);
//            con = ConnectionManager.getInstance().getConnection();
//            // This if statement is use to execute the query which has more than two where clause
//            if("1".equals(paramMap.get("lov_check"))){
//                System.out.println("in if==");
//                crstype = QueryProcessor.executeQuery(queryname,paramMap,con);
//            }else{
//                System.out.println("iin else of common popup");
//                crstype = QueryProcessor.executeRetrieveStatement(queryname, quaryParamArray, paramMap, con);
//            }
//            CommonGetterandSetter comgetandsetcol=new CommonGetterandSetter();
//            if(crstype != null)
//            {
//                crstype.beforeFirst();
//                rsm=crstype.getMetaData();
//                for(int j=1;j<=rsm.getColumnCount();j++){
//                  arrcol.add(rsm.getColumnName(j));
//
//                }
//
//                comgetandsetcol.setHeader(arrcol);
//                arrcol1.add(comgetandsetcol);
//                ////System.out.println("arrcol1111111>>>>>>>>>>>>>"+arrcol.size());
//                //crstype.beforeFirst();
//
//                // Add result to Parent & Child  Menu beans
//                while(crstype.next())
//                {
//                    CommonGetterandSetter comgetandset=new CommonGetterandSetter();
//                    for(int j=1;j<=rsm.getColumnCount();j++){
//                        ////System.out.println("crstype.getString(j)"+j+"---"+crstype.getString(j));
//                        comgetandset.setColValue(j,crstype.getString(j));
//                    }
//                    arrlist.add(comgetandset);
//                }
//
//                //System.out.println("arrcol1=============>"+arrcol1.size());
//                //System.out.println("arrlist=============>"+arrlist.size());
//
//                map.put("Header",arrcol1);
//                map.put("Column",arrlist);
//            }else{
//                //System.out.println("CRS is null.....!!");
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally{
//            try{
//                con.close();
//            }catch(Exception e){}
//        }
        return map;
    }

    public Map getPopupDet(String queryname,String param){
        ArrayList arrlist=new ArrayList();
        ArrayList arrcol=new ArrayList();
        ArrayList arrcol1=new ArrayList();
        ResultSetMetaData rsm=null;
        Connection con=null;
        CachedRowSet crstype;
        Map map=new HashMap();
//        try{
//
//            con = ConnectionManager.getInstance().getConnection();
//            crstype = QueryProcessor.executeQuery(queryname,param,con);
//            CommonGetterandSetter comgetandsetcol=new CommonGetterandSetter();
//            if(crstype != null)
//            {
//                crstype.beforeFirst();
//                rsm=crstype.getMetaData();
//                ////System.out.println("column count>>>>>>>>>>>>>"+rsm.getColumnCount());
//                for(int j=1;j<=rsm.getColumnCount();j++){
//                  arrcol.add(rsm.getColumnName(j));
//
//                }
//
//                comgetandsetcol.setHeader(arrcol);
//                arrcol1.add(comgetandsetcol);
//                ////System.out.println("arrcol1111111>>>>>>>>>>>>>"+arrcol.size());
//                //crstype.beforeFirst();
//
//                // Add result to Parent & Child  Menu beans
//                while(crstype.next())
//                {
//                    CommonGetterandSetter comgetandset=new CommonGetterandSetter();
//                    for(int j=1;j<=rsm.getColumnCount();j++){
//                        ////System.out.println("crstype.getString(j)"+j+"---"+crstype.getString(j));
//                        comgetandset.setColValue(j,crstype.getString(j));
//                    }
//                    arrlist.add(comgetandset);
//                }
//                map.put("Header",arrcol1);
//                map.put("Column",arrlist);
//            }else{
//                ////System.out.println("CRS is null.....!!");
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally{
//            try{
//                con.close();
//            }catch(Exception e){}
//        }
        return map;
    }


    public Map getPopupDetails(String queryname,Map param){
        ArrayList arrlist=new ArrayList();
        ArrayList arrcol=new ArrayList();
        ArrayList arrcol1=new ArrayList();
        ResultSetMetaData rsm=null;
        Connection con=null;
        CachedRowSet crstype;
        Map map=new HashMap();
//        try{
//
//            con = ConnectionManager.getInstance().getConnection();
//            crstype = QueryProcessor.executeQuery(queryname,param,con);
//            CommonGetterandSetter comgetandsetcol=new CommonGetterandSetter();
//            if(crstype != null)
//            {
//                crstype.beforeFirst();
//                rsm=crstype.getMetaData();
//                ////System.out.println("column count>>>>>>>>>>>>>"+rsm.getColumnCount());
//                for(int j=1;j<=rsm.getColumnCount();j++){
//                  arrcol.add(rsm.getColumnName(j));
//
//                }
//
//                comgetandsetcol.setHeader(arrcol);
//                arrcol1.add(comgetandsetcol);
//                ////System.out.println("arrcol1111111>>>>>>>>>>>>>"+arrcol.size());
//                //crstype.beforeFirst();
//
//                // Add result to Parent & Child  Menu beans
//                while(crstype.next())
//                {
//                    CommonGetterandSetter comgetandset=new CommonGetterandSetter();
//                    for(int j=1;j<=rsm.getColumnCount();j++){
//                        ////System.out.println("crstype.getString(j)"+j+"---"+crstype.getString(j));
//                        comgetandset.setColValue(j,crstype.getString(j));
//                    }
//                    arrlist.add(comgetandset);
//                }
//                map.put("Header",arrcol1);
//                map.put("Column",arrlist);
//            }else{
//                ////System.out.println("CRS is null.....!!");
//            }
//        }catch(Exception e){
//            e.printStackTrace();
//        }finally{
//            try{
//                con.close();
//            }catch(Exception e){}
//        }
        return map;
    }

}
