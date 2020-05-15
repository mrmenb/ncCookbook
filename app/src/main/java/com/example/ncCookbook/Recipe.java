package com.example.ncCookbook;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Serializable;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Recipe implements Serializable {

    private final File file;
    private final String imagePath;
    public String name, description, source, preparationTime, cookTime, totalTime;
    public JSONArray ingredients, instructions;
    public Long servings;

    public Recipe(File file) {
        this.file = file;
        this.imagePath = getImagePath();
        try {
            getJSON();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String getImagePath(){
        return this.file.getPath() + "/full.jpg";
    }

    private void getJSON() throws Exception{
        String[] path = this.file.getPath().split("/");
        this.name = path[path.length - 1];

        Object obj = new JSONParser().parse(new FileReader(this.file.getPath() + "/recipe.json"));

        JSONObject jo = (JSONObject) obj;

        this.name = (String) jo.get("name");
        this.description = (String) jo.get("description");
        this.source = (String) jo.get("url");
        this.servings = (Long) jo.get("recipeYield");
        this.preparationTime = (String) jo.get("prepTime");
        this.preparationTime = this.preparationTime.replace("PT","");
        this.preparationTime = this.preparationTime.replace("H",":");
        this.preparationTime = this.preparationTime.replace("M","");
        this.cookTime = (String) jo.get("cookTime");
        this.cookTime = this.cookTime.replace("PT","");
        this.cookTime = this.cookTime.replace("H",":");
        this.cookTime = this.cookTime.replace("M","");
        this.totalTime = (String) jo.get("totalTime");
        this.totalTime = this.totalTime.replace("PT","");
        this.totalTime = this.totalTime.replace("H",":");
        this.totalTime = this.totalTime.replace("M","");
        this.ingredients = (JSONArray) jo.get("recipeIngredient");
        this.instructions = (JSONArray) jo.get("recipeInstructions");
    }

    public void updateRecipe() throws Exception{
        Object obj = new JSONParser().parse(new FileReader(this.file.getPath() + "/recipe.json"));
        JSONObject jo = (JSONObject) obj;

        jo.put("description", this.description);
        jo.put("prepTime", "PT" + this.preparationTime.replace(":", "H") + "M");
        jo.put("cookTime", "PT" + this.cookTime.replace(":", "H") + "M");
        jo.put("totalTime", "PT" + this.totalTime.replace(":", "H") + "M");

        PrintWriter pw = new PrintWriter(this.file.getPath() + "/recipe.json");
        pw.write(jo.toJSONString());

        pw.flush();
        pw.close();

    }
}

