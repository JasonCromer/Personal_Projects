#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name(com.example.jason.renderscripthelloworld)

#include "rs_debug.rsh"

uchar4 RS_KERNEL invert(uchar4 in, uint32_t x, uint32_t y) {
    uchar4 out = in;
    out.r = 255 - in.r;
    out.g = 255 - in.g;
    out.b = 255 - in.b;

    return out;
}

// Using a single source pattern
void doubleTime(rs_allocation input, rs_allocation output) {
    rsForEach(invert, input, output);
}