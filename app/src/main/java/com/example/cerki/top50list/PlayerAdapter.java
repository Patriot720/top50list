package com.example.cerki.top50list;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by cerki on 29-Oct-17.
 */

public class PlayerAdapter extends ArrayAdapter<Player> {
    public int mLayout;
    public PlayerAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mLayout = textViewResourceId;
    }

    public PlayerAdapter(Context context, int resource, List<Player> items) {
        super(context, resource, items);
        mLayout = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
    View v = convertView;
    if(v == null){
        LayoutInflater vi ;
        vi = LayoutInflater.from(getContext());
        v = vi.inflate(mLayout,null);
    }
    Player p = getItem(position);
    if(p != null) {
        TextView username  = (TextView) v.findViewById(R.id.item_username);
        TextView acc = (TextView) v.findViewById(R.id.item_acc);
        TextView rank = (TextView) v.findViewById(R.id.item_rank);
        TextView pp = (TextView) v.findViewById(R.id.item_pp);
        TextView acc_diff = (TextView)v.findViewById(R.id.acc_diff);
        TextView rank_diff = (TextView)v.findViewById(R.id.rank_diff);
        TextView pp_diff = (TextView)v.findViewById(R.id.pp_diff);
        if(username != null) {
            username.setText(p.username);
        }
        if(acc != null) {
            acc.setText(p.accuracy);
            acc_diff.setText(p.mDifferenceFromDbValues.getAsString("acc"));
        }
        if(rank != null) {
            rank.setText(p.rank);
            rank_diff.setText(p.mDifferenceFromDbValues.getAsString("rank"));
        }
        if(pp != null) {
            pp.setText(p.pp);
            pp_diff.setText(p.mDifferenceFromDbValues.getAsString("pp"));
        }

    }
    return v;
    }
    }
