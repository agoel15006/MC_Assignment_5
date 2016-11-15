package com.example.anurag.dataapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private ServerTask mTask= null;
    private TextView mTextView = null;
    private ImageButton mButton = null;
    private View mDownloadFormView;
    private String output = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textview2);
        mButton = (ImageButton) findViewById(R.id.download);
        mDownloadFormView = findViewById(R.id.download_form);

        if(savedInstanceState!=null) {
            output = savedInstanceState.getString("output");
            mTextView.setText(output);
        }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextView.setText("");
                mTask = new ServerTask(MainActivity.this);
                mTask.execute();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("output", output);
    }

/*    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mDownloadFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mDownloadFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mDownloadFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
        } else {
            mDownloadFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }*/

    private class ServerTask extends AsyncTask<String, Void, String> {

        private Context context;

        public ServerTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... arg0) {
            String result = null;
            try {
                //Thread.sleep(3000);
                String link = "https://www.iiitd.ac.in/about/";

                URL url = new URL(link);
                HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();

                BufferedReader in=new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                StringBuffer sb = new StringBuffer("");
                String line="";
                while((line = in.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();
                in.close();
                return result;
            } catch(Exception e) {
                Log.d("ServerActivity","Exception");
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            if(result==null) {
                Toast.makeText(this.context, "Text empty", Toast.LENGTH_LONG).show();
            }
            else {
                Log.d("MainActivity**",result);
                Document html = Jsoup.parse(result);
                output = html.title();
                mTextView.setText(output);
                //Toast.makeText(this.context, "Download completed!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
