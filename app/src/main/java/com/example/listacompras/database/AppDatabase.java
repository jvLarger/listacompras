package com.example.listacompras.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.listacompras.dao.ListaDeComprasDao;
import com.example.listacompras.model.Item;
import com.example.listacompras.model.ListaDeCompras;
import com.example.listacompras.model.ListaItemCrossRef;

@Database(entities = {ListaDeCompras.class, Item.class, ListaItemCrossRef.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract ListaDeComprasDao listaDeComprasDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "lista_compras_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
