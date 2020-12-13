package ru.geekbrains.atmosphere.city_weather;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.geekbrains.atmosphere.R;

public class CityWeatherAdapter extends RecyclerView.Adapter<CityWeatherAdapter.ViewHolder> {

    private CityWeatherSource dataSource;
    private int position;
    private OnItemClickListener itemClickListener;
    private final Context context;

    public CityWeatherAdapter(Context context, CityWeatherSource dataSource) {
        this.context = context;
        this.dataSource = dataSource;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_weather_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        if (itemClickListener != null) {
            viewHolder.setOnClickListener(itemClickListener);
            viewHolder.setOnLongClickListener(itemClickListener);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CityWeather cityWeather = dataSource.getCityWeather(position);
        holder.setData(cityWeather);
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView city;
        private TextView temperature;
        private ImageView picture;
        private CardView card;

        public ViewHolder(View view) {
            super(view);
            city = view.findViewById(R.id.city);
            temperature = view.findViewById(R.id.labelTempNow);
            picture = view.findViewById(R.id.imagePrecipitationNow);
            card = view.findViewById(R.id.cityWeatherCard);

        }

        public void setOnClickListener(final OnItemClickListener listener) {
            card.setOnClickListener(view -> {
                int adapterPosition = getAdapterPosition();                 // Получаем позицию адаптера
                if (adapterPosition == RecyclerView.NO_POSITION)
                    return;    // Проверяем ее на корректность
                listener.onItemClick(view, adapterPosition);
            });
        }

        public void setOnLongClickListener(final OnItemClickListener listener) {
            card.setOnLongClickListener(view -> {
                position = getAdapterPosition();                 // Получаем позицию адаптера
                return false;
            });
            ((Activity) context).registerForContextMenu(card);
        }

        public void setData(CityWeather cityWeather) {
            getCity().setText(cityWeather.getCity());
            getTemperature().setText(String.valueOf(cityWeather.getTemperature()));
            getPicture().setImageResource(cityWeather.getPicture());
        }

        public TextView getCity() {
            return city;
        }

        public TextView getTemperature() {
            return temperature;
        }

        public ImageView getPicture() {
            return picture;
        }
    }

    public void addCity(String city){
        dataSource.addCity(city);
        notifyItemInserted(dataSource.size() - 1);
    }

    public void removeCity(int position){
        dataSource.removeCity(position);
        Log.i("Adapter", "Remove city - " + position);
        notifyItemRemoved(position);
    }

    public void clearCities(){
        dataSource.clearCities();
        notifyDataSetChanged();
    }

    public int getPosition() {
        return position;
    }

    public String getCityName() {
        return dataSource.getCityWeather(position).getCity();
    }
}