/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 *
 * @author msobkow
 */
@Embeddable
public class CFLibUuid6 implements java.io.Serializable, Comparable<CFLibUuid6> {
    public static final int IPV6_LENGTH = 16;
    public static final int IPV4_LENGTH = 4;
    public static final int IPV4_PAD = IPV6_LENGTH - IPV4_LENGTH;

    public final static int STAMP_START = 0;
    public final static int STAMP_BYTES = 6;
    public final static int VERSION_AND_VARIANT_START = STAMP_BYTES;
    public final static int VERSION_AND_VARIANT_BYTES = 2;
    public final static int SEQUENCE_START = VERSION_AND_VARIANT_START + VERSION_AND_VARIANT_BYTES;
    public final static int SEQUENCE_BYTES = 4;
    public final static int NODE0_START = SEQUENCE_START + SEQUENCE_BYTES;
    public final static int NODE0_BYTES = 4;
    public final static int NODE1_START = NODE0_START + NODE0_BYTES;
    public final static int NODE1_BYTES = 4;
    public final static int NODE2_START = NODE1_START + NODE1_BYTES;
    public final static int NODE2_BYTES = 4;
    public final static int NODE3_START = NODE2_START + NODE2_BYTES;
    public final static int NODE3_BYTES = 4;
    /**
     * Uuid6 values are 28 bytes long, representable as strings for transport
     */
    public final static int NUM_BYTES = NODE3_START + NODE3_BYTES;
    /**
     * String format for Uuid6 values is 62 characters long
     */
    public final static int STRING_LENGTH = (NUM_BYTES * 2) + 6;
    
    private static volatile int seqGen = 0;

    /*
     * The random number generator used by this class to create random
     * based UUIDs. In a holder class to defer initialization until needed.
     */
    private static class Holder {
        static final SecureRandom numberGenerator = new SecureRandom();
    }
    
    /**
     * Explicit serialVersionUID for interoperability.
     */
    @java.io.Serial
    private static final long serialVersionUID = 202504190939L;

    @Convert(converter = CFLibUuid6Converter.class)
    @Column(name = "bytes", nullable = false)
    private final byte[] bytes = new byte[NUM_BYTES];

    public byte[] getBytes() {
        return bytes;
    }
    
    private static boolean addrHeaderInitialized = false;
    private static byte[] addrHeader = new byte[IPV6_LENGTH];
    static {
        for (int i = 0; i < IPV4_LENGTH; i++) {
            addrHeader[i] = 0;
        }
        for (int i = IPV4_LENGTH; i < IPV6_LENGTH; i++) {
            addrHeader[i] = -1;
        }
    }

    public static boolean isAddrHeaderInitialized() {
        if (addrHeaderInitialized) {
            return true;
        }
    
        // Check if addrHeader is already initialized
        for (int i = 0; i < IPV4_LENGTH; i++) {
            if (addrHeader[i] != 0) {
                addrHeaderInitialized = true;
                break;
            }
        }

        if (!addrHeaderInitialized) {
            for (int i = IPV4_LENGTH; i < IPV6_LENGTH; i++) {
                if (addrHeader[i] != -1) {
                addrHeaderInitialized = true;
                break;
                }
            }
        }

        return addrHeaderInitialized;
    }

    public static boolean hasIPv4AddrHeader() {
        if (!isAddrHeaderInitialized()) {
            return false;
        }
        boolean hasIPv4Part = false;
        for (int i = 0; i < IPV4_LENGTH; i++) {
            if (addrHeader[i] != 0) {
                hasIPv4Part = true;
                break;
            }
        }
        for (int i = IPV4_LENGTH; i < IPV6_LENGTH; i++) {
            if (addrHeader[i] != -1) {
                return false;
            }
        }
        return hasIPv4Part;
    }

