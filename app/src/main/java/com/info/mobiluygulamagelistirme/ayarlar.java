package com.info.mobiluygulamagelistirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ayarlar extends AppCompatActivity {

    private Button ayarlar_btn_guncelle,sifre_degistirme_btn;
    private EditText ayarlar_isim, ayarlar_soyisim,ayarlar_numara,ayarlar_email,ayarlar_telNo;


    //Firebase
    private FirebaseAuth mYetki;
    private DatabaseReference veriyolu;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseUser user;

    private String mevcutKullaniciId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

        //Firebase
        mYetki=FirebaseAuth.getInstance();
        veriyolu= FirebaseDatabase.getInstance().getReference();

        //mecvut kullanıcının id sini atarız
        mevcutKullaniciId=mYetki.getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        ayarlar_isim=findViewById(R.id.ayarlar_isim);
        ayarlar_soyisim=findViewById(R.id.ayarlar_soyisim);
        ayarlar_numara=findViewById(R.id.ayarlar_numara);

        ayarlar_email=findViewById(R.id.ayarlar_email);
        ayarlar_telNo=findViewById(R.id.ayarlar_telNo);

        ayarlar_btn_guncelle=findViewById(R.id.ayarlar_btn_guncelle);
        sifre_degistirme_btn=findViewById(R.id.sifre_degistirme_btn);

        sifre_degistirme_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String kullaniciEmailAyarla= mYetki.getCurrentUser().getEmail();
                mYetki.sendPasswordResetEmail(kullaniciEmailAyarla)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ayarlar.this, "Yeni parola için gerekli bağlantı adresinize gönderildi!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ayarlar.this, "Mail gönderme hatası!", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });

            }
        });

        ayarlar_btn_guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                AyarlariGuncelle();
            }
        });

        AyarlarBilgisiAl();

        }

    ///Veri tabanından veri çekme işlemi
    private void AyarlarBilgisiAl() {

        veriyolu.child("Kullanici_ayarlar").child(mevcutKullaniciId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        if ((datasnapshot.exists())&&(datasnapshot.hasChild("ad")&&(datasnapshot.hasChild("soyad")&&(datasnapshot.hasChild("email")&&(datasnapshot.hasChild("tel-no")
                        &&(datasnapshot.hasChild("numara")&&(datasnapshot.hasChild("sifre")&&(datasnapshot.hasChild("sifre-tekrar"))))))))){

                            String kullaniciAdiAyarla=datasnapshot.child("ad").getValue().toString();
                            String  kullaniciSoyadiAyarla=datasnapshot.child("soyad").getValue().toString();
                            String kullaniciEmailAyarla=datasnapshot.child("e-mail").getValue().toString();
                            String  kullaniciTelNoAyarla=datasnapshot.child("tel-no").getValue().toString();
                            String kullaniciNumaraAyarla=datasnapshot.child("numara").getValue().toString();



                            ayarlar_isim.setText(kullaniciAdiAyarla);
                            ayarlar_soyisim.setText(kullaniciSoyadiAyarla);
                            ayarlar_email.setText(kullaniciEmailAyarla);
                            ayarlar_telNo.setText(kullaniciTelNoAyarla);
                            ayarlar_numara.setText(kullaniciNumaraAyarla);



                        }

                        else if ((datasnapshot.exists())&&(datasnapshot.hasChild("ad")&&(datasnapshot.hasChild("soyad")))){

                            String kullaniciAdiAyarla=datasnapshot.child("ad").getValue().toString();
                            String kullaniciSoydiAyarla=datasnapshot.child("soyad").getValue().toString();
                            String kullaniciEmailAyarla=datasnapshot.child("e-mail").getValue().toString();
                            String  kullaniciTelNoAyarla=datasnapshot.child("tel-no").getValue().toString();
                            String kullaniciNumaraAyarla=datasnapshot.child("numara").getValue().toString();



                            ayarlar_isim.setText(kullaniciAdiAyarla);
                            ayarlar_soyisim.setText(kullaniciSoydiAyarla);
                            ayarlar_email.setText(kullaniciEmailAyarla);
                            ayarlar_telNo.setText(kullaniciTelNoAyarla);
                            ayarlar_numara.setText(kullaniciNumaraAyarla);


                        }

                        else{
                            ayarlar_isim.setVisibility(View.VISIBLE);
                            Toast.makeText(ayarlar.this, "Lütfen  bilgilerinizi ayarlayınız! ", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    ///Veri tabanına bilgi kaydetme işlemi
    private void AyarlariGuncelle() {

        String kullaniciAdiAyarla= ayarlar_isim.getText().toString();
        String  kullaniciSoyadiAyarla=ayarlar_soyisim.getText().toString();
        String kullaniciEmailAyarla= ayarlar_email.getText().toString();
        String  kullaniciTelNoAyarla=ayarlar_telNo.getText().toString();
        String kullaniciNumaraAyarla= ayarlar_numara.getText().toString();



        if (TextUtils.isEmpty(kullaniciAdiAyarla)){
            Toast.makeText(this,"Lütfen adınızı yazınn..",Toast.LENGTH_LONG).show();
        }

        if (TextUtils.isEmpty(kullaniciSoyadiAyarla)){
            Toast.makeText(this,"Lütfen soyadınızı yazınn..",Toast.LENGTH_LONG).show();

        }
        if (TextUtils.isEmpty(kullaniciEmailAyarla)){
            Toast.makeText(this,"Lütfen e-mailinizii yazınn..",Toast.LENGTH_LONG).show();
            ayarlar_email.setText(mYetki.getCurrentUser().getEmail());

        }
        if (TextUtils.isEmpty(kullaniciTelNoAyarla)){
            Toast.makeText(this,"Lütfen telefon numaranızı yazınn..",Toast.LENGTH_LONG).show();

        }
        if (TextUtils.isEmpty(kullaniciNumaraAyarla)){
            Toast.makeText(this,"Lütfen numaranızı yazınn..",Toast.LENGTH_LONG).show();

        }



        else{
            HashMap<String,String> AyarlarHaritasi= new HashMap<>();
            //Firebase göndeririz
            AyarlarHaritasi.put("uid",mevcutKullaniciId);
            AyarlarHaritasi.put("ad",kullaniciAdiAyarla);
            AyarlarHaritasi.put("soyad",kullaniciSoyadiAyarla);
            AyarlarHaritasi.put("e-mail",kullaniciEmailAyarla);
            AyarlarHaritasi.put("tel-no",kullaniciTelNoAyarla);
            AyarlarHaritasi.put("numara",kullaniciNumaraAyarla);


            veriyolu.child("Kullanici_ayarlar").child(mevcutKullaniciId).setValue(AyarlarHaritasi)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){


                                Toast.makeText(ayarlar.this,"Bilgileriniz başarılı bir şekilde güncellendi..",Toast.LENGTH_SHORT).show();
                                Intent anasayfa=new Intent (ayarlar.this,main.class);
                                //anasayfa.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(anasayfa);
                                // finish();
                            } //}
                            else{
                                String mesaj=task.getException().toString();
                                Toast.makeText(ayarlar.this,"Hata"+mesaj, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }




    }
