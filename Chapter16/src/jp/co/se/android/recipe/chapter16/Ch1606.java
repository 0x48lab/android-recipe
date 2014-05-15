package jp.co.se.android.recipe.chapter16;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class Ch1606 extends Activity {
    private static final String TAG = "Ch1606";
    // @formatter:off
    private String[] NAMES = new String[] {
            "Anastassia", "Juan", "Enrique",
            "Frannie", "Paloma", "Francisco",
            "Lorenzio", "Maryvonne", "Siv",
            "Georgie", "Casimir", "Catharine",
            "Joker"};
    // @formatter:on

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1606_main);

        findViewById(R.id.buttonStart).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ContactDbOpenHelper helper = null;
                        SQLiteDatabase db = null;
                        try {
                            // ContactDbOpenHelper�𐶐�
                            helper = new ContactDbOpenHelper(Ch1606.this);
                            // �������݉\��SQLiteDatabase�C���X�^���X���擾
                            db = helper.getWritableDatabase();

                            // �f�[�^�̍쐬
                            createDataByTransaction(db);
                        } finally {
                            if (db != null) {
                                db.close();
                            }
                            if (helper != null) {
                                helper.close();
                            }
                        }
                    }
                });
    }

    private void createDataByTransaction(SQLiteDatabase db) {
        try {
            // �g�����U�N�V�������J�n
            db.beginTransaction();
            Log.d(TAG, "�g�����U�N�V�������J�n");

            // ���ݕۑ�����Ă���f�[�^�����ׂč폜
            db.delete(Contact.TBNAME, null, null);

            for (int i = 0; i < NAMES.length; i++) {
                // ��������f�[�^���i�[����ContentValues�𐶐�
                ContentValues values = new ContentValues();
                values.put(Contact.NAME, NAMES[i]);
                values.put(Contact.AGE, 20);
                // �f�[�^�x�[�X��Contact�̃f�[�^�𐶐�
                db.insert(Contact.TBNAME, null, values);
                Log.d(TAG, String.format("�f�[�^�𐶐�[%d]", i + 1));
            }
            // �g�����U�N�V�������m��
            db.setTransactionSuccessful();
            Log.d(TAG, "�g�����U�N�V�������m��");
        } finally {
            // �g�����U�N�V�������I��
            db.endTransaction();
            Log.d(TAG, "�g�����U�N�V�������I��");
        }
    }

}
