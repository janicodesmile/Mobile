package com.example.project.model;

public class DataModelBengkel {
    private String id_bengkel,nama_kecamatan,nama_kel,nama_bengkel,lat,lng,alamat_bengkel,no_hp,nama_pemilik,hari_kerja,jam_buka,jam_tutup,gambar_sampul;
    private String jarak;

    public DataModelBengkel() {
    }

    public DataModelBengkel(String id_bengkel, String nama_kecamatan, String nama_kel, String nama_bengkel, String lat, String lng, String alamat_bengkel, String no_hp, String nama_pemilik, String hari_kerja, String jam_buka, String jam_tutup, String gambar_sampul, String jarak) {
        this.id_bengkel = id_bengkel;
        this.nama_kecamatan = nama_kecamatan;
        this.nama_kel = nama_kel;
        this.nama_bengkel = nama_bengkel;
        this.lat = lat;
        this.lng = lng;
        this.alamat_bengkel = alamat_bengkel;
        this.no_hp = no_hp;
        this.nama_pemilik = nama_pemilik;
        this.hari_kerja = hari_kerja;
        this.jam_buka = jam_buka;
        this.jam_tutup = jam_tutup;
        this.gambar_sampul = gambar_sampul;
        this.jarak = jarak;
    }

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }

    public String getId_bengkel() {
        return id_bengkel;
    }

    public void setId_bengkel(String id_bengkel) {
        this.id_bengkel = id_bengkel;
    }

    public String getNama_kecamatan() {
        return nama_kecamatan;
    }

    public void setNama_kecamatan(String nama_kecamatan) {
        this.nama_kecamatan = nama_kecamatan;
    }

    public String getNama_kel() {
        return nama_kel;
    }

    public void setNama_kel(String nama_kel) {
        this.nama_kel = nama_kel;
    }

    public String getNama_bengkel() {
        return nama_bengkel;
    }

    public void setNama_bengkel(String nama_bengkel) {
        this.nama_bengkel = nama_bengkel;
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

    public String getAlamat_bengkel() {
        return alamat_bengkel;
    }

    public void setAlamat_bengkel(String alamat_bengkel) {
        this.alamat_bengkel = alamat_bengkel;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getNama_pemilik() {
        return nama_pemilik;
    }

    public void setNama_pemilik(String nama_pemilik) {
        this.nama_pemilik = nama_pemilik;
    }

    public String getHari_kerja() {
        return hari_kerja;
    }

    public void setHari_kerja(String hari_kerja) {
        this.hari_kerja = hari_kerja;
    }

    public String getJam_buka() {
        return jam_buka;
    }

    public void setJam_buka(String jam_buka) {
        this.jam_buka = jam_buka;
    }

    public String getJam_tutup() {
        return jam_tutup;
    }

    public void setJam_tutup(String jam_tutup) {
        this.jam_tutup = jam_tutup;
    }

    public String getGambar_sampul() {
        return gambar_sampul;
    }

    public void setGambar_sampul(String gambar_sampul) {
        this.gambar_sampul = gambar_sampul;
    }
}
