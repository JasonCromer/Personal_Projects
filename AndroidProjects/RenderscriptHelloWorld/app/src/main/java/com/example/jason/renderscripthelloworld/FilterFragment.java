package com.example.jason.renderscripthelloworld;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FilterFragment extends Fragment {

    private ImageView mBeforeImageView;
    private ImageView mAfterImageView;
    private Bitmap mBeforeImage;
    private Bitmap mAfterImage;

    public FilterFragment() {
        // Required empty constructor
    }

    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        mBeforeImageView = (ImageView) view.findViewById(R.id.before_image_view);
        mAfterImageView = (ImageView) view.findViewById(R.id.after_image_view);

        // Convert drawable to bitmap
        mBeforeImage = BitmapFactory.decodeResource(getResources(), R.drawable.landscape);
        mBeforeImageView.setImageBitmap(mBeforeImage);

        // Equalize our before bitmap and set it to mAfterImageView
        new HistogramEqualizationTask().execute();

        return view;
    }

    private void setResultBitmap() {
        mAfterImageView.setImageBitmap(mAfterImage);
    }

    private class HistogramEqualizationTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            mAfterImage = Utils.histogramEqualization(mBeforeImage, getActivity());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setResultBitmap();
        }
    }
}
