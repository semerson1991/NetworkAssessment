package com.semerson.networkassessment.activities.network;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.DynamicUI;
import com.semerson.networkassessment.activities.WelcomeActivity;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.storage.results.Host;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.utils.StyledText;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;
import com.semerson.networkassessment.utils.table.TableHeadings;
import com.semerson.networkassessment.utils.table.TableRow;
import com.semerson.networkassessment.utils.table.TableRowData;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetworkDevicesFragment extends Fragment implements View.OnClickListener, DynamicUI {
    private static final String NETWORK_DEVICES_TITLE = "Wi-Fi network: ";
    private static final String NETWORK_DEVICES_FOUND_TITLE = " devices found";
    private Context context;
    private ResultController resultController;
    private LinearLayout mainLayout;

    protected ScanResults scanResults;

    private NetworkDevices networkDevices;

    private FragmentHost fragmentHost;
    private String mitigationType;

    private Button btnFindDevices;
    private Button btnRunScan;

    private View view;
    private View previousTableItems = null;
    private Spinner spinnerNetworkScanType;
    private Spinner spinnerNetworkScanTechniques;
    private EditText hostname;
    private EditText port;

    public NetworkDevicesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        scanResults = AppStorage.getScanResults(context);
        resultController = new ResultController(scanResults.getHosts());
        return inflater.inflate(R.layout.fragment_network_devices, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        this.view = view;
        mainLayout = view.findViewById(R.id.mainLayout);
        btnFindDevices = view.findViewById(R.id.btnFindDevices);
        btnRunScan = view.findViewById(R.id.btnRunScan);

        btnFindDevices.setOnClickListener(this);

        TextView textViewTitle = view.findViewById(R.id.txtNetworkNameTitle);
        textViewTitle.setText(NETWORK_DEVICES_TITLE + AppStorage.getValue(networkDevices, AppStorage.NETWORK_NAME, "N/A"));

        spinnerNetworkScanType = view.findViewById(R.id.spinner_network_mapper_type);
        //spinnerCateogories = view.findViewById(R.id.spinner_categories);

        String[] networkScanTypes;

        if (AppStorage.getValue(WelcomeActivity.getAppContext(), AppStorage.ADVANCED_MODE, false)) {
            spinnerNetworkScanTechniques = view.findViewById(R.id.spinner_network_mapper_technique);
            hostname = view.findViewById(R.id.editHostname);
            port = view.findViewById(R.id.editPortRange);

            networkScanTypes = ScanTypes.getAllNetworkMappingScanTypeAdvanced();
            String[] networkScanTechniques = ScanTypes.getAllNetworkMappingScanTechniques();

            ArrayAdapter<String> adapterNetworkScanTechnique = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, networkScanTechniques);

            adapterNetworkScanTechnique.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerNetworkScanTechniques.setAdapter(adapterNetworkScanTechnique);
        } else {
            networkScanTypes = ScanTypes.getAllNetworkMappingScanType();
        }



        ArrayAdapter<String> adapterNetworkScanType = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, networkScanTypes);


        adapterNetworkScanType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerNetworkScanType.setAdapter(adapterNetworkScanType);
        updateDynamicUI();
    }

    public void updateDynamicUI() {
        Table table = new Table();
        TableCreator tableCreator = new TableCreator();

        if (getView().findViewById(R.id.deviceTableHeader) == null) {
            LinearLayout tableHeader = tableCreator.appendTableHeader(context, mainLayout, TableHeadings.HOST, TableHeadings.LAST_SCANNED, TableHeadings.RISKS);
            tableHeader.setId(R.id.deviceTableHeader);
        }

        if (previousTableItems != null) {
            mainLayout.removeView(previousTableItems);
        }

        ScanResults scanResults = AppStorage.getScanResults(networkDevices);
        resultController.updateHosts(scanResults.getHosts());

        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        List<Host> hosts = scanResults.getHosts();
        if (hosts.size() > 0) {
            btnRunScan.setVisibility(View.VISIBLE);


            for (Host host : hosts) {
                StyledText styledText = host.getRisksFound();

                TableRowData tableRowDataHost = new TableRowData(host.getHostname(true), Gravity.LEFT);
                TableRowData tableRowDataLastScanned = new TableRowData(host.getlastScannedResult(), Gravity.CENTER);
                TableRowData tableRowDataRisksFound = new TableRowData(styledText.getText(), styledText.getStyle(), Gravity.CENTER);

                TableRow tableRow = new TableRow(tableRowDataHost, tableRowDataLastScanned, tableRowDataRisksFound);
                tableRow.setOnClickListener(this);
                tableRow.setTag(host);
                table.appendTableRow(tableRow);
            }
        } else {
            TableRow tableRow = new TableRow(
                    new TableRowData("No Devices", R.style.never_scanned_text, Gravity.CENTER)
            );
            table.appendTableRow(tableRow);
        }
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);
        previousTableItems = mainLayout.getChildAt(mainLayout.getChildCount() - 1); //Store the table contents

        TextView textViewDevicesTitle = view.findViewById(R.id.txtDevicesTitle);
        textViewDevicesTitle.setText(Integer.toString(AppStorage.getScanResults(networkDevices).getHosts().size()) + NETWORK_DEVICES_FOUND_TITLE);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof LinearLayout) {
            Object object = v.getTag();
            if (object instanceof Host) {
                SingleNetworkDeviceFragment singleNetworkDeviceFragment = SingleNetworkDeviceFragment.newInstance((Host) object);
                fragmentHost.setFragment(singleNetworkDeviceFragment, true);
            }
        }

        if (view.getId() == R.id.spinner_network_mapper_type) {
            if (view instanceof Spinner ) {
                if (((Spinner) view).getSelectedItem().toString().equals(ScanTypes.NETWORK_SCAN_CUSTOM)) {
                    view.findViewById(R.id.custom_network_scanning_config).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.custom_network_scanning_config).setVisibility(View.GONE);
                }
            }
        }

        if (v instanceof Button) {
            if (networkDevices != null) {
                switch (v.getId()) {
                    case R.id.btnRunScan:
                        // networkDevices.performVulnerabilityScan();
                        break;
                    case R.id.btnFindDevices:
                        String scanType = spinnerNetworkScanType.getSelectedItem().toString();
                        AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.NETWORK_SCAN_TYPE, scanType);
                        if (scanType.equals(ScanTypes.NETWORK_SCAN_CUSTOM)){
                            String networkScanTechnique = spinnerNetworkScanTechniques.getSelectedItem().toString();
                            AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.NETWORK_SCAN_TECHNIQUE, networkScanTechnique);
                            AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.PORT_RANGE, "");
                            AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.NETWORK_SCAN_HOSTS, "");
                        } else {
                            AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.PORT_RANGE, "");
                            AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.NETWORK_SCAN_TECHNIQUE, "");
                            AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.NETWORK_SCAN_HOSTS, "");
                        }


                        networkDevices.discoverNetworkDevices();
                        break;
                }
            }

        } else if (v instanceof TextView) {
            String textValue = ((TextView) v).getText().toString();
            ScanResults scanResults = AppStorage.getScanResults(networkDevices);
            Host host = scanResults.getHost(textValue);
            SingleNetworkDeviceFragment singleNetworkDeviceFragment = SingleNetworkDeviceFragment.newInstance(host);
            fragmentHost.setFragment(singleNetworkDeviceFragment, true);
        }
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

    public void setConnectionError(String error) {
        TextView tvError = mainLayout.findViewById(R.id.txtConnctionError);
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(error);
    }

    @Override
    public void updateUI() {
        updateDynamicUI();
    }
}