package com.spaghetti_jester.widdly_lap.stockdock;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;

public class StockActivity extends AppCompatActivity {
    public String symbol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        symbol = getIntent().getStringExtra("symbol");
        new DataGetter().execute(symbol);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search_button) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        } else if (id == R.id.refresh_button) {
            new DataGetter().execute(symbol);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void setData(String[] in) {
        if(in[0] == null && in[1] == null) {
            Toast.makeText(getApplicationContext(), "Your search query was invalid! Please try again.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, SearchActivity.class));
        }
        ((TextView)findViewById(R.id.corpName)).setText(in[3]);
        ((TextView)findViewById(R.id.corpSymb)).setText(symbol);
        ((TextView)findViewById(R.id.change)).setText(in[0] + " " + in[1]);
        ((TextView)findViewById(R.id.price)).setText(in[5] + " " + in[1]);
        ((TextView)findViewById(R.id.lastTrade)).setText(in[2] + " " + in[1] + " at " + in[4]);
    }
    private class DataGetter extends AsyncTask<String, Void, String[]> {

        //private Exception exception;
        protected String[] doInBackground(String... symb) {
            String[] o = new String[6];
            try {
                URL url = new URL("http://query.yahooapis.com/v1/public/yql?q=select%20Name,Ask,Currency,Change,LastTradeTime,LastTradePriceOnly%20from%20yahoo.finance.quotes%20where%20symbol%20in%20%28%22"
                        + symb[0] + "%22%29&env=store://datatables.org/alltableswithkeys");
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str, fullXML = "";
                while ((str = in.readLine()) != null) {
                    fullXML += str;
                }
                in.close();
                //Load up the XML parser
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(fullXML));
                int i = 0;
                int eventType = 0;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.TEXT) {
                        o[i] = xpp.getText();
                        i++;
                    }
                    eventType = xpp.next();
                }
            } catch (Exception e) {
                System.out.println("EXCEPTION: " + e.toString());
            }
            return o;
        }

        protected void onPostExecute(String[] in) {
            setData(in);
        }
    }
}
