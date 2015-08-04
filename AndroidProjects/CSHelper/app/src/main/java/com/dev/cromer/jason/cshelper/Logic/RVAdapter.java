package com.dev.cromer.jason.cshelper.Logic;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dev.cromer.jason.cshelper.R;

import java.util.List;

/**
 * Created by jason on 8/3/15.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.DataStructuresViewHolder> {

    private List<DataStructureItem> dataStructureItems;


    public static class DataStructuresViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView dataStructureName;

        DataStructuresViewHolder(final View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.dataStructureCardView);
            dataStructureName = (TextView) itemView.findViewById(R.id.dataStructureItemText);
        }
    }



    public RVAdapter(List<DataStructureItem> dataStructureItems) {
        this.dataStructureItems = dataStructureItems;
    }

    //Inflate the layout by grabbing the card_view_item and instantiating the new layout view
    @Override
    public DataStructuresViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_item, viewGroup, false);
        DataStructuresViewHolder dataStructViewHolder = new DataStructuresViewHolder(v);

        return dataStructViewHolder;
    }


    //Use the DataStructuresViewHolder to populate the card via the Recycler View
    //This function is called for each card in the layout
    @Override
    public void onBindViewHolder(DataStructuresViewHolder dataStructViewHolder, int position) {
        dataStructViewHolder.dataStructureName.setText(dataStructureItems.get(position).name);

    }

    @Override
    public int getItemCount() {
        return dataStructureItems.size();
    }

    @Override
    public void onViewAttachedToWindow(DataStructuresViewHolder dataStructViewHolder) {
        super.onViewAttachedToWindow(dataStructViewHolder);
    }
}
