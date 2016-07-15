package com.example.daniel.projectnutella.graphic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Daniel on 15/7/2016.
 */
public class ExpGraphicView extends View {

    public ExpGraphicView(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect rect = new Rect();
        rect.set(0,0,canvas.getWidth(),90);

        Paint grey = new Paint();
        grey.setColor(Color.GRAY);
        grey.setStyle(Paint.Style.FILL);

        canvas.drawRect(rect,grey);

    }
}
