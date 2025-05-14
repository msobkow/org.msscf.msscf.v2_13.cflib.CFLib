package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author msobkow
 */
public abstract class CFLibDbKeyHashBase<T extends CFLibDbKeyHashBase<T>> implements Comparator<T>, Comparable<T> {

  static final String hexDigits = "0123456789abcdef";
  static final int UUID6_INDEX = 0;
  static final int UUID6_LENGTH = CFLibUuid6.TOTAL_BYTES;
  static final int COUNTER_INDEX = 28;
  static final int COUNTER_LENGTH = 8;
  static final int MACHINE_INDEX = 36;
  static final int MACHINE_LENGTH = 4;
  static final int PID_INDEX = 40;
  static final int PID_LENGTH = 8;
  static final int THREAD_INDEX = 48;
  static final int THREAD_LENGTH = 8;
  static final int HEADER_BYTES = 56;
  static final int RANDBYTES_INDEX = HEADER_BYTES;
  static final int RANDBYTES_LENGTH = 8;
  static final int TOTAL_BYTES = HEADER_BYTES + RANDBYTES_LENGTH;

  public abstract int getHashLength();
  public int getHashLengthString() {
    return getHashLength() * 2;
  }
  public abstract String getHashAlgo();

  public abstract byte[] getBytes();
  public abstract void setBytes(byte[] bytes);
  public abstract void setBytes(byte[] newBytes, int offset,  int length);

  static final int CONCURRENT_DIGESTS = Runtime.getRuntime().availableProcessors() * 2;
  static ByteBuffer[] hashBuffer = null;
  static volatile int rotator = 0;
  static volatile long counter = 1;

