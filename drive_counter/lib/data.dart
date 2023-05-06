import 'package:flutter/material.dart';

const Widget divider = SizedBox(height: 10);

const double narrowScreenWidthThreshold = 700;

class history extends StatelessWidget {
  const history({super.key});

  void handleExportPressed() {}

  void handleImportPressed() {}

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
                    handleExportPressed();
                  },
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Text('EXPORT DATA '),
                      Icon(Icons.file_upload),
                    ],
                  ),
                )),
            SizedBox(
              width: screenWidth,
              height: 50,
              child: ElevatedButton(
                onPressed: () {
                  handleImportPressed();
                },
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Text('IMPORT DATA '),
                    Icon(Icons.file_download),
                  ],
                ),
              ),
            ),
          ]),
    );
  }
}
