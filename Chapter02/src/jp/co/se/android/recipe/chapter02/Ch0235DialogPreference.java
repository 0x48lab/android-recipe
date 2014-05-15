package jp.co.se.android.recipe.chapter02;

import java.util.TimeZone;

import jp.co.se.android.recipe.chapter02.R;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.TimeFormatException;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class Ch0235DialogPreference extends DialogPreference {

    /**
     * Preference�ŗ��p����l�B�v���t�@�����XXML��android:defaultValue����`����Ă��Ȃ��ꍇ�́A���̒l�����p�����B
     */
    private String mPreferenceValue = "";

    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    public Ch0235DialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        // �_�C�A���O�̃��C�A�E�g�Ƃ��ė��p���郊�\�[�X���w��
        setDialogLayoutResource(R.layout.ch0235_main);

        // OK�{�^���̃��x�����w��
        setPositiveButtonText(android.R.string.ok);
        // �L�����Z���{�^���̃��x�����w��
        setNegativeButtonText(android.R.string.cancel);
    }

    /**
     * �l�̏��������s���Bandroid:defaultValue���Ȃ��ꍇ�͌Ă΂�Ȃ��B
     */
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue,
            Object defaultValue) {
        if (restorePersistedValue) {
            mPreferenceValue = getPersistedString(mPreferenceValue);
        } else {
            mPreferenceValue = (String) defaultValue;
            persistString(mPreferenceValue);
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);

        // �l���擾
        setTimeToView(mPreferenceValue);
    }

    /** �l����ʂ֔��f */
    private void setTimeToView(String preferenceValue) {
        // �w�肳�ꂽ�������Time�֕ϊ�
        Time time = new Time(TimeZone.getDefault().getID());
        try {
            time.parse(preferenceValue);
        } catch (TimeFormatException e) {
            // �l�̕ϊ��Ɏ��s�����ꍇ�i���ɕs���Ȓl�������Ă���ꍇ�Ȃǁj�A���ݎ����ɂ���
            time.setToNow();
        }

        // �l����ʂփZ�b�g
        mDatePicker.updateDate(time.year, time.month, time.monthDay);
        mTimePicker.setCurrentHour(time.hour);
        mTimePicker.setCurrentMinute(time.minute);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            // ���Ԃ𕶎���֕ύX
            Time time = new Time(TimeZone.getDefault().getID());
            time.year = mDatePicker.getYear();
            time.month = mDatePicker.getMonth();
            time.monthDay = mDatePicker.getDayOfMonth();
            time.hour = mTimePicker.getCurrentHour();
            time.minute = mTimePicker.getCurrentMinute();
            String newValue = time.format2445();
            if (callChangeListener(newValue)) {
                mPreferenceValue = newValue;
                persistString(mPreferenceValue);
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            return superState;
        }

        // �l��ۑ�����
        final PreferenceSavedState myState = new PreferenceSavedState(
                superState);
        myState.value = mPreferenceValue;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null
                || !state.getClass().equals(PreferenceSavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }

        // �l��ێ�����
        PreferenceSavedState myState = (PreferenceSavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // ���̒l�𗘗p���āA��ʂ��\��
        setTimeToView(myState.value);
    }

    /**
     * �l�̕ۑ����s�����߂̃N���X
     */
    private static class PreferenceSavedState extends BaseSavedState {
        String value;

        public PreferenceSavedState(Parcelable superState) {
            super(superState);
        }

        public PreferenceSavedState(Parcel source) {
            super(source);
            value = source.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(value);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<PreferenceSavedState> CREATOR = new Parcelable.Creator<PreferenceSavedState>() {

            public PreferenceSavedState createFromParcel(Parcel in) {
                return new PreferenceSavedState(in);
            }

            public PreferenceSavedState[] newArray(int size) {
                return new PreferenceSavedState[size];
            }
        };
    }
}
