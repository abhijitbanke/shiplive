/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.common;

/**
 *
 * @author sandeep
 */
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.*;
import org.zkoss.zul.impl.InputElement;
import org.zkoss.zul.impl.XulElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ComponentValidation {

    // Method for check the valid expression match , its having three parameters pattern to match,input type element on which
    // expression to be matched & message text to display if found
    public  static  WrongValueException validReg(String pattern,InputElement element,String msg)
    {
        Pattern px = null;
        px = Pattern.compile(pattern);
        Matcher m = px.matcher(element.getText());
        if(!m.find()){
            return  new WrongValueException(element, msg);
        }

        return null;
    }

    // Method to check the given input is valid email or not
    public static WrongValueException isValidEmail(InputElement element, String msg) {
        String eEmail = "^([0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\\w]*[0-9a-zA-Z]\\.)+[a-zA-Z]{2,9})$";
        return validReg(eEmail, element, msg == null ? org.zkoss.util.resource.Labels.getLabel("common.validation.email") : msg);
    }

    // Method to check the given input is within specific range or not
    public static WrongValueException isWithinRange(float start, float end, float number, InputElement element, String msg) {
        if (!(start <= number && number <= end)) {
            return new WrongValueException(element, msg);
        }
        return null;
    }

    // Method to check the given input is within specific range or not
    public static WrongValueException isWithinRange(float start, float end, String number, InputElement element, String msg) {
        if (!number.isEmpty()) {
            float value = new Float(number).floatValue();
            if (!(start <= value && value <= end)) {
                return new WrongValueException(element, msg == null ? org.zkoss.util.resource.Labels.getLabel("common.commonListAction.invalidRange") : msg);
            }
        } else {
            return new WrongValueException(element, msg == null ? org.zkoss.util.resource.Labels.getLabel("common.commonListAction.mandatoryInput") : msg);
        }
        return null;
    }

    // Method to compare the two dates , it returns 0 if both are equals,1 if first date is greater than second,-1 otherwise
    public static int compareDate(Date d1, Date d2) {

        if (d1.before(d2)) {
            return -1;
        }
        if (d1.after(d2)) {
            return 1;
        }
        if (d1.equals(d2)) {
            return 0;
        } else {
            return -100;
        }
    }

    // Method to check wheather  the given input is integer or not
    public static WrongValueException isValidInteger(InputElement element, String msg) {

        try {
            Integer.parseInt(element.getText().trim());
        } catch (NumberFormatException e) {
            return new WrongValueException(element, msg == null ? org.zkoss.util.resource.Labels.getLabel("common.validation.message.integer") : msg);
        }
        return null;
    }

    // Method to check the given input is float or not
    public static WrongValueException isValidFloat(InputElement element, String msg) {

        try {
            Float.parseFloat(element.getText().trim());
        } catch (NumberFormatException e) {
            return new WrongValueException(element, msg == null ? org.zkoss.util.resource.Labels.getLabel("common.validation.message.float") : msg);
        }
        return null;
    }
    // Method to check for the mandatory Text field

    public static WrongValueException validateManditoryTextbox(InputElement element, String msg) {
        try {
            if (element.getText().trim().length() == 0) {
                return new WrongValueException(element, msg == null ? org.zkoss.util.resource.Labels.getLabel("common.commonListAction.mandatoryInput") : msg);
            }

            return null;
        } catch (Exception e) {
            return new WrongValueException(element, msg == null ? org.zkoss.util.resource.Labels.getLabel("common.commonListAction.invalidDate") : msg);
        }

    }

    public static WrongValueException validateOptionalTextbox(InputElement element, String msg) {
        try {
            if (element.getText().trim().length() == 0) {
                return new WrongValueException(element, msg == null ? org.zkoss.util.resource.Labels.getLabel("common.commonListAction.mandatoryInput") : msg);
            }

            return null;
        } catch (Exception e) {
            return new WrongValueException(element, msg == null ? org.zkoss.util.resource.Labels.getLabel("common.commonListAction.invalidDate") : msg);
        }
    }
    // Method to check for if the datebox has date greater than the given date

    public static WrongValueException isDateAfterCurrentDate(Datebox dateBox, String msgKey, boolean isMandatory, String mandatoryMsgKey) {
        Date selectedDate = dateBox.getValue();

        if (selectedDate == null && isMandatory) {
            return new WrongValueException(dateBox, mandatoryMsgKey == null
                    ? Labels.getLabel("common.validation.mandatory.date") : mandatoryMsgKey);
        }

        if (selectedDate != null && selectedDate.after(new Date())) {
            return new WrongValueException(dateBox, msgKey == null
                    ? Labels.getLabel("common.validation.date.after.current") : msgKey);
        }
        return null;
    }

    //   Method to check for the mandatory Combo field
    public static WrongValueException validateManditoryCombo(Combobox element, String msg) {
        if (element.getSelectedIndex() < 0)//i.e select
        {
            return new WrongValueException(element, msg == null ? org.zkoss.util.resource.Labels.getLabel("common.validation.mandatory.combobox") : msg);
        }

        return null;
    }

    //   Method to check for the mandatory Combo field
    public static WrongValueException validateManditoryList(Listbox element, String msg) {
        if (element.getSelectedIndex() == 0)//i.e select
        {
            return new WrongValueException(element, msg == null ? org.zkoss.util.resource.Labels.getLabel("common.validation.mandatory.listbox") : msg);
        }

        return null;
    }

    // Method to check for the valid precision for the given float value
    public static WrongValueException validateFloatPrecision(String text, InputElement element, String msg) {


        //return  new WrongValueException( element,msg==null?org.zkoss.util.resource.Labels.getLabel( "common.commonListAction.mandatory"):msg);


        return null;
    }

    //remove previous error of component and append new Error
    public static void upadateErrorList(WrongValueException e, ArrayList<WrongValueException> errorList) {

        ArrayList<WrongValueException> preErrList = new ArrayList<WrongValueException>();
        for (WrongValueException error : errorList) {
            if (e.getComponent() == error.getComponent()) {
                preErrList.add(error);
            }
        }

        if (preErrList.size() != 0) {
            errorList.removeAll(preErrList);
        }

        errorList.add(e);

        showErrorMessage(e); // Call for show the error message
    }

    //removes all Errors associate with component
    public static void removeErrorFromList(Component comp, ArrayList<WrongValueException> errorList) {
        ArrayList<WrongValueException> preErrList = new ArrayList<WrongValueException>();
        for (WrongValueException error : errorList) {
            if (comp == error.getComponent()) {
                preErrList.add(error);

            }
        }
        if (preErrList.size() != 0) {
            errorList.removeAll(preErrList);
        }

        removeErrorMessage((HtmlBasedComponent) comp);  // Call for Remove error message

    }

    //show error message with red color an tooltiptext for perticular component
    public static void showErrorMessage(WrongValueException e) {
        if (e.getComponent() instanceof HtmlBasedComponent) {
            HtmlBasedComponent compo = (HtmlBasedComponent) e.getComponent();
            compo.setStyle("border:1px solid red;");
            compo.setTooltiptext(e.getMessage());
        }


    }

    //remove error message for perticular component an reset color
    public static void removeErrorMessage(HtmlBasedComponent e) {
        e.setStyle("border:1px solid #7F9DB9;");
        e.setTooltiptext("");
    }

    //show error message with red color an tooltiptext for all componenets in the list and set focus  to error componenet acive page
    public static void showErrorMesList(ArrayList<WrongValueException> errorList) {
        for (WrongValueException e : errorList) {
            if (e.getComponent() instanceof HtmlBasedComponent) {
                HtmlBasedComponent compo = (HtmlBasedComponent) e.getComponent();
                compo.setStyle("border:1px solid red;");
                compo.setTooltiptext(e.getMessage());
            }
        }
        showErrorPage(errorList);
    }
    //remove error message for all component: an reset color

    public static void removeErrorMesList(ArrayList<WrongValueException> errorList) {
        for (WrongValueException e : errorList) {
            if (e.getComponent() instanceof HtmlBasedComponent) {
                HtmlBasedComponent compo = (HtmlBasedComponent) e.getComponent();
                compo.setStyle("border:1px solid #7F9DB9;");
                compo.setTooltiptext("");
            }
        }
    }

    //set focus  to error componenet acive page
    public static void showErrorPage(ArrayList<WrongValueException> errorList) {
        //WrongValueException e= errorList.get(errorList.size()-1);

        for (WrongValueException e : errorList) {
            Object object = e.getComponent().getParent();
            //if(object!=null)
            if (object instanceof Row) {
                Row row = (Row) object;
                Grid grid = row.getGrid();
                if (grid == null || grid.getMold() != null) {
                    continue;
                }
                int rowIndex = row.getParent().getChildren().indexOf(row);
                int pageSise = grid.getPageSize();
                int newActivePage = rowIndex / pageSise;
                grid.setActivePage(newActivePage);

            }
        }
    }
    // this will remove updated row from savelist if if tha row is deleted

    public static void upadatesaveList(ArrayList deleteList, ArrayList<Row> saveList) {
        ArrayList<Row> deletedUpdatedRow = new ArrayList();

        for (int i = 0; i < saveList.size(); i++) {
            Row row = (Row) saveList.get(i);
            if (deleteList.contains(row.getValue())) {
                deletedUpdatedRow.add(row);

            }
        }
        if (deletedUpdatedRow.size() != 0) {
            saveList.removeAll(deletedUpdatedRow);
        }
    }

    public static void main(String[] args) {
//    String clmNames[]={"name"};
//    String clmValues[][]={{"T1","A1"},{"test133"} };
//
//    //ystem.out.println(deo.getDuplicateRecordQuery("LeaveHead", clmNames, clmValues)); ;
//   Row row=new Row();
// LeaveHead head=  new LeaveHead();
// head.setName("@@W");
//   row.setValue(head);
//   ArrayList<Row> rList=new ArrayList<Row>();
//   rList.add(row);
//   checkUniqueConstraint(rList,clmNames);
//}
    }

//    // Method for to check the Unique value for a perticular field(s) in the grid
//    public static boolean  checkUniqueConstraint(ArrayList<Row> saveList, String []prop,int index[])
//    {
//        ArrayList checkList=new ArrayList();
//        Login user = (Login) Sessions.getCurrent().getAttribute("user");
//        try{
//            for(Row row:saveList)
//            {
//                checkList.add(row.getValue());
//            }
//        }catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        boolean result= isUniqueText( saveList, index); // Check in Grid
//        if(result)
//        {
//            CommonDAO deo=new CommonDAO();
//            boolean temp=   deo.istDuplicate(checkList, prop,user.getDefaultCompany());   // Check in Database
//            return temp;
//        }
//        else
//            return  true;
//    }

    // Method to check the duplicate fields in the grid
    public static boolean isUniqueText(ArrayList<Row> saveList, int index[]) {
        if (saveList.size() < 0) {
            return false;
        }

        Rows rows = ((Row) saveList.get(0)).getGrid().getRows();

        List list =  rows.getChildren();
        List<Row> rowsList  = list;
        int m = 0;
        for (Row rowCurent : rowsList) {
            if (rowCurent.getValue() == null) {
                continue;
            }

            for (Row rowTemp : rowsList) {
                if (rowTemp.getValue() == null) {
                    continue;
                }

                if (rowTemp.getValue() != rowCurent.getValue()) {
                    for (int i = 0; i < index.length; i++) {
                        InputElement text1 = (InputElement) rowTemp.getChildren().get(index[i]);
                        InputElement text2 = (InputElement) rowCurent.getChildren().get(index[i]);
                        if (text1.getText().trim().equals(text2.getText().trim())) {
                            return false;
                        }
                    }
                }
            }

            m++;
        }
        return true;
    }

    public static WrongValueException isAlfaNumericField(InputElement input, boolean isManditory) {
//        if (isManditory) {
//
//            return validReg("^[A-Za-z0-9]+[A-Za-z0-9 ]*$", input, Labels.getLabel("common.validation.mandatory.alfanumeric"));
//        } else {
//            return validReg("^[A-Za-z0-9]*[A-Za-z0-9 ]*$", input, Labels.getLabel("common.validation.alfanumeric"));
//        }
        return null;
    }

    public static WrongValueException isAlfabeticOrAlfaNumericField(InputElement input, boolean isManditory) {



//        if (isManditory) {
//
//            if (isPositiveNumericField(input, isManditory) == null) {
//                // System.out.println("its an numric");
//                return new WrongValueException(input, Labels.getLabel("common.validation.manditory.alfanumeric.alfabetic"));
//            }
//            if (isAlfaNumericField(input, isManditory) != null) {
//
//                //  System.out.println("its not alfa ");
//                return new WrongValueException(input, Labels.getLabel("common.validation.manditory.alfanumeric.alfabetic"));
//            }
//
//
//        } else {
//            if (isPositiveNumericField(input, isManditory) == null) {
//                return new WrongValueException(input, Labels.getLabel("common.validation.alfanumeric.alfabetic"));
//            }
//            if (isAlfaNumericField(input, isManditory) != null) {
//                return new WrongValueException(input, Labels.getLabel("common.validation.alfanumeric.alfabetic"));
//            }
//        }
        return null;

    }

    public static WrongValueException isAlphaWithSpaceField(InputElement input, boolean isManditory) {
//        if (isManditory) {
//            return validReg("^[A-Za-z ]+$", input, Labels.getLabel("common.validation.mandatory.alfabetic"));
//        } else {
//            return validReg("^[A-Za-z ]*$", input, Labels.getLabel("common.validation.alfabetic"));
//        }
        return null;

    }

    public static WrongValueException isAlphaField(InputElement input, boolean isManditory) {
//        if (isManditory) {
//            return validReg("^[A-Za-z]+$", input, Labels.getLabel("common.validation.mandatory.alfabetic"));
//        } else {
//            return validReg("^[A-Za-z]*$", input, Labels.getLabel("common.validation.alfabetic"));
//        }
        return null;

    }

    public static WrongValueException isPositiveNumericField(InputElement input, boolean isManditory) {
//        if (isManditory) {
//            return validReg("^[0-9 +]+$", input, Labels.getLabel("common.validation.mandatory.numeric"));
//        } else {
//            return validReg("^[0-9 +]*$", input, Labels.getLabel("common.validation.numeric"));
//        }
        return null;

    }

    public static WrongValueException isPositiveDecimalField(InputElement input, boolean isManditory) {
//        if (isManditory) {
//            //return validReg("(^\\d*\\.?\\d*[0-9]+\\d+$)|(^[0-9]+\\d*\\.\\d+$)", input, Labels.getLabel("common.validation.mandatory.numeric"));
//            return validReg("^[0-9.{0,1}?+$", input, Labels.getLabel("common.validation.mandatory.numeric"));
//        } else {
//            //return validReg("(^\\d*\\.?\\d*[0-9]+\\d*$)|(^[0-9]+\\d*\\.\\d*$)", input, Labels.getLabel("common.validation.numeric"));
//            return validReg("^[0-9.]*$", input, Labels.getLabel("common.validation.numeric"));
//        }
        return null;

    }

    public static WrongValueException isValidBirtdate(Datebox input, boolean isManditory) {
//        WrongValueException e;
//        if (isManditory) {
//            e = validateManditoryTextbox(input, null);
//            if (e != null) {
//                return e;
//            }
//
//            long eighteenYears = 31556926000l * 18;
//            long today = new Date().getTime();
//            long inputDate = input.getValue().getTime();
//            System.out.println("today" + today);
//            System.out.println("inputDate" + inputDate);
//            if ((today - inputDate) < eighteenYears) {
//                return new WrongValueException(input, Labels.getLabel("common.validation.mandatory.birthdate"));
//            }
//            System.out.println("eighteen years =" + eighteenYears + "and  actual didd" + (today - inputDate));
//
//        } else {
//            e = validateManditoryTextbox(input, null);
//            if (e != null) {
//                return null;
//            }
//
//            long eighteenYears = 31556926000l * 18;
//            long today = new Date().getTime();
//            long inputDate = input.getValue().getTime();
//            System.out.println("eighteen years =" + eighteenYears + "and  actual didd" + (today - inputDate));
//            if ((today - inputDate) < eighteenYears) {
//                return new WrongValueException(input, Labels.getLabel("common.validation.birthdate"));
//            }
//
//        }




        return null;

    }

    public static WrongValueException isValidRangeNumbers(float inputFrom, float inputTo, InputElement inputToBox) {
//        WrongValueException e;
//        if (inputTo < inputFrom) {
//            e = new WrongValueException(inputToBox, Labels.getLabel("common.validation.validValidIntRange"));
//            return e;
//        } else {
//            return null;
//        }
        return null;
    }

    public static boolean isErrorToolTip(XulElement input) {
        boolean isError = false;
        if (input.getTooltiptext() != null) {
            if (input.getTooltiptext().length() > 0) {
                isError = true;
            }
        }
        System.out.println("valaidatio " + input.getId() + " " + isError);
        return isError;
    }

    public static WrongValueException isDecimalNumber(InputElement input, boolean isManditory) {
//        if (isManditory) {
//            return isValidFloat(input.getText(), input, Labels.getLabel("common.validation.mandatory.decimal"));
//        } else {
//
//            if(input.getText().length() > 0)
//                return isValidFloat(input.getText(), input, Labels.getLabel("common.validation.decimal"));
//            else
//                return null;
//        }
        return null;

    }


    // Method to check the duplicate field(s) in the grid
    public static boolean isDuplicateText(Grid grid, int index[]) {
       
        Rows rows=grid.getRows();

        try {
            for(int i=0;i<rows.getChildren().size()-1;i++){
                Row curRow=(Row)rows.getChildren().get(i);
                Row nxtRow=(Row)rows.getChildren().get(i+1);
                for (int j = 0; j < index.length; j++) {
                    InputElement text1 = (InputElement) curRow.getChildren().get(index[j]);
                    InputElement text2 = (InputElement) nxtRow.getChildren().get(index[j]);
                    if (text1.getText().trim().equals(text2.getText().trim())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {e.printStackTrace();}

        return false;
    }
}
