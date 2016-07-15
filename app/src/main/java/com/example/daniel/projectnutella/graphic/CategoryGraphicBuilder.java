package com.example.daniel.projectnutella.graphic;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.graphics.Palette;
import android.util.Log;

import com.example.daniel.projectnutella.data.CategoryManager;
import com.example.daniel.projectnutella.data.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 15/7/2016.
 */
public class CategoryGraphicBuilder {

    private double screenWidth;

    public CategoryGraphicBuilder(double width){
        this.screenWidth = width;
    }

    public LayerDrawable getGraphic(List<Transaction> expenses, double total, Activity activity){
        List<ShapeDrawable> sdList = new ArrayList<>();
        int suma = 0;
        for (Transaction t: expenses){
            ShapeDrawable s = new ShapeDrawable(new RectShape());
            int width = (int)((Double.valueOf(t.getAmount())/total) * screenWidth);
            s.setIntrinsicWidth(width);
            s.setIntrinsicHeight(200);
            s.setBounds(0,0,width,200);
            s.getPaint().setColor(getCategoryColor(t.getCat(),activity));
            //s.getPaint().setColor(Color.BLUE);
            s.setPadding(suma,0,width,0);
            suma = suma + width;
            sdList.add(0,s);
        }

        Drawable[] d = new Drawable[sdList.size()];
        d = sdList.toArray(d);
        Log.d("getGraphic",String.valueOf(d.length));
        LayerDrawable ld = new LayerDrawable(d);
        return ld;
    }

    private int getCategoryColor(int categoryId, Activity activity){
        Palette p = Palette.from(((BitmapDrawable)CategoryManager.getImage(activity,categoryId)).getBitmap())
                .generate();
        return p.getDarkVibrantColor(0x000000);
    }
}
