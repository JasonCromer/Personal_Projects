package com.example.jason.renderscripthelloworld;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class PictureFragment extends Fragment {
    private static final int GRAY_SCOTT_NUM_RUNS = 8000;

    private ImageView mImageView;
    private GrayScottDiffusionReaction mGrayScottDiffusionReaction;

    public PictureFragment() {
        // Required Empty Constructor
    }

    public static PictureFragment newInstance() {
        return new PictureFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter_picture, container, false);
        mImageView = (ImageView) view.findViewById(R.id.pictureView);
        setToolbarTitle();

//        executeAlgorithm(RenderScriptAsyncHelper.SCRIPT_TYPE_DIFFUSION_REACTION);
        mGrayScottDiffusionReaction = new GrayScottDiffusionReaction(getContext(), GRAY_SCOTT_NUM_RUNS);
        executeGrayScottDiffusionReaction();

        return view;
    }

    private void setToolbarTitle() {
        getActivity().setTitle(getString(R.string.picture_title));
    }

    private void executeAlgorithm(@RenderScriptAsyncHelper.ScriptType int scriptType) {
        RenderScriptAsyncHelper mRenderScriptHelper = new RenderScriptAsyncHelper(getContext());
        mRenderScriptHelper.init(scriptType);
        mRenderScriptHelper.execute();
    }

    private void executeGrayScottDiffusionReaction() {
        mGrayScottDiffusionReaction.execute();
    }

    public void setImageView(@NonNull Bitmap image) {
        mImageView.setImageBitmap(image);
    }

    @Override
    public void onDestroy() {
        mGrayScottDiffusionReaction.cleanRenderscriptObjects();
        super.onDestroy();
    }
}
