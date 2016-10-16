package com.example.jason.renderscripthelloworld;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

class Utils {

    static void replaceFragment(FragmentManager fragmentManager, int containerViewId,
                                Fragment fragment, String fragmentTag, boolean addToBackStack) {

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, fragmentTag);

        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragmentTag);
        }

        fragmentTransaction.commit();
    }

    static Bitmap histogramEqualization(Bitmap image, Context context) {
        // Get image size
        int width = image.getWidth();
        int height = image.getHeight();

        // Create new bitmap
        Bitmap bitmap = image.copy(image.getConfig(), true /* isMutable */);

        // Create renderscript
        RenderScript renderScript = RenderScript.create(context);

        // Create allocation from Bitmap
        Allocation allocationA = Allocation.createFromBitmap(renderScript, bitmap);

        // Create allocation with same type
        Allocation allocationB = Allocation.createTyped(renderScript, allocationA.getType());

        // Create script from renderscript file
        ScriptC_histogramEqualizer histogramEqualizer = new ScriptC_histogramEqualizer(renderScript);

        // Set size in script
        histogramEqualizer.set_size(width * height);

        // Call the first kernel
        histogramEqualizer.forEach_root(allocationA, allocationB);

        // Call the renderscript method to compute the remap array
        histogramEqualizer.invoke_createRemapArray();

        // Call second kernel
        histogramEqualizer.forEach_remaptoRGB(allocationB, allocationA);

        // Copy script result into our bitmap
        allocationA.copyTo(bitmap);

        // De-allocate memory usage
        allocationA.destroy();
        allocationB.destroy();
        histogramEqualizer.destroy();
        renderScript.destroy();

        return bitmap;
    }

    static Bitmap gaussianBlur(Bitmap image, Context context, float blurRadius) {
        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap( rs, image, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT );
        final Allocation output = Allocation.createTyped( rs, input.getType() );
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create( rs, Element.U8_4( rs ) );
        script.setRadius(blurRadius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(image);

        return image;
    }
}
