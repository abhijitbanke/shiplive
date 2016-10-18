package com.manikssys.in.operational.beans;
// Generated Jul 1, 2010 2:48:31 PM by Hibernate Tools 3.2.1.GA


import com.manikssys.in.security.beans.ScrUserMaster;

import java.util.Date;

/**
 * OprEntryMaster generated by hbm2java
 */
public class OprEntryMaster  implements java.io.Serializable {


     private String entryId;
     private OprPortMaster oprPortMaster;
     private String entryCode;
     private String entryType;
     private String status;
     private ScrUserMaster createdBy;
     private Date createdDate;
     private ScrUserMaster updatedBy;
     private Date updatedDate;
     private int versionId;
     private Date fromDate;
     private Date toDate;

    public OprEntryMaster() {
    }

	
    public OprEntryMaster(String entryId, OprPortMaster oprPortMaster, String entryCode, int versionId) {
        this.entryId = entryId;
        this.oprPortMaster = oprPortMaster;
        this.entryCode = entryCode;
        this.versionId = versionId;
    }
    public OprEntryMaster(String entryId, OprPortMaster oprPortMaster, String entryCode, String entryStatus, ScrUserMaster createdBy, Date createdDate, ScrUserMaster updatedBy, Date updatedDate, int versionId) {
       this.entryId = entryId;
       this.oprPortMaster = oprPortMaster;
       this.entryCode = entryCode;
       this.status = entryStatus;
       this.createdBy = createdBy;
       this.createdDate = createdDate;
       this.updatedBy = updatedBy;
       this.updatedDate = updatedDate;
       this.versionId = versionId;
    }
   
    public String getEntryId() {
        return this.entryId;
    }
    
    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }
    public OprPortMaster getOprPortMaster() {
        return this.oprPortMaster;
    }
    
    public void setOprPortMaster(OprPortMaster oprPortMaster) {
        this.oprPortMaster = oprPortMaster;
    }
    public String getEntryCode() {
        return this.entryCode;
    }
    
    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String entryStatus) {
        this.status = entryStatus;
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

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
}


