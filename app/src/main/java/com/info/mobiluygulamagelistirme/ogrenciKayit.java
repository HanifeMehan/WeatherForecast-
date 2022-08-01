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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ogrenciKayit extends AppCompatActivity {

    public Button btn_OgrenciKaydol2;
    public Toolbar mToolBar;
    private EditText ogrenciAdi,ogrenciSoyadi,ogrenciNumarasi,ogrenciEPosta,ogrenciTelNo,ogrenciSifre,ogrenciSifreYeniden;
    private ProgressDialog yukleniyorDialog;

    //Firebase
    private FirebaseAuth mYetki;
    private DatabaseReference kokReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrenci_kayit);

        //Firebase
        mYetki=FirebaseAuth.getInstance();
        kokReference= FirebaseDatabase.getInstance().getReference();



        mToolBar = findViewById(R.id.toolbar);

        mToolBar.setTitle("       Öğrenci Giriş...");
        //  mToolBar.setSubtitle("     Sadece sorular!!!");
        mToolBar.setLogo(R.drawable.icon_chat);
        mToolBar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolBar);

        ogrenciAdi = findViewById(R.id.isim_ogrenci);
        ogrenciSoyadi = findViewById(R.id.soyisim_ogrenci);
        ogrenciNumarasi = findViewById(R.id.ogrenci_numarasi_kayit);
        ogrenciEPosta = findViewById(R.id.email_ogrenci);
        ogrenciTelNo = findViewById(R.id.telNo_ogrenci);
        ogrenciSifre = findViewById(R.id.sifre_ogrenci);
        ogrenciSifreYeniden = findViewById(R.id.sifre_ogrenci_yeniden);

        yukleniyorDialog=new ProgressDialog(this);


        btn_OgrenciKaydol2 = findViewById(R.id.btn_ogrenciKaydol2);

        btn_OgrenciKaydol2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YeniHesapOluştur();

            }
        });
    }

    private void YeniHesapOluştur() {
        String email = ogrenciEPosta.getText().toString();
        String ogrenciIsim = ogrenciAdi.getText().toString();
        String ogrenciSoyisim = ogrenciSoyadi.getText().toString();
        String ogrenciNumara = ogrenciNumarasi.getText().toString();
        String telno = ogrenciTelNo.getText().toString();
        String sifre = ogrenciSifre.getText().toString();
        String sifreYeniden = ogrenciSifreYeniden.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email boş olamaz..",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(ogrenciIsim)){
            Toast.makeText(this,"İsim boş olamaz..",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(ogrenciSoyisim)){
            Toast.makeText(this,"Soyisim boş olamaz..",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(ogrenciNumara)){
            Toast.makeText(this,"Numara boş olamaz..",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(telno)){
            Toast.makeText(this,"Tel No  boş olamaz..",Toast.LENGTH_SHORT).show();
        }

        if (TextUtils.isEmpty(sifre)){
            Toast.makeText(this,"Şifre boş olamaz..",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(sifreYeniden)){
            Toast.makeText(this,"Şifre Yeniden boş olamaz..",Toast.LENGTH_SHORT).show();
        }

        else{
            if(sifre.equals(sifreYeniden)) {

                yukleniyorDialog.setTitle("Yeni Hesap oluşturuluyor");
                yukleniyorDialog.setMessage("Lütfen bekleyin");
                //dışarıdan bir yere tıklandığında iptal edilir.
                yukleniyorDialog.setCanceledOnTouchOutside(true);
                yukleniyorDialog.show();




                mYetki.createUserWithEmailAndPassword(email,sifre)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    //Firebase deki mevcut kullanıcının getUId sini alırız.uİd: authentication kısmında her kullanıcının uId si vardır.
                                    String mevcutKullaniciId=mYetki.getCurrentUser().getUid();
                                    //kök çocuk ilişkisi.
                                    kokReference.child("Kullanicilar").child(mevcutKullaniciId).setValue("");


                                    Intent anaSayfa=new Intent(ogrenciKayit.this,profil.class);
                                    //Kayıttan sonra ana sayfaya aktarılır ve aşagıdaki kodla ana sayfada kalma sağlanır ,istenene kadar çıkış yapmaz.
                                    anaSayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                                            .FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(anaSayfa);
                                    finish();

                                    Toast.makeText(ogrenciKayit.this,"Yeni Hesap Başarı ile oluşturuldu...",Toast.LENGTH_SHORT).show();
                                    yukleniyorDialog.dismiss();
                                }
                                else{
                                    String mesaj=task.getException().toString();
                                    Toast.makeText(ogrenciKayit.this,"Hata:" + mesaj +"Bilgilerinizi kontrol edin",Toast.LENGTH_SHORT).show();
                                    yukleniyorDialog.dismiss();
                                }
                            }
                        });

            }
            else{
                Toast.makeText(ogrenciKayit.this, "Sifreler Farklı Kayıt Olunamaz.",Toast.LENGTH_SHORT).show();
            }

        }

    }

}