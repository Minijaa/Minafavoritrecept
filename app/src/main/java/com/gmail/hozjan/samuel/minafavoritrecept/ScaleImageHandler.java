package com.gmail.hozjan.samuel.minafavoritrecept;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
// Klass som skalar ned en bild. OBS denna är tagen ur kursboken "Android Programming - The Big Nerd Ranch",
// s.314 av Bill Phillips, Chris Stewart och Kristin Marsicano.
class ScaleImageHandler {
    private static Bitmap getScaleBitMap(String path, int destWidth, int destHeight){
        //Läs in bildens storlek.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        //Räkna ut hur mycket bilden ska skalas ner
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth){
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth / destWidth;

            inSampleSize = Math.round(heightScale > widthScale ? heightScale : widthScale);
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        //Läst in och skapa en bitmap.
        return BitmapFactory.decodeFile(path, options);
    }

    // Kollar hur stor skärmen är och skalar ned bilden till den storleken.
    static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaleBitMap(path, size.x, size.y);
    }
}