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

public class ogretmenKayit extends AppCompatActivity {

    public Button btn_OgretmenKaydol2;
    public Toolbar mToolBar;
    private EditText ogretmenAdi,ogretmenSoyadi,ogretmenNumarasi,ogretmenEPosta,ogretmenTelNo,ogretmenSifre,ogretmenSifreYeniden;
    private FirebaseAuth mYetki;
    private ProgressDialog yukleniyorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogretmen_kayit);

        //Firebase
        mYetki=FirebaseAuth.getInstance();

        mToolBar=findViewById(R.id.toolbar);

        mToolBar.setTitle("       Öğretmen Kayıt...");
        //mToolBar.setSubtitle("     Sadece sorular!!!");
        mToolBar.setLogo(R.drawable.icon_chat);
        mToolBar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolBar);

        ogretmenAdi = findViewById(R.id.ogretmen_isim_kayit);
        ogretmenSoyadi = findViewById(R.id._ogretmen_soyisim_kayit);
        ogretmenNumarasi = findViewById(R.id.ogretmen_numarası_kayit);
        ogretmenEPosta = findViewById(R.id.ogretmen_email);
        ogretmenTelNo = findViewById(R.id.ogretmen_telNo);
        ogretmenSifre = findViewById(R.id.ogretmen_sifre);
        ogretmenSifreYeniden = findViewById(R.id.ogretmen_sifre_yeniden);

        yukleniyorDialog=new ProgressDialog(this);


        btn_OgretmenKaydol2=findViewById(R.id.btn_ogretmenKaydol2);

        btn_OgretmenKaydol2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YeniHesapOluştur();
            }
        });
    }
    private void YeniHesapOluştur() {
        String email = ogretmenEPosta.getText().toString();
        String ogretmenIsim = ogretmenAdi.getText().toString();
        String ogretmenSoyisim = ogretmenSoyadi.getText().toString();
        String ogretmenNumara = ogretmenNumarasi.getText().toString();
        String telno = ogretmenTelNo.getText().toString();
        String sifre = ogretmenSifre.getText().toString();
        String sifreYeniden = ogretmenSifreYeniden.getText().toString();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email boş olamaz..",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(ogretmenIsim)){
            Toast.makeText(this,"İsim boş olamaz..",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(ogretmenSoyisim)){
            Toast.makeText(this,"Soyisim boş olamaz..",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(ogretmenNumara)){
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

                mYetki.createUserWithEmailAndPassword(email, sifre)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent anaSayfa = new Intent(ogretmenKayit.this, profil.class);
                                    //Kayıttan sonra ana sayfaya aktarılır ve aşagıdaki kodla ana sayfada kalma sağlanır ,istenene kadar çıkış yapmaz.
                                    anaSayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                                            .FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(anaSayfa);
                                    finish();

                                    Toast.makeText(ogretmenKayit.this, "Yeni Hesap Başarı ile oluşturuldu...", Toast.LENGTH_SHORT).show();
                                    yukleniyorDialog.dismiss();
                                } else {
                                    String mesaj = task.getException().toString();
                                    Toast.makeText(ogretmenKayit.this, "Hata:" + mesaj + "Bilgilerinizi kontrol edin", Toast.LENGTH_SHORT).show();
                                    yukleniyorDialog.dismiss();
                                }
                            }
                        });
            }
            else{
                Toast.makeText(ogretmenKayit.this, "Sifreler Farklı Kayıt Olunamaz.",Toast.LENGTH_SHORT).show();
            }


        }

    }

}