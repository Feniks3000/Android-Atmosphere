package ru.geekbrains.atmosphere.database;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class History implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "date")
    public long date;

    @ColumnInfo(name = "temp")
    public int temp;

    @ColumnInfo(name = "city")
    public String city;

    public History(long date, int temp, String city) {
        this.date = date;
        this.temp = temp;
        this.city = city;
    }

    public History() {
    }

    protected History(Parcel in) {
        id = in.readLong();
        date = in.readLong();
        temp = in.readInt();
        city = in.readString();
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(date);
        dest.writeInt(temp);
        dest.writeString(city);
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", date=" + new Date(date).toString() +
                ", temp=" + temp +
                ", city='" + city + '\'' +
                '}';
    }
}
