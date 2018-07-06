package com.semerson.networkassessment.activities.network;

import java.util.ArrayList;
import java.util.List;

public class ScanTypes {
    public static final String NETWORK_SCAN_QUICK = "Quick Network Scan";
    public static final String NETWORK_SCAN_DEFAULT = "Network Scan";
    public static final String NETWORK_SCAN_CUSTOM = "Custom Network Scan";

    public static final String NETWORK_SCAN_STEALTH = "Stealth";
    public static final String NETWORK_SCAN_CONNECT = "TCP Connect";
    public static final String NETWORK_SCAN_UDP = "UDP";
    public static final String NETWORK_SCAN_FIN = "FIN";
    public static final String NETWORK_SCAN_NULL = "Null";
    public static final String NETWORK_SCAN_XMAS = "Xmas";

    public static String[] getAllNetworkMappingScanType() {
        return new String[]{
                NETWORK_SCAN_QUICK,
                NETWORK_SCAN_DEFAULT,
        };
    }

    public static String[] getAllNetworkMappingScanTypeAdvanced() {
        return new String[]{
                NETWORK_SCAN_QUICK,
                NETWORK_SCAN_DEFAULT,
                NETWORK_SCAN_CUSTOM,
        };
    }

    public static String[] getAllNetworkMappingScanTechniques() {
        return new String[]{
                NETWORK_SCAN_STEALTH,
                NETWORK_SCAN_CONNECT,
                NETWORK_SCAN_UDP,
                NETWORK_SCAN_FIN,
                NETWORK_SCAN_NULL,
                NETWORK_SCAN_XMAS,
        };
    }
}
