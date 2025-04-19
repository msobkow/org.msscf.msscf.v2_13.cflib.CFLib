package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import jakarta.persistence.*;
import java.util.Arrays;

@Embeddable
public class DbKey24 extends DbKey implements Comparable<DbKey24> {

    @Convert(converter = DbKey24Converter.class)
    @Column(name = "key", nullable = false)
    private byte[] key;

    public static final int SUBKEY_LENGTH = 4;
    public static final int TOTAL_LENGTH = HEADER_LENGTH + SUBKEY_LENGTH;
    private static final long serialVersionUID = 202504160340L;

    public DbKey24() {
        if (!addrHeaderInitialized) {
            initAddrHeader();
        }
        this.key = null;
    }
    
    public DbKey24(boolean generate) {
        if (!addrHeaderInitialized) {
            initAddrHeader();
        }
        this.key = null;
        initKey(SUBKEY_LENGTH);
        if (generate) {
            generate();
        }
    }
    
    public DbKey24(byte[] key) {
        if (!addrHeaderInitialized) {
            initAddrHeader();
        }
        this.key = null;
        initKey(SUBKEY_LENGTH);
        setKey(key);
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
    public int compareTo(DbKey24 other) {
        if (this.key == null && other.key == null) {
            return 0;
        } else if (this.key == null) {
            return -1;
        } else if (other.key == null) {
            return 1;
        }
        for (int i = 0; i < Math.min(this.key.length, other.key.length); i++) {
            if (this.key[i] != other.key[i]) {
                return Byte.compare(this.key[i], other.key[i]);
            }
        }
        return Integer.compare(this.key.length, other.key.length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbKey24 dbKey24 = (DbKey24) o;
        return Arrays.equals(key, dbKey24.key);
    }

    @Override
    public int hashCode() {
        if (key == null) {
            return 0;
        }
        int hash = 7;
        for (int i = 0; i < key.length; i++) {
            hash = 31 * hash + key[i];
        }
        return hash;
    }

    protected void initKey(int subkeySize) {
        if (subkeySize < 4) {
            throw new IllegalArgumentException("Subkey size must be at least 4 key");
        }
        if (subkeySize % 4 != 0) {
            throw new IllegalArgumentException("Subkey size must be a multiple of 4 key");
        }
        if (subkeySize > 4096 - IPV6_LENGTH) {
            throw new IllegalArgumentException("Subkey size must be less than or equal to " + (4096 - IPV6_LENGTH) + " key");
        }
        if (key == null) {
            key = new byte[HEADER_LENGTH + subkeySize];
            for (int i = 0; i < IPV4_LENGTH; i++) {
                key[i] = 0;
            }
            for (int i = IPV4_LENGTH; i < IPV6_LENGTH; i++) {
                key[i] = -1;
            }
            for (int i = IPV6_LENGTH; i < HEADER_LENGTH; i++) {
                key[i] = 0;
            }
//        System.arraycopy(addrHeader, 0, key, 0, IPV6_LENGTH);
            for (int i = HEADER_LENGTH; i < key.length; i++) {
                key[i] = 0;
            }
        } else if (key.length < IPV6_LENGTH + subkeySize) {
            throw new IllegalArgumentException("Existing key length must be at least " + (IPV6_LENGTH + subkeySize) + " key");
        } else {
            for (int i = 0; i < IPV4_LENGTH; i++) {
                key[i] = 0;
            }
            for (int i = IPV4_LENGTH; i < IPV6_LENGTH; i++) {
                key[i] = -1;
            }
            for (int i = IPV6_LENGTH; i < HEADER_LENGTH; i++) {
                key[i] = 0;
            }
//        System.arraycopy(addrHeader, 0, key, 0, IPV6_LENGTH);
            for (int i = HEADER_LENGTH; i < key.length; i++) {
                key[i] = 0;
            }
        }
    }

    public boolean isNull() {
        if (key == null) {
            return true;
        }
        for (int i = 0; i < IPV4_LENGTH; i++) {
            if (key[i] != 0) {
                return false;
            }
        }
        for (int i = IPV4_LENGTH; i < IPV6_LENGTH; i++) {
            if (key[i] != -1) {
                return false;
            };
        }
        for (int i = IPV6_LENGTH; i < HEADER_LENGTH; i++) {
            if (key[i] != 0) {
                return false;
            }
        }
//        System.arraycopy(addrHeader, 0, key, 0, IPV6_LENGTH);
        for (int i = HEADER_LENGTH; i < key.length; i++) {
            if (key[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public void setNull() {
        initKey(key.length - HEADER_LENGTH);
    }

    public byte[] getKey() {
        return key;
    }

    protected void setKey(byte[] key) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        if (key.length < IPV6_LENGTH) {
            throw new IllegalArgumentException("key must be at least " + IPV6_LENGTH + " key long");
        }
        if (key.length % 4 != 0) {
            throw new IllegalArgumentException("key length must be a multiple of 4 key");
        }
        if (this.key == null) {
            this.key = new byte[key.length];
        } else if (this.key.length < key.length) {
            throw new IllegalArgumentException("Existing key length must be at least " + key.length + " key long");
        }
        System.arraycopy(key, 0, this.key, 0, key.length);
        for (int i = key.length; i < this.key.length; i++) {
            this.key[i] = 0;
        }
    }

    public DbKey24 generate() {
        if (key == null) {
            throw new IllegalStateException("Key not initialized. Call initkey() first.");
        }
        for (int i = 0; i < IPV6_LENGTH; i++) {
            key[i] = addrHeader[i];
        }
        long stamp = System.currentTimeMillis();
        for (int i = 0; i < STAMP_LENGTH; i++) {
            key[IPV6_LENGTH + i] = (byte) ((stamp >> (i * 8)) & 0xFF);
        }
        for (int i = STAMP_LENGTH; i < key.length; i++) {
            key[i] = (byte) (Math.random() * 256);
        }
        return this;
    }
}
