package com.example.tour;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tour.Adapter.WeatherForecastAdapter;
import com.example.tour.Common.Common;
import com.example.tour.Model.WeatherForecastResualt;
import com.example.tour.Retrofit.IOpenWeatherMap;
import com.example.tour.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {
    private CompositeDisposable compositeDisposable;
    private IOpenWeatherMap mService;
    private TextView txt_city_name, txt_geo_coord;
    private RecyclerView recycler_forecast;

    static ForecastFragment instance;

    public static ForecastFragment getInstance() {
        if (instance == null) {
            instance = new ForecastFragment();
        }
        return instance;
    }

    public ForecastFragment() {
        // Required empty public constructor
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_forecast, container, false);
        txt_city_name = (TextView) itemView.findViewById(R.id.txt_city_name_id);
        txt_geo_coord = (TextView) itemView.findViewById(R.id.txt_geo_coord_id);
        recycler_forecast = (RecyclerView) itemView.findViewById(R.id.recycler_forecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getForecastWeatherInformation();
        return itemView;
    }

    private void getForecastWeatherInformation() {
        compositeDisposable.add(mService.getForecastWeatherByLatlng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResualt>() {
                    @Override
                    public void accept(WeatherForecastResualt weatherForecastResualt) throws Exception {
                        displayForecastWeather(weatherForecastResualt);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Error: ", "" + throwable.getMessage());
                    }
                })
        );
    }
    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    private void displayForecastWeather(WeatherForecastResualt weatherForecastResualt) {
        txt_city_name.setText(new StringBuilder(weatherForecastResualt.city.name));
        txt_geo_coord.setText(new StringBuilder(weatherForecastResualt.city.coord.toString()));
        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getContext(),weatherForecastResualt);
        recycler_forecast.setAdapter(adapter);
    }

}
