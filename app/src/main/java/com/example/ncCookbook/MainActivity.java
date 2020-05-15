package com.example.ncCookbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;

import android.content.SharedPreferences;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import android.Manifest;

import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_FOLDER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestForPermission();
    }

    public void updateMain(){
        int elem_per_row = 3;
        int elem_width = Resources.getSystem().getDisplayMetrics().widthPixels / elem_per_row;

        GridView grid = (GridView) findViewById(R.id.gridView);
        grid.setNumColumns(elem_per_row);
        grid.setColumnWidth(elem_width);

        String path = getPath();
        if (path == ""){
            pathSelector();
        } else {
            // reformat path string to actually be callable
            path = Environment.getExternalStorageDirectory().toString() + "/" + path.split(":")[1];

            File directory = new File(path);
            File[] files = directory.listFiles();


            final Recipe[] recipes = new Recipe[files.length];

            for (int i = 0; i < files.length; i++) {
                recipes[i] = new Recipe(files[i]);
            }

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view, int position, long id) {
                    Recipe recipe = recipes[position];
                    openRecipe(view, recipe);
                }
            });

            RecipeAdapter recipeAdapter = new RecipeAdapter(this, recipes, elem_per_row);
            grid.setAdapter(recipeAdapter);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateMain();
    }

    public void openRecipe(View v, Recipe recipe){
        Intent intent = new Intent (MainActivity.this, RecipeActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }

    protected String getPath(){
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.ncCookbook", Context.MODE_PRIVATE);
        String pathKey = "com.example.app.path";
        String path = prefs.getString(pathKey, "");
        return path;
    }

    // Path selection UI code

    protected void pathSelector(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, REQUEST_PICK_FOLDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_FOLDER && resultCode == Activity.RESULT_OK) {
            setPath(data.getData());
            updateMain();
        }
    }

    protected void setPath(Uri uri){
        Uri docUri = uri;
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.ncCookbook", Context.MODE_PRIVATE);
        String pathKey = "com.example.app.path";
        SharedPreferences.Editor editor = prefs.edit();

        String p = uri.getPath();
        p = uri.getLastPathSegment();
        editor.putString(pathKey, p);
        editor.commit();
    }

    // Get permision code

    public final String[] EXTERNAL_PERMS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public final int EXTERNAL_REQUEST = 138;

    public boolean requestForPermission() {

        boolean isPermissionOn = true;
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            if (!canAccessExternalSd()) {
                isPermissionOn = false;
                requestPermissions(EXTERNAL_PERMS, EXTERNAL_REQUEST);
            }
        }

        return isPermissionOn;
    }

    public boolean canAccessExternalSd() {
        return (hasPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE));
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));

    }

    // define the option menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.path:
                pathSelector();
                return true;

            default:
                return true;
        }
    }

}
