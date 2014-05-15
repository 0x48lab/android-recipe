package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

public class Ch0119 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0119_main);
        final EditText et = (EditText) findViewById(R.id.restriction);

        // ���[���A�h���X�p�̃t�B���^�[���쐬
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                    Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[0-9a-zA-Z@\\.\\_\\-]+$")) {
                    return source;
                } else {
                    return "";
                }
            }
        };

        // �t�B���^�[���i�[
        InputFilter[] filters = new InputFilter[] { inputFilter };
        // �t�B���^�[��K�p
        et.setFilters(filters);

    }
}
