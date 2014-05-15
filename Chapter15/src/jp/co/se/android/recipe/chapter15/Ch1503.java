package jp.co.se.android.recipe.chapter15;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class Ch1503 extends Activity {
    private static final String CONFIG_NAME = "appconfig";

    private EditText mEditConfigText;
    private CheckBox mCheckConfigCheck1;
    private CheckBox mCheckConfigCheck2;
    private Spinner mSpinnerConfigSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ch1503_main);

        mEditConfigText = (EditText) findViewById(R.id.editConfigText);
        mCheckConfigCheck1 = (CheckBox) findViewById(R.id.checkConfigCheck1);
        mCheckConfigCheck2 = (CheckBox) findViewById(R.id.checkConfigCheck2);
        mSpinnerConfigSelect = (Spinner) findViewById(R.id.spinnerConfigSelect);

        loadConfig();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveConfig();
    }

    private void loadConfig() {
        // Private��CONFIG_NAME�̃t�@�C�����쐬���ASharedPreferences�C���X�^���X���擾
        SharedPreferences pref = getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);

        // editConfigText���L�[�Ƀe�L�X�g�l���擾
        mEditConfigText.setText(pref.getString("editConfigText", ""));
        // checkConfigCheck1���L�[��Boolean�l���擾
        mCheckConfigCheck1.setChecked(pref.getBoolean("checkConfigCheck1",
                false));
        // checkConfigCheck2���L�[��Boolean�l���擾
        mCheckConfigCheck2.setChecked(pref.getBoolean("checkConfigCheck2",
                false));
        // spinnerConfigSelect���L�[�ɐ����l���擾
        mSpinnerConfigSelect
                .setSelection(pref.getInt("spinnerConfigSelect", 0));
    }

    private void saveConfig() {
        // Private��CONFIG_NAME�̃t�@�C�����쐬���ASharedPreferences�C���X�^���X���擾
        SharedPreferences pref = getSharedPreferences(CONFIG_NAME,
                Context.MODE_PRIVATE);
        Editor editor = pref.edit();

        // editConfigText���L�[�Ƀe�L�X�g�l��ݒ�
        editor.putString("editConfigText", mEditConfigText.getText().toString());
        // checkConfigCheck1���L�[��Boolean�l��ݒ�
        editor.putBoolean("checkConfigCheck1", mCheckConfigCheck1.isChecked());
        // mCheckConfigCheck2���L�[��Boolean�l��ݒ�
        editor.putBoolean("checkConfigCheck2", mCheckConfigCheck2.isChecked());
        // spinnerConfigSelect���L�[�ɐ����l��ݒ�
        editor.putInt("spinnerConfigSelect",
                mSpinnerConfigSelect.getSelectedItemPosition());

        // �ݒ�𔽉f
        editor.commit();
    }

}
