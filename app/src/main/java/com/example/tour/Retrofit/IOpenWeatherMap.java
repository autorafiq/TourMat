package com.example.tour.Retrofit;

import com.example.tour.Model.WeatherForecastResualt;
import com.example.tour.Model.WeatherResualt;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("weather")
    Observable<WeatherResualt> getWeatherByLatlng(@Query("lat") String lat,
                                                  @Query("lon") String lon,
                                                  @Query("appid") String appid,
                                                  @Query("units") String units);

    @GET("forecast")
    Observable<WeatherForecastResualt> getForecastWeatherByLatlng(@Query("lat") String lat,
                                                                  @Query("lon") String lon,
                                                                  @Query("appid") String appid,
                                                                  @Query("units") String units);

}
