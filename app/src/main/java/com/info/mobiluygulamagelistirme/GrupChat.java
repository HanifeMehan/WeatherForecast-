package com.info.mobiluygulamagelistirme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class GrupChat extends AppCompatActivity {

    private static final String CHANNEL_ID = "";
    private Toolbar mToolBar;
    private ImageButton MesajGonderButton,photoButton;
    private EditText KullaniciMesajiGirdisi;
    private ScrollView mScrollWiev;
    private TextView metinMesajlariGoster;

    //Firebase
    private FirebaseAuth mYetki;
    private DatabaseReference kullaniciYolu,grupAdiYolu,grupMesajAnahtariYolu;
    private StorageReference resimYolu;


    private String mevcutKullaniciId;


    //Intent Değişken
    private String mevcutGrupAdi, aktifKullaniciId, aktifKullaniciAdi,aktifTarih,aktifZaman;

    private String myUrl="";
    private StorageTask uploadTask;
    private Uri resimUri;
    private static final int GALLERY_PICK=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grup_chat);

        //Intenti Al
        mevcutGrupAdi=getIntent().getExtras().get("grupAdi").toString();
        Toast.makeText(this,mevcutGrupAdi,Toast.LENGTH_LONG).show();

        //FirebaseTanımlama
        mYetki=FirebaseAuth.getInstance();
        aktifKullaniciId=mYetki.getCurrentUser().getUid();
        mevcutKullaniciId=mYetki.getCurrentUser().getUid();
        kullaniciYolu= FirebaseDatabase.getInstance().getReference().child("Kullanicilar");
        grupAdiYolu= FirebaseDatabase.getInstance().getReference().child("Gruplar").child(mevcutGrupAdi);
        resimYolu= FirebaseStorage.getInstance().getReference().child("Grup Sohbet Resimleri");



        //Tanımlamalar
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle(mevcutGrupAdi);

        MesajGonderButton=findViewById(R.id.mesaj_gonderme_btn);
        KullaniciMesajiGirdisi=findViewById(R.id.grup_mesaj_girdisi);
        metinMesajlariGoster=findViewById(R.id.chat_metni_gosterme);
        mScrollWiev=findViewById(R.id.my_scroll_view);
        photoButton=findViewById(R.id.photo_gonderme_btn);



        //Kullanici Bilgisi Alma
        kullaniciBilgisiAl();

        //Mesajı veritabanına kaydetme
        MesajGonderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    }
//Foto Gönderme
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && requestCode == RESULT_OK) {

            Uri imageUri=data.getData();

            final String current_user_ref="messages/"+mevcutKullaniciId+"/"+aktifKullaniciAdi;
            final String chat_user_ref="messages/"+aktifKullaniciAdi+"/"+mevcutKullaniciId;

            DatabaseReference user_message_push=grupAdiYolu.child("messages")
                    .child(mevcutGrupAdi).child(mevcutKullaniciId).push();

            final String push_id=user_message_push.getKey();

            StorageReference filepath=resimYolu.child("message_images").child(push_id+"jpg");

            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()){
                        String download_url=task.getResult().getStorage().getDownloadUrl().toString();

                        Map messageMap=new HashMap();
                        messageMap.put("message",download_url);
                        messageMap.put("seen",false);
                        messageMap.put("type","image");
                        messageMap.put("time", ServerValue.TIMESTAMP);
                        messageMap.put("from",mevcutGrupAdi);


                        Map messageUserMap=new HashMap();
                        messageUserMap.put(aktifKullaniciAdi+"/"+push_id,messageMap);
                        messageUserMap.put(mevcutGrupAdi+"/"+push_id,messageMap);

                        KullaniciMesajiGirdisi.setText("");

                        grupAdiYolu.updateChildren(messageUserMap,(databaseError,databaseReference)->{

                            if (databaseError!=null){
                                Log.d("Chat_LOG",databaseError.getMessage().toString());
                            }
                        });
                    }
                }
            });


        }
    }
*/
    @Override
    //Activity Başladığında...
    protected void onStart() {
        super.onStart();

        grupAdiYolu.addChildEventListener(new ChildEventListener() {
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
        String mesajAnahtari=grupAdiYolu.push().getKey();

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
            HashMap<String,Object>grupMesajAnahtari=new HashMap<>();
            grupAdiYolu.updateChildren(grupMesajAnahtari);

            grupMesajAnahtariYolu=grupAdiYolu.child(mesajAnahtari);

            HashMap<String,Object>mesajBilgisiMap=new HashMap<>();

            mesajBilgisiMap.put("ad",aktifKullaniciAdi);
            mesajBilgisiMap.put("mesaj",mesaj);
            mesajBilgisiMap.put("tarih",aktifTarih);
            mesajBilgisiMap.put("zaman",aktifZaman);

            grupMesajAnahtariYolu.updateChildren(mesajBilgisiMap);


            //Bildirimmm
         /*   NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.camera_icon)
                    .setContentText("Mesajlarınız Kontrol Ediniz...")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Much longer text that cannot fit one line..."))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);*/
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