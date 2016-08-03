package com.example.daniel.projectnutella.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.projectnutella.MainActivity;
import com.example.daniel.projectnutella.PocketActivity;
import com.example.daniel.projectnutella.R;
import com.example.daniel.projectnutella.data.DbHelper;
import com.example.daniel.projectnutella.data.Pocket;
import com.example.daniel.projectnutella.data.Transaction;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Daniel on 13/7/2016.
 */
public class PocketAdapter extends RecyclerView.Adapter<PocketAdapter.PocketViewHolder>{

    private List<Pocket> pocketList;
    private Activity act;
    private AlertDialog dialog;

    public PocketAdapter(List<Pocket> pl, Activity activity){
        pocketList = pl;
        act = activity;
    }

    @Override
    public int getItemCount() {
        return pocketList.size();
    }

    @Override
    public void onBindViewHolder(PocketAdapter.PocketViewHolder holder, int position) {
        Pocket p = pocketList.get(position);
        holder.nameTv.setText(p.getName());
        double balance = 0.0;
        Cursor c = (new DbHelper(act)).getTransactions(p.getId());
        while (c.moveToNext())
            if (c.getInt(2)>0)  //is Income
                balance = balance + Double.valueOf(c.getString(1));
            else balance = balance - Double.valueOf(c.getString(1));
        holder.balanceTv.setText(String.valueOf(balance));
        if (balance > 0)
            holder.balanceTv.setTextColor(ContextCompat.getColor(act,R.color.colorIncome));
        else
            if (balance < 0)
                holder.balanceTv.setTextColor(ContextCompat.getColor(act,R.color.colorExpense));
            //leave 0.0 in neutral color
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
        //Manage the dialog options when Long pressing
        private AlertDialog dialog;
        private String pocketName;
        private MainActivity act;

        public PocketViewHolder(View v){
            super(v);
            nameTv = (TextView) v.findViewById(R.id.card_pocket_text_view);
            balanceTv = (TextView) v.findViewById(R.id.card_pocket_balance_tv);
            act = (MainActivity)v.getContext();

            v.findViewById(R.id.pocket_card_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pocketPos = ((RecyclerView)v.getParent()).getChildLayoutPosition(v);
                    Pocket p = act.getPocket(pocketPos);
                    if (p != null){
                        DbHelper db = new DbHelper(act);
                        Intent i = new Intent(act,PocketActivity.class);
                        i.putExtra("TITLE",db.getPocketName(p.getId()));
                        i.putExtra("ID",p.getId());
                        if (Build.VERSION.SDK_INT >= 21)
                            act.startActivity(i, ActivityOptions.makeSceneTransitionAnimation(act).toBundle());
                        else
                            act.startActivity(i);
                    }
                }
            });

            v.findViewById(R.id.pocket_card_view).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    pocketName = ((TextView)v.findViewById(R.id.card_pocket_text_view)).getText().toString();
                    AlertDialog.Builder builder = new AlertDialog.Builder(act);
                    builder.setTitle(R.string.update_pocket);
                    builder.setView(R.layout.update_pocket_layout);
                    dialog = builder.create();
                    dialog.show();

                    EditText pocketET = (EditText) dialog.findViewById(R.id.update_pocket_et);
                    if(pocketET != null){
                        pocketET.setText(pocketName);
                        pocketET.setSelection(pocketName.length());
                    }
                    Button cancelButton = (Button) dialog.findViewById(R.id.update_cancel_button);
                    if (cancelButton!= null)
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (dialog != null){
                                    dialog.hide();
                                }
                            }
                        });
                    Button deleteButton = (Button) dialog.findViewById(R.id.update_delete_button);
                    if (deleteButton != null)
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (dialog != null){
                                    act.deletePocket(getLayoutPosition());
                                    dialog.hide();
                                }
                            }
                        });
                    Button okButton = (Button) dialog.findViewById(R.id.update_ok_button);
                    if (okButton != null)
                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView dialogTV = (TextView) dialog.findViewById(R.id.update_pocket_et);
                                if (dialogTV != null && !dialogTV.getText().toString().equals(pocketName)
                                        && !dialogTV.getText().toString().isEmpty())
                                    act.updatePocket(getLayoutPosition(), dialogTV.getText().toString());
                                dialog.hide();
                            }
                        });
                    return true;
                }
            });
        }
    }
}