package com.manikssys.in.operational.beans;

import com.manikssys.in.security.beans.ScrUserMaster;

/**
 * Created by IntelliJ IDEA.
 * User: sandeep
 * Date: Sep 14, 2010
 * Time: 11:23:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class OprEntrySeal {
    private OprEntrySealPK id;

    public OprEntrySealPK getId() {
        return id;
    }

    public void setId(OprEntrySealPK id) {
        this.id = id;
    }

    private ScrUserMaster updatedBy;

    public ScrUserMaster getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(ScrUserMaster updatedBy) {
        this.updatedBy = updatedBy;
    }

    private int versionId;

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OprEntrySeal that = (OprEntrySeal) o;

        if (versionId != that.versionId) return false;

        if (updatedBy != null ? !updatedBy.equals(that.updatedBy) : that.updatedBy != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;

        result = 31 * result + (updatedBy != null ? updatedBy.hashCode() : 0);
        result = 31 * result + versionId;
        return result;
    }

    private ScrUserMaster createdBy;

    public ScrUserMaster getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ScrUserMaster createdBy) {
        this.createdBy = createdBy;
    }
    
//    private Date createdDate;

//    public Date getCreatedDate() {
//        return createdDate;
//    }
//
//    public void setCreatedDate(Date createdDate) {
//        this.createdDate = createdDate;
//    }
    
    
    
    private OprBranchMaster oprBranchMasterByBranchId;

    public OprBranchMaster getOprBranchMasterByBranchId() {
        return oprBranchMasterByBranchId;
    }

    public void setOprBranchMasterByBranchId(OprBranchMaster oprBranchMasterByBranchId) {
        this.oprBranchMasterByBranchId = oprBranchMasterByBranchId;
    }


}
