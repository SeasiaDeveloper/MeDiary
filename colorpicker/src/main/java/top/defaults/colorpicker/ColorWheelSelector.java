package top.defaults.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import static top.defaults.colorpicker.Constants.SELECTOR_RADIUS_DP;

public class ColorWheelSelector extends View {

    private final Paint bgWhitePaint;
    private final Paint bgGrayPaint;
    private Paint selectorPaint;
    private float selectorRadiusPx = SELECTOR_RADIUS_DP * 3;
    private PointF currentPoint = new PointF();

    public ColorWheelSelector(Context context) {
        this(context, null);
    }

    public ColorWheelSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorWheelSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        selectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectorPaint.setColor(Color.WHITE);
        selectorPaint.setStyle(Paint.Style.STROKE);
        selectorPaint.setStrokeWidth(25);

        bgGrayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgGrayPaint.setColor(Color.GRAY);
        bgGrayPaint.setStyle(Paint.Style.STROKE);
        bgGrayPaint.setStrokeWidth(1);

        bgWhitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgWhitePaint.setColor(Color.WHITE);
        bgWhitePaint.setStyle(Paint.Style.STROKE);
        bgWhitePaint.setStrokeWidth(6);
    }

    public void setWheelColor(int color) {
        selectorPaint.setColor(color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(currentPoint.x - selectorRadiusPx, currentPoint.y,
                currentPoint.x + selectorRadiusPx, currentPoint.y, selectorPaint);
        canvas.drawLine(currentPoint.x, currentPoint.y - selectorRadiusPx,
                currentPoint.x, currentPoint.y + selectorRadiusPx, selectorPaint);
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx * 0.90f, selectorPaint);
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx * 1.2f, bgWhitePaint);
        canvas.drawCircle(currentPoint.x, currentPoint.y, selectorRadiusPx * 1.3f, bgGrayPaint);
    }

    public void setSelectorRadiusPx(float selectorRadiusPx) {
        this.selectorRadiusPx = selectorRadiusPx;
    }

    public void setCurrentPoint(PointF currentPoint) {
        this.currentPoint = currentPoint;
        invalidate();
    }
}
