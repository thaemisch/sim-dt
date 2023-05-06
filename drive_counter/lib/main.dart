import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

import 'history.dart';
import 'home.dart';
import 'settings.dart';

void main() async {
  runApp(const Drive_Counter());
}

class Drive_Counter extends StatefulWidget {
  const Drive_Counter({super.key});

  @override
  State<Drive_Counter> createState() => _Drive_CounterState();
}

const double narrowScreenWidthThreshold = 700;

const Color m3BaseColor = Color(0xff6750a4);
const Color blue = Color(0xff5E81AC);
const Color teal = Color(0xff88C0D0);
const Color green = Color(0xffA3BE8C);
const Color yellow = Color(0xffEBCB8B);
const Color orange = Color(0xffD08770);
const Color pink = Color(0xffB48EAD);
const List<Color> colorOptions = [
  m3BaseColor,
  blue,
  teal,
  green,
  yellow,
  orange,
  pink
];
const List<String> colorText = <String>[
  "Default",
  "Blue",
  "Teal",
  "Green",
  "Yellow",
  "Orange",
  "Pink",
];

class _Drive_CounterState extends State<Drive_Counter> {
  bool useMaterial3 = true;
  bool useLightMode = false;
  int colorSelected = 0;
  int screenIndex = 0;

  late ThemeData themeData;

  /////////SHARED PREFERENCES
  ///

  void setBrightnessSP(bool brightnessSP) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setBool('brightness', brightnessSP);
  }

  void getBrightnessSP() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    bool useLightModeSP = prefs.getBool('brightness') as bool;
    setState(() {
      useLightMode = useLightModeSP;
    });
  }

  void setSelectedColorSP(int colorSP) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setInt('color', colorSP);
  }

  void getSelectedColorSP() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    int colorSelectedSP = prefs.getInt('color') as int;
    setState(() {
      colorSelected = colorSelectedSP;
    });
  }

  ///
  /////////SHARED PREFERENCES

  @override
  initState() {
    super.initState();
    getBrightnessSP();
    getSelectedColorSP();
    themeData = updateThemes(colorSelected, useMaterial3, useLightMode);
  }

  ThemeData updateThemes(int colorIndex, bool useMaterial3, bool useLightMode) {
    return ThemeData(
        colorSchemeSeed: colorOptions[colorSelected],
        useMaterial3: useMaterial3,
        brightness: useLightMode ? Brightness.light : Brightness.dark);
  }

  void handleScreenChanged(int selectedScreen) {
    setState(() {
      screenIndex = selectedScreen;
    });
  }

  void handleBrightnessChange() {
    setState(() {
      useLightMode = !useLightMode;
      setBrightnessSP(useLightMode);
      themeData = updateThemes(colorSelected, useMaterial3, useLightMode);
    });
  }

  void handleColorSelect(int value) {
    setState(() {
      colorSelected = value;
      setSelectedColorSP(colorSelected);
      themeData = updateThemes(colorSelected, useMaterial3, useLightMode);
    });
  }

  Widget createScreenFor(int screenIndex, bool showNavBarExample) {
    switch (screenIndex) {
      case 0:
        return home();
      case 1:
        return const history();
      case 2:
        return const settings();
      default:
        return home();
    }
  }

  PreferredSizeWidget createAppBar() {
    return AppBar(
      title: TextStyleExample(
        name: "Drive_Counter",
        style: TextStyle(fontSize: 50),
      ),
      actions: [
        IconButton(
          icon: useLightMode
              ? const Icon(Icons.wb_sunny_outlined)
              : const Icon(Icons.wb_sunny),
          onPressed: handleBrightnessChange,
          tooltip: "Toggle darkmode",
        ),
        PopupMenuButton(
          icon: const Icon(Icons.color_lens),
          shape:
              RoundedRectangleBorder(borderRadius: BorderRadius.circular(10)),
          itemBuilder: (context) {
            return List.generate(colorOptions.length, (index) {
              return PopupMenuItem(
                  value: index,
                  child: Wrap(
                    children: [
                      Padding(
                        padding: const EdgeInsets.only(left: 10),
                        child: Icon(
                          index == colorSelected
                              ? Icons.color_lens
                              : Icons.color_lens_outlined,
                          color: colorOptions[index],
                        ),
                      ),
                      Padding(
                          padding: const EdgeInsets.only(left: 20),
                          child: Text(colorText[index]))
                    ],
                  ));
            });
          },
          onSelected: handleColorSelect,
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'Drive_Counter',
      themeMode: useLightMode ? ThemeMode.light : ThemeMode.dark,
      theme: themeData =
          updateThemes(colorSelected, useMaterial3, useLightMode),
      home: LayoutBuilder(builder: (context, constraints) {
        if (constraints.maxWidth < narrowScreenWidthThreshold) {
          return Scaffold(
            appBar: createAppBar(),
            body: Row(children: <Widget>[
              createScreenFor(screenIndex, false),
            ]),
            bottomNavigationBar: NavigationBars(
              onSelectItem: handleScreenChanged,
              selectedIndex: screenIndex,
              isExampleBar: false,
            ),
          );
        } else {
          return Scaffold(
            appBar: createAppBar(),
            body: SafeArea(
              bottom: false,
              top: false,
              child: Row(
                children: <Widget>[
                  Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 5),
                      child: NavigationRailSection(
                          onSelectItem: handleScreenChanged,
                          selectedIndex: screenIndex)),
                  const VerticalDivider(thickness: 1, width: 1),
                  createScreenFor(screenIndex, true),
                ],
              ),
            ),
          );
        }
      }),
    );
  }
}

class TextStyleExample extends StatelessWidget {
  const TextStyleExample({
    super.key,
    required this.name,
    required this.style,
  });

  final String name;
  final TextStyle style;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(8.0),
      child: Text(name, style: style),
    );
  }
}
