package com.example.daniel.projectnutella;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.daniel.projectnutella.adapter.TransactionAdapter;
import com.example.daniel.projectnutella.data.Transaction;

import java.util.List;


public class TransFragment extends Fragment {
    private String date;
    private List<Transaction> trans;
    private boolean isLast = false;
    private boolean isFirst = false;
    private OnFragmentInteractionListener mListener;

    public TransFragment() {
        // Required empty public constructor
    }

    public void setDate(String date, List<Transaction> transactions){
        this.date = date;
        this.trans = transactions;
    }

    //Adjust arrows for changing days. Both bools can be true if its the only day
    public void setDayPosition(boolean isFirst, boolean isLast){
        this.isFirst = isFirst;
        this.isLast = isLast;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trans, container, false);
        ((TextView)v.findViewById(R.id.day_text_view)).setText(date);
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.history_recycler_view);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setAdapter(new TransactionAdapter(trans,getActivity()));

        if (isFirst)
            v.findViewById(R.id.day_left_button).setVisibility(View.INVISIBLE);
        if (isLast)
            v.findViewById(R.id.day_right_button).setVisibility(View.INVISIBLE);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //if (context instanceof OnFragmentInteractionListener) {
          //  mListener = (OnFragmentInteractionListener) context;
        //} else {
          //  throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
        //}
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
