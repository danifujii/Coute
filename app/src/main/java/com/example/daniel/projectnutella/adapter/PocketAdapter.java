package com.example.daniel.projectnutella.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.projectnutella.MainActivity;
import com.example.daniel.projectnutella.PocketActivity;
import com.example.daniel.projectnutella.R;
import com.example.daniel.projectnutella.data.DbHelper;
import com.example.daniel.projectnutella.data.Pocket;
import com.example.daniel.projectnutella.data.Transaction;

import java.util.List;

/**
 * Created by Daniel on 13/7/2016.
 */
public class PocketAdapter extends RecyclerView.Adapter<PocketAdapter.PocketViewHolder>{

    private List<Pocket> pocketList;

    public PocketAdapter(List<Pocket> pl){
        pocketList = pl;
    }

    @Override
    public int getItemCount() {
        return pocketList.size();
    }

    @Override
    public void onBindViewHolder(PocketAdapter.PocketViewHolder holder, int position) {
        Pocket t = pocketList.get(position);
        holder.nameTv.setText(t.getName());
        holder.balanceTv.setText(String.valueOf(t.getBalance()));
    }

    @Override
    public PocketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.pocket_layout, parent, false);

        return new PocketViewHolder(itemView);
    }

    public static class PocketViewHolder extends RecyclerView.ViewHolder{
        protected TextView nameTv;
        protected TextView balanceTv;

        public PocketViewHolder(View v){
            super(v);
            nameTv = (TextView) v.findViewById(R.id.card_pocket_text_view);
            balanceTv = (TextView) v.findViewById(R.id.card_pocket_balance_tv);

            v.findViewById(R.id.pocket_card_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity act = (Activity)v.getContext();
                    int pocketNumber = ((RecyclerView)v.getParent()).getChildLayoutPosition(v) + 1;
                    DbHelper db = new DbHelper(act);
                    Intent i = new Intent(act,PocketActivity.class);
                    i.putExtra("TITLE",db.getPocketName(pocketNumber));
                    i.putExtra("ID",pocketNumber);
                    act.startActivity(i);
                }
            });
        }
    }
}