package com.semerson.networkassessment.results;

import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.ThreatLevels;
import com.semerson.networkassessment.activities.Results.ResultCallback;

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
        this.hosts = new HashMap<>();

        for (Host host : hosts) {
            this.hosts.put(host.getHostname(false), host);
        }
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


    public List<VulnerabilityResult> getVulnInfoFilterByHost(String hostname) {
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

    public Map<String, Float> getIntegrityScore() {
        ResultCallback resultCallback = new ResultCallback() {
            @Override
            public String getResult(VulnerabilityResult vulnerabilityResult) {
                return vulnerabilityResult.getIntegrityScore();
            }
        };
        return getResultDataOrdered(resultCallback);
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
}
