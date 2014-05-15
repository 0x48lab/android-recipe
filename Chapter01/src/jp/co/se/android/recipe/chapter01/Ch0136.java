package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Ch0136 extends Activity {
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0136_main);

        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);

        // �[���I��3�b�Ԃ̃��[�f�B���O��Ԃ��쐬
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                // �v���O���X���\����Ԃɂ���
                pb.setVisibility(View.GONE);
                // ImageView��\����Ԃɂ���
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(R.drawable.ic_launcher);
            }
        }, 3000);
    }
}
