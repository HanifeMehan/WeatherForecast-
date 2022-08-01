package com.info.mobiluygulamagelistirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ogretmenGiris extends AppCompatActivity {

        public FloatingActionButton btn_OgretmenGiris2;
        public Button btn_OgretmenKayit;
        public Toolbar mToolBar;
        private EditText email, sifre;
        private TextView sifremiUnuttumOgretmen;

    //Firebase
   private FirebaseUser mevcutKullanici;
    private FirebaseAuth mYetki;

    //Progress
    ProgressDialog girisDialog;

 /*@Override
    protected void onStart() {
        super.onStart();
        if(mevcutKullanici != null){
            kullaniciyiAnaActiviyeGonder();

        }
    }

    private void kullaniciyiAnaActiviyeGonder() {
        Intent AnaActivityeGonder=new Intent(ogretmenGiris.this,main.class);
        startActivity(AnaActivityeGonder);
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogretmen_giris);

      /*/  mToolBar=findViewById(R.id.toolbar);

        mToolBar.setTitle("       Öğretmen Giriş...");
        //mToolBar.setSubtitle("     Sadece sorular!!!");
        mToolBar.setLogo(R.drawable.icon_chat);
        mToolBar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolBar);
*/
        email = findViewById(R.id.email_ogretmen_giris);
        sifre = findViewById(R.id.sifre_ogretmen_giris);
        sifremiUnuttumOgretmen=findViewById(R.id.sifremiUnuttumOgretmen);


        //Progress
        girisDialog=new ProgressDialog(this);

        //Firebase
        mYetki=FirebaseAuth.getInstance();
        mevcutKullanici=mYetki.getCurrentUser();


        btn_OgretmenGiris2=findViewById(R.id.btn_ogretmenGiris2);
        btn_OgretmenGiris2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent giris= new Intent(ogretmenGiris.this,main.class);
                startActivity(giris);

            }
        });

        btn_OgretmenKayit=findViewById(R.id.btn_ogretmenKaydol);
        btn_OgretmenKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent kayit= new Intent(ogretmenGiris.this,ogretmenKayit.class);
                startActivity(kayit);

            }
        });


        sifremiUnuttumOgretmen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(ogretmenGiris.this,SifreDegistirme.class);
                startActivity(intent);
            }
        });


        btn_OgretmenGiris2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KullaniciGirisIzniVer();

            }
        });


    }
            private void KullaniciGirisIzniVer() {
                String eposta = email.getText().toString();
                String sifree=sifre.getText().toString();

                if (TextUtils.isEmpty(eposta)){
                    Toast.makeText(this,"E mail boş olmaz..",Toast.LENGTH_SHORT).show();

                }
                if (TextUtils.isEmpty(sifree))
                    Toast.makeText(this, "Sifre boş olamaz..", Toast.LENGTH_SHORT).show();

                else{
                    //Progress
                    girisDialog.setMessage("Giriş yapılıyor..");
                    girisDialog.setMessage("Lütfen bekleyin...");
                    girisDialog.setCanceledOnTouchOutside(true);
                    girisDialog.show();
                    //Giris
                    mYetki.signInWithEmailAndPassword(eposta,sifree)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        Intent anasayfa=new Intent(ogretmenGiris.this,main.class);
                                        anasayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(anasayfa);
                                        finish();

                                        Toast.makeText(ogretmenGiris.this,"Giriş başarılı",Toast.LENGTH_SHORT).show();
                                        girisDialog.dismiss();

                                    }
                                    else{
                                        String mesaj =task.getException().toString();
                                        Toast.makeText(ogretmenGiris.this,"Hata"+mesaj+"Bilgileri Kontrol ediniz",Toast.LENGTH_SHORT).show();
                                        girisDialog.dismiss();
                                    }
                                }
                            });

                }}


}