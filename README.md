# 📊 GraphLite Library

## 🚀 Overview
**GraphLite** is an Android library that enables developers to create dynamic, visually appealing line and bar charts — with a focus on clean design, smooth animations, and optimal performance.

<p align="center">
  <img src="https://github.com/your-username/your-repo/assets/your-image-1" width="250">
  <img src="https://github.com/your-username/your-repo/assets/your-image-2" width="250">
</p>
Link to video: 
https://drive.google.com/file/d/17hQTX8J3S-9bavs_ISSo7h9WcxEOWmYU/view?usp=drive_link

---

## 🧩 Main Features

- 📈 **Line Graphs**: Plot multiple series.
- 📊 **Bar Charts**: Vertical bar graphs.
- 🌀 **Graph Animations**: Enable entrance animations for transitions.
- 💡 **Dynamic Labeling**: show labels on X and Y axes.
- 🖼️ **Fully Customizable**: Modify colors, sizes, radii, and much more with easy-to-use setters.
- 👆 **Touch Interaction**: Optional support for responding to point click events.

---

## 📦 Library Modules

### `graphlitelib`
- Contains reusable classes like:
  - `GraphLine`: Defines a line dataset with attributes like color, width, and point visibility.
  - `LineChartView`: Custom `View` that draws animated line graphs.
  - `BarChartView`: Custom `View` for drawing colorful bar charts.

### `app`
- Sample demo app demonstrating how to use the library:
  - Shows a **multi-line chart** with average and max values.
  - Displays a **bar chart** with device usage categories.

---

## 🏗️ Sample Usage (MainActivity.java)

```java
List<Float> values = Arrays.asList(8500f, 6200f, 3100f, 1200f, 600f);
List<String> labels = Arrays.asList("Mobile", "Desktop", "Tablet", "Smart TV", "Wearable");

barChartView.setValues(values);
barChartView.setLabels(labels);
barChartView.setBarColors(Arrays.asList(
    Color.parseColor("#3B82F6"),
    Color.parseColor("#60A5FA"),
    Color.parseColor("#A78BFA"),
    Color.parseColor("#F472B6"),
    Color.parseColor("#FBBF24")
));
barChartView.setBarSpacing(dpToPx(24));
barChartView.setBarWidth(dpToPx(60));
```

---

## 📦 Installation
To use this SDK, add the dependency to your *Android project* using JitPack:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.NoaSharabi8:GraphLite:1.0.1'
}
```

---

## 🤝 Contributing

Feel free to open issues or pull requests if you find bugs or want to suggest improvements.

---


📌 _Maintained by:_ **Noa Sharabi**
