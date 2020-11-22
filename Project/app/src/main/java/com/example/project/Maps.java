package com.example.project;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.project.app.AppController;
import com.example.project.model.DataModelBengkel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skyfishjy.library.RippleBackground;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class Maps extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,SearchView.OnQueryTextListener, OnMapReadyCallback {

    ProgressDialog pDialog;
    SwipeRefreshLayout swipe;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient mPlacesClient;

    private SearchView searchBar;
    private Button btn_find;
    private ArrayList<DataModelBengkel> predictionlist;
    MapFragment mapFragment;
    String nama_bengkel,id_bengkel, hari_kerja, jam_buka, jam_tutup;
    FloatingActionButton lokasisaya;

    private RippleBackground ripplebg;
    CameraPosition cameraPosition;
    MarkerOptions markerOptions = new MarkerOptions();
    LatLng center,latLng;
    Double lat,lng;


    public static final String url_data = com.example.project.URL_SERVER.Ctampilbengkel;
    public static final String url_cari = URL_SERVER.Ccaribengkel;

    public static final String ID = "id_bengkel";
    public static final String TITLE = "nama_bengkel";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String HariKerja = "hari_kerja";
    public static final String JamBuka = "jam_buka";
    public static final String JamTutup = "jam_tutup";

    public static final String TAG_RESULTS = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_VALUE = "value";

    String tag_json_obj = "json_obj_req";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchBar = findViewById(R.id.search_view);
        searchBar.setQueryHint("Cari Kecamatan");
        searchBar.setIconified(true);
        searchBar.setOnQueryTextListener(this);
        btn_find = findViewById(R.id.btn_find);
        ripplebg = findViewById(R.id.ripple_bg);
        lokasisaya = findViewById(R.id.btn_lokasi);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refreshmaps);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           callData();
                       }
                   }
        );




        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng currentMarkerLocation = mMap.getCameraPosition().target;
                ripplebg.startRippleAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ripplebg.stopRippleAnimation();
                        Intent ten = new Intent(Maps.this,AboutUs.class);
                        ten.putExtra("dekat","ada");
                        startActivity(ten);
                        finish();
                    }
                }, 3000);
            }
        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMyLocationEnabled(true);

        center = new LatLng(3.3280664, 99.1191069);
        cameraPosition = new CameraPosition.Builder().target(center).zoom(13).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        lokasisaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLocation getLocation = new GetLocation(getApplicationContext());
                Location location = getLocation.getLocation();
                Double latSaya = location.getLatitude();
                Double lngSaya = location.getLongitude();
                center = new LatLng(latSaya, lngSaya);
                cameraPosition = new CameraPosition.Builder().target(center).zoom(16).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
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
        pDialog = new ProgressDialog(Maps.this);
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
                        mMap.clear();
//                        mapFragment.onDestroy();
//                        adapter.notifyDataSetChanged();

                        String getObject = jObj.getString(TAG_RESULTS);
                        JSONArray jsonArray = new JSONArray(getObject);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            id_bengkel = obj.getString(ID);
                            nama_bengkel = obj.getString(TITLE);
                            hari_kerja = obj.getString(HariKerja);
                            jam_buka = obj.getString(JamBuka);
                            jam_tutup = obj.getString(JamTutup);
                            //latLng = new LatLng(Double.parseDouble(obj.getString(LNG)), Double.parseDouble(obj.getString(LAT)));
                            lat = Double.valueOf(obj.getString(LAT));
                            lng = Double.valueOf(obj.getString(LNG));
                            addMarker(lng, lat, nama_bengkel, id_bengkel, hari_kerja, jam_buka, jam_tutup);
                        }
                        cameraPosition = new CameraPosition.Builder().target(latLng).zoom(10).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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


    private void callData() {
//        listData.clear();
//        adapter.notifyDataSetChanged();
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
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            id_bengkel = obj.getString(ID);
                            nama_bengkel = obj.getString(TITLE);
                            hari_kerja = obj.getString(HariKerja);
                            jam_buka = obj.getString(JamBuka);
                            jam_tutup = obj.getString(JamTutup);
                            //latLng = new LatLng(Double.parseDouble(obj.getString(LNG)), Double.parseDouble(obj.getString(LAT)));
                            lat = Double.valueOf(obj.getString(LAT));
                            lng = Double.valueOf(obj.getString(LNG));
                            addMarker(lng, lat, nama_bengkel, id_bengkel, hari_kerja, jam_buka, jam_tutup);
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


    private void addMarker(final Double lng,final Double lat, final String nama_bengkel,  final String id_bengkel, final String hari_kerja,
                           final String jam_buka,final String jam_tutup) {
        latLng = new LatLng(lng,lat);
        GetLocation getLocation = new GetLocation(getApplicationContext());
        Location location = getLocation.getLocation();
        Double latSaya = location.getLatitude();
        Double lngSaya = location.getLongitude();
        GetJarak getJarak = new GetJarak(lng,lat,latSaya,lngSaya);
        long jar = Math.round(getJarak.cariJarak());
        markerOptions.position(latLng);
        markerOptions.title("Bengkel "+nama_bengkel);
        markerOptions.snippet("Jarak : "+jar+" meter");
        if(jar <= 500){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
        else {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }

        Marker marker=mMap.addMarker(markerOptions);
        marker.setTag(id_bengkel);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String clikTag=(String)marker.getTag();
                Intent kotak = new Intent(com.example.project.Maps.this, DetailBengkel.class);
                kotak.putExtra("id_bengkel",clikTag);
                startActivity(kotak);
                Toasty.info(com.example.project.Maps.this,clikTag, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        callData();
    }

}