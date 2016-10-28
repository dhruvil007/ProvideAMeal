package com.codeshastra.coderr.provideameal;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class FragmentDonate extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context mContext;

    public String time;
    public EditText nameEditText;
    public EditText addressEditText;
    public EditText mealsEditText;
    public EditText contactEditText;
    public EditText emailEditText;
    public TextInputLayout nameTextInputLayout;
    public TextInputLayout addressTextInputLayout;
    public TextInputLayout mealsTextInputLayout;
    public TextInputLayout contactTextInputLayout;
    public TextInputLayout emailTextInputLayout;
    Intent summary;
    public GoogleApiClient googleApiClient;
    public Location lastLocation;
    public double latitude;
    public double longitude;

    public static boolean returnFromSummary = false;

    public FragmentDonate() {
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
        View view = inflater.inflate(R.layout.fragment_donate, container, false);
        mContext = getActivity();

        nameEditText = (EditText) view.findViewById(R.id.edit_name);
        addressEditText = (EditText) view.findViewById(R.id.edit_address);
        mealsEditText = (EditText) view.findViewById(R.id.edit_meals);
        contactEditText = (EditText) view.findViewById(R.id.edit_contact);
        emailEditText = (EditText) view.findViewById(R.id.edit_email);

        nameEditText.addTextChangedListener(new MyTextWatcher(nameEditText));
        addressEditText.addTextChangedListener(new MyTextWatcher(addressEditText));
        mealsEditText.addTextChangedListener(new MyTextWatcher(mealsEditText));
        contactEditText.addTextChangedListener(new MyTextWatcher(contactEditText));
        emailEditText.addTextChangedListener(new MyTextWatcher(emailEditText));

        nameTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_ip_layout_name);
        addressTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_ip_layout_address);
        mealsTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_ip_layout_meals);
        contactTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_ip_layout_contact);
        emailTextInputLayout = (TextInputLayout) view.findViewById(R.id.text_ip_layout_email);

        buildGoogleApiClient();
        googleApiClient.connect();

        Button sendButton = (Button) view.findViewById(R.id.btn_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps();
                    }
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        if (lastLocation == null) {
                            Log.e("Location", "Unavailable");
                        }
                        longitude = lastLocation.getLongitude();
                        Log.e("Longitude", "" + longitude);
                        latitude = lastLocation.getLatitude();
                        Log.e("Latitude", "" + latitude);

                        // sendToServer();

                        Log.e("FDonate", "All valid");
                        Toast.makeText(mContext, "Sent", Toast.LENGTH_SHORT).show();
                        summary = new Intent(getActivity(), SummaryActivity.class);
                        summary.putExtra("name", nameEditText.getText().toString());
                        summary.putExtra("address", addressEditText.getText().toString());
                        summary.putExtra("meals", mealsEditText.getText().toString());
                        summary.putExtra("contact", contactEditText.getText().toString());
                        summary.putExtra("email", contactEditText.getText().toString());
                        summary.putExtra("latitude", String.valueOf(latitude));
                        summary.putExtra("longitude", String.valueOf(longitude));
                        getActivity().startActivity(summary);

                    }
                }
            }
        });
        return view;
    }

    private boolean isFormValid() {
        if (!isNameValid() || !isAddressValid() || !isMealsValid() || !isContactValid() || !isEmailValid()) {
            if (!isNameValid()) {
                nameTextInputLayout.setErrorEnabled(true);
                nameTextInputLayout.setError("Please enter a valid name");
            } else {
                nameTextInputLayout.setError(null);
                nameTextInputLayout.setErrorEnabled(false);
            }
            if (!isAddressValid()) {
                addressTextInputLayout.setErrorEnabled(true);
                addressTextInputLayout.setError("Please enter a valid address");
            } else {
                addressTextInputLayout.setError(null);
                addressTextInputLayout.setErrorEnabled(false);
            }
            if (!isMealsValid()) {
                mealsTextInputLayout.setErrorEnabled(true);
                mealsTextInputLayout.setError("Please enter a valid number");
            } else {
                mealsTextInputLayout.setError(null);
                mealsTextInputLayout.setErrorEnabled(false);
            }
            if (!isContactValid()) {
                contactTextInputLayout.setErrorEnabled(true);
                contactTextInputLayout.setError("Please enter a valid contact number");
            } else {
                contactTextInputLayout.setError(null);
                contactTextInputLayout.setErrorEnabled(false);
            }
            if (!isEmailValid()) {
                emailTextInputLayout.setErrorEnabled(true);
                emailTextInputLayout.setError("Please enter a valid email address");
            } else {
                emailTextInputLayout.setError(null);
                emailTextInputLayout.setErrorEnabled(false);
            }
            return false;
        } else {
            nameTextInputLayout.setErrorEnabled(false);
            addressTextInputLayout.setErrorEnabled(false);
            mealsTextInputLayout.setErrorEnabled(false);
            contactTextInputLayout.setErrorEnabled(false);
            contactTextInputLayout.setErrorEnabled(false);
            return true;
        }
    }

    public boolean isNameValid() {
        return !nameEditText.getText().toString().trim().isEmpty();
    }

    public boolean isAddressValid() {
        return !addressEditText.getText().toString().trim().isEmpty();
    }

    public boolean isMealsValid() {
        return !mealsEditText.getText().toString().trim().isEmpty();
    }

    public boolean isContactValid() {
        return !contactEditText.getText().toString().trim().isEmpty();
    }

    public boolean isEmailValid() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return emailEditText.getText().toString().matches(emailPattern);
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

