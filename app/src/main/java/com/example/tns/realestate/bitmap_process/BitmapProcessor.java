package com.example.tns.realestate.bitmap_process;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by TNS on 11/19/2016.
 * This utility factory class provide methods related to effective {@link Bitmap} processing
 */

public class BitmapProcessor {
    private BitmapProcessor() {
    }


    /**
     * To tell the decoder to subsample the image, loading a smaller version into memory,
     * set inSampleSize to true in your BitmapFactory.Options object.
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    /**
     * Decode to find the perfect fit size of {@link Bitmap} for a particular size from a Resource's file
     *
     * @param res       :  Resources from application context
     * @param resId     : id of resource's file
     * @param reqWidth  : width of View need to decode
     * @param reqHeight :  height of View need to decode
     * @return Bitmap with size that fit the size of desired View
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    /**
     * Decode to find the perfect fit size of {@link Bitmap} for a particular size from a Resource's file
     *
     * @param originalBitmap : The original {@link Bitmap} that need to be changed the size
     * @param reqWidth       : width of View need to decode
     * @param reqHeight      :  height of View need to decode
     * @return Bitmap with size that fit the size of desired View
     */
    public static Bitmap decodeSampledBitmapFromOriginalBitmap(Bitmap originalBitmap,
                                                               int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        byte[] bitmapBytes = BitmapToByteArray(originalBitmap);
        BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);
    }

    // convert bitmap to byte array
    private static byte[] BitmapToByteArray(Bitmap value) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        value.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    // convert byte array to bitmap
    private static Bitmap ByteArrayToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


}
