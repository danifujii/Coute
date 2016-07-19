package com.example.daniel.projectnutella.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daniel.projectnutella.PocketActivity;
import com.example.daniel.projectnutella.R;
import com.example.daniel.projectnutella.data.CategoryManager;
import com.example.daniel.projectnutella.data.DbHelper;
import com.example.daniel.projectnutella.data.Pocket;
import com.example.daniel.projectnutella.data.Transaction;

import java.util.List;

/**
 * Created by Daniel on 12/7/2016.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>{

    private List<Transaction> transactionList;
    private Activity act;
    private int transLongClicked = -1;
    private RecyclerView rv;

    public TransactionAdapter(List<Transaction> tl, Activity act)
    {
        transactionList = tl;
        this.act = act;
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        Transaction t = transactionList.get(position);

        holder.iv.setImageDrawable(CategoryManager.getImage(act,t.getCat()));
        holder.amountTv.setText(t.getAmount());
        if (t.getIsIncome())
            holder.amountTv.setTextColor(ContextCompat.getColor(holder.amountTv.getContext(),R.color.colorIncome));
        else holder.amountTv.setTextColor(ContextCompat.getColor(holder.amountTv.getContext(),R.color.colorExpense));

        holder.rl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                rv = (RecyclerView)v.getParent();
                transLongClicked = rv.getChildLayoutPosition(v);

                AlertDialog.Builder builder = new AlertDialog.Builder(act);
                builder.setMessage(act.getString(R.string.delete_dialog))
                        .setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (transLongClicked >= 0) {
                                    DbHelper db = new DbHelper(act);
                                    db.deleteTransaction(transactionList.get(transLongClicked).getId());
                                    transactionList.remove(transLongClicked);
                                    notifyItemRemoved(transLongClicked);
                                    ((PocketActivity)act).setBalance();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.transaction_layout, parent, false);

        return new TransactionViewHolder(itemView);
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder{
        protected ImageView iv;
        protected TextView amountTv;
        protected RelativeLayout rl;

        public TransactionViewHolder(View v){
            super(v);
            rl = (RelativeLayout) v.findViewById(R.id.trans_relative_layout);
            iv = (ImageView) v.findViewById(R.id.cat_image_view);
            amountTv = (TextView) v.findViewById(R.id.t_amount_text_view);
        }
    }
}
