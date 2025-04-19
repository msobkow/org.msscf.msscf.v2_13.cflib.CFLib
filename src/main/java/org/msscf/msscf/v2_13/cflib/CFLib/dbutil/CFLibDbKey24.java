package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import jakarta.persistence.*;
import java.util.Arrays;

@Embeddable
public class CFLibDbKey24 extends CFLibDbKey implements Comparable<CFLibDbKey24> {

    @Convert(converter = CFLibDbKey24Converter.class)
    @Column(name = "bytes", nullable = false)
    private byte[] bytes;

    public static final int SUBKEY_LENGTH = 4;
    public static final int TOTAL_LENGTH = HEADER_LENGTH + SUBKEY_LENGTH;
    private static final long serialVersionUID = 202504160340L;

    public CFLibDbKey24() {
        if (!addrHeaderInitialized) {
            initAddrHeader();
        }
        this.bytes = null;
    }
    
    public CFLibDbKey24(boolean generate) {
        if (!addrHeaderInitialized) {
            initAddrHeader();
        }
        this.bytes = null;
        initBytes(SUBKEY_LENGTH);
        if (generate) {
            generate();
        }
    }
    
    public CFLibDbKey24(byte[] bytes) {
        if (!addrHeaderInitialized) {
            initAddrHeader();
        }
        this.bytes = null;
        initBytes(SUBKEY_LENGTH);
        setBytes(bytes);
    }

    @PrePersist
    private void ensureKeyInitialized() {
        if (!addrHeaderInitialized) {
            initAddrHeader();
        }
        if (isNull()) {
            generate();
        }
    }

    @Override
    public int compareTo(CFLibDbKey24 other) {
        if (this.bytes == null && other.bytes == null) {
            return 0;
        } else if (this.bytes == null) {
            return -1;
        } else if (other.bytes == null) {
            return 1;
        }
        for (int i = 0; i < Math.min(this.bytes.length, other.bytes.length); i++) {
            if (this.bytes[i] != other.bytes[i]) {
                return Byte.compare(this.bytes[i], other.bytes[i]);
            }
        }
        return Integer.compare(this.bytes.length, other.bytes.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CFLibDbKey24 dbKey24 = (CFLibDbKey24) o;
        return Arrays.equals(bytes, dbKey24.bytes);
    }

    @Override
    public int hashCode() {
        if (bytes == null) {
            return 0;
        }
        int hash = 7;
        for (int i = 0; i < bytes.length; i++) {
            hash = 31 * hash + bytes[i];
        }
        return hash;
    }

    protected void initBytes(int subkeySize) {
        if (subkeySize < 4) {
            throw new IllegalArgumentException("Subkey size must be at least 4 key");
        }
        if (subkeySize % 4 != 0) {
            throw new IllegalArgumentException("Subkey size must be a multiple of 4 key");
        }
        if (subkeySize > 4096 - IPV6_LENGTH) {
            throw new IllegalArgumentException("Subkey size must be less than or equal to " + (4096 - IPV6_LENGTH) + " key");
        }
        if (bytes == null) {
            bytes = new byte[HEADER_LENGTH + subkeySize];
            for (int i = 0; i < IPV4_LENGTH; i++) {
                bytes[i] = 0;
            }
            for (int i = IPV4_LENGTH; i < IPV6_LENGTH; i++) {
                bytes[i] = -1;
            }
            for (int i = IPV6_LENGTH; i < HEADER_LENGTH; i++) {
                bytes[i] = 0;
            }
//        System.arraycopy(addrHeader, 0, key, 0, IPV6_LENGTH);
            for (int i = HEADER_LENGTH; i < bytes.length; i++) {
                bytes[i] = 0;
            }
        } else if (bytes.length < IPV6_LENGTH + subkeySize) {
            throw new IllegalArgumentException("Existing bytes length must be at least " + (IPV6_LENGTH + subkeySize) + " key");
        } else {
            for (int i = 0; i < IPV4_LENGTH; i++) {
                bytes[i] = 0;
            }
            for (int i = IPV4_LENGTH; i < IPV6_LENGTH; i++) {
                bytes[i] = -1;
            }
            for (int i = IPV6_LENGTH; i < HEADER_LENGTH; i++) {
                bytes[i] = 0;
            }
//        System.arraycopy(addrHeader, 0, key, 0, IPV6_LENGTH);
            for (int i = HEADER_LENGTH; i < bytes.length; i++) {
                bytes[i] = 0;
            }
        }
    }

    public boolean isNull() {
        if (bytes == null) {
            return true;
        }
        for (int i = 0; i < IPV4_LENGTH; i++) {
            if (bytes[i] != 0) {
                return false;
            }
        }
        for (int i = IPV4_LENGTH; i < IPV6_LENGTH; i++) {
            if (bytes[i] != -1) {
                return false;
            };
        }
        for (int i = IPV6_LENGTH; i < HEADER_LENGTH; i++) {
            if (bytes[i] != 0) {
                return false;
            }
        }
//        System.arraycopy(addrHeader, 0, key, 0, IPV6_LENGTH);
        for (int i = HEADER_LENGTH; i < bytes.length; i++) {
            if (bytes[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public void setNull() {
        initBytes(bytes.length - HEADER_LENGTH);
    }

    public byte[] getBytes() {
        return bytes;
    }

    protected void setBytes(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("bytes cannot be null");
        }
        if (bytes.length < IPV6_LENGTH) {
            throw new IllegalArgumentException("bytes must be at least " + IPV6_LENGTH + " bytes long");
        }
        if (bytes.length % 4 != 0) {
            throw new IllegalArgumentException("bytes length must be a multiple of 4 bytes");
        }
        if (this.bytes == null) {
            this.bytes = new byte[bytes.length];
        } else if (this.bytes.length < bytes.length) {
            throw new IllegalArgumentException("Existing bytes length must be at least " + bytes.length + " bytes long");
        }
        System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
        for (int i = bytes.length; i < this.bytes.length; i++) {
            this.bytes[i] = 0;
        }
    }

    public CFLibDbKey24 generate() {
        if (bytes == null) {
            throw new IllegalStateException("bytes not initialized. Call initBytes() first.");
        }
        for (int i = 0; i < IPV6_LENGTH; i++) {
            bytes[i] = addrHeader[i];
        }
        long stamp = System.currentTimeMillis();
        for (int i = 0; i < STAMP_LENGTH; i++) {
            bytes[IPV6_LENGTH + i] = (byte) ((stamp >> (i * 8)) & 0xFF);
        }
        for (int i = STAMP_LENGTH; i < bytes.length; i++) {
            bytes[i] = (byte) (Math.random() * 256);
        }
        return this;
    }
}
