# Bhatia Interior — Showroom & Premium Home Interiors

A modern, highly polished Android application designed for **Bhatia Interior** showroom clients. This app delivers a premium showcase of custom-designed wooden furniture, raw material catalogs, customized design pricing estimators, real-time client project tracking, and showroom consultation booking.

Built entirely using **Kotlin**, **Jetpack Compose (Material 3)**, and modern Android architecture.

---

## 🌐 Live Web Demo & Preview Options

To preview and interact with this application directly in your web browser—without installing any local tools or encountering billing paywalls—we have provided several high-fidelity options:

### 🏆 Option 1: Live Interactive Web Emulator (Highly Recommended for Portfolios!)
We have built a beautiful, custom **HTML5 & Tailwind CSS Mobile Emulator** directly inside the repository (`index.html` + `vercel.json`). When you host this repository on Vercel, it serves an interactive virtual smartphone showing a mock running instance of the Bhatia Interior Android App:
*   **Fully Functional Screens**: Tap through all navigation views: **Home**, **Browse**, **Customize**, **Tracker**, and **Contact**.
*   **Live Customizer & Calculator**: Select different timber species (Teak, Walnut, Oak) or hardware options to see live grain updates and real-time cost estimations calculated in-browser!
*   **Simulated Real-time Architect Chat**: Type in messages and chat interactively with Lead Architect Sameer Bhatia.
*   **Showroom Scheduler**: Book live appointments and trigger immediate scheduling confirmation alerts.

#### 🚀 How to deploy to Vercel in 30 Seconds (100% Free):
If you see a `404: Page Not Found` or want to launch this live on your own custom Vercel subdomain:
1.  **Direct Github Import**:
    *   Go to [vercel.com](https://vercel.com) and click **Add New** ➔ **Project**.
    *   Import your **Bhatia Interior** GitHub repository.
    *   Vercel will automatically detect the static project root, read the custom `/vercel.json` routing configuration, and deploy the mock emulator within seconds!
2.  **Using Vercel CLI**:
    ```bash
    # Install Vercel CLI globally
    npm install -g vercel

    # Run deployment from the project root directory
    vercel

    # Optional: Link to a permanent alias
    vercel --prod
    ```

### 📱 Option 2: Live Interactive Emulator (No-Install Web Preview)
*   👉 **[Launch Active Development Preview](https://ais-dev-c5h5oe24ld2an7gtpenjme-1059087218413.asia-southeast1.run.app)** 
    *(Accesses the **live streaming emulator** directly connected to your active developer container).*
*   ⚠️ **If the screen is stuck on "Starting Server":** 
    1. Simply **refresh your browser page** to wake up the server container.
    2. Alternatively, open the **Preview** or **Streaming Emulator** panel on the right side of your Google AI Studio workspace and press **Run** or click the refresh button inside the panel. This triggers an incremental build and forces the streaming service to start immediately.

### 📦 Option 3: Download & Share the Native APK (Free Alternative)
You can share a fully functional native version of this app for free with any reviewer or employer:
1.  **Generate a Debug APK**: Run `./gradlew assembleDebug` in Android Studio or compile it in this workspace.
2.  **Download & Share**:
    *   Navigate to the `.build-outputs/` folder inside this workspace or your local project directory.
    *   Download the `.apk` file.
    *   Upload it to a free file-sharing service (e.g., **Google Drive**, **OneDrive**, or directly as a **GitHub Release asset** on your repository).
    *   Your audience can instantly install and test the actual high-performance app on any physical Android device!

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
