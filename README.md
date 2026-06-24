# Bhatia Interior — Showroom & Premium Home Interiors

A modern, highly polished Android application designed for **Bhatia Interior** showroom clients. This app delivers a premium showcase of custom-designed wooden furniture, raw material catalogs, customized design pricing estimators, real-time client project tracking, and showroom consultation booking.

Built entirely using **Kotlin**, **Jetpack Compose (Material 3)**, and modern Android architecture.

---

## 🌐 Live Web Demo

To preview and interact with the application directly in your web browser, please use the appropriate link below based on your current workspace status:

1.  👉 **[Launch Active Development Preview](https://ais-dev-c5h5oe24ld2an7gtpenjme-1059087218413.asia-southeast1.run.app)** 
    *(Use this link to access the **live streaming emulator** directly connected to your active developer workspace container. This is active during your coding session).*
2.  👉 **[Launch Shared Web App Build](https://ais-pre-c5h5oe24ld2an7gtpenjme-1059087218413.asia-southeast1.run.app)**
    *(This is the production CDN link. Note: If this link returns `Error: Page not found`, it is because you have not yet completed a **Publish / Share** action. To activate it, click the **Share** button in the top-right corner of the Google AI Studio interface to compile your production release!)*

> 💡 **Tip:** The easiest way to run and test the app instantly is directly inside the **Streaming Emulator / Preview** panel on the right side of your Google AI Studio workspace!

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

## 📸 Screen Previews & App Layouts

Due to the image generation system's daily API quota limit during automated workspace builds, pre-generated mockup graphics could not be written directly into the project repository. However, you can instantly add your own screenshots by following this quick 3-step guide!

### 🛠️ How to Add Your Screenshots
1.  **Launch the App**: Open the **Active Development Preview** link or the **Streaming Emulator** panel inside the Google AI Studio workspace.
2.  **Take Screen Grabs**: Capture a screenshot of the main screens (**Home, Browse, Customize, Tracker, Contact**).
3.  **Place in Repository**: Create a folder named `art/` in the root of your repository, place the images inside (e.g., `home.png`, `customize.png`), and reference them in this markdown file.

---

### 📱 Visual Architecture & Wireframe Layouts

To help you visualize the premium Material 3 UI design constructed for **Bhatia Interior**, here is the structural layout of the major screens:

```
┌──────────────────────────────────────┐  ┌──────────────────────────────────────┐
│  BHATIA INTERIOR - PREMIUM HOME      │  │  BESPOKE CUSTOMIZER (MATERIAL 3)     │
├──────────────────────────────────────┤  ├──────────────────────────────────────┤
│  [Hero Banner Carousel]             │  │  [3D-Style Living Room Scene]        │
│  "Discover Curated Spaces"           │  │  "Turnkey Wooden Fabrication"        │
│                                      │  │                                      │
│  [Timber Selector Swatches]          │  │  [ Timber Species Selector ]         │
│  Teak   •   Walnut   •   Oak         │  │  (•) Burmese Teak   ( ) Walnut       │
│                                      │  │                                      │
│  [Architectural Design Articles]     │  │  [ Hardware / Handles Selection ]    │
│  - Space Optimization in Mumbai      │  │  - Brass Knobs    - Matte Black Rail │
│  - Contrast Styling & Highlights     │  │                                      │
│                                      │  │  [Estimated Price: ₹ 1,45,000/-]     │
│  [Quick Booking Floating Button]     │  │  [Add to Moodboard]   [Book Design]  │
└──────────────────────────────────────┘  └──────────────────────────────────────┘
             (Home Screen)                           (Customize Screen)

┌──────────────────────────────────────┐  ┌──────────────────────────────────────┐
│  CLIENT PORTAL & PROGRESS TRACKER    │  │  SHOWROOM LOCATOR & BOOKING          │
├──────────────────────────────────────┤  ├──────────────────────────────────────┤
│  [Milestone Progress Card]           │  │  [Interactive Vector Map]            │
│  Active Project: Modular Wardrobe    │  │  "Linking Road, Bandra West, Mumbai" │
│  Progress: 75%                       │  │                                      │
│  ■■■■■■■■■■■■■■■■■■■■■■■■■■■□□□□□    │  │  [Designer Consultation Scheduler]   │
│                                      │  │  - Select Preferred Service          │
│  [Architect Chat Support Zone]       │  │  - Enter Target Booking Date         │
│  "Send site photos / coordinate"     │  │  - Full Name / Contact Number        │
│  [Type message...]            [Send] │  │                                      │
│                                      │  │  [ Submit Booking Reservation ]      │
│  [Submit Showroom Visit Review]      │  │                                      │
│  ★ ★ ★ ★ ☆  "Write comments here..."  │  │  [Showroom Working Hours Info Card]  │
└──────────────────────────────────────┘  └──────────────────────────────────────┘
            (Tracker Screen)                         (Contact Screen)
```

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
