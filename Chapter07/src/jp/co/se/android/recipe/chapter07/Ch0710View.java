package jp.co.se.android.recipe.chapter07;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("NewApi")
public class Ch0710View extends View {
    private int DIVISIONS = 16;
    private int HEIGHT_HINT = 10;
    private int WIDTH_HINT = 50;
    private int BAR_COLOR = Color.RED;

    private byte[] mFFTBytes;
    protected float[] mFFTPoints;
    private Rect mRect = new Rect();
    private Visualizer mVisualizer;
    private Paint mPaint;
    private Bitmap mCanvasBitmap;
    private Canvas mCanvas;

    public Ch0710View(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        mFFTBytes = null;

        // Barの塗るPaintを作成
        mPaint = new Paint();
        mPaint.setStrokeWidth(WIDTH_HINT);
        mPaint.setAntiAlias(true);
        mPaint.setColor(BAR_COLOR);
    }

    public Ch0710View(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Ch0710View(Context context) {
        this(context, null, 0);
    }

    public void attach(MediaPlayer mediaplayer) {
        // Visualizer生成し、MediaPlayerにアタッチ
        mVisualizer = new Visualizer(mediaplayer.getAudioSessionId());
        // キャプチャサイズを設定
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

        // キャプチャリスナーを設定
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    @Override
                    public void onWaveFormDataCapture(Visualizer visualizer,
                            byte[] bytes, int samplingRate) {
                    }

                    @Override
                    public void onFftDataCapture(Visualizer visualizer,
                            byte[] bytes, int samplingRate) {
                        // FFTデータを設定
                        mFFTBytes = bytes;
                        // 画面を再描画
                        invalidate();
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, true);

        // Visualizerを有効
        mVisualizer.setEnabled(true);
    }

    public void deatach() {
        // Visualizerを解放
        mVisualizer.release();
        mVisualizer = null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mRect.set(0, 0, getWidth(), getHeight());

        if (mCanvasBitmap == null) {
            mCanvasBitmap = Bitmap.createBitmap(canvas.getWidth(),
                    canvas.getHeight(), Config.ARGB_8888);
        }
        if (mCanvas == null) {
            mCanvas = new Canvas(mCanvasBitmap);
        }

        if (mFFTBytes != null) {
            drawVisualizer(mCanvas, mFFTBytes, mRect);
        }

        canvas.drawBitmap(mCanvasBitmap, 0, 0, null);
    }

    void drawVisualizer(Canvas canvas, byte[] bytes, Rect rect) {
        if (mFFTPoints == null || mFFTPoints.length < bytes.length * 4) {
            mFFTPoints = new float[bytes.length * 4];
        }

        // 画面をクリアする
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        // 周波数スペクトルの計算
        for (int i = 0; i < bytes.length / DIVISIONS; i++) {
            mFFTPoints[i * 4] = i * 4 * DIVISIONS;
            mFFTPoints[i * 4 + 2] = i * 4 * DIVISIONS;
            byte rfk = bytes[DIVISIONS * i];
            byte ifk = bytes[DIVISIONS * i + 1];
            float magnitude = (rfk * rfk + ifk * ifk);
            int dbValue = (int) (10 * Math.log10(magnitude));

            mFFTPoints[i * 4 + 1] = rect.height();
            mFFTPoints[i * 4 + 3] = rect.height() - dbValue * HEIGHT_HINT;
        }

        // 周波数スペクトルを描画
        canvas.drawLines(mFFTPoints, mPaint);
    }
}
