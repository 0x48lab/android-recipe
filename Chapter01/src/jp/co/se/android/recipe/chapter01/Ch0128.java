package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;

public class Ch0128 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0128_main);

        // �ݒ肵����������2010�N6��30���Ƃ���
        int year = 2010;
        int month = 6 - 1;// 0����n�܂�̂�-1����
        int day = 30;

        // ���t���Z�b�g
        DatePicker dp = (DatePicker) findViewById(R.id.DatePicker);
        dp.updateDate(year, month, day);
    }
}
