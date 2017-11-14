package cn.com.wh.ring.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import cn.com.wh.ring.R;


/**
 * Created by Hui on 2016/3/11.
 */
public class CircleImageView extends ImageView {
    private PorterDuffXfermode xFermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private final Paint mPaint = new Paint();
    private final Paint mBorderPaint = new Paint();
    private Rect mSrcBitmap = new Rect();
    private Rect mDstBitmap = new Rect();

    private int mBorderColor = Color.BLACK;
    private int mBorderWidth = 0;
    private boolean isPressed = false;
    private boolean isFilter = true;
    private ColorMatrixColorFilter mColorMatrixColorFilter;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);

        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width, 0);
        mBorderColor = a.getColor(R.styleable.CircleImageView_civ_border_color, Color.BLACK);

        a.recycle();

        mBorderPaint.setStyle(Paint.Style.STROKE);
        setBorderWidth(mBorderWidth);
        setBorderColor(mBorderColor);
        setFocusable(true);
        // 生成色彩矩阵
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.5F, 0, 0, 0, 0,
                0, 0.5F, 0, 0, 0,
                0, 0, 0.5F, 0, 0,
                0, 0, 0, 1, 0,
        });
        mColorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
    }

    public void setFilterAble(boolean isFilter){
        this.isFilter = isFilter;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获取当前控件的 drawable
        Drawable drawable = getDrawable();

        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return;
        }
        // 这里 get 回来的宽度和高度是当前控件相对应的宽度和高度（在 xml 设置）
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        mPaint.reset();
        // 颜色设置
        mPaint.setColor(0xff424242);
        // 设置颜色滤镜
        if (isFilter)
            mPaint.setColorFilter( isPressed ? mColorMatrixColorFilter : null);
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 获取 bitmap，即传入 imageview 的 bitmap
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        // 标志
        int saveFlags = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, saveFlags);

        // 画遮罩，画出来就是一个和空间大小相匹配的圆
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() - 2 * mBorderWidth) / 2, mPaint);
        mPaint.setXfermode(xFermode);

        // 空间的大小 /bitmap 的大小 =bitmap 缩放的倍数
        float scaleWidth = ((float) getWidth() - 2 * mBorderWidth) / bitmap.getWidth();
        float scaleHeight = ((float) getHeight() - 2 * mBorderWidth) / bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        //bitmap 缩放
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        //draw 上去
        mSrcBitmap.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
        mDstBitmap.set(mBorderWidth, mBorderWidth,getWidth() - mBorderWidth, getHeight() - mBorderWidth);
        canvas.drawBitmap(bitmap, mSrcBitmap, mDstBitmap, mPaint);
        canvas.restore();
        if (mBorderWidth > 0) {
            mBorderPaint.setAntiAlias(true);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2,
                    (getWidth() - mBorderWidth) / 2, mBorderPaint);
        }

        bitmap.recycle();
    }

    public void setBorderWidth(int width) {
        mBorderWidth = width;
        mBorderPaint.setStrokeWidth(mBorderWidth);
        invalidate();
    }

    public void setBorderColor(@ColorInt int color) {
        mBorderColor = color;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (isFilter) {
                    isPressed = true;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                performClick();
            case MotionEvent.ACTION_CANCEL:
                if (isFilter) {
                    isPressed = false;
                    invalidate();
                }
                break;
            default:
                break;
        }
        return true;
    }
}