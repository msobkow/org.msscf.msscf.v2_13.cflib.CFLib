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

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;

/**
 *
 * @author msobkow
 */
@Embeddable
public class CFLibDbKeyHash384 implements Serializable, Comparator<CFLibDbKeyHash384>, Comparable<CFLibDbKeyHash384> {

  static final long serialVersionUID = 202505062L;
  static final String hexDigits = "0123456789abcdef";
  static final int UUID6_INDEX = 0;
  static final int UUID6_LENGTH = CFLibUuid6.TOTAL_BYTES;
  static final int COUNTER_INDEX = 28;
  static final int COUNTER_LENGTH = 8;
  static final int MACHINE_INDEX = 36;
  static final int MACHINE_LENGTH = 4;
  static final int WGTID_INDEX = 40;
  static final int WGTID_LENGTH = 4;
  static final int TOTAL_BYTES = 44;
  static final public  int HASH_LENGTH = 48; // hash size in bytes
  static final public int HASH_LENGTH_STRING = HASH_LENGTH * 2; // SHA-1 hash size as a string
  static final String HASH_ALGO = "SHA-384";
  
  @Convert(converter = CFLibDbKeyHash384Converter.class)
  @Column(name = "bytes", nullable = false)
  protected byte[] bytes;

  static final int CONCURRENT_DIGESTS = 32;
  static final ByteBuffer[] hashBuffer = new ByteBuffer[CONCURRENT_DIGESTS];
  static long counter = 1;
  static final MessageDigest[] m = new MessageDigest[CONCURRENT_DIGESTS];

