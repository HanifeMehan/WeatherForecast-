package com.info.mobiluygulamagelistirme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;


public class SohbetFragment extends Fragment {

    private View kisiCerceveView;
    private ListView list_view_sohbet;
    private ArrayAdapter<String>arrayAdapterKisi;
    private ArrayList<String>kisi_listeleri= new ArrayList<>();



    //Firebase
    private DatabaseReference kisiyolu;
    private FirebaseUser mevcutKullanici;
    private FirebaseAuth mYetki;




    private String mevcutKullaniciId;




    public SohbetFragment(){

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        kisiCerceveView= inflater.inflate(R.layout.fragment_sohbet, container, false);


        //Firebase Tanımlama
        mYetki=FirebaseAuth.getInstance();
        mevcutKullanici=mYetki.getCurrentUser();
        mevcutKullaniciId=mYetki.getCurrentUser().getUid();
        kisiyolu = FirebaseDatabase.getInstance().getReference().child("kisi_ad");

        //Tanımlamalar
        //fragmant olduğu için bu şekilde yaptık tanımlamaları
        list_view_sohbet=kisiCerceveView.findViewById(R.id.list_view_sohbet);
        arrayAdapterKisi=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,kisi_listeleri);
        list_view_sohbet.setAdapter(arrayAdapterKisi);

        //Grupları alma kodları
        KisileriAlVeGoster();
        list_view_sohbet.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //position her satırın bir pozisyonu var onu temsil eder
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String mevcutKisiAdi=parent.getItemAtPosition(position).toString();

                Intent kisiChat= new Intent(getContext(),Sohbet.class);
                //GrupChatten çekeçeğiz
                kisiChat.putExtra("kisiAdi",mevcutKisiAdi);
                startActivity(kisiChat);
            }
        });

        return kisiCerceveView;
    }



    private void KisileriAlVeGoster() {

        //veri yazarken OnCompletLİsterner kullanıryoruz, veri çekerken de genellikle addValueEventListener()


        kisiyolu.addValueEventListener(new ValueEventListener() {

            //DataSnapShot veri tabanındaki bilgileri temsil eder
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Set<String> set= new HashSet<>();
                Iterator iterator2=dataSnapshot.getChildren().iterator();

                //diğerine geçiyorken:
                while(iterator2.hasNext()){

                    set.add(((DataSnapshot)iterator2.next()).getKey());

                }

                //clear yapmazsak veriler üst üste biner
                kisi_listeleri.clear();
                //Hashseti hepsine ekler
                kisi_listeleri.addAll(set);
                //eşzamanlı yenileme için
                arrayAdapterKisi.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseerror) {

            }
        });

    }


}

