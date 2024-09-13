package com.kelompok2.uasmobpro.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kelompok2.uasmobpro.Beranda;
import com.kelompok2.uasmobpro.Model.ModelDataResep;
import com.kelompok2.uasmobpro.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterDaftarResepBesar extends RecyclerView.Adapter<AdapterDaftarResepBesar.MyViewHolder> {
    private OnItemClickListener listener;
    String userID;
    List<ModelDataResep> resepList;
    StorageReference storageReference;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public AdapterDaftarResepBesar(List<ModelDataResep> resepList, String userID){
        this.resepList = resepList;
        this.userID = userID;
    }
    @NonNull
    @Override
    public AdapterDaftarResepBesar.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout (Giving look to our rows)
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.resep_item_besar, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDaftarResepBesar.MyViewHolder holder, int position) {
        holder.namaResep.setText(resepList.get(position).getNama());

        storageReference = FirebaseStorage.getInstance().getReference("images/"+resepList.get(position).getFoto()+".jpg");
        try{
            File localFile = File.createTempFile("tempfile", ".jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            holder.ivGambarResep.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Gagal Mengambil Foto");
                        }
                    });
        }catch (IOException e){
            e.printStackTrace();
        }
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    listener.onItemClick(position);
                }
            }
        });
        holder.ivBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                int position = holder.getAdapterPosition();
                String idResepYangInginDihapus = resepList.get(position).getId();

                db.collection("users")
                        .document(userID)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    List<String> favorit = (List<String>) document.get("favorit");

                                    // Hapus id resep dari array favorit
                                    favorit.remove(idResepYangInginDihapus);

                                    // Update dokumen
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("favorit", favorit);

                                    db.collection("users")
                                            .document(userID)
                                            .update(updates)
                                            .addOnSuccessListener(aVoid -> {
                                                Log.d("Firestore", "Dokumen berhasil diperbarui");
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.w("Firestore", "Error updating document", e);
                                            });
                                } else {
                                    // Dokumen pengguna tidak ditemukan
                                }
                            } else {
                                // Handle error
                            }
                        });

                holder.ivBookmark.setImageResource(R.drawable.ic_favorit_black);
            }
        });
    }

    @Override
    public int getItemCount() {
        // total item that we want to displayed
        return resepList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // grabbing views from the rvItem layout, its like onCreateMethod for it

        TextView namaResep;
        ImageView ivGambarResep, ivBookmark;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            namaResep = itemView.findViewById(R.id.textView23);
            ivGambarResep = itemView.findViewById(R.id.imageView16);
            ivBookmark = itemView.findViewById(R.id.imageView17);
        }
    }



}
