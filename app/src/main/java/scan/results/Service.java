package scan.results;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Service implements Parcelable {

    private String protocol;
    private String service;
    private Integer port;
    private String state;

    public Service() {

    }

    public void storeJsonValues(JSONObject jsonObject){
        try {
            protocol = jsonObject.getString("protocol");
            service = jsonObject.getString("service");
            port = jsonObject.getInt("port");
            state = jsonObject.getString("state");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
