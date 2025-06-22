package org.msscf.msscf.v2_13.cflib.CFLib.CFDbTest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.msscf.msscf.v2_13.cflib.CFLib.CFDbTest.appdb.*;
import org.msscf.msscf.v2_13.cflib.CFLib.dbutil.CFLibDbKeyHash256;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class CFDbTestRunner implements CommandLineRunner {
    @Autowired
    private SecUserRepository secUserRepository;
    @Autowired
    private SecSessionRepository secSessionRepository;

    @Override
    public void run(String... args) throws Exception {
        // Example: create and save a SecUser and SecSession
        SecUser user = new SecUser( new CFLibDbKeyHash256("0123456789abcdef")); // Example PID
        secUserRepository.save(user);
        SecSession session = new SecSession(new CFLibDbKeyHash256("fedcba9876543210"), user);
        secSessionRepository.save(session);
        System.out.println("Sample SecUser and SecSession created.");
    }
}
