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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GruplarFragment extends Fragment {

    private View grupCerceveView;
    private ListView list_view;
    private ArrayAdapter<String>arrayAdapter;
    private ArrayList<String>grup_listeleri= new ArrayList<>();
    


    //Firebase
    private DatabaseReference grupyolu;



  public GruplarFragment(){

  }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        grupCerceveView= inflater.inflate(R.layout.fragment_gruplar, container, false);

        //Firebase Tanımlama
        grupyolu = FirebaseDatabase.getInstance().getReference().child("Gruplar");

        //Tanımlamalar
        //fragmant olduğu için bu şekilde yaptık tanımlamaları
        list_view=grupCerceveView.findViewById(R.id.list_view);
        arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,grup_listeleri);
        list_view.setAdapter(arrayAdapter);


        //Grupları alma kodları
        GruplariAlVeGoster();
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //position her satırın bir pozisyonu var onu temsil eder
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String mevcutGrupAdi=parent.getItemAtPosition(position).toString();

                Intent grupChat= new Intent(getContext(),GrupChat.class);
                //GrupChatten çekeçeğiz
                grupChat.putExtra("grupAdi",mevcutGrupAdi);
                startActivity(grupChat);
            }
        });

        return grupCerceveView;

    }

    private void GruplariAlVeGoster() {

      //veri yazarken OnCompletLİsterner kullanıyoruz, veri çekerken de genellikle addValueEventListener()


        grupyolu.addValueEventListener(new ValueEventListener() {

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
                grup_listeleri.clear();
                //Hashseti hepsine ekler
                grup_listeleri.addAll(set);
                //eşzamanlı yenileme için
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseerror) {

            }
        });

    }
}