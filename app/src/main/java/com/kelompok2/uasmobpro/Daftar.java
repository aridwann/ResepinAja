package com.kelompok2.uasmobpro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kelompok2.uasmobpro.Model.ModelUsers;

public class Daftar extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");

    TextView tvMasuk;
    Button btnDaftar;
    EditText etNamaPengguna, etPassword, etPasswordUlang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daftar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvMasuk = findViewById(R.id.textView36);
        btnDaftar = findViewById(R.id.buttonDaftar);
        etNamaPengguna = findViewById(R.id.editTextText2);
        etPassword = findViewById(R.id.editTextTextPassword3);
        etPasswordUlang = findViewById(R.id.editTextTextPassword2);

        tvMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Daftar.this, Masuk.class);
                startActivity(intent);
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaPengguna = etNamaPengguna.getText().toString();
                String password = etPassword.getText().toString();
                String passwordUlang = etPasswordUlang.getText().toString();

                if(!namaPengguna.equals("") && !password.equals("") && password.equals(passwordUlang)){
                    ModelUsers user = new ModelUsers();
                    user.setNamapengguna(namaPengguna);
                    user.setPassword(passwordUlang);
                    usersRef.add(user)
                            .addOnSuccessListener(documentReference -> {
                                    Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
                                    // Simpan
                                    user.setId(documentReference.getId());

                                    // Update dokumen dengan ID yang baru didapatkan
                                    documentReference.update("id", user.getId())
                                            .addOnSuccessListener(aVoid -> {
                                                Toast.makeText(Daftar.this, "Akun berhasil didaftarkan", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Daftar.this, Masuk.class);
                                                startActivity(intent);
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.w("TAG", "Error updating document", e);
                                            });
                                })
                                        .addOnFailureListener(e -> {Log.w("TAG", "Error adding document", e);
                                });
                }else if(namaPengguna.equals("")){
                    Toast.makeText(Daftar.this, "Nama Pengguna Kosong", Toast.LENGTH_SHORT).show();
                }else if(password.equals("")){
                    Toast.makeText(Daftar.this, "Password Kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}