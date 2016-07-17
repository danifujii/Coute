package com.example.daniel.projectnutella.graphic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.example.daniel.projectnutella.data.CategoryManager;
import com.example.daniel.projectnutella.data.DbHelper;
import com.example.daniel.projectnutella.data.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 15/7/2016.
 */
public class ExpGraphicView extends View {

    private List<Pair<Rect,Paint>> rects = new ArrayList<>();

    public ExpGraphicView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    public void setCategories(List<Transaction> categories, double total, double totalWidth){
        int suma = 0;
        for(Transaction t: categories){
            Paint p = new Paint();
            //p.setColor(Palette.from(((BitmapDrawable)CategoryManager.getImage((Activity)getContext(),t.getCat())).getBitmap())
                    //.generate().getLightVibrantColor(0x000000));
            p.setColor(CategoryManager.getColor((Activity)getContext(),t.getCat()));
            p.setStyle(Paint.Style.FILL);

            int width = (int)((Double.valueOf(t.getAmount())/total) * totalWidth);
            Rect rect = new Rect();
            rect.set(suma,0,suma+width+5,700);  //plus 5 because of the rounding done when converting from double to int

            rects.add(new Pair<>(rect,p));
            suma = suma + width;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(Pair<Rect,Paint> p : rects){
            canvas.drawRect(p.first,p.second);
        }
    }
}
