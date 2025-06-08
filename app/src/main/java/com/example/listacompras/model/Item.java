package com.example.listacompras.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Item {

    public Item(@NonNull String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public Item() {}

    @PrimaryKey(autoGenerate = true)
    public int id;

    @NonNull
    public String nome;

    public double preco;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Item)) return false;
        Item other = (Item) obj;
        return nome.equals(other.nome);
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }
}
