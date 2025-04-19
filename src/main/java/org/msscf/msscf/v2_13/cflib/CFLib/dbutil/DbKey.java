package org.msscf.msscf.v2_13.cflib.CFLib.dbutil;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class DbKey implements Serializable {
    public static final int IPV6_LENGTH = 16;
    public static final int IPV4_LENGTH = 4;
    public static final int IPV4_PAD = IPV6_LENGTH - IPV4_LENGTH;
    public static final int STAMP_LENGTH = 8;
    public static final int HEADER_LENGTH = IPV6_LENGTH + STAMP_LENGTH;

    protected static boolean addrHeaderInitialized = false;
    protected static byte[] addrHeader = new byte[IPV6_LENGTH];
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
}
