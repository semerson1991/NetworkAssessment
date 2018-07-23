package com.semerson.networkassessment.activities.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.BaseActivity;
import com.semerson.networkassessment.activities.account.LoginActivity;
import com.semerson.networkassessment.activities.user.awareness.UserAwareness;
import com.semerson.networkassessment.activities.user.awareness.quiz.QuizContract;
import com.semerson.networkassessment.activities.user.awareness.quiz.QuizDbHelper;
import com.semerson.networkassessment.storage.AppStorage;
import com.semerson.networkassessment.utils.UiObjectCreator;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class WelcomeActivity extends BaseActivity implements
        OnChartGestureListener,
        OnChartValueSelectedListener {

    public static final boolean TEST_MODE = false;
    public static final boolean NMAP_TEST_MODE = true;
    public static final boolean OPENVAS_TEST_MODE = true;


    public static final boolean TEST_DATA = true;
    public static final boolean TEST_LOGIN_DATA = false;
    private static final boolean TEST_NETWORK_CONF_DATA = false;

    public static final boolean TEST_DEVICE = true;
    public static final boolean TEST_LOGIN = true;
    public static boolean CLEAR_SCAN_DATA = false;

    public static final DateTimeFormatter dateformat = DateTimeFormat.forPattern("dd-MM-yyyy");
    private LineChart lineChart;

    //   private DrawerLayout mDrawerLayout;
    //  private ActionBarDrawerToggle actionBarDrawerToggle;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        int mainbodyText = R.style.custom_mainbody_text;
        int mainBodyTitleMedium = R.style.custom_mainbody_heading_medium;

        initialiseTestData(); //TODO This is just temp, for test purposes

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        TextView txtViewTip = (TextView) findViewById(R.id.txtTipOfTheDay);
        SpannableStringBuilder spannableStringNetworkBuilderTip = new SpannableStringBuilder();
        spannableStringNetworkBuilderTip.append(Html.fromHtml("<font color=\"black\">  <b>" + "Tip of the day </b><br>"));
        spannableStringNetworkBuilderTip.append("Be extra careful while clicking links and attachments in emails. The sending party may look legitimate. It is very important to be wary before clicking any attachments. Please visit the User Awareness ");

        SpannableString updatesSection = new SpannableString(Html.fromHtml(" <u> 'Phishing' </u>"));
        ClickableSpan clickableText = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent awarenessActivity = new Intent(context, UserAwareness.class);
                awarenessActivity.putExtra(getString(R.string.awarenessKey), R.id.nav_phishing);
                startActivity(awarenessActivity);
            }
        };

        updatesSection.setSpan(clickableText, 0, updatesSection.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        updatesSection.setSpan(new ForegroundColorSpan(Color.BLUE), 0, updatesSection.toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringNetworkBuilderTip.append(updatesSection);
        spannableStringNetworkBuilderTip.append("section for more information.");

        txtViewTip.setText(spannableStringNetworkBuilderTip);
        txtViewTip.setClickable(true);
        txtViewTip.setMovementMethod(LinkMovementMethod.getInstance());
        txtViewTip.setGravity(Gravity.CENTER);


        LinearLayout headerView = findViewById(R.id.headerOpsView);
        if (!AppStorage.getValue(this, AppStorage.LOGGED_IN, false)) {
            headerView.addView(UiObjectCreator.createLoginRequiredTextView(this));
        } else {
            TextView textViewWelcome = new TextView(this);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append(Html.fromHtml("<font color=\"black\"> <b>" + "Welcome " + AppStorage.getValue(this, AppStorage.LOGIN_NAME, "") + "</b>"));

            textViewWelcome.setText(spannableStringBuilder);
            headerView.addView(textViewWelcome);
            if (AppStorage.getValue(this, AppStorage.DEVICE_CONNECTED, false)) {
                TextView networkWelcome = new TextView(this);
                SpannableStringBuilder spannableStringNetworkBuilder = new SpannableStringBuilder();
                spannableStringNetworkBuilder.append(Html.fromHtml("<font color=\"black\"> <b>" + "Network: " + AppStorage.getValue(this, AppStorage.NETWORK_NAME, "") + "</b>"));
                networkWelcome.setText(spannableStringNetworkBuilder);
                headerView.addView(networkWelcome);
                lineChart = (LineChart) findViewById(R.id.linechart);
                lineChart.setOnChartGestureListener(this);
                lineChart.setOnChartValueSelectedListener(this);

                // add data
                try {
                    setData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // get the legend (only possible after setting data)
                Legend l = lineChart.getLegend();

                // modify the legend ...
                // l.setPosition(LegendPosition.LEFT_OF_CHART);
                l.setForm(Legend.LegendForm.LINE);

                Description description = new Description();
                description.setText("Vulnerability Risk Score History");
                lineChart.setDescription(description);
                lineChart.setNoDataText("No Scan History.");
                lineChart.setTouchEnabled(true);
                lineChart.setDragEnabled(true);
                lineChart.setScaleEnabled(true);

                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                lineChart.getXAxis().setLabelCount(1);
                lineChart.getAxisRight().setEnabled(false);

                //  LocalDate date = new LocalDate( 01,06,  2018 );

                String lastScanDate = AppStorage.getValue(WelcomeActivity.getAppContext(), AppStorage.LAST_DISCOVERY_SCAN_DATE, "");
                TextView lastScanDateTxt = findViewById(R.id.lastscandate);
                if (!lastScanDate.equals("")) {
                    DateTime dt = new DateTime(lastScanDate);
                    lastScanDateTxt.setText("Last scan date: " + dt.toString("dd-MM-yyyy"));
                } else {
                    lastScanDateTxt.setText("Last scan date: " + "Never!");
                }
            } else {
                headerView.addView(UiObjectCreator.createNetworkRequiredTextView(this));
            }
        }


        //Description

        mainLayout.addView(UiObjectCreator.createTextView(this, "\nSecuring your home", mainBodyTitleMedium));
        mainLayout.addView(UiObjectCreator.createTextView(context, "https://www.sans.org/sites/default/files/2017-12/STH-Poster-CyberSecureHome-Print_0.pdf", mainbodyText));

        List<Double> lowList = new ArrayList();
        List<Double> medList = new ArrayList();
        List<Double> highList = new ArrayList();
        Random rand = new Random();

        final int low = 20;
        final int med = 5;
        final int high = 3;

        Double lowWeight = 0.25;
        Double medWeight = 0.50;

        for (int i = 0; i < low; i++) {
            lowList.add(rand.nextDouble() * (3.9 - 0.0));
        }

        for (int i = 0; i < med; i++) {
            medList.add(rand.nextDouble() * (4.0 - 6.9));
        }

        for (int i = 0; i < high; i++) {
            highList.add(rand.nextDouble() * (7.0 - 10.0));
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Welc Resume Advanced:", AppStorage.getValue(WelcomeActivity.getAppContext(), AppStorage.ADVANCED_MODE, false).toString());

    }

    @Override
    public void onStop() {
        super.onStop();
        // AppStorage.putValue(this, AppStorage.ADVANCED_MODE, "True");
        Log.i("Welc Stop Advanced:", AppStorage.getValue(WelcomeActivity.getAppContext(), AppStorage.ADVANCED_MODE, false).toString());

    }

    @Override
    public void onPause() {
        super.onPause();
        // Log.i("Welc Pause Advanced:", AppStorage.getValue(WelcomeActivity.getAppContext(), AppStorage.ADVANCED_MODE, "False") );

    }

    // This is used to store Y-axis values
    private ArrayList<Entry> setYAxisValues() throws JSONException {
        SharedPreferences preferences = getSharedPreferences(AppStorage.APP_PREFERENCE, Context.MODE_PRIVATE);
        JSONArray scanHistory = new JSONArray(preferences.getString(AppStorage.RISK_SCORE_HISTORY, null));

        if (scanHistory != null) {
            ArrayList<Entry> yVals = new ArrayList<>();
            Integer count = -1;
            for (Integer i = 0; i < scanHistory.length(); i++) {
                String score = scanHistory.get(i).toString();
                yVals.add(new Entry(i, Float.valueOf(score)));
            }
            return yVals;
        }
        return null;
    }

    private void setData() throws JSONException {

        // ArrayList<String> xVals = setXAxisValues();

        ArrayList<Entry> yVals = setYAxisValues();

        LineDataSet set1;

        // create a dataset and give it a type
        set1 = new LineDataSet(yVals, "Previous Risk Score");
        set1.setFillAlpha(110);

        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(2f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setFillColor(Color.WHITE);

        List<Integer> colors = new ArrayList<>();
        final float lowRisk = 3.5f;
        final float highRisk = 7f;

        for (int i = 0; i < yVals.size() - 1; i++) {
            Entry entry = yVals.get(i + 1);
            if (entry.getY() <= lowRisk) {
                colors.add(Color.GREEN);
            } else if (entry.getY() >= highRisk) {
                colors.add(Color.RED);
            } else {
                colors.add(Color.parseColor("#FFA500"));
            }
        }
        set1.setColors(colors);


        LimitLine upperLimit = new LimitLine(7f, "Critical Risk");
        upperLimit.setLineWidth(4f);
        upperLimit.setEnabled(true);
        upperLimit.setLineColor(Color.RED);
        upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        upperLimit.setTextSize(15f);
        upperLimit.enableDashedLine(10f, 10f, 0);

        LimitLine lowerLimit = new LimitLine(3.5f, "Low Risk");
        lowerLimit.setLineWidth(4f);
        lowerLimit.setEnabled(true);
        lowerLimit.setLineColor(Color.GREEN);
        lowerLimit.enableDashedLine(10f, 10f, 0);
        lowerLimit.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        lowerLimit.setTextSize(15f);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines();
        leftAxis.addLimitLine(upperLimit);
        leftAxis.addLimitLine(lowerLimit);
        leftAxis.enableGridDashedLine(10f, 10f, 0);
        leftAxis.setDrawLimitLinesBehindData(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(dataSets);//TODO

        // set data
        lineChart.setData(data);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart longpressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2,
                             float velocityX, float velocityY) {
        Log.i("Fling", "Chart flinged. VeloX: "
                + velocityX + ", VeloY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    /*
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }
    */
    public void initialiseTestData() {
        Log.i("Welcome Ac", "Initialise Test Data");
        if (TEST_DATA) {
            String riskScore1 = "10";
            String riskScore2 = "3";
            String riskScore3 = "4";
            String riskScore4 = "8";
            String riskScore5 = "7";

            Set<String> riskScores = new LinkedHashSet<>();
            riskScores.add(riskScore1);
            riskScores.add(riskScore2);
            riskScores.add(riskScore3);
            riskScores.add(riskScore4);
            riskScores.add(riskScore5);

            JSONArray jsonArray = new JSONArray(riskScores);

            SharedPreferences preferences = getSharedPreferences(AppStorage.APP_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            //editor.clear();


            editor.putString(AppStorage.RISK_SCORE_HISTORY, jsonArray.toString());

            DateTimeFormatter dateformat = DateTimeFormat.forPattern("dd-MM-yyyy");
            DateTime date = new DateTime(2018, 7, 23, 0, 0, 0, 0);

            //DateTime currentDate = new DateTime();
            // DateTime parse = dateformat.parseDateTime(currentDate.toString());
            // Log.i("WelcomeActivity Date", parse.toString());


            editor.putString(AppStorage.LAST_DISCOVERY_SCAN_DATE, date.toString());

            //Just for db test purposes
            editor.putBoolean(AppStorage.DATABASE_CREATED, false);
            editor.commit();

            testDropDatabase(true);
        }

        if (TEST_LOGIN_DATA) {
            testModeLoginData();
        }
        if (TEST_NETWORK_CONF_DATA) {
            testNetworkConfData();
        }

    }

    private void testModeLoginData() {
        AppStorage.putValue(this, AppStorage.LOGIN_NAME, "a");
        AppStorage.putValue(this, AppStorage.LOGGED_IN, true);
    }

    private void testNetworkConfData() {
        AppStorage.putValue(this, AppStorage.NETWORK_NAME, "Stephens Home Network");
        AppStorage.putValue(this, AppStorage.DEVICE_CONNECTED, true);
    }

    private void testDropDatabase(boolean drop) {
        if (drop) {
            class TestDatabaseHelpers extends SQLiteOpenHelper {

                public TestDatabaseHelpers(Context context) {
                    super(context, QuizDbHelper.DATABASE_NAME, null, QuizDbHelper.DATABASE_VERSION);
                }

                @Override
                public void onCreate(SQLiteDatabase db) {
                    db.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuestionsTable.TABLE_NAME);
                    db.execSQL("DROP TABLE IF EXISTS " + QuizContract.CategoryScore.TABLE_NAME);
                }

                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

                }
            }

            TestDatabaseHelpers dbHelper = new TestDatabaseHelpers(this);
            dbHelper.onCreate(dbHelper.getWritableDatabase());
            dbHelper.close();
        }
    }

    public static Context getAppContext() {
        return WelcomeActivity.context;
    }
}
