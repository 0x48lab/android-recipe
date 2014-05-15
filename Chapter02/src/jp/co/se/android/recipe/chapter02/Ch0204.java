package jp.co.se.android.recipe.chapter02;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Ch0204 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0201_main);

        View header = (View) getLayoutInflater().inflate(
                R.layout.ch0204_list_header_item, null);
        View footer = (View) getLayoutInflater().inflate(
                R.layout.ch0204_list_fotter_item, null);

        ListView listView = (ListView) findViewById(R.id.ListView);
        listView.addHeaderView(header);
        listView.addFooterView(footer);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // �A�C�e�����N���b�N���ꂽ�Ƃ��ɌĂяo�����R�[���o�b�N��o�^
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // �w�b�_�[�̎��͉������Ȃ�
                if (position == 0) {
                    return;
                }
            }
        });

        // �A�_�v�^�̍쐬
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, DAYS));
        // �t�H�[�J�X��������Ȃ��悤�ݒ�
        listView.setItemsCanFocus(false);
    }

    // ListView �ɕ\�������镶����
    private static final String[] DAYS = new String[] { "Sunday", "Monday",
            "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday",
            "Thursday", "Friday", "Saturday", "Thursday", "Friday", "Saturday",
            "Thursday", "Friday", "Saturday", };

}
