package com.example.project.model;

/**
 * Created by Balpir on 20/11/2020.
 */

public class DataModelKecamatan {
    private String id_kecamatan, nama_kabupaten ,nama_kecamatan,jumlahikm,lat,lng;

    public DataModelKecamatan() {
    }

    public DataModelKecamatan(String id_kecamatan, String nama_kabupaten, String nama_kecamatan, String jumlahikm, String lat, String lng) {
        this.id_kecamatan = id_kecamatan;
        this.nama_kabupaten = nama_kabupaten;
        this.nama_kecamatan = nama_kecamatan;
        this.jumlahikm = jumlahikm;
        this.lat=lat;
        this.lng=lng;
    }

    public String getId_kecamatan() {
        return id_kecamatan;
    }

    public void setId_kecamatan(String id_kecamatan) {
        this.id_kecamatan = id_kecamatan;
    }

    public String getNama_kabupaten() {
        return nama_kabupaten;
    }

    public void setNama_kabupaten(String nama_kabupaten) {
        this.nama_kabupaten = nama_kabupaten;
    }

    public String getNama_kecamatan() {
        return nama_kecamatan;
    }

    public void setNama_kecamatan(String nama_kecamatan) {
        this.nama_kecamatan = nama_kecamatan;
    }

    public String getJumlahikm() {
        return jumlahikm;
    }

    public void setJumlahikm(String jumlahikm) {
        this.jumlahikm = jumlahikm;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
