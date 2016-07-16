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
    public static final String cat_fitness = "FITNESS";
    public static final String cat_travel = "TRAVEL";
    public static final String cat_games = "GAMES";
    public static final String cat_work = "WORK";
    public static final String cat_study = "STUDY";

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
            case cat_fitness: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_fitness,null);
            case cat_travel: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_travel,null);
            case cat_games: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_games,null);
            case cat_work: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_work,null);
            case cat_study: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_study,null);
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
        db.insertCategory(cat_fitness);
        db.insertCategory(cat_travel);
        db.insertCategory(cat_games);
        db.insertCategory(cat_work);
        db.insertCategory(cat_study);
        db.close();
    }

}
