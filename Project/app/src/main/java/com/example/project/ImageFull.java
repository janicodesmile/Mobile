package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageFull extends AppCompatActivity {


    ImageView myImage;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full);

        url = getIntent().getStringExtra("image_url");
        myImage = findViewById(R.id.myImage);

        Picasso.get()
                .load(url)
                .placeholder(R.drawable.img)
                .into(myImage);
    }
}