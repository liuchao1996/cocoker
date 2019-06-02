package com.cocoker.enums;

import net.coobird.thumbnailator.geometry.Position;

import java.awt.*;

/**
 * @Description:
 * @Author: y
 * @CreateDate: 2019/5/11 9:58 PM
 * @Version: 1.0
 */

/**
 * calculate
 * Point calculate(int enclosingWidth,
 * int enclosingHeight,
 * int width,
 * int height,
 * int insetLeft,
 * int insetRight,
 * int insetTop,
 * int insetBottom)
 * Calculates the position of an object enclosed by an enclosing object.
 * Parameters:
 * enclosingWidth - The width of the enclosing object that is to contain the enclosed object.
 * enclosingHeight - The height of the enclosing object that is to contain the enclosed object.
 * width - The width of the object that is to be placed inside an enclosing object.
 * height - The height of the object that is to be placed inside an enclosing object.
 * insetLeft - The inset on the left-hand side of the object to be enclosed.
 * insetRight - The inset on the right-hand side of the object to be enclosed.
 * insetTop - The inset on the top side of the object to be enclosed.
 * insetBottom - The inset on the bottom side of the object to be enclosed.
 * Returns:
 * The position to place the object.
 */
public enum Positions implements Position {

    CUSTOM_UPPER {
        public Point calculate(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
            int var9 = var1 / 2 - var3 / 2;
            int var10 = var2 / 2 - var4 ;
            return new Point(var9, var10);
        }
    },

}
