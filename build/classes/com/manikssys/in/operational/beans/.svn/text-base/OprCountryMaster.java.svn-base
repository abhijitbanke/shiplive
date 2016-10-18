package com.manikssys.in.operational.beans;

/**

 * User: sandeep
 * Date: Oct 7, 2010
 */

public class OprCountryMaster implements java.io.Serializable {

     private String countryId;
     private String countryCode;
     private String countryName;

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OprCountryMaster that = (OprCountryMaster) o;

        if (!countryCode.equals(that.countryCode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return countryCode.hashCode();
    }
}
