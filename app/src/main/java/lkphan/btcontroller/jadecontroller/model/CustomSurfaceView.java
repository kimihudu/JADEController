package lkphan.btcontroller.jadecontroller.model;

/**
 * Created by kimiboo on 2017-10-17.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.system.ErrnoException;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import lkphan.btcontroller.jadecontroller.R;

public class CustomSurfaceView extends SurfaceView{

    SurfaceHolder surfaceHolder;
    Bitmap bmpDrawing = null;

    public CustomSurfaceView(Context context) {
        super(context);
        init();
    }

    public CustomSurfaceView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomSurfaceView(Context context,AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setBmpDrawing(Bitmap bitmap){
        bmpDrawing = bitmap;
    }

    private void init(){
        surfaceHolder = getHolder();
//        bmpIcon = BitmapFactory.decodeResource(getResources(),R.drawable.ic_build_black_24dp);

        surfaceHolder.addCallback(new SurfaceHolder.Callback(){

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                setWillNotDraw(false);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder,
                                       int format, int width, int height) {
                // TODO Auto-generated method stub

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // TODO Auto-generated method stub

            }});
    }

    public void drawView(Canvas canvas) {
        postInvalidate();
        canvas = this.getHolder().lockCanvas(null);
        Paint paint = new Paint();
        canvas.drawColor(Color.BLACK);
        canvas.drawBitmap(bmpDrawing,getWidth()/2, getHeight()/2, paint);
        Toast.makeText(getContext(),"drawView",Toast.LENGTH_LONG).show();
        this.getHolder().unlockCanvasAndPost(canvas);

    }

}
