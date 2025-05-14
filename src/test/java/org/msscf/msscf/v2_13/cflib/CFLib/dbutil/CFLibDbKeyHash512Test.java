package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CFLibDbKeyHash512Test {

    @Test
    void testGetHashAlgo() {
		assertEquals("SHA-512", CFLibDbKeyHash512.HASH_ALGO);
		CFLibDbKeyHash512 k = new CFLibDbKeyHash512();
		assertEquals("SHA-512", k.getHashAlgo());
    }

    @Test
    void testGetHashLength() {
		assertEquals(64, CFLibDbKeyHash512.HASH_LENGTH);
		CFLibDbKeyHash512 k = new CFLibDbKeyHash512();
		assertEquals(64, k.getHashLength());
    }

    @Test
    void testGetHashLengthString() {
		assertEquals(128, CFLibDbKeyHash512.HASH_LENGTH_STRING);
		CFLibDbKeyHash512 k = new CFLibDbKeyHash512();
		assertEquals(128, k.getHashLengthString());
    }

    @Test
    void testGetBytes() {
		CFLibDbKeyHash512 k = new CFLibDbKeyHash512();
		byte[] bytes = k.getBytes();
		assertNull(bytes);
		CFLibDbKeyHash512 k2 = CFLibDbKeyHash512.nullGet();
		byte[] bytes2 = k2.getBytes();
		assertNotNull(bytes2);
    }

    @Test
    void testNullGet() {
		CFLibDbKeyHash512 n = CFLibDbKeyHash512.nullGet();
		byte[] bytes = n.getBytes();
		assertNotNull(bytes);
		assertEquals(64, bytes.length);
		for (int i = 0; i < bytes.length; i++) {
			assertEquals(0, bytes[i]);
		}
    }

	@Test
    void testCompareOrdered() {

		CFLibDbKeyHash512 nullA = CFLibDbKeyHash512.nullGet();
		CFLibDbKeyHash512 nullB = CFLibDbKeyHash512.nullGet();
		assertEquals(0, CFLibDbKeyHash512.compareOrdered(nullA, nullB));

		byte[] bA = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		byte[] bB = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
		CFLibDbKeyHash512 valA = new CFLibDbKeyHash512(bA);
		assertEquals(0, CFLibDbKeyHash512.compareOrdered(valA, valA));
		CFLibDbKeyHash512 anotherA = new CFLibDbKeyHash512(bA);
		assertEquals(0, CFLibDbKeyHash512.compareOrdered(valA, anotherA));

		CFLibDbKeyHash512 valB = new CFLibDbKeyHash512(bB);
		assertTrue(CFLibDbKeyHash512.compareOrdered(valA, valB) < 0);
		assertTrue(CFLibDbKeyHash512.compareOrdered(valB, valA) > 0);

		CFLibDbKeyHash512 notNull = new CFLibDbKeyHash512(0);
		assertTrue(CFLibDbKeyHash512.compareOrdered(nullA, notNull) != 0);
		assertTrue(CFLibDbKeyHash512.compareOrdered(notNull, nullA) != 0);
    }
}
