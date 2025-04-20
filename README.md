# ♻️ WasteExpress

**WasteExpress** is a mobile prototype app built to simplify the process of waste resale and recycling connections. It allows individuals and small companies with various types of waste to publish offers for potential buyers who are interested in repurposing or recycling those materials.

---

## 🎓 About the Project

This ProtoType was created at the request of students **Akram Bouras**, **Amine Chouah**, and **Louai Bz** as part of their **final graduation project** at the **University of Constantine 3 Salah Boubnider**.

The goal was to provide a practical and eco-friendly mobile solution that contributes to environmental sustainability by connecting people who want to dispose of waste with those who can make use of it through recycling or other projects.

---

## 🚀 Overview

WasteExpress bridges the gap between **waste sellers** and **waste buyers**. Whether you're a household, a small factory, or a recycling startup — this app lets you create offers, discover available waste materials, and manage purchase requests all in one place.

The app is especially designed to support the ecosystem of **recyclers (إعادة التدوير)** who are always on the lookout for recyclable materials such as:

- ♻️ Plastique (Plastic)  
- 🔩 Métaux (Metals)  
- 🌱 Organique (Organic)  
- 📄 Papier (Paper)  
- 💉 Bios (Biomedical Waste)

---

## 📱 Features

### 🧾 For Waste Sellers:
- Easy account creation using Firebase Authentication.
- Add a new offer with the following details:
  - **Type** of waste (plastique, métaux, etc.)
  - **Sous-type** (subcategory based on type)
  - **Title & Description**
  - **Quantity** (in kg or tonnes)
  - **Région** (location)
  - **Image** of the waste item
- Submit your offer with a single tap.

### 🛒 For Buyers:
- Browse through all available offers.
- Filter by type or region.
- Request to purchase listed waste.
- Updated **achat (purchase) prices** in real-time from Firebase.

### 🔐 Admin Panel:
- Admin access only with `admin@gmail.com`.
- Admin can:
  - Approve or reject offers
  - Update purchase prices for each type
  - Monitor buyer requests

---

## 🛠️ Tech Stack

- 🧠 **Language**: Kotlin
- 🔥 **Database**: Firebase Realtime Database
- ☁️ **Storage**: Firebase Storage (for offer images)
- 🔐 **Authentication**: Firebase Auth
- 📲 **Admin Panel**: Built-in (visible only for admin login)

---

## 📦 Installation & Run

1. Clone this repository
2. Open it in **Android Studio**
3. Connect to Firebase
4. Sync Gradle and Run the app

---

## 📸 Screenshots

<p float="left">
  <img src="https://i.imgur.com/cr8DEGX.jpeg" width="45%" />
  <img src="https://i.imgur.com/nksihTJ.jpeg" width="45%" />
  <img src="https://i.imgur.com/tvCLsr8.jpeg" width="45%" />
  <img src="https://i.imgur.com/0gqQWM8.jpeg" width="45%" />
</p>

---

## ⚠️ Prototype Note

> This app is currently a **prototype**. It demonstrates core functionality and is not intended for production deployment yet. Enhancements in security, UI/UX, and backend validation are planned for future versions.

---

## 💡 Future Improvements

- Push notifications for offer updates
- Chat system between buyers and sellers
- Advanced filtering and region-based search
- Dashboard analytics for the admin
- Web dashboard for wider accessibility

---

## 📬 Contact

**Charaf Boulmerka**  
Android & Laravel Developer | IoT & CRM Solutions.
📧 charaf.boulmerka25@gmail.com  

---

## 📝 License

This project is open-source and available under the [MIT License](LICENSE).
