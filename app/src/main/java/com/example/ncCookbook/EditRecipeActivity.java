package com.example.ncCookbook;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;

public class EditRecipeActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        final Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");

        EditText editDescription = findViewById(R.id.editDescription);
        editDescription.setText(recipe.description);

        TimePicker prepTime = findViewById(R.id.editPreparationTime);
        prepTime.setIs24HourView(true);
        prepTime.setHour(Integer.parseInt(recipe.preparationTime.split(":")[0]));
        prepTime.setMinute(Integer.parseInt(recipe.preparationTime.split(":")[1]));

        TimePicker cookTime = findViewById(R.id.editCookTime);
        cookTime.setIs24HourView(true);
        cookTime.setHour(Integer.parseInt(recipe.cookTime.split(":")[0]));
        cookTime.setMinute(Integer.parseInt(recipe.cookTime.split(":")[1]));

        TimePicker totalTime = findViewById(R.id.editTotalTime);
        totalTime.setIs24HourView(true);
        totalTime.setHour(Integer.parseInt(recipe.totalTime.split(":")[0]));
        totalTime.setMinute(Integer.parseInt(recipe.totalTime.split(":")[1]));

        Button button = findViewById(R.id.editSaveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipe(v,recipe);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void saveRecipe(View v, Recipe recipe){

        EditText editDescription = findViewById(R.id.editDescription);
        recipe.description = editDescription.getText().toString();

        TimePicker prepTime = findViewById(R.id.editPreparationTime);
        recipe.preparationTime = prepTime.getHour() + ":" + prepTime.getMinute();

        TimePicker cookTime = findViewById(R.id.editCookTime);
        recipe.cookTime = cookTime.getHour() + ":" + cookTime.getMinute();

        TimePicker totalTime = findViewById(R.id.editTotalTime);
        recipe.totalTime = totalTime.getHour() + ":" + totalTime.getMinute();

        try{
        recipe.updateRecipe();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Snackbar snackbar = Snackbar.make(v, "Saved", Snackbar.LENGTH_LONG);
        snackbar.show();

        Intent intent = new Intent();
        intent.putExtra("recipe", recipe);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}