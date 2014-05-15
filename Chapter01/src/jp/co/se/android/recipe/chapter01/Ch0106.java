package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Ch0106 extends Activity {
    private Handler mHandler = new Handler();
    private int value = 0;
    private ProgressBar mProgress;
    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0106_main);

        mProgress = (ProgressBar) findViewById(R.id.progressbar);
        mTextView = (TextView) findViewById(R.id.textview);

        // �v���O���X�̒l�𑝂₵����(�f���\���p)
        mHandler.post(addProgress);
    }

    @Override
    protected void onDestroy() {
        // ���s���̃n���h���[��j��
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    /**
     * �v���O���X�̒l��10ms���ɑ���������X���b�h.
     */
    private Runnable addProgress = new Runnable() {
        @Override
        public void run() {
            value++;
            if (value > 100) {
                value = 0;
            }
            mProgress.setProgress(value);
            mTextView.setText(value + "%");
            mHandler.postDelayed(addProgress, 10);
        }
    };
}
