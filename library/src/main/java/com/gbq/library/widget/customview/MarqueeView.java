package com.gbq.library.widget.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.gbq.library.R;

/**
 * 类说明：自定义滚动控件/跑马灯效果
 *          注：退出页面的时候要调用stopScroll()
 * Author: Kuzan
 * Date: 2017/8/25 9:14.
 */
public class MarqueeView extends SurfaceView implements SurfaceHolder.Callback {
    private final static String TAG = "MarqueeView";
    public Context  mContext;
    /**字体大小*/
    private float mTextSize = 100;
    /**字体的颜色*/
    private int mTextColor = Color.RED;
    /**是否重复滚动*/
    private boolean mIsRepeat;
    /**开始滚动的位置  0(start)是从最左面开始 1(end)是从最末尾开始*/
    private int mStartPoint;
    /**滚动方向 0(left)左向右滚动 1(right)右向左滚动*/
    private int mDirection;
    /**滚动速度*/
    private int mSpeed;

    private SurfaceHolder mHolder;
    /**字体画笔*/
    private TextPaint mTextPaint;

    private MarqueeViewThread mThread;

    private String margueeString;
    /**字体长度*/
    private int textWidth = 0;
    /**字体高度*/
    private int textHeight = 0;
    /**阴影颜色*/
    private int ShadowColor = Color.BLACK;
    /**当前x的位置*/
    public int currentX = 0;
    /**每一步滚动的距离*/
    public int sepX = 5;

    private int mViewWidth;
    private boolean isInit;
    /**滚动回调*/
    private OnMarqueeListener mListener;

    public MarqueeView(Context context) {
        this(context, null);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
        initView(attrs, defStyleAttr);
    }

    private void initView(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeView, defStyleAttr, 0);
        mTextColor = a.getColor(R.styleable.MarqueeView_textColor, Color.BLACK);
        mTextSize = a.getDimension(R.styleable.MarqueeView_textSize, 48);
        mIsRepeat = a.getBoolean(R.styleable.MarqueeView_isRepeat, false);
        mStartPoint = a.getInt(R.styleable.MarqueeView_startPoint, 0);
        mDirection = a.getInt(R.styleable.MarqueeView_direction, 0);
        mSpeed = a.getInt(R.styleable.MarqueeView_speed, 20);
        a.recycle();

