package com.semerson.networkassessment.storage.results;

import android.os.Parcel;
import android.os.Parcelable;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.home.WelcomeActivity;
import com.semerson.networkassessment.utils.StyledText;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Host implements Parcelable, JsonStorage{

    public static final String NEVER = "Never";
    public static final String NEVER_SCANNED = NEVER + " Scanned";
    public static final String LAST_SCANNED_NEVER = "Last Scanned: " + NEVER;

    public static final String NO_RISKS_FOUND = "No Risks Found";
    public static final String RISKS_FOUND = "Risk(s) found";

    private List<Service> services;
    private List<VulnerabilityResult> vulnerabilityResults;
    private String ip;
    private String osFamily;
    private String osGen;
    private Integer osAccuracy;
    private String manufacturer;
    private String macAddress;
    private String lastScanned;
    private String lastScanDate;

    private String scanID;

    public Host(){
        services = new ArrayList<>();
        vulnerabilityResults = new ArrayList<>();
        ip = "";
        osFamily = "";
        osGen = "";
        osAccuracy = -1;
        manufacturer = "";
        macAddress = "";
        lastScanned = NEVER;
        lastScanDate = NEVER;
        scanID = "";
    }

    public void storeJsonValues(JSONObject host){
        try {
            if (!host.isNull("ip")) {
                this.ip = host.getString("ip");
            }
            if (!host.isNull("osfamily")) {
                this.osFamily = host.getString("osfamily");
            }
            if (!host.isNull("osgen")) {
                this.osGen = host.getString("osgen");
            }
            if (!host.isNull("osaccuracy")) {
                this.osAccuracy = host.getInt("osaccuracy");
            }
            if (!host.isNull("vendor")) {
                this.manufacturer = host.getString("vendor");
            }
            if (!host.isNull("mac")) {
                this.macAddress = host.getString("mac");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getHostname(boolean attemptToResolveIP) {
        if (attemptToResolveIP){
            return ip;
        }
        return ip;
    }
    public void addService(JSONArray jsonServices){
        for (int i = 0; i < jsonServices.length(); i++) {
            Service service = new Service();
            try {
                service.storeJsonValues(jsonServices.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            services.add(service);
        }
    }

    public void appendOpenVas(VulnerabilityResult vulnerabilityResult){
        vulnerabilityResults.add(vulnerabilityResult);
    }

    public List<VulnerabilityResult> getVulnerabilityResults(){
        return vulnerabilityResults;
    }

    public List<Service> getServices(){
        return services;
    }

    public Host(Parcel in){
        ip = in.readString();
        osFamily = in.readString();
        osGen = in.readString();
        osAccuracy = in.readInt();
        manufacturer = in.readString();
        macAddress = in.readString();
        lastScanned = in.readString();
        lastScanDate = in.readString();
        scanID = in.readString();

        services = new ArrayList<>();
        in.readTypedList(services, Service.CREATOR);
        vulnerabilityResults = new ArrayList<>();
        in.readTypedList(vulnerabilityResults, VulnerabilityResult.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ip);
        dest.writeString(osFamily);
        dest.writeString(osGen);
        dest.writeInt(osAccuracy);
        dest.writeString(manufacturer);
        dest.writeString(macAddress);
        dest.writeString(lastScanned);
        dest.writeString(lastScanDate);
        dest.writeString(scanID);

        dest.writeTypedList(services);
        dest.writeTypedList(vulnerabilityResults);
    }

    public static final Parcelable.Creator<Host> CREATOR= new Parcelable.Creator<Host>() {

        @Override
        public Host createFromParcel(Parcel source) {
            return new Host(source);  //using parcelable constructor
        }

        @Override
        public Host[] newArray(int size) {
            return new Host[size];
        }
    };

    public String getOs() {
        final String other = "Other";
        if (!osFamily.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append(osFamily);
            if (!osGen.isEmpty()) {
                builder.append(" "+osGen);
            }
            return builder.toString();
        }
        return other;
    }

    public String getIp() {
        return ip;
    }

    public String getOsFamily() {
        return osFamily;
    }

    public String getOsGen() {
        return osGen;
    }

    public Integer getOsAccuracy() {
        return osAccuracy;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setLastScannedResult(String lastScanned){
        this.lastScanned = lastScanned;
    }

    public String getlastScannedResult(){
        return lastScanned;
    }

    public StyledText getRisksFound() {
        if (lastScanned.equals(NEVER)) {
            return new StyledText(NEVER_SCANNED, R.style.never_scanned_text);
        } else {
            Integer numberOfVulns = vulnerabilityResults.size();
            if (numberOfVulns == 0) {
                return new StyledText(NO_RISKS_FOUND, R.style.no_risks_found_text);
            } else {
                return new StyledText(numberOfVulns.toString() + " " + RISKS_FOUND, R.style.risks_found_text);
            }
        }
    }

    public void setScanID(String scanID) {
        this.scanID = scanID;
    }

    public void setLastScanDate(DateTime dateTime){
        lastScanDate = dateTime.toString("dd-MM-yyyy");
    }

    public String getLastScanDate(){
        return lastScanDate;
    }

    public static String getOsDescription(String operatingSystem){
        switch (operatingSystem){
            case "Linux 3.X":
                WelcomeActivity.getAppContext().getString(R.string.linux_3X_description);
            case "Linux 2.6.X":
                return WelcomeActivity.getAppContext().getString(R.string.linux_2X_description);
            case "Windows XP":
                return WelcomeActivity.getAppContext().getString(R.string.windows_xp_description);
        }
        return "";
    }
}

