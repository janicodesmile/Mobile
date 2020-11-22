package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.project.adapter.AdapterBengkel;
import com.example.project.adapter.AdapterFasi;
import com.example.project.adapter.GroupAdapter;
import com.example.project.app.AppController;
import com.example.project.model.ModelFasilitas;
import com.example.project.model.ModelGambar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailBengkel extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    DrawerLayout drawerLayout;
    TextView NamaBengkel, alamat, harijam,jarak;
    ImageView sampul, imageView;
    ProgressDialog pDialog;

    SwipeRefreshLayout swipe;
    ListView list_view;
    RecyclerView recyclerView2,recyclerView;
    private ArrayList<ModelGambar> listData;
    GroupAdapter adapter;
    private ArrayList<ModelFasilitas> listData2;
    AdapterFasi adapter2;
    String _id;

    public static final String url_data = URL_SERVER.Ctampildetial;
    public static final String Csampul = URL_SERVER.GambarSampul;

    private static final String TAG = DetailBengkel.class.getSimpleName();

    public static final String TAG_IDBENGKEL = "id_ikm";
    public static final String TAG_NamaBengkel= "nama_bengkel";
    public static final String TAG_NamaKecamatan = "";
    public static final String TAG_NamaKel= "nama_pemilik";
    public static final String TAG_LAT = "lat";
    public static final String TAG_LNG= "lng";
    public static final String TAG_hariKerja = "hari_kerja";
    public static final String TAG_jamBuka= "jam_buka";
    public static final String TAG_jamTutup= "jam_tutup";
    public static final String TAG_AlamatBengkel= "alamat_bengkel";
    public static final String TAG_GambarSampul = "gambar_sampul";

    public static final String TAG_RESULTS = "data";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_VALUE = "status";
    public static final String TAG_GAMBAR = "gambar";
    public static final String TAG_GAMBAR_ = "gambar_";
    public static final String TAG_FASILITAS = "fasilitas";
    public static final String TAG_IdFas_ = "idfas_";
    public static final String TAG_NamaFas = "namafas_";
    public static final String TAG_IconFas = "iconfas_";
    RecyclerView.LayoutManager RecyclerViewLayoutManager,RecyclerViewLayoutManager2;
    LinearLayoutManager HorizontalLayout2,HorizontalLayout;

    String tag_json_obj = "json_obj_req";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bengkel);

        Bundle bundle = getIntent().getExtras();
        _id = bundle.getString("id_bengkel");

        NamaBengkel = findViewById(R.id.tv_NamaBengkel);
        sampul = findViewById(R.id.iv_gambarSampul);
        imageView = findViewById(R.id.img1);
        alamat = findViewById(R.id.alamatDetail);
        harijam = findViewById(R.id.harijamDetail);
        jarak = findViewById(R.id.jarakDetail);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refreshikm);
        drawerLayout = findViewById(R.id.drawer_layout);

        recyclerView = findViewById(R.id.recycler);

        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(RecyclerViewLayoutManager);

        HorizontalLayout = new LinearLayoutManager(DetailBengkel.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);

        //2

        recyclerView2 = findViewById(R.id.recyclefas);
        RecyclerViewLayoutManager2 = new LinearLayoutManager(getApplicationContext());
        recyclerView2.setLayoutManager(RecyclerViewLayoutManager2);

        HorizontalLayout2 = new LinearLayoutManager(DetailBengkel.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(HorizontalLayout2);

        swipe.setOnRefreshListener(this);

        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           callData();
                       }
                   }
        );

    }
    private void callData() {
        swipe.setRefreshing(true);

        // Creating volley request obj
        StringRequest jArr = new StringRequest(Request.Method.POST, url_data, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("Response: ", response.toString());


                try {
                    JSONObject jObj = new JSONObject(response);

                    int value = jObj.getInt(TAG_VALUE);
                    NamaBengkel.setText(jObj.getString(TAG_NamaBengkel));

                    if (value == 1) {


                        String getObject = jObj.getString(TAG_RESULTS);
                        JSONObject jsonData = new JSONObject(getObject);

                        GetLocation getLocation = new GetLocation(getApplicationContext());
                        Location location = getLocation.getLocation();
                        Double latSaya = location.getLatitude();
                        Double lngSaya = location.getLongitude();
                        double lng = Double.valueOf(jsonData.getString(TAG_LNG));
                        double lat = Double.valueOf(jsonData.getString(TAG_LAT));
                        GetJarak getJarak = new GetJarak(lng,lat,latSaya,lngSaya);
                        long jar = Math.round(getJarak.cariJarak());
                        if(jar >= 1000){
                            Double jarr = getJarak.cariJarak()/1000;
                            jarak.setText(String.format("%.2f", jarr) +" KM");
                        }
                        else{
                            jarak.setText(String.valueOf(jar) +" meter");
                        }

                        String anu = jsonData.getString(TAG_hariKerja)+" / "+jsonData.getString(TAG_jamBuka)+" - "+jsonData.getString(TAG_jamTutup);
                        harijam.setText(anu);
                        alamat.setText(jsonData.getString(TAG_AlamatBengkel));
                        Picasso.get()
                                .load(Csampul+jsonData.getString(TAG_GambarSampul))
                                .fit()
                                .centerCrop()
                                .placeholder(R.drawable.img)
                                .into(sampul);

                        String getObjectGambar = jObj.getString(TAG_GAMBAR);
                        JSONArray jsonArray = new JSONArray(getObjectGambar);
                        listData = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            ModelGambar item = new ModelGambar();
                            item.setGambar(obj.getString(TAG_GAMBAR_));
                            listData.add(item);
                        }
                        adapter = new GroupAdapter(DetailBengkel.this, listData);
                        recyclerView.setAdapter(adapter);

                        String getObjectFasilitas = jObj.getString(TAG_FASILITAS);
                        JSONArray jsonArrayfas = new JSONArray(getObjectFasilitas);
                        listData2 = new ArrayList<>();
                        for (int i = 0; i < jsonArrayfas.length(); i++) {
                            JSONObject obj2 = jsonArrayfas.getJSONObject(i);
                            ModelFasilitas item = new ModelFasilitas();
                            item.setIcon(obj2.getString(TAG_IconFas));
                            item.setNama(obj2.getString(TAG_NamaFas));
                            listData2.add(item);
                        }
                        adapter2 = new AdapterFasi(DetailBengkel.this, listData2);
                        recyclerView2.setAdapter(adapter2);
                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                swipe.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                swipe.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();

                params.put("id_bengkel", _id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jArr, tag_json_obj);
    }
    @Override
    public void onRefresh() {
        callData();
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

    public void ClickAboutUs(View view){
        MainActivity.redirectActivity(this,AboutUs.class);
    }

    public void ClickInfo(View view){
        //Redirect Activity to About us
        MainActivity.redirectActivity(this,ActivityInfo.class);
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