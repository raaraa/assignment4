package com.example.rewards;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class SendPointsAsync extends AsyncTask<JSONObject, Void, String> {
    private static final String TAG = "LoginAPIAyncTask";
    private static final String baseUrl =
            "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com";
    private static final String loginEndPoint ="/rewards";
    @SuppressLint("StaticFieldLeak")
    private AwardActivity mainActivity;

    public SendPointsAsync(AwardActivity mainActivity) {
        this.mainActivity = mainActivity;
    }


    @Override
    protected void onPostExecute(String connectionResult) {

        // Normally we would parse the results and make use of the data
        // For this example, we just use the returned string size - empty is fail
        if (connectionResult.contains("error")) // If there is "error" in the results...
            mainActivity.sendResults(connectionResult);
        else
            mainActivity.sendResults(connectionResult);

    }

    @Override
    protected String doInBackground(JSONObject... params) {
        JSONObject jsonObject = params[0];
        try {
            return doAPICall(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private String doAPICall(JSONObject jsonObject) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            String urlString = baseUrl + loginEndPoint;  // Build the full URL

            Uri uri = Uri.parse(urlString);    // Convert String url to URI
            URL url = new URL(uri.toString()); // Convert URI to URL

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");  // POST - others might use PUT, DELETE, GET

            // Set the Content-Type and Accept properties to use JSON data
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            // Write the JSON (as String) to the open connection
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            out.write(jsonObject.toString());
            out.close();

            int responseCode = connection.getResponseCode();

            StringBuilder result = new StringBuilder();

            // If successful (HTTP_OK)
            if (responseCode == HTTP_OK) {

                // Read the results - use connection's getInputStream
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                // Return the results (to onPostExecute)
                return result.toString();

            } else {
                // Not HTTP_OK - some error occurred - use connection's getErrorStream
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String line;
                while (null != (line = reader.readLine())) {
                    result.append(line).append("\n");
                }

                // Return the results (to onPostExecute)
                return result.toString();
            }

        } catch (Exception e) {
            // Some exception occurred! Log it.
            Log.d(TAG, "doAuth: " + e.getClass().getName() + ": " + e.getMessage());

        } finally { // Close everything!
            if (connection != null) {
                connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error closing stream: " + e.getMessage());
                }
            }
        }
        return "Some error has occurred"; // Return an error message if Exception occurred
    }
}
