package jp.co.se.android.recipe.chapter02;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Ch0205 extends ListActivity {
    /***
     * �C���f�b�N�X�f�[�^
     * 
     * @author yokmama
     * 
     */
    private class BindData {
        String title;
        String line1;
        String line2;

        public BindData(String string0, String string1, String string2) {
            this.title = string0;
            this.line1 = string1;
            this.line2 = string2;
        }
    }

    // �C���f�b�N�X��\�����邽�߂̃T���v���f�[�^
    private BindData[] INDEX_DATA = new BindData[] {
            new BindData("�^�C�g���P", null, null),
            new BindData(null, "foo", "bar"),
            new BindData("�^�C�g��2", null, null),
            new BindData(null, "hoge", "fuga"),
            new BindData(null, "null", "po"),
            new BindData("�^�C�g��3", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("�^�C�g��4", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("�^�C�g��5", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("�^�C�g��6", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("�^�C�g��7", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("�^�C�g��8", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("�^�C�g��9", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"),
            new BindData("�^�C�g��10", null, null),
            new BindData(null, "hoge", "hoge"),
            new BindData(null, "null", "po"), };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0205_main);

        // �C���f�b�N�X�f�[�^�̒ǉ�
        List<BindData> list = new ArrayList<BindData>();
        for (int i = 0; i < INDEX_DATA.length; i++) {
            list.add(INDEX_DATA[i]);
        }

        // �C���f�b�N�X�\���̃A�_�v�^�[�𐶐�
        Ch0205Adapter adapter = new Ch0205Adapter(this, list);

        // �A�_�v�^�[��ݒ�
        setListAdapter(adapter);
    }

    /***
     * ���X�g�����\���̂���View��ێ����߂̃N���X
     * 
     * @author yokmama
     * 
     */
    private class ViewHolder {
        TextView title;
        TextView line1;
        TextView line2;
    }

    private class Ch0205Adapter extends ArrayAdapter<BindData> {
        private LayoutInflater mInflater;

        public Ch0205Adapter(Context context, List<BindData> objects) {
            super(context, 0, objects);
            this.mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public boolean isEnabled(int position) {
            // �I��s�ɂ���
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            // ���X�g�A�C�e���\���p�̃��C�A�E�g��ǂݍ��ݐ���
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.ch0205_list_item,
                        parent, false);
                // ���X�g�̕\�������������邽�߁AView�ێ��p�N���X�𐶐���Tag�ɐݒ�
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.line1 = (TextView) convertView.findViewById(R.id.line1);
                holder.line2 = (TextView) convertView.findViewById(R.id.line2);
                convertView.setTag(holder);
            } else {
                // View�ێ��p�̃C���X�^���X��Tag����擾
                holder = (ViewHolder) convertView.getTag();
            }

            // Adapter�ɐݒ肳��Ă��郊�X�g����BindData���擾
            BindData data = getItem(position);

            if (getItem(position).title != null) {
                // �C���f�b�N�X�p�̃C���f�b�N�X�f�[�^�Ȃ�C���f�b�N�X�^�C�g����\��
                holder.title.setVisibility(View.VISIBLE);
                holder.title.setText(data.title);
                holder.line1.setVisibility(View.GONE);
                holder.line2.setVisibility(View.GONE);
                // line1,2
            } else {
                // �@�C���f�b�N�X�p�̃f�[�^�łȂ��ꍇ�̓e�L�X�g������\��
                holder.title.setVisibility(View.GONE);
                holder.line1.setVisibility(View.VISIBLE);
                holder.line1.setText(data.line1);
                holder.line2.setVisibility(View.VISIBLE);
                holder.line2.setText(data.line2);
            }
            return convertView;
        }
    }
}