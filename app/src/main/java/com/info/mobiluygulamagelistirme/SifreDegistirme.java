package com.info.mobiluygulamagelistirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class SifreDegistirme extends AppCompatActivity {

    private FirebaseAuth auth;
    private FloatingActionButton yeniParolaGönder;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_degistirme);



        yeniParolaGönder=findViewById(R.id.btn_sifre_gonder);
        email=findViewById(R.id.sifre_gonder_mail);

        //FirebaseAuth sınıfının referans olduğu nesneleri kullanabilmek için getInstance methodunu kullanıyoruz.
        auth = FirebaseAuth.getInstance();

                yeniParolaGönder.setOnClickListener(new View.OnClickListener() {
                 @Override
                     public void onClick(View v) {

                     String mail = email.getText().toString().trim();

                     if (TextUtils.isEmpty(mail)) {
                         Toast.makeText(getApplication(), "Lütfen email adresinizi giriniz", Toast.LENGTH_SHORT).show();
                         return;
                     }



                     auth.sendPasswordResetEmail(mail)
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if (task.isSuccessful()) {
                                         Toast.makeText(SifreDegistirme.this, "Yeni parola için gerekli bağlantı adresinize gönderildi!", Toast.LENGTH_SHORT).show();
                                     } else {
                                         Toast.makeText(SifreDegistirme.this, "Mail gönderme hatası!", Toast.LENGTH_SHORT).show();
                                     }


                                 }
                             });
                 }
                });
    }

}

