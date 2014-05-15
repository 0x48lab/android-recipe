package jp.co.se.android.recipe.chapter15;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class Ch1506 extends Activity implements ViewBinder {
    private ListView mListSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch1506_main);

        mListSongs = (ListView) findViewById(R.id.listSongs);

        loadSongs();
    }

    private void loadSongs() {
        // �O���X�g���[�W��̉��y�t�@�C���̏����R���e���c�v���o�C�_�o�R�Ŏ擾
        Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI,
                null, null, null, null);

        // �擾��������\�����邽�߂�SimpleCursorAdapter�𐶐�
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.ch1506_listrow, cursor, new String[] { Media.TITLE,
                        Media.ARTIST, Media.DURATION }, new int[] {
                        R.id.textTitle, R.id.textArtist, R.id.textTime }, 0);
        // SimpleCursorAdapter�̕\�����J�X�^�}�C�Y���邽��Binder��ݒ�
        adapter.setViewBinder(this);

        // �\���p�̃A�_�v�^�[�Ƃ��Đ�������SimpleCursorAdapter��ListView�ɐݒ�
        mListSongs.setAdapter(adapter);
    }

    @Override
    public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
        int index = cursor.getColumnIndex(Media.DURATION);
        if (index == columnIndex) {
            TextView textTime = (TextView) view;
            long durationMs = cursor.getLong(columnIndex);
            long duration = durationMs / 1000;
            long h = duration / 3600;
            long m = (duration - h * 3600) / 60;
            long s = duration - (h * 3600 + m * 60);
            textTime.setText(String.format("%02d:%02d", m, s));
            return true;
        }
        return false;
    }
}
