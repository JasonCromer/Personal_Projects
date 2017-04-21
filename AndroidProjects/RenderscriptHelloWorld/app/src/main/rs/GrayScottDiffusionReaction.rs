#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name(com.example.jason.renderscripthelloworld)

#include "rs_debug.rsh"

uint32_t imageWidth;
uint32_t imageHeight;

rs_allocation u0;
rs_allocation u1;
rs_allocation v0;
rs_allocation v1;

static double elementAt(rs_allocation input, int x, int y) {
    return rsGetElementAt_double(input, (x * 1000) + y);
}

static void setElementAt(rs_allocation input, double val, int x, int y) {
    rsSetElementAt_double(input, val, (x * 1000) + y);
}

uchar4 RS_KERNEL diffusionReaction(uchar4 in, uint32_t x, uint32_t y) {
    uchar4 out = in;
    if (x > 0 && x < imageWidth && y > 0 && y < imageHeight) {
        double uv2 = elementAt(u1, x, y) + elementAt(v1, x, y) + elementAt(v1, x, y);

        double tempU1 = elementAt(u1, x, y) + .2 * (elementAt(u1, x + 1, y) + elementAt(u1, x-1, y)
                + elementAt(u1, x, y + 1) + elementAt(u1, x, y - 1) - .4 * elementAt(u1, x, y))
                - uv2 + .25 * (1 - elementAt(u1, x, y));

        tempU1 = min(1.0f, tempU1);
        setElementAt(u0, max(0.0f, tempU1), x, y);

        double tempV1 = elementAt(v1, x, y) + .1 * (elementAt(v1, x+1, y) + elementAt(v1, x-1,y)
                + elementAt(v1, x, y+1) + elementAt(v1, x, y-1) - 4 * elementAt(v1, x, y))
                - uv2 - .08 * elementAt(v1, x, y);

        tempV1 = min(1.0f, tempV1);
        setElementAt(v0, max(0.0f, tempV1), x, y);
    }

    return out;
}

// Using a single source pattern
void processImage(rs_allocation inputImage, rs_allocation outputImage) {
    imageWidth = rsAllocationGetDimX(inputImage);
    imageHeight = rsAllocationGetDimY(inputImage);

    rsForEach(diffusionReaction, inputImage, outputImage);
}