package com.kelompok2.uasmobpro.Model;

import java.util.List;

public class ModelDataResep {
    private String id, nama, jenis, foto;
    private List<String> bahan, langkah;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<String> getBahan() {
        return bahan;
    }

    public void setBahan(List<String> bahan) {
        this.bahan = bahan;
    }

    public List<String> getLangkah() {
        return langkah;
    }

    public void setLangkah(List<String> langkah) {
        this.langkah = langkah;
    }
}
