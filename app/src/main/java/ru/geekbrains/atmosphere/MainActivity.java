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

import java.util.ArrayList;
import java.util.Calendar;

import ru.geekbrains.atmosphere.cities.Cities;
import ru.geekbrains.atmosphere.city_weather.CityWeatherSource;
import ru.geekbrains.atmosphere.city_weather.CityWeatherSourceBuilder;
import ru.geekbrains.atmosphere.settings.Settings;

public class MainActivity extends AppCompatActivity
        implements
        SettingsFragment.OnUpdateSettingsListener,
        CitiesFragment.OnUpdateCitiesListener,
        ExtraConstants,
        NavigationView.OnNavigationItemSelectedListener {

    private static final String CLASS = MainActivity.class.getSimpleName();
    private static final boolean LOGGING = false;

    private boolean landscapeOrientation;
    private CityWeatherSource dataSource;
    private Settings settings;
    private Cities cities;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigationAndMenuElements();
        //setTheme(getCurrentTheme());

        if (savedInstanceState == null) {
            initApp();
        } else {
            restoreApp(savedInstanceState);
        }

        if (LOGGING) {
            Log.d(CLASS, "OnCreate. Landscape orientation - " + landscapeOrientation);
            Log.d(CLASS, "OnCreate. Settings - " + settings);
            Log.d(CLASS, "OnCreate. Cities - " + cities);
            Log.d(CLASS, "OnCreate. Data source - " + dataSource);
        }
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
            // TODO: Отображать кнопку во фсех фрагментах, кроме фрагмента с городами
            if (activeFragment instanceof CitiesFragment) {
                Toast.makeText(this, "it's Cities Fragment", Toast.LENGTH_LONG).show();
            } else {
                onChangeFragment(CitiesFragment.create(cities));
                Toast.makeText(this, "toolbar_add", Toast.LENGTH_LONG).show();
            }
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
        dataSource = new CityWeatherSourceBuilder().setResources(getResources(), cities).build();
        switch (savedInstanceState.getInt(ACTIVE_FRAGMENT, 0)) {
            case 0:
                onChangeFragment(CityWeatherFragment.create(dataSource, landscapeOrientation));
                Toast.makeText(this, "create CityWeatherFragment", Toast.LENGTH_LONG).show();
            case 1:
                onChangeFragment(SettingsFragment.create(settings));
                Toast.makeText(this, "create SettingsFragment", Toast.LENGTH_LONG).show();
            case 2:
                onChangeFragment(CitiesFragment.create(cities));
                Toast.makeText(this, "create CitiesFragment", Toast.LENGTH_LONG).show();
        }
    }

    private void initApp() {
        landscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        SharedPreferences sharedPreferences = getSharedPreferences(SETTINGS, MODE_PRIVATE);
        if (sharedPreferences != null) {
            int theme = sharedPreferences.getInt(THEME, Settings.DEFAULT_THEME);
            boolean allDetail = sharedPreferences.getBoolean(ALL_DETAIL, Settings.DEFAULT_ALL_DETAIL);
            settings = new Settings(theme, allDetail ? 1 : 0);
        }

        cities = new Cities(getResources().getStringArray(R.array.cities));
        dataSource = new CityWeatherSourceBuilder().setResources(getResources(), cities).build();
        onChangeFragment(CityWeatherFragment.create(dataSource, landscapeOrientation));
    }

    public void onChangeFragment(androidx.fragment.app.Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.weatherFragment, fragment).addToBackStack(String.valueOf(fragment.getId())).commit();
        activeFragment = fragment;
    }

    @Override
    public void onUpdateSettings(Settings settings) {
        this.settings = settings;
        Log.i(CLASS, this.settings.toString());
        setMyTheme(settings);
        //recreate();
    }

    @Override
    public void onUpdateCities(Cities cities) {
        this.cities = cities;
        Log.i(CLASS, this.cities.toString());
    }

    public void setMyTheme(Settings settings) {
        SharedPreferences sharedPref = getSharedPreferences(SETTINGS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(THEME, settings.getTheme());
        editor.putBoolean(ALL_DETAIL, settings.isAllDetail());
        editor.apply();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        Log.i(CLASS, "onSaveInstanceState");
        outState.putParcelable(SETTINGS, settings);
        outState.putParcelable(CITIES, cities);
        int fragment = 0;
        if (activeFragment instanceof CityWeatherFragment) {
            fragment = 0;
        } else if (activeFragment instanceof SettingsFragment) {
            fragment = 1;
        } else if (activeFragment instanceof CitiesFragment) {
            fragment = 2;
        }
        outState.putInt(ACTIVE_FRAGMENT, fragment);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_weather:
                onChangeFragment(CityWeatherFragment.create(dataSource, landscapeOrientation));
                Toast.makeText(this, "nav_weather", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_settings:
                onChangeFragment(SettingsFragment.create(settings));
                Toast.makeText(this, "nav_settings", Toast.LENGTH_LONG).show();
                break;
            case R.id.nav_city_choose:
                onChangeFragment(CitiesFragment.create(cities));
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
                onChangeFragment(CitiesFragment.create(cities));
                Toast.makeText(this, "toolbar_add", Toast.LENGTH_LONG).show();
                return true;
            case R.id.toolbar_clear:
                cities.setCities(new ArrayList<>());
                dataSource.rebuild(cities);
                Toast.makeText(this, "toolbar_clear", Toast.LENGTH_LONG).show();
                return true;
            case R.id.toolbar_settings:
                onChangeFragment(SettingsFragment.create(settings));
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