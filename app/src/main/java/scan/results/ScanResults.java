package scan.results;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ScanResults {
    private List<Host> hosts;

    public ScanResults(){
        hosts = new ArrayList<>();
    }

    public Host appendHost(JSONObject jsonHost){
        Host host = new Host();
        host.storeJsonValues(jsonHost);
        try {
            JSONArray jsonServices = jsonHost.getJSONArray("services");
            host.addService(jsonServices);
            hosts.add(host);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return host;
    }
}
