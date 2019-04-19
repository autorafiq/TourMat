package com.example.tour.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tour.Common.Common;
import com.example.tour.Model.WeatherForecastResualt;
import com.example.tour.R;
import com.squareup.picasso.Picasso;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.ViewHolder> {
    private Context context;
    private WeatherForecastResualt weatherForecastResualt;

    public WeatherForecastAdapter(Context context, WeatherForecastResualt weatherForecastResualt) {
        this.context = context;
        this.weatherForecastResualt = weatherForecastResualt;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_weather_forecast,viewGroup,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
//load image
        Picasso.with(context).load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResualt.list.get(i).weather.get(0).getIcon())
                .append(".png").toString()).into(viewHolder.img_weather);
        viewHolder.txt_date_time.setText(new StringBuilder(Common.convertUnixToDate(weatherForecastResualt.list.get(i).dt)));
        viewHolder.txt_description.setText(new StringBuilder(weatherForecastResualt.list.get(i).weather.get(0).getDescription()));
        viewHolder.txt_temperature.setText(new StringBuilder(String.valueOf(weatherForecastResualt.list.get(i).main.getTemp())).append("°C"));

    }

    @Override
    public int getItemCount() {
        return weatherForecastResualt.list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_date_time, txt_description, txt_temperature;
        ImageView img_weather;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_weather=(ImageView)itemView.findViewById(R.id.img_weather_id);
            txt_date_time=(TextView)itemView.findViewById(R.id.txt_date_id);
            txt_description=(TextView)itemView.findViewById(R.id.txt_description_id);
            txt_temperature=(TextView)itemView.findViewById(R.id.txt_temperature_id);
        }
    }
}
