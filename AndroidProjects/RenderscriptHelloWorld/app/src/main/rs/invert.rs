#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name(com.example.jason.renderscripthelloworld)

#include "rs_debug.rsh"

uchar4 RS_KERNEL invert(uchar4 in) {
    uchar4 out = in;
    out.r = 255 - in.r;
    out.g = 255 - in.g;
    out.b = 255 - in.b;

    return out;
}

// Using a single source pattern
void process(rs_allocation inputImage) {
    const uint32_t imageWidth = rsAllocationGetDimX(inputImage);
    const uint32_t imageHeight = rsAllocationGetDimY(inputImage);

    rs_allocation temp = rsCreateAllocation_uchar4(imageWidth, imageHeight);
    rsForEach(invert, inputImage, temp);
}