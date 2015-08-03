package com.dev.cromer.jason.cshelper.Logic;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.cromer.jason.cshelper.R;

/**
 * Created by jason on 8/3/15.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.DataStructuresViewHolder> {


    public static class DataStructuresViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView dataStructureName;

        DataStructuresViewHolder(final View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.dataStructureCardView);
            dataStructureName = (TextView) itemView.findViewById(R.id.dataStructureItemText);
        }
    }


    @Override
    public RVAdapter.DataStructuresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RVAdapter.DataStructuresViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
