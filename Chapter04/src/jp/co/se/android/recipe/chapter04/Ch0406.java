package jp.co.se.android.recipe.chapter04;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class Ch0406 extends Activity {

    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0406_main);

        findViewById(R.id.launchActivity).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // �Ăяo�����Intent���쐬
                        final Intent intent = new Intent(Ch0406.this,
                                Ch0406Sub.class);
                        // Intent��KEY_NAME�̃L�[�ŕ������ݒ�
                        EditText inputString = (EditText) findViewById(R.id.inputString);
                        String value = inputString.getText().toString();
                        intent.putExtra("key_name", value);

                        // �߂�l���擾�ł���Ăяo�����@��Activity���J�n
                        startActivityForResult(intent, REQUEST_CODE);

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // startActivityForResult���s���̈����ɐݒ肵��RequestCode���r
        if (requestCode == REQUEST_CODE) {
            // Activity�I�����̃t���O�𔻒�
            if (resultCode == RESULT_OK) {
                // �߂�l�Ƃ��Đݒ肳�ꂽKEY_NAME�̒l���擾
                String value = data.getStringExtra("key_name");
                EditText result = (EditText) findViewById(R.id.result);
                result.setText(value);
            }
        }
    }
}
