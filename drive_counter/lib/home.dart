import 'dart:io';
import 'package:drive_counter/main.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:flutter_vibrate/flutter_vibrate.dart';
import 'dart:convert';
import 'package:path_provider/path_provider.dart';
import 'package:file_picker/file_picker.dart';

class home extends StatefulWidget {
  const home({super.key});

  @override
  State<home> createState() => _homeState();
}

class _homeState extends State<home> {
  @override
  initState() {
    super.initState();
    getQueueSP();
    getOrderSP();
    getPickupQueueSP();
    getPickupSP();
    getExitSP();
  }

  int queue = 0;
  bool orderOccupied = false;
  int pickupQueueCount = 0;
  bool pickupOccupied = false;
  int exit = 0;

  List<DateTime> queueEntry = [];
  List<DateTime> orderEntry = [];
  List<DateTime> pickupQueue = [];
  List<DateTime> pickupEntry = [];
  List<DateTime> exitEntry = [];

  void handleQueuePressed() {
    setState(() {
      queue++;
    });
    queueEntry.add(DateTime.now());
    setQueueSP();
  }

  void setQueueSP() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setInt('queue', queue);
    prefs.setString('queueEntry', queueEntry.toString());
  }

  void getQueueSP() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    queue = prefs.getInt('queue') as int;
    queueEntry = prefs.getString('queueEntry') as List<DateTime>;
    setState(() {
      queue = queue;
    });
  }

  void handleOrderPressed() async {
    if (orderOccupied) {
      setState(() {
        queue++;
      });
      queueEntry.add(DateTime.now());
      setQueueSP();
    } else if (queue > 0 && !orderOccupied) {
      setState(() {
        queue--;
        orderOccupied = true;
      });
      orderEntry.add(DateTime.now());
      setQueueSP();
      setOrderSP();
    } else {
      setState(() {
        orderOccupied = true;
      });
      queueEntry.add(DateTime.now());
      orderEntry.add(DateTime.now());
      setQueueSP();
      setOrderSP();
    }
  }

  void setOrderSP() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setBool('orderOccupied', orderOccupied);
    prefs.setString('orderEntry', orderEntry.toString());
  }

  void getOrderSP() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    orderOccupied = prefs.getBool('orderOccupied') as bool;
    orderEntry = prefs.getString('orderEntry') as List<DateTime>;
    setState(() {
      orderOccupied = orderOccupied;
    });
  }

  void setPickupQueueSP() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setInt('pickupQueueCount', pickupQueueCount);
    prefs.setString('pickupQueue', pickupQueue.toString());
  }

  void getPickupQueueSP() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    pickupQueueCount = prefs.getInt('pickupQueueCount') as int;
    pickupQueue = prefs.getString('pickupQueue') as List<DateTime>;
    setState(() {
      pickupQueueCount = pickupQueueCount;
    });
  }

  void handlePickupPressed() {
    if (pickupOccupied && orderOccupied) {
      setState(() {
        orderOccupied = false;
        pickupQueueCount++;
      });
      pickupQueue.add(DateTime.now());
      setPickupQueueSP();
    } else if (!pickupOccupied && pickupQueueCount > 0) {
      setState(() {
        pickupQueueCount--;
        pickupOccupied = true;
      });
      pickupEntry.add(DateTime.now());
      setPickupSP();
      setPickupQueueSP();
    } else if (orderOccupied) {
      setState(() {
        pickupOccupied = true;
        orderOccupied = false;
      });
      pickupEntry.add(DateTime.now());
      setPickupSP();
      setOrderSP();
    }
  }

  void setPickupSP() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setBool('pickupOccupied', pickupOccupied);
    prefs.setString('pickupEntry', pickupEntry.toString());
  }

  void getPickupSP() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    pickupOccupied = prefs.getBool('pickupOccupied') as bool;
    pickupEntry = prefs.getString('pickupEntry') as List<DateTime>;
    setState(() {
      pickupOccupied = pickupOccupied;
    });
  }

  void handleExitPressed() {
    setState(() {
      exit++;
      pickupOccupied = !pickupOccupied;
    });
    exitEntry.add(DateTime.now());
    setExitSP();
  }

  void setExitSP() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setInt('exit', exit);
    prefs.setString('exitEntry', exitEntry.toString());
  }

  void getExitSP() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    exit = prefs.getInt('exit') as int;
    exitEntry = prefs.getString('exitEntry') as List<DateTime>;
    setState(() {
      exit = exit;
    });
  }

  void vibrateWarning() {
    Vibrate.feedback(FeedbackType.warning);
  }

  void vibrateSuccess() {
    Vibrate.feedback(FeedbackType.success);
  }

  List<List<String>> convertListsToJson(List<List<DateTime>> lists) {
    return lists.map((list) {
      return list.map((dateTime) => dateTime.toIso8601String()).toList();
    }).toList();
  }

  Future<void> writeListsToFile() async {
    // Convert the lists to a JSON-serializable format
    List<List<String>> jsonLists = convertListsToJson(
        [queueEntry, orderEntry, pickupQueue, pickupEntry, exitEntry]);

    // Get the device's documents directory
    Directory directory = await getApplicationDocumentsDirectory();
    String filePath = directory.path + '/lists.json';

    // Convert the lists to a JSON string
    String jsonStr = jsonEncode(jsonLists);

    // Write the JSON string to the file
    File file = File(filePath);
    await file.writeAsString(jsonStr);
  }

