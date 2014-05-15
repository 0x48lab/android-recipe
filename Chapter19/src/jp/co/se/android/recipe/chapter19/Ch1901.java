package jp.co.se.android.recipe.chapter19;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

public class Ch1901 extends Activity implements OnClickListener {
    private static final String TAG = Ch1901.class.getSimpleName();
    private SurfaceView mSurfaceView;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSurfaceView = new SurfaceView(this);
        mSurfaceView.setOnClickListener(this);
        setContentView(mSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(callback);
    }

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // SurfaceView�̐������ɁA�v���r���[���J�����ɐݒ�
            mCamera = Camera.open();
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                Log.e(TAG, "�v���r���[�̍쐬�Ɏ��s", e);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                int height) {
            // width, height��ύX����
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> previewSizes = parameters
                    .getSupportedPreviewSizes();
            Camera.Size previewSize = previewSizes.get(0);
            parameters.setPreviewSize(previewSize.width, previewSize.height);

            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mCamera.release();
            mCamera = null;
        }
    };

    @Override
    public void onClick(View v) {
        if (mCamera != null) {
            mCamera.autoFocus(mAutoFocusCallback);
        }
    }

    private AutoFocusCallback mAutoFocusCallback = new AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // �\�����̃v���r���[��ʂ��A�o�C�i���f�[�^�ɕϊ�
                camera.setOneShotPreviewCallback(mPreviewCallback);
            }
        }
    };

    private PreviewCallback mPreviewCallback = new PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            // �v���r���[�f�[�^���� BinaryBitmap �𐶐�
            int width = camera.getParameters().getPreviewSize().width;
            int height = camera.getParameters().getPreviewSize().height;
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
                    data, width, height, 0, 0, width, height, false);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            // �ǂݎ��
            Reader reader = new MultiFormatReader();
            Result result = null;
            String msg = "�ǂݎ��G���[�ł��B\n";
            try {
                result = reader.decode(bitmap);
                msg = result.getText();
            } catch (NotFoundException e) {
                msg += "QR�R�[�h��������܂���ł����B";
                Log.e(TAG, msg, e);
            } catch (ChecksumException e) {
                msg += "�s����QR�R�[�h�ł��B";
                Log.e(TAG, msg, e);
            } catch (FormatException e) {
                msg += "�ǂݎ��Ɏ��s�������A�s����QR�R�[�h�ł��B";
                Log.e(TAG, msg, e);
            }
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG)
                    .show();
        }
    };

}
