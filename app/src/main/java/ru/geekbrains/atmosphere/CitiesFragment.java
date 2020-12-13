package ru.geekbrains.atmosphere;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.regex.Pattern;

import ru.geekbrains.atmosphere.settings.Cities;
import ru.geekbrains.atmosphere.settings.CitiesAdapter;

public class CitiesFragment extends Fragment implements View.OnClickListener, ExtraConstants {

    private static final String CLASS = CitiesFragment.class.getSimpleName();
    private static final boolean LOGGING = false;
    private final Pattern patternCityName = Pattern.compile("^[A-ZА-Я][a-zа-я]{2,}$");

    private OnUpdateCitiesListener onUpdateCitiesListener;

    private CitiesAdapter citiesAdapter;
    private Cities cities;
    private EditText newCity;

    public CitiesFragment() {
    }

    public static CitiesFragment create(Cities cities) {
        CitiesFragment fragment = new CitiesFragment();
        Bundle args = new Bundle();
        args.putParcelable(CITIES, cities);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cities = getArguments().getParcelable(CITIES);
        }

        if (LOGGING) {
            Log.d(CLASS, "OnCreate. Cities - " + cities);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cities, container, false);

        RecyclerView citiesList = view.findViewById(R.id.cityList);
        citiesAdapter = new CitiesAdapter(getContext(), cities.getCities());
        citiesList.setAdapter(citiesAdapter);

        newCity = view.findViewById(R.id.cityChooseText);

        Button addCity = view.findViewById(R.id.addCity);
        addCity.setOnClickListener(button -> {
            if (validate(newCity, patternCityName, getResources().getString(R.string.errorCityName))
                    && !citiesAdapter.existsCity(getNewCity())) {
                citiesAdapter.addCity(getNewCity());
                newCity.setText(null);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnUpdateCitiesListener) {
            onUpdateCitiesListener = (OnUpdateCitiesListener) context;
        }
    }

    private String getNewCity() {
        return newCity.getText().toString();
    }

    private boolean validate(TextView textView, Pattern pattern, String message) {
        String value = textView.getText().toString();
        if (pattern.matcher(value).matches()) {
            textView.setError(null);
            return true;
        } else {
            textView.setError(message);
            return false;
        }
    }

    public interface OnUpdateCitiesListener {
        void onUpdateCities(Cities cities);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.context_move_up:
                citiesAdapter.moveUpCity(citiesAdapter.getPosition());
                break;
            case R.id.context_move_down:
                citiesAdapter.moveDownCity(citiesAdapter.getPosition());
                break;
            case R.id.context_delete:
                citiesAdapter.removeCity(citiesAdapter.getPosition());
                break;
            case R.id.context_clear:
                citiesAdapter.clearCities();
                break;
        }
        onUpdateCitiesListener.onUpdateCities(new Cities(citiesAdapter.getData()));
        return super.onContextItemSelected(item);
    }
}