package jp.co.se.android.recipe.chapter10;

import java.util.ArrayList;
import java.util.List;

import jp.co.se.android.recipe.chapter10.ParseJson.Leg;
import jp.co.se.android.recipe.chapter10.ParseJson.Route;
import jp.co.se.android.recipe.chapter10.ParseJson.Step;
import jp.co.se.android.recipe.chapter10.RequestDirectionsTask.RequestDirectionsTaskCallback;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class Ch1004 extends FragmentActivity {
    public static final String TRAVEL_MODE = "driving";// default

    GoogleMap mGmap;
    ArrayList<LatLng> mMarkerPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1004_main);

        // 初期化
        mMarkerPoints = new ArrayList<LatLng>();

        // GoogleMapのインスタンスを生成
        SupportMapFragment mapfragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mGmap = mapfragment.getMap();

        // 初期位置を設定
        LatLng location = new LatLng(34.802556297454004, 135.53884506225586);
        mGmap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17));
        mGmap.setMyLocationEnabled(true);

        // マップをタップした時のイベントを設定
        mGmap.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // 既に２度タップしていた場合、経路をクリアする
                if (mMarkerPoints.size() >= 2) {
                    mMarkerPoints.clear();
                    mGmap.clear();
                }

                // タップした座標を記録しておく
                mMarkerPoints.add(point);

                // タップした点にマーカーを追加
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(point);
                if (mMarkerPoints.size() == 1) {
                    markerOptions.title("A");
                } else if (mMarkerPoints.size() == 2) {
                    markerOptions.title("B");
                }
                mGmap.addMarker(markerOptions);

                // ２度目のタップだった場合、ルート検索を開始する
                if (mMarkerPoints.size() >= 2) {
                    LatLng origin = mMarkerPoints.get(0);
                    LatLng dest = mMarkerPoints.get(1);
                    routeSearch(origin, dest);
                }
            }
        });

        mGmap.setOnMarkerClickListener(new OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marker.getTitle();
                if (title.equals("A")) {
                    marker.setSnippet("");
                } else if (title.equals("B")) {
                    marker.setSnippet("");
                }

                return false;
            }
        });
    }

    /**
     * 経路を検索
     * 
     * @param origin
     * @param dest
     */
    private void routeSearch(LatLng origin, LatLng dest) {
        // 進捗ダイアログを表示
        ProgressDialogFragment.newInstance("検索中...").show(
                getSupportFragmentManager(), ProgressDialogFragment.FRG_TAG);

        // 通信を行う
        RequestDirectionsTask task = new RequestDirectionsTask(this, origin,
                dest, TRAVEL_MODE);
        task.setRequestDirectionsTaskCallback(
                new RequestDirectionsTaskCallback() {

                    @Override
                    public void onSucceed(List<Route> routes) {
                        // 進捗ダイアログを閉じる
                        FragmentManager fm = getSupportFragmentManager();
                        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment) fm
                                .findFragmentByTag(ProgressDialogFragment.FRG_TAG);
                        if (progressDialogFragment != null) {
                            progressDialogFragment.dismissAllowingStateLoss();
                        }

                        // ルートの描画開始
                        if (routes.size() > 0) {
                            PolylineOptions lineOptions = new PolylineOptions();
                            for (Route route : routes) {
                                for (Leg leg : route.getLegs()) {
                                    for (Step step : leg.getSteps()) {
                                        lineOptions.addAll(step
                                                .getPolylinePoints());
                                        lineOptions.width(10);
                                        lineOptions.color(0x550000ff);
                                    }
                                }
                            }
                            // 描画
                            mGmap.addPolyline(lineOptions);
                        } else {
                            mGmap.clear();
                            Toast.makeText(Ch1004.this, "ルート情報を取得できませんでした",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        Toast.makeText(Ch1004.this,
                                "ルート検索中にエラーが発生しました。(" + e + ")",
                                Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }
}
