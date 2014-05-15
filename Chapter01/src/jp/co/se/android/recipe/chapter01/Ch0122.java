package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

public class Ch0122 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0122_main);

        // ���C�A�E�g����RatingBar�C���X�^���X����
        final RatingBar ratingbar = (RatingBar) findViewById(R.id.ratingbar);
        // RatingBar�C���X�^���X�Ƀ��X�i�[�ǉ�
        ratingbar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            // ���[�e�B���O���ω������ꍇ
            public void onRatingChanged(RatingBar ratingBar, float rating,
                    boolean fromUser) {
                // �g�[�X�g���b�Z�[�W�\��
                Toast.makeText(Ch0122.this,
                        "New Rating: " + rating + "  /fromUser:" + fromUser,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}
