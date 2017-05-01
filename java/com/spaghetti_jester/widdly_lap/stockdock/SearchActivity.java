package com.spaghetti_jester.widdly_lap.stockdock;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void goStocks(View v) {
        String s = ((EditText)findViewById(R.id.searchInput)).getText().toString();
        if (s.length() < 1)
            Toast.makeText(getApplicationContext(), "Please enter a company symbol to search for!", Toast.LENGTH_SHORT).show();
        else {
            Intent i = new Intent(this, StockActivity.class);
            i.putExtra("symbol", s);
            startActivity(i);
        }
    }

}
