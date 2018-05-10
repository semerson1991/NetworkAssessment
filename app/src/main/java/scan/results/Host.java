package scan.results;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Host implements Parcelable, JsonStorage{

    private List<Service> services;
    private String ip;
    private String os;

    public Host(){
        services = new ArrayList<>();
    }

    public void storeJsonValues(JSONObject host){
        try {
            this.ip = host.getString("ip");
            this.os = host.getString("op");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public Host(Parcel in){
        String[] data = new String[1];

        in.readStringArray(data);
        ip = data[0];
        os = data[1];
        services = (ArrayList<Service>) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{ip,os});
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
}

