package com.example.myapplication;
import android.annotation.SuppressLint;
import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {
    private EditText ss;
    private Button wer;
    private TextView textView1;
    TextView textView2;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.TextView1);
        ss = findViewById(R.id.ss);
        wer = findViewById(R.id.wer);
        textView2 = findViewById(R.id.TextView2);
        wer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ss.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this,R.string.nn,Toast.LENGTH_LONG).show();
                else {
                    String city = ss.getText().toString();
                    String key = "36b0844d27c1832c9f05337c318e5d8e";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=36b0844d27c1832c9f05337c318e5d8e&units=metric";
                    new GetURLData().execute(url);
                }


            }
        });
    }
    private class GetURLData extends AsyncTask<String,String,String> {

        protected void onPreExecute() {
            super.onPreExecute();
            textView1.setText("Ожидайте....");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();


                try {
                    if (reader != null)

                        reader.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject obj = new JSONObject(result);
                textView1.setText("Температура " + obj.getJSONObject("main").getInt("temp") + ("°C"));
                textView2.setText("Как ощущается " + obj.getJSONObject("main").getInt("feels_like") + ("°C"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}