        mHolder = this.getHolder();
        mHolder.addCallback(this);
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        setZOrderOnTop(true);   // 使surfaceView放到最顶层
        getHolder().setFormat(PixelFormat.TRANSLUCENT); // 使窗口支持透明度
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mHolder = surfaceHolder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if (mThread != null) {
            mThread.isRun = true;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mThread != null) {
            synchronized (this) {
                mThread.isRun = false;
            }
        }
    }

    /**
     * 设置文字
     * */
    public void setText(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            margueeString = msg;
            if (isInit) {
                measurementsText();
            }
        }
    }

    /**
     * 计算文字
     * */
    protected void measurementsText() {
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setStrokeWidth(0.5f);
        //mTextPaint.setFakeBoldText(true);
        // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)
//        mTextPaint.setShadowLayer(5, 3, 3, ShadowColor);
        textWidth = (int) mTextPaint.measureText(margueeString);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        textHeight = (int) fontMetrics.bottom;

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        if (mStartPoint == 0) {
            currentX = 0;
        } else {
            currentX = width - getPaddingLeft() - getPaddingRight();
        }

        if (textWidth > mViewWidth) {
            // 开始滚动
            mHandler.sendEmptyMessageDelayed(START_SCROLL, 1000);
        } else {
            // 不滚动
            mHandler.sendEmptyMessageDelayed(UN_SCROLL, 200);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        isInit = true;
        if (!TextUtils.isEmpty(margueeString)) {
            measurementsText();
        }
    }

    /**
     * 开始滚动
     */
    public void startScroll() {
        Log.e(TAG, "startScroll");
        if (mThread != null && mThread.isRun) {
            return;
        }
        mThread = new MarqueeViewThread(mHolder);//创建一个绘图线程
        mThread.start();
    }

    /**
     * 停止滚动
     */
    public void stopScroll() {
        Log.e(TAG, "stopScroll");
        if (mThread != null) {
            mThread.isRun = false;
            mThread.interrupt();
        }
        mThread = null;
    }

    /**
     * 重置
     * */
    public void reset() {
        if (textWidth > mViewWidth) {
            // 开始滚动
            stopScroll();
            mHandler.sendEmptyMessageDelayed(START_SCROLL, 1000);
        } else {
            // 不滚动
            mHandler.sendEmptyMessageDelayed(UN_SCROLL, 200);
        }
    }

    /**
     * 滚动线程
     */
    class MarqueeViewThread extends Thread {
        private SurfaceHolder threadHolder;
        /**是否在运行*/
        public boolean isRun ;

        public  MarqueeViewThread(SurfaceHolder holder) {
            this.threadHolder = holder;
            isRun = true;
        }

        /**
         * 画字
         * */
        public void onDraw() {
            try {
                synchronized (threadHolder) {
                    if (TextUtils.isEmpty(margueeString)) {
                        Thread.sleep(1000);//睡眠时间为1秒
                        return;
                    }
                    Canvas canvas = threadHolder.lockCanvas();
                    int paddingLeft = getPaddingLeft();
                    int paddingTop = getPaddingTop();
                    int paddingRight = getPaddingRight();
                    int paddingBottom = getPaddingBottom();

                    int contentWidth = getWidth() - paddingLeft - paddingRight;
                    int contentHeight = getHeight() - paddingTop - paddingBottom;

                    int centerYLine = paddingTop + contentHeight / 2;   //中心线

                    if(mDirection == 0) {//向左滚动
                        if (currentX <= -textWidth){
                            if (!mIsRepeat){//如果是不重复滚动
                                mHandler.sendEmptyMessage(ROLL_OVER);
                            }
                            currentX = contentWidth;
                        } else {
                            currentX -= sepX;
                        }
                    } else {//  向右滚动
                        if (currentX >= contentWidth) {
                            if (!mIsRepeat) {//如果是不重复滚动
                                mHandler.sendEmptyMessage(ROLL_OVER);
                            }
                            currentX = -textWidth;
                        } else {
                            currentX += sepX;
                        }
                    }

                    if(canvas!=null) {
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
                    }

                    canvas.drawText(margueeString, currentX, centerYLine + dip2px(getContext(), textHeight) / 2, mTextPaint);
                    threadHolder.unlockCanvasAndPost(canvas);//结束锁定画图，并提交改变。

                    int a = textWidth/margueeString.trim().length();
                    int b = a/sepX;
                    int c = mSpeed/b==0?1:mSpeed/b;

                    Thread.sleep(c);//睡眠时间为移动的频率
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (isRun) {
                onDraw();
            }
        }
    }

    /**滑动结束*/
    private static final int ROLL_OVER = 100;
    /**开始滑动*/
    private static final int START_SCROLL = 101;
    /**不滑动*/
    private static final int UN_SCROLL = 102;
    Handler mHandler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ROLL_OVER:
                    stopScroll();
                    if (mListener != null) {
                        mListener.onRollOver();
                    }
                    break;
                case START_SCROLL:
                    startScroll();
                    break;
                case UN_SCROLL:
                    Canvas canvas = null;
                    try {
                        canvas = mHolder.lockCanvas();
                        int paddingTop = getPaddingTop();
                        int paddingBottom = getPaddingBottom();

                        int contentHeight = getHeight() - paddingTop - paddingBottom;

                        int centerYLine = paddingTop + contentHeight / 2;//中心线

                        if (canvas != null) {
                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色
                            canvas.drawText(margueeString, 0, centerYLine + dip2px(getContext(), textHeight) / 2, mTextPaint);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    } finally {
                        try {
                            mHolder.unlockCanvasAndPost(canvas);//结束锁定画图，并提交改变。
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    break;
            }
        }
    };

    public int getTextWidth() {
        return textWidth > 0 ? textWidth : 0;
    }

    public int getViewWidth() {
        return mViewWidth > 0 ? mViewWidth : 0;
    }

    /**
     * dip转换为px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 滚动回调
     */
    public interface OnMarqueeListener {
        void onRollOver();//滚动完毕
    }

    public void setOnMarqueeListener(OnMarqueeListener listener){
        this.mListener = listener;
    }
}
