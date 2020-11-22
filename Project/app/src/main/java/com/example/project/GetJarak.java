package com.example.project;

public class GetJarak {
    Double latTujuan,lngTujuan,latUser,lngUser;

    public GetJarak(Double latTujuan, Double lngTujuan, Double latUser, Double lngUser) {
        this.latTujuan= latTujuan;
        this.lngTujuan = lngTujuan;
        this.latUser = latUser;
        this.lngUser=lngUser;
    }

    public double cariJarak(){
        Double pi = 3.14159265358979323846;
        Double lat1 = latTujuan;
        Double lng1 = lngTujuan;
        Double lat2 = latUser;
        Double lng2 = lngUser;
        Double R = 6371e3;

        Double latRad1 = lat1 * (pi / 180); // lat tujuan dalam bentuk radian
        Double latRad2 = lat2 * (pi / 180);
        Double deltaLatRad = (lat2 - lat1) * (pi / 180);
        Double deltaLongRad = (lng2 - lng1) * (pi / 180);

        //rumus raversine
        Double a = Math.sin(deltaLatRad / 2) * Math.sin(deltaLatRad / 2) + Math.cos(latRad1) * Math.cos(latRad2) * Math.sin(deltaLongRad/2) * Math.sin(deltaLongRad/2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double s = R * c; //jarak dalam meter
        return s;
    }
}
