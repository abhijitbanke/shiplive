package com.manikssys.in.security.beans;
// Generated Jun 23, 2010 5:14:05 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * ScrMacMaster generated by hbm2java
 */
public class ScrMacMaster  implements java.io.Serializable {


     private String macId;
     private String machineName;
     private String status;
     private ScrUserMaster createdBy;
     private Date createdDate;
     private ScrUserMaster updatedBy;
     private Date updatedDate;
     private int versionId;

    public ScrMacMaster() {
    }

	
    public ScrMacMaster(String macId, String machineName, int versionId) {
        this.macId = macId;
        this.machineName = machineName;
        this.versionId = versionId;
    }
    public ScrMacMaster(String macId, String machineName, String userStatus, ScrUserMaster createdBy, Date createdDate, ScrUserMaster updatedBy, Date updatedDate, int versionId) {
       this.macId = macId;
       this.machineName = machineName;
       this.status = userStatus;
       this.createdBy = createdBy;
       this.createdDate = createdDate;
       this.updatedBy = updatedBy;
       this.updatedDate = updatedDate;
       this.versionId = versionId;
    }
   
    public String getMacId() {
        return this.macId;
    }
    
    public void setMacId(String macId) {
        this.macId = macId;
    }
    public String getMachineName() {
        return this.machineName;
    }
    
    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String userStatus) {
        this.status = userStatus;
    }
    public ScrUserMaster getCreatedBy() {
        return this.createdBy;
    }
    
    public void setCreatedBy(ScrUserMaster createdBy) {
        this.createdBy = createdBy;
    }
    public Date getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public ScrUserMaster getUpdatedBy() {
        return this.updatedBy;
    }
    
    public void setUpdatedBy(ScrUserMaster updatedBy) {
        this.updatedBy = updatedBy;
    }
    public Date getUpdatedDate() {
        return this.updatedDate;
    }
    
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    public int getVersionId() {
        return this.versionId;
    }
    
    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }




}

