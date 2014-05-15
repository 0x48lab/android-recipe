package jp.co.se.android.recipe.chapter02;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

public class Ch0202 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0202_main);
        GridView gridView = (GridView) findViewById(R.id.gridView);

        // �A�_�v�^�̍쐬
        ListAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, WEEK);
        // �A�_�v�^�̐ݒ�
        gridView.setAdapter(adapter);
    }

    // ListView �ɕ\�������镶����
    private static final String[] WEEK = new String[] { "��", "��", "��", "��",
            "��", "�y", "��" };
}