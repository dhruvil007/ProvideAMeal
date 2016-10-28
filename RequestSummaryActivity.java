package com.codeshastra.coderr.provideameal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RequestSummaryActivity extends AppCompatActivity {

    public String donorName;
    public String donorNumber;
    public String donorAddress;
    public String donorContact;

    public Button submitCompletion;
    public Button call;

    private TextView nameTextView;
    private TextView addressTextView;
    private TextView numberTextView;

    @Override
    public void onBackPressed() {
            Intent intent = new Intent(this, ListActivity.class);
            startActivity(intent);
            finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        setSupportActionBar(toolbar);

        nameTextView = (TextView) findViewById(R.id.text_view_summary_name);
        addressTextView = (TextView) findViewById(R.id.text_view_summary_address);
        numberTextView = (TextView) findViewById(R.id.text_view_summary_number);
        call = (Button) findViewById(R.id.button_summary_call);
        submitCompletion = (Button) findViewById(R.id.button_summary_submit);

        final Context context = this;
        submitCompletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessagesDataSource m = new MessagesDataSource(context);
                m.open();
                List<Message> list = m.getAllMessages();
                int counter;
                for (counter = 0; counter < list.size(); counter++) {
                    String openRequestName = nameTextView.getText().toString();
                    Message current = list.get(counter);
                    long id;
                    if (current.getName().equals(openRequestName))
                        break;
                }
                Log.e("NameDeletion", list.get(counter).getName());
                m.deleteMessage(list.get(counter));
                m.close();
                Toast.makeText(getApplicationContext(), "Request Cleared", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + donorContact));
                startActivity(intent);
            }
        });

        Intent incomingIntent = getIntent();
        donorName = incomingIntent.getStringExtra("donorName");
        donorNumber = "Number of Meals: " + incomingIntent.getStringExtra("donorMeals");
        donorAddress = incomingIntent.getStringExtra("donorAddress");
        donorContact = incomingIntent.getStringExtra("donorContact");

        nameTextView.setText(donorName);
        numberTextView.setText(donorNumber);
        addressTextView.setText(donorAddress);

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(donorName + "'s Summary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
