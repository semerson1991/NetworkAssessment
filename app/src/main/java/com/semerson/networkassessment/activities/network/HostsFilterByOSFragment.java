package com.semerson.networkassessment.activities.network;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.text.util.Linkify;
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
public class HostsFilterByOSFragment extends Fragment {

    public static final String OS_KEY = "OS_KEY";
    public static final String DESCRIPTION = "<b> Description: </b>";
    private String os;
    private Context context;
    private NetworkDevices networkDevices;
    private ScanResults scanResults;
    private ResultController resultController;
    private LinearLayout mainLayout;

    private FragmentHost fragmentHost;

    private static final String TITLE = "Hosts with Operating System: ";

    public HostsFilterByOSFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        resultController = new ResultController(AppStorage.getScanResults(networkDevices).getHosts());
        os = getArguments().getString(OS_KEY);
        return inflater.inflate(R.layout.fragment_hosts_filter_by_os, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Drawable customBoarder = getResources().getDrawable(R.drawable.customboarder_top_bottom_isvisible);

        mainLayout = view.findViewById(R.id.mainLayout);
        TextView textTitle = view.findViewById(R.id.title);
        TextView textDesc = view.findViewById(R.id.description);
        textDesc.setClickable(true);
        textDesc.setLinkTextColor(Color.BLUE);
        Linkify.addLinks(textDesc, Linkify.WEB_URLS);

        textTitle.setText(TITLE + os);
        String description = Host.getOsDescription(os);
        if (!description.equals("")) {
            textDesc.setText(Html.fromHtml(DESCRIPTION + Host.getOsDescription(os)));
        }
        List<Host> hosts = resultController.getHostsFilterByOS(os);
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
                tableRow.setTag(host);
                table.appendTableRow(tableRow);
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


    public static HostsFilterByOSFragment newInstance(String operatingSystem) {
        HostsFilterByOSFragment fragment = new HostsFilterByOSFragment();
        Bundle bundle = new Bundle();
        bundle.putString(OS_KEY, operatingSystem);
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
