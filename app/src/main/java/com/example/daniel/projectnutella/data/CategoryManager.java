package com.example.daniel.projectnutella.data;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
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
    public static final String cat_games = "GAMES";
    public static final String cat_work = "WORK";
    public static final String cat_study = "STUDY";
    public static final String cat_other = "OTHER";
    public static final String cat_income = "INCOME";

    public static Drawable getImage(Activity act, int cat){
        DbHelper db = new DbHelper(act);
        String catName = db.getCategoryName(cat);
        switch(catName){
            case cat_clothes: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_clothes,null);
            case cat_drinks: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_drinks,null);
            case cat_food: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_food,null);
            case cat_house: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_house,null);
            case cat_car: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_car,null);
            case cat_shop: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_shopping,null);
            case cat_transp: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_transp,null);
            case cat_fitness: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_fitness,null);
            case cat_other: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_other,null);
            case cat_games: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_games,null);
            case cat_work: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_work,null);
            case cat_study: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_study,null);
            case cat_income: return ResourcesCompat.getDrawable(act.getResources(),R.mipmap.cat_income,null);
        }
        return new ColorDrawable(Color.TRANSPARENT);
    }

    public static void insertCategoriesIntoDB(Activity act){
        DbHelper db = new DbHelper(act);
        db.getWritableDatabase();
        db.insertCategory(cat_other);
        db.insertCategory(cat_clothes);
        db.insertCategory(cat_drinks);
        db.insertCategory(cat_food);
        db.insertCategory(cat_house);
        db.insertCategory(cat_car);
        db.insertCategory(cat_shop);
        db.insertCategory(cat_transp);
        db.insertCategory(cat_fitness);
        db.insertCategory(cat_games);
        db.insertCategory(cat_work);
        db.insertCategory(cat_study);
        db.insertCategory(cat_income);
        db.close();
    }

    public static int getColor(Activity act, int categoryId){
        DbHelper db = new DbHelper(act);
        String catName = db.getCategoryName(categoryId);
        switch(catName){
            case cat_clothes: return ContextCompat.getColor(act,R.color.cat_clothes);
            case cat_drinks: return ContextCompat.getColor(act,R.color.cat_drinks);
            case cat_food: return ContextCompat.getColor(act,R.color.cat_food);
            case cat_house: return ContextCompat.getColor(act,R.color.cat_house);
            case cat_car: return ContextCompat.getColor(act,R.color.cat_car);
            case cat_shop: return ContextCompat.getColor(act,R.color.cat_shopping);
            case cat_transp: return ContextCompat.getColor(act,R.color.cat_transp);
            case cat_fitness: return ContextCompat.getColor(act,R.color.cat_fitness);
            case cat_games: return ContextCompat.getColor(act,R.color.cat_games);
            case cat_work: return ContextCompat.getColor(act,R.color.cat_work);
            case cat_study: return ContextCompat.getColor(act,R.color.cat_study);
            case cat_other: return ContextCompat.getColor(act,R.color.cat_other);
        }
        return Color.TRANSPARENT;
    }

}
