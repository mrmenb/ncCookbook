package com.example.ncCookbook;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecipeAdapter extends BaseAdapter {

    private final Context mContext;
    private final Recipe[] recipes;
    private final int numOfCol;

    public RecipeAdapter(Context context, Recipe[] recipes, int numOfCol) {
        this.mContext = context;
        this.recipes = recipes;
        this.numOfCol = numOfCol;
    }

    @Override
    public int getCount() {
        return recipes.length;
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Bitmap thumb = BitmapFactory.decodeFile(recipes[position].getImagePath());
        ImageView iconImage = new ImageView(mContext);
        iconImage.setImageBitmap(thumb);
        iconImage.setAdjustViewBounds(true);
        iconImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

        TextView hLabel = new TextView(mContext);
        hLabel.setText(recipes[position].name);
        hLabel.setPadding(6, 15, 6, 15);
        hLabel.setBackgroundColor(Color.parseColor("#FFFFFF"));
        hLabel.setAlpha(0.8f);
        hLabel.setTextSize(10 + 20/numOfCol);

        int bookItemWidth = (Resources.getSystem().getDisplayMetrics().widthPixels - numOfCol * 10 - 20)/ numOfCol;
        RelativeLayout bookItem = new RelativeLayout(mContext);
        bookItem.setPadding(5,5,5,5);

        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(bookItemWidth,bookItemWidth);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(bookItemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);

        bookItem.addView(iconImage, imageParams);
        bookItem.addView(hLabel, textParams);

        TextView dummyTextView = new TextView(mContext);
        dummyTextView.setText(String.valueOf(position));
        return bookItem;
    }

}
