package com.example.listacompras.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ListaDeCompras {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String nome;

    @NonNull
    public String data;
}