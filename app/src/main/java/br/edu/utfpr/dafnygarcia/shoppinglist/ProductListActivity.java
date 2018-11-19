package br.edu.utfpr.dafnygarcia.shoppinglist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import br.edu.utfpr.dafnygarcia.shoppinglist.Modelo.Produto;
import br.edu.utfpr.dafnygarcia.shoppinglist.Persistencia.ShoppingListDatabase;
import br.edu.utfpr.dafnygarcia.shoppinglist.Utils.UtilsGUI;

public class ProductListActivity extends AppCompatActivity {

    private ListView listViewProdutos;
    private ArrayAdapter<Produto> adapter;
    private List<Produto> listaProdutos;

    private ActionMode actionMode;
    private int posicao = -1;
    private View selectedView;

    private static final int REQ_NEW_PRODUCT = 1;
    private static final int REQ_UPDATE_PRODUCT = 2;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_product_selected, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Produto produto = (Produto) listViewProdutos.getItemAtPosition(posicao);
            switch (item.getItemId()) {
                case R.id.update_product:

                    CrudProductActivity.updateProduct(ProductListActivity.this, REQ_UPDATE_PRODUCT, produto);
                    mode.finish();
                    return true;

                case R.id.delete_product:
                    deleteProduct(produto);

                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (selectedView != null) {
                selectedView.setBackgroundColor(Color.TRANSPARENT);
            }

            actionMode = null;
            selectedView = null;

            listViewProdutos.setEnabled(true);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        setTitle(R.string.products);

        listViewProdutos = findViewById(R.id.listViewProdutos);

        listViewProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicao = position;
                Produto produto = (Produto) listViewProdutos.getItemAtPosition(posicao);
                CrudProductActivity.updateProduct(ProductListActivity.this, REQ_UPDATE_PRODUCT, produto);
            }
        });

        listViewProdutos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewProdutos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionMode != null) return false;

                posicao = position;
                view.setBackgroundColor(Color.rgb(245, 205, 6));
                selectedView = view;
                listViewProdutos.setEnabled(false);
                actionMode = startSupportActionMode(mActionModeCallback);

                return true;
            }
        });

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        loadProducts();
        registerForContextMenu(listViewProdutos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQ_NEW_PRODUCT || requestCode == REQ_UPDATE_PRODUCT) && resultCode == Activity.RESULT_OK) {
            loadProducts();
        }
    }

    private void loadProducts() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ShoppingListDatabase database = ShoppingListDatabase.getDatabase(ProductListActivity.this);

                listaProdutos = database.produtoDao().queryAll();

                ProductListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new ArrayAdapter<>(ProductListActivity.this, android.R.layout.simple_list_item_1, listaProdutos);

                        listViewProdutos.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private void deleteProduct(final Produto produto) {
        String mensagem = getString(R.string.deseja_realmente_apagar)
                + "\n" + produto.getNome();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        ShoppingListDatabase database =
                                                ShoppingListDatabase.getDatabase(ProductListActivity.this);

                                        database.produtoDao().delete(produto);

                                        ProductListActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                adapter.remove(produto);
                                            }
                                        });
                                    }
                                });

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

        UtilsGUI.confirmaAcao(this, mensagem, listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_product:
                CrudProductActivity.newProduct(this, REQ_NEW_PRODUCT);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
