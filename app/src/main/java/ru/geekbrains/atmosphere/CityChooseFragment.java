package ru.geekbrains.atmosphere;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ru.geekbrains.atmosphere.settings.Cities;
import ru.geekbrains.atmosphere.settings.Settings;

public class CityChooseFragment extends Fragment implements View.OnClickListener, ExtraConstants {

    private static final String CLASS = CityChooseFragment.class.getSimpleName();
    private static final boolean LOGGING = false;

    private OnChangeFragmentListener onChangeFragmentListener;

    private Settings settings;
    private Cities cities;

    public CityChooseFragment() {
    }

    public static CityChooseFragment create(Settings settings, Cities cities) {
        CityChooseFragment fragment = new CityChooseFragment();
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

        if (LOGGING) {
            Log.d(CLASS, "OnCreate. Settings - " + settings);
            Log.d(CLASS, "OnCreate. Cities - " + cities);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_choose, container, false);

        EditText newCity = view.findViewById(R.id.cityChooseText);

        Button addCity = view.findViewById(R.id.addCity);
        addCity.setOnClickListener(button -> {
            cities.addCity(newCity.getText().toString());
            onChangeFragmentListener.onChangeFragment(SettingsFragment.create(settings, cities), false);
        });
        return view;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnChangeFragmentListener) {
            onChangeFragmentListener = (OnChangeFragmentListener) context;
        }
    }
}