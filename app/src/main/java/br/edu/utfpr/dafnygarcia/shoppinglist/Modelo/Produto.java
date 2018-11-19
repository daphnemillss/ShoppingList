package br.edu.utfpr.dafnygarcia.shoppinglist.Modelo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "produtos",
            indices = @Index(value = {"nome"}, unique = true))
    public class Produto implements Serializable{

        @PrimaryKey(autoGenerate = true)
        private int idProduto;

        @NonNull
        private String nome;

        private float preco;

        private String obs;

        public Produto(String nome){
            setNome(nome);
        }

        public int getIdProduto() {
            return idProduto;
        }

        public void setIdProduto(int idProduto) {
            this.idProduto = idProduto;
        }

        @NonNull
        public String getNome() {
            return nome;
        }

        public void setNome(@NonNull String nome) {
            this.nome = nome;
        }

        public float getPreco() {return preco; }

        public void setPreco(float preco) {this.preco = preco; }

        public String getObs() {
            return obs;
        }

        public void setObs(String obs) {this.obs = obs; }


        @Override
        public String toString(){
            return getNome();
        }
    }


