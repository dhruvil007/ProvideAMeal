package com.codeshastra.coderr.provideameal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class ListActivity extends AppCompatActivity implements Adapter.ClickListener {

    private RecyclerView listRecyclerView;
    private TextView textView;
    private List<Message> paperList;
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Donation Requests");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_list);
        textView = (TextView) findViewById(R.id.list_text_view);

        MainActivity.returnFromListActivity = true;
        MessagesDataSource m = new MessagesDataSource(this);
        m.open();
        paperList = m.getAllMessages();
        Log.e("Size", "" + paperList.size());
        if (paperList.size() == 0) {
            textView.setText("No Requests Pending :<");
        } else {
            textView.setText(null);
        }
        m.close();
        // dataRequests = getDataRequests();
        adapter = new Adapter(this, paperList);
        adapter.setClickListener(this);
        listRecyclerView.setAdapter(adapter);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void itemClicked(View view, int position) {
        Intent intent = new Intent(this, RequestSummaryActivity.class);
        intent.putExtra("donorName", paperList.get(position).getName());
        intent.putExtra("donorMeals", paperList.get(position).getMeals());
        intent.putExtra("donorContact", paperList.get(position).getNumber());
        intent.putExtra("donorAddress", paperList.get(position).getAddress());
        intent.putExtra("donorEmail", paperList.get(position).getEmail());
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clear, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            MessagesDataSource m = new MessagesDataSource(this);
            m.open();
            m.deleteAll();
            m.close();
            paperList.clear();
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
}

