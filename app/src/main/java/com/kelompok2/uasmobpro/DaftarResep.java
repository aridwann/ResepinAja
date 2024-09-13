package com.kelompok2.uasmobpro;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.kelompok2.uasmobpro.Adapter.AdapterDaftarResepKecil;
import com.kelompok2.uasmobpro.Interface.RecyclerViewInterface;
import com.kelompok2.uasmobpro.Model.ModelDataResep;

import java.util.ArrayList;
import java.util.List;

public class DaftarResep extends Fragment {
    TextView tvJenis;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference resepRef = db.collection("resep");

    List<ModelDataResep> resepList = new ArrayList<>();
    List<ModelDataResep> searchResults;
    SearchView svSearch;
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    AdapterDaftarResepKecil adapter;
    ImageView btnLogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daftar_resep, container, false);
        Bundle bundle = getArguments();
        String id = bundle.getString("id");

        btnLogout = view.findViewById(R.id.imageView13);
        tvJenis = view.findViewById(R.id.textView4);
        svSearch = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recyclerView1);
        layoutManager = new GridLayoutManager(requireContext(), 3);
        adapter = new AdapterDaftarResepKecil(resepList);

        tvJenis.setText(getBundle());
        recyclerView.setLayoutManager(layoutManager);

        svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            // Search on text changes
            @Override
            public boolean onQueryTextChange(String newText) {
                List<ModelDataResep> dataSearchList = new ArrayList<>();
                for (ModelDataResep data : resepList){
                    if (data.getNama().toLowerCase().contains(newText.toLowerCase())) {
                        dataSearchList.add(data);
                    }
                }
                if (dataSearchList.isEmpty()){
                    Toast.makeText(requireContext(), "Not Found", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.setSearchList(dataSearchList);
                    searchResults = dataSearchList;
                }
                return false;
            }
        });

        // ambil data resep dan tampilkan
        resepRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ModelDataResep resep = document.toObject(ModelDataResep.class);
                        if(resep.getJenis().equals(getBundle())){
                            resepList.add(resep);
                        }
                    }
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });

        adapter.setOnItemClickListener(new AdapterDaftarResepKecil.OnItemClickListener(){
            @Override
            public void onItemClick(int position){
                Intent intent = new Intent(requireContext(), DetailResep.class);
                List<ModelDataResep> currentList = searchResults != null && !searchResults.isEmpty() ? searchResults : resepList;
                intent.putExtra("id resep", currentList.get(position).getId());
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

    public String getBundle(){
        Bundle bundle = getArguments();
        if(bundle != null){
            return bundle.getString("jenis");
        }
        return "Bundle Kosong";
    }

}

