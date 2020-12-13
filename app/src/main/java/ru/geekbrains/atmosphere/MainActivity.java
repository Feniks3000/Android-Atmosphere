package ru.geekbrains.atmosphere;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import ru.geekbrains.atmosphere.city_weather.CityWeatherSource;
import ru.geekbrains.atmosphere.city_weather.CityWeatherSourceBuilder;
import ru.geekbrains.atmosphere.settings.Cities;
import ru.geekbrains.atmosphere.settings.Settings;

public class MainActivity extends AppCompatActivity
        implements
        SettingsFragment.OnUpdateSettingsListener,
        CitiesFragment.OnUpdateCitiesListener,
        CityWeatherFragment.OnUpdateActiveCityListener,
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
    Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigationAndMenuElements();
        //setTheme(getCurrentTheme());

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
    }

    private void initNavigationAndMenuElements() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            // TODO: Добавить открытие формы добавления города
            onChangeFragment(CitiesFragment.create(cities), false);
            Toast.makeText(this, "toolbar_add", Toast.LENGTH_LONG).show();
        });
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

    // Заменяем фрагменты в макете
    @Override
    public void onChangeFragment(androidx.fragment.app.Fragment fragment, boolean needCityWeather) {
        if (landscapeOrientation) {
            if (needCityWeather) {
                updateCityWeatherFragment();
            }
//            getSupportFragmentManager().beginTransaction().replace(R.id.infoFragment, fragment).addToBackStack(String.valueOf(fragment.getId())).commit();
        } else {
            if (needCityWeather) {
                getSupportFragmentManager().beginTransaction().remove(activeFragment).commit();
                updateCityWeatherFragment();
            } else {
                getSupportFragmentManager().beginTransaction().remove(cityWeatherFragment).commit();
//                getSupportFragmentManager().beginTransaction().remove(buttonsFragment).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainActivity, fragment).addToBackStack(String.valueOf(fragment.getId())).commit();
            }
            activeFragment = fragment;
        }
    }

    @Override
    public void onUpdateSettings(Settings settings) {
        this.settings = settings;
        setMyTheme(settings);
        //recreate();
    }

    @Override
    public void onUpdateCities(Cities cities) {
        this.cities = cities;
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
        switch (id) {
            case R.id.nav_weather:
                onChangeFragment(CityWeatherFragment.create(dataSource, landscapeOrientation), false);
                Toast.makeText(this, "nav_weather", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_settings:
                onChangeFragment(SettingsFragment.create(settings), false);
                Toast.makeText(this, "nav_settings", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_city_choose:
                onChangeFragment(CitiesFragment.create(cities), false);
                Toast.makeText(this, "nav_city_choose", Toast.LENGTH_LONG).show();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem search = menu.findItem(R.id.toolbar_search);
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
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.toolbar_add:
                onChangeFragment(CitiesFragment.create(cities), false);
                Toast.makeText(this, "toolbar_add", Toast.LENGTH_LONG).show();
                return true;
            case R.id.toolbar_clear:
                Toast.makeText(this, "toolbar_clear", Toast.LENGTH_LONG).show();
                return true;
            case R.id.toolbar_settings:
                onChangeFragment(SettingsFragment.create(settings), false);
                Toast.makeText(this, "toolbar_settings", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        int id = v.getId();
        switch (id) {
            case R.id.cityWeatherCard:
                inflater.inflate(R.menu.context_weather, menu);
                break;
            case R.id.content:
                inflater.inflate(R.menu.context_cities, menu);
                break;
        }
    }
}