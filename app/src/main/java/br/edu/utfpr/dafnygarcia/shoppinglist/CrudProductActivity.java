package br.edu.utfpr.dafnygarcia.shoppinglist;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import br.edu.utfpr.dafnygarcia.shoppinglist.Modelo.Produto;
import br.edu.utfpr.dafnygarcia.shoppinglist.Persistencia.ShoppingListDatabase;
import br.edu.utfpr.dafnygarcia.shoppinglist.Utils.UtilsGUI;

public class CrudProductActivity extends AppCompatActivity {
    public static final int NOVO = 1;
    public static final int ATUALIZAR = 2;

    public static final String MODO = "MODO";
    public static final String ID = "ID";
    public static final String PRODUTO = "PRODUTO";

    private int modo;

    private EditText editText_Name;
    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_product);

        editText_Name = findViewById(R.id.editText_Name);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        if(bundle != null){
            modo = bundle.getInt(MODO, NOVO);

            if(modo == NOVO){
                setTitle(R.string.new_product);
                
                produto = new Produto("");
            }else if(modo == ATUALIZAR){
                setTitle(R.string.update_product);

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        int id = bundle.getInt(ID);

                        ShoppingListDatabase database = ShoppingListDatabase.getDatabase(CrudProductActivity.this);

                        produto = database.produtoDao().queryForId(id);

                        CrudProductActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                editText_Name.setText(produto.getNome());
                            }
                        });
                    }
                });
            }

            editText_Name.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
                        saveProduct();

                    return false;
                }
            });

            editText_Name.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            ActionBar ab = getSupportActionBar();
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    public void saveButtonClicked(View view){
        saveProduct();
    }

    public void saveProduct(){
        String nome = UtilsGUI.validaCampoTexto(this, editText_Name, R.string.name_empty);
        if(nome == null)return;
        
        produto.setNome(nome);
     
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ShoppingListDatabase database = ShoppingListDatabase.getDatabase(CrudProductActivity.this);

                if (modo == NOVO) {
                    int novoId = (int) database.produtoDao().insert(produto);
                    produto.setIdProduto(novoId);
                }else{
                    database.produtoDao().update(produto);
                }

                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    public static void newProduct(AppCompatActivity activity, int requestCode){
        Intent intent = new Intent(activity, CrudProductActivity.class);
        intent.putExtra(MODO, NOVO);

        activity.startActivityForResult(intent, requestCode);
    }

    public static void updateProduct(AppCompatActivity activity, int requestCode, Produto produto){

        Intent intent = new Intent(activity, CrudProductActivity.class);

        intent.putExtra(MODO, ATUALIZAR);

        intent.putExtra(ID, produto.getIdProduto()  );

        activity.startActivityForResult(intent, requestCode);
    }

    private void cancelar(){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
    @Override
    public void onBackPressed(){
        cancelar();
    }
}
