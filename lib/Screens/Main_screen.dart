import 'package:flutter/material.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:untitled/Screens/Side_drawer.dart';
import 'package:untitled/Utils/Weather.dart';
import 'Side_drawer.dart';

class MainScreen extends StatefulWidget {

  final WeatherData weatherData;

  MainScreen({required this.weatherData});
  @override
  _MainScreenState createState() => _MainScreenState();
}
 void temperatureImage() {
  if(temperature>25) {
   Image(
     image: AssetImage('assets/sunny.png'),
     width: 200,
     height: 150,
   );
  }
  else {
    Image(
      image: AssetImage('assets/raining.png'),
      width: 200,
      height: 150,
    );
  }
}
late int temperature;
class _MainScreenState extends State<MainScreen> {


  late Icon weatherDisplayIcon;
  late AssetImage backgroundImage;
  late String city;
  late int humidity;
  late int windSpeed;
  late int clouds;

  void updateDisplayInfo(WeatherData weatherData) {
    setState(() {
      //statik verileri anlık olarak değiştirmek için setstate kullanırız.
      temperature =
          weatherData.currentTemperature.round(); //round ile değeri yuvarlarız.
      city = weatherData.city;
      humidity = weatherData.humidity;
      windSpeed = weatherData.windSpeed.round();
      clouds = weatherData.clouds.round();
      WeatherDisplayData weatherDisplayData = weatherData
          .getWeatherDisplayData();
      backgroundImage = weatherDisplayData.weatherImage;
      weatherDisplayIcon = weatherDisplayData.weatherIcon;
    });
  }

  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    updateDisplayInfo(widget.weatherData);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Hava Durumu'),
        backgroundColor: Colors.blueGrey,
      ),
      drawer: MainDrawer(),
      body: Container(
            constraints: BoxConstraints.expand(),
        decoration: BoxDecoration(
          image: DecorationImage(
            image: backgroundImage,
            fit: BoxFit.cover,
          ),
        ),

        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            SizedBox(height: 50,),
            Image(
              image: AssetImage('assets/sunny.png'),
              width: 200,
              height: 150,
            ),
            //SizedBox(height: 60,),
            SizedBox(height: 10,),
            Center(
              child: Text('$temperature°',
                style: TextStyle(
                    color: Colors.white,
                    fontSize: 60.0,
                    letterSpacing: -5
                ),
              ),
            ),
            Image(
              image: AssetImage('assets/location.png'),
              width: 40,
              height: 40,
            ),
            SizedBox(height: 0,),
            Center(

              child: Text(city,
                style: TextStyle(
                    color: Colors.white,
                    fontSize: 40.0,
                    letterSpacing: -5
                ),
              ),
            ),
            Image(
              image: AssetImage('assets/humidity.png'),
              width: 50,
              height: 50,
            ),
            SizedBox(height: 0,),
            Center(
              child: Text("Nem: %$humidity",
                style: TextStyle(
                    color: Colors.white,
                    fontSize: 40.0,
                    letterSpacing: -5
                ),
              ),
            ),
            Image(
              image: AssetImage('assets/wind.png'),
              width: 50,
              height: 50,
            ),
            SizedBox(height: 0,),
            Center(
              child: Text("Rüzgar Hızı: $windSpeed",
                style: TextStyle(
                    color: Colors.white,
                    fontSize: 40.0,
                    letterSpacing: -5
                ),
              ),
            ),
            Image(
              image: AssetImage('assets/raining.png'),
              width: 50,
              height: 50,
            ),
            SizedBox(height: 0,),
            Center(
              child: Text("Yağış : %$clouds",
                style: TextStyle(
                    color: Colors.white,
                    fontSize: 40.0,
                    letterSpacing: -5
                ),
              ),

            ),
          ],
        ),
      ),
    );
  }
}
