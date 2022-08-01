import 'package:flutter/material.dart';

class  MainDrawer extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Drawer(
    child:Column(
      children: <Widget>[
        Container(
          width: double.infinity,
          padding: EdgeInsets.all(20),
          color: Theme.of(context).primaryColor,
          child: Center(
            child: Column(
              children:<Widget>[
                Container(
                  margin:EdgeInsets.only(
                    top: 30,
                    bottom: 10,
                  ) ,
                  width: 250,
                  height:110,
                  decoration: BoxDecoration(
                    shape: BoxShape.rectangle,
                    image: DecorationImage(image: NetworkImage("https://larende.com/resimler/haber/karaman-hava-durumu-1600548472.jpg",
                    ),
                        fit:BoxFit.fill
                    ),
                  ),
                ),
                Text('Hava Durumu',
                  style: TextStyle(fontSize: 22,
                      color: Colors.white
                ),
                ),
                Text('Hava Durumu',
                  style: TextStyle(
                      color: Colors.white
                  ),
                ),
              ],
            ),
          ),
        ),
        ListTile(
          leading: Icon(Icons.person),
          title: Text('Profile', style: TextStyle(
            fontSize: 10,
          ),
          ),
          onTap: null,
        ),
        ListTile(
          leading: Icon(Icons.settings),
          title: Text('Settings', style: TextStyle(
            fontSize: 10,
          ),
          ),
          onTap: null,
        ),
        ListTile(
          leading: Icon(Icons.arrow_back),
          title: Text('Logout', style: TextStyle(
            fontSize: 10,
          ),
          ),
          onTap: null,
        ),
      ],
    ),
  );
  }
}
