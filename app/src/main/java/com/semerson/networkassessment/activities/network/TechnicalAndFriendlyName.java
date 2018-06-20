package com.semerson.networkassessment.activities.network;

import com.semerson.networkassessment.storage.results.Service;

public class TechnicalAndFriendlyName {
    private String friendlyName = "";
    private String technicalName = "";

    public TechnicalAndFriendlyName(String friendlyName, String technicalName){
        this.friendlyName = friendlyName;
        this.technicalName = technicalName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
    }
}
