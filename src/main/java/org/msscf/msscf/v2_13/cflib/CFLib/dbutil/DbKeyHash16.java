package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author msobkow
 */
public class DbKeyHash16 implements Serializable, Comparator, Comparable {

  static final String hexDigits = "0123456789abcdef";
  static final int UUID_INDEX_LOW = 0;
  static final int UUID_INDEX_HIGH = 8;
  static final int COUNTER_INDEX = 16;
  static final int MACHINE_INDEX = 24;
  static final int WGTID_INDEX = 28;
  static final int TOTAL_BYTES = 32;
  static final public  int HASH_LENGTH = 16; // md5 hash size
  static final public int HASH_LENGTH_STRING = HASH_LENGTH * 2; // md5 hash size as a string

  public static byte[] buildFromHex(String string) {
    if (string == null) {
      // allowed
    }
    else if (string.length() > HASH_LENGTH * 2) {
      throw new IllegalArgumentException("string length is " + string.length() + ".  Must be <= " + HASH_LENGTH * 2 + ".  string is '" + string + "'.");
    }
    byte[] b = new byte[HASH_LENGTH];
    if (string == null) {
      return b;
    }

    int n = string.length();
    for (int i = 0; i < n; i += 2) {
      b[i / 2] = (byte) ((Character.digit(string.charAt(i), 16) << 4) + Character.digit(string.charAt(i + 1), 16));
    }
    return b;
  }

  public static DbKeyHash16 fromHex(String string) {
    byte[] b = buildFromHex(string);
    DbKeyHash16 h = new DbKeyHash16();
    h.id16 = b;
    return h;
  }

  public static Comparator<DbKeyHash16> getComparator() {

    return new Comparator<DbKeyHash16>() {
      @Override
      public int compare(DbKeyHash16 a, DbKeyHash16 b) {
        return compareOrdered(a, b);
      }
    };
  }
  
  byte[] id16;
  static final int CONCURRENT_DIGESTS = 8;
  static final ByteBuffer[] hashBuffer = new ByteBuffer[CONCURRENT_DIGESTS];
  static long counter = 1;
  static final MessageDigest[] m = new MessageDigest[CONCURRENT_DIGESTS];
  private static final long serialVersionUID = 1L;

