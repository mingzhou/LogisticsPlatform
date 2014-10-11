/*
 * Copyright (C) 2013 FMSoft (http://www.fmsoft.cn)
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

package org.espier.ios6.ui.utils;

import java.lang.reflect.Method;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class Compatibility {
    // View.setLayerType() was introduced in Honeycomb
    private static Method setLayerTypeMethod = getMethod(View.class, "setLayerType", int.class,
            Paint.class);

    private static Method isHardwareAcceleratedMethod = getMethod(Canvas.class, "isHardwareAccelerated");
    private static Method setOverScrollModeMethod = getMethod(View.class, "setOverScrollMode", int.class);

    private static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getMethod(name, parameterTypes);
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }

    private Compatibility() {
    }

    public static void setOverScrollMode(View view, int mode) {
        try {
            if (setOverScrollModeMethod != null) {
                setOverScrollModeMethod.invoke(view, mode);
            }
        }
        catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public static void disableHardwareAcceleration(View view) {
        try {
            if (setLayerTypeMethod != null) {
                int layerType = 1; // View.LAYER_TYPE_SOFTWARE
                setLayerTypeMethod.invoke(view, layerType, null);
            }
        }
        catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public static boolean isHardwareAccelerated(Canvas canvas) {
        try {
            if (isHardwareAcceleratedMethod != null) {
                return (Boolean) isHardwareAcceleratedMethod.invoke(canvas);
            }
        }
        catch (Exception ignored) {
            ignored.printStackTrace();
        }
        return false;
    }
}
