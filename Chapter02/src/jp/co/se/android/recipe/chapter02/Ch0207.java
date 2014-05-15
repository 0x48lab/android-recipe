package jp.co.se.android.recipe.chapter02;

import jp.co.se.android.recipe.chapter02.R;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Ch0207 extends Activity implements OnScrollListener {

    private ArrayAdapter<String> mAdapter;
    private AsyncTask<String, Void, String> mTask;
    private ListView mListView;
    private View mFooter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0201_main);

        mListView = (ListView) findViewById(R.id.ListView);

        // �V���v���ȃ��X�g��\������Adapter�𐶐�
        mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);
        // �e�X�g�f�[�^��ǉ�
        for (int i = 1; i < 10; i++) {
            mAdapter.add("Item" + i);
        }

        // �ǂݍ��ݒ��̃t�b�^�[�𐶐�
        mFooter = getLayoutInflater().inflate(
                R.layout.ch0207_list_progress_item, null);

        // ListView�Ƀt�b�^�[��ݒ�
        mListView.addFooterView(mFooter);

        // �A�_�v�^�[��ݒ�
        mListView.setAdapter(mAdapter);

        // �X�N���[�����X�i�[��ݒ�
        mListView.setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        // �����̏ꍇ���̃f�[�^��ǂݍ���
        if (totalItemCount == firstVisibleItem + visibleItemCount) {
            additionalReading();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView arg0, int arg1) {
    }

    private void additionalReading() {
        // ���ɓǂݍ��ݒ��Ȃ�X�L�b�v
        if (mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING) {
            return;
        }
        /*
         * �ʏ�̓l�b�g���[�N��t�@�C������f�[�^���ǂ܂�邽�߁A�񓯊��ɓǂݍ��ݏ�������������
         * �{�T���v���ł͊ȗ����̂��߁A�񓯊������͂��̂܂܂Ƃ��A�f�[�^�̓ǂݍ��݂͈Ӑ}�I�ɒx������������悤�Ɏ������Ă���
         */
        mTask = new MyAsyncTask(this).execute("text");
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {

        public MyAsyncTask(Ch0207 androidAsyncTaskActivity) {
        }

        protected String doInBackground(String... params) {
            // 2�b�~�߂�
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return params[0];
        }

        @Override
        protected void onPostExecute(String text) {
            // �f�[�^�ǉ�
            for (int n = 0; n < 10; n++) {
                mAdapter.add(text + n);
            }
        }

    }
}