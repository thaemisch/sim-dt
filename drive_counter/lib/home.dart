import 'package:drive_counter/main.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class home extends StatefulWidget {
  const home({super.key});

  @override
  State<home> createState() => _homeState();
}

class _homeState extends State<home> {
  @override
  initState() {
    super.initState();
  }

  int queue = 0;
  bool orderOccupied = false;
  bool pickupOccupied = false;
  int exit = 0;

  void handleQueuePressed() {
    setState(() {
      queue++;
    });
  }

  void setQueueSP() {}
  void getQueueSP() {}

  void handleOrderPressed() {
    setState(() {
      orderOccupied = !orderOccupied;
    });
  }

  void setOrderSP() {}
  void getOrderSP() {}

  void handlePickupPressed() {
    setState(() {
      pickupOccupied = !pickupOccupied;
    });
  }

  void setPickupSP() {}
  void getPickupSP() {}

  void handleExitPressed() {
    setState(() {
      exit++;
    });
  }

  void setExitSP() {}
  void getExitSP() {}

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
                  handleQueuePressed();
                },
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    Text('QUEUE'),
                    Text('is $queue'),
                  ],
                )),
          ),
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
                      Text('is occupied')
                    else
                      Text('is empty'),
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
                      Text('is occupied')
                    else
                      Text('is empty'),
                  ],
                )),
          ),
          SizedBox(
            width: screenWidth,
            height: screenHeight / 4,
            child: ElevatedButton(
                onPressed: () {
                  handleExitPressed();
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
