package com.spaghetti_jester.widdly_lap.stockdock;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(isNetworkAvailable())
            new ChangeGetter().execute("YHOO", "MSFT", "FB", "AAPL", "T");
        else {
            findViewById(R.id.noInternet).setVisibility(View.VISIBLE);
            findViewById(R.id.options_list).setVisibility(View.GONE);
        }
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
            if(isNetworkAvailable()) {
                findViewById(R.id.noInternet).setVisibility(View.GONE);
                findViewById(R.id.options_list).setVisibility(View.VISIBLE);
                new ChangeGetter().execute("YHOO", "MSFT", "FB", "AAPL", "T");
            } else {
                findViewById(R.id.noInternet).setVisibility(View.VISIBLE);
                findViewById(R.id.options_list).setVisibility(View.GONE);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ListView listView;
    public void initListView(String[] changes) {


        listView = (ListView) findViewById(R.id.options_list);

        StockMenuSetting[] mySettings = new StockMenuSetting[] {
                new StockMenuSetting("Yahoo!","Search engine company",changes[0]),
                new StockMenuSetting("Microsoft","Technology company",changes[1]),
                new StockMenuSetting("Facebook","Social network company",changes[2]),
                new StockMenuSetting("Apple","Technology company",changes[3]),
                new StockMenuSetting("AT&T","Telecommunications company",changes[4])
        };


        StockMenuSettingAdapter adapter = new StockMenuSettingAdapter(this, R.layout.stocklist_layout,mySettings);

        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // Show Alert
                Intent showStock = new Intent(getApplicationContext(), StockActivity.class);
                switch (itemPosition) {
                    case 0:
                        showStock.putExtra("symbol", "YHOO");
                        break;
                    case 1:
                        showStock.putExtra("symbol", "MSFT");
                        break;
                    case 2:
                        showStock.putExtra("symbol", "FB");
                        break;
                    case 3:
                        showStock.putExtra("symbol", "AAPL");
                        break;
                    case 4:
                        showStock.putExtra("symbol", "T");
                        break;
                }
                startActivity(showStock);
            }

        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private class ChangeGetter extends AsyncTask<String, Void, String[]> {

        //private Exception exception;
        protected String[] doInBackground(String... symb) {
            String[] o = new String[5];
            try {
                for (int i = 0; i < o.length; i++) {

                    URL url = new URL("http://query.yahooapis.com/v1/public/yql?q=select%20Change%20from%20yahoo.finance.quotes%20where%20symbol%20in%20%28%22"
                            + symb[i] + "%22%29&env=store://datatables.org/alltableswithkeys");
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
                    int eventType = 0;
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if(eventType == XmlPullParser.TEXT) {
                            o[i] = xpp.getText();
                        }
                        eventType = xpp.next();
                    }
                }
            } catch (Exception e) {
                System.out.println("EXCEPTION: " + e.toString());
            }
            return o;
        }

        protected void onPostExecute(String[] in) {
            for(int i = 0; i < in.length; i++) {
                if (in[i] == null)
                    in[i] = "+0.00";
            }
            initListView(in);
        }
    }
}

