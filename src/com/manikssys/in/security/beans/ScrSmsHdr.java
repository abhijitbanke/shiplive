/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.manikssys.in.security.beans;

/**
 * ScrSmsHdr generated by hbm2java
 */
public class ScrSmsHdr  implements java.io.Serializable {


     private Integer id;
     private Integer SStatus;
    

    public ScrSmsHdr() {
    }

    public ScrSmsHdr(Integer SStatus) {
       this.SStatus = SStatus;
       
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getSStatus() {
        return this.SStatus;
    }

    public void setSStatus(Integer SStatus) {
        this.SStatus = SStatus;
    }
}



