package com.info.mobiluygulamagelistirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class main extends AppCompatActivity {
    
    public Toolbar mToolBar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ObjectAnimator animator;


    //firebase
    private FirebaseUser mevcutKullanici;
    private FirebaseAuth mYetki;
    private DatabaseReference kullanicilarReference;





    //fragmentleri aktarmak için liste,dizi oluşturuldur
    private ArrayList<Fragment> fragmentListesi=new ArrayList<>();
    private ArrayList<String> fragmentBaslikListesi=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolBar = findViewById(R.id.toolbar);

        mToolBar.setTitle("       Sorular...");
        mToolBar.setSubtitle("     Sadece sorular!!!");
       // mToolBar.setLogo(R.drawable.icon_chat);
        mToolBar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(mToolBar);
//********************
        tabLayout=findViewById(R.id.tabLayout);
        viewPager2=findViewById(R.id.viewPager);

        fragmentListesi.add(new SohbetFragment());
        fragmentListesi.add(new GruplarFragment());
        fragmentListesi.add(new KisilerFragment());

        //Adapterı asıl görüntüleme işlemini yapacak olan viewPager a aktardık.
        MyViewPagerAdapter adapter= new MyViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        fragmentBaslikListesi.add("Sohbet");
        fragmentBaslikListesi.add("Gruplar");
        fragmentBaslikListesi.add("Kisiler");



        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position)->tab.setText(fragmentBaslikListesi.get(position))).attach();

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_chat_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_people_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_emoji_people_24);

      //Firebase
        mYetki=FirebaseAuth.getInstance();
        mevcutKullanici=mYetki.getCurrentUser();
        kullanicilarReference=FirebaseDatabase.getInstance().getReference("Kullanicilar");
        kullanicilarReference=FirebaseDatabase.getInstance().getReference("Gruplar");



    }
   @Override
    protected void onStart() {
        super.onStart();

        if(mevcutKullanici==null){
            KullaniciyiLoginActivityeGonder();
        }
        else {

            kullanicininVarliginiDogrula();
        }
    }

    private void kullanicininVarliginiDogrula() {

       String mevcutKullaniciId=mYetki.getCurrentUser().getUid();
       //Database de chil ların altında özelliklerinde bulunmasını sağladık.
       kullanicilarReference.child("Kullanicilar").child(mevcutKullaniciId).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.child("ad").exists()){

                   Toast.makeText(main.this,"Hoşgeldiniz..", Toast.LENGTH_LONG).show();
               }
               else{
                   //Intent profil=new Intent(main.this,profil.class);
                  // profil.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                 //  startActivity(profil);
                   //finish();
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    private void KullaniciyiLoginActivityeGonder() {

        Intent loginIntent= new Intent(main.this,giris.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }


    //Görüntülemeyi organize burada edilir.
    private class MyViewPagerAdapter extends FragmentStateAdapter{

         //Bu yapı sayesinde adapterı kullanırız
        public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        //Fragment sınıfından nesne bekleniyor
        public Fragment createFragment(int position) {
            //get sayesiindenesneleri sırayla çekebiliriz
            return fragmentListesi.get(position);
        }

        @Override
        //Kaç tane fragmenti organize edeceği burada belirtilir.
        public int getItemCount() {

            return fragmentListesi.size();
        }
    }

    @Override//tool bar menüsü görünür hale getirildi.
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_menu,menu); //tasarımı kodlama alanına taşımak için kullanılır
        return true;
    }

    @Override//tıklanılan optionun davrnaışı belirlenir.
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {

            case R.id.action_profil:
                Intent profil = new Intent(main.this, profil.class);
                startActivity(profil);
                break;
                //return true;
                // Toast.makeText(getApplicationContext(), "Profile Tıklandı",Toast.LENGTH_SHORT).show();



            case R.id.action_bilgi:
                Intent bilgi = new Intent(main.this, bilgi.class);
                //Toast.makeText(getApplicationContext(), "Bilgi Tıklandı",Toast.LENGTH_SHORT).show();
                // return true;
                startActivity(bilgi);
                break;


            case R.id.action_ayarlar:
                Intent ayarlar = new Intent(main.this, ayarlar.class);
                // Toast.makeText(getApplicationContext(), "Ayarlara Tıklandı",Toast.LENGTH_SHORT).show();
                // return true;
                startActivity(ayarlar);
                break;


            case R.id.action_cikis:

                Intent cikis = new Intent(main.this, giris.class);
                startActivity(cikis);
                 // return true;
                break;

            case R.id.action_grup_olustur:

                yeniGrupTalebi();
                break;

            default:
                return super.onOptionsItemSelected(item);



        }


        return false;
    }

    //Grup oluşturma işlemleri
    private void yeniGrupTalebi() {
        //AlerDialog da positiveButton ve NegativeBottonn diye 2 buton bulunur.

        AlertDialog.Builder builder= new AlertDialog.Builder(main.this,R.style.AlertDialog);
        builder.setTitle("Grup Adı Giriniz:");

        final EditText grupAdiAlani=new EditText(main.this);
        grupAdiAlani.setHint("Örnekk:Matematik");
        builder.setView(grupAdiAlani);

        builder.setPositiveButton("Oluştur", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String grupAdi= grupAdiAlani.getText().toString();

                //GrupAdinin boş kalmaması sağlandı.
                if(TextUtils.isEmpty(grupAdi)){
                    Toast.makeText(main.this,"Grup Adı Boş Bırakılmaz",Toast.LENGTH_LONG).show();
                }
                else{

                    YeniGrupOlustur(grupAdi);
                }

            }
        });

        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });

        builder.show();
    }

    //Eğer grupAdi boş değil ise bu metod ile verilen isimde bir grup oluştururuz.
    private void YeniGrupOlustur(String grupAdi) {

        kullanicilarReference.child(grupAdi).setValue("")
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){

                    Toast.makeText(main.this, grupAdi+"  adlı grup başarıyla oluşturuldu", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}