/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.common;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author pc
 */
public class VPRemarkWin extends Window implements ICommonWindowInterface, org.zkoss.zk.ui.event.EventListener
{
    Textbox remarkHistory;
    Textbox remark;
    
    public void onLoad(){
        System.out.println("VPRemarkWin onLoad called .....");
        remarkHistory = (Textbox)this.getFellow("remarkHistory");
                remark = (Textbox)this.getFellow("remark");
    }
    public void clearScreeen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void saveRecord() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void addRecord() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void closeScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void retrieveRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void deleteRecord() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setControls() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getFirstRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getLastRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getNextRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void getPreviousRecord() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void onEvent(Event event) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
