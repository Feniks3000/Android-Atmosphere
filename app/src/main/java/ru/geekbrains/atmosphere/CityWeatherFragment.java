package ru.geekbrains.atmosphere;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.atmosphere.city_weather.CityWeatherAdapter;
import ru.geekbrains.atmosphere.city_weather.CityWeatherSource;

public class CityWeatherFragment extends Fragment implements View.OnClickListener, ExtraConstants {

    private static final String CLASS = CityWeatherFragment.class.getSimpleName();
    private static final boolean LOGGING = false;

    private CityWeatherSource cityWeatherSource;
    private boolean landscapeOrientation;
    private MainActivity mainActivity;

    public CityWeatherFragment() {
    }

    public static CityWeatherFragment create(CityWeatherSource cityWeatherSource, boolean landscapeOrientation) {
        CityWeatherFragment fragment = new CityWeatherFragment();
        Bundle args = new Bundle();
        args.putParcelable(DATA_SOURCE, cityWeatherSource);
        args.putBoolean(LANDSCAPE_ORIENTATION, landscapeOrientation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cityWeatherSource = getArguments().getParcelable(DATA_SOURCE);
            landscapeOrientation = getArguments().getBoolean(LANDSCAPE_ORIENTATION);
        }
        mainActivity = (MainActivity) getActivity();

        if (LOGGING) {
            Log.d(CLASS, "OnCreate. Data source - " + cityWeatherSource);
            Log.d(CLASS, "OnCreate. Landscape orientation - " + landscapeOrientation);
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_city_weather, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.cityWeatherCards);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(landscapeOrientation ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), landscapeOrientation ? LinearLayoutManager.VERTICAL : LinearLayoutManager.HORIZONTAL);
        itemDecoration.setDrawable(landscapeOrientation ? getContext().getDrawable(R.drawable.separator_horizontal) : getContext().getDrawable(R.drawable.separator_vertical));
        recyclerView.addItemDecoration(itemDecoration);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(500);
        animator.setRemoveDuration(500);
        recyclerView.setItemAnimator(animator);

        CityWeatherAdapter adapter = new CityWeatherAdapter(cityWeatherSource);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((view1, position) -> {
            String activeCity = ((TextView) view1.findViewById(R.id.city)).getText().toString();
            mainActivity.updateActiveCity(activeCity);
            Log.i(CLASS, "Active city - " + activeCity);
            Toast.makeText(getContext(), String.format("City %s, position %d", activeCity, position), Toast.LENGTH_SHORT).show();
        });
        return view;
    }

    @Override
    public void onClick(View v) {
    }
}