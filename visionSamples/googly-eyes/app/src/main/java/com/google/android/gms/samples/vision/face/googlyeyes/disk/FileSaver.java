package com.google.android.gms.samples.vision.face.googlyeyes.disk;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by fmatos on 19/10/2016.
 */

public class FileSaver extends ContextWrapper {


    private static final String TAG = FileSaver.class.getCanonicalName();

    public FileSaver(Context base) {
        super(base);
    }

    public void saveImage(View v, String filename) {

        try {
            Bitmap drawingCache = getBitmap(v);
            if (drawingCache != null) {
                drawingCache.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(getFilePath(filename)));
            } else {
                Log.i(TAG, "Null bitmap here");
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG, e.getLocalizedMessage());
        }

    }

    private Bitmap getBitmap(View v) {

        if ( v.getHeight() > 0 && v.getWidth() > 0 ) {
            Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
            v.draw(c);
            return b;
        } else {
            return null;
        }

    }

    public void saveImage(Canvas cv, String filename) {

        Bitmap bitmap = getBitmap(cv);

        saveImage(bitmap,filename);
    }


    public void saveImage(Bitmap bitmap, String filename) {
        try {

            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(getFilePath(filename)));
            } else {
                Log.i(TAG, "Null bitmap here");
            }
        } catch (FileNotFoundException e) {
            Log.i(TAG, e.getLocalizedMessage());
        }

    }

    private Bitmap getBitmap(Canvas  cv) {

        if ( cv.getHeight() > 0 && cv.getWidth() > 0 ) {
            Bitmap b = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(), Bitmap.Config.ARGB_8888);

            cv.setBitmap(b);
//            Canvas c = new Canvas(b);
//            cv.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
//            cv.draw(c);
//            return b;
            return b;
        } else {
            return null;
        }

    }


    public File getFilePath(String filename) {
//        return new File("/sdcard/arun.jpg");

        File file = getAlbumStorageDir(getBaseContext(), "smile");

        file = new File(file, filename);
        Log.i(TAG, "Filename  " + file.getPath());
        return file;
//        return filePath;
    }

    private File getAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.d(TAG, "Directory not created");
        }
        return file;
    }

}
