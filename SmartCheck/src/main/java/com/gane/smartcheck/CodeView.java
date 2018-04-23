package com.gane.smartcheck;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.camera2.CameraManager;
import android.util.AttributeSet;
import android.view.View;

public class CodeView extends View {
    private Paint paint = null;
    private int maskColor;
    private int resultColor = 0;
    private int frameColor = 0;
    private int laserColor = 0;
    private int resultPointColor = 0;
    private int green = 0;
    /**
     * 刷新界面的时间
     */
    private static final long ANIMATION_DELAY = 10L;

    /**
     * 中间滑动线的最顶端位置
     */
    private int slideTop;

    /**
     * 中间滑动线的最底端位置
     */
    private int slideBottom;
    /**
     * 中间那条线每次刷新移动的距离
     */
    private static final int SPEEN_DISTANCE = 5;

    public CodeView(Context context) {
        super(context);
        init(context);
    }

    public CodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 初始化这些曾经为性能而不是叫他们在ondraw()每次。
        paint = new Paint();
        Resources resources = getResources();
//        maskColor = resources.getColor(R.color.viewfinder_mask);
//        resultColor = resources.getColor(R.color.result_view);
//        frameColor = resources.getColor(R.color.viewfinder_frame);
//        laserColor = resources.getColor(R.color.viewfinder_laser);
//        resultPointColor = resources.getColor(R.color.possible_result_points);
        green = Color.GREEN;

        init(context);
    }

    public CodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //中间的扫描框，你要修改扫描框的大小，去CameraManager里面修改
        Rect frame = new Rect(50, 50, 250, 250);
        if (frame == null) {
            return;
        }
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // 画的外部（即外部框架矩形）黑暗
        paint.setColor(maskColor);
        //
        /**
         * 想要调节二维码扫描的速度 就要在CameraManager里面调节
         *   private static final int MIN_FRAME_WIDTH = 240;
         private static final int MIN_FRAME_HEIGHT = 240;
         private static final int MAX_FRAME_WIDTH = 675;
         private static final int MAX_FRAME_HEIGHT = 800;
         这四个参数的大小
         然后通过Rect frame = CameraManager.get().getFramingRect();获取相机扫描区域的大小
         如果只是单纯的用相机的高宽去设置扫描二维码区域会发现扫描比较快 但是二维码扫描区域太大
         代码是这样的
         // 画的外部（即外部框架矩形）黑暗
         paint.setColor(resultBitmap != null ? resultColor : maskColor);
         canvas.drawRect(0, 0, width, frame.top, paint);
         canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
         canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
         canvas.drawRect(0, frame.bottom + 1, width, height, paint);
         画出了是和相机一样大小的边框
         但是如果为了扫描快一些就要调大相机区域 也要重新画出扫描区域
         就要自己画一个比相机小的正方形的扫描区域
         */
        //画一个方形 获取相机宽度
        int x = frame.right - frame.left;
        //上面阴影部分高度 x=height-2*top; 算出top
        int top = (height - x) / 2;
        //获取下面阴影部分的在y轴的坐标
        int botton = x + top;
        canvas.drawRect(0, 0, width, top, paint);//最上面的一块透明的背景
        canvas.drawRect(0, top, frame.left, botton, paint);//左边的一块透明的背景
        canvas.drawRect(frame.right + 1, top, width, botton, paint);//右边的一块透明背景
        canvas.drawRect(0, botton, width, height, paint);//最下面的一块透明背景

        //画四个角上面的绿线
        paint.setColor(green);
        paint.setStrokeWidth((float) 3.0);              //线宽
        paint.setStyle(Paint.Style.FILL);
        canvas.drawLine(frame.left - 2, top - 2, frame.left + 50, top - 2, paint);//左上角横着的线
        canvas.drawLine(frame.left - 2, top, frame.left - 2, top + 50, paint);//左上角竖着的线

        canvas.drawLine(frame.left - 2, botton - 50, frame.left - 2, botton, paint);//左下角竖着的线
        canvas.drawLine(frame.left - 2, botton + 2, frame.left + 52, botton + 2, paint);//左下角横着的线

        canvas.drawLine(frame.right - 50, top - 2, frame.right + 2, top - 2, paint);//右上角横着的线
        canvas.drawLine(frame.right + 2, top, frame.right + 2, top + 50, paint);//右上角竖着的线


        canvas.drawLine(frame.right + 2, botton - 50, frame.right + 2, botton + 2, paint);//右下角竖着的线
        canvas.drawLine(frame.right - 50, botton + 2, frame.right, botton + 2, paint);//右下角横着的线

        //绘制中间的线,每次刷新界面，中间的线往下移动SPEEN_DISTANCE
        slideTop += SPEEN_DISTANCE;
        if (slideTop >= botton) {
            slideTop = top;
        }
        if (slideTop >= top) {
            //绘制中间的线
            canvas.drawLine(frame.left, slideTop, frame.right, slideTop, paint);//中间的横线
        } else {
            canvas.drawLine(frame.left, top + x / 2, frame.right, top + x / 2, paint);//中间的横线
        }

        //只刷新扫描框的内容，其他地方不刷新
        postInvalidateDelayed(ANIMATION_DELAY, frame.left, top,
                frame.right, botton);
    }
}