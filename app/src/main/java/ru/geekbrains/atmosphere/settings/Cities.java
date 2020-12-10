package ru.geekbrains.atmosphere.settings;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cities implements Parcelable {
    private List<String> cities;

    public List<String> getCities() {
        return cities;
    }

    public String[] getCitiesArray() {
        return cities.toArray(new String[0]);
    }

    public String getFirstCity() {
        return cities != null ? cities.get(0) : null;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public void addCity(String city) {
        if (cities == null) {
            cities = new ArrayList();
        }
        cities.add(city);
    }

    @Override
    public String toString() {
        return "Cities{" +
                "cities=" + cities +
                '}';
    }

    public Cities(String[] array) {
        cities = new ArrayList<>(Arrays.asList(array));
    }

    protected Cities(Parcel in) {
        cities = in.readArrayList(String.class.getClassLoader());
    }

    public static final Creator<Cities> CREATOR = new Creator<Cities>() {
        @Override
        public Cities createFromParcel(Parcel in) {
            return new Cities(in);
        }

        @Override
        public Cities[] newArray(int size) {
            return new Cities[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(cities);
    }
}
