package jp.co.se.android.recipe.chapter15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.TextView;

public class Ch1512 extends Activity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTextView = new TextView(this);
        setContentView(mTextView);

        // JSON�f�[�^�̓ǂݎ��
        String json = readTestJson("ch1512.json");
        if (json == null) {
            mTextView.setText("�t�@�C���̓ǂݍ��݂Ɏ��s���܂����B");
            return;
        }
        /*
         * �ǂ݂���JSON�f�[�^�F { "data": [{ "title": "hoge", "body": "fugafuga" }, {
         * "title": "foo", "body": "barbar" }] }
         */
        mTextView.setText("");
        mTextView.append("json:\n");
        mTextView.append(json);

        try {
            mTextView.append("\n\n�p�[�X����");
            // �ǂݎ����JSON�f�[�^�̃p�[�X
            JSONObject jsRoot = new JSONObject(json);
            JSONArray jsDataList = jsRoot.getJSONArray("data");

            for (int i = 0, iL = jsDataList.length(); i < iL; i++) {
                JSONObject jsData = jsDataList.getJSONObject(i);
                String title = jsData.getString("title");
                String body = jsData.getString("body");

                mTextView.append("\n title: " + title + ", body: " + body);
            }
        } catch (JSONException e) {
            mTextView.setText("JSON�̃p�[�X�Ɏ��s���܂����B JSONException=" + e);
        }
    }

    private String readTestJson(String filePath) {
        AssetManager assets = getResources().getAssets();
        InputStream is = null;
        BufferedReader br = null;

        StringBuilder sb = new StringBuilder();
        try {
            is = assets.open(filePath);
            br = new BufferedReader(new InputStreamReader(is));

            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
