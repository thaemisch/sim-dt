import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class settings extends StatelessWidget {
  const settings({super.key});

  void handleResetPressed() async {
    autoBackup();
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setInt('queue', 0);
    prefs.setBool('orderOccupied', false);
    prefs.setBool('pickupOccupied', false);
    prefs.setInt('exit', 0);
  }

  void autoBackup() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setInt('oldQueue', prefs.getInt('queue') as int);
    prefs.setBool('oldOrderOccupied', prefs.getBool('orderOccupied') as bool);
    prefs.setBool('oldPickupOccupied', prefs.getBool('pickupOccupied') as bool);
    prefs.setInt('oldExit', prefs.getInt('exit') as int);
  }

  void handleRestorePressed() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setInt('queue', prefs.getInt('oldQueue') as int);
    prefs.setBool('orderOccupied', prefs.getBool('oldOrderOccupied') as bool);
    prefs.setBool('pickupOccupied', prefs.getBool('oldPickupOccupied') as bool);
    prefs.setInt('exit', prefs.getInt('oldExit') as int);
  }

  @override
  Widget build(BuildContext context) {
    final Size screenSize = MediaQuery.of(context).size;
    final double screenWidth = screenSize.width;
    final double screenHeight = screenSize.height - 200;
    return SizedBox(
      height: screenHeight / 10,
      child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            SizedBox(
              width: screenWidth,
              height: 50,
              child: ElevatedButton(
                  onPressed: () {
                    handleResetPressed();
                  },
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Text('RESET DATA'),
                    ],
                  )),
            ),
            SizedBox(
              width: screenWidth,
              height: 50,
              child: ElevatedButton(
                  onPressed: () {
                    handleRestorePressed();
                  },
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Text('RESTORE LAST DATA'),
                    ],
                  )),
            ),
          ]),
    );
  }
}
