package com.codeshastra.coderr.provideameal;


import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentLogin extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public EditText emailEditText;
    public EditText passworkEditText;

    public TextInputLayout emailTextInputLayout;
    public TextInputLayout passwordTextInputLayout;

    public GoogleApiClient googleApiClient;
    public Location lastLocation;
    public double latitude;
    public double longitude;
    String token;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private ProgressDialog progressDialog;

    public FragmentLogin() {
        // Required empty public constructor
    }

    public void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailEditText = (EditText) view.findViewById(R.id.edit_email);
        passworkEditText = (EditText) view.findViewById(R.id.edit_password);

        progressDialog = new ProgressDialog(getActivity());

        emailTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_ip_layout_email);
        passwordTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_ip_layout_password);

        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));
        passworkEditText.addTextChangedListener(new MyTextWatcher(passworkEditText));

        buildGoogleApiClient();
        googleApiClient.connect();

        if (RequestSummaryActivity.isRequestCanceled) {
            Intent intent = new Intent(getActivity(), ListActivity.class);
            startActivity(intent);
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    token = intent.getStringExtra("token");
                    sendToServer();
                    // Toast.makeText(getApplicationContext(), "GCM registration token: " + token, Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL

                    // Toast.makeText(getApplicationContext(), "GCM registration token is stored in server!", Toast.LENGTH_LONG).show();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    //  Toast.makeText(getApplicationContext(), "Push notification is received!", Toast.LENGTH_LONG).show();
                }
            }
        };

        Button login = (Button) view.findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    boolean isLoginValid = true; //make isLoginValid a method if suggested way doesn't work
                    if (isLoginValid) {
                        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                            buildAlertMessageNoGps();

                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                                manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                            if (lastLocation != null) {
                                latitude = lastLocation.getLatitude();
                                longitude = lastLocation.getLongitude();
                                Log.e("Location", "" + latitude + " | " + longitude);
                                Log.e("Login", "PHP Request here");
                            }
                        }
                    }
                    progressDialog.setMessage("Logging in");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
                if (checkPlayServices()) {
                    registerGCM();
                }
            }
        });

        return view;
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("", "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getActivity(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();

            }
            return false;
        }
        return true;
    }

    private void registerGCM() {
        Intent intent = new Intent(getActivity(), GcmIntentService.class);
        intent.putExtra("key", "register");
        getActivity().startService(intent);
        GcmIntentService g = new GcmIntentService();
        Config.flag++;
       /* InstanceID instanceID = InstanceID.getInstance(getActivity());
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.e("server", "response: " + token);
            sendToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

    private void sendToServer() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://provideameal.esy.es/pam_login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("server", "response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    // Check for error node in json
                    if (status.equals("success")) {

                        if (progressDialog.isShowing())
                            progressDialog.hide();
                        Intent intent = new Intent(getActivity(), ListActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Wrong username/password", Toast.LENGTH_LONG).show();
                        if (progressDialog.isShowing())
                            progressDialog.hide();
                    }
                } catch (JSONException e) {
                    Log.e("", "json parsing error: " + e.getMessage());
                    //Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e("", "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity(), "There was an error connecting to the server. Please try again later ", Toast.LENGTH_SHORT).show();
                if (progressDialog.isShowing())
                    progressDialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // String id =token.substring(6,token.length()-1);
                params.put("email", emailEditText.getText().toString());
                params.put("password", passworkEditText.getText().toString());
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("token", token);
                Log.e("", params.toString());
                return params;
            }
        };
        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }


    @Override
    public void onResume() {
        super.onResume();
        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean isFormValid() {
        if (!isEmailValid() || !isPasswordValid()) {
            if (!isEmailValid()) {
                emailTextInputLayout.setErrorEnabled(true);
                emailTextInputLayout.setError("Please enter a valid name");
            } else {
                emailTextInputLayout.setError(null);
                emailTextInputLayout.setErrorEnabled(false);
            }
            if (!isPasswordValid()) {
                passwordTextInputLayout.setErrorEnabled(true);
                passwordTextInputLayout.setError("Please enter a valid address");
            } else {
                passwordTextInputLayout.setError(null);
                passwordTextInputLayout.setErrorEnabled(false);
            }
            return false;
        } else {
            emailTextInputLayout.setErrorEnabled(false);
            passwordTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public boolean isEmailValid() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return emailEditText.getText().toString().matches(emailPattern);
    }

    public boolean isPasswordValid() {
        return !passworkEditText.getText().toString().trim().isEmpty();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_name:
                    if (!isEmailValid()) {
                        emailTextInputLayout.setErrorEnabled(true);
                        emailTextInputLayout.setError("Please enter a valid email");
                    } else {
                        emailTextInputLayout.setError(null);
                        emailTextInputLayout.setErrorEnabled(false);
                    }
                    break;
                case R.id.edit_address:
                    if (!isPasswordValid()) {
                        passwordTextInputLayout.setErrorEnabled(true);
                        passwordTextInputLayout.setError("Please enter a password");
                    } else {
                        passwordTextInputLayout.setError(null);
                        passwordTextInputLayout.setErrorEnabled(false);
                    }
                    break;
            }
        }
    }

}
