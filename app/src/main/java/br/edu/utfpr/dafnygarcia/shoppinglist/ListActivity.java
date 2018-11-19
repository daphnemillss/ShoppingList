package br.edu.utfpr.dafnygarcia.shoppinglist;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ListActivity extends AppCompatActivity {

    private static final String SHARED_FILE = "br.edu.utfpr.dafnygarcia.sharedpreferences.COLOR_PREF";
    private static final String COLOR = "COLOR";

    public static int selectedColor = Color.TRANSPARENT;

    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        layout = findViewById(R.id.layoutList);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newList();
            }
        });

        loadColor();
    }

    public void loadColor(){
        SharedPreferences sp = getSharedPreferences(SHARED_FILE, Context.MODE_PRIVATE);
        selectedColor = sp.getInt(COLOR, selectedColor);

        changeColor();
    }

    public void newList(){
        Intent intent = new Intent(this, Activity_NewList.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            Intent intent = new Intent(this, DevelopmentActivity.class);
            startActivityForResult(intent, 0);

            return true;
        }

        if(id == R.id.action_bg_color){
            chooseBgColor();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void chooseBgColor(){
        CharSequence option[] = new CharSequence[]{getString(R.string.coral), getString(R.string.sea), getString(R.string.ype), getString(R.string.transparent)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pick_color);

        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        saveColor(Color.rgb(255, 147, 146));
                        break;
                    case 1:
                        saveColor(Color.rgb(182, 255, 232));
                        break;
                    case 2:
                        saveColor(Color.rgb(190, 176, 232));
                        break;
                    case 3:
                        saveColor(Color.TRANSPARENT);
                        break;
                }
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // faz nada
            }
        });

        builder.show();
    }

    public void saveColor(int color){
        SharedPreferences sp = getSharedPreferences(SHARED_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putInt(COLOR, color);
        editor.commit();

        selectedColor = color;

        changeColor();
    }

    public void changeColor(){
        layout.setBackgroundColor(selectedColor);
    }
}
