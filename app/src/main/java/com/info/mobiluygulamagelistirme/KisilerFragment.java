package com.info.mobiluygulamagelistirme;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class KisilerFragment extends Fragment {

    private View kisiCerceveView;
    private ListView list_view;
    private ArrayAdapter<String>arrayAdapter;
    private ArrayList<String>kisi_listeleri= new ArrayList<>();



    //Firebase
    private DatabaseReference kisiYolu;
    private FirebaseUser mevcutKullanici;
    private FirebaseAuth mYetki;

    private String mevcutKullaniciId;

    private FirebaseStorage m;

    public KisilerFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        kisiCerceveView= inflater.inflate(R.layout.fragment_kisiler, container, false);


        mYetki=FirebaseAuth.getInstance();
        mevcutKullanici=mYetki.getCurrentUser();
        mevcutKullaniciId=mYetki.getCurrentUser().getUid();


        //Firebase Tanımlama
        kisiYolu = FirebaseDatabase.getInstance().getReference().child("kisi_ad");



        //Tanımlamalar
        //fragmant olduğu için bu şekilde yaptık tanımlamaları
        list_view=kisiCerceveView.findViewById(R.id.list_view);
        arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,kisi_listeleri);
        list_view.setAdapter(arrayAdapter);


        //Grupları alma kodları
        GruplariAlVeGoster();
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //position her satırın bir pozisyonu var onu temsil eder
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return kisiCerceveView;
    }

    private void GruplariAlVeGoster() {

        //veri yazarken OnCompletLİsterner kullanıryoruz, veri çekerken de genellikle addValueEventListener()


        kisiYolu.addValueEventListener(new ValueEventListener() {

            //DataSnapShot veri tabanındaki bilgileri temsil eder
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                Set<String> set= new HashSet<>();
                Iterator iterator=datasnapshot.getChildren().iterator();

                //diğerine geçiyorken:
                while(iterator.hasNext()){

                    set.add(((DataSnapshot)iterator.next()).getKey());

                }

                //clear yapmazsak veriler üst üste biner
                kisi_listeleri.clear();
                //Hashseti hepsine ekler
                kisi_listeleri.addAll(set);
                //eşzamanlı yenileme için
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseerror) {

            }
        });

    }

}