package com.codeshastra.coderr.provideameal;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {

    private TextView name, v_name, v_contact;
    private TextView address;
    private TextView meals;
    private TextView contact;
    private ProgressDialog progressDialog;
    private Intent summary;
    private String vname, c;
    private NotificationUtils notificationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Summary for Pickup");
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing())
            progressDialog.show();

        summary = getIntent();

        name = (TextView) findViewById(R.id.text_name);
        address = (TextView) findViewById(R.id.text_address);
        meals = (TextView) findViewById(R.id.text_meals);
        contact = (TextView) findViewById(R.id.text_contact);
        v_contact = (TextView) findViewById(R.id.contact);
        v_name = (TextView) findViewById(R.id.name);

        String nameField, addressField, mealsField, contactField;
        nameField = "Name: " + summary.getStringExtra("name");
        addressField = "Address: " + summary.getStringExtra("address");
        mealsField = "No. of Meals: " + summary.getStringExtra("meals");
        contactField = "Contact: " + summary.getStringExtra("contact");

        name.setText(nameField);
        address.setText(addressField);
        meals.setText(mealsField);
        contact.setText(contactField);

        sendToServer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void sendToServer() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                "http://provideameal.esy.es/pam_donate.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("server", "response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("status");
                    vname = obj.getString("name");
                    c = obj.getString("contact");
                    v_name.setText("Name of Volunteer Assigned: " + vname);
                    v_contact.setText("Number: " + c);
                    if (progressDialog.isShowing())
                        progressDialog.hide();
                    Log.e("name:", vname);
                    // Check for error node in json
                    if (status.equals("success")) {
                        /*if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                            Log.e("in","dfsf");
                            // app is in foreground, broadcast the push message
                            Intent pushNotification = new Intent("pushNotification");
                            pushNotification.putExtra("message", name);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);
                            showNotificationMessage(getApplicationContext(), "Provide A Meal", name,resultIntent);
                            // play notification sound
                            // NotificationUtils notificationUtils = new NotificationUtils();
                            // notificationUtils.playNotificationSound();
                        } else {
                            Intent resultIntent = new Intent(SummaryActivity.this, SummaryActivity.class);
                            resultIntent.putExtra("message", name);
                        }*/
                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
                        mBuilder.setContentTitle("Provide a Meal");
                        mBuilder.setContentText("Volunteer Assigned: " + vname);
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(100, mBuilder.build());
                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to send information to our server. ", Toast.LENGTH_LONG).show();
                        if (progressDialog.isShowing())
                            progressDialog.hide();
                    }

                } catch (JSONException e) {
                    Log.e("", "json parsing error: " + e.getMessage());
                    if (progressDialog.isShowing())
                        progressDialog.hide();
                    //Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;

                Log.e("", "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "There was an error connecting to the server.Please try again later ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //   String id =token.substring(6,token.length()-1);
                params.put("name", summary.getStringExtra("name"));
                params.put("address", summary.getStringExtra("address"));
                params.put("email", summary.getStringExtra("email"));
                params.put("quantity", summary.getStringExtra("meals"));
                params.put("contact", summary.getStringExtra("contact"));
                params.put("latitude", summary.getStringExtra("latitude"));
                params.put("longitude", summary.getStringExtra("longitude"));
                Log.e("", params.toString());
                return params;
            }
        };
        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }

}
