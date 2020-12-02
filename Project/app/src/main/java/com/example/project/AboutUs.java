package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.project.adapter.AdapterBengkel;
import com.example.project.adapter.GroupAdapter;
import com.example.project.app.AppController;
import com.example.project.model.DataModelBengkel;
import com.example.project.model.ModelGambar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AboutUs extends AppCompatActivity implements AdapterView.OnItemSelectedListener,SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener{
    DrawerLayout drawerLayout;

    ProgressDialog pDialog;
    SwipeRefreshLayout swipe;
    RecyclerView recyclerView;
    SearchView searchBar;
    private ArrayList<DataModelBengkel> listData;
    AdapterBengkel adapter;

    public static final String url_data = URL_SERVER.Ctampilbengkel;
    public static final String url_cari = URL_SERVER.Ccaribengkel;
    public static final String url_cariFas = URL_SERVER.Ccarifas;

    private static final String TAG = DetailBengkel.class.getSimpleName();

    public static final String TAG_IDBENGKEL = "id_bengkel";
    public static final String TAG_NamaBengkel= "nama_bengkel";
    public static final String TAG_NamaKecamatan = "nama_kecamatan";
    public static final String TAG_NamaKel= "nama_kel";
    public static final String TAG_LAT = "lat";
    public static final String TAG_LNG= "lng";
    public static final String TAG_AlamatBengkel= "alamat_bengkel";
    public static final String TAG_GambarSampul = "gambar_sampul";
    public static final String TAG_HariKerja = "hari_kerja";
    public static final String TAG_jamBuka = "jam_buka";
    public static final String TAG_JamTutup = "jam_tutup";
    public static final String TAG_NamaPemilik = "nama_pemilik";
    public static final String TAG_NoHp = "no_hp";

    public static final String TAG_RESULTS = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_VALUE = "value";

    String tag_json_obj = "json_obj_req";
    String dekat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Spinner spinfasi = findViewById(R.id.spinfasi);
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,R.array.fasilitas, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinfasi.setAdapter(adapterSpinner);
        spinfasi.setOnItemSelectedListener(this);


        Spinner spinsort = findViewById(R.id.spinsorting);
        ArrayAdapter<CharSequence> adapterSpinsort = ArrayAdapter.createFromResource(this,R.array.sort, android.R.layout.simple_spinner_item);
        adapterSpinsort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinsort.setAdapter(adapterSpinsort);
        spinsort.setOnItemSelectedListener(this);


        drawerLayout = findViewById(R.id.drawer_layout);

        searchBar = findViewById(R.id.search_data);
        searchBar.setQueryHint("Cari Bengkel");
        searchBar.setIconified(true);
        searchBar.setOnQueryTextListener(this);

        recyclerView = findViewById(R.id.recyclerview);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refreshData);
        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           callData(null);
                       }
                   }
        );
    }
    private void callData(String shortt) {
        swipe.setRefreshing(true);

        // Creating volley request obj
        StringRequest jArr = new StringRequest(Request.Method.GET, url_data, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    int value = jObj.getInt(TAG_VALUE);
                    if (value == 1) {
                        String getObject = jObj.getString(TAG_RESULTS);
                        JSONArray jsonArray = new JSONArray(getObject);
                        listData = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            DataModelBengkel item = new DataModelBengkel();

                            GetLocation getLocation = new GetLocation(getApplicationContext());
                            Location location = getLocation.getLocation();
                            Double latSaya = location.getLatitude();
                            Double lngSaya = location.getLongitude();
                            double lng = Double.valueOf(obj.getString(TAG_LNG));
                            double lat = Double.valueOf(obj.getString(TAG_LAT));
                            GetJarak getJarak = new GetJarak(lng,lat,latSaya,lngSaya);
                            long jar = Math.round(getJarak.cariJarak());

                            Bundle bundle = getIntent().getExtras();
                            if (bundle != null){
                                dekat = bundle.getString("dekat");
                                if (dekat.equals("ada") ){
                                    if (jar <= 500){
                                        item.setNama_bengkel(obj.getString(TAG_NamaBengkel));
                                        item.setAlamat_bengkel(obj.getString(TAG_AlamatBengkel));
                                        item.setGambar_sampul(obj.getString(TAG_GambarSampul));
                                        item.setHari_kerja(obj.getString(TAG_HariKerja));
                                        item.setLat(obj.getString(TAG_LAT));
                                        item.setLng(obj.getString(TAG_LNG));
                                        item.setJarak(String.valueOf(jar));
                                        item.setId_bengkel(obj.getString(TAG_IDBENGKEL));
                                        item.setJam_buka(obj.getString(TAG_jamBuka));
                                        item.setJam_tutup(obj.getString(TAG_JamTutup));
                                        item.setNama_kecamatan(obj.getString(TAG_NamaKecamatan));
                                        item.setNama_kel(obj.getString(TAG_NamaKel));
                                        item.setNama_pemilik(obj.getString(TAG_NamaPemilik));
                                        item.setNo_hp(obj.getString(TAG_NoHp));
                                        listData.add(item);
                                    }
                                }
                            }
                            else{
                                item.setNama_bengkel(obj.getString(TAG_NamaBengkel));
                                item.setAlamat_bengkel(obj.getString(TAG_AlamatBengkel));
                                item.setGambar_sampul(obj.getString(TAG_GambarSampul));
                                item.setHari_kerja(obj.getString(TAG_HariKerja));
                                item.setLat(obj.getString(TAG_LAT));
                                item.setLng(obj.getString(TAG_LNG));
                                item.setJarak(String.valueOf(jar));
                                item.setId_bengkel(obj.getString(TAG_IDBENGKEL));
                                item.setJam_buka(obj.getString(TAG_jamBuka));
                                item.setJam_tutup(obj.getString(TAG_JamTutup));
                                item.setNama_kecamatan(obj.getString(TAG_NamaKecamatan));
                                item.setNama_kel(obj.getString(TAG_NamaKel));
                                item.setNama_pemilik(obj.getString(TAG_NamaPemilik));
                                item.setNo_hp(obj.getString(TAG_NoHp));
                                listData.add(item);
                            }
                        }
                        if (shortt != null){
                            if (shortt.equals("Nama")){
                                Collections.sort(listData, new Comparator<DataModelBengkel>() {
                                    @Override
                                    public int compare(DataModelBengkel o1, DataModelBengkel o2) {
                                        return o1.getNama_bengkel().compareTo(o2.getNama_bengkel());
                                    }
                                });
                                adapter = new AdapterBengkel(AboutUs.this, listData);
                                recyclerView.setAdapter(adapter);
                            }else if(shortt.equals("Jarak")){
                                Collections.sort(listData, new Comparator<DataModelBengkel>() {
                                    @Override
                                    public int compare(DataModelBengkel o1, DataModelBengkel o2) {
                                        return Integer.parseInt(o1.getJarak())- Integer.parseInt(o2.getJarak());
                                    }
                                });
                                adapter = new AdapterBengkel(AboutUs.this, listData);
                                recyclerView.setAdapter(adapter);
                            }
                            else {

                            }

                        }
                        else {
                            adapter = new AdapterBengkel(AboutUs.this, listData);
                            recyclerView.setAdapter(adapter);
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

//                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jArr, tag_json_obj);
    }
    @Override
    public void onRefresh() {
        callData(null);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        cariData(newText);
        return false;
    }

    private void cariData(final String keyword) {
        pDialog = new ProgressDialog(AboutUs.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_cari, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    int value = jObj.getInt(TAG_VALUE);
                    if (value == 1) {
                        String getObject = jObj.getString(TAG_RESULTS);
                        JSONArray jsonArray = new JSONArray(getObject);
                        listData = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            DataModelBengkel item = new DataModelBengkel();
                            GetLocation getLocation = new GetLocation(getApplicationContext());
                            Location location = getLocation.getLocation();
                            Double latSaya = location.getLatitude();
                            Double lngSaya = location.getLongitude();
                            double lng = Double.valueOf(obj.getString(TAG_LNG));
                            double lat = Double.valueOf(obj.getString(TAG_LAT));
                            GetJarak getJarak = new GetJarak(lng,lat,latSaya,lngSaya);
                            long jar = Math.round(getJarak.cariJarak());

                            Bundle bundle = getIntent().getExtras();
                            if (bundle != null){
                                dekat = bundle.getString("dekat");
                                if (dekat.equals("ada") ){
                                    if (jar <= 500){
                                        item.setNama_bengkel(obj.getString(TAG_NamaBengkel));
                                        item.setAlamat_bengkel(obj.getString(TAG_AlamatBengkel));
                                        item.setGambar_sampul(obj.getString(TAG_GambarSampul));
                                        item.setHari_kerja(obj.getString(TAG_HariKerja));
                                        item.setLat(obj.getString(TAG_LAT));
                                        item.setLng(obj.getString(TAG_LNG));

                                        item.setJarak(String.valueOf(jar));
                                        item.setId_bengkel(obj.getString(TAG_IDBENGKEL));
                                        item.setJam_buka(obj.getString(TAG_jamBuka));
                                        item.setJam_tutup(obj.getString(TAG_JamTutup));
                                        item.setNama_kecamatan(obj.getString(TAG_NamaKecamatan));
                                        item.setNama_kel(obj.getString(TAG_NamaKel));
                                        item.setNama_pemilik(obj.getString(TAG_NamaPemilik));
                                        item.setNo_hp(obj.getString(TAG_NoHp));
                                    }
                                }
                            }
                            else{
                                item.setNama_bengkel(obj.getString(TAG_NamaBengkel));
                                item.setAlamat_bengkel(obj.getString(TAG_AlamatBengkel));
                                item.setGambar_sampul(obj.getString(TAG_GambarSampul));
                                item.setHari_kerja(obj.getString(TAG_HariKerja));
                                item.setLat(obj.getString(TAG_LAT));
                                item.setLng(obj.getString(TAG_LNG));


                                item.setJarak(String.valueOf(jar));
                                item.setId_bengkel(obj.getString(TAG_IDBENGKEL));
                                item.setJam_buka(obj.getString(TAG_jamBuka));
                                item.setJam_tutup(obj.getString(TAG_JamTutup));
                                item.setNama_kecamatan(obj.getString(TAG_NamaKecamatan));
                                item.setNama_kel(obj.getString(TAG_NamaKel));
                                item.setNama_pemilik(obj.getString(TAG_NamaPemilik));
                                item.setNo_hp(obj.getString(TAG_NoHp));
                                listData.add(item);
                            }
                        }
                        adapter = new AdapterBengkel(AboutUs.this, listData);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

//                adapter.notifyDataSetChanged();
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("keyword", keyword);


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinfasi:{
                String text = parent.getItemAtPosition(position).toString();
                if (text.equals("Pilih Fasilitas"))
                {
                }else{
                    cariDFasilitas(text);
                    parent.setSelection(0);
                }
                break;
            }
            case R.id.spinsorting:{
                String text = parent.getItemAtPosition(position).toString();
                callData(text);
                parent.setSelection(0);
            }
        }

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void cariDFasilitas(final String keyword) {
        pDialog = new ProgressDialog(AboutUs.this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_cariFas, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    int value = jObj.getInt(TAG_VALUE);
                    if (value == 1) {
                        String getObject = jObj.getString(TAG_RESULTS);
                        JSONArray jsonArray = new JSONArray(getObject);
                        listData = new ArrayList<>();
                        Log.e("Response: ", String.valueOf(jsonArray.length()));
                        Toast.makeText(AboutUs.this,"Menampilkan "+String.valueOf(jsonArray.length())+" Data",Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            DataModelBengkel item = new DataModelBengkel();

                            GetLocation getLocation = new GetLocation(getApplicationContext());
                            Location location = getLocation.getLocation();
                            Double latSaya = location.getLatitude();
                            Double lngSaya = location.getLongitude();

                            double lng = Double.valueOf(obj.getString(TAG_LNG));
                            double lat = Double.valueOf(obj.getString(TAG_LAT));
                            GetJarak getJarak = new GetJarak(lng,lat,latSaya,lngSaya);
                            long jar = Math.round(getJarak.cariJarak());


                                item.setNama_bengkel(obj.getString(TAG_NamaBengkel));
                                item.setAlamat_bengkel(obj.getString(TAG_AlamatBengkel));
                                item.setGambar_sampul(obj.getString(TAG_GambarSampul));
                                item.setHari_kerja(obj.getString(TAG_HariKerja));
                                item.setLat(obj.getString(TAG_LAT));
                                item.setLng(obj.getString(TAG_LNG));


                                item.setJarak(String.valueOf(jar));

                                item.setId_bengkel(obj.getString(TAG_IDBENGKEL));
                                item.setJam_buka(obj.getString(TAG_jamBuka));
                                item.setJam_tutup(obj.getString(TAG_JamTutup));
                                item.setNama_pemilik(obj.getString(TAG_NamaPemilik));
                                item.setNo_hp(obj.getString(TAG_NoHp));
                                listData.add(item);
                        }
                        adapter = new AdapterBengkel(AboutUs.this, listData);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

//                adapter.notifyDataSetChanged();
                pDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("keyword", keyword);


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }















    public void ClickMenu(View view){
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        //close drawer
        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickHome(View view){
        //recreate activity
        MainActivity.redirectActivity(this,MainActivity.class);
    }

    public void ClickDashboard(View view){
        MainActivity.redirectActivity(this,Maps.class);
    }
    public void ClickInfo(View view){
        //Redirect Activity to About us
        MainActivity.redirectActivity(this,ActivityInfo.class);
    }


    public void ClickAboutUs(View view){
        recreate();
    }

    public void ClickLogout(View view){
        MainActivity.logout(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MainActivity.closeDrawer(drawerLayout);
    }
}