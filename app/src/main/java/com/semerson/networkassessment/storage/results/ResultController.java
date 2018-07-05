package com.semerson.networkassessment.storage.results;

import android.content.Context;

import com.semerson.networkassessment.activities.Results.ResultCallback;
import com.semerson.networkassessment.activities.network.NetworkDevices;
import com.semerson.networkassessment.activities.network.TechnicalAndFriendlyName;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResultController {

    private Map<String, Host> hosts;

    public ResultController(List<Host> hosts) {
        updateHosts(hosts);
    }

    //Filter

    /**
     * Get the vulnerability families for a specific host
     *
     * @param hostId
     * @return
     */
    public Map<String, Float> getVulnFamily(String hostId) {
        Map<String, Float> vulnFamily = new HashMap<>();
        final float numToIncrement = 1.0f;
        Host host = hosts.get(hostId);

        List<VulnerabilityResult> ovResults = host.getVulnerabilityResults();

        for (VulnerabilityResult ovResult : ovResults) {
            String family = ovResult.getVulnFamily();
            if (vulnFamily.containsKey(family)) {
                float value = vulnFamily.get(family);
                value = value + numToIncrement;
                vulnFamily.put(family, value); // May not need to put again? Check if value within map changes automatically
            } else {
                vulnFamily.put(family, 1.0f);
            }
        }
        return vulnFamily;
    }

    public List<VulnerabilityResult> getAllVulnerabilities() {
        List<VulnerabilityResult> vulnerabilities = new ArrayList<>();
        for (String hostname : hosts.keySet()) {
            Host host = hosts.get(hostname);

            List<VulnerabilityResult> vulnResults = host.getVulnerabilityResults();

            for (VulnerabilityResult vulnerabilityResult : vulnResults) {
                vulnerabilities.add(vulnerabilityResult);
            }
        }
        return vulnerabilities;
    }

    /**
     * Get all vulnerabilities for a certain category
     *
     * @param category
     */
    public List<VulnerabilityResult> getVulnerabiltiesFilterByCategory(String category) {
        List<VulnerabilityResult> resultsFilteresByCategory = new ArrayList<>();

        for (String hostname : hosts.keySet()) {
            Host host = hosts.get(hostname);

            List<VulnerabilityResult> vulnResults = host.getVulnerabilityResults();

            for (VulnerabilityResult vulnerabilityResult : vulnResults) {
                String vulnCategory = vulnerabilityResult.getVulnFamily();
                if (vulnCategory.equals(category)) {
                    resultsFilteresByCategory.add(vulnerabilityResult);
                }
            }
        }
        return resultsFilteresByCategory;
    }


    public List<VulnerabilityResult> getVulnerabilitiesFilterByHost(String hostname) {
        Host host = hosts.get(hostname);
        return host.getVulnerabilityResults();
    }


    //All Data (No filtering)

    public Map<String, Float> getOperatingSystems() {
        Map<String, Float> operatingSystems = new HashMap<>();
        final float numToIncrement = 1.0f;
        for (String hostName : hosts.keySet()) {
            Host host = hosts.get(hostName);
            String os = host.getOs();
            if (operatingSystems.containsKey(os)) {
                float value = operatingSystems.get(os);
                value = value + numToIncrement;
                operatingSystems.put(os, value); // May not need to put again? Check if value within map changes automatically
            } else {
                operatingSystems.put(os, 1.0f);
            }
        }
        return operatingSystems;
    }

    public Map<String, Float> getVulnFamily() {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getVulnFamily();
            }
        };
        return getResultData(resultCallback);
    }

    public Map<String, Float> getMitigationCategories() {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getSolutionType();
            }
        };
        return getResultData(resultCallback);
    }

    public Map<String, Float> getVulnFamilyFilterByThreatLevel(String threatLevel) {
        Map<String, Float> resultData = new HashMap<>();
        final float numToIncrement = 1.0f;

        for (String hostName : hosts.keySet()) {
            Host host = hosts.get(hostName);
            List<VulnerabilityResult> vulnerabilityResults = host.getVulnerabilityResults();

            for (VulnerabilityResult vulnResult : vulnerabilityResults) {
                if (vulnResult.getThreatLevel().equals(threatLevel)) {
                    String result = vulnResult.getVulnFamily();
                    if (resultData.containsKey(result)) {
                        float value = resultData.get(result);
                        value = value + numToIncrement;
                        resultData.put(result, value);
                    } else {
                        resultData.put(result, 1.0f);
                    }
                }
            }
        }
        return resultData;
    }

    public Map<String, Float> getThreatLevelFilterByVulnFamily(String vulnFamily) {
        Map<String, Float> resultData = new HashMap<>();
        final float numToIncrement = 1.0f;

        for (String hostName : hosts.keySet()) {
            Host host = hosts.get(hostName);
            List<VulnerabilityResult> vulnerabilityResults = host.getVulnerabilityResults();

            for (VulnerabilityResult vulnResult : vulnerabilityResults) {
                if (vulnResult.getVulnFamily().equals(vulnFamily)) {
                    String result = vulnResult.getThreatLevel();
                    if (resultData.containsKey(result)) {
                        float value = resultData.get(result);
                        value = value + numToIncrement;
                        resultData.put(result, value);
                    } else {
                        resultData.put(result, 1.0f);
                    }
                }
            }
        }
        return resultData;
    }

    public Map<String, Float> getVulnFamilyFilterByComplexityLevel(String complexityLevel) {
        Map<String, Float> resultData = new HashMap<>();
        final float numToIncrement = 1.0f;

        for (String hostName : hosts.keySet()) {
            Host host = hosts.get(hostName);
            List<VulnerabilityResult> vulnerabilityResults = host.getVulnerabilityResults();

            for (VulnerabilityResult vulnResult : vulnerabilityResults) {
                if (vulnResult.getAttackComplexity().equals(complexityLevel)) {
                    String result = vulnResult.getVulnFamily();
                    if (resultData.containsKey(result)) {
                        float value = resultData.get(result);
                        value = value + numToIncrement;
                        resultData.put(result, value);
                    } else {
                        resultData.put(result, 1.0f);
                    }
                }
            }
        }
        return resultData;
    }

    public Map<String, Float> getNumberOfHostVulnerabilities() {
        Map<String, Float> vulnerableHosts = new LinkedHashMap<>();
        final float numToIncrement = 1.0f;

        for (String hostName : hosts.keySet()) {
            Host host = hosts.get(hostName);
            Integer vulnerabilityCount = host.getVulnerabilityResults().size();
            float numOfResults = 0.0f;
            for (int i = 0; i < vulnerabilityCount; i++) {
                numOfResults = numOfResults + numToIncrement;
            }
            String numOfVulns = " - Total Vulnerabilities: " + vulnerabilityCount.toString();
            vulnerableHosts.put(hostName + numOfVulns, numOfResults);
        }
        removeEmptyDataFromMap(vulnerableHosts);
        return vulnerableHosts;
    }


    /**
     * Gets the threats levels from the openvas results. The "Low" "Medium" and "high" are put
     * in the map so they are inserted in the correct order. The keys that have no values are removed at
     * the end.
     *
     * @return A map containing the NVT score (High Medium and Low) levels of threats with the total amount of each.
     */
    public Map<String, Float> getThreatLevel() {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getThreatLevel();
            }
        };
        return getResultDataOrdered(resultCallback);
    }

    public Map<String, Float> getAttackComplexityLevel() {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getAttackComplexity();
            }
        };
        return getResultDataOrdered(resultCallback);
    }

    public Map<String, Float> getConfidentialityScore() {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getConfidentialityScore();
            }
        };
        return getResultDataOrdered(resultCallback);
    }

    public Map<String, Float> getConfidentialityScore(String host) {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getConfidentialityScore();
            }
        };
        return getResultDataOrderedFilterByHost(resultCallback, host);
    }

    public Map<String, Float> getIntegrityScore() {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getIntegrityScore();
            }
        };
        return getResultDataOrdered(resultCallback);
    }

    public Map<String, Float> getIntegrityScore(String host) {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getIntegrityScore();
            }
        };
        return getResultDataOrderedFilterByHost(resultCallback, host);
    }

    public Map<String, Float> getAvailabilityScore() {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getAvailabilityScore();
            }
        };
        return getResultDataOrdered(resultCallback);
    }

    public Map<String, Float> getAvailabilityScore(String host) {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getAvailabilityScore();
            }
        };
        return getResultDataOrderedFilterByHost(resultCallback, host);
    }

    /**
     * Get the results ordered from High to Low
     *
     * @param resultCallback
     * @return
     */
    private Map<String, Float> getResultDataOrdered(ResultCallback resultCallback) {
        Map<String, Float> vulnLevel = new LinkedHashMap<>();
        vulnLevel.put("None", 0.0f);
        vulnLevel.put("Low", 0.0f);
        vulnLevel.put("Medium", 0.0f);
        vulnLevel.put("High", 0.0f);
        vulnLevel.put("Critical", 0.0f);
        final float numToIncrement = 1.0f;
        for (String hostName : hosts.keySet()) {
            Host host = hosts.get(hostName);
            List<VulnerabilityResult> vulnerabilityResults = host.getVulnerabilityResults();

            for (VulnerabilityResult ovResult : vulnerabilityResults) {
                String result = resultCallback.getResult(ovResult);
                if (vulnLevel.containsKey(result)) {
                    float value = vulnLevel.get(result);
                    value = value + numToIncrement;
                    vulnLevel.put(result, value); // May not need to put again? Check if value within map changes automatically
                } else {
                    vulnLevel.put(result, 1.0f);
                }
            }
        }
        removeEmptyDataFromMap(vulnLevel);
        return vulnLevel;
    }

    private Map<String, Float> getResultDataOrderedFilterByHost(ResultCallback resultCallback, String hostname) {
        Map<String, Float> vulnLevel = new LinkedHashMap<>();
        vulnLevel.put("None", 0.0f);
        vulnLevel.put("Low", 0.0f);
        vulnLevel.put("Medium", 0.0f);
        vulnLevel.put("High", 0.0f);
        vulnLevel.put("Critical", 0.0f);
        final float numToIncrement = 1.0f;

        Host host = hosts.get(hostname);
        List<VulnerabilityResult> vulnerabilityResults = host.getVulnerabilityResults();

        for (VulnerabilityResult ovResult : vulnerabilityResults) {
            String result = resultCallback.getResult(ovResult);
            if (vulnLevel.containsKey(result)) {
                float value = vulnLevel.get(result);
                value = value + numToIncrement;
                vulnLevel.put(result, value); // May not need to put again? Check if value within map changes automatically
            } else {
                vulnLevel.put(result, 1.0f);
            }
        }

        removeEmptyDataFromMap(vulnLevel);
        return vulnLevel;
    }

    private void removeEmptyDataFromMap(Map<String, Float> map) {
        Iterator<Map.Entry<String, Float>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Float> entry = iter.next();
            if (entry.getValue() == 0.0f) {
                iter.remove();
            }
        }
    }

    private Map<String, Float> getResultData(ResultCallback resultCallback) {
        Map<String, Float> resultData = new HashMap<>();
        final float numToIncrement = 1.0f;

        for (String hostName : hosts.keySet()) {
            Host host = hosts.get(hostName);
            List<VulnerabilityResult> vulnerabilityResults = host.getVulnerabilityResults();

            for (VulnerabilityResult vulnResult : vulnerabilityResults) {
                String result = resultCallback.getResult(vulnResult);
                if (resultData.containsKey(result)) {
                    float value = resultData.get(result);
                    value = value + numToIncrement;
                    resultData.put(result, value);
                } else {
                    resultData.put(result, 1.0f);
                }
            }
        }
        return resultData;
    }

    /**
     * @return A map that contains a hostname as the key, with an object that contains the number of
     * None, Low, Medium, and High level threats for that host.
     */
    public Map<String, ResultScoreMetrics> getVulnThreatLevelCount() {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getThreatLevel();
            }
        };
        return getScoreMetrics(resultCallback);
    }

    public Map<String, ResultScoreMetrics> getConfidentialityScoreCount() {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getConfidentialityScore();
            }
        };
        return getScoreMetrics(resultCallback);
    }

    public Map<String, ResultScoreMetrics> getIntegrityScoreCount() {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getIntegrityScore();
            }
        };
        return getScoreMetrics(resultCallback);
    }

    public Map<String, ResultScoreMetrics> getAvailabilityScoreCount() {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getAvailabilityScore();
            }
        };
        return getScoreMetrics(resultCallback);
    }

    public Map<String, ResultScoreMetrics> getScoreMetrics(ResultCallback resultCallback) {
        Map<String, ResultScoreMetrics> metricLevel = new HashMap<>();

        for (String hostName : hosts.keySet()) {
            Host host = hosts.get(hostName);
            List<VulnerabilityResult> ovResults = host.getVulnerabilityResults();

            for (VulnerabilityResult ovResult : ovResults) {
                String hostip = ovResult.getHost();

                if (metricLevel.containsKey(hostip)) {
                    ResultScoreMetrics resultScoreMetrics = metricLevel.get(hostip);
                    resultScoreMetrics.appendThreatLevel(resultCallback, ovResult);
                    metricLevel.put(hostip, resultScoreMetrics); // May not need to put again? Check if value within map changes automatically
                } else {
                    ResultScoreMetrics resultScoreMetrics = new ResultScoreMetrics(hostip);
                    resultScoreMetrics.appendThreatLevel(resultCallback, ovResult);
                    metricLevel.put(hostip, resultScoreMetrics);
                }
            }
        }
        return metricLevel;
    }

    public List<VulnerabilityResult> getVulnerabiltiesFilterByThreatLevel(String filter) {
        List<VulnerabilityResult> results = new ArrayList<>();
        switch (filter) {
            case ResultLevels.HIGH:
                results = getResultsFilterByThreat(ResultLevels.HIGH);
                break;
            case ResultLevels.MEDIUM:
                results = getResultsFilterByThreat(ResultLevels.MEDIUM);
                break;
            case ResultLevels.LOW:
                results = getResultsFilterByThreat(ResultLevels.LOW);
                break;
            default:
                return results;
        }
        return results;
    }

    public List<VulnerabilityResult> getVulnerabiltiesFilterByMitigationType(String mitigationType) {
        List<VulnerabilityResult> results = new ArrayList<>();
        for (String hostname : hosts.keySet()) {
            Host host = hosts.get(hostname);
            List<VulnerabilityResult> vulnerabilityResults = host.getVulnerabilityResults();
            for (VulnerabilityResult vulnerabilityResult : vulnerabilityResults) {
                if (vulnerabilityResult.getSolutionType().equals(mitigationType)) {
                    results.add(vulnerabilityResult);
                }
            }
        }
        return results;
    }


    private List<VulnerabilityResult> getResultsFilterByThreat(String threatLevel) {
        List<VulnerabilityResult> results = new ArrayList<>();
        for (String hostname : hosts.keySet()) {
            Host host = hosts.get(hostname);
            List<VulnerabilityResult> vulnerabilityResults = host.getVulnerabilityResults();
            for (VulnerabilityResult vulnerabilityResult : vulnerabilityResults) {
                if (vulnerabilityResult.getThreatLevel().equals(threatLevel)) {
                    results.add(vulnerabilityResult);
                }
            }
        }
        return results;
    }


    public List<VulnerabilityResult> getVulnerabiltiesFilterByComplexityLevel(String complexityLevel) {
        List<VulnerabilityResult> results = new ArrayList<>();
        for (String hostname : hosts.keySet()) {
            Host host = hosts.get(hostname);
            List<VulnerabilityResult> vulnerabilityResults = host.getVulnerabilityResults();
            for (VulnerabilityResult vulnerabilityResult : vulnerabilityResults) {
                if (vulnerabilityResult.getAttackComplexity().equals(complexityLevel)) {
                    results.add(vulnerabilityResult);
                }
            }
        }
        return results;
    }

    public List<VulnerabilityResult> filterByRiskScoreHighToLow(List<VulnerabilityResult> vulnerabilityResults) {
        Collections.sort(vulnerabilityResults, new Comparator<VulnerabilityResult>() {
            public int compare(VulnerabilityResult v1, VulnerabilityResult v2) {
                return v2.getRiskScore().compareTo(v1.getRiskScore());
            }
        });
        return vulnerabilityResults;
    }

    //TODO This method returns the exact number of number of categories. This should eventually be changed to be more flexible on returning
    //TODO occurences based on additional condtions if there are more than 3 categories in the list with the same number of occurrences.
    public List<VulnerabilityCategoryOccurrences> getTopVulnerabilityCategories(int numberOfCategories) {
        List<VulnerabilityCategoryOccurrences> vulnerabilityCategoryOccrences = new ArrayList<>();

        for (String hostname : hosts.keySet()) {
            Host host = hosts.get(hostname);
            List<VulnerabilityResult> vulnerabilityResults = host.getVulnerabilityResults();
            for (VulnerabilityResult vulnerabilityResult : vulnerabilityResults) {
                String family = vulnerabilityResult.getVulnFamily();
                boolean exists = false;
                for (VulnerabilityCategoryOccurrences vulnCatergory : vulnerabilityCategoryOccrences) {
                    if (vulnCatergory.getVulnerabilityFamily().equals(family)) {
                        vulnCatergory.appendOccurence();
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    vulnerabilityCategoryOccrences.add(new VulnerabilityCategoryOccurrences(family));
                }
            }
        }
        Collections.sort(vulnerabilityCategoryOccrences, new Comparator<VulnerabilityCategoryOccurrences>() {
            public int compare(VulnerabilityCategoryOccurrences v1, VulnerabilityCategoryOccurrences v2) {
                return v2.getOccurences().compareTo(v1.getOccurences());
            }
        });
        return vulnerabilityCategoryOccrences.size() > numberOfCategories ? vulnerabilityCategoryOccrences.subList(0, numberOfCategories) : vulnerabilityCategoryOccrences;
    }

    public String getCategoryOccurrenceAsPercent(String vulnerabilityFamily) {
        List<VulnerabilityResult> allResults = getAllVulnerabilities();

        Float totalNumOfResults = 0.0f;
        Float occurences = 0.0f;
        final Float numToIncrement = 1.0f;
        for (VulnerabilityResult result : allResults) {
            totalNumOfResults = numToIncrement + totalNumOfResults;
            if (result.getVulnFamily().equals(vulnerabilityFamily)) {
                occurences = occurences + numToIncrement;
            }
        }
        return Utils.getFormattedPercentage(totalNumOfResults, occurences);
    }

    public void updateHosts(List<Host> hosts) {
        this.hosts = new HashMap<>();

        for (Host host : hosts) {
            this.hosts.put(host.getHostname(false), host);
        }
    }

    public String checkVulnerableOs(String os) {
        switch (os) {
            case "Windows XP":
                return "Warning: This Operating System is obsolete and should be upgraded.";
        }
        return "";
    }

    public List<Host> getHostsFilterByOS(String os) {
        List filteredHostsByOs = new ArrayList();

        for (String hostname : hosts.keySet()) {
            Host host = hosts.get(hostname);

            if (host.getOs().equals(os)) {
                filteredHostsByOs.add(host);
            }
        }
        return filteredHostsByOs;
    }

    public Map<String, Float> getServices(boolean advancedMode) {
        Map<String, Float> services = new HashMap<>();
        final float numToIncrement = 1.0f;
        for (String hostName : hosts.keySet()) {
            Host host = hosts.get(hostName);
            List<Service> hostServices = host.getServices();
            for (Service service : hostServices) {
                TechnicalAndFriendlyName serviceNames = service.getService();
                String serviceName = advancedMode ? serviceNames.getTechnicalName() : serviceNames.getFriendlyName();
                if (services.containsKey(serviceName)) {
                    float value = services.get(serviceName);
                    value = value + numToIncrement;
                    services.put(serviceName, value); // May not need to put again? Check if value within map changes automatically
                } else {
                    services.put(serviceName, 1.0f);
                }
            }
        }
        return services;
    }

    public List<Host> getHostsFilterByService(String theService) {
        List filteredHostsByService = new ArrayList();

        for (String hostname : hosts.keySet()) {
            Host host = hosts.get(hostname);
            List<Service> services = host.getServices();
            for (Service service : services) {
                TechnicalAndFriendlyName names = service.getService();
                if (names.getFriendlyName().equals(theService) || names.getTechnicalName().equals(theService)) {
                    filteredHostsByService.add(host);
                    break;
                }
            }
        }
        return filteredHostsByService;
    }

    public List<String> getUnsupportOperatingSystems() {
        List<String> unsupportOperatingSystems = new ArrayList<>();
        final String WINDOWS_XP = "Windows Xp";
        final String LINUX_2_6 = "Linux 2.6.X";

        for (String hostname : hosts.keySet()) {
            Host host = hosts.get(hostname);
            String os = host.getOs();
            switch (os) {
                case LINUX_2_6:
                case WINDOWS_XP:
                    unsupportOperatingSystems.add(os);
                    break;

            }
        }
        return unsupportOperatingSystems;
    }

    public Host getUpdatedHost(Context context, Host previousHost) {
        ScanResults scanResults = AppStorage.getScanResults(context);
        String hostIp = previousHost.getIp();
        return scanResults.getHost(hostIp);
    }

    public class VulnerabilityCategoryOccurrences {
        Integer occurences = 1;
        String vulnerabilityFamily;

        public VulnerabilityCategoryOccurrences(String vulerabilityFamily) {
            this.vulnerabilityFamily = vulerabilityFamily;
        }

        public void appendOccurence() {
            occurences++;
        }

        public Integer getOccurences() {
            return occurences;
        }

        public String getVulnerabilityFamily() {
            return vulnerabilityFamily;
        }

        @Override
        public String toString() {
            return vulnerabilityFamily;
        }
    }
}
