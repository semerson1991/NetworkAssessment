package com.semerson.networkassessment.results;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class OpenVasResult implements Parcelable, JsonStorage {
    private String host; //TODO Change protocl numbers (TCP, OpenVas Family (Web application abuses) etc to Static final values!!!
    private Integer portNumber;
    private String portProto;

    private String nvtName;
    private Double nvtScore;
    private String attackVectorScore;
    private String accessComplexityScore;    //   "_OpenVASNVT__cvss_base_vector": "AV:N/AC:L/Au:N/C:P/I:N/A:N",
    private String authenticationScore;
    private String confidentialityScore;
    private String integrityScore;
    private String availabilityScore;

    private String vulnFamily; //The vulnFamily of vulnerabilities e.g. SMPTP
    private String solution;
    private String summary;
    private String insight;
    private String impact;
    private String solutionType;
    private String vuldetect;


    private String threatRanking; //Low Medum High
    private String description;

    //TODO get the CVE's
    /*
                    "_OpenVASNVT__cves": [
                            "CVE-2012-1823",
                            "CVE-2012-2311",
                            "CVE-2012-2336",
                            "CVE-2012-2335"
                            ],
     */

    public OpenVasResult(){
        host = "";
        portNumber = -1;
        portProto = "";

        nvtName = "";
        nvtScore = -1.0;

        attackVectorScore = "";
        accessComplexityScore = "";
        authenticationScore = "";
        confidentialityScore = "";
        integrityScore = "";
        availabilityScore = "";

        vulnFamily = "";
        solution = "";
        summary = "";
        impact = "";
        solutionType = "";
        insight = "";
        vuldetect = "";
        threatRanking = "";
        description = "";

    }

    public String getVulnFamily(){
        return vulnFamily;
    }
    @Override
    public void storeJsonValues(JSONObject object) {
        try {
            storeHostInfo(object);
            storePortInfo(object);
            storeNvtInfo(object);
        } catch (JSONException e){

        }
    }

    public Double getNvtScore() {return nvtScore; }

    public String getThreatLevel() {return threatRanking;}

    public String getAttackComplexity() {
        switch (accessComplexityScore) {
            case "H":
                return "High";
            case "M":
                return "Medium";
            case "L":
                return "Low";
        }
        return accessComplexityScore;
    }

    private void storeHostInfo(JSONObject object) throws JSONException {
        if (!object.isNull("_OpenVASResult__host")) {
            host = object.getString("_OpenVASResult__host");
        }
    }

    private void storePortInfo(JSONObject object) throws JSONException {
        JSONObject ports = new JSONObject();
        if (!object.isNull("_OpenVASResult__port")) {
            ports = object.getJSONObject("_OpenVASResult__port");

            if (!ports.isNull("_OpenVASPort__port_name")) {
                if (ports.get("_OpenVASPort__port_name") instanceof String){ // General vulnerability's do not require port info (e.g. OS system outdated)
                    return;
                }
                portNumber = ports.getInt("_OpenVASPort__port_name");
            }

            if (!ports.isNull("_OpenVASPort__proto")) {
                portProto = ports.getString("_OpenVASPort__proto");
            }
        }
    }

    private void storeNvtInfo(JSONObject object) throws JSONException {
        JSONObject jsonOpenvasNvt = new JSONObject();
        if (!object.isNull("_OpenVASResult__nvt")) {
            jsonOpenvasNvt = object.getJSONObject("_OpenVASResult__nvt");

            if (!jsonOpenvasNvt.isNull("_OpenVASNVT__name")) {
                nvtName = jsonOpenvasNvt.getString("_OpenVASNVT__name");
            }

            if (!jsonOpenvasNvt.isNull("_OpenVASNVT__cvss_base")) {
                nvtScore = jsonOpenvasNvt.getDouble("_OpenVASNVT__cvss_base");
            }

            if (!jsonOpenvasNvt.isNull("_OpenVASNVT__family")) {
                vulnFamily = jsonOpenvasNvt.getString("_OpenVASNVT__family");
            }

            if (!jsonOpenvasNvt.isNull("_OpenVASNVT__tags")) {
                JSONArray openvasNvtTags = jsonOpenvasNvt.getJSONArray("_OpenVASNVT__tags");
                parseTags(openvasNvtTags.getString(0));
            }
        }

        if (!object.isNull("_OpenVASResult__threat")) {
            threatRanking = object.getString("_OpenVASResult__threat");

        }

        if (!object.isNull("_OpenVASResult__description")) {
            description = object.getString("_OpenVASResult__description");
        }
    }

    private void parseTags(String openvasNvtTags){
        final String cvssBaseVector = "cvss_base_vector="; //TODO PARSE THE BASE VECTOR
        final String summary = "summary=";
        final String vuldetect = "vuldetect=";
        final String insight = "insight=";
        final String impact = "impact=";
        final String solution = "solution=";
        final String solution_type = "solution_type=";

        String[] parts = openvasNvtTags.split("\\|");
        for (int i = 0; i < parts.length; i++){
            String currentTag = parts[i];
            String key = currentTag.substring(0, currentTag.indexOf("=") + 1);
            switch (key){
                case cvssBaseVector:
                    parseCvssBaseVector(currentTag.substring(key.length(), currentTag.length()));
                    break;
                case summary:
                    this.summary = currentTag.substring(key.length(), currentTag.length());
                    break;
                case vuldetect:
                    this.vuldetect = currentTag.substring(key.length(), currentTag.length());
                    break;
                case insight:
                    this.insight = currentTag.substring(key.length(), currentTag.length());
                    break;
                case impact:
                    this.impact = currentTag.substring(key.length(), currentTag.length());
                    break;
                case solution:
                    this.solution = currentTag.substring(key.length(), currentTag.length());
                    break;
                case solution_type:
                    this.solutionType = currentTag.substring(key.length(), currentTag.length());
                    break;
            }
        }
    }

    private void parseCvssBaseVector(String cvssBaseVectorInfo){
        final String attackVectorScore = "AV:";
        final String accessComplexityScore = "AC:";
        final String authenticationScore = "Au:";
        final String confidentialityScore = "C:";
        final String integrityScore = "I:";
        final String availabilityScore = "A:";

        String[] parts = cvssBaseVectorInfo.split("/");
        for (int i = 0; i < parts.length; i++) {
            String currentTag = parts[i];
            String key = currentTag.substring(0, currentTag.indexOf(":") + 1);
            switch (key) {
                case attackVectorScore:
                    this.attackVectorScore = currentTag.substring(key.length(), currentTag.length());
                    break;
                case accessComplexityScore:
                    this.accessComplexityScore = currentTag.substring(key.length(), currentTag.length());
                    break;
                case authenticationScore:
                    this.authenticationScore = currentTag.substring(key.length(), currentTag.length());
                    break;
                case confidentialityScore:
                    this.confidentialityScore = currentTag.substring(key.length(), currentTag.length());
                    break;
                case integrityScore:
                    this.integrityScore = currentTag.substring(key.length(), currentTag.length());
                    break;
                case availabilityScore:
                    this.availabilityScore = currentTag.substring(key.length(), currentTag.length());
                    break;
            }
        }
    }

    public OpenVasResult(Parcel in){
        host = in.readString();
        portNumber = in.readInt();
        portProto = in.readString();

        nvtName = in.readString();
        nvtScore = in.readDouble();
        vulnFamily = in.readString();

        attackVectorScore = in.readString();
        accessComplexityScore = in.readString();
        authenticationScore = in.readString();
        confidentialityScore = in.readString();
        integrityScore = in.readString();
        availabilityScore = in.readString();

        solution = in.readString();
        summary = in.readString();
        impact = in.readString();
        insight = in.readString();
        solutionType = in.readString();
        vuldetect = in.readString();

        threatRanking = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(host);
        dest.writeInt(portNumber);
        dest.writeString(portProto);

        dest.writeString(nvtName);
        dest.writeDouble(nvtScore);
        dest.writeString(vulnFamily);

        dest.writeString(attackVectorScore);
        dest.writeString(accessComplexityScore);
        dest.writeString(authenticationScore);
        dest.writeString(confidentialityScore);
        dest.writeString(integrityScore);
        dest.writeString(availabilityScore);

        dest.writeString(solution);
        dest.writeString(summary);
        dest.writeString(impact);
        dest.writeString(insight);
        dest.writeString(solutionType);
        dest.writeString(vuldetect);

        dest.writeString(threatRanking);
        dest.writeString(description);

    }

    public static final Parcelable.Creator<OpenVasResult> CREATOR = new Parcelable.Creator<OpenVasResult>() {

        @Override
        public OpenVasResult createFromParcel(Parcel source) {
            return new OpenVasResult(source);
        }

        @Override
        public OpenVasResult[] newArray(int size) {
            return new OpenVasResult[size];
        }
    };


}
