package com.codeshastra.coderr.provideameal;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static int navSelectedItem = -1;
    private Toolbar toolbar;
    public GoogleApiClient googleApiClient;
    public static boolean returnFromListActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frame_for_fragments, new FragmentHome()).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1);
        }

        if (returnFromListActivity)
            navSelectedItem = -1;

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (navSelectedItem != 1) {
            getFragmentManager().beginTransaction().replace(R.id.frame_for_fragments, new FragmentHome()).commit();
            navSelectedItem = 1;
            toolbar.setTitle("Home");
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                if (navSelectedItem == 1)
                    break;
                getFragmentManager().beginTransaction().replace(R.id.frame_for_fragments, new FragmentHome()).commit();
                navSelectedItem = 1;
                toolbar.setTitle("Home");
                break;

            case R.id.nav_donate:
                if (navSelectedItem == 2)
                    break;
                getFragmentManager().beginTransaction().replace(R.id.frame_for_fragments, new FragmentDonate()).commit();
                navSelectedItem = 2;
                toolbar.setTitle("Donate a Meal");
                break;

            case R.id.nav_vision:
                if (navSelectedItem == 3)
                    break;
                getFragmentManager().beginTransaction().replace(R.id.frame_for_fragments, new FragmentVision()).commit();
                navSelectedItem = 3;
                toolbar.setTitle("Mission and Vision");
                break;

            case R.id.nav_gift:
                if (navSelectedItem == 4)
                    break;
                getFragmentManager().beginTransaction().replace(R.id.frame_for_fragments, new FragmentGift()).commit();
                navSelectedItem = 4;
                toolbar.setTitle("Gift a Meal");
                break;

            case R.id.nav_register:
                if (navSelectedItem == 5)
                    break;
                getFragmentManager().beginTransaction().replace(R.id.frame_for_fragments, new FragmentRegister()).commit();
                navSelectedItem = 5;
                toolbar.setTitle("Join Us");
                break;

            case R.id.nav_login:
                if (navSelectedItem == 6)
                    break;
                getFragmentManager().beginTransaction().replace(R.id.frame_for_fragments, new FragmentLogin()).commit();
                navSelectedItem = 6;
                toolbar.setTitle("Login");
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void intentBox8(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.box8.in"));
        startActivity(intent);
    }

    public void intentHolachef(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.holachef.com"));
        startActivity(intent);
    }

    public void intentFoodpanda(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.foodpanda.in"));
        startActivity(intent);
    }

    public void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("APICLIENT", "Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("APICLIENT", "Connection Failed");
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("APICLIENT", "Connected");
    }
}

