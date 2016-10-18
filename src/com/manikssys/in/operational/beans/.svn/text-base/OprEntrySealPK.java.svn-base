package com.manikssys.in.operational.beans;

import java.io.Serializable;
import java.util.Date;

/**
 
 * User: sandeep
 * Date: Sep 14, 2010
 */
public class OprEntrySealPK implements Serializable {
    private String branchId;

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    private Date createdDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OprEntrySealPK that = (OprEntrySealPK) o;

        if (branchId != null ? !branchId.equals(that.branchId) : that.branchId != null) return false;
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = branchId != null ? branchId.hashCode() : 0;
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }
}
