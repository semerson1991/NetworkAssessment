package com.semerson.networkassessment.utils.table;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TableCreator {
    /**
     * The labels must be added in the correct order. They will be inserted started from the left column.
     *
     * @param context
     * @param view
     * @param labels
     */
    public LinearLayout appendTableHeader(Context context, LinearLayout view, String... labels) {
        LinearLayout linearLayout = getTitleArea(context);

        for (int i = 0; i < labels.length; i++) {
            linearLayout.addView(addTitle(context, labels[i]));
        }
        view.addView(linearLayout);
        return linearLayout;
    }

    private LinearLayout getTitleArea(Context context) {
        LinearLayout titleArea = new LinearLayout(context);
        titleArea.setOrientation(LinearLayout.HORIZONTAL);
        titleArea.setGravity(Gravity.CENTER);
        titleArea.setPadding(0, 0, 0, 10);

        return titleArea;
    }

    private LinearLayout addTitle(Context context, String title) {
        LinearLayout.LayoutParams titleLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        titleLayoutParams.weight = 1F;
        LinearLayout titleLayout = createLinearLayout(context, titleLayoutParams, Gravity.CENTER, LinearLayout.HORIZONTAL);
        titleLayout.addView(createTextView(context, title, Gravity.CENTER));

        return titleLayout;
    }

    private LinearLayout createLinearLayout(Context context, ViewGroup.LayoutParams layoutParams, int gravity, int orientation) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(orientation);
        linearLayout.setGravity(gravity);
        linearLayout.setLayoutParams(layoutParams);
        return linearLayout;
    }

    private TextView createTextView(Context context, String text, int gravity) {
        TextView textviewText = new TextView(context);
        textviewText.setText(text);
        textviewText.setGravity(gravity);
        textviewText.setPadding(0, 0, 0, 0);
        textviewText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        return textviewText;
    }

    public void createTableViews(Context context, LinearLayout view, Drawable customBoarder, Table table) {
        List<TableRow> tableRows = table.getTableRows();

        for (TableRow tableRow : tableRows) {
            LinearLayout rowLayout = appendTableRow(context, view, customBoarder, tableRow);
            tableRow.setRowLayout(rowLayout);
        }
        //For sorting from highest to lowest value. E.g. number of occurrences
        if (table.getSortedOccurrencesHighToLow()) {
            Collections.sort(tableRows, new Comparator<TableRow>() {
                @Override
                public int compare(TableRow row1, TableRow row2) {
                    return Float.compare(row2.getRowValue(), row1.getRowValue());
                }
            });
        }

        for (TableRow tableRow : tableRows) {
            View tableRowView = tableRow.getLayout();
            // tableRow.getLayout().setBackground(customBoarder);
            tableRowView.setPadding(0, 5, 0, 5);
            int rowId = tableRow.getRowId();
            if (rowId != -1) {
                tableRowView.setId(rowId);
            }
            View.OnClickListener onClickListener = tableRow.getOnClickListener();
            if (onClickListener != null) {
                tableRowView.setOnClickListener(onClickListener);
            }
            int tableRowid = tableRow.getRowId();
            if (tableRowid != -1) {
                tableRowView.setId(tableRowid);
            }

            tableRowView.setTag(tableRow.getTag());

            view.addView(tableRowView);
        }
    }

    /**
     * The labelData must be added in the correct order. The first label will be inserted started from the first column
     *
     * @param context
     * @param view
     * @param customBoarder
     */
    private LinearLayout appendTableRow(Context context, LinearLayout view, Drawable customBoarder, TableRow tableRow) {
        LinearLayout tableRowLayout = new LinearLayout(context);
        tableRowLayout.setOrientation(LinearLayout.HORIZONTAL);


        List<TableRowData> allTableRowData = tableRow.getTableDataRows();
        for (TableRowData tableRowData : allTableRowData) {

            LinearLayout rowDataLayout = createTableDataRowLayout(context, tableRowData.getGravity());

            if (tableRowData.isIncludeLegendIcon()) {
                rowDataLayout.addView(createLegendIcon(context, tableRowData));
            }
            rowDataLayout.setClickable(tableRowData.getClickable());
            rowDataLayout.addView(createTextView(context, tableRowData));
            tableRowLayout.addView(rowDataLayout);
        }
        // tableRowLayout.setBackground(customBoarder);
        return tableRowLayout;
    }

    private LinearLayout createTableDataRowLayout(Context context, int gravity) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1F;
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(gravity);
        layout.setPadding(5, 0, 5, 10);
        layout.setLayoutParams(layoutParams);

        return layout;
    }

    private TextView createTextView(Context context, TableRowData label) {
        TextView txt;
        if (label.getTextStyle() != -1) {
            txt = new TextView(new ContextThemeWrapper(context, label.getTextStyle()));
        } else {
            txt = new TextView(context);
            ;
        }
        txt.setText(label.getLabelname());
        //  txt.setOnClickListener(label.getOnClickListener());
        txt.setEllipsize(TextUtils.TruncateAt.END);
        txt.setMaxLines(7);

        return txt;
    }

    private LinearLayout createLegendIcon(Context context, TableRowData tableRowData) {
        LinearLayout.LayoutParams paramsLegendLayout = new LinearLayout.LayoutParams(
                20, 20);
        paramsLegendLayout.setMargins(0, 10, 20, 0);
        LinearLayout legend_layout = new LinearLayout(context);
        legend_layout.setLayoutParams(paramsLegendLayout);
        legend_layout.setOrientation(LinearLayout.HORIZONTAL);
        legend_layout.setBackgroundColor(tableRowData.getColor());
        return legend_layout;
    }
}
