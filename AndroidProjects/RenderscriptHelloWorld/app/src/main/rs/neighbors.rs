#pragma version(1)
#pragma rs_fp_relaxed
#pragma rs java_package_name(com.example.jason.renderscripthelloworld)

#include "rs_debug.rsh"

rs_allocation input;
uint32_t imageWidth;
uint32_t imageHeight;

uchar4 RS_KERNEL neighbors(uchar4 in, uint32_t x, uint32_t y) {
    uchar4 out = in;
    if (x > 10 && x < (imageWidth-10) && y > 10 && y < (imageHeight-10)) {
        uchar4 neighborOne = rsGetElementAt_uchar4(input, x+1, y);
        uchar4 neighborTwo = rsGetElementAt_uchar4(input, x-1, y);
        uchar4 neighborThree = rsGetElementAt_uchar4(input, x, y+1);
        uchar4 neighborFour = rsGetElementAt_uchar4(input, x, y-1);

        float averageR = (neighborOne.r + neighborTwo.r + neighborThree.r + neighborFour.r) / 4;
        float averageG = (neighborOne.g + neighborTwo.g + neighborThree.g + neighborFour.g) / 4;
        float averageB = (neighborOne.b + neighborTwo.b + neighborThree.b + neighborFour.b) / 4;

        if (averageB > 200) {
            averageR = 200;
            averageB = 0;
            averageG = 0;
        }
        out.r = averageR;
        out.g = averageG;
        out.b = averageB;
    }

    return out;
}

// Using a single source pattern
void processImage(rs_allocation inputImage, rs_allocation outputImage) {
    imageWidth = rsAllocationGetDimX(inputImage);
    imageHeight = rsAllocationGetDimY(inputImage);

    rs_allocation temp = rsCreateAllocation_uchar4(imageWidth, imageHeight);
    rsForEach(neighbors, inputImage, outputImage);
}