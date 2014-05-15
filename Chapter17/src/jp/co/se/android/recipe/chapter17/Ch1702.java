package jp.co.se.android.recipe.chapter17;

import java.util.ArrayList;
import java.util.List;

import jp.co.se.android.recipe.chapter17.util.IabHelper;
import jp.co.se.android.recipe.chapter17.util.IabResult;
import jp.co.se.android.recipe.chapter17.util.Inventory;
import jp.co.se.android.recipe.chapter17.util.Purchase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Ch1702 extends Activity {
    /** ���O�o�͗p��TAG */
    private static final String TAG = "Ch1702";

    /** ���J�� */
    private static final String PUBLIC_KEY = "�A�v���̃��C�Z���X�L�[�����";

    /** �A�v�����A�C�e����ID */
 // �i���^�A�C�e��
    static final String ITEM_GOLD = "gold";
 // ����w���^�A�C�e��
    static final String ITEM_SILVER = "silver";
 // ���Ռ^�A�C�e��
    static final String ITEM_BRONZE = "bronze";

    /** �A�v�����A�C�e�����w�����邽�߂̗v���R�[�h */
    static final int RC_REQUEST = 10001;

    /** �A�C�e���̏����t���O */
    boolean mHasGold = false;
    boolean mHasSilver = false;
    boolean mHasBronze = false;

    /** �t�B�[���h */
    IabHelper mIabHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1702_main);
        // �A�v�����A�C�e���w���p�[���쐬
        mIabHelper = new IabHelper(this, PUBLIC_KEY);

        // ���O��L���ɂ���
        mIabHelper.enableDebugLogging(true);

        // �A�C�e�����X�g�𐶐�
        List<String> itemList = new ArrayList<String>();
        itemList.add("�S�[���h�A�C�e���@500�~(�i�����p�\)");
        itemList.add("�V���o�[�A�C�e���@300�~(����w��)");
        itemList.add("�u�����Y�A�C�e���@100�~(�g������)");

        // �X�s�i�[�̏�����
        final Spinner spIab = (Spinner) findViewById(R.id.spinnerIab);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIab.setAdapter(adapter);

        // �A�v�����w���p�[�̃Z�b�g�A�b�v���J�n
        mIabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) {
                    // �A�v�����A�C�e���̏����`�F�b�N
                    mIabHelper.queryInventoryAsync(mGotInventoryListener);
                } else {
                    Log.d(TAG, "�Z�b�g�A�b�v�����s���܂���: " + result);
                }
            }
        });

        // �w���{�^�����^�b�v
        findViewById(R.id.buyItem).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                exeBuyItem(spIab.getSelectedItemPosition());
            }
        });
    }

    /**
     * �A�C�e���̍w������.
     * 
     * @param itemType
     */
    private void exeBuyItem(int itemType) {
        String payload = "";
        switch (itemType) {
        case 0:
            // �S�[���h�A�C�e��
            mIabHelper.launchPurchaseFlow(this, ITEM_GOLD, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
            break;
        case 1:
            // �V���o�[�A�C�e��
            mIabHelper.launchPurchaseFlow(this, ITEM_SILVER,
                    IabHelper.ITEM_TYPE_SUBS, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
            break;
        case 2:
            // �u�����Y�A�C�e��
            mIabHelper.launchPurchaseFlow(this, ITEM_BRONZE, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
            break;
        default:
            break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // �A�v�����A�C�e���w���p�[��j��
        if (mIabHelper != null) {
            mIabHelper.dispose();
            mIabHelper = null;
        }
    }

    /**
     * �A�v�����A�C�e���̏����`�F�b�N�I����m�点�郊�X�i�[.
     */
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                Inventory inventory) {
            if (result.isSuccess()) {
                // �S�[���h�A�C�e���������Ă��邩�m�F(�i���^)
                Purchase itemGold = inventory.getPurchase(ITEM_GOLD);
                if (itemGold != null) {
                    mHasGold = verifyDeveloperPayload(itemGold);
                    Log.d(TAG, "�S�[���h�A�C�e��������:" + mHasGold);
                }
                // �V���o�[�A�C�e���������Ă��邩�m�F(����w���^)
                Purchase itemSilver = inventory.getPurchase(ITEM_SILVER);
                if (itemSilver != null) {
                    mHasSilver = (itemSilver != null && verifyDeveloperPayload(itemSilver));
                    Log.d(TAG, "�V���o�[�A�C�e��������:" + mHasSilver);
                }
                // �u�����Y�A�C�e���������Ă��邩�m�F(���Ռ^)
                Purchase itemBronze = inventory.getPurchase(ITEM_BRONZE);
                if (itemBronze != null) {
                    mHasBronze = (itemBronze != null && verifyDeveloperPayload(itemBronze));
                    Log.d(TAG, "�u�����Y�A�C�e��������:" + mHasSilver);
                    // �u�����Y�A�C�e�����������Ă�����g��
                    if (mHasBronze) {
                        mIabHelper.consumeAsync(
                                inventory.getPurchase(ITEM_BRONZE),
                                mConsumeFinishedListener);
                        return;
                    }
                } else {
                    Toast.makeText(Ch1702.this, "�u�����Y�A�C�e�����������Ă��܂���",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "�C���x���g���̌��������s���܂���: " + result);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // onActivityResult�̌��ʂ��A�v�����A�C�e���w���p�[�ɓn��
        if (mIabHelper != null) {
            if (!mIabHelper.handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        // TODO�@�����Ńx���t�@�C��������������

        return true;
    }

    /**
     * �A�v�����A�C�e�����w��������m�点�郊�X�i�[.
     */
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isSuccess()) {
                // �w�����������A�v�����A�C�e���̏����`�F�b�N
                if (!verifyDeveloperPayload(purchase)) {
                    Log.d(TAG, "�A�v�����A�C�e�����������w���ł��܂���ł���");
                    return;
                }
                if (purchase.getSku().equals(ITEM_GOLD)) {
                    // �S�[���h�A�C�e�����������܂���
                    mHasGold = true;
                } else if (purchase.getSku().equals(ITEM_SILVER)) {
                    // �V���o�[�A�C�e�����������܂���
                    mHasSilver = true;
                } else if (purchase.getSku().equals(ITEM_BRONZE)) {
                    // �u�����Y�A�C�e�����������܂���
                    mHasBronze = true;
                }
            }
        }
    };

    /**
     * ����^�A�C�e�����g�p�������ɌĂяo����郊�X�i�[
     */
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()) {
                Toast.makeText(Ch1702.this, "�u�����Y�A�C�e��������܂���",
                        Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "�A�C�e������Ɏ��s���܂���: " + result);
            }
        }
    };
}
