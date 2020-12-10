package ru.geekbrains.atmosphere;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import ru.geekbrains.atmosphere.city_weather.CityWeatherSource;
import ru.geekbrains.atmosphere.city_weather.CityWeatherSourceBuilder;
import ru.geekbrains.atmosphere.settings.Cities;
import ru.geekbrains.atmosphere.settings.Settings;

public class MainActivity extends AppCompatActivity
        implements
        SettingsFragment.OnUpdateSettingsAndCitiesListener,
        CityWeatherFragment.OnUpdateActiveCityListener,
        ButtonsFragment.GetDataListener,
        OnChangeFragmentListener,
        ExtraConstants,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String CLASS = MainActivity.class.getSimpleName();
    private static final boolean LOGGING = false;

    private boolean landscapeOrientation;
    private CityWeatherSource dataSource;
    private Settings settings;
    private Cities cities;
    private String activeCity;

    CityWeatherFragment cityWeatherFragment;
    ButtonsFragment buttonsFragment;
    Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTheme(getCurrentTheme());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Инициализируем основные параметры приложения
        if (savedInstanceState == null) {
            initApp();
        } else {
            restoreApp(savedInstanceState);
        }

        if (LOGGING) {
            Log.d(CLASS, "OnCreate. Landscape orientation - " + landscapeOrientation);
            Log.d(CLASS, "OnCreate. Data source - " + dataSource);
            Log.d(CLASS, "OnCreate. Settings - " + settings);
            Log.d(CLASS, "OnCreate. Cities - " + cities);
        }

        createCityWeatherFragment();    // Создаем основной фрагмент с погодой
        createButtonsFragment();        // Создаем фрагмент с кнопками

    }

    private int getCurrentTheme() {
        Integer theme = 0;
        SharedPreferences sharedPreferences = getSharedPreferences(SETTINGS, MODE_PRIVATE);
        if (sharedPreferences != null) {
            theme = sharedPreferences.getInt(THEME, 0);
        }
        switch (theme) {
            case 0:
                Log.d(CLASS, "theme Day");
                return R.style.DayTheme;
            case 1:
                Log.d(CLASS, "theme Night");
                return R.style.NightTheme;
            case 2:
                Log.d(CLASS, "theme Auto");
                Calendar calendar = Calendar.getInstance();
                int nowHour = calendar.get(Calendar.HOUR_OF_DAY);
                if (nowHour >= 9 && nowHour <= 21) {
                    return R.style.DayTheme;
                } else {
                    return R.style.NightTheme;
                }
            default:
                return R.style.DayTheme;
        }
    }

    private void restoreApp(Bundle savedInstanceState) {
        landscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        settings = savedInstanceState.getParcelable(SETTINGS);
        cities = savedInstanceState.getParcelable(CITIES);
        activeCity = savedInstanceState.getString(ACTIVE_CITY);
        dataSource = savedInstanceState.getParcelable(DATA_SOURCE);
    }

    private void initApp() {
        cities = new Cities(getResources().getStringArray(R.array.cities));
        settings = new Settings(Settings.DEFAULT_THEME, Settings.DEFAULT_ALL_DETAIL);
        landscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        activeCity = cities.getFirstCity();
        dataSource = new CityWeatherSourceBuilder().setResources(getResources(), cities).build();
    }

    private void createCityWeatherFragment() {
        cityWeatherFragment = CityWeatherFragment.create(dataSource, landscapeOrientation);
        getSupportFragmentManager().beginTransaction().replace(R.id.weatherFragment, cityWeatherFragment).commit();
    }

    private void updateCityWeatherFragment() {
        dataSource.rebuild(cities);
        createCityWeatherFragment();
    }

    private void createButtonsFragment() {
        buttonsFragment = ButtonsFragment.create(settings, cities);
        if (landscapeOrientation) {
            getSupportFragmentManager().beginTransaction().replace(R.id.infoFragment, buttonsFragment).commit();
        } else {
//            getSupportFragmentManager().beginTransaction().replace(R.id.buttonsFragment, buttonsFragment).commit();
        }
    }

    // Заменяем фрагменты в макете
    @Override
    public void onChangeFragment(androidx.fragment.app.Fragment fragment, boolean needCityWeather) {
        if (landscapeOrientation) {
            if (needCityWeather) {
                updateCityWeatherFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.infoFragment, fragment).addToBackStack(String.valueOf(fragment.getId())).commit();
        } else {
            if (needCityWeather) {
                getSupportFragmentManager().beginTransaction().remove(activeFragment).commit();
                updateCityWeatherFragment();
                createButtonsFragment();
            } else {
                getSupportFragmentManager().beginTransaction().remove(cityWeatherFragment).commit();
                getSupportFragmentManager().beginTransaction().remove(buttonsFragment).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity, fragment).addToBackStack(String.valueOf(fragment.getId())).commit();
            }
            activeFragment = fragment;
        }
    }

    @Override
    public void onUpdateSettingsAndCities(Settings settings, Cities cities) {
        this.settings = settings;
        this.cities = cities;
        setMyTheme(settings);
        recreate();
    }

    public void setMyTheme(Settings settings) {
        SharedPreferences sharedPref = getSharedPreferences(SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(THEME, settings.getTheme());
        editor.putBoolean(ALL_DETAIL, settings.isAllDetail());
        editor.apply();
    }

    @Override
    public void onUpdateActiveCity(String activeCity) {
        this.activeCity = activeCity;
    }

    @Override
    public String getActiveCity() {
        return activeCity;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i(CLASS, "onSaveInstanceState");
        outState.putParcelable(SETTINGS, settings);
        outState.putParcelable(CITIES, cities);
        outState.putString(ACTIVE_CITY, activeCity);
        outState.putParcelable(DATA_SOURCE, dataSource);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Log.i(CLASS, "Navigation " + id);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        final SearchView searchText = (SearchView) search.getActionView();

        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(searchText, query, Snackbar.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        } );

        return true;
    }
}