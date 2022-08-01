
import 'package:location/location.dart';
class LocationHelper{
  //Paralel ve meridyen iki değer kullanılacak
  late double latitute;
  late double longitude;

  Future<void> getCurrentLocation() async{
    Location location = Location();

    bool _serviceEnable;
    PermissionStatus _permissionGranted;
    LocationData _locationData;

    //Location için servis etkin mı?
    _serviceEnable = await location.serviceEnabled();//servisin etkin olup olmadığı değişkene atanır
    if(!_serviceEnable){
      _serviceEnable= await location.requestService();//servisi etkin hale getiririz.
      if(!_serviceEnable){
        return;//boş döner
      }
    }
  //Konum izni kontrolü
    _permissionGranted= await location.hasPermission();
    if(_permissionGranted==PermissionStatus.denied) { //PermissionStatus.denied= izin almamış ise anlamında kullanılır.

      _permissionGranted = await location.requestPermission();//izin ver

      if(_permissionGranted==PermissionStatus.denied){
        return;
      }
    }
    //izinler tamamsa;
    _locationData = await location.getLocation();//Konum alınıp atama yapıldı
    latitute= _locationData.latitude!;
    longitude=_locationData.longitude!;
  }
}