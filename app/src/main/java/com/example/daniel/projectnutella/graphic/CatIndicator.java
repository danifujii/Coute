package com.example.daniel.projectnutella.graphic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.daniel.projectnutella.data.CategoryManager;

/**
 * Created by Daniel on 17/7/2016.
 */
public class CatIndicator extends View{
    private Paint paint = new Paint();

    public CatIndicator(Context context, AttributeSet attributeSet) {
        super(context,attributeSet);
    }

    public void setCategory(int catId){
        paint = new Paint();
        paint.setColor(CategoryManager.getColor((Activity)getContext(),catId));
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint != null)
            canvas.drawCircle(100, 100, 35, paint);
    }
}
