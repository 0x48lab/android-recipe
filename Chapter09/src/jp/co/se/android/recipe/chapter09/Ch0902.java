package jp.co.se.android.recipe.chapter09;

import jp.co.se.android.recipe.chapter09.R;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Ch0902 extends Activity implements OnClickListener {
    private GraphUser mGraphUser;
    private StatusCallback mStatusCallback = new SessionStatusCallback();
    private Button mBtnLogin;
    private ProfilePictureView mPpvImage;
    private TextView mTvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0902_main);

        mPpvImage = (ProfilePictureView) findViewById(R.id.image);
        mTvName = (TextView) findViewById(R.id.name);
        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnLogin.setOnClickListener(this);

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

        // �A�N�Z�X�g�[�N����v�������ۂ̃��O�o�͂�ON�ɂ���
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
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
            mBtnLogin.setText(getString(R.string.label_logout));
            mPpvImage.setProfileId(mGraphUser.getId());
            mTvName.setText(mGraphUser.getName());
        } else {
            mBtnLogin.setText(getString(R.string.label_login));
            mPpvImage.setProfileId(null);
            mTvName.setText("");
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
}
