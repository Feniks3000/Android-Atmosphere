package ru.geekbrains.atmosphere.cities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.atmosphere.R;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

    private final List<String> data;
    private final Context context;
    private int position;

    public CitiesAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = new ArrayList<>(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TextView textView = holder.getTextView();
        textView.setText(data.get(position));

        textView.setOnLongClickListener(view -> {
            this.position = position;
            return false;
        });

        ((Activity) context).registerForContextMenu(textView);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getPosition() {
        return position;
    }

    public List<String> getData() {
        return data;
    }

    public void moveUpCity(int position) {
        if (position == 0) {
            return;
        }
        String item = data.remove(position);
        notifyItemRemoved(position);
        data.add(position - 1, item);
        notifyItemInserted(position - 1);
    }

    public void moveDownCity(int position) {
        if (position == data.size() - 1) {
            return;
        }
        String item = data.remove(position);
        notifyItemRemoved(position);
        data.add(position + 1, item);
        notifyItemInserted(position + 1);
    }

    public void addCity(String city) {
        data.add(city);
        notifyItemInserted(data.size() - 1);
    }

    public void removeCity(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public boolean existsCity(String city) {
        return data.contains(city);
    }

    public void clearCities() {
        data.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.content);
        }

        public TextView getTextView() {
            return textView;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textView.getText() + "'";
        }
    }
}