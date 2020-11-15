package ru.geekbrains.atmosphere;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import ru.geekbrains.atmosphere.settings.Cities;
import ru.geekbrains.atmosphere.settings.Settings;

import static android.app.Activity.RESULT_OK;

public class CityChooseFragment extends Fragment implements View.OnClickListener, ExtraConstants {

    private static final String CLASS = CityChooseFragment.class.getSimpleName();
    private static final boolean LOGGING = false;

    private Settings settings;
    private Cities cities;
    private boolean landscapeOrientation;
    private MainActivity mainActivity;

    public CityChooseFragment() {
    }

    public static CityChooseFragment create(Settings settings, Cities cities, boolean landscapeOrientation) {
        CityChooseFragment fragment = new CityChooseFragment();
        Bundle args = new Bundle();
        args.putParcelable(SETTINGS, settings);
        args.putParcelable(CITIES, cities);
        args.putBoolean(LANDSCAPE_ORIENTATION, landscapeOrientation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            settings = getArguments().getParcelable(SETTINGS);
            cities = getArguments().getParcelable(CITIES);
            landscapeOrientation = getArguments().getBoolean(LANDSCAPE_ORIENTATION);
        } else {
            settings = MyApp.getInstance().getStorage().getSettings();
            cities = MyApp.getInstance().getStorage().getCities();
            landscapeOrientation = false;
        }
        if (landscapeOrientation) {
            mainActivity = (MainActivity) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_choose, container, false);

        EditText newCity = view.findViewById(R.id.cityChooseText);

        Button addCity = view.findViewById(R.id.addCity);
        addCity.setOnClickListener(button -> {
            cities.addCity(newCity.getText().toString());
            if (landscapeOrientation) {
                mainActivity.changeFragment(R.id.infoFragment, SettingsFragment.create(settings, cities, landscapeOrientation));
            } else {
                Intent intentResult = new Intent();
                intentResult.putExtra(SETTINGS, settings);
                intentResult.putExtra(CITIES, cities);
                getActivity().setResult(RESULT_OK, intentResult);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
    }
}