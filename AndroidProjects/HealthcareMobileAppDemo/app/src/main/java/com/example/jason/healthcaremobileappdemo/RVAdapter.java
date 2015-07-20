package com.example.jason.healthcaremobileappdemo;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by jason on 7/19/15.
 *
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    private List<Person> persons;

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        private CardView cv;
        private TextView personName;
        private TextView personAge;
        private ImageView personPhoto;


        PersonViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            personName = (TextView)itemView.findViewById(R.id.person_name);
            personAge = (TextView)itemView.findViewById(R.id.person_age);
            personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent exerciseVideoIntent = new Intent(itemView.getContext(), ExerciseVideo.class);
                    exerciseVideoIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    exerciseVideoIntent.putExtra("Exercise Name", personName.getText().toString());
                    itemView.getContext().startActivity(exerciseVideoIntent);


                }
            });
        }
    }

    RVAdapter(List<Person> persons){
        this.persons = persons;
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(persons.get(i).name);
        personViewHolder.personAge.setText(persons.get(i).age);
        personViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public void onViewAttachedToWindow(PersonViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }
}
