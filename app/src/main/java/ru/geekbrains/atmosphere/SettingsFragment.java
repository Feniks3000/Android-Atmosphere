package ru.geekbrains.atmosphere;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import ru.geekbrains.atmosphere.settings.Settings;

public class SettingsFragment extends Fragment implements View.OnClickListener, ExtraConstants {

    private static final String CLASS = SettingsFragment.class.getSimpleName();
    private static final boolean LOGGING = false;

    private OnUpdateSettingsListener onUpdateSettingsListener;
    private RadioGroup settingTheme;
    private SwitchCompat settingWeatherDetail;
    private Settings settings;

    public SettingsFragment() {
    }

    public static SettingsFragment create(Settings settings) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putParcelable(SETTINGS, settings);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            settings = getArguments().getParcelable(SETTINGS);
        }

        if (LOGGING) {
            Log.d(CLASS, "OnCreate. Settings - " + settings);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        initSettingsFragment(view);

        settingWeatherDetail.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settings.setAllDetail(settingWeatherDetail.isChecked());
            onUpdateSettingsListener.onUpdateSettings(settings);
        });

        settingTheme.setOnCheckedChangeListener((button, isChecked) -> {
            settings.setTheme(settingTheme.indexOfChild(settingTheme.findViewById(settingTheme.getCheckedRadioButtonId())));
            onUpdateSettingsListener.onUpdateSettings(settings);
        });
        return view;
    }

    private void initSettingsFragment(View view) {
        settingTheme = view.findViewById(R.id.settingsTheme);
        settingWeatherDetail = view.findViewById(R.id.weatherDetail);

        ((RadioButton) settingTheme.getChildAt(settings.getTheme())).setChecked(true);
        settingWeatherDetail.setChecked(settings.isAllDetail());
    }

    @Override
    public void onClick(View v) {
    }

    public interface OnUpdateSettingsListener {
        void onUpdateSettings(Settings settings);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnUpdateSettingsListener) {
            onUpdateSettingsListener = (OnUpdateSettingsListener) context;
        }
    }
}