package com.semerson.networkassessment.results;

import com.semerson.networkassessment.activities.Results.ResultCallback;

public class ResultScoreMetrics {
    private String host;
    private Integer highCount = 0;
    private Integer medCount = 0;
    private Integer lowCount = 0;
    private Integer criticalCount = 0;
    private Integer noneCount = 0;

    public ResultScoreMetrics(String host) {
        this.host = host;
    }

    public Integer getTotal(){
        return highCount + medCount + lowCount;
    }
    public void appendCrit(int num) {
        criticalCount++;
    }

    public void appendHigh(int num) {
        highCount++;
    }

    public int getHighCount() {
        return highCount;
    }

    public void appendMed(int num) {
        medCount++;
    }

    public int getMedCount() {
        return medCount;
    }

    public void appendLow(int num) {
        lowCount++;
    }

    public int getLowCount() {
        return lowCount;
    }

    public void appendNone(int num) {
        noneCount++;
    }

    public int getNoneCount() {
        return noneCount;
    }

    public void appendThreatLevel(ResultCallback resultCallback, VulnerabilityResult result) {
        switch (resultCallback.getResult(result)) {
            case ResultLevels.CRITICAL:
                appendCrit(1);
                break;
            case ResultLevels.HIGH:
                appendHigh(1);
                break;
            case ResultLevels.MEDIUM:
                appendMed(1);
                break;
            case ResultLevels.LOW:
                appendLow(1);
                break;
            case ResultLevels.NONE:
                appendNone(1);
                break;
            default:
                break;
        }
    }
}
