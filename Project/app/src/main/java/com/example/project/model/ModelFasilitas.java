package com.example.project.model;

/**
 * Created by Balpir on 20/11/2020.
 */

public class ModelFasilitas {
    String fasilitas, icon,nama ;

    public ModelFasilitas() {
    }

    public ModelFasilitas(String fasilitas, String icon, String nama) {
        this.fasilitas = fasilitas;
        this.icon = icon;
        this.nama = nama;
    }

    public String getFasilitas() {
        return fasilitas;
    }

    public void setFasilitas(String fasilitas) {
        this.fasilitas = fasilitas;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
