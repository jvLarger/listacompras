package com.example.listacompras.model;

import androidx.room.Entity;

@Entity(primaryKeys = {"listaId", "itemId"})
public class ListaItemCrossRef {
    public int listaId;
    public int itemId;
}