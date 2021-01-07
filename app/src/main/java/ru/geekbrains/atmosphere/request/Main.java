package ru.geekbrains.atmosphere.request;

import android.os.Parcel;
import android.os.Parcelable;

public class Main implements Parcelable {
    private float temp;
    private int pressure;
    private int humidity;
    private float tempMin;
    private float tempMax;

    protected Main(Parcel in) {
        temp = in.readFloat();
        pressure = in.readInt();
        humidity = in.readInt();
        tempMin = in.readFloat();
        tempMax = in.readFloat();
    }

    public static final Creator<Main> CREATOR = new Creator<Main>() {
        @Override
        public Main createFromParcel(Parcel in) {
            return new Main(in);
        }

        @Override
        public Main[] newArray(int size) {
            return new Main[size];
        }
    };

    public float getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(temp);
        dest.writeInt(pressure);
        dest.writeInt(humidity);
        dest.writeFloat(tempMin);
        dest.writeFloat(tempMax);
    }
}
