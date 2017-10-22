package com.krishnchinya.personalhealthmonitoringsystem.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by KrishnChinya on 2/26/17.
 */

public class CircleImage extends BitmapTransformation {

    public CircleImage(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return cropCircle(pool,toTransform);
    }

    private static Bitmap cropCircle(BitmapPool pool,Bitmap source)
    {
        if(source==null)
            return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size)/2;
        int y = (source.getHeight() - size)/2;

        Bitmap img = Bitmap.createBitmap(source,x,y,size,size);

        Bitmap result = pool.get(size,size, Bitmap.Config.ARGB_8888);
        if(result == null)
        {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(img);

        Paint paint = new Paint();
        paint.setShader(new BitmapShader(img,BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);

        float r = size/2f;
        canvas.drawCircle(r,r,r,paint);
        return result;

    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
