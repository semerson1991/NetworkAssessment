package com.semerson.networkassessment.activities.network;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.Results.MainNavigationFragments.home.singleview.VulnerabilityDetailsFragment;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.storage.results.Host;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.storage.results.VulnerabilityResult;
import com.semerson.networkassessment.utils.UiObjectCreator;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleNetworkDeviceFragment extends Fragment implements View.OnClickListener {

    private static final String NETWORK_DEVICE_TITLE = "Device details for host: ";
    private Context context;
    private ScanResults scanResults;
    private ResultController resultController;
    private LinearLayout mainLayout;

    private FragmentHost fragmentHost;

    private Button btnRunScan;
    private NetworkDevices networkDevices;
    private Host host;

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
        mainLayout = view.findViewById(R.id.mainLayout);
        btnRunScan = view.findViewById(R.id.btnRunScan);
        host = getArguments().getParcelable("host");
        TextView textViewHost = view.findViewById(R.id.txtHostDetailsTitle);
        textViewHost.setText("Host: "+host.getHostname(true));

        TextView ostextView = view.findViewById(R.id.txtHostOperatingSystem);
        String os = host.getOs();
        String osText = resultController.checkVulnerableOs(os);
        ostextView.setText("Operating System "+os + "\n" + osText);

        String lastScanned = networkDevices.getHostLastScanned(host);
        LinearLayout scanInformationView = view.findViewById(R.id.deviceScannedInfoView);
        switch (lastScanned) {
            case NetworkDevices.LAST_SCANNED_NEVER:
                scanInformationView.addView(UiObjectCreator.createTextView(context, getString(R.string.device_not_scanned), R.style.never_scanned_text));
                scanInformationView.addView(UiObjectCreator.createTextView(context, getString(R.string.device_not_scanned_instructions), R.style.custom_mainbody_text));
                break;
            case NetworkDevices.NO_RISKS_FOUND:
                scanInformationView.addView(UiObjectCreator.createTextView(context, getString(R.string.device_no_vulnerabilities), R.style.no_risks_found_text));
                scanInformationView.addView(UiObjectCreator.createTextView(context, getString(R.string.device_no_vulnerabilities_instructions), R.style.custom_mainbody_text));
                break;

            default:
                scanInformationView.addView(UiObjectCreator.createTextView(context, getString(R.string.device_previous_vulnerabilities), R.style.risks_found_text));
                scanInformationView.addView(UiObjectCreator.createTextView(context, getString(R.string.device_previous_vulnerabilities_instructions), R.style.custom_mainbody_text));
        }

        TextView textViewLastScanned = view.findViewById(R.id.txtHostLastScannedValue);
        textViewLastScanned.setText(lastScanned);

        TextView textViewMacAddress = view.findViewById(R.id.txtHostMacAddressValue);
        textViewMacAddress.setText(host.getMacAddress());

        TextView textViewIpAddress = view.findViewById(R.id.txtHostIpAddressValue);
        textViewIpAddress.setText(host.getIp());

        TextView textViewVendor = view.findViewById(R.id.txtVendorValue);
        textViewVendor.setText(host.getManufacturer());

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
        if (v instanceof TextView) {
            String textValue = ((TextView) v).getText().toString();
            VulnerabilityResult vulnerability = scanResults.getVulnerabilityInfo(textValue);
            android.app.Fragment fragment = VulnerabilityDetailsFragment.newInstance(scanResults, vulnerability);
            fragmentHost.setFragment(fragment, true);
        }
    }

}
