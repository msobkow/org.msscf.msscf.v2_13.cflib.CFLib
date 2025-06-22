package org.msscf.msscf.v2_13.cflib.CFLib.CFDbTest.appdb;

import org.msscf.msscf.v2_13.cflib.CFLib.dbutil.CFLibDbKeyHash256;

import jakarta.persistence.*;

@Entity
@Table(name = "SecAdmin", schema = "appdb")
public class SecAdmin extends SecUser {
    // ... additional fields for SecAdmin ...

    public SecAdmin() {
        super();
    }

    public SecAdmin(CFLibDbKeyHash256 pid) {
        super(pid);
    }
}