  static {
    try {
      for (int i = 0; i < CONCURRENT_DIGESTS; i++) {
        UUID u = UUID.randomUUID();
        hashBuffer[i] = ByteBuffer.allocate(TOTAL_BYTES);
        hashBuffer[i].putLong(UUID_INDEX_LOW, u.getMostSignificantBits());
        hashBuffer[i].putLong(UUID_INDEX_HIGH, u.getLeastSignificantBits());
        hashBuffer[i].putLong(COUNTER_INDEX, counter);
        hashBuffer[i].putInt(MACHINE_INDEX, 1);
        hashBuffer[i].putInt(MACHINE_INDEX, 1);
        m[i] = MessageDigest.getInstance("MD5");
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public DbKeyHash16() {
  }

  /**
   * This is the hex code of the underlying ID. THIS IS NOT A HASHING FUNCTION.
   */
  public DbKeyHash16(String hexId) {
    id16 = buildFromHex(hexId);
  }

  public DbKeyHash16(long low, long high) {
    setAsLongs(low, high);
  }

  public DbKeyHash16(double v) {
    setAsLongs(0, (long) v);
  }

  public DbKeyHash16(byte[] anId) {
    if (anId == null) {
      // allowed
    }
    else if (anId.length > HASH_LENGTH) {
      throw new IllegalArgumentException("anId length must be <= " + HASH_LENGTH + ".");
    }
    id16 = new byte[HASH_LENGTH];
    if (anId != null) {
      System.arraycopy(anId, 0, id16, 0, anId.length);
    }
  }

  public DbKeyHash16(DbKeyHash16 otherKey) {
    if (otherKey == null) {
      id16 = new byte[HASH_LENGTH];
      return;
    }
    byte[] _newId = new byte[otherKey.id16.length];
    System.arraycopy(otherKey.id16, 0, _newId, 0, _newId.length);
    this.id16 = _newId;
  }

  public DbKeyHash16(DbKeyHash20 k20) {
    id16 = new byte[HASH_LENGTH];
    if (k20 != null) {
      System.arraycopy(k20.getId(), 0, id16, 0, HASH_LENGTH);
    }
  }

  public static DbKeyHash16 fromInt(int v) {
    DbKeyHash16 h = nullGet();
    h.id16[3] = (byte) (v & 0xFF);
    h.id16[2] = (byte) ((v >> 8) & 0xFF);
    h.id16[1] = (byte) ((v >> 16) & 0xFF);
    h.id16[0] = (byte) ((v >> 24) & 0xFF);
//        h.dump();
//        Logger.getLogger(DbKeyHash16.class.getName()).log(Level.INFO,);
    return h;
  }

  public DbKeyHash16(int notUsed) {
    int thready = (int) (Thread.currentThread().getId() % CONCURRENT_DIGESTS);
    synchronized (m[thready]) {
      while (true) {
        counter++;
        hashBuffer[thready].putLong(COUNTER_INDEX, counter);
        hashBuffer[thready].putInt(WGTID_INDEX, (int) (Math.random() * Integer.MAX_VALUE));
        m[thready].update(hashBuffer[thready].array(), 0, TOTAL_BYTES);

        id16 = m[thready].digest();

        // we want to reserve the bottom 32 bits of the counter for incremental temporary indexing so we regenerate entries that have the top 12 bytes as 0's */
        for (int i = 4; i < HASH_LENGTH; i++) {
          if (id16[i] != 0) {
            return;
          }
        }
      }
    }
  }

  public int hashCode() {
    int result = 0;
    for (int i = 3; i >= 0; i--) {
      //result = (result << 8) | id16[i];
      result = (result << 8) | (0xFF & ((int) id16[i]));
    }
    return result;
  }

  public boolean equals(Object aTest) {
    if (aTest == null) {
      return false;
    }
    if (aTest == this) {
      return true;
    }
    if (aTest.getClass() != getClass()) {
      return false;
    }
    DbKeyHash16 test = (DbKeyHash16) aTest;
    return Arrays.equals(id16, test.id16);
  }

  public int reduceToInt() {
    return hashCode();
  }

  public boolean isNull() {
    if (id16 != null) {
      for (int i = 0; i < HASH_LENGTH; i++) {
        if (id16[i] != 0) {
          return false;
        }
      }
    }
    return true;
  }

  public static final boolean isNull(DbKeyHash16 anId) {
    return anId == null || anId.isNull();
  }

  static public void setMachineId(int id) {
    for (int i = 0; i < CONCURRENT_DIGESTS; i++) {
      hashBuffer[i].putInt(MACHINE_INDEX, id);
    }
  }

  public String asString() {
    if (id16 == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder(id16.length * 2);
    for (int i = 0; i < HASH_LENGTH; i++) {
      sb.append(hexDigits.charAt((id16[i] & 0xF0) >>> 4));
      sb.append(hexDigits.charAt(id16[i] & 0x0F));
    }
    return sb.toString();
  }

  public static final String getJava(DbKeyHash16 id) {
    if (id == null) {
      return "null";
    }
    else {
      return String.format("new DbKeyHash16(\"%s\")", id.asString());
    }
  }

  public void asString(StringBuilder sb) {
    // Construct and return the representive hex string
    for (int i = 0; i < HASH_LENGTH; i++) {
      sb.append(hexDigits.charAt((id16[i] & 0xF0) >>> 4));
      sb.append(hexDigits.charAt(id16[i] & 0x0F));
    }
  }

  @Override
  public String toString() {
    return asString();
  }

  public void dump() {
    Logger.getLogger(DbKeyHash16.class.getName()).log(Level.INFO, dump(new StringBuilder()));
  }

  public String dump(StringBuilder sb) {
    for (int i = 0; i < id16.length; i++) {
      String hx = Integer.toHexString(id16[i] & 0xff);
      if (hx.length() < 2) {
        sb.append("0");
      }
      sb.append(hx);
    }
    return sb.toString();
  }

  public byte[] idGet() {
    return id16;
  }

  /**
   * Get a new hash object with the key set to all 0s
   */
  static public DbKeyHash16 nullGet() {
    DbKeyHash16 k = new DbKeyHash16();
    k.id16 = new byte[HASH_LENGTH];
    return k;
  }

  static public String getNullString() {
    return "00000000000000000000000000000000";
  }

  public byte[] getId16() {
    return id16;
  }

  /**
   * We want DbKeyHashXX to be immutable so this method shouldn't even exist;
   * however it is necessary for JPA. This is the only time it should be used.
   *
   * @param newId16 to be copied from.
   */
  public void setId16(byte[] newId16) {
    if (newId16 == null) {
      throw new NullPointerException("newId16 must not be null.");
    }
    if (newId16.length != 16) {
      throw new IllegalArgumentException("newId16 must be of length 16.");
    }
    id16 = newId16.clone();
  }

  /** Copy into existing key */
  public void setId16(byte[] newId16, int offset,  int length) {
      System.arraycopy(newId16, offset, id16, 0, length);
  }

  public int compare(Object o1, Object o2) {
    DbKeyHash16 h1 = (DbKeyHash16) o1;
    DbKeyHash16 h2 = (DbKeyHash16) o2;
    if (h1 == null && h2 != null) {
      return 1;
    }
    if (h2 == null && h1 != null) {
      return -1;
    }
    if (h2 == null && h1 == null) {
      return 0;
    }
    for (int i = 0; i < HASH_LENGTH; i++) {
      int v1 = h1.id16[i];
      int v2 = h2.id16[i];
      if (v1 < 0) {
        v1 += 256;
      }
      if (v2 < 0) {
        v2 += 256;
      }
      int c = v1 - v2;
      if (c != 0) {
        return c;
      }
    }
    return 0;
  }

  public int compareTo(Object o) {
    int result = compare(this, o);
    return result;
  }

  static public int compareOrdered(DbKeyHash16 h1, DbKeyHash16 h2) {
    if (h1 == null && h2 != null) {
      return -1;
    }
    if (h1 != null && h2 == null) {
      return 1;
    }
    if (h1 == null && h2 == null) {
      return 0;
    }

    for (int i = 0; i < HASH_LENGTH; i++) {
      int v1 = h1.id16[i] + 256;
      int v2 = h2.id16[i] + 256;
      if (v1 < v2) return -1;
      if (v1 > v2) return 1;
    }
    return 0;
  }

  public static DbKeyHash16 hash(String text) {
    if (text != null) {
      try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] buf = text.getBytes("UTF-8");
        md.update(buf);

        return new DbKeyHash16(md.digest());
      }
      catch (Exception ex) {
      }
    }
    return new DbKeyHash16(0);
  }

  public static DbKeyHash16 hash(byte[] payload) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(payload);

      return new DbKeyHash16(md.digest());
    }
    catch (Exception ex) {
    }
    return new DbKeyHash16(0);
  }

