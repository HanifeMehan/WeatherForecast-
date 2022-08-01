import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:untitled/Screens/Main_screen.dart';
import 'package:untitled/Utils/Location.dart';
import 'package:untitled/Utils/Weather.dart';

//Dinamik işlemler yapacağımız için stateful ekledik
class LoadingScreen extends StatefulWidget {

  @override
  _LoadingScreenState createState() => _LoadingScreenState();
}

class _LoadingScreenState extends State<LoadingScreen> {

  late LocationHelper locationData;
  //Lokasyonumuzu çağırırız.
  Future<void> getLocationData() async{
    locationData = LocationHelper();
    await locationData.getCurrentLocation();


    if(locationData.latitute == null || locationData.longitude == null){
      print("konum bilgileri gelmiyor.");
    }else {
      print("latitude" + locationData.latitute.toString());
      print("longitude" + locationData.longitude.toString());
    }

  }

  void getweatherData() async{
    await getLocationData();

    WeatherData weatherData = WeatherData(locationData: locationData);
    await weatherData.getCurrentTemperature();

    if(weatherData.currentTemperature == null || weatherData.currentTemperature ==null){

      print("API den sıcaklık veya durum bilgisi boş dönüyor.");
    }
    Navigator.pushReplacement(context, MaterialPageRoute(builder:(context){
      return MainScreen(weatherData: weatherData,);
    }));
  }



  @override//initState ile sayfayı çağırırız.İlk çalışacak komutlar burada çalışır.
  void initState() {
    // TODO: implement initState
    super.initState();
    getweatherData();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        decoration: BoxDecoration(
          gradient: LinearGradient(
            begin: Alignment.topLeft,
            end: Alignment.bottomCenter,
              colors: [Colors.purple, Colors.blue]
          ),
        ),
        child: Center(
          child: SpinKitFadingCircle(
            color: Colors.white,
            size: 150.0,
            duration: Duration(milliseconds: 1200),
          ),
        ),
      ),
    );

  }
}
