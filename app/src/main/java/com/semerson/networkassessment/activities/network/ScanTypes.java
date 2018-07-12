package com.semerson.networkassessment.activities.network;

import java.util.ArrayList;
import java.util.List;

public class ScanTypes {
    public static final String NETWORK_SCAN_QUICK = "Quick Network Scan";
    public static final String NETWORK_SCAN_DEFAULT = "Network Scan";
    public static final String NETWORK_SCAN_CUSTOM = "Custom Network Scan";

    public static final String VULN_SCAN_FULL_FAST = "Full and Fast Scan";
    public static final String VULN_SCAN_FULL_DEEP= "Full and Deep Scan";
    public static final String VULN_SCAN_FULL_FAST_ULTIMATE = "Full and Fast Ultimate";
    public static final String VULN_SCAN_FULL__DEEP_ULTIMATE = "Full and Deep Ultimate";

    public static final String NETWORK_SCAN_STEALTH = "Stealth";
    public static final String NETWORK_SCAN_CONNECT = "TCP Connect";
    public static final String NETWORK_SCAN_UDP = "UDP";
    public static final String NETWORK_SCAN_FIN = "FIN";
    public static final String NETWORK_SCAN_NULL = "Null";
    public static final String NETWORK_SCAN_XMAS = "Xmas";

    public static final String NETWORK_SCAN_DETECTION_OS = "Operating System";
    public static final String NETWORK_SCAN_DETECTION_OS_AND_SERVICE = "Operating Systems & Service Versions";
    public static final String NETWORK_SCAN_DETECTION_NONE = "None";

    public static String[] getAllNetworkMappingScanType() {
        return new String[]{
                NETWORK_SCAN_DEFAULT,
                NETWORK_SCAN_QUICK
        };
    }

    public static String[] getAllNetworkMappingScanTypeAdvanced() {
        return new String[]{
                NETWORK_SCAN_DEFAULT,
                NETWORK_SCAN_QUICK,
                NETWORK_SCAN_CUSTOM,
        };
    }

    public static String[] getAllVulnerabilityScanType() {
        return new String[]{
                VULN_SCAN_FULL_FAST,
                VULN_SCAN_FULL_DEEP,
        };
    }

    public static String[] getAllVulnerabilityScanTypeAdvanced() {
        return new String[]{
                VULN_SCAN_FULL_FAST,
                VULN_SCAN_FULL_DEEP,
                VULN_SCAN_FULL_FAST_ULTIMATE,
                VULN_SCAN_FULL__DEEP_ULTIMATE,

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

    public static String[] getDetectionOptions() {
        return new String[] {
                NETWORK_SCAN_DETECTION_OS,
                NETWORK_SCAN_DETECTION_OS_AND_SERVICE,
                NETWORK_SCAN_DETECTION_NONE
        };
    }
}
