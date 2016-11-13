package com.example.anurag.dataapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Anurag on 10/4/2016.
 */
public class ServerActivity extends AsyncTask<String, Void, String> {

    private Context context;

    public ServerActivity(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... arg0) {
        String result = null;
        try {
            String link = "http://www.android.com/";

            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            HttpURLConnection httpConnection = (HttpURLConnection) conn;
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in=new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";
                while((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                result = sb.toString();
                in.close();
            }
            Log.d("ServerActivity","Http response not ok");
            return "";
        } catch(Exception e) {
            Log.d("ServerActivity","Exception");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result){
        if(result==null)
            Toast.makeText(this.context,"Text empty",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this.context, result ,Toast.LENGTH_LONG).show();
    }
}
