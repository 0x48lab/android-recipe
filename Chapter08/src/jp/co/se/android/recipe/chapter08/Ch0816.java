package jp.co.se.android.recipe.chapter08;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Ch0816 extends Activity implements OnClickListener {
    private static final String TAG = Ch0816.class.getSimpleName();
    private TextView mTextView;
    private Button mBtnRun;
    private Button mBtnCancel;
    private AsyncTask<Uri, Void, String> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0817_main);

        mBtnRun = (Button) findViewById(R.id.btnRun);
        mBtnCancel = (Button) findViewById(R.id.btnCancel);
        mTextView = (TextView) findViewById(R.id.text);

        mBtnRun.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    private void sendText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.append(text);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnRun) {
            requestGetMethod();
        } else if (id == R.id.btnCancel) {
            if (mTask != null) {
                mTask.cancel(false);
                mTextView.append("\n�ʐM���L�����Z�����Ă��܂��c�B");
            } else {
                mTextView.append("\n�ʐM���Ă��܂���B�܂��́A�ʐM�L�����Z�����ł��B");
            }
        }
    }

    private void requestGetMethod() {
        mTextView.setText("");
        sendText("�ʐM����");

        // URL���A�����₷��Uri�^�őg��
        Uri baseUri = Uri
                .parse("http://android-recipe.herokuapp.com/samples/ch08/json");

        // �p�����[�^�̕t�^
        Uri uri = baseUri.buildUpon().appendQueryParameter("param1", "hoge")
                .build();

        if (mTask == null) {
            mTask = new AsyncTask<Uri, Void, String>() {
                /** �ʐM�ɂ����Ĕ��������G���[ */
                private Throwable mError = null;

                @Override
                protected String doInBackground(Uri... params) {
                    String result = request(params[0]);

                    if (isCancelled()) {
                        sendText("\n�ʐM�̓L�����Z���ς݂ł����B");
                        return result;
                    }

                    return result;
                }

                private String request(Uri uri) {
                    DefaultHttpClient httpClient = new DefaultHttpClient();

                    // �^�C���A�E�g�̐ݒ�
                    HttpParams httpParams = httpClient.getParams();
                    // �ڑ��m���܂ł̃^�C���A�E�g�ݒ� (�~���b)
                    HttpConnectionParams.setConnectionTimeout(httpParams,
                            5 * 1000);
                    // �ڑ���܂ł̃^�C���A�E�g�ݒ� (�~���b)
                    HttpConnectionParams.setSoTimeout(httpParams, 5 * 1000);

                    String result = null;
                    HttpGet request = new HttpGet(uri.toString());
                    try {
                        sendText("\n�ʐM�J�n");
                        result = httpClient.execute(request,
                                new ResponseHandler<String>() {
                                    @Override
                                    public String handleResponse(
                                            HttpResponse response)
                                            throws ClientProtocolException,
                                            IOException {
                                        int statusCode = response
                                                .getStatusLine()
                                                .getStatusCode();
                                        sendText("\n�X�e�[�^�X�R�[�h : " + statusCode);
                                        if (statusCode == HttpStatus.SC_OK) {
                                            String result = EntityUtils
                                                    .toString(response
                                                            .getEntity());
                                            return result;
                                        } else if (statusCode == HttpStatus.SC_NOT_FOUND) {
                                            throw new RuntimeException(
                                                    "404 NOT FOUND");
                                        } else {
                                            throw new RuntimeException(
                                                    "���̂ق��̒ʐM�G���[");
                                        }
                                    }
                                });
                        sendText("\n�ʐM����");
                    } catch (RuntimeException e) {
                        mError = e;
                        sendText("\n�ʐM���s" + e.getClass().getSimpleName());
                        Log.e(TAG, "�ʐM���s", e);
                    } catch (ClientProtocolException e) {
                        mError = e;
                        sendText("\n�ʐM���s" + e.getClass().getSimpleName());
                        Log.e(TAG, "�ʐM���s", e);
                    } catch (IOException e) {
                        mError = e;
                        sendText("\n�ʐM���s" + e.getClass().getSimpleName());
                        Log.e(TAG, "�ʐM���s", e);
                    } finally {
                        // ���\�[�X���J������
                        httpClient.getConnectionManager().shutdown();
                    }

                    return result;
                }

                @Override
                protected void onPostExecute(String result) {
                    sendText("\nonPostExecute(String result)");

                    if (mError == null) {
                        sendText("\n�ʐM�����F");
                        sendText("\n  ��M�����f�[�^ : " + result);
                    } else {
                        sendText("\n�ʐM���s�F");
                        sendText("\n  �G���[ : " + mError.getMessage());
                    }

                    mTask = null;
                }

                @Override
                protected void onCancelled() {
                    onCancelled(null);
                }

                @Override
                protected void onCancelled(String result) {
                    sendText("\nonCancelled(String result), result=" + result);

                    mTask = null;
                }
            }.execute(uri);
        } else {
            // ���ݒʐM�̃^�X�N�����s���B�d�����Ď��s����Ȃ��悤�ɐ���B
        }
    }
}
