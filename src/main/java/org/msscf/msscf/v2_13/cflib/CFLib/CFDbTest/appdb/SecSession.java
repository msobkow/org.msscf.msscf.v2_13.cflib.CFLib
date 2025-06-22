package org.msscf.msscf.v2_13.cflib.CFLib.CFDbTest.appdb;

import org.msscf.msscf.v2_13.cflib.CFLib.dbutil.CFLibDbKeyHash256;

import jakarta.persistence.*;

@Entity
@Table(name = "SecSession", schema = "appdb")
public class SecSession {
    @Id
    @AttributeOverrides({
        @AttributeOverride(name = "bytes", column = @Column(name = "pid", nullable = false, unique = true, length = 32))
    })
    private CFLibDbKeyHash256 pid;

    @ManyToOne(optional = false)
    @JoinColumn(name = "secuser_pid", referencedColumnName = "pid")
    private SecUser secUser;

    // ... other fields ...

    public SecSession() {}

    public SecSession(CFLibDbKeyHash256 pid, SecUser secUser) {
        this.pid = pid;
        this.secUser = secUser;
    }

    public CFLibDbKeyHash256 getPid() {
        return pid;
    }

    public void setPid(CFLibDbKeyHash256 pid) {
        this.pid = pid;
    }

    public SecUser getSecUser() {
        return secUser;
    }

    public void setSecUser(SecUser secUser) {
        this.secUser = secUser;
    }
}
