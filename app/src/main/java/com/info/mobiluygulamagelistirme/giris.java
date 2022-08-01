package com.info.mobiluygulamagelistirme;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class giris extends AppCompatActivity {

      public Button btn_OgretmenGiris;
      public Button btn_OgrenciGiris;
      public ImageView image;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        btn_OgrenciGiris=findViewById(R.id.btn_ogrenciGiris);
        btn_OgrenciGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent giris= new Intent(giris.this,ogrenciGiris.class);
                startActivity(giris);

            }
        });

        btn_OgretmenGiris=findViewById(R.id.btn_ogretmenGiris);
        btn_OgretmenGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent giris2= new Intent(giris.this,ogretmenGiris.class);
                startActivity(giris2);

            }
        });

    }
}