package com.info.mobiluygulamagelistirme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class Sohbet extends AppCompatActivity {

    private Toolbar mToolBar;
    private ImageButton MesajGonderButton,photoButton;
    private EditText KullaniciMesajiGirdisi;
    private ScrollView mScrollWiev;
    private TextView metinMesajlariGoster;


    //Firebase
    private FirebaseAuth mYetki;
    private DatabaseReference kullaniciYolu,sohbetAdiYolu,grupMesajAnahtariYolu;
    private String mevcutKullaniciId;

    //Intent Değişken
    private String mevcutKisiAdi, aktifKullaniciId, aktifKullaniciAdi,aktifTarih,aktifZaman;

    private static final int GALLERY_PICK=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sohbet);

        //Intenti Al
        mevcutKisiAdi=getIntent().getExtras().get("kisiAdi").toString();
        Toast.makeText(this,mevcutKisiAdi,Toast.LENGTH_LONG).show();


        //FirebaseTanımlama
        mYetki= FirebaseAuth.getInstance();
        aktifKullaniciId=mYetki.getCurrentUser().getUid();
        mevcutKullaniciId=mYetki.getCurrentUser().getUid();
        kullaniciYolu= FirebaseDatabase.getInstance().getReference().child("Kullanicilar");
        sohbetAdiYolu= FirebaseDatabase.getInstance().getReference().child("kisi_ad").child(mevcutKisiAdi);


        //Tanımlamalar
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(mevcutKisiAdi);

        MesajGonderButton=findViewById(R.id.mesaj_gonderme_btn2);
        KullaniciMesajiGirdisi=findViewById(R.id.grup_mesaj_girdisi2);
        metinMesajlariGoster=findViewById(R.id.chat_metni_gosterme2);
        mScrollWiev=findViewById(R.id.my_scroll_view2);
        photoButton=findViewById(R.id.photo_gonderme_btn2);

        //Kullanici Bilgisi Alma
        kullaniciBilgisiAl();

        //Mesajı veritabanına kaydetme
        MesajGonderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  sendMessage();
                MesajiVeritabaninaKaydet();
                //Mesajı gönderdikten sonra ilgili editText boşaltılsın
                KullaniciMesajiGirdisi.setText("");
                //Otomatik scroll kullanımı sağlandı.Sayfa otomatik kaydırıldı
                mScrollWiev.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent=new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent,"Fotopraf Seçiniz."),GALLERY_PICK);

                MesajiVeritabaninaKaydet();
                /*CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(GrupChat.this);

*/
            }
        });
 /* public void sendMessage (){

        FirebaseUser user=mYetki.getCurrentUser();
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myref=database.getReference();
        String usermail=user.getEmail().toString();
        UUID uuid=UUID.randomUUID();
        String uuidstring = uuid.toString();
        String message=KullaniciMesajiGirdisi.getText().toString();
        myref.child("Posts").child(uuidstring).child("useremail").setValue(usermail);
        myref.child("Posts").child(uuidstring).child("message").child(message);

}}
*/


    }


    @Override
    //Activity Başladığında...
    protected void onStart() {
        super.onStart();

        sohbetAdiYolu.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot DataSnapshot, @Nullable String previousChildName) {

                if (DataSnapshot.exists()){
                    metinMesajlariGoster(DataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot DataSnapshot, @Nullable String previousChildName) {

                if (DataSnapshot.exists()){
                    metinMesajlariGoster(DataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot datasnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot DataSnapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void metinMesajlariGoster(DataSnapshot DataSnapshot) {

        //iterator satır satır yineleyerek işlem yapmamızı sağlar
        Iterator iterator=DataSnapshot.getChildren().iterator();

        while(iterator.hasNext()){
            String sohbetTarihi=(String) ((DataSnapshot)iterator.next()).getValue();
            String sohbetMesaji=(String) ((DataSnapshot)iterator.next()).getValue();
            String sohbetAdi=(String) ((DataSnapshot)iterator.next()).getValue();
            String sohbetZamani=(String) ((DataSnapshot)iterator.next()).getValue();

            metinMesajlariGoster.append(sohbetAdi+ " :\n"+ sohbetMesaji+ " \n"+ sohbetZamani + "  "+ sohbetTarihi+ " \n\n\n");

            //Otomatik scroll kullanımı sağlandı.Sayfa otomatik kaydırıldı
            mScrollWiev.fullScroll(ScrollView.FOCUS_DOWN);
        }

    }

    private void MesajiVeritabaninaKaydet() {

        String mesaj=KullaniciMesajiGirdisi.getText().toString();
        String mesajAnahtari=sohbetAdiYolu.push().getKey();

        if(TextUtils.isEmpty(mesaj)){
            Toast.makeText(this, "Mesaj alanı boş olamaz..", Toast.LENGTH_LONG).show();
        }
        else {
            //Mesajın tarihine erişim
            Calendar tarihIcinTakvim =Calendar.getInstance();
            SimpleDateFormat aktifTarihFormati=new SimpleDateFormat("MMM dd, yyyy");
            aktifTarih=aktifTarihFormati.format(tarihIcinTakvim.getTime());


            Calendar zamanIcinTakvim=Calendar.getInstance();
            SimpleDateFormat aktifZamanFormati=new SimpleDateFormat("hh:mm:ss a");
            aktifZaman=aktifZamanFormati.format(zamanIcinTakvim.getTime());

            //Veritabanına göndermek için HashMap kullanırız
            HashMap<String,Object> grupMesajAnahtari=new HashMap<>();
            sohbetAdiYolu.updateChildren(grupMesajAnahtari);

            grupMesajAnahtariYolu=sohbetAdiYolu.child(mesajAnahtari);

            HashMap<String,Object>mesajBilgisiMap=new HashMap<>();

            mesajBilgisiMap.put("ad",aktifKullaniciAdi);
            mesajBilgisiMap.put("mesaj",mesaj);
            mesajBilgisiMap.put("tarih",aktifTarih);
            mesajBilgisiMap.put("zaman",aktifZaman);


            grupMesajAnahtariYolu.updateChildren(mesajBilgisiMap);
        }

    }

    private void kullaniciBilgisiAl() {

        kullaniciYolu.child(aktifKullaniciId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists()){
                    aktifKullaniciAdi=datasnapshot.child("ad").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}
