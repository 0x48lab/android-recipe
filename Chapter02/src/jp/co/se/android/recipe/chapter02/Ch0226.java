package jp.co.se.android.recipe.chapter02;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class Ch0226 extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView���O��Window��ActionBar�\����ݒ�
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.ch0226_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ch0226_menu, menu);

        // ���j���[�̗v�f��ǉ�
        MenuItem actionItem = menu.add("�v���O��������ǉ������A�C�e��");

        // SHOW_AS_ACTION_IF_ROOM: �]�T������Ε\���ASHOW_AS_ACTION_WITH_TEXT: �e�L�X�g�������ɕ\��
        actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        // �A�C�R����ݒ�
        actionItem.setIcon(android.R.drawable.ic_menu_compass);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "�I����������: " + item.getTitle(), Toast.LENGTH_SHORT)
                .show();
        return true;
    }
}