  public void initStatics() {
    if (hashBuffer != null) {
      return;
    }
    try {
      hashBuffer = new ByteBuffer[CONCURRENT_DIGESTS];
      CFLibDbHostAddr.initAddrHeader();
      long pid = ProcessHandle.current().pid();
      long tid = Thread.currentThread().getId();
      for (int i = 0; i < CONCURRENT_DIGESTS; i++) {
        CFLibUuid6 u = CFLibUuid6.generateUuid6();
        hashBuffer[i] = ByteBuffer.allocate(TOTAL_BYTES);
        byte[] uub = u.getBytes();
        for (int j = 0; j <  CFLibUuid6.TOTAL_BYTES; j++) {
          hashBuffer[i].put(uub[j]);
        }
        hashBuffer[i].putLong(COUNTER_INDEX, counter);
        hashBuffer[i].putInt(MACHINE_INDEX, 1);
        hashBuffer[i].putLong(PID_INDEX, pid);
        hashBuffer[i].putLong(THREAD_INDEX, tid);
        hashBuffer[i].putLong(RANDBYTES_INDEX, 1);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected abstract MessageDigest[] getM();

  public final byte[] bytesFromHex(String string) {
    if (string == null) {
      // allowed
    }
    else if (string.length() > getHashLength() * 2) {
      throw new IllegalArgumentException("string length is " + string.length() + ".  Must be <= " + getHashLength() * 2 + ".  string is '" + string + "'.");
    }
    byte[] b = new byte[getHashLength()];
    if (string == null) {
      return b;
    }

    int n = string.length();
    for (int i = 0; i < n; i += 2) {
      b[i / 2] = (byte) ((Character.digit(string.charAt(i), 16) << 4) + Character.digit(string.charAt(i + 1), 16));
    }
    return b;
  }

  public CFLibDbKeyHashBase() {
  }

  /**
   * This is the hex code of the underlying ID. THIS IS NOT A HASHING FUNCTION.
   */
  public CFLibDbKeyHashBase(String hexId) {
    setBytes(bytesFromHex(hexId));
  }

  public CFLibDbKeyHashBase(byte[] anId) {
    if (anId == null) {
      // allowed
    }
    else if (anId.length > getHashLength()) {
      throw new IllegalArgumentException("anId length must be <= " + getHashLength() + ".");
    }
    setBytes(new byte[getHashLength()]);
    if (anId != null) {
      System.arraycopy(anId, 0, getBytes(), 0, Math.min(anId.length, getHashLength()));
    }
  }

  public CFLibDbKeyHashBase(T otherKey) {
    if (otherKey == null) {
      setBytes(new byte[getHashLength()]);
      return;
    }
    byte[] _newId = new byte[getHashLength()];
    System.arraycopy(otherKey.getBytes(), 0, _newId, 0, getHashLength());
    setBytes(_newId);
  }

  public CFLibDbKeyHashBase(int notUsed) {
    initStatics();
    int thid = (int) (Math.abs(rotator++) % CONCURRENT_DIGESTS);
    synchronized (getM()[thid]) {
      while (true) {
        counter++;
        hashBuffer[thid].putLong(COUNTER_INDEX, counter);
        hashBuffer[thid].putLong(THREAD_INDEX, Thread.currentThread().getId());
        hashBuffer[thid].putLong(RANDBYTES_INDEX, (long) (Math.random() * Long.MAX_VALUE));
        getM()[thid].update(hashBuffer[thid].array(), 0, TOTAL_BYTES);

        setBytes(getM()[thid].digest());

        // we want to reserve the bottom 32 bits of the counter for incremental temporary indexing so we regenerate entries that have the top 12 bytes as 0's */
        byte[] b = getBytes();
        for (int i = 4; i < getHashLength(); i++) {
          if (b[i] != 0) {
            return;
          }
        }
      }
    }
  }

  public int hashCode() {
    int result = 0;
    byte[] b = getBytes();
    for (int i = 3; i >= 0; i--) {
      //result = (result << 8) | id16[i];
      result = (result << 8) | (0xFF & ((int) b[i]));
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
    T test = (T) aTest;
    return Arrays.equals(this.getBytes(), test.getBytes());
  }

  public int reduceToInt() {
    return hashCode();
  }

  public boolean isNull() {
    byte[] b = getBytes();
    if (b != null) {
      for (int i = 0; i < getHashLength(); i++) {
        if (b[i] != 0) {
          return false;
        }
      }
    }
    return true;
  }

  static public void setMachineId(int id) {
    for (int i = 0; i < CONCURRENT_DIGESTS; i++) {
      hashBuffer[i].putInt(MACHINE_INDEX, id);
    }
  }

  public String asString() {
    byte[] b = getBytes();
    if (b == null) {
      return "null";
    }
    StringBuilder sb = new StringBuilder(b.length * 2);
    for (int i = 0; i < getHashLength(); i++) {
      sb.append(hexDigits.charAt((b[i] & 0xF0) >>> 4));
      sb.append(hexDigits.charAt(b[i] & 0x0F));
    }
    return sb.toString();
  }

  public void asString(StringBuilder sb) {
    // Construct and return the representive hex string
    byte[] b = getBytes();
    for (int i = 0; i < getHashLength(); i++) {
      sb.append(hexDigits.charAt((b[i] & 0xF0) >>> 4));
      sb.append(hexDigits.charAt(b[i] & 0x0F));
    }
  }

  @Override
  public String toString() {
    return asString();
  }

  @Override
  public int compare(T h1, T h2) {
    if (h1 == null && h2 != null) {
      return 1;
    }
    if (h2 == null && h1 != null) {
      return -1;
    }
    if (h2 == null && h1 == null) {
      return 0;
    }
    byte[] b1 = h1.getBytes();
    byte[] b2 = h2.getBytes();
    for (int i = 0; i < h1.getHashLength(); i++) {
      int v1 = b1[i];
      int v2 = b2[i];
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
  public int compareTo(T o) {
    int result = compare((T)this, o);
    return result;
  }

  public byte[] fingerprint() {
    return getBytes();
  }

  public abstract T deepClone();
}
