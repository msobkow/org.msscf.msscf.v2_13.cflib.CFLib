/*
 *	MSS Code Factory CFLib 2.13 Common Application Functionality
 *
 *	Copyright (C) 2016-2026 Mark Stephen Sobkow (mailto:mark.sobkow@gmail.com)
 *	
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *	
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *	
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see &lt;https://www.gnu.org/licenses/&gt;.
 *	
 *	If you wish to modify and use this code without publishing your changes,
 *	or integrate it with proprietary code, please contact Mark Stephen Sobkow
 *	for a commercial license at mark.sobkow@gmail.com
 */

package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CFLibDbKeyHash128Test {

    @Test
    void testGetHashAlgo() {
		assertEquals("MD5", CFLibDbKeyHash128.HASH_ALGO);
		CFLibDbKeyHash128 k = new CFLibDbKeyHash128();
		assertEquals("MD5", k.getHashAlgo());
    }

    @Test
    void testGetHashLength() {
		assertEquals(16, CFLibDbKeyHash128.HASH_LENGTH);
		CFLibDbKeyHash128 k = new CFLibDbKeyHash128();
		assertEquals(16, k.getHashLength());
    }

    @Test
    void testGetHashLengthString() {
		assertEquals(32, CFLibDbKeyHash128.HASH_LENGTH_STRING);
		CFLibDbKeyHash128 k = new CFLibDbKeyHash128();
		assertEquals(32, k.getHashLengthString());
    }

    @Test
    void testGetBytes() {
		CFLibDbKeyHash128 k = new CFLibDbKeyHash128();
		byte[] bytes = k.getBytes();
		assertNull(bytes);
		CFLibDbKeyHash128 k2 = CFLibDbKeyHash128.nullGet();
		byte[] bytes2 = k2.getBytes();
		assertNotNull(bytes2);
    }

    @Test
    void testNullGet() {
		CFLibDbKeyHash128 n = CFLibDbKeyHash128.nullGet();
		byte[] bytes = n.getBytes();
		assertNotNull(bytes);
		assertEquals(16, bytes.length);
		for (int i = 0; i < bytes.length; i++) {
			assertEquals(0, bytes[i]);
		}
    }

	@Test
    void testCompareOrdered() {

		CFLibDbKeyHash128 nullA = CFLibDbKeyHash128.nullGet();
		CFLibDbKeyHash128 nullB = CFLibDbKeyHash128.nullGet();
		assertEquals(0, CFLibDbKeyHash128.compareOrdered(nullA, nullB));

		byte[] bA = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
		byte[] bB = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
		CFLibDbKeyHash128 valA = new CFLibDbKeyHash128(bA);
		assertEquals(0, CFLibDbKeyHash128.compareOrdered(valA, valA));
		CFLibDbKeyHash128 anotherA = new CFLibDbKeyHash128(bA);
		assertEquals(0, CFLibDbKeyHash128.compareOrdered(valA, anotherA));

		CFLibDbKeyHash128 valB = new CFLibDbKeyHash128(bB);
		assertTrue(CFLibDbKeyHash128.compareOrdered(valA, valB) < 0);
		assertTrue(CFLibDbKeyHash128.compareOrdered(valB, valA) > 0);

		CFLibDbKeyHash128 notNull = new CFLibDbKeyHash128(0);
		assertTrue(CFLibDbKeyHash128.compareOrdered(nullA, notNull) != 0);
		assertTrue(CFLibDbKeyHash128.compareOrdered(notNull, nullA) != 0);
    }
}
