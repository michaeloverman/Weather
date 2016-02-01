package com.overman.weather.app;

/**
 * Created by Michael on 1/31/2016.
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.overman.weather.app.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * fetching the forecast and displaying as ListView
 */
public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> mForecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // fake data array
        String[] forecastArray = {
                "Today - Sunny - 86/59",
                "Tomorrow - Mellow - 86/59",
                "Monday - Juicy - 86/59",
                "Tuesday - Cloudy - 86/59",
                "Wednesday - Despondent - 86/59",
                "Thursday - Miserable - 86/59",
                "Friday - Somewhat Melancholy - 86/59",
                "Saturday - Sheepish, with a touch of drama - 86/59",
                "Sunday - Rain, rain, rain - 86/59",
                "Wednesday - Despondent - 86/59",
                "Thursday - Miserable - 86/59",
                "Friday - Somewhat Melancholy - 86/59",
                "Saturday - Sheepish, with a touch of drama - 86/59",
                "Sunday - Rain, rain, rain - 86/59"
        };

        ArrayList<String> weekForecast = new ArrayList<String>(
                Arrays.asList(forecastArray)
        );

    //    Log.d("ForecastFragment", "creating mForecastAdapter");
        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(),                          //current context
                R.layout.list_item_forecast,            //id of list item layout
                R.id.list_item_forecast_textview,       //id of textview
                weekForecast                            //data
        );


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        // Get reference to ListView, attach to adapter
        ListView listView = (ListView)rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }

    //@Override
    public class FetchWeatherTask extends AsyncTask<Void, Void, Void> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        protected Void doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=22812&mode=json&units=metric&cnt=10&appid=fbdda77527441934deacec52b2167fab");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

    }
}