  public static DbKeyHash16 hash(byte[]... payload) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      for (byte[] bs : payload) {
        md.update(bs);
      }

      return new DbKeyHash16(md.digest());
    }
    catch (Exception ex) {
    }
    return new DbKeyHash16(0);
  }

  public static DbKeyHash16 hash(DbKeyHash16... payload) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      for (DbKeyHash16 k : payload) {
        md.update(k.id16);
      }
      return new DbKeyHash16(md.digest());
    }
    catch (Exception ex) {
    }
    return new DbKeyHash16(0);
  }

  public static DbKeyHash16 hash(int[] payload) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      for (int x : payload) {
        md.update((byte) ((x >>> 24) & 255));
        md.update((byte) ((x >>> 16) & 255));
        md.update((byte) ((x >>> 8) & 255));
        md.update((byte) (x & 255));
      }

      return new DbKeyHash16(md.digest());
    }
    catch (Exception ex) {
    }
    return new DbKeyHash16(0);
  }

  public byte[] fingerprint() {
    return id16;
  }

  public DbKeyHash16 deepClone() {
    return new DbKeyHash16(this);
  }

  public long getLow() {
    if (id16 == null) {
      return 0;
    }
    long result = 0;
    for (int i = 7; i >= 0; i--) {
      //result = (result << 8) | id16[i];
      result = (result << 8) | (0xFF & ((int) id16[i]));
    }
    return result;
  }

  public long getHigh() {
    if (id16 == null) {
      return 0;
    }
    long result = 0;
    for (int i = 15; i >= 8; i--) {
      //result = (result << 8) | id16[i];
      result = (result << 8) | (0xFF & ((int) id16[i]));
    }
    return result;
  }

  public void setLow(long v) {
    if (id16 == null) {
      id16 = new byte[HASH_LENGTH];
    }
    for (int i = 0; i < 8; i++) {
      id16[i] = (byte) (v & 0xFF);
      v >>= 8;
    }
  }

  public void setHigh(long v) {
    if (id16 == null) {
      id16 = new byte[HASH_LENGTH];
    }
    for (int i = 8; i < 16; i++) {
      id16[i] = (byte) (v & 0xFF);
      v >>= 8;
    }
  }

  public void setAsLongs(long low, long high) {
    setLow(low);
    setHigh(high);
  }

  static public DbKeyHash16 createLow(long low) {
    DbKeyHash16 k = nullGet();
    k.setLow(low);
    return k;
  }

  public static int compareLow(DbKeyHash16 a, DbKeyHash16 b) {
    if (a == null && b == null) {
      return 0;
    }
    if (a == null) {
      return -1;
    }
    if (b == null) {
      return 1;
    }
    long z = a.getLow() - b.getLow();
    if (z < 0) {
      return -1;
    }
    if (z > 0) {
      return 1;
    }
    return 0;
  }

  static public int getHashLength() {
    return HASH_LENGTH;
  }

  static public int getHashStringLength() {
    return HASH_LENGTH_STRING;
  }

  static public DbKeyHash16 fromHexQuick(String string) {
    if (string == null) {
      return null;
    }
    if (string.length() != HASH_LENGTH * 2) {
      return null;
    }
    for (int i = 0; i < string.length(); i++) {
      try {
        if (Character.digit(string.charAt(i), 16) < 0) {
          return null;
        }
      }
      catch (Exception e) {
        return null;
      }

    }
    try {
      return fromHex(string);
    }
    catch (Exception e) {
      return null;
    }
  }

  public short getLowShort() {
    if (id16 == null) {
      return 0;
    }
    return (short) (((0xff & id16[1]) << 8) + (0xFF & id16[0]));
  }

  public int getLowInt() {
    if (id16 == null) {
      return 0;
    }
    return (int) (((0xff & id16[3]) << 24) + ((0xff & id16[2]) << 16) + ((0xff & id16[1]) << 8) + (0xFF & id16[0]));
  }

  public static final DbKeyHash16[] toDbKeyHash16(String[] ids) {
    if (ids == null) {
      return null;
    }
    if (ids.length == 0) {
      return new DbKeyHash16[0];
    }
    DbKeyHash16[] r = new DbKeyHash16[ids.length];
    for (int i = 0; i < ids.length; i++) {
      r[i] = new DbKeyHash16(ids[i]);
    }
    return r;
  }

  public static final List<DbKeyHash16> toDbKeyHash16List(String[] ids) {

    if (ids == null) {
      return null;
    }
    if (ids.length == 0) {
      return Collections.emptyList();
    }
    List<DbKeyHash16> r = new ArrayList<DbKeyHash16>(ids.length);
    for (int i = 0; i < ids.length; i++) {
      r.add(new DbKeyHash16(ids[i]));
    }
    return r;

  }

  public static final Set<DbKeyHash16> toDbKeyHash16Set(String[] ids) {

    if (ids == null) {
      return null;
    }
    if (ids.length == 0) {
      return Collections.emptySet();
    }
    Set<DbKeyHash16> r = new HashSet<DbKeyHash16>(ids.length);
    for (int i = 0; i < ids.length; i++) {
      r.add(new DbKeyHash16(ids[i]));
    }
    return r;

  }

  public static String getUString() {
    return new DbKeyHash16(23934).asString();
  }
}
