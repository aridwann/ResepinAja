package com.kelompok2.uasmobpro;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // ambil data dari activity sebelumnya
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");

        // kirim data ke fragment
        Beranda beranda = new Beranda();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        beranda.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, beranda).commit();

        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.beranda) {
                beranda.setArguments(bundle);

                changeFragment(beranda);
                return true;
            } else if (menuItem.getItemId() == R.id.makanan) {
                bundle.putString("jenis", "Makanan");
                DaftarResep makanan = new DaftarResep();
                makanan.setArguments(bundle);
                changeFragment(makanan);
                return true;
            }else if (menuItem.getItemId() == R.id.minuman) {
                bundle.putString("jenis", "Minuman");
                DaftarResep minuman = new DaftarResep();
                minuman.setArguments(bundle);
                changeFragment(minuman);
                return true;
            }else if (menuItem.getItemId() == R.id.resepharian) {
                ResepHarian resepHarian = new ResepHarian();
                resepHarian.setArguments(bundle);
                changeFragment(resepHarian);
                return true;
            }
            return false;
        });
    }

    private void changeFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }
}