    public static boolean hasIPv6AddrHeader() {
        if (!isAddrHeaderInitialized()) {
            return false;
        }
        boolean hasIPv4Part = false;
        for (int i = 0; i < IPV4_LENGTH; i++) {
            if (addrHeader[i] != 0) {
                hasIPv4Part = true;
                break;
            }
        }
        for (int i = IPV4_LENGTH; i < IPV6_LENGTH; i++) {
            if (addrHeader[i] != -1) {
                return hasIPv4Part;
            }
        }
        return false;
    }
    
    public static void initAddrHeader() {
        initAddrHeader(false);
    }

    public static void initAddrHeader(boolean reinit) {
        if (isAddrHeaderInitialized() && !reinit) {
            return;
        }

        // Try to initialize addrHeader with the server's IP address
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            byte[] address = inetAddress.getAddress();

            if (address.length == IPV6_LENGTH) {
                System.arraycopy(address, 0, addrHeader, 0, IPV6_LENGTH);
                addrHeaderInitialized = true;
            } else if (address.length == IPV4_LENGTH) {
                System.arraycopy(address, 0, addrHeader, 0, IPV4_LENGTH);
                for (int i = IPV4_LENGTH; i < IPV6_LENGTH; i++) {
                    addrHeader[i] = -1;
                }
                addrHeaderInitialized = true;
            }
        } catch (UnknownHostException e) {
            // Fallback to IPv4 127.0.0.1 (loopback) in worst case
            addrHeader[0] = 127;
            addrHeader[1] = 0;
            addrHeader[2] = 0;
            addrHeader[3] = 1;
            for (int i = IPV4_LENGTH; i < IPV6_LENGTH; i++) {
                addrHeader[i] = -1;
            }
            addrHeaderInitialized = true;
        }
    }

    // Constructors and Factories

    /*
     * JPA needs access to this formerly private constructor which uses a byte array to construct the new Uuid6.
     */
    public CFLibUuid6(byte[] data) {
        assert data != null;
        assert data.length >= NUM_BYTES;
        for (int i = 0; i < NUM_BYTES; i++) {
            this.bytes[i] = data[i];
        }
    }

    /**
     * Static factory to generate a Uuid6.
     *
     * @return  A newly initialized Uuid6
     */
    public static CFLibUuid6 generateUuid6() {
        initAddrHeader();
        byte[] genBytes = new byte[NUM_BYTES];
        long ts = System.currentTimeMillis() >> 4;
        genBytes[STAMP_START] = (byte)((ts >> (5*8))&0xff);
        genBytes[STAMP_START+1] = (byte)((ts >> (4*8))&0xff);
        genBytes[STAMP_START+2] = (byte)((ts >> (3*8))&0xff);
        genBytes[STAMP_START+3] = (byte)((ts >> (2*8))&0xff);
        genBytes[STAMP_START+4] = (byte)((ts >> (1*8))&0xff);
        genBytes[STAMP_START+5] = (byte)((ts)&0xff);
        genBytes[VERSION_AND_VARIANT_START] = (byte)0x10;
        genBytes[VERSION_AND_VARIANT_START+1] = (byte)0x80;
        int sq = seqGen++;
        genBytes[SEQUENCE_START] = (byte)((sq>>(3*8))&0xff);
        genBytes[SEQUENCE_START+1] = (byte)((sq>>(2*8))&0xff);
        genBytes[SEQUENCE_START+2] = (byte)((sq>>8)&0xff);
        genBytes[SEQUENCE_START+3] = (byte)(sq&0xff);
        genBytes[NODE0_START] = addrHeader[0];
        genBytes[NODE0_START+1] = addrHeader[1];
        genBytes[NODE0_START+2] = addrHeader[2];
        genBytes[NODE0_START+3] = addrHeader[3];
        genBytes[NODE1_START] = addrHeader[4];
        genBytes[NODE1_START+1] = addrHeader[5];
        genBytes[NODE1_START+2] = addrHeader[6];
        genBytes[NODE1_START+3] = addrHeader[7];
        genBytes[NODE2_START] = addrHeader[8];
        genBytes[NODE2_START+1] = addrHeader[9];
        genBytes[NODE2_START+2] = addrHeader[10];
        genBytes[NODE2_START+3] = addrHeader[11];
        genBytes[NODE3_START] = addrHeader[12];
        genBytes[NODE3_START+1] = addrHeader[13];
        genBytes[NODE3_START+2] = addrHeader[14];
        genBytes[NODE3_START+3] = addrHeader[15];
        return new CFLibUuid6(genBytes);
    }

    /**
     * Static factory to retrieve a type 4 (pseudo randomly generated) UUID.
     *
     * The {@code UUID} is generated using a cryptographically strong pseudo
     * random number generator.
     *
     * @return  A randomly generated {@code UUID}
     */
    public static CFLibUuid6 randomUuid6() {
        SecureRandom ng = Holder.numberGenerator;

        byte[] randomBytes = new byte[NUM_BYTES];
        ng.nextBytes(randomBytes);
        randomBytes[VERSION_AND_VARIANT_START] &= 0x0f;
        randomBytes[VERSION_AND_VARIANT_START] |= 0x40;
        randomBytes[VERSION_AND_VARIANT_START+1] &= 0x3f;
        randomBytes[VERSION_AND_VARIANT_START+1] |= 0x80;
        return new CFLibUuid6(randomBytes);
    }

    private static final char[] HEXFORMAT;
    static {
        char[] hf = new char[16];
        hf[0] = '0';
        hf[1] = '1';
        hf[2] = '2';
        hf[3] = '3';
        hf[4] = '4';
        hf[5] = '5';
        hf[6] = '6';
        hf[7] = '7';
        hf[8] = '8';
        hf[9] = '9';
        hf[10] = 'a';
        hf[11] = 'b';
        hf[12] = 'c';
        hf[13] = 'd';
        hf[14] = 'e';
        hf[15] = 'f';
        HEXFORMAT = hf;
    }
    
    private static final byte[] NIBBLES;
    static {
        byte[] ns = new byte[256];
        Arrays.fill(ns, (byte) -1);
        ns['0'] = 0;
        ns['1'] = 1;
        ns['2'] = 2;
        ns['3'] = 3;
        ns['4'] = 4;
        ns['5'] = 5;
        ns['6'] = 6;
        ns['7'] = 7;
        ns['8'] = 8;
        ns['9'] = 9;
        ns['A'] = 10;
        ns['B'] = 11;
        ns['C'] = 12;
        ns['D'] = 13;
        ns['E'] = 14;
        ns['F'] = 15;
        ns['a'] = 10;
        ns['b'] = 11;
        ns['c'] = 12;
        ns['d'] = 13;
        ns['e'] = 14;
        ns['f'] = 15;
        NIBBLES = ns;
    }

    private static byte parseHexByte(String name, int pos) {
        byte[] ns = NIBBLES;
        char ch1 = name.charAt(pos);
        char ch2 = name.charAt(pos+1);
        int v1 = ch1 > 0xff ? -1 : ns[ch1];
        int v2 = ch2 > 0xff ? -1 : ns[ch2];
        if (v1 < 0 || v2 < 0) {
            throw new IllegalArgumentException("Uuid6 string does not follow valid format");
        }
        return (byte)(v1 << 4 | v2);
    }
    
    private static String formatHexByte(byte b) {
        return "" + HEXFORMAT[(b>>8)&0x0f] + HEXFORMAT[(b)&0x0f];
    }
  
    /**
     * Creates a {@code Uuid6} from the string standard representation as
     * described in the {@link #toString} method.
     *
     * @param  name
     *         A string that specifies a {@code Uuid6}
     *
     * @return  A {@code Uuid6} with the specified value
     *
     * @throws  IllegalArgumentException
     *          If name does not conform to the string representation as
     *          described in {@link #toString}
     *
     */
    public static CFLibUuid6 fromString(String name) {
        if (name.length() == STRING_LENGTH) {
            char ch1 = name.charAt(11);
            char ch2 = name.charAt(16);
            char ch3 = name.charAt(25);
            char ch4 = name.charAt(34);
            char ch5 = name.charAt(43);
            char ch6 = name.charAt(52);
            byte[] v = new byte[NUM_BYTES];
            if (ch1 == '-' && ch2 == '-' && ch3 == '-' && ch4 == '-' && ch5 == '-' && ch6 == '-') {
                // STAMP_BYTES
                v[0] = parseHexByte(name,0);
                v[1] = parseHexByte(name,2);
                v[2] = parseHexByte(name,4);
                v[3] = parseHexByte(name,6);
                v[4] = parseHexByte(name,8);
                v[5] = parseHexByte(name,10);
                // VERSION_AND_VARIANT_BYTES
                v[6] = parseHexByte(name,13);
                v[7] = parseHexByte(name,15);
                // SEQUENCE_BYTES
                v[8] = parseHexByte(name,17);
                v[9] = parseHexByte(name,19);
                v[10] = parseHexByte(name,21);
                v[11] = parseHexByte(name,23);
                // NODE0_BYTES
                v[12] = parseHexByte(name,26);
                v[13] = parseHexByte(name,28);
                v[14] = parseHexByte(name,30);
                v[15] = parseHexByte(name,32);
                // NODE1_BYTES
                v[16] = parseHexByte(name,35);
                v[17] = parseHexByte(name,37);
                v[18] = parseHexByte(name,39);
                v[19] = parseHexByte(name,41);
                // NODE2_BYTES
                v[20] = parseHexByte(name,44);
                v[21] = parseHexByte(name,46);
                v[22] = parseHexByte(name,48);
                v[23] = parseHexByte(name,50);
                // NODE3_BYTES
                v[24] = parseHexByte(name,53);
                v[25] = parseHexByte(name,55);
                v[26] = parseHexByte(name,57);
                v[27] = parseHexByte(name,59);

                if (v[0] != 0 || v[1] != 0 || v[2] != 0 || v[3] != 0 || v[4] != 0
                    || v[5] != 0 || v[6] != 0 || v[7] != 0 || v[8] != 0 || v[9] != 0
                    || v[10] != 0 || v[11] != 0 || v[12] != 0 || v[13] != 0 || v[14] != 0
                    || v[15] != 0 || v[16] != 0 || v[17] != 0 || v[18] != 0 || v[19] != 0
                    || v[20] != 0 || v[21] != 0 || v[22] != 0 || v[23] != 0 || v[24] != 0
                    || v[25] != 0)
                {
                    return new CFLibUuid6(v);
                }
            }
            else {
                throw new IllegalArgumentException("Uuid6 string does not follow valid format");
            }
        }
        return fromString1(name);
    }
    
    private static CFLibUuid6 fromString1(String name) {
        int len = name.length();
        if (len > STRING_LENGTH) {
            throw new IllegalArgumentException("Uuid6 string too large");
        }

        int dash1 = name.indexOf('-');
        int dash2 = name.indexOf('-', dash1 + 1);
        int dash3 = name.indexOf('-', dash2 + 1);
        int dash4 = name.indexOf('-', dash3 + 1);
        int dash5 = name.indexOf('-', dash4 + 1);
        int dash6 = name.indexOf('-', dash5 + 1);
        int dash7 = name.indexOf('-', dash6 + 1);

        if (dash1 < 0 || dash2 < 0 || dash3 < 0 || dash4 < 0 | dash5 < 0 | dash6 < 0 | dash7 >= 0) {
            throw new IllegalArgumentException("Invalid Uuid6 string: " + name);
        }

        MessageDigest md;
        try {
            // Try to get as close to 26 bytes as possible; SHA-224 is 224 bits, or 28 bytes, so we only lose two bytes of entropy
            md = MessageDigest.getInstance("SHA-224");
        } catch (NoSuchAlgorithmException nsae) {
            try {
                md = MessageDigest.getInstance("SHA-256");
            }
            catch (NoSuchAlgorithmException nope) {
                throw new InternalError("SHA-224 and SHA-256 not supported", nope);
            }
        }

        byte[] rawBytes;
        try {
            rawBytes = name.getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException ex) {
            rawBytes = name.getBytes();
        }
        
        byte[] shaBytes = md.digest(rawBytes);
        shaBytes[VERSION_AND_VARIANT_START]      &= 0x0f;  /* clear version        */
        shaBytes[VERSION_AND_VARIANT_START]      |= 0x40;  /* set to version 4     */
        shaBytes[VERSION_AND_VARIANT_START+1]    &= 0x3f;  /* clear variant        */
        shaBytes[VERSION_AND_VARIANT_START+1]    |= (byte) 0x80;  /* set to IETF variant  */
        return new CFLibUuid6(shaBytes);

    }

    /**
     * Static factory to retrieve a type 3 (name based) {@code UUID} based on
     * the specified byte array.
     *
     * @param  name
     *         A byte array to be used to construct a {@code UUID}
     *
     * @return  A {@code UUID} generated from the specified array
     */
    public static CFLibUuid6 nameUuid6FromBytes(byte[] name) {
        MessageDigest md;
        try {
            // Try to get as close to 26 bytes as possible; SHA-224 is 224 bits, or 28 bytes, so we only lose two bytes of entropy
            md = MessageDigest.getInstance("SHA-224");
        } catch (NoSuchAlgorithmException nsae) {
            try {
                md = MessageDigest.getInstance("SHA-256");
            }
            catch (NoSuchAlgorithmException nope) {
                throw new InternalError("SHA-224 and SHA-256 not supported", nope);
            }
        }
        byte[] shaBytes = md.digest(name);
        shaBytes[VERSION_AND_VARIANT_START]      &= 0x0f;  /* clear version        */
        shaBytes[VERSION_AND_VARIANT_START]      |= 0x40;  /* set to version 4     */
        shaBytes[VERSION_AND_VARIANT_START+1]    &= 0x3f;  /* clear variant        */
        shaBytes[VERSION_AND_VARIANT_START+1]    |= (byte) 0x80;  /* set to IETF variant  */
        return new CFLibUuid6(shaBytes);
    }

    /**
     * The version number associated with this {@code Uuid6}.  The version
     * number describes how this {@code Uuid6} was generated.
     *
     * The version number has the following meaning:
     * <ul>
     * <li>1    Time-based Uuid6
     * <li>2    DCE security Uuid6
     * <li>3    Name-based Uuid6
     * <li>4    Randomly generated Uuid6
     * </ul>
     *
     * @return  The version number of this {@code Uuid6}
     */
    public int version() {
        return (bytes[VERSION_AND_VARIANT_START] & 0xf0) >> 4;
    }

    /**
     * The variant number associated with this {@code Uuid6}.  The variant
     * number describes the layout of the {@code Uuid6}.
     *
     * The variant number has the following meaning:
     * <ul>
     * <li>0    Reserved for NCS backward compatibility
     * <li>2    <a href="http://www.ietf.org/rfc/rfc4122.txt">IETF&nbsp;RFC&nbsp;4122</a>
     * (Leach-Salz), used by this class
     * <li>6    Reserved, Microsoft Corporation backward compatibility
     * <li>7    Reserved for future definition
     * </ul>
     *
     * @return  The variant number of this {@code Uuid6}
     *
     * @spec https://www.rfc-editor.org/info/rfc4122
     *      RFC 4122: A Universally Unique IDentifier (Uuid6) URN Namespace
     */
    public int variant() {
        // This field is composed of a varying number of bits.
        // 0    -    -    Reserved for NCS backward compatibility
        // 1    0    -    The IETF aka Leach-Salz variant (used by this class)
        // 1    1    0    Reserved, Microsoft backward compatibility
        // 1    1    1    Reserved for future definition.
        return bytes[VERSION_AND_VARIANT_START+1] & 0x0f;
    }

    /**
     * The timestamp value associated with this Uuid6.
     *
     * <p> The 60 bit timestamp value is constructed from the time_low,
     * time_mid, and time_hi fields of this {@code Uuid6}.  The resulting
     * timestamp is measured in 100-nanosecond units since midnight,
     * October 15, 1582 UTC.
     *
     * <p> The timestamp value is only meaningful in a time-based Uuid6, which
     * has version type 1.  If this {@code Uuid6} is not a time-based Uuid6 then
     * this method throws UnsupportedOperationException.
     *
     * @throws UnsupportedOperationException
     *         If this Uuid6 is not a version 1 Uuid6
     * @return The timestamp of this {@code Uuid6}.
     */
    public long timestamp() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based Uuid6");
        }

        return ((long)(bytes[STAMP_START]) << (5*8)
                | (long)(bytes[STAMP_START+1]) << (4*8)
                | (long)(bytes[STAMP_START+2]) << (3*8)
                | (long)(bytes[STAMP_START+3]) << (2*8)
                | (long)(bytes[STAMP_START+4]) << (1*8)
                | (long)(bytes[STAMP_START+5])) << 4;
    }

    /**
     * The clock sequence value associated with this Uuid6.
     *
     * <p> The 32 bit clock sequence value is constructed from the clock
     * sequence field of this Uuid6.  The clock sequence field is used to
     * guarantee temporal uniqueness in a time-based Uuid6.
     *
     * <p> The {@code clockSequence} value is only meaningful in a time-based
     * Uuid6, which has version type 1.  If this Uuid6 is not a time-based Uuid6
     * then this method throws UnsupportedOperationException.
     *
     * @return  The clock sequence of this {@code Uuid6}
     *
     * @throws  UnsupportedOperationException
     *          If this Uuid6 is not a version 1 Uuid6
     */
    public int clockSequence() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based Uuid6");
        }

        return (((int)bytes[SEQUENCE_START] << (3*8)) & 0xff000000)
                | (((int)bytes[SEQUENCE_START+1] << (2*8)) & 0xff0000)
                | (((int)bytes[SEQUENCE_START+2] << 8) & 0xff00)
                | (((int)bytes[SEQUENCE_START+3]) & 0xff);
    }

    /**
     * The node values associated with this Uuid6.
     */
    public int node0() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based Uuid6");
        }

        return (int)(bytes[NODE0_START]) << (3*8)
                | (int)(bytes[NODE0_START+1]) << (2*8)
                | (int)(bytes[NODE0_START+2]) << 8
                | (int)(bytes[NODE0_START+3]);
    }

    public int node1() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based Uuid6");
        }

        return (int)(bytes[NODE1_START]) << (3*8)
                | (int)(bytes[NODE1_START+1]) << (2*8)
                | (int)(bytes[NODE1_START+2]) << 8
                | (int)(bytes[NODE1_START+3]);
    }
    
    public int node2() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based Uuid6");
        }

        return (int)(bytes[NODE2_START]) << (3*8)
                | (int)(bytes[NODE2_START+1]) << (2*8)
                | (int)(bytes[NODE2_START+2]) << 8
                | (int)(bytes[NODE2_START+3]);
    }
    
    public int node3() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based Uuid6");
        }

        return (int)(bytes[NODE3_START]) << (3*8)
                | (int)(bytes[NODE3_START+1]) << (2*8)
                | (int)(bytes[NODE3_START+2]) << 8
                | (int)(bytes[NODE3_START+3]);
    }

    // Object Inherited Methods

    /**
     * Returns a {@code String} object representing this {@code Uuid6}.
     *
     * <p> The Uuid6 string representation is as described by this BNF:
     * <blockquote><pre>
     * {@code
     * Uuid6                   = <time_stamp> "-"
     *                          <version_and_variant> "-"
     *                          <sequence> "-"
     *                          <node0> "-" <node1> "-" <node2> "-" <node3>
     * time_stamp             = 6*<hexOctet>
     * version_and_variant    = 2*<hexOctet>
     * sequence               = 4*<hexOctet>
     * node0                  = 4*<hexOctet>
     * node1                  = 4*<hexOctet>
     * node2                  = 4*<hexOctet>
     * node3                  = 4*<hexOctet>
     * hexOctet               = <hexDigit><hexDigit>
     * hexDigit               =
     *       "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
     *       | "a" | "b" | "c" | "d" | "e" | "f"
     *       | "A" | "B" | "C" | "D" | "E" | "F"
     * }</pre></blockquote>
     *
     * @return  A string representation of this {@code Uuid6}
     */
    @Override
    public String toString() {
        return formatHexByte(bytes[STAMP_START]) + formatHexByte(bytes[STAMP_START+1]) + formatHexByte(bytes[STAMP_START+2]) + formatHexByte(bytes[STAMP_START+3]) + formatHexByte(bytes[STAMP_START+4]) + formatHexByte(bytes[STAMP_START+5])
                + "-" + formatHexByte(bytes[VERSION_AND_VARIANT_START]) + formatHexByte(bytes[VERSION_AND_VARIANT_START+1])
                + "-" + formatHexByte(bytes[SEQUENCE_START]) + formatHexByte(bytes[SEQUENCE_START+1]) + formatHexByte(bytes[SEQUENCE_START+2]) + formatHexByte(bytes[SEQUENCE_START+3])
                + "-" + formatHexByte(bytes[NODE0_START]) + formatHexByte(bytes[NODE0_START+1]) + formatHexByte(bytes[NODE0_START+2]) + formatHexByte(bytes[NODE0_START+3])
                + "-" + formatHexByte(bytes[NODE1_START]) + formatHexByte(bytes[NODE1_START+1]) + formatHexByte(bytes[NODE1_START+2]) + formatHexByte(bytes[NODE1_START+3])
                + "-" + formatHexByte(bytes[NODE2_START]) + formatHexByte(bytes[NODE2_START+1]) + formatHexByte(bytes[NODE2_START+2]) + formatHexByte(bytes[NODE2_START+3])
                + "-" + formatHexByte(bytes[NODE3_START]) + formatHexByte(bytes[NODE3_START+1]) + formatHexByte(bytes[NODE3_START+2]) + formatHexByte(bytes[NODE3_START+3]);
    }

    /**
     * Returns a hash code for this {@code Uuid6}.
     *
     * @return  A hash code value for this {@code Uuid6}
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    /**
     * Compares this object to the specified object.  The result is {@code
     * true} if and only if the argument is not {@code null}, is a {@code Uuid6}
     * object, has the same variant, and contains the same value, bit for bit,
     * as this {@code Uuid6}.
     *
     * @param  obj
     *         The object to be compared
     *
     * @return  {@code true} if the objects are the same; {@code false}
     *          otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if ((null == obj) || (obj.getClass() != CFLibUuid6.class))
            return false;
        CFLibUuid6 id = (CFLibUuid6)obj;
        if (this == id) return true;
        return Arrays.equals(bytes, id.bytes);
    }

    // Comparison Operations

    /**
     * Compares this Uuid6 with the specified Uuid6.
     *
     * <p> The first of two Uuid6s is greater than the second if the most
     * significant field in which the Uuid6s differ is greater for the first
     * Uuid6.
     *
     * @param  val
     *         {@code Uuid6} to which this {@code Uuid6} is to be compared
     *
     * @return  -1, 0 or 1 as this {@code Uuid6} is less than, equal to, or
     *          greater than {@code val}
     *
     */
    @Override
    public int compareTo(CFLibUuid6 val) {
        if (val == null) {
            return 1;
        }
        if (this == val) {
            return 0;
        }
        return Arrays.compare(bytes, val.bytes);
    }
}
