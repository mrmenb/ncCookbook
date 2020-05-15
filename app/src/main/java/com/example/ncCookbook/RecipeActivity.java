package com.example.ncCookbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class RecipeActivity extends AppCompatActivity {

    Recipe recipe;

    private FloatingActionButton timerButton;
    private TextView cookTime;

    private CountDownTimer countDownTimer;
    private long timerTime = 0L;
    boolean timerRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        updateView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK) {
                recipe = (Recipe) data.getSerializableExtra("recipe");
                updateView();
            }
        }
    }

    private void updateView(){
        this.setTitle(recipe.name);
        Bitmap thumb = BitmapFactory.decodeFile(recipe.getImagePath());
        ImageView image = findViewById(R.id.recipeImage);
        image.setImageBitmap(thumb);
        image.setAdjustViewBounds(true);
        image.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TextView title = findViewById(R.id.title);
        title.setText(recipe.name);

        TextView description = findViewById(R.id.description);
        description.setText(recipe.description);

        TextView prepTime = findViewById(R.id.prepTime);
        prepTime.setText(recipe.preparationTime);

        cookTime = findViewById(R.id.cookingTime);
        cookTime.setText(recipe.cookTime);
        timerTime = Integer.parseInt(recipe.cookTime.split(":")[0]) * 3600000 + Integer.parseInt(recipe.cookTime.split(":")[1]) * 60000;
        timerButton = findViewById(R.id.timer);
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {startStop();}
        });


        TextView totalTime = findViewById(R.id.totalTime);
        totalTime.setText(recipe.totalTime);

        LinearLayout ingredientsLayout = findViewById(R.id.ingredients);
        if (recipe.ingredients != null) {
            for (int i = 0; i < recipe.ingredients.size(); i++) {
                TextView ingredient = new TextView(this);
                ingredient.setText(recipe.ingredients.get(i).toString());
                ingredientsLayout.addView(ingredient);
            }
        }else{
            TextView ingredients = findViewById(R.id.ingredientsText);
            ingredients.setVisibility(View.INVISIBLE);
        }

        LinearLayout instructionLayout = findViewById(R.id.instructions);
        if (recipe.instructions != null) {
            for (int i = 0; i < recipe.instructions.size(); i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                instructionLayout.addView(line(i, recipe.instructions.get(i).toString()), params);
            }
        }else{
            TextView instructions = findViewById(R.id.instructionsText);
            instructions.setVisibility(View.INVISIBLE);
        }

        //final Recipe rec = recipe;
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editRecipe(v);
            }
        });

    }

    public View line(int index, String text){
        LinearLayout linLay = new LinearLayout(this);
        linLay.setOrientation(LinearLayout.HORIZONTAL);
        linLay.setPadding(0,10,0,10);

        ToggleButton button = new ToggleButton(this);
        Drawable drawable = getResources().getDrawable(R.drawable.toggle_button);
        button.setBackgroundDrawable(drawable);
        button.setTextOff(String.valueOf(index + 1));
        button.setTextOn("");
        button.toggle();
        button.toggle();

        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(20,15,10,15);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(100, 100);


        linLay.addView(button, buttonParams);
        linLay.addView(textView);

        return linLay;
    }

    public void startStop(){
        if (timerRunning){
            stopTimer();
        } else {
            startTimer();
        }
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timerTime, 1000) {
            @Override
            public void onTick(long l) {
                timerTime = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.alarm_sound);
                mp.start();
                timerTime = Integer.parseInt(recipe.cookTime.split(":")[0]) * 3600000 + Integer.parseInt(recipe.cookTime.split(":")[1]) * 60000;
                stopTimer();
                updateTimer();
            }
        }.start();
        timerButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_pause));
        timerRunning = true;
    }


    public void stopTimer(){
        countDownTimer.cancel();
        timerButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_play));
        timerRunning = false;
    }

    public void updateTimer(){
        int hours = (int) timerTime / 3600000;
        int minutes = (int) timerTime % 3600000 / 60000;
        int seconds = (int) timerTime % 60000 / 1000;

        String timeText;
        timeText = hours + ":";
        if (minutes < 10){timeText += "0";}
        timeText += minutes + ":";
        if (seconds < 10){timeText +="0";}
        timeText += seconds;

        cookTime.setText(timeText);
    }

    public void editRecipe(View v){
        Intent intent = new Intent (this, EditRecipeActivity.class);
        intent.putExtra("recipe", recipe);
        startActivityForResult(intent,1);
    }
}
