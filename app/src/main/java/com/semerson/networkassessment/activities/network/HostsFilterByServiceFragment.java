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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.controller.FragmentHost;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.storage.results.Host;
import com.semerson.networkassessment.storage.results.ResultController;
import com.semerson.networkassessment.storage.results.ScanResults;
import com.semerson.networkassessment.storage.results.Service;
import com.semerson.networkassessment.utils.StyledText;
import com.semerson.networkassessment.utils.UiObjectCreator;
import com.semerson.networkassessment.utils.table.Table;
import com.semerson.networkassessment.utils.table.TableCreator;
import com.semerson.networkassessment.utils.table.TableHeadings;
import com.semerson.networkassessment.utils.table.TableRow;
import com.semerson.networkassessment.utils.table.TableRowData;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HostsFilterByServiceFragment extends Fragment {
    public static final String SERVICE_KEY = "SERVICE_KEY";
    private String service;
    private Context context;
    private NetworkDevices networkDevices;
    private ScanResults scanResults;
    private ResultController resultController;
    private LinearLayout mainLayout;

    private FragmentHost fragmentHost;

    private static final String TITLE = "Hosts with service: ";
    private static final String DESCRIPTION = "Service Description: ";
    public HostsFilterByServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        resultController = new ResultController(AppStorage.getScanResults(networkDevices).getHosts());
        service =  getArguments().getString(SERVICE_KEY);
        return inflater.inflate(R.layout.fragment_hosts_filter_by_os, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        mainLayout = view.findViewById(R.id.mainLayout);
        TextView textTitle = view.findViewById(R.id.title);
        TextView textDesc = view.findViewById(R.id.description);

        textTitle.setText(TITLE + service);

        textDesc.setText(UiObjectCreator.createTextWithBoldTitle(DESCRIPTION, Service.getServiceDescription(service)));

        List<Host> hosts = resultController.getHostsFilterByService(service);
        Table table = new Table();
        TableCreator tableCreator = new TableCreator();
        if (hosts.size() > 0) {
            for (Host host : hosts) {
                StyledText risksFound = host.getRisksFound();

                TableRowData tableRowDataHost = new TableRowData(host.getHostname(true), Gravity.LEFT);
                TableRowData tableRowDataLastScanned = new TableRowData(host.getLastScanDate(), Gravity.CENTER);
                TableRowData tableRowDataRisksFound = new TableRowData(risksFound.getText(), risksFound.getStyle(), Gravity.CENTER);
                tableRowDataHost.setRowId(R.id.rowDataListener);

                TableRow tableRow = new TableRow(tableRowDataHost, tableRowDataLastScanned, tableRowDataRisksFound);
                tableRow.setOnClickListener(networkDevices);
                tableRow.setRowId(R.id.rowListener);
                table.appendTableRow(tableRow);
                tableRow.setTag(host);
            }
        } else {
            TableRow tableRow = new TableRow(
                    new TableRowData("No Devices", R.style.never_scanned_text, Gravity.CENTER)
            );
            table.appendTableRow(tableRow);
        }
        tableCreator.appendTableHeader(context, mainLayout, TableHeadings.HOST, TableHeadings.LAST_SCANNED, TableHeadings.RISKS);
        tableCreator.createTableViews(context, mainLayout, customBoarder, table);
    }


    public static HostsFilterByServiceFragment newInstance(String service) {
        HostsFilterByServiceFragment fragment = new HostsFilterByServiceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SERVICE_KEY, service);
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
}