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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kelompok2.uasmobpro.Adapter.AdapterDaftarResepKecil;
import com.kelompok2.uasmobpro.Model.ModelDataResep;

import java.util.ArrayList;
import java.util.List;


public class ResepHarian extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference resepRef = db.collection("resep");
    CollectionReference usersRef = db.collection("users");
    DocumentReference docUserRef;
    List<ModelDataResep> resepList1 = new ArrayList<>();
    List<ModelDataResep> resepList2 = new ArrayList<>();
    List<ModelDataResep> resepList3 = new ArrayList<>();
    List<ModelDataResep> resepList4 = new ArrayList<>();
    List<ModelDataResep> resepList5 = new ArrayList<>();
    List<ModelDataResep> resepList6 = new ArrayList<>();
    List<ModelDataResep> resepList7 = new ArrayList<>();
    RecyclerView recyclerView1, recyclerView2, recyclerView3, recyclerView4, recyclerView5, recyclerView6, recyclerView7;
    LinearLayoutManager layoutManager1;
    LinearLayoutManager layoutManager2;
    LinearLayoutManager layoutManager3;
    LinearLayoutManager layoutManager4;
    LinearLayoutManager layoutManager5;
    LinearLayoutManager layoutManager6;
    LinearLayoutManager layoutManager7;
    AdapterDaftarResepKecil adapter1, adapter2, adapter3, adapter4, adapter5, adapter6, adapter7;
    CardView cvHolder1, cvHolder2, cvHolder3, cvHolder4, cvHolder5, cvHolder6, cvHolder7;
    ImageView btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resep_harian, container, false);
        Bundle bundle = getArguments();
        String id = bundle.getString("id");
        docUserRef = usersRef.document(id);

        btnLogout = view.findViewById(R.id.imageView15);
        layoutManager1 = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);;
        layoutManager2 = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);;
        layoutManager3 = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);;
        layoutManager4 = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);;
        layoutManager5 = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);;
        layoutManager6 = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);;
        layoutManager7 = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);;

        recyclerView1 = view.findViewById(R.id.recyclerView3);
        recyclerView2 = view.findViewById(R.id.recyclerView4);
        recyclerView3 = view.findViewById(R.id.recyclerView5);
        recyclerView4 = view.findViewById(R.id.recyclerView6);
        recyclerView5 = view.findViewById(R.id.recyclerView7);
        recyclerView6 = view.findViewById(R.id.recyclerView8);
        recyclerView7 = view.findViewById(R.id.recyclerView9);

        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView4.setLayoutManager(layoutManager4);
        recyclerView5.setLayoutManager(layoutManager5);
        recyclerView6.setLayoutManager(layoutManager6);
        recyclerView7.setLayoutManager(layoutManager7);

        cvHolder1 = view.findViewById(R.id.holder3);
        cvHolder2 = view.findViewById(R.id.holder4);
        cvHolder3 = view.findViewById(R.id.holder5);
        cvHolder4 = view.findViewById(R.id.holder6);
        cvHolder5 = view.findViewById(R.id.holder7);
        cvHolder6 = view.findViewById(R.id.holder8);
        cvHolder7 = view.findViewById(R.id.holder9);

        adapter1 = new AdapterDaftarResepKecil(resepList1);
        adapter2 = new AdapterDaftarResepKecil(resepList2);
        adapter3 = new AdapterDaftarResepKecil(resepList3);
        adapter4 = new AdapterDaftarResepKecil(resepList4);
        adapter5 = new AdapterDaftarResepKecil(resepList5);
        adapter6 = new AdapterDaftarResepKecil(resepList6);
        adapter7 = new AdapterDaftarResepKecil(resepList7);

        // senin
        docUserRef
                .get()
                .addOnCompleteListener(taskUser -> {
                    if (taskUser.isSuccessful()) {
                        DocumentSnapshot documentUser = taskUser.getResult();
                        if (documentUser.exists()) {
                                List<String> idResepList = (List<String>) documentUser.get("senin");

                                if(!idResepList.isEmpty()){
                                    db.collection("resep")
                                            .whereIn("id", idResepList)
                                            .get()
                                            .addOnCompleteListener(taskResep -> {
                                                if (taskResep.isSuccessful()) {
                                                    for (QueryDocumentSnapshot documentResep : taskResep.getResult()) {
                                                        ModelDataResep resep = documentResep.toObject(ModelDataResep.class);
                                                        resepList1.add(resep);
                                                    }
                                                    setResepSenin();
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
        // selasa
        docUserRef
                .get()
                .addOnCompleteListener(taskUser -> {
                    if (taskUser.isSuccessful()) {
                        DocumentSnapshot documentUser = taskUser.getResult();
                        if (documentUser.exists()) {
                            List<String> idResepList = (List<String>) documentUser.get("selasa");

                            if(!idResepList.isEmpty()){
                                db.collection("resep")
                                        .whereIn("id", idResepList)
                                        .get()
                                        .addOnCompleteListener(taskResep -> {
                                            if (taskResep.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentResep : taskResep.getResult()) {
                                                    ModelDataResep resep = documentResep.toObject(ModelDataResep.class);
                                                    resepList2.add(resep);
                                                }
                                                setResepSelasa();
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
        // rabu
        docUserRef
                .get()
                .addOnCompleteListener(taskUser -> {
                    if (taskUser.isSuccessful()) {
                        DocumentSnapshot documentUser = taskUser.getResult();
                        if (documentUser.exists()) {
                            List<String> idResepList = (List<String>) documentUser.get("rabu");

                            if(!idResepList.isEmpty()){
                                db.collection("resep")
                                        .whereIn("id", idResepList)
                                        .get()
                                        .addOnCompleteListener(taskResep -> {
                                            if (taskResep.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentResep : taskResep.getResult()) {
                                                    ModelDataResep resep = documentResep.toObject(ModelDataResep.class);
                                                    resepList3.add(resep);
                                                }
                                                setResepRabu();
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
        // kamis
        docUserRef
                .get()
                .addOnCompleteListener(taskUser -> {
                    if (taskUser.isSuccessful()) {
                        DocumentSnapshot documentUser = taskUser.getResult();
                        if (documentUser.exists()) {
                            List<String> idResepList = (List<String>) documentUser.get("kamis");

                            if(!idResepList.isEmpty()){
                                db.collection("resep")
                                        .whereIn("id", idResepList)
                                        .get()
                                        .addOnCompleteListener(taskResep -> {
                                            if (taskResep.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentResep : taskResep.getResult()) {
                                                    ModelDataResep resep = documentResep.toObject(ModelDataResep.class);
                                                    resepList4.add(resep);
                                                }
                                                setResepKamis();
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
        // jumat
        docUserRef
                .get()
                .addOnCompleteListener(taskUser -> {
                    if (taskUser.isSuccessful()) {
                        DocumentSnapshot documentUser = taskUser.getResult();
                        if (documentUser.exists()) {
                            List<String> idResepList = (List<String>) documentUser.get("jumat");

                            if(!idResepList.isEmpty()){
                                db.collection("resep")
                                        .whereIn("id", idResepList)
                                        .get()
                                        .addOnCompleteListener(taskResep -> {
                                            if (taskResep.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentResep : taskResep.getResult()) {
                                                    ModelDataResep resep = documentResep.toObject(ModelDataResep.class);
                                                    resepList5.add(resep);
                                                }
                                                setResepJumat();
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
        // sabtu
        docUserRef
                .get()
                .addOnCompleteListener(taskUser -> {
                    if (taskUser.isSuccessful()) {
                        DocumentSnapshot documentUser = taskUser.getResult();
                        if (documentUser.exists()) {
                            List<String> idResepList = (List<String>) documentUser.get("sabtu");

                            if(!idResepList.isEmpty()){
                                db.collection("resep")
                                        .whereIn("id", idResepList)
                                        .get()
                                        .addOnCompleteListener(taskResep -> {
                                            if (taskResep.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentResep : taskResep.getResult()) {
                                                    ModelDataResep resep = documentResep.toObject(ModelDataResep.class);
                                                    resepList6.add(resep);
                                                }
                                                setResepSabtu();
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
        // minggu
        docUserRef
                .get()
                .addOnCompleteListener(taskUser -> {
                    if (taskUser.isSuccessful()) {
                        DocumentSnapshot documentUser = taskUser.getResult();
                        if (documentUser.exists()) {
                            List<String> idResepList = (List<String>) documentUser.get("minggu");

                            if(!idResepList.isEmpty()){
                                db.collection("resep")
                                        .whereIn("id", idResepList)
                                        .get()
                                        .addOnCompleteListener(taskResep -> {
                                            if (taskResep.isSuccessful()) {
                                                for (QueryDocumentSnapshot documentResep : taskResep.getResult()) {
                                                    ModelDataResep resep = documentResep.toObject(ModelDataResep.class);
                                                    resepList7.add(resep);
                                                }
                                                setResepMinggu();
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

        adapter1.setOnItemClickListener(new AdapterDaftarResepKecil.OnItemClickListener(){
            @Override
            public void onItemClick(int position){
                Intent intent = new Intent(requireContext(), DetailResep.class);
                intent.putExtra("id resep", resepList1.get(position).getId());
                intent.putExtra("id user", id);
                startActivity(intent);
            }
        });
        adapter2.setOnItemClickListener(new AdapterDaftarResepKecil.OnItemClickListener(){
            @Override
            public void onItemClick(int position){
                Intent intent = new Intent(requireContext(), DetailResep.class);
                intent.putExtra("id resep", resepList2.get(position).getId());
                intent.putExtra("id user", id);
                startActivity(intent);
            }
        });
        adapter3.setOnItemClickListener(new AdapterDaftarResepKecil.OnItemClickListener(){
            @Override
            public void onItemClick(int position){
                Intent intent = new Intent(requireContext(), DetailResep.class);
                intent.putExtra("id resep", resepList3.get(position).getId());
                intent.putExtra("id user", id);
                startActivity(intent);
            }
        });
        adapter4.setOnItemClickListener(new AdapterDaftarResepKecil.OnItemClickListener(){
            @Override
            public void onItemClick(int position){
                Intent intent = new Intent(requireContext(), DetailResep.class);
                intent.putExtra("id resep", resepList4.get(position).getId());
                intent.putExtra("id user", id);
                startActivity(intent);
            }
        });

        adapter5.setOnItemClickListener(new AdapterDaftarResepKecil.OnItemClickListener(){
            @Override
            public void onItemClick(int position){
                Intent intent = new Intent(requireContext(), DetailResep.class);
                intent.putExtra("id resep", resepList5.get(position).getId());
                intent.putExtra("id user", id);
                startActivity(intent);
            }
        });
        adapter6.setOnItemClickListener(new AdapterDaftarResepKecil.OnItemClickListener(){
            @Override
            public void onItemClick(int position){
                Intent intent = new Intent(requireContext(), DetailResep.class);
                intent.putExtra("id resep", resepList6.get(position).getId());
                intent.putExtra("id user", id);
                startActivity(intent);
            }
        });
        adapter7.setOnItemClickListener(new AdapterDaftarResepKecil.OnItemClickListener(){
            @Override
            public void onItemClick(int position){
                Intent intent = new Intent(requireContext(), DetailResep.class);
                intent.putExtra("id resep", resepList7.get(position).getId());
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

    public void setResepSenin(){
        if(!resepList1.isEmpty()){
            recyclerView1.setVisibility(View.VISIBLE);
            cvHolder1.setVisibility(View.GONE);
            recyclerView1.setAdapter(adapter1);
        }else {
            recyclerView1.setVisibility(View.GONE);
            cvHolder1.setVisibility(View.VISIBLE);
        }
    }
    public void setResepSelasa(){
        if(!resepList2.isEmpty()){
            recyclerView2.setVisibility(View.VISIBLE);
            cvHolder2.setVisibility(View.GONE);
            recyclerView2.setAdapter(adapter2);
        }else {
            recyclerView2.setVisibility(View.GONE);
            cvHolder2.setVisibility(View.VISIBLE);
        }
    }
    public void setResepRabu(){
        if(!resepList3.isEmpty()){
            recyclerView3.setVisibility(View.VISIBLE);
            cvHolder3.setVisibility(View.GONE);
            recyclerView3.setAdapter(adapter3);
        }else {
            recyclerView3.setVisibility(View.GONE);
            cvHolder3.setVisibility(View.VISIBLE);
        }
    }
    public void setResepKamis(){
        if(!resepList4.isEmpty()){
            recyclerView4.setVisibility(View.VISIBLE);
            cvHolder4.setVisibility(View.GONE);
            recyclerView4.setAdapter(adapter4);
        }else {
            recyclerView4.setVisibility(View.GONE);
            cvHolder4.setVisibility(View.VISIBLE);
        }
    }
    public void setResepJumat(){
        if(resepList5.isEmpty()){
            recyclerView5.setVisibility(View.VISIBLE);
            cvHolder5.setVisibility(View.GONE);
            recyclerView5.setAdapter(adapter5);
        }else {
            recyclerView5.setVisibility(View.GONE);
            cvHolder5.setVisibility(View.VISIBLE);
        }
    }
    public void setResepSabtu(){
        if(!resepList6.isEmpty()){
            recyclerView6.setVisibility(View.VISIBLE);
            cvHolder6.setVisibility(View.GONE);
            recyclerView6.setAdapter(adapter6);
        }else {
            recyclerView6.setVisibility(View.GONE);
            cvHolder6.setVisibility(View.VISIBLE);
        }
    }
    public void setResepMinggu(){
        if(!resepList7.isEmpty()){
            recyclerView7.setVisibility(View.VISIBLE);
            cvHolder7.setVisibility(View.GONE);
            recyclerView7.setAdapter(adapter7);
        }else {
            recyclerView7.setVisibility(View.GONE);
            cvHolder7.setVisibility(View.VISIBLE);
        }
    }
}