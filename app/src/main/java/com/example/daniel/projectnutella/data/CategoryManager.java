package com.example.daniel.projectnutella.data;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import com.example.daniel.projectnutella.R;

/**
 * Created by Daniel on 13/7/2016.
 */
public class CategoryManager {

    public static final String cat_clothes = "CLOTHES";
    public static final String cat_drinks = "DRINKS";
    public static final String cat_food = "FOOD";
    public static final String cat_house = "HOUSE";
    public static final String cat_car = "CAR";
    public static final String cat_shop = "SHOPPING";
    public static final String cat_transp = "TRANSPORT";

    public static Drawable getImage(Activity act, int cat){
        DbHelper db = new DbHelper(act);
        String catName = db.getCategoryName(cat);
        switch(catName){
            case cat_clothes: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_clothes,null);
            case cat_drinks: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_drinks,null);
            case cat_food: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_food,null);
            case cat_house: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_house,null);
            case cat_car: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_oil,null);
            case cat_shop: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_shopping,null);
            case cat_transp: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_transport,null);
        }
        return new ColorDrawable(Color.TRANSPARENT);
    }

    public static void insertCategoriesIntoDB(Activity act){
        DbHelper db = new DbHelper(act);
        db.getWritableDatabase();
        db.insertCategory(cat_clothes);
        db.insertCategory(cat_drinks);
        db.insertCategory(cat_food);
        db.insertCategory(cat_house);
        db.insertCategory(cat_car);
        db.insertCategory(cat_shop);
        db.insertCategory(cat_transp);
        db.close();
    }

    public static int getCategoryId(Activity act, int idDrawable){
        DbHelper db = new DbHelper(act);
        switch(idDrawable){
            case R.mipmap.cat_clothes: return db.getCategoryId(cat_clothes);
            case R.mipmap.cat_drinks: return db.getCategoryId(cat_drinks);
            case R.mipmap.cat_food: return db.getCategoryId(cat_food);
            case R.mipmap.cat_house: return db.getCategoryId(cat_house);
            case R.mipmap.cat_oil: return db.getCategoryId(cat_car);
            case R.mipmap.cat_shopping: return db.getCategoryId(cat_shop);
            case R.mipmap.cat_transport: return db.getCategoryId(cat_transp);
        }
        return 0;
    }

}
