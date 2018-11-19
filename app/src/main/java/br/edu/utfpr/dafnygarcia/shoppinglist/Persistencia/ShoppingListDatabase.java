package br.edu.utfpr.dafnygarcia.shoppinglist.Persistencia;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import br.edu.utfpr.dafnygarcia.shoppinglist.Modelo.Lista;
import br.edu.utfpr.dafnygarcia.shoppinglist.Modelo.Produto;

@Dao
@Database(entities = {Produto.class, Lista.class}, version = 1)
public abstract class ShoppingListDatabase extends RoomDatabase {

    public abstract ProdutoDao produtoDao();

    public abstract ListaDao listaDao();

    private static ShoppingListDatabase instance;

    public static ShoppingListDatabase getDatabase(final Context context) {

        if (instance == null) {

            synchronized (ShoppingListDatabase.class) {
                if (instance == null) {
                    Builder builder =  Room.databaseBuilder(context,
                            ShoppingListDatabase.class,
                            "shoppingList.db");
/*
                    builder.addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                }
                            });
                        }
                    });
*/
                    instance = (ShoppingListDatabase) builder.build();
                }
            }
        }

        return instance;
    }
}

