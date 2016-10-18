package com.manikssys.in.operational.business;

import com.manikssys.in.operational.beans.OprEntrySeal;
import com.manikssys.in.security.beans.ScrUserMaster;

/**
 * User: sandeep
 * Date: Sep 14, 2010
 */
public interface IEntrySeal {
    public OprEntrySeal getEntrySeal(ScrUserMaster userMaster);
    public OprEntrySeal  sealEntry(OprEntrySeal entrySealMaster);
}
