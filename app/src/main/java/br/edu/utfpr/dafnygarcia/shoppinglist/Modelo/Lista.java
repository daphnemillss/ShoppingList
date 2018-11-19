package br.edu.utfpr.dafnygarcia.shoppinglist.Modelo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

    @Entity(tableName = "lista")
    public class Lista {

        @PrimaryKey(autoGenerate = true)
        private int idLista;

        @NonNull
        private String nome;

        public Lista(String nome){
            setNome(nome);
        }

        public int getIdLista() {
            return idLista;
        }

        public void setIdLista(int idLista) {
            this.idLista = idLista;
        }

        @NonNull
        public String getNome() {
            return nome;
        }

        public void setNome(@NonNull String nome) {
            this.nome = nome;
        }

        @Override
        public String toString(){
            return getNome();
        }
}
