package com.avaroom.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * View cho phép hiển thị ảnh và kéo/chỉnh kích thước khung chữ nhật khoanh vùng thẻ sinh viên.
 */
public class CropOverlayView extends View {

    private Bitmap bitmap;
    private final RectF imageRect = new RectF();
    private final RectF cropRect = new RectF();

    private final Paint dimPaint = new Paint();
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint handlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static final float HANDLE_RADIUS = 28f;
    private static final float MIN_CROP_SIZE = 120f;

    private int activeMode = 0; // 0: none, 1..4: corners TL, TR, BL, BR, 5: move
    private float lastX, lastY;

    public CropOverlayView(Context context) {
        super(context);
        dimPaint.setColor(0x99000000);
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(4f);
        handlePaint.setColor(0xFF2563EB);
        handlePaint.setStyle(Paint.Style.FILL);
    }

    public void setImageBitmap(@NonNull Bitmap bmp) {
        this.bitmap = bmp;
        computeImageRect();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        computeImageRect();
    }

    private void computeImageRect() {
        if (bitmap == null || getWidth() == 0 || getHeight() == 0) return;
        float vw = getWidth();
        float vh = getHeight();
        float scale = Math.min(vw / bitmap.getWidth(), vh / bitmap.getHeight());
        float dw = bitmap.getWidth() * scale;
        float dh = bitmap.getHeight() * scale;
        float left = (vw - dw) / 2f;
        float top = (vh - dh) / 2f;
        imageRect.set(left, top, left + dw, top + dh);

        // Khung mặc định tỷ lệ thẻ sinh viên (~1.6:1) ở giữa ảnh
        float cw = dw * 0.85f;
        float ch = Math.min(dh * 0.65f, cw / 1.58f);
        float cl = imageRect.centerX() - cw / 2f;
        float ct = imageRect.centerY() - ch / 2f;
        cropRect.set(cl, ct, cl + cw, ct + ch);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap == null) return;
        canvas.drawBitmap(bitmap, null, imageRect, null);

        // Tô tối 4 vùng ngoài khung khoanh
        canvas.drawRect(imageRect.left, imageRect.top, imageRect.right, cropRect.top, dimPaint);
        canvas.drawRect(imageRect.left, cropRect.bottom, imageRect.right, imageRect.bottom, dimPaint);
        canvas.drawRect(imageRect.left, cropRect.top, cropRect.left, cropRect.bottom, dimPaint);
        canvas.drawRect(cropRect.right, cropRect.top, imageRect.right, cropRect.bottom, dimPaint);

        canvas.drawRect(cropRect, borderPaint);
        canvas.drawCircle(cropRect.left, cropRect.top, 14f, handlePaint);
        canvas.drawCircle(cropRect.right, cropRect.top, 14f, handlePaint);
        canvas.drawCircle(cropRect.left, cropRect.bottom, 14f, handlePaint);
        canvas.drawCircle(cropRect.right, cropRect.bottom, 14f, handlePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (bitmap == null) return false;
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                activeMode = detectMode(x, y);
                lastX = x;
                lastY = y;
                return activeMode != 0;
            case MotionEvent.ACTION_MOVE:
                float dx = x - lastX;
                float dy = y - lastY;
                applyDelta(dx, dy);
                lastX = x;
                lastY = y;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                activeMode = 0;
                return true;
        }
        return super.onTouchEvent(event);
    }

    private int detectMode(float x, float y) {
        if (dist(x, y, cropRect.left, cropRect.top) <= HANDLE_RADIUS * 1.6f) return 1;
        if (dist(x, y, cropRect.right, cropRect.top) <= HANDLE_RADIUS * 1.6f) return 2;
        if (dist(x, y, cropRect.left, cropRect.bottom) <= HANDLE_RADIUS * 1.6f) return 3;
        if (dist(x, y, cropRect.right, cropRect.bottom) <= HANDLE_RADIUS * 1.6f) return 4;
        if (cropRect.contains(x, y)) return 5;
        return 0;
    }

    private void applyDelta(float dx, float dy) {
        if (activeMode == 5) {
            float newLeft = Math.max(imageRect.left, Math.min(cropRect.left + dx, imageRect.right - cropRect.width()));
            float newTop = Math.max(imageRect.top, Math.min(cropRect.top + dy, imageRect.bottom - cropRect.height()));
            cropRect.offsetTo(newLeft, newTop);
            return;
        }
        if (activeMode == 1 || activeMode == 3) {
            cropRect.left = Math.max(imageRect.left, Math.min(cropRect.left + dx, cropRect.right - MIN_CROP_SIZE));
        }
        if (activeMode == 2 || activeMode == 4) {
            cropRect.right = Math.min(imageRect.right, Math.max(cropRect.right + dx, cropRect.left + MIN_CROP_SIZE));
        }
        if (activeMode == 1 || activeMode == 2) {
            cropRect.top = Math.max(imageRect.top, Math.min(cropRect.top + dy, cropRect.bottom - MIN_CROP_SIZE));
        }
        if (activeMode == 3 || activeMode == 4) {
            cropRect.bottom = Math.min(imageRect.bottom, Math.max(cropRect.bottom + dy, cropRect.top + MIN_CROP_SIZE));
        }
    }

    private float dist(float x1, float y1, float x2, float y2) {
        return (float) Math.hypot(x1 - x2, y1 - y2);
    }

    @Nullable
    public Bitmap getCroppedBitmap() {
        if (bitmap == null || imageRect.width() <= 0 || imageRect.height() <= 0) return null;
        float scaleX = bitmap.getWidth() / imageRect.width();
        float scaleY = bitmap.getHeight() / imageRect.height();

        int x = Math.max(0, Math.round((cropRect.left - imageRect.left) * scaleX));
        int y = Math.max(0, Math.round((cropRect.top - imageRect.top) * scaleY));
        int w = Math.min(bitmap.getWidth() - x, Math.round(cropRect.width() * scaleX));
        int h = Math.min(bitmap.getHeight() - y, Math.round(cropRect.height() * scaleY));
        if (w <= 0 || h <= 0) return bitmap;
        return Bitmap.createBitmap(bitmap, x, y, w, h);
    }
}
