package com.semerson.networkassessment.activities.network;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.ActivityID;
import com.semerson.networkassessment.activities.DynamicUI;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.singleview.VulnerabilityDetailsFragment;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.storage.results.Host;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.storage.results.VulnerabilityResult;
import com.semerson.networkassessment.utils.UiObjectCreator;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleNetworkDeviceFragment extends Fragment implements View.OnClickListener, DynamicUI {

    private static final String NETWORK_DEVICE_TITLE = "Device details for host: ";
    private Context context;
    private ScanResults scanResults;
    private ResultController resultController;
    private LinearLayout mainLayout;

    private FragmentHost fragmentHost;

    private Button btnRunScan;
    private Button btnViewResults;

    private NetworkDevices networkDevices;
    private Host host;

    private View view;
    public SingleNetworkDeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        scanResults = AppStorage.getScanResults(context);
        resultController = new ResultController(scanResults.getHosts());
        return inflater.inflate(R.layout.fragment_single_host_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.view = view;
        mainLayout = view.findViewById(R.id.mainLayout);
        btnRunScan = view.findViewById(R.id.btnRunScan);
        btnRunScan.setOnClickListener(this);
        btnViewResults = view.findViewById(R.id.btnViewResults);
        btnViewResults.setOnClickListener(this);

        host = getArguments().getParcelable("host");
        TextView textViewHost = view.findViewById(R.id.txtHostDetailsTitle);
        textViewHost.setText("Host: "+host.getHostname(true));

        LinearLayout warning = view.findViewById(R.id.os_warning_view);

        TextView ostextView = view.findViewById(R.id.txtHostOperatingSystem);
        String os = host.getOs();
        ostextView.setText("Operating System: "+os );


        String osWarningText = resultController.checkVulnerableOs(os);
        if (osWarningText.equals("")){
            warning.setVisibility(View.GONE);
        } else {
            warning.setVisibility(View.VISIBLE);
            TextView textWarning = (TextView) view.findViewById(R.id.txtHostOperatingSystem_warning);
            textWarning.setLinkTextColor(Color.BLUE);
            textWarning.setClickable(true);
            Linkify.addLinks(textWarning, Linkify.WEB_URLS);
            textWarning.setText(osWarningText );
        }

        setDynamicUI();

        TextView textViewMacAddress = view.findViewById(R.id.txtHostMacAddressValue);
        textViewMacAddress.setText(host.getMacAddress());

        TextView textViewIpAddress = view.findViewById(R.id.txtHostIpAddressValue);
        textViewIpAddress.setText(host.getIp());

        TextView textViewVendor = view.findViewById(R.id.txtVendorValue);
        textViewVendor.setText(host.getManufacturer());

    }

    public void setDynamicUI(){
        Host updatedHost = resultController.getUpdatedHost(networkDevices, this.host);
        String lastScanned = updatedHost.getlastScannedResult();
        LinearLayout scanInformationView = view.findViewById(R.id.deviceScannedInfoView);
        scanInformationView.removeAllViews();
        switch (lastScanned) {
            case Host.NEVER:
                scanInformationView.addView(UiObjectCreator.createTextView(context, getString(R.string.device_not_scanned), R.style.never_scanned_text));
                scanInformationView.addView(UiObjectCreator.createTextView(context, getString(R.string.device_not_scanned_instructions), R.style.custom_mainbody_text));
                break;
            case Host.NO_RISKS_FOUND:
                scanInformationView.addView(UiObjectCreator.createTextView(context, getString(R.string.device_no_vulnerabilities), R.style.no_risks_found_text));
                scanInformationView.addView(UiObjectCreator.createTextView(context, getString(R.string.device_no_vulnerabilities_instructions), R.style.custom_mainbody_text));
                btnViewResults.setVisibility(View.VISIBLE);
                break;
            default:
                scanInformationView.addView(UiObjectCreator.createTextView(context, getString(R.string.device_previous_vulnerabilities), R.style.risks_found_text));
                scanInformationView.addView(UiObjectCreator.createTextView(context, getString(R.string.device_previous_vulnerabilities_instructions), R.style.custom_mainbody_text));
                btnViewResults.setVisibility(View.VISIBLE);
        }

        TextView textViewLastScanned = view.findViewById(R.id.txtHostLastScannedValue);
        textViewLastScanned.setText(lastScanned);
    }

    public static SingleNetworkDeviceFragment newInstance(Host host) {
        SingleNetworkDeviceFragment fragment = new SingleNetworkDeviceFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("host", host);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        try {
            fragmentHost = (FragmentHost) context;
            if (context instanceof NetworkDevices) {
                this.networkDevices = (NetworkDevices) context;
            }

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement FragmentHost");
        }
    }

    @Override
    public void onClick(View v) {
        //if (v instanceof TextView) {
         //   String textValue = ((TextView) v).getText().toString();
          //  VulnerabilityResult vulnerability = scanResults.getVulnerabilityInfo(textValue);
           // android.app.Fragment fragment = VulnerabilityDetailsFragment.newInstance(scanResults, vulnerability);
           // fragmentHost.setFragment(fragment, true);
       // }
        switch (v.getId()){
            case R.id.btnRunScan:
                networkDevices.performVulnerabilityScan(host);
                break;
            case R.id.btnViewResults:
                networkDevices.displayResults(host);
                break;
        }
    }

    @Override
    public void updateUI() {
        setDynamicUI();
    }
}
