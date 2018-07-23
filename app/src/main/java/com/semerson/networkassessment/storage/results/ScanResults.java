package com.semerson.networkassessment.storage.results;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScanResults implements Parcelable {
    private static final String TAG = "ScanResult";
    private List<Host> hosts;

    public ScanResults() {
        hosts = new ArrayList<>();
    }

    public boolean appendHost(JSONObject jsonHost) {
        try {
            if (jsonHost.getString("status").equals("down")) {
                return false;
            }

            String hostIp = "";
            if (!jsonHost.isNull("ip")) {
                hostIp = jsonHost.getString("ip");
                removeDuplicateHosts(hostIp);

                Host host = new Host();
                host.storeJsonValues(jsonHost);

                JSONArray jsonServices = jsonHost.getJSONArray("services");
                host.addService(jsonServices);
                hosts.add(host);
            } else {
                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void appendHost(Host host) {
        hosts.add(host);
    }

    public void removeDuplicateHosts(String ip) {
        int indexToRemove = -1;
        for (int i = 0; i < hosts.size(); i++) {
            if (hosts.get(i).getHostname(false).equals(ip)) {
                Log.i(TAG, "Host exists: " + ip + ". Removing host to add the newly updated host values");
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove != -1) {
            hosts.remove(indexToRemove);
        }
    }

    public void appendOpenVasResults(JSONObject openvasResult) {
        VulnerabilityResult ovasResult = new VulnerabilityResult();
        ovasResult.storeJsonValues(openvasResult);

        String hostIp = ovasResult.getHost();
        for (Host host : hosts) {
            if (host.getHostname(false).equals(hostIp)) {

                //This is to avoid duplicate scan results. This should later be moved to avoid parsing the whole JSON object before finding out it is not required.
                List<VulnerabilityResult> previousVulns = host.getVulnerabilityResults();
                for (VulnerabilityResult previousVuln : previousVulns){
                    if (previousVuln.getId().equals(ovasResult.getId())) {
                        return;
                    }
                }
                host.appendOpenVas(ovasResult);
            }
        }
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public Host getHost(String hostname) {
        for (Host host : hosts) {
            if (host.getHostname(true).equals(hostname)) {
                return host;
            }
        }
        return null;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public ScanResults(Parcel in) {
        hosts = new ArrayList<>();
        in.readTypedList(hosts, Host.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(hosts);
    }

    public static final Parcelable.Creator<ScanResults> CREATOR = new Parcelable.Creator<ScanResults>() {

        @Override
        public ScanResults createFromParcel(Parcel in) {
            return new ScanResults(in);
        }

        @Override
        public ScanResults[] newArray(int size) {
            return new ScanResults[size];
        }
    };

    public VulnerabilityResult getVulnerabilityInfo(String vulnTitle) {
        for (Host host : hosts) {
            for (VulnerabilityResult result : host.getVulnerabilityResults()) {
                if (result.getNvtName().equals(vulnTitle)) {
                    return result;
                }
            }
        }
        return null;
    }
}