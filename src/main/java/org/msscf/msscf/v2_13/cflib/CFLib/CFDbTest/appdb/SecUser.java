package org.msscf.msscf.v2_13.cflib.CFLib.CFDbTest.appdb;

import jakarta.persistence.*;
import org.msscf.msscf.v2_13.cflib.CFLib.dbutil.CFLibDbKeyHash256;

@Entity
@Table(name = "SecUser", schema = "appdb")
@Inheritance(strategy = InheritanceType.JOINED)
public class SecUser {
    @Id
    @AttributeOverrides({
        @AttributeOverride(name = "bytes", column = @Column(name = "pid", nullable = false, unique = true, length = 32))
    })
    private CFLibDbKeyHash256 pid;

    // ... other fields ...

    public SecUser() {}

    public SecUser(CFLibDbKeyHash256 pid) {
        this.pid = pid;
    }

    public CFLibDbKeyHash256 getPid() {
        return pid;
    }

    public void setPid(CFLibDbKeyHash256 pid) {
        this.pid = pid;
    }
}
