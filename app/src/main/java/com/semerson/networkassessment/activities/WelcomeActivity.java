package com.semerson.networkassessment.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.Results.ResultsActivity;
import com.semerson.networkassessment.activities.network.NetworkDevices;
import com.semerson.networkassessment.activities.settings.SettingsActivity;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class WelcomeActivity extends AppCompatActivity implements
        OnChartGestureListener,
        OnChartValueSelectedListener {

    public static final boolean TEST_MODE = true;
    public static final boolean TEST_DATA = true;
    public static final boolean TEST_LOGIN = true;

    public static final DateTimeFormatter dateformat = DateTimeFormat.forPattern("dd-MM-yyyy");
    private LineChart lineChart;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public static boolean ADVANCED_MODE;

    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        context = getApplicationContext();

        initialiseTestData(); //TODO This is just temp, for test purposes

        SharedPreferences preferences = getSharedPreferences(AppStorage.APP_PREFERENCE, Context.MODE_PRIVATE);
        ADVANCED_MODE = preferences.getBoolean(AppStorage.ADVANCED_MODE, false);

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                break;
                            case R.id.nav_my_network:
                                Intent activity_vulnScanner = new Intent(WelcomeActivity.this, NetworkDevices.class);
                                WelcomeActivity.this.startActivity(activity_vulnScanner);
                                break;
                            case R.id.nav_assessment_results:
                                if (TEST_MODE) { //TODO REMOVE - GET THIS FROM THE TEST CLASS INSTEAD
                                    AssetManager assetManager = getAssets();
                                    InputStream input;
                                    try {
                                        input = assetManager.open("sample_nmap.txt");
                                        int size = input.available();
                                        byte[] buffer = new byte[size];
                                        input.read(buffer);
                                        input.close();

                                        // byte buffer into a string
                                        String text = new String(buffer);
                                        NetworkDevices networkDevices = new NetworkDevices();
                                        // ScanResults scanResults = networkDevices.processResponseTest(text);
                                        Intent resultActivity = new Intent(WelcomeActivity.this, ResultsActivity.class);
                                        // resultActivity.putExtra("scan-results", scanResults);
                                        startActivity(resultActivity);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            case R.id.nav_sec_awareness:
                                Intent activity_secAwareness = new Intent(WelcomeActivity.this, UserAwareness.class);
                                WelcomeActivity.this.startActivity(activity_secAwareness);
                                break;
                            case R.id.nav_settings:
                                Intent activity_settings = new Intent(WelcomeActivity.this, SettingsActivity.class);
                                WelcomeActivity.this.startActivity(activity_settings);
                                break;
                            case R.id.nav_account:
                                Intent activity_account = new Intent(WelcomeActivity.this, AccountActivity.class);
                                WelcomeActivity.this.startActivity(activity_account);
                                break;
                            default:
                        }
                        return true;
                    }
                });


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

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

        String lastScanDate = preferences.getString(AppStorage.LAST_DISCOVERY_SCAN_DATE, null);
        TextView lastScanDateTxt = findViewById(R.id.lastscandate);
        if (lastScanDate != null) {
            DateTime dt = new DateTime(lastScanDate);
            lastScanDateTxt.setText("Last scan date: " + dt.toString("dd-MM-yyyy"));
        } else {
            lastScanDateTxt.setText("Last scan date: " + "Never!");
        }

        TextView textViewHomePoster = UiObjectCreator.createTextView(this, "Securing your home: https://www.sans.org/sites/default/files/2017-12/STH-Poster-CyberSecureHome-Print_0.pdf",
                R.style.custom_mainbody_text);
        textViewHomePoster.setPadding(0, 20, 0, 0);
        mainLayout.addView(textViewHomePoster);

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

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(dataSets);//TODO

        // set data
        lineChart.setData(data);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initialiseTestData() {
        if (TEST_DATA) {
            String riskScore1 = "43526";
            String riskScore2 = "54652";
            String riskScore3 = "32786";
            String riskScore4 = "25673";
            String riskScore5 = "15782";

            Set<String> riskScores = new LinkedHashSet<>();
            riskScores.add(riskScore1);
            riskScores.add(riskScore2);
            riskScores.add(riskScore3);
            riskScores.add(riskScore4);
            riskScores.add(riskScore5);

            JSONArray jsonArray = new JSONArray(riskScores);

            SharedPreferences preferences = getSharedPreferences(AppStorage.APP_PREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();


            editor.putString(AppStorage.RISK_SCORE_HISTORY, jsonArray.toString());

            DateTimeFormatter dateformat = DateTimeFormat.forPattern("dd-MM-yyyy");
            DateTime date = new DateTime(2018, 6, 1, 0, 0, 0, 0);

            //DateTime currentDate = new DateTime();
           // DateTime parse = dateformat.parseDateTime(currentDate.toString());
           // Log.i("WelcomeActivity Date", parse.toString());


            editor.putString(AppStorage.LAST_DISCOVERY_SCAN_DATE, date.toString());

            //Just for db test purposes
            editor.putBoolean(AppStorage.DATABASE_CREATED, false);
            editor.commit();

            testDropDatabase(true);
        }

        if (TEST_LOGIN) {
            testModeLoginData();
        }

    }

    private void testModeLoginData() {
        AppStorage.putValue(this, AppStorage.NETWORK_NAME, "Stephens Home Network");
        AppStorage.putValue(this, AppStorage.ALIAS_NAME, "Stephen");
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