//test
  @override
  Widget build(BuildContext context) {
    final Size screenSize = MediaQuery.of(context).size;
    final double screenWidth = screenSize.width;
    final double screenHeight = screenSize.height - 270;
    return Column(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: [
          SizedBox(
            width: screenWidth,
            height: screenHeight / 4,
            child: ElevatedButton(
                onPressed: () {
                  handleOrderPressed();
                },
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Text('ORDER'),
                    if (orderOccupied)
                      Text('OCCUPIED | Queue: $queue')
                    else
                      Text('EMPTY | Queue: $queue'),
                  ],
                )),
          ),
          SizedBox(
            width: screenWidth,
            height: screenHeight / 4,
            child: ElevatedButton(
                onPressed: () {
                  handlePickupPressed();
                },
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Text('PICKUP'),
                    if (pickupOccupied)
                      Text('OCCUPIED | Queue: $pickupQueueCount')
                    else
                      Text('EMPTY | Queue: $pickupQueueCount'),
                  ],
                )),
          ),
          SizedBox(
            width: screenWidth,
            height: screenHeight / 4,
            child: ElevatedButton(
                onPressed: () {
                  if (pickupOccupied) {
                    handleExitPressed();
                    vibrateSuccess();
                  }
                },
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Text('EXIT'),
                    Text('$exit have exited'),
                  ],
                )),
          ),
          SizedBox(
            width: screenWidth,
            height: screenHeight / 4,
            child: ElevatedButton(
                onPressed: () {
                  writeListsToFile();
                },
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Text('WRITE TO FILE'),
                  ],
                )),
          ),
        ]);
  }
}

const List<NavigationDestination> appBarDestinations = [
  NavigationDestination(
    tooltip: "",
    icon: Icon(Icons.home),
    label: 'Home',
    selectedIcon: Icon(Icons.home),
  ),
  NavigationDestination(
    tooltip: "",
    icon: Icon(Icons.data_array),
    label: 'Data',
    selectedIcon: Icon(Icons.data_array),
  ),
  NavigationDestination(
    tooltip: "",
    icon: Icon(Icons.settings),
    label: 'Settings',
    selectedIcon: Icon(Icons.settings),
  ),
];

final List<NavigationRailDestination> navRailDestinations = appBarDestinations
    .map(
      (destination) => NavigationRailDestination(
        icon: Tooltip(
          message: destination.label,
          child: destination.icon,
        ),
        selectedIcon: Tooltip(
          message: destination.label,
          child: destination.selectedIcon,
        ),
        label: Text(destination.label),
      ),
    )
    .toList();

class NavigationBars extends StatefulWidget {
  final void Function(int)? onSelectItem;
  final int selectedIndex;
  final bool isExampleBar;

  const NavigationBars(
      {super.key,
      this.onSelectItem,
      required this.selectedIndex,
      required this.isExampleBar});

  @override
  State<NavigationBars> createState() => _NavigationBarsState();
}

class _NavigationBarsState extends State<NavigationBars> {
  int _selectedIndex = 0;

  @override
  void initState() {
    super.initState();
    _selectedIndex = widget.selectedIndex;
  }

  @override
  Widget build(BuildContext context) {
    return NavigationBar(
      selectedIndex: _selectedIndex,
      onDestinationSelected: (index) {
        setState(() {
          _selectedIndex = index;
        });
        if (!widget.isExampleBar) widget.onSelectItem!(index);
      },
      destinations: appBarDestinations,
    );
  }
}

class NavigationRailSection extends StatefulWidget {
  final void Function(int) onSelectItem;
  final int selectedIndex;

  const NavigationRailSection(
      {super.key, required this.onSelectItem, required this.selectedIndex});

  @override
  State<NavigationRailSection> createState() => _NavigationRailSectionState();
}

class _NavigationRailSectionState extends State<NavigationRailSection> {
  int _selectedIndex = 0;

  @override
  void initState() {
    super.initState();
    _selectedIndex = widget.selectedIndex;
  }

  @override
  Widget build(BuildContext context) {
    return NavigationRail(
      minWidth: 50,
      destinations: navRailDestinations,
      selectedIndex: _selectedIndex,
      useIndicator: true,
      onDestinationSelected: (index) {
        setState(() {
          _selectedIndex = index;
        });
        widget.onSelectItem(index);
      },
    );
  }
}
