import 'package:flutter/material.dart';
import 'package:flutter_vibrate/flutter_vibrate.dart';
import 'package:shared_preferences/shared_preferences.dart';

const Widget divider = SizedBox(height: 10);

const double narrowScreenWidthThreshold = 700;

class history extends StatelessWidget {
  const history({super.key});

  @override
  Widget build(BuildContext context) {
    final Size screenSize = MediaQuery.of(context).size;
    final double screenWidth = screenSize.width;
    final double screenHeight = screenSize.height - 200;

    return SizedBox(
      width: screenWidth,
      height: screenHeight,
      child: Column(
        children: [
          StatCard(title: 'N/A', subtitle: 'N/A'),
        ],
      ),
    );
  }
}

class StatCard extends StatelessWidget {
  final String title;
  final String subtitle;

  StatCard({required this.title, required this.subtitle});

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        title: Text(
          title,
          style: TextStyle(fontSize: 18.0, fontWeight: FontWeight.bold),
        ),
        subtitle: Text(subtitle),
      ),
    );
  }
}
