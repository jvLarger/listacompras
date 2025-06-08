package com.example.listacompras;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.listacompras.adapter.ListaComprasAdapter;
import com.example.listacompras.database.AppDatabase;
import com.example.listacompras.model.ListaDeCompras;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton newListButton;
    RecyclerView listasRecyclerView;
    ListaComprasAdapter adapter;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        newListButton = findViewById(R.id.newListButton);
        listasRecyclerView = findViewById(R.id.listasRecyclerView);
        listasRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListaComprasAdapter(new ArrayList<>());
        listasRecyclerView.setAdapter(adapter);

        db = AppDatabase.getInstance(this);

        carregarListas();

        newListButton.setOnClickListener(v -> {
            Intent newListIntent = new Intent(MainActivity.this, NewListActivity.class);
            startActivity(newListIntent);
        });
    }

    private void carregarListas() {
        new Thread(() -> {
            List<ListaDeCompras> listas = db.listaDeComprasDao().getTodasListas();
            runOnUiThread(() -> adapter.setListas(listas));
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarListas();
    }
}
