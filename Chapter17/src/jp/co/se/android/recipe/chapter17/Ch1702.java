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
    /** ログ出力用のTAG */
    private static final String TAG = "Ch1702";

    /** 公開鍵 */
    private static final String PUBLIC_KEY = "アプリのライセンスキーを入力";

    /** アプリ内アイテムのID */
 // 永続型アイテム
    static final String ITEM_GOLD = "gold";
 // 定期購入型アイテム
    static final String ITEM_SILVER = "silver";
 // 消耗型アイテム
    static final String ITEM_BRONZE = "bronze";

    /** アプリ内アイテムを購入するための要求コード */
    static final int RC_REQUEST = 10001;

    /** アイテムの所持フラグ */
    boolean mHasGold = false;
    boolean mHasSilver = false;
    boolean mHasBronze = false;

    /** フィールド */
    IabHelper mIabHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1702_main);
        // アプリ内アイテムヘルパーを作成
        mIabHelper = new IabHelper(this, PUBLIC_KEY);

        // ログを有効にする
        mIabHelper.enableDebugLogging(true);

        // アイテムリストを生成
        List<String> itemList = new ArrayList<String>();
        itemList.add("ゴールドアイテム　500円(永続利用可能)");
        itemList.add("シルバーアイテム　300円(定期購入)");
        itemList.add("ブロンズアイテム　100円(使いきり)");

        // スピナーの初期化
        final Spinner spIab = (Spinner) findViewById(R.id.spinnerIab);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, itemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spIab.setAdapter(adapter);

        // アプリ内ヘルパーのセットアップを開始
        mIabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) {
                    // アプリ内アイテムの所持チェック
                    mIabHelper.queryInventoryAsync(mGotInventoryListener);
                } else {
                    Log.d(TAG, "セットアップが失敗しました: " + result);
                }
            }
        });

        // 購入ボタンをタップ
        findViewById(R.id.buyItem).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                exeBuyItem(spIab.getSelectedItemPosition());
            }
        });
    }

    /**
     * アイテムの購入処理.
     * 
     * @param itemType
     */
    private void exeBuyItem(int itemType) {
        String payload = "";
        switch (itemType) {
        case 0:
            // ゴールドアイテム
            mIabHelper.launchPurchaseFlow(this, ITEM_GOLD, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
            break;
        case 1:
            // シルバーアイテム
            mIabHelper.launchPurchaseFlow(this, ITEM_SILVER,
                    IabHelper.ITEM_TYPE_SUBS, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
            break;
        case 2:
            // ブロンズアイテム
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
        // アプリ内アイテムヘルパーを破棄
        if (mIabHelper != null) {
            mIabHelper.dispose();
            mIabHelper = null;
        }
    }

    /**
     * アプリ内アイテムの所持チェック終了を知らせるリスナー.
     */
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                Inventory inventory) {
            if (result.isSuccess()) {
                // ゴールドアイテムを持っているか確認(永続型)
                Purchase itemGold = inventory.getPurchase(ITEM_GOLD);
                if (itemGold != null) {
                    mHasGold = verifyDeveloperPayload(itemGold);
                    Log.d(TAG, "ゴールドアイテムを所持:" + mHasGold);
                }
                // シルバーアイテムを持っているか確認(定期購入型)
                Purchase itemSilver = inventory.getPurchase(ITEM_SILVER);
                if (itemSilver != null) {
                    mHasSilver = (itemSilver != null && verifyDeveloperPayload(itemSilver));
                    Log.d(TAG, "シルバーアイテムを所持:" + mHasSilver);
                }
                // ブロンズアイテムを持っているか確認(消耗型)
                Purchase itemBronze = inventory.getPurchase(ITEM_BRONZE);
                if (itemBronze != null) {
                    mHasBronze = (itemBronze != null && verifyDeveloperPayload(itemBronze));
                    Log.d(TAG, "ブロンズアイテムを所持:" + mHasSilver);
                    // ブロンズアイテムを所持していたら使う
                    if (mHasBronze) {
                        mIabHelper.consumeAsync(
                                inventory.getPurchase(ITEM_BRONZE),
                                mConsumeFinishedListener);
                        return;
                    }
                } else {
                    Toast.makeText(Ch1702.this, "ブロンズアイテムを所持していません",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "インベントリの検索が失敗しました: " + result);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // onActivityResultの結果をアプリ内アイテムヘルパーに渡す
        if (mIabHelper != null) {
            if (!mIabHelper.handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        // TODO　自分でベリファイ処理を実装する

        return true;
    }

    /**
     * アプリ内アイテムを購入完了を知らせるリスナー.
     */
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isSuccess()) {
                // 購入完了したアプリ内アイテムの所持チェック
                if (!verifyDeveloperPayload(purchase)) {
                    Log.d(TAG, "アプリ内アイテムが正しく購入できませんでした");
                    return;
                }
                if (purchase.getSku().equals(ITEM_GOLD)) {
                    // ゴールドアイテムを所持しました
                    mHasGold = true;
                } else if (purchase.getSku().equals(ITEM_SILVER)) {
                    // シルバーアイテムを所持しました
                    mHasSilver = true;
                } else if (purchase.getSku().equals(ITEM_BRONZE)) {
                    // ブロンズアイテムを所持しました
                    mHasBronze = true;
                }
            }
        }
    };

    /**
     * 消費型アイテムを使用した時に呼び出されるリスナー
     */
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()) {
                Toast.makeText(Ch1702.this, "ブロンズアイテムを消費しました",
                        Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "アイテム消費に失敗しました: " + result);
            }
        }
    };
}
