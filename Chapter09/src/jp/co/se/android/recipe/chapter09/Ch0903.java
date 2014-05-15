package jp.co.se.android.recipe.chapter09;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Ch0903 extends Activity implements OnClickListener {
    private GraphUser mGraphUser;
    private StatusCallback mStatusCallback = new SessionStatusCallback();
    private Button mBtnLogin;
    private Button mBtnPost;
    private EditText mEtInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0903_main);

        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnLogin.setOnClickListener(this);
        mBtnPost = (Button) findViewById(R.id.post);
        mBtnPost.setOnClickListener(this);
        mEtInput = (EditText) findViewById(R.id.input);

        // �Z�b�V�������擾
        Session session = Session.getActiveSession();
        if (session == null) {
            // �Z�b�V������ۑ����Ă���ꍇ�͕��A
            if (savedInstanceState != null) {
                session = Session.restoreSession(this, null, mStatusCallback,
                        savedInstanceState);
            }
            // �Z�b�V�������Ȃ��ꍇ�͐V���ɐ���
            if (session == null) {
                session = new Session(this);
            }
            // �Z�b�V�����̏�Ԃ��Z�b�g
            Session.setActiveSession(session);
            // �g�[�N�������ɑ��݂��Ă�ꍇ�̓Z�b�V�����X�e�[�^�X��v��
            if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                session.openForRead(new Session.OpenRequest(this)
                        .setCallback(mStatusCallback));
            }
        } else {
            // �Z�b�V����������ꍇ�̓v���t�B�[�����擾
            getMyProfile(session);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // �Z�b�V�����X�e�[�^�X�R�[���o�b�N��o�^
        Session.getActiveSession().addCallback(mStatusCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        // �Z�b�V�����X�e�[�^�X�R�[���o�b�N��j��
        Session.getActiveSession().removeCallback(mStatusCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ���O�C�����ʂ�FacebookSDK�ɓn��
        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // �Z�b�V��������ۑ�
        Session session = Session.getActiveSession();
        Session.saveSession(session, outState);
    }

    /**
     * �Z�b�V�����X�e�[�^�X�R�[���o�b�N.
     */
    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(final Session session, SessionState state,
                Exception exception) {
            // �擾�����Z�b�V���������Ƀv���t�B�[�����擾
            getMyProfile(session);
        }
    }

    /**
     * �v���t�B�[�����擾.
     * 
     * @param session
     */
    private void getMyProfile(final Session session) {
        if (session != null && session.isOpened()) {
            // �����̃��[�U�[�����擾
            Request request = Request.newMeRequest(session,
                    new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser user,
                                Response response) {
                            if (session == Session.getActiveSession()) {
                                if (user != null) {
                                    mGraphUser = user;
                                    updateView();
                                }
                            }
                        }
                    });
            Request.executeBatchAsync(request);
        }
    }

    /**
     * ��ʂ��X�V.
     */
    private void updateView() {
        Session session = Session.getActiveSession();
        if (mGraphUser != null && session.isOpened()) {
            mBtnLogin.setText("���O�A�E�g");
            mBtnPost.setEnabled(true);
            mEtInput.setEnabled(true);
        } else {
            mBtnLogin.setText("���O�C��");
            mBtnPost.setEnabled(false);
            mEtInput.setEnabled(false);
            mGraphUser = null;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.login) {
            Session session = Session.getActiveSession();
            if (session.isOpened()) {
                // ���O�A�E�g����
                onClickLogout();
            } else {
                // ���O�C������
                onClickLogin();
            }
        } else if (id == R.id.post) {
            String message = mEtInput.getText().toString();
            postWall(message);
        }
    }

    /**
     * Facebook�Ƀ��O�C��.
     */
    private void onClickLogin() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this)
                    .setCallback(mStatusCallback));
        } else {
            Session.openActiveSession(this, true, mStatusCallback);
        }
    }

    /**
     * Facebook���烍�O�A�E�g.
     */
    private void onClickLogout() {
        Session session = Session.getActiveSession();
        if (!session.isClosed()) {
            session.closeAndClearTokenInformation();
            updateView();
        }
    }

    /**
     * �ߋ����A�b�v�f�[�g.
     * 
     * @param message
     */
    private void postWall(String message) {
        Request.newStatusUpdateRequest(Session.getActiveSession(), message,
                null, null, new Request.Callback() {
                    @Override
                    public void onCompleted(Response response) {
                        mEtInput.setText("");
                        Toast.makeText(Ch0903.this, "�ߋ����A�b�v�f�[�g���܂���",
                                Toast.LENGTH_SHORT).show();
                    }
                }).executeAsync();
    }
}
