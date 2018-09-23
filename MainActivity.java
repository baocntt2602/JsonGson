package com.example.administrator.jsongson;

import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import model.GoogleData;
import model.Result;

public class MainActivity extends AppCompatActivity {

    ListView lvKetqua;
    ArrayList<Result> dsKetqua;
    ArrayAdapter<Result> adapterKetqua;

    EditText txtKeyword;
    Button btnTim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xyLyTimKiem();
            }
        });
    }

    private void xyLyTimKiem() {
        GoogleDataTask task = new GoogleDataTask();
        task.execute(txtKeyword.getText().toString());
    }

    class GoogleDataTask extends AsyncTask<String, Void, ArrayList<Result>> {

        @Override
        protected ArrayList<Result> doInBackground(String... strings) {
            ArrayList<Result> ds = new ArrayList<>();
            try {
                String keyword = strings[0];
                String api = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
                String api_search = api + URLEncoder.encode(keyword, "UTF-8")+"&start=1&rsz=8";
                URL url = new URL(api_search);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openStream(), "UTF-8");
                GoogleData googleData = new Gson().fromJson(inputStreamReader, GoogleData.class);
                return googleData.getResponseData().getResults();
            } catch (Exception e) {
                Log.e("Loi", e.toString());
            }

            return ds;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapterKetqua.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<Result> results) {
            super.onPostExecute(results);
            adapterKetqua.clear();
            adapterKetqua.addAll(results);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    private void addControls() {
        txtKeyword = findViewById(R.id.edtSearch);
        btnTim = findViewById(R.id.btnSearch);
        lvKetqua = findViewById(R.id.lstResult);
        adapterKetqua = new ArrayAdapter<Result>(MainActivity.this, android.R.layout.simple_list_item_1, dsKetqua);
        lvKetqua.setAdapter(adapterKetqua);
    }
}
