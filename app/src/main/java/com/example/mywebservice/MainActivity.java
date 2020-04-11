package com.example.mywebservice;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import.org.json.JSONObject;

import androidx.appcompat.app.AlertController;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listAkademik;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listAkademik = (RecyclerView) findViewById(R.id.ListAkademik);
        linearLayoutManager = new LinearLayoutManager( MainActivity.this);
        listAkademik.setLayoutManager(linearLayoutManager);
        new GetAkademikAsyncTask().execute();
    }

    private clas GetAkademikAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog( MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String params) {
            String respon = "";
            try {
                String url =
                        "http://sab.if-unpas.org/tugas/akademik.php?action=get_akademik";
                respon = CustomeHttp.executeHttpGet(url);
            } catch (Exception e) {
                respon = e.toString();
            }
            return respon;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            try {
                Log.e( "masuk", "RESPON result -> "+result) ;
                JSONObject object = new JSONObject(result);
                ArrayList<HashMap<String, String>> arr = new ArrayList<>();
                if (object.getString("success").equalsIgnoreCase( "1")) {
                    JSONArray array = object.getJSONArray("data");
                    HashMap<String, String> map;
                    for (int 1 = 0; i < array.length(); i++) {
                        JSONObject jsonObject= array.getJSONObject(i);
                        map = new HashMap<String, String>();
                        map.put("img_url", jsonObject.getString("img_url"));
                        map.put("singkatan", jsonObject.getString("singkatan"));
                        map.put("nama", jsonObject.getString("nama"));
                        map.put("url", jsonObject.getString("url"));
                        arr.add(map);
                    }
                }
                listAkademik.setAdapter(new AkademikAdapter(arr, MainActivity.this));
            } catch (Exception e) {
                Log.e("masuk","-> "+e.getMessage());
            }
        }
    }
}
