package com.kelompok2.uasmobpro.Model;

import java.util.ArrayList;
import java.util.List;

public class ModelUsers {
    private String id, namapengguna, password;
    private List<String> favorit = new ArrayList<String>();
    private List<String> senin = new ArrayList<String>();
    private List<String> selasa = new ArrayList<String>();
    private List<String> rabu = new ArrayList<String>();
    private List<String> kamis = new ArrayList<String>();
    private List<String> jumat = new ArrayList<String>();
    private List<String> sabtu = new ArrayList<String>();
    private List<String> minggu = new ArrayList<String>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamapengguna() {
        return namapengguna;
    }

    public void setNamapengguna(String namapengguna) {
        this.namapengguna = namapengguna;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getFavorit() {
        return favorit;
    }

    public void setFavorit(List<String> favorit) {
        this.favorit = favorit;
    }

    public List<String> getSenin() {
        return senin;
    }

    public void setSenin(List<String> senin) {
        this.senin = senin;
    }

    public List<String> getSelasa() {
        return selasa;
    }

    public void setSelasa(List<String> selasa) {
        this.selasa = selasa;
    }

    public List<String> getRabu() {
        return rabu;
    }

    public void setRabu(List<String> rabu) {
        this.rabu = rabu;
    }

    public List<String> getKamis() {
        return kamis;
    }

    public void setKamis(List<String> kamis) {
        this.kamis = kamis;
    }

    public List<String> getJumat() {
        return jumat;
    }

    public void setJumat(List<String> jumat) {
        this.jumat = jumat;
    }

    public List<String> getSabtu() {
        return sabtu;
    }

    public void setSabtu(List<String> sabtu) {
        this.sabtu = sabtu;
    }

    public List<String> getMinggu() {
        return minggu;
    }

    public void setMinggu(List<String> minggu) {
        this.minggu = minggu;
    }
}
