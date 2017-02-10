/*
 * Copyright (C) 2011-2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This file is auto-generated. DO NOT MODIFY!
 * The source Renderscript file: /home/jason/Desktop/Projects/AndroidProjects/RenderscriptHelloWorld/app/src/main/rs/imageRipple.rs
 */

package com.example.jason.renderscripthelloworld;

import android.content.res.Resources;
import android.support.v8.renderscript.*;
import com.example.jason.renderscripthelloworld.imageRippleBitCode;

/**
 * @hide
 */
public class ScriptC_newImageRipple extends ScriptC {
    private static final String __rs_resource_name = "imageripple";
    // Constructor
    public  ScriptC_newImageRipple(RenderScript rs) {
        super(rs,
                __rs_resource_name,
                imageRippleBitCode.getBitCode32(),
                imageRippleBitCode.getBitCode64());
        __F32 = Element.F32(rs);
        mExportVar_minMult = 0.f;
        mExportVar_maxMult = 1.5f;
        __U8_4 = Element.U8_4(rs);
    }

    private Element __F32;
    private Element __I32;
    private Element __U8_4;
    private FieldPacker __rs_fp_F32;
    private final static int mExportVarIdx_centerX = 0;
    private float mExportVar_centerX;

    public ScriptC_newImageRipple(RenderScript rs, Resources resources, int id) {
        super(rs, resources, id);
        __F32 = Element.F32(rs);
        __I32 = Element.I32(rs);
        __U8_4 = Element.U8_4(rs);
    }

    public synchronized void set_centerX(float v) {
        setVar(mExportVarIdx_centerX, v);
        mExportVar_centerX = v;
    }

    public float get_centerX() {
        return mExportVar_centerX;
    }

    public Script.FieldID getFieldID_centerX() {
        return createFieldID(mExportVarIdx_centerX, null);
    }

    private final static int mExportVarIdx_centerY = 1;
    private float mExportVar_centerY;
    public synchronized void set_centerY(float v) {
        setVar(mExportVarIdx_centerY, v);
        mExportVar_centerY = v;
    }

    public float get_centerY() {
        return mExportVar_centerY;
    }

    public Script.FieldID getFieldID_centerY() {
        return createFieldID(mExportVarIdx_centerY, null);
    }

    private final static int mExportVarIdx_minRadius = 2;
    private float mExportVar_minRadius;
    public synchronized void set_minRadius(float v) {
        setVar(mExportVarIdx_minRadius, v);
        mExportVar_minRadius = v;
    }

    public float get_minRadius() {
        return mExportVar_minRadius;
    }

    public Script.FieldID getFieldID_minRadius() {
        return createFieldID(mExportVarIdx_minRadius, null);
    }

    private final static int mExportVarIdx_scalar = 3;
    private float mExportVar_scalar;
    public synchronized void set_scalar(float v) {
        setVar(mExportVarIdx_scalar, v);
        mExportVar_scalar = v;
    }

    public float get_scalar() {
        return mExportVar_scalar;
    }

    public Script.FieldID getFieldID_scalar() {
        return createFieldID(mExportVarIdx_scalar, null);
    }

    private final static int mExportVarIdx_damper = 4;
    private float mExportVar_damper;
    public synchronized void set_damper(float v) {
        setVar(mExportVarIdx_damper, v);
        mExportVar_damper = v;
    }

    public float get_damper() {
        return mExportVar_damper;
    }

    public Script.FieldID getFieldID_damper() {
        return createFieldID(mExportVarIdx_damper, null);
    }

    private final static int mExportVarIdx_frequency = 5;
    private float mExportVar_frequency;
    public synchronized void set_frequency(float v) {
        setVar(mExportVarIdx_frequency, v);
        mExportVar_frequency = v;
    }

    public float get_frequency() {
        return mExportVar_frequency;
    }

    public Script.FieldID getFieldID_frequency() {
        return createFieldID(mExportVarIdx_frequency, null);
    }

    private final static int mExportVarIdx_minMult = 6;
    private float mExportVar_minMult;
    public final static float const_minMult = 0.f;
    public float get_minMult() {
        return mExportVar_minMult;
    }

    public Script.FieldID getFieldID_minMult() {
        return createFieldID(mExportVarIdx_minMult, null);
    }

    private final static int mExportVarIdx_maxMult = 7;
    private float mExportVar_maxMult;
    public final static float const_maxMult = 1.5f;
    public float get_maxMult() {
        return mExportVar_maxMult;
    }

    public Script.FieldID getFieldID_maxMult() {
        return createFieldID(mExportVarIdx_maxMult, null);
    }

    private final static int mExportForEachIdx_root = 0;
    public Script.KernelID getKernelID_root() {
        return createKernelID(mExportForEachIdx_root, 31, null, null);
    }

    public void forEach_root(Allocation ain, Allocation aout) {
        forEach_root(ain, aout, null);
    }

    public void forEach_root(Allocation ain, Allocation aout, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        // check aout
        if (!aout.getType().getElement().isCompatible(__U8_4)) {
            throw new RSRuntimeException("Type mismatch with U8_4!");
        }
        Type t0, t1;        // Verify dimensions
        t0 = ain.getType();
        t1 = aout.getType();
        if ((t0.getCount() != t1.getCount()) ||
                (t0.getX() != t1.getX()) ||
                (t0.getY() != t1.getY()) ||
                (t0.getZ() != t1.getZ()) ||
                (t0.hasFaces()   != t1.hasFaces()) ||
                (t0.hasMipmaps() != t1.hasMipmaps())) {
            throw new RSRuntimeException("Dimension mismatch between parameters ain and aout!");
        }

        forEach(mExportForEachIdx_root, ain, aout, null, sc);
    }

}

