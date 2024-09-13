package com.kelompok2.uasmobpro;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kelompok2.uasmobpro.Adapter.AdapterDaftarResepBesar;
import com.kelompok2.uasmobpro.Adapter.AdapterDaftarResepKecil;
import com.kelompok2.uasmobpro.Model.ModelDataResep;
import com.kelompok2.uasmobpro.Model.ModelUsers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Beranda extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference resepRef = db.collection("resep");
    CollectionReference usersRef = db.collection("users");
    DocumentReference docUserRef;
    List<ModelDataResep> resepList1 = new ArrayList<>();
    List<ModelDataResep> resepList2 = new ArrayList<>();
    RecyclerView recyclerView1, recyclerView2;
    LinearLayoutManager layoutManager1;
    LinearLayoutManager layoutManager2;
    AdapterDaftarResepKecil adapter1;
    AdapterDaftarResepBesar adapter2;
    CardView cvHolder1, cvHolder2;
    ImageView btnLogout;

    TextView tvTanggal, tvNama;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        // mendapatkan data dari activity
        Bundle bundle = getArguments();
        String id = bundle.getString("id");
        docUserRef = usersRef.document(id);

        tvNama = view.findViewById(R.id.textView22);
        tvTanggal = view.findViewById(R.id.textView5);
        btnLogout = view.findViewById(R.id.imageView10);

        recyclerView1 = view.findViewById(R.id.recyclerView);
        layoutManager1 = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);

        recyclerView2 = view.findViewById(R.id.recyclerView3);
        layoutManager2 = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        cvHolder1 = view.findViewById(R.id.holder1);
        cvHolder2 = view.findViewById(R.id.holder2);

        adapter1 = new AdapterDaftarResepKecil(resepList1);
        adapter2 = new AdapterDaftarResepBesar(resepList2, id);

        tvTanggal.setText(getTanggal());

        // mendapatkan data dan menampilkan resep harian
        docUserRef
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String> idResepList = (List<String>) document.get(getHari().toLowerCase());

                        if(!idResepList.isEmpty()){
                            resepRef
                                    .whereIn("id", idResepList)
                                    .get()
                                    .addOnCompleteListener(taskResep -> {
                                        if (taskResep.isSuccessful()) {
                                            resepList1.clear();
                                            for (QueryDocumentSnapshot documentResep : taskResep.getResult()) {
                                                ModelDataResep resep = documentResep.toObject(ModelDataResep.class);
                                                resepList1.add(resep);
                                                setResepHariIni(resepList1);
                                            }
                                        } else {
                                            // Handle error
                                        }
                                    });
                        }
                    } else {
                        // Pengguna tidak ditemukan
                    }
                } else {
                    // Handle error
                }
            });

        // mendapatkan data dan menampilkan resep favorit
        docUserRef
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<String>
                        favorit = (List<String>) document.get("favorit");
                        tvNama.setText(document.get("namapengguna").toString());
                        if(!favorit.isEmpty()){
                            resepRef
                                    .whereIn("id", favorit)
                                    .get()
                                    .addOnCompleteListener(taskResep -> {
                                        if (taskResep.isSuccessful()) {
                                            resepList2.clear();
                                            for (QueryDocumentSnapshot documentResep : taskResep.getResult()) {
                                                ModelDataResep resep = documentResep.toObject(ModelDataResep.class);
                                                resepList2.add(resep);
                                                setResepFavorit(resepList2);
                                            }
                                        } else {
                                            // Handle error
                                        }
                                    });
                        }
                    } else {
                        // Dokumen pengguna tidak ditemukan
                    }
                } else {
                    // Handle error
                }
            });

        adapter1.setOnItemClickListener(new AdapterDaftarResepKecil.OnItemClickListener(){
            @Override
            public void onItemClick(int position){
                ModelDataResep selectedResep = resepList1.get(position);
                Intent intent = new Intent(requireContext(), DetailResep.class);
                intent.putExtra("id resep", selectedResep.getId());
                intent.putExtra("id user", id);
                startActivity(intent);
            }
        });
        adapter2.setOnItemClickListener(new AdapterDaftarResepBesar.OnItemClickListener(){
            @Override
            public void onItemClick(int position){
                Intent intent = new Intent(requireContext(), DetailResep.class);
                intent.putExtra("id resep", resepList2.get(position).getId());
                intent.putExtra("id user", id);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), Masuk.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public String getHari(){
        Calendar calendar = Calendar.getInstance();
        Locale indonesiaLocale = new Locale("id", "ID");

        SimpleDateFormat hariFormat = new SimpleDateFormat("EEEE", indonesiaLocale);
        return hariFormat.format(calendar.getTime());
    }

    public String getTanggal(){
        Calendar calendar = Calendar.getInstance();
        Locale indonesiaLocale = new Locale("id", "ID");

        SimpleDateFormat tanggalFormat = new SimpleDateFormat("dd", indonesiaLocale);
        String tanggal = tanggalFormat.format(calendar.getTime());

        SimpleDateFormat bulanFormat = new SimpleDateFormat("MMMM", indonesiaLocale);
        String bulan = bulanFormat.format(calendar.getTime());

        SimpleDateFormat tahunFormat = new SimpleDateFormat("yyyy", indonesiaLocale);
        String tahun = tahunFormat.format(calendar.getTime());

        return getHari() + ", " + tanggal + " " + bulan + " " + tahun;
    }

    public void setResepHariIni(List<ModelDataResep> resepList1){
        if(!resepList1.isEmpty()){
            recyclerView1.setVisibility(View.VISIBLE);
            cvHolder1.setVisibility(View.GONE);
            recyclerView1.setAdapter(adapter1);
        }else {
            recyclerView1.setVisibility(View.GONE);
            cvHolder1.setVisibility(View.VISIBLE);
        }
    }

    public void setResepFavorit(List<ModelDataResep> resepList2){
        if(!resepList2.isEmpty()){
            recyclerView2.setVisibility(View.VISIBLE);
            cvHolder2.setVisibility(View.GONE);
            recyclerView2.setAdapter(adapter2);
        }else {
            recyclerView2.setVisibility(View.GONE);
            cvHolder2.setVisibility(View.VISIBLE);
        }
    }
}