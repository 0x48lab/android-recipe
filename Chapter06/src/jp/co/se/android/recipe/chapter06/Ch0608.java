package jp.co.se.android.recipe.chapter06;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.os.Bundle;
import android.view.View;

public class Ch0608 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
    }

    class MyView extends View {
        Paint mPaint;
        Path mPath;

        public MyView(Context context) {
            super(context);
            // テキストの描画を定義するPaintを生成
            mPaint = new Paint();
            mPaint.setARGB(255, 0, 0, 0);
            mPaint.setTextSize(38);

            // テキストの描画位置を指定するPathを生成
            mPath = new Path();
            mPath.addCircle(300, 300, 100, Direction.CW);
        }

        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            canvas.drawTextOnPath("Hello World", mPath, 300, 20, mPaint);
        }
    }
}
