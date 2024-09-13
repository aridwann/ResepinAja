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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kelompok2.uasmobpro.Model.ModelDataResep;
import com.kelompok2.uasmobpro.Model.ModelUsers;

import java.util.List;

public class Masuk extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");
    EditText etNamaPengguna, etPassword;
    TextView tvDaftar;
    Button btnMasuk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_masuk);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvDaftar = findViewById(R.id.textView30);
        btnMasuk = findViewById(R.id.buttonMasuk);
        etNamaPengguna = findViewById(R.id.editTextText);
        etPassword = findViewById(R.id.editTextTextPassword);

        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Masuk.this, Daftar.class);
                startActivity(intent);
            }
        });

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namaPengguna = etNamaPengguna.getText().toString();
                String password = etPassword.getText().toString();

                usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ModelUsers user = document.toObject(ModelUsers.class);
                                if (user.getNamapengguna().equals(namaPengguna) && user.getPassword().equals(password)) {
                                    Toast.makeText(Masuk.this, "Selamat Datang " + user.getNamapengguna(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Masuk.this, MenuActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", user.getId());
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    break;
                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
            }
        });
    }
}