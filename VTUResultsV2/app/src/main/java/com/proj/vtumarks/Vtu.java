package com.proj.vtumarks;

import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

public class Vtu extends ActionBarActivity implements OnClickListener {

    TextView display;
    EditText input1;
    Button b1;
    String usn = null;
    String input = "http://results.vtu.ac.in/vitavi.php?rid=";
    Boolean asubmit = false;
    Button bStartService, bStopService;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerAdapter;
    private String[] leftSliderData = {"About Us", "Contact Us/Feedback", "Settings", "Exit"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nitView();
        if (toolbar != null) {
            toolbar.setTitle("VTU Results");
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
        }
        initDrawer();

        input1 = (EditText) findViewById(R.id.editText1);
        b1 = (Button) findViewById(R.id.button1);
        display = (TextView) findViewById(R.id.textView1);
        bStartService = (Button) findViewById(R.id.button2);
        bStopService = (Button) findViewById(R.id.button3);

        bStartService.setOnClickListener(this);
        bStopService.setOnClickListener(this);

        SharedPreferences getPrefs2 = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        usn = getPrefs2.getString("name", "");
        input1.setText(usn);
        asubmit = getPrefs2.getBoolean("a_submit", false);

        if (asubmit) {
            if (!isNetworkAvailable()) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        Vtu.this);
                alertDialog.setTitle("No Internet Connectivity");
                alertDialog
                        .setMessage("Please connect to the internet and then continue");
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setNeutralButton("OK", null);
                alertDialog.show();

            } else if (usn.matches("")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        Vtu.this);
                alertDialog.setTitle("Default USN requlred");
                alertDialog
                        .setMessage("Please set 'Default USN' in preferences to use auto submit feature");
                alertDialog.setIcon(R.drawable.delete);
                alertDialog.setNeutralButton("OK", null);
                alertDialog.show();
            } else {
                usn += "&submit=SUBMIT";
                input += usn;
                Bundle basket = new Bundle();
                basket.putString("key", input);
                Intent display_res = new Intent(Vtu.this, ParseUSN.class);
                display_res.putExtras(basket);
                startActivity(display_res);
                input = "http://results.vtu.ac.in/vitavi.php?rid=";
            }
        }

        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                usn = input1.getText().toString();
                if (!isNetworkAvailable()) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            Vtu.this);
                    alertDialog.setTitle("No Internet Connectivity");
                    alertDialog
                            .setMessage("Please connect to the internet and then continue");
                    alertDialog.setIcon(R.drawable.delete);
                    alertDialog.setNeutralButton("OK", null);
                    alertDialog.show();

                } else {
                    if (usn.matches("")) {

                        SharedPreferences getPrefs2 = PreferenceManager
                                .getDefaultSharedPreferences(getBaseContext());
                        usn = getPrefs2.getString("name", "");
                        input1.setText(usn);

                    }
                    if (usn.matches("")) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                Vtu.this);
                        alertDialog.setTitle("Usn Field is Empty");
                        alertDialog
                                .setMessage("Please enter USN in the field above or set 'default USN' in Preferences");
                        alertDialog.setIcon(R.drawable.delete);
                        alertDialog.setNeutralButton("OK", null);
                        alertDialog.show();
                    } else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input1.getWindowToken(), 0);
                        usn += "&submit=SUBMIT";
                        input += usn;
                        Bundle basket = new Bundle();
                        basket.putString("key", input);
                        // new loadUsn().execute(input);
                        Intent display_res = new Intent(Vtu.this,
                                ParseUSN.class);
                        display_res.putExtras(basket);
                        startActivity(display_res);
                        input = "http://results.vtu.ac.in/vitavi.php?rid=";
                    }
                }
            }
        });

    }

    private void nitView() {
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationDrawerAdapter = new ArrayAdapter<String>(Vtu.this, android.R.layout.simple_list_item_1, leftSliderData);
        leftDrawerList.setAdapter(navigationDrawerAdapter);
        leftDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            //drawerLayout.closeDrawers();
            switch (position) {
                case 0:
                    Intent i = new Intent("android.intent.action.ABOUTUS_VTU");
                    startActivity(i);
                    break;
                case 1:
                    Intent j = new Intent("android.intent.action.EMAIL_US");
                    startActivity(j);
                    break;
                case 2:
                    Intent k = new Intent("android.intent.action.PREFS_VTU");
                    startActivity(k);
                    break;
                case 3:
                    finish();
                    break;


            }
        }
    }

    private void initDrawer() {

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button2:
                SharedPreferences getPrefs2 = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());
                String usn_noti = getPrefs2.getString("name", "");
                if (usn_noti.matches("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            Vtu.this);
                    alertDialog.setTitle("Set default USN");
                    alertDialog
                            .setMessage("Please set 'Default USN' in preferences to continue.");
                    alertDialog.setIcon(R.drawable.delete);
                    alertDialog.setNeutralButton("OK", null);
                    alertDialog.show();
                }
                // the following elseif condition is made useless by the removal of
                // en (enable notification) option from preferences
            /*
             * else if (!enotification) { AlertDialog.Builder alertDialog = new
			 * AlertDialog.Builder( Vtu.this);
			 * alertDialog.setTitle("Enable Notification"); alertDialog
			 * .setMessage
			 * ("Please enable notification option in preferences to continue");
			 * alertDialog.setIcon(R.drawable.delete);
			 * alertDialog.setNeutralButton("OK", null); alertDialog.show(); }
			 */
                else {
                    String usn_toast = getPrefs2.getString("name", "");
                    Toast.makeText(getApplicationContext(),
                            "Polling Started for USN: " + usn_toast,
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(),
                            "You may now close this app", Toast.LENGTH_LONG).show();
                    // Intent mIntent = new Intent();
                    // mIntent.setClass(this, StartedServiceVTU.class);
                    // startService(mIntent);
                    Intent ialarm = new Intent("android.intent.action.ALARMINI");
                    ialarm.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    ialarm.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    ialarm.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(ialarm);
                }
                // finish();
                break;
            case R.id.button3:
                Toast.makeText(getApplicationContext(), "Polling Stoped",
                        Toast.LENGTH_LONG).show();
                Intent intentstop = new Intent(this, AlarmReceiver.class);
                PendingIntent senderstop = PendingIntent.getBroadcast(this, 900900,
                        intentstop, 0);
                AlarmManager alarmManagerstop = (AlarmManager) getSystemService(ALARM_SERVICE);

                alarmManagerstop.cancel(senderstop);
                break;
            default:
                break;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (usn.matches("")) {
            SharedPreferences getPrefs2 = PreferenceManager
                    .getDefaultSharedPreferences(getBaseContext());
            usn = getPrefs2.getString("name", "");
            input1.setText(usn);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


}
