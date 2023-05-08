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

    String overallCustomers;
    String overallTime;
    String avgTimePerCustomer;
    String avgQueueTime;
    String minQueueTime;
    String maxQueueTime;
    String avgOrderTime;
    String minOrderTime;
    String maxOrderTime;
    String avgPickupTime;
    String minPickupTime;
    String maxPickupTime;

    return SizedBox(
      width: screenWidth,
      height: screenHeight,
      child: Column(
        children: [
          StatCard(title: '5s', subtitle: 'overall customers served'),
          StatCard(title: '5s', subtitle: 'overall time'),
          StatCard(title: '5s', subtitle: 'avg. time per customer'),
          StatCard(title: '5s', subtitle: 'avg. queue time'),
          StatCard(title: '5s', subtitle: 'min. queue time'),
          StatCard(title: '5s', subtitle: 'max. queue time'),
          StatCard(title: '5s', subtitle: 'avg. order time'),
          StatCard(title: '5s', subtitle: 'min. order time'),
          StatCard(title: '5s', subtitle: 'max. order time'),
          StatCard(title: '5s', subtitle: 'avg. pickup time'),
          StatCard(title: '5s', subtitle: 'min. pickup time'),
          StatCard(title: '5s', subtitle: 'max. pickup time'),
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
