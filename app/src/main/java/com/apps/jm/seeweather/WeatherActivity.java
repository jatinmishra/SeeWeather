package com.apps.jm.seeweather;

/**
 * Created by Jatin on 8/18/2017.
 */

import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.model.City;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;



import java.util.List;


public class WeatherActivity extends AppCompatActivity {

    private static WeatherClient weatherclient;
    private Toolbar toolbar;
    private ListView cityListView;
    private City currentCity;
    private Dialog b;
    // Widget
    private TextView tempView;
    private pl.droidsonroids.gif.GifTextView gifBack;
    private ImageView weatherIcon;
    private TextView pressView;
    private TextView humView;
    private TextView windView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setTitle("SeeWeather");
        setSupportActionBar(toolbar);
        gifBack=(pl.droidsonroids.gif.GifTextView) findViewById(R.id.gifBack);
        //
        tempView = (TextView) findViewById(R.id.temp);
        weatherIcon = (ImageView) findViewById(R.id.weather_icon);
        pressView = (TextView) findViewById(R.id.pressure);
        humView = (TextView) findViewById(R.id.hum);
        windView = (TextView) findViewById(R.id.wind);

        initWeatherClient();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_search) {
            // We show the dialog
            Dialog d = createDialog();
            d.show();
        }
        if (id==R.id.action_refresh)
        {
            startActivity(new Intent(this,TextToSpeechActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    private void initWeatherClient() {
        WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();
        WeatherConfig config = new WeatherConfig();
        config.ApiKey="8c391a88da53ca5b95cdab0b9ddc8ba0";
        config.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
        config.lang = "en"; // If you want to use english
        config.maxResult = 5; // Max number of cities retrieved
        config.numDays = 6; // Max num of days in the forecast

        try {
            weatherclient = builder.attach(this)
                    .provider(new OpenweathermapProviderType())
                    .httpClient(com.survivingwithandroid.weather.lib.client.volley.WeatherClientDefault.class)
                    .config(config)
                    .build();
        }
        catch(Throwable t) {
            // we will handle it later
        }
    }
    public void ShowProgressDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.progress_bar, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.show();
    }

    public void HideProgressDialog(){

        b.dismiss();
    }
    private void getWeather() {
        ShowProgressDialog();
        weatherclient.getCurrentCondition(new WeatherRequest(currentCity.getId()),
                new WeatherClient.WeatherEventListener() {
                    @Override
                    public void onWeatherRetrieved(CurrentWeather currentWeather) {
                        // We have the current weather now
                        // Update subtitle toolbar
                        /*String test=currentWeather.weather.currentCondition.getSolarRadiation();
                        toolbar.setSubtitle(test);*/
                        toolbar.setSubtitle(currentWeather.weather.currentCondition.getDescr());
                        setWeatherBack(currentWeather.weather.currentCondition.getDescr());
                        tempView.setText(String.format("%.0f",currentWeather.weather.temperature.getTemp())+(char) 0x00B0+"C");
                        pressView.setText(String.valueOf(currentWeather.weather.currentCondition.getPressure()));
                        windView.setText(String.valueOf(currentWeather.weather.wind.getSpeed()));
                        humView.setText(String.valueOf(currentWeather.weather.currentCondition.getHumidity()));
                        weatherIcon.setImageResource(WeatherIconMapper.getWeatherResource(currentWeather.weather.currentCondition.getIcon(), currentWeather.weather.currentCondition.getWeatherId()));

                        setToolbarColor(currentWeather.weather.temperature.getTemp());
                    }


                    @Override
                    public void onWeatherError(WeatherLibException e) {

                    }

                    @Override
                    public void onConnectionError(Throwable throwable) {

                    }
                });
        HideProgressDialog();
    }

    private Dialog createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DialogTheme);
        builder.setTitle("Search City");
        builder.setMessage("Please Enter City Name");
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.select_city_dialog, null);
        builder.setView(v);

        final EditText et = (EditText) v.findViewById(R.id.ptnEdit);
        cityListView = (ListView) v.findViewById(R.id.cityList);
        cityListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currentCity = (City) parent.getItemAtPosition(position);
                //pixabay====6205180-132722c71a701ea7c5644b492
                //google--places=======AIzaSyDljvS3BFeLxO9ql6-c5tZwgdwcVLda1ww
                et.setText(currentCity.toString());
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 3) {
                    ShowProgressDialog();
                    // We start searching
                    weatherclient.searchCity(s.toString(), new WeatherClient.CityEventListener() {
                        @Override
                        public void onCityListRetrieved(List<City> cities) {
                            CityAdapter ca = new CityAdapter(WeatherActivity.this, cities);
                            cityListView.setAdapter(ca);
                            HideProgressDialog();
                            //findViewById(R.id.msg).setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onWeatherError(WeatherLibException e) {

                        }

                        @Override
                        public void onConnectionError(Throwable throwable) {

                        }
                    });
                   HideProgressDialog();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(currentCity==null){
                    Toast.makeText(WeatherActivity.this,"Please Select a City from the list",Toast.LENGTH_LONG).show();
                }
                else{
                dialog.dismiss();
                // We update toolbar
                toolbar.setTitle(currentCity.getName() + "," + currentCity.getCountry());
                // Start getting weather
                getWeather();}
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }


    private void setToolbarColor(float temp) {
        int color = -1;

        if (temp < -10)
            color = getResources().getColor(R.color.primary_indigo);
        else if (temp >=-10 && temp <=-5)
            color = getResources().getColor(R.color.primary_blue);
        else if (temp >-5 && temp < 5)
            color = getResources().getColor(R.color.primary_light_blue);
        else if (temp >= 5 && temp < 10)
            color = getResources().getColor(R.color.primary_teal);
        else if (temp >= 10 && temp < 15)
            color = getResources().getColor(R.color.primary_light_green);
        else if (temp >= 15 && temp < 20)
            color = getResources().getColor(R.color.primary_green);
        else if (temp >= 20 && temp < 25)
            color = getResources().getColor(R.color.primary_lime);
        else if (temp >= 25 && temp < 28)
            color = getResources().getColor(R.color.primary_yellow);
        else if (temp >= 28 && temp < 32)
            color = getResources().getColor(R.color.primary_amber);
        else if (temp >= 32 && temp < 35)
            color = getResources().getColor(R.color.primary_orange);
        else if (temp >= 35)
            color = getResources().getColor(R.color.primary_red);

        toolbar.setBackgroundColor(color);

    }


    // This is the City Adapter used to fill the listview when user searchs for the city
    class CityAdapter extends ArrayAdapter<City> {

        private List<City> cityList;
        private Context ctx;

        public CityAdapter(Context ctx, List<City> cityList) {
            super(ctx, R.layout.city_row);
            this.cityList = cityList;
            this.ctx = ctx;
        }

        @Override
        public City getItem(int position) {
            if (cityList != null)
                return cityList.get(position);
            return null;
        }

        @Override
        public int getCount() {
            if (cityList == null)
                return 0;

            return cityList.size();
        }

        @Override
        public long getItemId(int position) {
            if (cityList == null)
                return -1;

            return cityList.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.city_row, null, false);
            }

            TextView tv = (TextView) v.findViewById(R.id.descrCity);

            tv.setText(cityList.get(position).getName() + "," + cityList.get(position).getCountry());

            return v;
        }
    }
    public void setWeatherBack(String cond){
        if(cond.contains("rain")){
            gifBack.setBackgroundResource(R.drawable.raining);
        }
        else if(cond.equalsIgnoreCase("haze")||cond.contains("clouds")){
            gifBack.setBackgroundResource(R.drawable.c1f009b219a31b387765b9a7bd8f6f1b);
        }
    }
}