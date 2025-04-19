/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author msobkow
 */
public class DbKeyHash20 implements Serializable, Comparator, Comparable<DbKeyHash20> {

    static final int CONCURRENT_DIGESTS = 8;
    static final int UUID_INDEX_LOW = 0;
    static final int UUID_INDEX_HIGH = 8;
    static final int COUNTER_INDEX = 16;
    static final int MACHINE_INDEX = 24;
    static final int WGTID_INDEX = 28;
    static final int TOTAL_BYTES = 32;
    public static final int HASH_LENGTH = 20; // SHA-1 hash size

    public static byte[] buildFromHex(String string) {
        if (string == null) {
            // allowed
        }
        else if (string.length() > HASH_LENGTH * 2) {
            throw new IllegalArgumentException("string length is " + string.length() + ".  Must be <= " + HASH_LENGTH * 2 + ".  string is '" + string + "'.");
        }
        byte[] b = new byte[HASH_LENGTH];
        if (string == null) return b;
        string = string.toLowerCase();
        for (int i = 0; i < string.length() / 2; i++) {
            b[i] = (byte) Integer.parseInt(string.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    // NZ - once this class is immuteable, this field will be made final.
    // see setId for reason not immuteable yet.
    byte[] id;
    static final ByteBuffer[] hashBuffer = new ByteBuffer[CONCURRENT_DIGESTS];
    static long counter = 1;
    static final MessageDigest[] m = new MessageDigest[CONCURRENT_DIGESTS];
    private static final long serialVersionUID = 1L;

    static {
        try {
            for (int i=0; i<CONCURRENT_DIGESTS; i++) {
            UUID u = UUID.randomUUID();
            hashBuffer[i] = ByteBuffer.allocate(TOTAL_BYTES);
            hashBuffer[i].putLong(UUID_INDEX_LOW, u.getMostSignificantBits());
            hashBuffer[i].putLong(UUID_INDEX_HIGH, u.getLeastSignificantBits());
            hashBuffer[i].putLong(COUNTER_INDEX, counter);
            hashBuffer[i].putInt(MACHINE_INDEX, 1);
            hashBuffer[i].putInt(WGTID_INDEX, 1);
            m[i] = MessageDigest.getInstance("SHA-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DbKeyHash20() {
    }

    public DbKeyHash20(byte[] anId) {
        if (anId == null) {
            // allowed
        }
        else if (anId.length > HASH_LENGTH) {
            throw new IllegalArgumentException("anId length must be <= " + HASH_LENGTH + ".");
        }
        id = new byte[HASH_LENGTH];
        if (anId != null) {
        	System.arraycopy(anId, 0, id, 0, anId.length);
        }
    }

    public DbKeyHash20(String hexId) {
        id = buildFromHex(hexId);
    }

    public DbKeyHash20(DbKeyHash20 otherKey) {
        byte[] _newId = new byte[otherKey.id.length];
        System.arraycopy(otherKey.id, 0, _newId, 0, _newId.length);
        this.id = _newId;
    }

    /** Convert a k16 to 20 by writing FF into the high bytes */
    public DbKeyHash20(DbKeyHash16 k16) {
        byte[] anId = k16.idGet();
        Arrays.fill(id, (byte)255);
        id = new byte[HASH_LENGTH];
        if (anId != null) {
        	 System.arraycopy(anId, 0, id, 0, anId.length);
        }
    }

    /** Create a hash that enumerates the given value */
    public static DbKeyHash20 fromInt(int v) {
        DbKeyHash20 h = nullGet();
        h.id[3] = (byte) (v & 255);
        h.id[2] = (byte) ((v >> 8) & 255);
        h.id[1] = (byte) ((v >> 16) & 255);
        h.id[0] = (byte) ((v >> 24) & 255);
//        h.dump();
        return h;
    }

    public DbKeyHash20(int notUsed) {
        int thready = (int) (Thread.currentThread().getId() % CONCURRENT_DIGESTS);
        synchronized (m[thready]) {
            while (true) {
                counter++;
                hashBuffer[thready].putLong(COUNTER_INDEX, counter);
                hashBuffer[thready].putInt(WGTID_INDEX, (int) (Math.random() * Integer.MAX_VALUE));
                m[thready].update(hashBuffer[thready].array(), 0, TOTAL_BYTES);
                id = m[thready].digest();

                // reserve bottom 32 bits for incremental temporary indexing so we regenerate entries that have the top 16 bytes as zeroes

                for (int i = 4; i < HASH_LENGTH; i++) {
                    if (id[i] != 0) return;
                }

            }
        }
    }

    public boolean isNull() {
        if (id == null) return true;
        for (int i = 0; i < HASH_LENGTH; i++) {
            if (id[i] != 0) return false;
        }
        return true;
    }

    public static final boolean isNull(DbKeyHash20 anId) {
    	return anId == null || anId.isNull();
    }

    public String asString() {
        if (id == null) return null;
        StringBuilder sb = new StringBuilder();
        String hx;
        for (int i = 0; i < id.length; i++) {
            hx = Integer.toHexString(id[i] & 0xff);
            if (hx.length() < 2) sb.append('0');
            sb.append(hx);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return asString();
    }

    /**
     * READ-ONLY!
     *
     * @return
     */
    public byte[] idGet() {
        return id;
    }

    public void dump() {
       Logger.getLogger(DbKeyHash20.class.getName()).log(Level.INFO,toString());
    }

    public void dump(StringBuilder sb) {
        sb.append(toString());
    }

    public int hashCode() {
        if (id == null) return 3;
        int result = 0;
        for (int i = 0; i < 4; i++) {
            result = (result << 8) | id[i];
        }
        return result;
    }

    public static final DbKeyHash20 hash(DbKeyHash20 ... payload) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            for (DbKeyHash20 k : payload) md.update(k.id);
            return new DbKeyHash20(md.digest());
        } catch (Exception ex) {
        }
        return new DbKeyHash20(0);
    }

    public static final DbKeyHash20 hash(DbKeyHash20 a, DbKeyHash20 b) {
        return hash(new DbKeyHash20[]{a, b});
    }

    public final boolean equals(Object aTest) {

        if (aTest == null) return false;
        if (aTest == this) return true;
        // NZ: Because we have subclasses now ...
        //if (aTest.getClass() != getClass()) return false;
        if (!(aTest instanceof DbKeyHash20)) { return false; }
        DbKeyHash20 test = (DbKeyHash20) aTest;
        boolean sameId = Arrays.equals(id, test.id);
        return sameId;
    }

    static public DbKeyHash20 nullGet() {
        DbKeyHash20 k = new DbKeyHash20(new byte[HASH_LENGTH]);
        return k;
    }

    /**
     * READ-ONLY!
     */
    public byte[] getId() {
        return id;
    }

    static public void setMachineId(int id) {
        for (int i=0; i<CONCURRENT_DIGESTS; i++) {
            hashBuffer[i].putInt(MACHINE_INDEX, id);
        }
    }

    public final void setId(byte[] newId) {
        if (newId == null) {
            throw new NullPointerException("newId must not be null.");
        }
        if (newId.length != 20) {
            throw new IllegalArgumentException("newId must be of length 20.");
        }
        id = newId.clone();
    }

    public int compare(Object o1, Object o2) {
        DbKeyHash20 h1 = (DbKeyHash20) o1;
        DbKeyHash20 h2 = (DbKeyHash20) o2;
        if (h1 == null) {
            if (h2 != null) {
                return -1;
            }
            else {
                return 0;
            }
        }
        else {
            if (h2 == null) {
                return 1;
            }
            else {
                for (int i = 0; i < HASH_LENGTH; i++) {
                    int v1 = h1.id[i];
                    int v2 = h2.id[i];
                    if (v1 < 0) v1 += 256;
                    if (v2 < 0) v2 += 256;
                    int c = v1 - v2;
                    if (c != 0) return c;
                }
                return 0;
            }
        }
    }

    public static final DbKeyHash20 shaHash(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] buf = text.getBytes("UTF-8");
            md.update(buf);
            DbKeyHash20 h = new DbKeyHash20(md.digest());
            return h;
        }
        // change to unchecked exception because never going to happen,
        // so don't bother the caller with it.
        catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        // change to unchecked exception because never going to happen,
        // so don't bother the caller with it.
        catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** Calculate the standard SHA-1 hash for the byte stream  */
    public static final DbKeyHash20 sha(byte[] payload) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(payload);
            DbKeyHash20 h = new DbKeyHash20(md.digest());
            return h;

        }
        // change to unchecked exception because never going to happen,
        // so don't bother the caller with it.
        catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** Calculate the standard SHA-1 hash for the byte stream  */
    public static final DbKeyHash20 sha(byte[] payload, int offset, int len) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(payload,offset, len);
            DbKeyHash20 h = new DbKeyHash20(md.digest());
            return h;

        }
        // change to unchecked exception because never going to happen,
        // so don't bother the caller with it.
        catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    /** Calculate the standard SHA-1 hash for the array .toString() */
    public static final DbKeyHash20 sha(List list) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update("LIST:".getBytes());
            for (Object o : list) {
                if (o == null) md.update("<NULL>".getBytes());
                else md.update(o.toString().getBytes());
            }
            DbKeyHash20 h = new DbKeyHash20(md.digest());
            return h;
        }
        // change to unchecked exception because never going to happen,
        // so don't bother the caller with it.
        catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public DbKeyHash20 deepClone() {
        return new DbKeyHash20(this);
    }

    /**
     * READ-ONLY!
     */
    public byte[] fingerprint() {
        return id;
    }

    @Override
    public int compareTo(DbKeyHash20 o) {
        return compare(this, o);
    }

    public String getIdAsString() {
        return asString();
    }

    public static DbKeyHash20 hash(String text) {
        if (text != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                byte[] buf = text.getBytes("UTF-8");
                md.update(buf);

                return new DbKeyHash20(md.digest());
            }
            catch (Exception ex) {
            }
        }
        return new DbKeyHash20(0);
    }

  public static String getUString() {
    return new DbKeyHash20(27934).asString();
  }
    
}
