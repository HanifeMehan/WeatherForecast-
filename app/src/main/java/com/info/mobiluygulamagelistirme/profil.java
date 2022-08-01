package com.info.mobiluygulamagelistirme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class profil extends AppCompatActivity {

    private Button profilGüncelle;
    private EditText profilName;
    private CircleImageView kullaniciProfilResmi;

    //Firebase
    private FirebaseAuth mYetki;
    private DatabaseReference veriyolu,kisiAdYolu;
    private StorageReference kullaniciProfilResimYolu;
    private StorageTask yuklemeGorevi;

    private String mevcutKullaniciId;



    //Yükleniyor
    private ProgressDialog yukleniyorBar;

    //Uri
    Uri resimUri;
    String myUri="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        //Firebase

        //Firebase
        mYetki=FirebaseAuth.getInstance();
        veriyolu= FirebaseDatabase.getInstance().getReference();
        kisiAdYolu=FirebaseDatabase.getInstance().getReference();
        kullaniciProfilResimYolu= FirebaseStorage.getInstance().getReference().child("Profil Resimleri");

        //mecvut kullanıcının id sini atarız
        mevcutKullaniciId=mYetki.getCurrentUser().getUid();

        //yukleniyor tanımlama
        yukleniyorBar=new ProgressDialog(this);

        //kontrol tanımlamaları yapıldı.
        profilGüncelle = findViewById(R.id.btn_profil_guncelle);
        profilName = findViewById(R.id.profil_name);
        kullaniciProfilResmi = findViewById(R.id.kullanici_profil_resmi);

        profilGüncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfiliGuncelle();

                Intent anasayfa=new Intent(profil.this,main.class);
                startActivity(anasayfa);

            }
        });


        KullaniciBilgisiAl();


        kullaniciProfilResmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tıkladığında galeriyi açsın

                //Kırpma Activity açma
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(profil.this);

            }
        });


    }




    private String dosyaUzantisiAl(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //Resim seçme kodu
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&& resultCode==RESULT_OK){
            //Resim seçilirse
            CropImage.ActivityResult result=CropImage.getActivityResult(data);

            resimUri=result.getUri();
            //Kırpılan fotoyu profil resmi yapar
            kullaniciProfilResmi.setImageURI(resimUri);

        }
        else
        {
            Toast.makeText(this, "Resim Seçilemedi", Toast.LENGTH_LONG).show();
        }
    }




    ///Veri tabanından veri çekme işlemi
    private void KullaniciBilgisiAl() {


        veriyolu.child("Kullanicilar").child(mevcutKullaniciId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if ((dataSnapshot.exists())&& (dataSnapshot.hasChild("ad")&&(dataSnapshot.hasChild("resim")))){

                    String kullaniciAdiAl=dataSnapshot.child("ad").getValue().toString();
                    String kullaniciResimAl=dataSnapshot.child("resim").getValue().toString();

                    profilName.setText(kullaniciAdiAl);
                    Picasso.get().load(kullaniciResimAl).into(kullaniciProfilResmi);

                }
                else if ((dataSnapshot.exists())&& (dataSnapshot.hasChild("ad"))){

                    String kullaniciAdiAl=dataSnapshot.child("ad").getValue().toString();
                    String kullaniciResimAl=dataSnapshot.child("resim").getValue().toString();

                    profilName.setText(kullaniciAdiAl);
                    Picasso.get().load(kullaniciResimAl).into(kullaniciProfilResmi);
                }
                else
                {
                    profilName.setVisibility(View.VISIBLE);
                    Toast.makeText(profil.this, "Lütfen Profil Bilgilerinizi Ayarlayınız", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseerror) {

            }
        });


    }

    private void ProfiliGuncelle() {


        String kullaniciAdiAyarla= profilName.getText().toString();


        if (TextUtils.isEmpty(kullaniciAdiAyarla)){
            Toast.makeText(this,"Lütfen adınızı yazınn..",Toast.LENGTH_LONG).show();

        }


        else {
            resimYukle();
        }


    }

    private void resimYukle() {

        yukleniyorBar.setTitle("Bilgi Aktarma");
        yukleniyorBar.setMessage("Lütfen Bekleyin..");
        yukleniyorBar.setCanceledOnTouchOutside(false);
        yukleniyorBar.show();

        final StorageReference resimYolu=kullaniciProfilResimYolu.child(mevcutKullaniciId+"."+dosyaUzantisiAl(resimUri));
        yuklemeGorevi=resimYolu.putFile(resimUri);

        yuklemeGorevi.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }


                return resimYolu.getDownloadUrl();
            }

        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                //görev tamamlandığında
                if(task.isSuccessful()) {
                    //başarılıysa
                    Uri indirmeUrisi = task.getResult();
                    myUri = indirmeUrisi.toString();

                    DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference().child("Kullanicilar");

                    String gonderiId = veriYolu.push().getKey();

                    String kullaniciAdiAl = profilName.getText().toString();


                    HashMap<String, String> profilHaritası = new HashMap<>();
                    profilHaritası.put("uid", gonderiId);
                    profilHaritası.put("ad", kullaniciAdiAl);
                    //Resmin linki vertabanına kayıt edildi
                    profilHaritası.put("resim", myUri);


                    veriYolu.child(mevcutKullaniciId).setValue(profilHaritası);


                    kisiAdYolu.child("kisi_ad").child(kullaniciAdiAl).setValue(kullaniciAdiAl)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                 //       Toast.makeText(profil.this, kullaniciAdiAl + "  adlı grup başarıyla oluşturuldu", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });



                    yukleniyorBar.dismiss();

                }
                else {
                    //başarısızsa

                    String hata=task.getException().toString();
                    Toast.makeText(profil.this, "Hata: " +hata, Toast.LENGTH_SHORT).show();
                    yukleniyorBar.dismiss();
                }


            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //görev tamamlanmadığında
                Toast.makeText(profil.this, "Hata"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                yukleniyorBar.dismiss();
            }
        });



    }

    public EditText getProfilName() {
        return profilName;
    }



}