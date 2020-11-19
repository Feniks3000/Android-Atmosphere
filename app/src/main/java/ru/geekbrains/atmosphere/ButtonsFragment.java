package ru.geekbrains.atmosphere;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import ru.geekbrains.atmosphere.settings.Cities;
import ru.geekbrains.atmosphere.settings.Settings;

public class ButtonsFragment extends Fragment implements View.OnClickListener, ExtraConstants {

    private static final String CLASS = ButtonsFragment.class.getSimpleName();
    private static final boolean LOGGING = false;

    private String city;
    private Settings settings;
    private Cities cities;
    private MainActivity mainActivity;

    public ButtonsFragment() {
    }

    public static ButtonsFragment create(Settings settings, Cities cities) {
        ButtonsFragment fragment = new ButtonsFragment();
        Bundle args = new Bundle();
        args.putParcelable(SETTINGS, settings);
        args.putParcelable(CITIES, cities);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            settings = getArguments().getParcelable(SETTINGS);
            cities = getArguments().getParcelable(CITIES);
        }
        mainActivity = (MainActivity) getActivity();

        if (LOGGING) {
            Log.d(CLASS, "OnCreate. Settings - " + settings);
            Log.d(CLASS, "OnCreate. Cities - " + cities);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons, container, false);

        Button buttonSettings = view.findViewById(R.id.settingsButton);
        buttonSettings.setOnClickListener(button -> {
            mainActivity.changeFragment(SettingsFragment.create(settings, cities), false);
        });

        Button buttonAboutCity = view.findViewById(R.id.webInfoButton);
        buttonAboutCity.setOnClickListener(button -> {
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, String.format(getResources().getString(R.string.weatherIn), mainActivity.getActiveCity()));
            startActivity(intent);
        });
        return view;
    }

    @Override
    public void onClick(View v) {
    }
}