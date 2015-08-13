package com.dev.cromer.jason.cshelper.Logic;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.cromer.jason.cshelper.R;

import java.util.List;

/**
 * Created by jason on 8/3/15.
 * This class is a custom Recycler View adapter. It takes in a DataStructureItem List as
 * a parameter and generates a recycler view from it.
 * The static class DataStructureViewHolder is used to generate the data that will populate the
 * card item that fills the recycler view. This is done so that the data is only called once,
 * and thus improving performance so that the RecyclerViewAdapter doesn't have to inflate and re-populate
 * the entire card items each time it is loaded.
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.DataStructuresViewHolder> {

    //Create an empty list of dataStructureItems
    private List<DataStructureItem> dataStructureItems;


    //Constructor for RecyclerViewAdapter
    public RecyclerViewAdapter(List<DataStructureItem> dataStructureItems) {
        this.dataStructureItems = dataStructureItems;
    }



    //This class generates the data for the card view and implements an OnClickListener
    public static class DataStructuresViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView cardView;
        private TextView dataStructureName;
        private TextView dataStructureDescription;
        private TextView clickToLearnMoreTextButton;

        private FrameLayout cardItemsLayout;
        private ImageButton expandCardButton;

        private boolean cardIsExpanded;
        private String wikipediaStartingURL = "https://en.wikipedia.org/wiki/";
        private int ORIGINAL_CARD_HEIGHT = 136;
        private int ENLARGED_CARD_HEIGHT = 550;

        //Constructor to inflate the card item
        DataStructuresViewHolder(final View itemView) {
            super(itemView);

            cardIsExpanded = false;
            cardView = (CardView) itemView.findViewById(R.id.dataStructureCardView);
            dataStructureName = (TextView) itemView.findViewById(R.id.dataStructureItemText);
            dataStructureDescription = (TextView) itemView.findViewById(R.id.dataStructureDescriptionText);
            clickToLearnMoreTextButton = (TextView) itemView.findViewById(R.id.cardLearnMoreLinkTextView);

            expandCardButton = (ImageButton) itemView.findViewById(R.id.expandCardButton);
            cardItemsLayout = (FrameLayout) itemView.findViewById(R.id.cardItemsLayout);

            expandCardButton.setOnClickListener(this);
            cardView.setOnClickListener(this);
            clickToLearnMoreTextButton.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if(v == expandCardButton || v == cardView) {
                //Increase textview size when clicked
                if(!cardIsExpanded){
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ENLARGED_CARD_HEIGHT);
                    cardItemsLayout.setLayoutParams(lp);

                    //Create alert Dialog


                    //show extended information
                    dataStructureDescription.setVisibility(View.VISIBLE);
                    clickToLearnMoreTextButton.setVisibility(View.VISIBLE);

                    //change arrow direction to up
                    expandCardButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    cardIsExpanded = true;
                }
                else {
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ORIGINAL_CARD_HEIGHT);
                    cardItemsLayout.setLayoutParams(lp);

                    //hide extended information
                    dataStructureDescription.setVisibility(View.GONE);
                    clickToLearnMoreTextButton.setVisibility(View.GONE);

                    //change arrow direction to down
                    expandCardButton.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    cardIsExpanded = false;
                }
            }
            else if(v == clickToLearnMoreTextButton) {
                //replace all whitespace with underscore for correct Wikipedia URL
                final String dataStructURLExtension = dataStructureName.getText().toString().replaceAll(" ", "_").toLowerCase();

                //Open the wikipedia page via the phone's internet browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaStartingURL + dataStructURLExtension));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                itemView.getContext().startActivity(browserIntent);
            }
        }
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
        dataStructViewHolder.dataStructureDescription.setText(dataStructureItems.get(position).description);
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