//    public void setFieldsToNull() {
//        nameEditText.setText(null);
//        nameTextInputLayout.setError(null);
//        nameTextInputLayout.setErrorEnabled(false);
//        addressEditText.setText(null);
//        addressTextInputLayout.setErrorEnabled(false);
//        mealsEditText.setText(null);
//        mealsTextInputLayout.setErrorEnabled(false);
//        contactEditText.setText(null);
//        contactTextInputLayout.setErrorEnabled(false);
//        emailEditText.setText(null);
//        emailTextInputLayout.setErrorEnabled(false);
//    }

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
                    if (!isNameValid()) {
                        nameTextInputLayout.setErrorEnabled(true);
                        nameTextInputLayout.setError("Please enter a valid name");
                    } else {
                        nameTextInputLayout.setError(null);
                        nameTextInputLayout.setErrorEnabled(false);
                    }
                    break;
                case R.id.edit_address:
                    if (!isAddressValid()) {
                        addressTextInputLayout.setErrorEnabled(true);
                        addressTextInputLayout.setError("Please enter a valid address");
                    } else {
                        addressTextInputLayout.setError(null);
                        addressTextInputLayout.setErrorEnabled(false);
                    }
                    break;
                case R.id.edit_meals:
                    if (!isMealsValid()) {
                        mealsTextInputLayout.setErrorEnabled(true);
                        mealsTextInputLayout.setError("Please enter a valid number");
                    } else {
                        mealsTextInputLayout.setError(null);
                        mealsTextInputLayout.setErrorEnabled(false);
                    }
                    break;
                case R.id.edit_contact:
                    if (!isContactValid()) {
                        contactTextInputLayout.setErrorEnabled(true);
                        contactTextInputLayout.setError("Please enter a valid number");
                    } else {
                        contactTextInputLayout.setError(null);
                        contactTextInputLayout.setErrorEnabled(false);
                    }
                    break;
                case R.id.edit_email:
                    if (!isEmailValid()) {
                        emailTextInputLayout.setErrorEnabled(true);
                        emailTextInputLayout.setError("Please enter a valid email address");
                    } else {
                        emailTextInputLayout.setError(null);
                        emailTextInputLayout.setErrorEnabled(false);
                    }
            }
        }
    }
}
