# 🖨️ PrintXpress

### 🚀 Digital Printing, Delivered Fast

A Native Android Printing Management App built with Java & SQLite

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Android-Native-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white" />
  <img src="https://img.shields.io/badge/Material%20Design-UI-6200EE?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Min%20SDK-24-success?style=for-the-badge" />
</p>

---

# 📌 Overview

**PrintXpress** is a full-featured native Android application designed for a modern digital printing business.

The app enables customers to browse printing services, customize print orders, upload design files, and track their order progress — while administrators manage products, promotions, customers, and order workflows through a dedicated admin interface.

Built entirely using **Java**, **Android SDK**, and **SQLite** without any backend server.

---

# ✨ Core Features

## 👤 Customer Features

* 🔐 User registration & login
* 👤 Profile management
* 🔑 Password change functionality
* 🛍 Browse products by category
* 📄 Product detail view
* 🖨 Custom print order placement
* 📎 Upload custom design files
* 🚚 Delivery or pickup selection
* 📦 Track order status
* 🎉 View active promotions
* 📘 Printing guidelines & instructions

---

## 👨‍💼 Admin Features

* 📊 Admin dashboard
* 🛒 Manage customer orders
* 📦 Product management
* 🎯 Promotion management
* 👥 Customer management
* 🔄 Update order statuses
* 📈 Monitor platform activity

---

# 🛠 Tech Stack

| Layer         | Technology          |
| ------------- | ------------------- |
| Language      | Java                |
| Platform      | Native Android      |
| Database      | SQLite              |
| UI Components | AndroidX            |
| Design System | Material Components |
| Layouts       | ConstraintLayout    |
| Lists         | RecyclerView        |
| Cards         | CardView            |
| Build System  | Gradle              |
| Min SDK       | 24                  |
| Target SDK    | 34                  |

---

# 🧱 Architecture

The application follows a clean modular structure for maintainability and scalability.

```bash id="b0lgzt"
app/src/main/java/com/printxpress/app/
│
├── activities/           # App screens
├── adapter/              # RecyclerView adapters
├── model/                # Data models
├── db/
│   └── DatabaseHelper.java
├── util/
│   ├── SessionManager
│   ├── Validator
│   └── PasswordUtil
└── resources/
```

---

# 📱 App Modules

| Module              | Description                   |
| ------------------- | ----------------------------- |
| Authentication      | Login, registration, sessions |
| Product Catalog     | Browse and filter products    |
| Order System        | Create & manage print orders  |
| Promotions          | Promotional offers system     |
| Customer Management | Admin customer control        |
| File Upload         | Upload design assets          |
| Order Tracking      | Status monitoring             |
| SQLite Layer        | Offline local data storage    |

---

# 🎨 UI & UX

The app is designed using **Material Design principles** with:

* ✨ Clean modern layouts
* 📱 Mobile-first responsive screens
* 🎯 User-friendly navigation
* 🧩 Reusable UI components
* 🖼 Card-based product presentation

---

# ⚙️ Getting Started

## 1️⃣ Clone Repository

```bash id="f8m5t7"
git clone https://github.com/yourusername/printxpress.git
```

---

## 2️⃣ Open in Android Studio

Recommended:

* Android Studio Giraffe or newer
* Android SDK 34

---

## 3️⃣ Sync Gradle

Allow Gradle dependencies to download automatically.

---

## 4️⃣ Run Application

Launch the app on:

* Android Emulator
* Physical Android Device (Android 7.0+)

---

# 🗄 Database

The application uses:

* 📦 Local SQLite database
* 🧠 Custom `DatabaseHelper`
* 🌱 Automatic seed data generation
* ⚡ Offline-first architecture

The database initializes automatically on first launch.

---

# 🧪 Demo Accounts

| Role        | Email                     | Password      |
| ----------- | ------------------------- | ------------- |
| 👑 Admin    | `admin@printxpress.lk`    | `admin123`    |
| 👤 Customer | `customer@printxpress.lk` | `customer123` |

> ⚠️ Demo credentials are for development/testing purposes only.

---

# 🔥 Highlights

* ✅ Native Android development
* ✅ Offline-first architecture
* ✅ SQLite CRUD operations
* ✅ Material Design UI
* ✅ File upload support
* ✅ Order tracking workflow
* ✅ Role-based system
* ✅ Custom session handling
* ✅ Local persistence without backend

---

# 🚀 Future Improvements

* ☁️ Firebase backend integration
* 💳 Online payment gateway
* 📩 Push notifications
* 📍 Real-time order tracking
* 🌐 Cloud file storage
* 📊 Analytics dashboard
* 🧾 PDF invoice generation
* 🤖 AI-based print recommendations

---

# 🧠 What I Learned

This project helped strengthen my knowledge in:

* Native Android development
* SQLite database design
* RecyclerView architecture
* Material UI implementation
* Local session management
* CRUD operations
* Mobile UX design
* File handling in Android

---

# 👨‍💻 Developer

## Jenushan

Full Stack & Mobile Application Developer 🇱🇰

### Skills

* Android (Java)
* Django
* PostgreSQL
* SaaS Systems
* ERP / HRMS Development
* UI/UX Focused Applications

---

# ⭐ Support

If you found this project interesting:

* ⭐ Star the repository
* 🍴 Fork the project
* 🚀 Share feedback
* 💡 Suggest improvements

---

<p align="center">
  Built with ❤️ using Java & Android SDK
</p>
