package jp.co.se.android.recipe.chapter01;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class Ch0121 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0121_main);

        // ���̓T�W�F�X�g�p�̃��X�g�𐶐�
        List<String> colorList = new ArrayList<String>();
        colorList.add("�D���ȐF�͐�");
        colorList.add("�D���ȐF�͐�");
        colorList.add("�D���ȐF�͗�");

        // ���̓T�W�F�X�g���Z�b�g
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        SuggestAdapter adapter = new SuggestAdapter(this, colorList);
        textView.setAdapter(adapter);
    }

    /**
     * ���̓T�W�F�X�g�\���Ɏg�p����A�_�v�^.
     */
    private class SuggestAdapter extends ArrayAdapter<String> implements
            SpinnerAdapter {
        private LayoutInflater mInflator;
        private List<String> mItems;

        public SuggestAdapter(Context context, List<String> objects) {
            super(context, R.layout.ch0121_item_suggest, objects);
            mInflator = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mItems = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = mInflator.inflate(R.layout.ch0121_item_suggest,
                        null, false);
                holder = new ViewHolder();
                holder.text = (TextView) convertView
                        .findViewById(R.id.spinnerText);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // �T�W�F�X�g�����Z�b�g
            holder.text.setText(mItems.get(position));

            return convertView;
        }

        private class ViewHolder {
            TextView text;
        }
    }
}