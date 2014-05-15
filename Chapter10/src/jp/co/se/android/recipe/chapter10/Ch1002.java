package jp.co.se.android.recipe.chapter10;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class Ch1002 extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1001_main);
        SupportMapFragment fragment = ((SupportMapFragment) 
getSupportFragmentManager()
                .findFragmentById(R.id.map));
        final GoogleMap gMap = fragment.getMap();
        gMap.setOnMapClickListener(new OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // �s���𗧂Ă邽�߂Ƀ}�[�J�[���쐬
                MarkerOptions mOptions = new MarkerOptions();
                // �s���̃^�C�g����ݒ�
                mOptions.title(getString(R.string.ch1002_pinpoint));
                // �s���̃X�j�y�b�g��ݒ�
                String sLatitude = getString(R.string.ch1002_latitude,point.latitude);
                String sLongitude = getString(R.string.ch1002_longitude,point.longitude);
                mOptions.snippet(sLatitude + "," + sLongitude);
                // �s���̈ܓx�o�x��ݒ�
                mOptions.position(new LatLng(point.latitude, point.longitude));
                // �}�b�v�Ƀ}�[�J�[��ǉ�
                gMap.addMarker(mOptions);
            }
        });
    }
}
