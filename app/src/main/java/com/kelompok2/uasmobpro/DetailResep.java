package com.kelompok2.uasmobpro;

import static android.content.ContentValues.TAG;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kelompok2.uasmobpro.Model.ModelDataResep;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailResep extends AppCompatActivity {
    private TextView tvNamaResep, tvBahan, tvLangkah;
    private ImageView ivGambarResep, ivFavorit;
    private Spinner sPilihHari;
    StorageReference storageReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    DocumentReference docUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_resep);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvNamaResep = findViewById(R.id.textView15);
        tvBahan = findViewById(R.id.textView16);
        tvLangkah = findViewById(R.id.textView17);
        ivGambarResep = findViewById(R.id.imageView);
        ivFavorit = findViewById(R.id.imageView2);
        sPilihHari = findViewById(R.id.spinner);

        // mendapatkan data dari activity/fragment sebelumnya
        Bundle bundle = getIntent().getExtras();
        String idResep = bundle.getString("id resep");
        String idUser = bundle.getString("id user");

        // spinner
        String[] items = {"~Pilih Hari~", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sPilihHari.setAdapter(adapter);

        // referensi ke db collection
        docRef = db.collection("resep").document(idResep);
        docUserRef = db.collection("users").document(idUser);

        docUserRef
        .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> favorit = (List<String>) document.get("favorit");
                        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Log.w("Firestore", "Listen failed.", error);
                                    return;
                                }

                                if (snapshot != null && snapshot.exists()) {
                                    ModelDataResep resep = snapshot.toObject(ModelDataResep.class);
                                    boolean isFav = false;
                                    if(favorit != null && !favorit.isEmpty()){
                                        for (String idresep : favorit) {
                                            if(idresep.equals(resep.getId())){
                                                isFav = true;
                                            }
                                        }
                                    }
                                    // mapping data ke layout
                                    setFoto(resep.getFoto());
                                    tvNamaResep.setText(resep.getNama());
                                    setBahan(resep.getBahan());
                                    setLangkah(resep.getLangkah());
                                    setBookmark(isFav);

                                    String[] daftarHari = {"senin", "selasa", "rabu", "kamis", "jumat", "sabtu", "minggu"};
                                    db.collection("users")
                                            .get()
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    boolean resepDitemukan = false;
                                                    for (QueryDocumentSnapshot document : task.getResult())                                                         {
                                                        for (String hari : daftarHari) {
                                                            List<String> idResepList = (List<String>) document.get(hari);
                                                            if (idResepList != null && idResepList.contains(resep.getId())) {
                                                                setSpinner(items, capitalizeFirstLetter(hari));
                                                                resepDitemukan = true;
                                                                break;
                                                            }
                                                        }
                                                        if (resepDitemukan) {
                                                            break;
                                                        }
                                                    }
                                                    if (!resepDitemukan) {
                                                        setSpinner(items, items[0]);
                                                    }
                                                } else {
                                                    // Handle error
                                                }
                                            });

                                    // handle select pada spinner
                                    sPilihHari.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            String selectedValue = adapterView.getItemAtPosition(i).toString().toLowerCase();
                                            Map<String, Object> data = new HashMap<>();
                                            docUserRef.get().addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        data.putAll(document.getData());
                                                    }
                                                }
                                                for (String hari : daftarHari) {
                                                    List<String> idResepList = (List<String>) document.get(hari);
                                                    if (idResepList != null && idResepList.contains(resep.getId())) {
                                                        idResepList.remove(resep.getId());
                                                        data.put(hari, idResepList);
                                                    }
                                                }

                                                List<String> idResepList = (List<String>) data.getOrDefault(selectedValue, new ArrayList<>());
                                                if(!selectedValue.equals("~pilih hari~")){
                                                    if (!idResepList.contains(resep.getId())) {
                                                        idResepList.add(resep.getId());
                                                    }
                                                    data.put(selectedValue, idResepList);

                                                    // Tulis data yang diperbarui ke Firestore
                                                    docUserRef.set(data, SetOptions.merge())
                                                            .addOnSuccessListener(aVoid -> {
                                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.w(TAG, "Error updating document", e);
                                                            });
                                                }else{
                                                    idResepList.remove(resep.getId());

                                                    data.put(selectedValue, idResepList);

                                                    // Tulis data yang diperbarui ke Firestore
                                                    docUserRef.set(data, SetOptions.merge())
                                                            .addOnSuccessListener(aVoid -> {
                                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.w(TAG, "Error updating document", e);
                                                            });
                                                }
                                            });
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });

                                    // update favorit lewat icon bookmark
                                    boolean finalIsFav = isFav;
                                    ivFavorit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if(finalIsFav){
                                                favorit.remove(resep.getId());
                                                Map<String, Object> updates = new HashMap<>();
                                                updates.put("favorit", favorit);

                                                docUserRef
                                                        .update(updates)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Log.d("Firestore", "Dokumen berhasil diperbarui");
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Log.w("Firestore", "Error updating document", e);
                                                        });
                                            }else{
                                                favorit.add(resep.getId());

                                                // Update dokumen
                                                Map<String, Object> updates = new HashMap<>();
                                                updates.put("favorit", favorit);

                                                docUserRef
                                                        .update(updates)
                                                        .addOnSuccessListener(aVoid -> {
                                                            Log.d("Firestore", "Dokumen berhasil diperbarui");
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            Log.w("Firestore", "Error updating document", e);
                                                        });
                                            }
                                            setBookmark(!finalIsFav);
                                        }
                                    });


                                } else {
                                    Log.d("Firestore", "Current data: null");
                                }
                            }
                        });

                    } else {
                        Log.d("Favorit", "Dokumen pengguna tidak ditemukan");
                    }
                } else {
                    // Handle error
                }
            });
    }

    // set bookmark icon
    public void setBookmark(Boolean favorit){
        if(favorit){
            ivFavorit.setImageResource(R.drawable.ic_favorit_fill);
        }else{
            ivFavorit.setImageResource(R.drawable.ic_favorit);
        }
    }

    // set selected spinner
    public void setSpinner(String[] items, String itemToSelect){
        int spinnerPosition = -1;
        for (int i = 0; i < items.length; i++) {
            if (items[i].equals(itemToSelect)) {
                spinnerPosition = i;
                break;
            }
        }

        // Pilih item di Spinner
        if (spinnerPosition != -1) {
            sPilihHari.setSelection(spinnerPosition);
        }
    }

    // set foto
    public void setFoto(String foto){
        storageReference = FirebaseStorage.getInstance().getReference("images/"+foto+".jpg");
        try{
            File localFile = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ivGambarResep.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetailResep.this, "Gagal Mengambil Foto", Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // set bahan
    public void setBahan(List<String> bahan){
        StringBuilder stringBuilder1 = new StringBuilder();
        for (String bahanItem : bahan) {
            stringBuilder1.append(bahanItem);
            stringBuilder1.append("\n");
        }
        tvBahan.setText(stringBuilder1.toString());
    }

    // set langkah
    public void setLangkah(List<String> langkah){
        StringBuilder stringBuilder2 = new StringBuilder();
        for (String langkahItem : langkah) {
            stringBuilder2.append(langkahItem);
            stringBuilder2.append("\n");
        }
        tvLangkah.setText(stringBuilder2.toString());
    }

    // buat awal kata kapital
    public String capitalizeFirstLetter(String originalString) {
        // Cek apakah string kosong
        if (originalString == null || originalString.isEmpty()) {
            return originalString;
        }

        // Ambil karakter pertama, ubah menjadi uppercase, lalu gabungkan dengan sisa string
        return originalString.substring(0, 1).toUpperCase() + originalString.substring(1);
    }
}