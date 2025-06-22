package org.msscf.msscf.v2_13.cflib.CFLib.CFDbTest.appdb;

import org.msscf.msscf.v2_13.cflib.CFLib.dbutil.CFLibDbKeyHash256;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SecSessionRepository extends JpaRepository<SecSession, CFLibDbKeyHash256> {}
