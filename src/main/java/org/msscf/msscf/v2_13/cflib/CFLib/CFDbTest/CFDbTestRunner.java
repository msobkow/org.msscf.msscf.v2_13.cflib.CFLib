package org.msscf.msscf.v2_13.cflib.CFLib.CFDbTest;

import org.msscf.msscf.v2_13.cflib.CFLib.CFDbTest.appdb.*;
import org.msscf.msscf.v2_13.cflib.CFLib.dbutil.CFLibDbKeyHash256;

import jakarta.persistence.EntityManager;

public class CFDbTestRunner {
    public void run(EntityManager em) throws Exception {
        // Example: create and save a SecUser and SecSession using JPA
        em.getTransaction().begin();
        SecUser user = new SecUser(new CFLibDbKeyHash256("0123456789abcdef"));
        em.persist(user);
        SecSession session = new SecSession(new CFLibDbKeyHash256("fedcba9876543210"), user);
        em.persist(session);
        System.out.println("Sample SecUser and SecSession created.");
        em.getTransaction().commit();
    }
}