  static {
    try {
      CFLibDbHostAddr.initAddrHeader();
      for (int i = 0; i < CONCURRENT_DIGESTS; i++) {
        CFLibUuid6 u = CFLibUuid6.generateUuid6();
        hashBuffer[i] = ByteBuffer.allocate(TOTAL_BYTES);
        System.arraycopy(u.getBytes(), 0, hashBuffer, 0, CFLibUuid6.TOTAL_BYTES);
        hashBuffer[i].putLong(COUNTER_INDEX, counter);
        hashBuffer[i].putInt(MACHINE_INDEX, 1);
        hashBuffer[i].putInt(WGTID_INDEX, 1);
        m[i] = MessageDigest.getInstance(HASH_ALGO);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static byte[] bytesFromHex(String string) {
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

  public static CFLibDbKeyHash384 fromHex(String string) {
    byte[] b = bytesFromHex(string);
    CFLibDbKeyHash384 h = new CFLibDbKeyHash384();
    h.bytes = b;
    return h;
  }

  public static Comparator<CFLibDbKeyHash384> getComparator() {

    return new Comparator<CFLibDbKeyHash384>() {
      @Override
      public int compare(CFLibDbKeyHash384 a, CFLibDbKeyHash384 b) {
        return compareOrdered(a, b);
      }
    };
  }

  public CFLibDbKeyHash384() {
  }

  /**
   * This is the hex code of the underlying ID. THIS IS NOT A HASHING FUNCTION.
   */
  public CFLibDbKeyHash384(String hexId) {
    bytes = bytesFromHex(hexId);
  }

  public CFLibDbKeyHash384(byte[] anId) {
    if (anId == null) {
      // allowed
    }
    else if (anId.length > HASH_LENGTH) {
      throw new IllegalArgumentException("anId length must be <= " + HASH_LENGTH + ".");
    }
    bytes = new byte[HASH_LENGTH];
    if (anId != null) {
      System.arraycopy(anId, 0, bytes, 0, Math.min(anId.length, HASH_LENGTH));
    }
  }

  public CFLibDbKeyHash384(CFLibDbKeyHash384 otherKey) {
    if (otherKey == null) {
      bytes = new byte[HASH_LENGTH];
      return;
    }
    byte[] _newId = new byte[HASH_LENGTH];
    System.arraycopy(otherKey.getBytes(), 0, _newId, 0, HASH_LENGTH);
    this.bytes = _newId;
  }

  public CFLibDbKeyHash384(CFLibDbKeyHash512 otherKey) {
    if (otherKey == null) {
      bytes = new byte[HASH_LENGTH];
      return;
    }
    byte[] _newId = new byte[HASH_LENGTH];
    System.arraycopy(otherKey.getBytes(), 0, _newId, 0, HASH_LENGTH);
    this.bytes = _newId;
  }

  public static CFLibDbKeyHash384 fromInt(int v) {
    CFLibDbKeyHash384 h = nullGet();
    h.bytes[3] = (byte) (v & 0xFF);
    h.bytes[2] = (byte) ((v >> 8) & 0xFF);
    h.bytes[1] = (byte) ((v >> 16) & 0xFF);
    h.bytes[0] = (byte) ((v >> 24) & 0xFF);
    return h;
  }

  public CFLibDbKeyHash384(int notUsed) {
    int thid = (int) (Thread.currentThread().getId() % CONCURRENT_DIGESTS);
    synchronized (m[thid]) {
      while (true) {
        counter++;
        hashBuffer[thid].putLong(COUNTER_INDEX, counter);
        hashBuffer[thid].putInt(WGTID_INDEX, (int) (Math.random() * Integer.MAX_VALUE));
        m[thid].update(hashBuffer[thid].array(), 0, TOTAL_BYTES);

        bytes = m[thid].digest();

        // we want to reserve the bottom 32 bits of the counter for incremental temporary indexing so we regenerate entries that have the top 12 bytes as 0's */
        for (int i = 4; i < HASH_LENGTH; i++) {
          if (bytes[i] != 0) {
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
      result = (result << 8) | (0xFF & ((int) bytes[i]));
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
    CFLibDbKeyHash384 test = (CFLibDbKeyHash384) aTest;
    return Arrays.equals(bytes, test.bytes);
  }

  public int reduceToInt() {
    return hashCode();
  }

  public boolean isNull() {
    if (bytes != null) {
      for (int i = 0; i < HASH_LENGTH; i++) {
        if (bytes[i] != 0) {
          return false;
        }
      }
    }
    return true;
  }

  public static final boolean isNull(CFLibDbKeyHash384 anId) {
    return anId == null || anId.isNull();
  }

  static public void setMachineId(int id) {
    for (int i = 0; i < CONCURRENT_DIGESTS; i++) {
      hashBuffer[i].putInt(MACHINE_INDEX, id);
    }
  }

  public String asString() {
    if (bytes == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder(bytes.length * 2);
    for (int i = 0; i < HASH_LENGTH; i++) {
      sb.append(hexDigits.charAt((bytes[i] & 0xF0) >>> 4));
      sb.append(hexDigits.charAt(bytes[i] & 0x0F));
    }
    return sb.toString();
  }

  public static final String getJava(CFLibDbKeyHash384 id) {
    if (id == null) {
      return "null";
    }
    else {
      return String.format("new CFLibDbKeyHash384(\"%s\")", id.asString());
    }
  }

  public void asString(StringBuilder sb) {
    // Construct and return the representive hex string
    for (int i = 0; i < HASH_LENGTH; i++) {
      sb.append(hexDigits.charAt((bytes[i] & 0xF0) >>> 4));
      sb.append(hexDigits.charAt(bytes[i] & 0x0F));
    }
  }

  @Override
  public String toString() {
    return asString();
  }

  public byte[] getBytes() {
    return bytes;
  }

  /**
   * Get a new hash object with the key set to all 0s
   */
  static public CFLibDbKeyHash384 nullGet() {
    CFLibDbKeyHash384 k = new CFLibDbKeyHash384();
    k.bytes = new byte[HASH_LENGTH];
    return k;
  }

  static public String getNullString() {
    return "00000000000000000000000000000000";
  }

  /**
   * We want DbKeyHashXX to be immutable so this method shouldn't even exist;
   * however it is necessary for JPA. This is the only time it should be used.
   *
   * @param newBytes to be copied from.
   */
  public void setBytes(byte[] newBytes) {
    if (newBytes == null) {
      throw new NullPointerException("newBytes must not be null.");
    }
    if (newBytes.length != 16) {
      throw new IllegalArgumentException("newBytes must be of length 16.");
    }
    bytes = newBytes.clone();
  }

  /** Copy into existing key */
  public void setBytes(byte[] newBytes, int offset,  int length) {
      System.arraycopy(newBytes, offset, bytes, 0, Math.min(TOTAL_BYTES,length));
  }

  @Override
  public int compare(CFLibDbKeyHash384 h1, CFLibDbKeyHash384 h2) {
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
      int v1 = h1.bytes[i];
      int v2 = h2.bytes[i];
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

  @Override
  public int compareTo(CFLibDbKeyHash384 o) {
    int result = compare(this, o);
    return result;
  }

  static public int compareOrdered(CFLibDbKeyHash384 h1, CFLibDbKeyHash384 h2) {
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
      int v1 = h1.bytes[i] + 256;
      int v2 = h2.bytes[i] + 256;
      if (v1 < v2) return -1;
      if (v1 > v2) return 1;
    }
    return 0;
  }

  public static CFLibDbKeyHash384 hash(String text) {
    if (text != null) {
      try {
        MessageDigest md = MessageDigest.getInstance(HASH_ALGO);
        byte[] buf = text.getBytes("UTF-8");
        md.update(buf);

        return new CFLibDbKeyHash384(md.digest());
      }
      catch (Exception ex) {
      }
    }
    return new CFLibDbKeyHash384(0);
  }

  public static CFLibDbKeyHash384 hash(byte[] payload) {
    try {
      MessageDigest md = MessageDigest.getInstance(HASH_ALGO);
      md.update(payload);

      return new CFLibDbKeyHash384(md.digest());
    }
    catch (Exception ex) {
    }
    return new CFLibDbKeyHash384(0);
  }

  public static CFLibDbKeyHash384 hash(byte[]... payload) {
    try {
      MessageDigest md = MessageDigest.getInstance(HASH_ALGO);
      for (byte[] bs : payload) {
        md.update(bs);
      }

      return new CFLibDbKeyHash384(md.digest());
    }
    catch (Exception ex) {
    }
    return new CFLibDbKeyHash384(0);
  }

  public static CFLibDbKeyHash384 hash(CFLibDbKeyHash384... payload) {
    try {
      MessageDigest md = MessageDigest.getInstance(HASH_ALGO);
      for (CFLibDbKeyHash384 k : payload) {
        md.update(k.bytes);
      }
      return new CFLibDbKeyHash384(md.digest());
    }
    catch (Exception ex) {
    }
    return new CFLibDbKeyHash384(0);
  }

  public static CFLibDbKeyHash384 hash(int[] payload) {
    try {
      MessageDigest md = MessageDigest.getInstance(HASH_ALGO);
      for (int x : payload) {
        md.update((byte) ((x >>> 24) & 255));
        md.update((byte) ((x >>> 16) & 255));
        md.update((byte) ((x >>> 8) & 255));
        md.update((byte) (x & 255));
      }

      return new CFLibDbKeyHash384(md.digest());
    }
    catch (Exception ex) {
    }
    return new CFLibDbKeyHash384(0);
  }

  public byte[] fingerprint() {
    return bytes;
  }

  public CFLibDbKeyHash384 deepClone() {
    return new CFLibDbKeyHash384(this);
  }

  static public int getHashLength() {
    return HASH_LENGTH;
  }

  static public int getHashStringLength() {
    return HASH_LENGTH_STRING;
  }

  static public CFLibDbKeyHash384 fromHexQuick(String string) {
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

  public static final CFLibDbKeyHash384[] toDbKeyHash16(String[] ids) {
    if (ids == null) {
      return null;
    }
    if (ids.length == 0) {
      return new CFLibDbKeyHash384[0];
    }
    CFLibDbKeyHash384[] r = new CFLibDbKeyHash384[ids.length];
    for (int i = 0; i < ids.length; i++) {
      r[i] = new CFLibDbKeyHash384(ids[i]);
    }
    return r;
  }

  public static final List<CFLibDbKeyHash384> toDbKeyHash16List(String[] ids) {

    if (ids == null) {
      return null;
    }
    if (ids.length == 0) {
      return Collections.emptyList();
    }
    List<CFLibDbKeyHash384> r = new ArrayList<CFLibDbKeyHash384>(ids.length);
    for (int i = 0; i < ids.length; i++) {
      r.add(new CFLibDbKeyHash384(ids[i]));
    }
    return r;

  }

  public static final Set<CFLibDbKeyHash384> toDbKeyHash16Set(String[] ids) {

    if (ids == null) {
      return null;
    }
    if (ids.length == 0) {
      return Collections.emptySet();
    }
    Set<CFLibDbKeyHash384> r = new HashSet<CFLibDbKeyHash384>(ids.length);
    for (int i = 0; i < ids.length; i++) {
      r.add(new CFLibDbKeyHash384(ids[i]));
    }
    return r;

  }

  public static String getUString() {
    return new CFLibDbKeyHash384(23934).asString();
  }
}
