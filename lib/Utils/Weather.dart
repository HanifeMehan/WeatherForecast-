import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:http/http.dart' as http;
import 'package:http/http.dart';
import 'package:untitled/Utils/Location.dart';

const apiKey = "aef2a85fe3250c69709ee5b4c3a1df8a";

class WeatherDisplayData{
  late Icon weatherIcon;
  late AssetImage weatherImage;

  WeatherDisplayData({required this.weatherIcon,required this.weatherImage});
}

class WeatherData{

  WeatherData({required this.locationData});//locationData nın içindeki her şeyi kullanmak için;

  LocationHelper locationData;

  late double currentTemperature;
  late int currentCondition;
  late String city;
  late int humidity;//nem
  late double windSpeed;
  late int clouds;

  Future<void> getCurrentTemperature() async{

    Response response = await get(Uri.parse("https://api.openweathermap.org/data/2.5/weather?lat=${locationData.latitute}&lon=${locationData.longitude}&appid=${apiKey}&units=metric"));


    if(response.statusCode == 200){
      String data = response.body;
       var currentWeather = jsonDecode(data);

       try{
         currentTemperature = currentWeather['main']['temp'];
         currentCondition = currentWeather['weather'][0]['id'];
         city=currentWeather['name'];
         humidity=currentWeather['main']['humidity'];
         windSpeed=currentWeather['wind']['speed'];
         clouds=currentWeather['clouds']['all'];

       }catch(e){
         print(e);
       }
    }else{
      print("API den değer gelmiyor");
    }
  }

  WeatherDisplayData getWeatherDisplayData(){
    //hava bulutlu
    if(currentCondition <600){
      return WeatherDisplayData(
        weatherIcon: Icon(
            FontAwesomeIcons.cloud,
            size:75.0,
          color:Colors.white,
      ),
      weatherImage: AssetImage('assets/bulutlu.png'));
    }
    else{
      //hava çok iyi
      //gece gündüz kontrolü
      var now =  new DateTime.now();
      if(now.hour >=19){
        //akşam
        return WeatherDisplayData(weatherIcon: Icon(
          FontAwesomeIcons.moon,
          size: 75.0,
          color: Colors.white,
        ),
            weatherImage: AssetImage('assets/gece.png'));
      }
      else{
        //güneşli
        return WeatherDisplayData(weatherIcon:
        Icon(
          FontAwesomeIcons.sun,
          size: 75.0,
          color: Colors.white,
        ),
            weatherImage: AssetImage('assets/gunesli2.jpg'));
      }

    }
  }


}