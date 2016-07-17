package com.example.daniel.projectnutella.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.projectnutella.MainActivity;
import com.example.daniel.projectnutella.PocketActivity;
import com.example.daniel.projectnutella.R;
import com.example.daniel.projectnutella.data.CategoryManager;
import com.example.daniel.projectnutella.data.DbHelper;
import com.example.daniel.projectnutella.data.Pocket;
import com.example.daniel.projectnutella.data.Transaction;
import com.example.daniel.projectnutella.graphic.CatIndicator;

import java.util.List;

/**
 * Created by Daniel on 13/7/2016.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Transaction> catsList;
    private Activity act;
    private int expandedPos = -1;

    public CategoryAdapter(Activity act, List<Transaction> pl){
        this.act = act;
        catsList = pl;
    }

    @Override
    public int getItemCount() {
        return catsList.size();
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.CategoryViewHolder holder, int position) {
        Transaction t = catsList.get(position);
        DbHelper db = new DbHelper(act);
        String catName = db.getCategoryName(t.getCat());
        if (catName.length() > 0)
            holder.nameTv.setText(catName.charAt(0)+catName.substring(1,catName.length()).toLowerCase());
        holder.sumTv.setText("$"+String.valueOf(t.getAmount()));
        holder.catIv.setImageDrawable(CategoryManager.getImage(act,t.getCat()));
        holder.indicator.invalidate();
        holder.indicator.setCategory(t.getCat());
        holder.indicator.invalidate();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.category_layout, parent, false);

        return new CategoryViewHolder(itemView);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{
        protected TextView nameTv;
        protected TextView sumTv;
        protected ImageView catIv;
        protected CatIndicator indicator;

        public CategoryViewHolder(View v){
            super(v);
            indicator = (CatIndicator)v.findViewById(R.id.indicator_view);
            catIv = (ImageView) v.findViewById(R.id.cat_image_view);
            nameTv = (TextView) v.findViewById(R.id.cat_name_text_view);
            sumTv = (TextView) v.findViewById(R.id.cat_sum_text_view);
        }
    }
}