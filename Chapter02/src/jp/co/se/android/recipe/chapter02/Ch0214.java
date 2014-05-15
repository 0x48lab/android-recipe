package jp.co.se.android.recipe.chapter02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class Ch0214 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // �J�X�^���r���[��ݒ�
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(
                R.layout.ch0214_dialog_contents_item,
                (ViewGroup) findViewById(R.id.dialogcustom));

        // �A���[�g�_�C�A���O �𐶐�
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("�_�C�A���O�^�C�g��");
        builder.setView(layout);
        builder.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // OK �{�^���N���b�N����
                EditText id = (EditText) layout.findViewById(R.id.customDialog);
                String strId = id.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Cancel �{�^���N���b�N����
            }
        });

        // �\��
        builder.create().show();
    }
}