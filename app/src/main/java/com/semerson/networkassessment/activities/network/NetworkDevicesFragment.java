package com.semerson.networkassessment.activities.network;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class NetworkDevicesFragment extends Fragment implements View.OnClickListener, DynamicUI, AdapterView.OnItemSelectedListener {
    private static final String NETWORK_DEVICES_TITLE = "Wi-Fi network: ";
    private static final String NETWORK_DEVICES_FOUND_TITLE = " devices found";
    private Context context;
    private ResultController resultController;
    private LinearLayout mainLayout;
    private LinearLayout customOptionLayout;

    protected ScanResults scanResults;

    private NetworkDevices networkDevices;

    private FragmentHost fragmentHost;
    private String mitigationType;

    private Button btnViewResults;
    private Button btnFindDevices;
    private Button btnRunScan;
    private LinearLayout vulnScanLayout;

    private View view;
    private View previousTableItems = null;
    private TextView tvError;

    private Spinner spinnerNetworkScanType;
    private Spinner spinnerVulnScanType;
    private Spinner spinnerNetworkScanTechniques;
    private Spinner spinnerDetectionOptions;
    private EditText edtHostname;
    private EditText edtPortRange;
    private EditText edtCustomArgs;
    private TextView networkScanWarning;

    private int check = 0; //Used to check if the initialization has been calling actions in the spinner listener

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
        tvError = mainLayout.findViewById(R.id.txtConnectionError);
        tvError.setVisibility(View.GONE);
        btnFindDevices = view.findViewById(R.id.btnFindDevices);
        btnRunScan = view.findViewById(R.id.btnRunScan);
        btnViewResults = view.findViewById(R.id.btnViewResults);

        btnFindDevices.setOnClickListener(this);
        btnRunScan.setOnClickListener(this);
        btnViewResults.setOnClickListener(this);

        customOptionLayout = view.findViewById(R.id.custom_network_scanning_config);
        vulnScanLayout = view.findViewById(R.id.vulnScanLayout);

        TextView textViewTitle = view.findViewById(R.id.txtNetworkNameTitle);
        textViewTitle.setText(NETWORK_DEVICES_TITLE + AppStorage.getValue(WelcomeActivity.getAppContext(), AppStorage.NETWORK_NAME, "N/A"));
        networkScanWarning = view.findViewById(R.id.networkScanWarning);
        networkScanWarning.setVisibility(View.GONE);

        spinnerNetworkScanType = view.findViewById(R.id.spinner_network_mapper_type);
        spinnerVulnScanType = view.findViewById(R.id.spinner_vuln_scan_type);

        String[] networkScanTypes;
        String[] vulnerabilityScanTypes;
        if (AppStorage.getValue(WelcomeActivity.getAppContext(), AppStorage.ADVANCED_MODE, false)) {
            spinnerNetworkScanTechniques = view.findViewById(R.id.spinner_network_mapper_technique);
            spinnerDetectionOptions = view.findViewById(R.id.spinner_network_mapper_detection);
            edtHostname = view.findViewById(R.id.editHostname);
            edtPortRange = view.findViewById(R.id.editPortRange);
            edtCustomArgs = view.findViewById(R.id.editAdditionalArguments);
            networkScanTypes = ScanTypes.getAllNetworkMappingScanTypeAdvanced();
            vulnerabilityScanTypes = ScanTypes.getAllVulnerabilityScanTypeAdvanced();
            String[] networkScanTechniques = ScanTypes.getAllNetworkMappingScanTechniques();
            String[] networkDetectionOptions = ScanTypes.getDetectionOptions();

            ArrayAdapter<String> adapterNetworkScanTechnique = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, networkScanTechniques);

            ArrayAdapter<String> adapterNetworkScanDetection = new ArrayAdapter<String>(context,
                    android.R.layout.simple_spinner_item, networkDetectionOptions);

            adapterNetworkScanTechnique.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapterNetworkScanDetection.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerNetworkScanTechniques.setAdapter(adapterNetworkScanTechnique);
            spinnerDetectionOptions.setAdapter(adapterNetworkScanDetection);
        } else {
            networkScanTypes = ScanTypes.getAllNetworkMappingScanType();
            vulnerabilityScanTypes = ScanTypes.getAllVulnerabilityScanType();
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();


            spannableStringBuilder.append(Html.fromHtml("<font color=\"red\"> <b>" + getString(R.string.network_scan_warning) + "</b>"));
            spannableStringBuilder.append(System.getProperty("line.separator"));
            networkScanWarning.setText(spannableStringBuilder);
        }
        customOptionLayout.setVisibility(View.GONE);

        ArrayAdapter<String> adapterNetworkScanType = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, networkScanTypes);
        ArrayAdapter<String> adapterVulnerabilityScanType = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, vulnerabilityScanTypes);

        adapterNetworkScanType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterVulnerabilityScanType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerNetworkScanType.setAdapter(adapterNetworkScanType);
        spinnerNetworkScanType.setOnItemSelectedListener(this);

        spinnerVulnScanType.setAdapter(adapterVulnerabilityScanType);

        updateDynamicUI();
    }

    public void updateDynamicUI() {
        Table table = new Table();
        TableCreator tableCreator = new TableCreator();

        if (getView().findViewById(R.id.deviceTableHeader) == null) {
            LinearLayout tableHeader = tableCreator.appendTableHeader(context, mainLayout, TableHeadings.HOST, TableHeadings.LAST_SCANNED, TableHeadings.RISKS);
            tableHeader.setId(R.id.deviceTableHeader);
        }

        //Remove Table rows before applying new ones to avoid duplicates
        if (mainLayout.getTag() instanceof Integer) {
            Integer previousRowsSize = (Integer) mainLayout.getTag();
            int layoutTotalCount = mainLayout.getChildCount() - 1;
            for (int i = 0; i < previousRowsSize; i++) {
                mainLayout.removeView(mainLayout.getChildAt(layoutTotalCount - i));
            }
        }

        ScanResults scanResults = AppStorage.getScanResults(networkDevices);
        resultController.updateHosts(scanResults.getHosts());

        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        List<Host> hosts = scanResults.getHosts();
        btnViewResults.setVisibility(View.GONE);
        boolean viewDisplayed = false;
        if (hosts.size() > 0) {
            vulnScanLayout.setVisibility(View.VISIBLE);

            for (Host host : hosts) {
                StyledText styledText = host.getRisksFound();

                TableRowData tableRowDataHost = new TableRowData(host.getHostname(true), Gravity.LEFT);
                TableRowData tableRowDataLastScanned = new TableRowData(host.getLastScanDate(), Gravity.CENTER);
                TableRowData tableRowDataRisksFound = new TableRowData(styledText.getText(), styledText.getStyle(), Gravity.CENTER);

                TableRow tableRow = new TableRow(tableRowDataHost, tableRowDataLastScanned, tableRowDataRisksFound);
                tableRow.setOnClickListener(this);
                tableRow.setTag(host);
                table.appendTableRow(tableRow);

                if (!viewDisplayed && !host.getLastScanDate().equals(Host.NEVER)) {
                    viewDisplayed = false;
                    btnViewResults.setVisibility(View.VISIBLE);
                }
            }
        } else {
            vulnScanLayout.setVisibility(View.GONE);
            TableRow tableRow = new TableRow(
                    new TableRowData("No Devices", R.style.never_scanned_text, Gravity.CENTER)
            );
            table.appendTableRow(tableRow);
        }
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);

        Integer size = hosts.size() == 0 ? 1 : hosts.size(); //Remove the 'No devices' else remove the number of hosts displayed
        mainLayout.setTag(size); //This is to remove rows when the view is updated
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

        if (v instanceof Button) {
            if (networkDevices != null) {
                switch (v.getId()) {
                    case R.id.btnRunScan:
                        tvError.setVisibility(View.GONE);
                        String vulnerabilityScanType = spinnerVulnScanType.getSelectedItem().toString();
                        AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.VULN_SCAN_TYPE, vulnerabilityScanType);

                        List<Host> allHosts = AppStorage.getScanResults(networkDevices).getHosts();
                        Host[] hosts = allHosts.toArray(new Host[allHosts.size()]);
                        networkDevices.performVulnerabilityScan(hosts);
                        break;
                    case R.id.btnFindDevices:
                        tvError.setVisibility(View.GONE);

                        String scanType = spinnerNetworkScanType.getSelectedItem().toString();
                        AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.NETWORK_SCAN_TYPE, scanType);
                        if (scanType.equals(ScanTypes.NETWORK_SCAN_CUSTOM)) {
                            String networkScanTechnique = spinnerNetworkScanTechniques.getSelectedItem().toString();
                            String portRange = edtPortRange.getText().toString();
                            String hostsToScan = edtHostname.getText().toString();
                            String customCommands = edtCustomArgs.getText().toString();
                            String networkMappingDetection = spinnerDetectionOptions.getSelectedItem().toString();

                            AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.NETWORK_SCAN_TECHNIQUE, networkScanTechnique);
                            AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.PORT_RANGE, portRange);
                            AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.NETWORK_SCAN_HOSTS, hostsToScan);
                            AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.NETWORK_SCAN_CUSTOM_ARGS, customCommands);
                            AppStorage.putValue(WelcomeActivity.getAppContext(), AppStorage.NETWORK_MAPPING_DETECTION, networkMappingDetection);
                        }
                        networkDevices.discoverNetworkDevices();
                        break;
                    case R.id.btnViewResults:
                            networkDevices.displayResultsAll();
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
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(error);
    }

    @Override
    public void updateUI() {
        updateDynamicUI();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (++check > 1) {
            String selectedItem = parent.getItemAtPosition(position).toString();
            if (selectedItem.equals(ScanTypes.NETWORK_SCAN_CUSTOM)) {
                customOptionLayout.setVisibility(View.VISIBLE);
            } else {
                customOptionLayout.setVisibility(View.GONE);
            }
            if (!AppStorage.getValue(WelcomeActivity.getAppContext(), AppStorage.ADVANCED_MODE, false)) {
                if (!selectedItem.equals(ScanTypes.NETWORK_SCAN_DEFAULT)) {
                    networkScanWarning.setVisibility(View.VISIBLE);
                } else {
                    networkScanWarning.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}