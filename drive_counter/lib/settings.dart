import 'package:flutter/material.dart';

class settings extends StatelessWidget {
  const settings({super.key});

  void handleResetPressed() {}

  @override
  Widget build(BuildContext context) {
    final Size screenSize = MediaQuery.of(context).size;
    final double screenWidth = screenSize.width;
    final double screenHeight = screenSize.height - 200;
    return Column(
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
        ]);
  }
}
