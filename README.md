# Bhatia Interior — Showroom & Premium Home Interiors

A modern, highly polished Android application designed for **Bhatia Interior** showroom clients. This app delivers a premium showcase of custom-designed wooden furniture, raw material catalogs, customized design pricing estimators, real-time client project tracking, and showroom consultation booking.

Built entirely using **Kotlin**, **Jetpack Compose (Material 3)**, and modern Android architecture.

---

## 🌐 Live Web Demo

You can interactively run and preview the full Android app directly in your web browser via the high-performance streaming emulator—no setup or installation required!

*   👉 **[Launch Active Development Preview](https://ais-dev-c5h5oe24ld2an7gtpenjme-1059087218413.asia-southeast1.run.app)** (Active streaming preview during your workspace development)
*   👉 **[Launch Shared Web App Build](https://ais-pre-c5h5oe24ld2an7gtpenjme-1059087218413.asia-southeast1.run.app)** (Published pre-release build)

> 💡 **Tip:** You can also run the app instantly in the browser by opening the **Preview** or **Streaming Emulator** panel inside the **Google AI Studio** workspace. It provides live interactive feedback as you develop!

---

## 🎨 Design Philosophy & Visual Styling

The visual architecture is crafted to replicate the luxury and warmth of a high-end interior design studio:
*   **Warm Heritage Palette**: Accented with golden-bronze highlights (`#8B7355` / `#9E845A`) and cozy, luxurious background tints to convey premium wooden craftsmanship.
*   **Modern Rounded Aesthetics**: Leverages generous Material 3 spacing and elegant container rounding (`12.dp` and `16.dp` custom shapes) to replace rigid, dated borders.
*   **High-Fidelity Schematics**: Vector-based rendering of architectural plans and interactive showroom components that look razor-sharp on any screen density.
*   **Fluent Accessibility**: Ensures readable typography pairings, dark-theme responsive contrast, and a minimum of `48.dp` touch targets for every interactive button.

---

## 🚀 Key Features

### 1. **Premium Showcase Dashboard** (Home)
*   **Dynamic Banner Carousel**: An interactive card carousel representing premium home spaces (Living Rooms, Modular Kitchens, Study Dens) with rich architectural descriptions.
*   **Heritage Wood Selector**: Direct access to raw swatches (Teak, Oak, Walnut) paired with deep-cut wood detail sheets.
*   **Aesthetic Blog Feed**: Curated articles detailing turnkey interior tips, space optimizations, and color pairings.

### 2. **Interactive Architectural Catalog** (Browse)
*   **Filtered Search**: Smooth search integration filtering across showroom designs, modular layouts, and material catalogs.
*   **Interactive 360° Material Viewer**: Select different premium timber types to preview their grains, physical properties, and live structural rendering.

### 3. **Real-time Bespoke Customizer** (Customize)
*   **Wood, Finish & Hardware Selector**: Customize furniture elements on-the-fly. Choose between premium species like *Burmese Teak*, *American Oak*, or *Indian Walnut*.
*   **Dynamic Price Estimator**: Instantly calculates estimated custom fabrication and installation costs as options change.
*   **Local Moodboards**: Bookmark and save personalized configurations locally for instant retrieval during showroom visits.

### 4. **Unified Client Portal** (Tracker)
*   **Active Project Progress**: Monitor construction or fabrication milestones via graphical linear progress trackers.
*   **Showroom Chat Interface**: Direct message support simulator for coordinating with lead interior architects.
*   **Bespoke Quotation Request**: Upload site photos, detail structural guidelines, select budgets, and instantly submit RFQs.
*   **Client Feedback Node**: Rate the showroom visit and write custom comments using Outlined Material 3 controls.

### 5. **Consultation Scheduler** (Contact)
*   **Interactive Schematic Map**: A styled vector map highlighting showroom proximity (Bandra Linking Road, Mumbai).
*   **Booking Engine**: Instantly secure designer consultation slots with validation and input protection.

---

## 📸 Screen Previews

> [!TIP]
> **Capture Screenshots**: Run the application in the AI Studio streaming emulator, take screen grabs of each key screen (Home, Customize, Tracker, Portfolio), place them inside an `art/` or `screenshots/` directory, and link them below to showcase your gorgeous UI!

| Home Screen | Bespoke Customizer | Interactive Catalog | Client Tracker |
| :---: | :---: | :---: | :---: |
| _[Add Home Screenshot Here]_ | _[Add Customizer Screenshot Here]_ | _[Add Catalog Screenshot Here]_ | _[Add Tracker Screenshot Here]_ |

---

## 🛠️ Tech Stack & Architecture

*   **Language**: 100% Kotlin with modern Coroutines and StateFlow management.
*   **UI Framework**: Jetpack Compose (Material 3) with full Edge-to-Edge window inset compliance.
*   **Image Loading**: Coil (Coroutines Image Loader) for asynchronous graphics processing.
*   **State Management**: MVVM Architecture with state-safe unidirectional flow and ViewModel abstraction.
*   **Build Tooling**: Gradle (Kotlin DSL - `.gradle.kts`) with modern version catalog management (`libs.versions.toml`).

---

## 🏗️ Getting Started

### Prerequisites
*   Android Studio Ladybug (or newer)
*   JDK 17 or higher
*   Android SDK 34 (Android 14)

### Running the App locally
1. Clone your published repository:
   ```bash
   git clone https://github.com/YOUR_USERNAME/bhatia-interior.git
   ```
2. Open the project directory inside **Android Studio**.
3. Allow Gradle synchronization to complete successfully.
4. Select a connected device or Virtual Device (AVD API 33+ recommended).
5. Click **Run** (`Shift + F10`) or execute:
   ```bash
   ./gradlew assembleDebug
   ```

---

## 📄 License
This project is licensed under the Apache License 2.0. See the LICENSE details.
