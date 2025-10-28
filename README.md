# 📱 AnunciosLoc

**AnunciosLoc** is an Android mobile application that allows users to post and receive **location-based announcements**.  
It supports both **centralized communication (via a server)** and **decentralized communication (via WiFi Direct)** — enabling message delivery even when users are offline.

---

## 🎯 Project Goal

To develop a distributed mobile application that enhances community interaction through **location-aware messaging**, allowing users to share and receive announcements tied to specific geographic locations.

---

## 🧩 Core Features

- 🧍‍♂️ **F1:** User registration  
- 🔑 **F2:** Login / Logout  
- 📍 **F3:** List, create, and remove locations  
- 📰 **F4:** Register and delete announcements  
- 👀 **F5:** View announcements  
- 🔔 **F6:** Location-based notifications  
- ☁️ **Centralized mode:** Communication via the main server  
- 📶 **Decentralized mode:** Peer-to-peer communication using WiFi Direct  

---

## 🔐 Advanced Features

- 🚚 **Relay Routing (Mule Nodes):**  
  Speeds up message delivery by allowing selected intermediary devices (mules) to carry messages to their target destinations.

- 🛡️ **Security Layer:**  
  Ensures encrypted communication and message authentication to prevent tampering, interception, or injection attacks.

---

## 🧱 System Architecture

- 🖥️ **Central Server:** Java standalone app managing users, messages, and delivery policies.  
- 📲 **Android Client:** Java-based app that allows users to interact and exchange messages.  
- 🧪 **Testing Environment:** Android Emulator + Termite WiFi Direct (for GPS, mobility, and peer-to-peer simulation).

---

## ⚙️ Technologies Used

- 💻 **Language:** Java  
- 📱 **Framework:** Android SDK  
- 🌐 **Server:** Java standalone app  
- 🧩 **Simulators:** Android Emulator & Termite WiFi Direct  
- 🗺️ **APIs:** GPS & WiFi for location and proximity detection  

---

## 🧠 Usability Rules

- ✅ Intuitive and simple user interface  
- 🎨 Consistent color palette and spacing  
- 💬 Clear user feedback for every interaction  
- 🔄 Easy navigation between screens  
- ♿ Accessibility and responsiveness  
- 🧩 Minimalist and functional design  

---

## 📁 Project Structure

```
AnunciosLoc/
├── app
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src
│       ├── androidTest/
│       ├── main/
│       │   ├── java/                   # Source code (activities, models, utils)
│       │   ├── res/                    # Layouts, drawables, strings, colors
│       │   └── AndroidManifest.xml
│       └── test/                       # Unit tests
├── build.gradle.kts
├── gradle
│   ├── libs.versions.toml
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
└── settings.gradle.kts
```

## 👩‍💻 Authors

- **Ângela Amaro**  
- **Adriana Mazanga**  
- **Raquel Da Gama**

---

## 🧾 License

This project was developed for academic purposes under the **Mobile Applications** course at **ISPTEC (Department of Engineering and Technologies)**.  
All rights reserved © 2025 — *Not intended for commercial use.*

---
