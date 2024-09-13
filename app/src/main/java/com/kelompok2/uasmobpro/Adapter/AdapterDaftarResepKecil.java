package com.kelompok2.uasmobpro.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kelompok2.uasmobpro.Interface.RecyclerViewInterface;
import com.kelompok2.uasmobpro.Model.ModelDataResep;
import com.kelompok2.uasmobpro.R;

import java.util.List;

public class AdapterDaftarResepKecil extends RecyclerView.Adapter<AdapterDaftarResepKecil.MyViewHolder> {
    private OnItemClickListener listener;
    List<ModelDataResep> resepList;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSearchList(List<ModelDataResep> searchDatas){
        this.resepList = searchDatas;
        notifyDataSetChanged();
    }

    public AdapterDaftarResepKecil(List<ModelDataResep> resepList){
        this.resepList = resepList;
    }
    @NonNull
    @Override
    public AdapterDaftarResepKecil.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout (Giving look to our rows)
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.resep_item_kecil, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDaftarResepKecil.MyViewHolder holder, int position) {
        holder.namaResep.setText(resepList.get(position).getNama());
        if (resepList.get(position).getJenis().equals("Makanan")) {
                holder.gambarJenis.setImageResource(R.drawable.makanan);
            } else {
                holder.gambarJenis.setImageResource(R.drawable.minuman);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = holder.getAdapterPosition();
                        listener.onItemClick(position);
                    }
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
        ImageView gambarJenis;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            namaResep = itemView.findViewById(R.id.textView18);
            gambarJenis = itemView.findViewById(R.id.imageView3);
        }
    }
}
