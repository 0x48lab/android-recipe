package jp.co.se.android.recipe.chapter02;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class Ch0236Preference extends Preference implements
        OnRatingBarChangeListener {

    private float mCurrentRating;
    private float mOldRating;

    public Ch0236Preference(Context context, AttributeSet attrs) {
        super(context, attrs);
        // �J�X�^�}�C�Y���C�A�E�g��ݒ�
        setWidgetLayoutResource(R.layout.ch0236_preference);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        // ���߂�Preference���Ă΂ꂽ�ۂɐݒ肷�鏉���l��ԋp
        return a.getFloat(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
            Object defaultValue) {
        // Preferencen�̏����l��ݒ�
        if (restorePersistedValue) {
            // restorePersistedValue��true�̏ꍇ�ASharedPreference����l���擾
            mCurrentRating = getPersistedFloat(mCurrentRating);
        } else {
            // restorePersistedValue��false�̏ꍇ�APreference�Ƀf�t�H���g�l��ݒ�
            mCurrentRating = (Float) defaultValue;
            persistFloat(mCurrentRating);
        }
        mOldRating = mCurrentRating;
    }

    @Override
    protected void onBindView(View view) {
        // Preference�ƃJ�X�^�}�C�Y���ꂽView��Bind
        final RatingBar rating = (RatingBar) view
                .findViewById(R.id.ratingPreference);
        if (rating != null) {
            rating.setRating(mCurrentRating);
            rating.setOnRatingBarChangeListener(this);
        }
        super.onBindView(view);
    }

    @Override
    public void onRatingChanged(RatingBar rating, float value, boolean arg2) {
        // ���[�U�[�ɂ��ݒ�ύX�����������ɌĂ΂��
        mCurrentRating = (callChangeListener(value)) ? value : mOldRating;
        persistFloat(mCurrentRating);
        mOldRating = mCurrentRating;
    }

}
