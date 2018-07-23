package com.semerson.networkassessment.storage.results;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.home.WelcomeActivity;
import com.semerson.networkassessment.activities.network.TechnicalAndFriendlyName;

import org.json.JSONException;
import org.json.JSONObject;

public class Service implements Parcelable {

    public static final String FTP_FRIENDLY = "File Transfer Service";
    public static final String FTP_TECHNICAL = "FTP";
    public static final String SSH_FRIENDLY = "Secure Remote Login";
    public static final String SSH_TECHNICAL = "SSH";
    public static final String TELNET_FRIENDLY = "Remote Authentication";
    public static final String TELNET_TECHNICAL = "TELNET";
    public static final String SMTP_FRIENDLY = "Email service";
    public static final String SMTP_TECHNICAL = "SMTP";
    public static final String DNS_FRIENDLY = "Address Friendly Name Service";
    public static final String DNS_TECHNICAL = "DNS";
    public static final String HTTP_FRIENDLY = "Website Service";
    public static final String HTTP_TECHNICAL = "HTTP";
    public static final String NBT_FRIENDLY = "Windows Communication Service";
    public static final String NBT_TECHNICAL = "NBT";

    public static final String SMB_FRIENDLY = "Windows File Sharing";
    public static final String SMB_TECHNICAL = "SMB";

    public static final String NFS_FRIENDLY = "Network File System Service";
    public static final String NFS_TECHNICAL = "NFS";

    public static final String MYSQL_FRIENDLY = "Database Service (data storage)";
    public static final String MYSQL_TECHNICAL = "My MSQL";

    public static final String POSTGRESQL_FRIENDLY = "Database Service (data storage) ";
    public static final String POSTGRESQL_TECHNICAL = "PostGreSQL";

    public static final String VNC_FRIENDLY = "Desktop Sharing System";
    public static final String VNC_TECHNICAL = "VNC";

    public static final String IRC_FRIENDLY = "Internet Chat Communication";
    public static final String IRC_TECHNICAL = "IRC";

    public static final String NA = "N/A";

    private String protocol;
    private String service;
    private Integer port;
    private String state;

    public Service() {

    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public TechnicalAndFriendlyName getService() {
        switch (service) {
            case "ftp":
                return new TechnicalAndFriendlyName(FTP_FRIENDLY, FTP_TECHNICAL);
            case "ssh":
                return new TechnicalAndFriendlyName(SSH_FRIENDLY, SSH_TECHNICAL);
            case "telnet":
                return new TechnicalAndFriendlyName(TELNET_FRIENDLY, TELNET_TECHNICAL);
            case "smtp":
                return new TechnicalAndFriendlyName(SMTP_FRIENDLY, SMTP_TECHNICAL);
            case "domain":
                return new TechnicalAndFriendlyName(DNS_FRIENDLY, DNS_TECHNICAL);
            case "http":
                return new TechnicalAndFriendlyName(HTTP_FRIENDLY, HTTP_TECHNICAL);
            case "rpcblind":
                return new TechnicalAndFriendlyName("REMOVE", "REMOVE");
            case "netbios-ssn":
                return new TechnicalAndFriendlyName(NBT_FRIENDLY, NBT_TECHNICAL);
            case "microsoft-ds":
                return new TechnicalAndFriendlyName(SMB_FRIENDLY, SMB_TECHNICAL);
            case "nfs":
                return new TechnicalAndFriendlyName(NFS_FRIENDLY, NFS_TECHNICAL);
            case "mysql":
                return new TechnicalAndFriendlyName(MYSQL_FRIENDLY, MYSQL_TECHNICAL);
            case "postgresql":
                return new TechnicalAndFriendlyName(POSTGRESQL_FRIENDLY, POSTGRESQL_TECHNICAL);
            case "vnc":
                return new TechnicalAndFriendlyName(VNC_FRIENDLY, VNC_TECHNICAL);
            case "irc":
            return new TechnicalAndFriendlyName(IRC_FRIENDLY, IRC_TECHNICAL);
            default:
                return new TechnicalAndFriendlyName(NA, service);
        }
    }

    public static String getServiceDescription(String service) {
        switch (service) {
            case FTP_FRIENDLY:
            case FTP_TECHNICAL:
                return WelcomeActivity.getAppContext().getString(R.string.ftp_description);
            case SSH_FRIENDLY:
            case SSH_TECHNICAL:
                return WelcomeActivity.getAppContext().getString(R.string.ssh_description);
            case TELNET_FRIENDLY:
            case TELNET_TECHNICAL:
                return WelcomeActivity.getAppContext().getString(R.string.telnet_description);
            case SMTP_FRIENDLY:
                SMTP_TECHNICAL:
                return WelcomeActivity.getAppContext().getString(R.string.smtp_description);
            case DNS_FRIENDLY:
            case DNS_TECHNICAL:
                return WelcomeActivity.getAppContext().getString(R.string.domain_description);
            case HTTP_FRIENDLY:
                HTTP_TECHNICAL:
                return WelcomeActivity.getAppContext().getString(R.string.http_description);
            case "rpcblind":
                return Resources.getSystem().getString(R.string.rpcblind_description);
            case NBT_FRIENDLY:
            case NBT_TECHNICAL:
                return WelcomeActivity.getAppContext().getString(R.string.netbios_ssn_description);
            case SMB_FRIENDLY:
            case SMB_TECHNICAL:
                return WelcomeActivity.getAppContext().getString(R.string.microsoft_ds_description);
            case NFS_FRIENDLY:
            case NFS_TECHNICAL:
                return WelcomeActivity.getAppContext().getString(R.string.nfs_description);
            case MYSQL_FRIENDLY:
                MYSQL_TECHNICAL:
                return WelcomeActivity.getAppContext().getString(R.string.mysql_description);
            case POSTGRESQL_FRIENDLY:
                POSTGRESQL_TECHNICAL:
                return WelcomeActivity.getAppContext().getString(R.string.postgresql_description);
            case VNC_FRIENDLY:
                VNC_TECHNICAL:
                return WelcomeActivity.getAppContext().getString(R.string.vnc_description);
            case IRC_FRIENDLY:
                IRC_TECHNICAL:
                return WelcomeActivity.getAppContext().getString(R.string.irc_description);
            default:
                return "";
        }
    }

    public void setService(String service) {
        this.service = service;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Service(Parcel in) {
        protocol = in.readString();
        service = in.readString();
        port = in.readInt();
        state = in.readString();
    }

    public void storeJsonValues(JSONObject jsonObject) {
        try {
            protocol = jsonObject.getString("protocol");
            service = jsonObject.getString("service");
            port = jsonObject.getInt("port");
            state = jsonObject.getString("state");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(protocol);
        dest.writeString(service);
        dest.writeInt(port);
        dest.writeString(state);
    }
}
