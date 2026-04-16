# 🌿 Holistic Health System

A holistic health assessment system (mind + body) that creates personalized exercise plans and advice
**Java OOP | MIT License**

---

## 📋 Overview

Holistic Health System is a Java-based terminal application that integrates physical and mental health assessment into a single platform.
It evaluates the user's mental status through a 9-question quiz, calculates BMI, and generates a fully personalized 7-day workout plan along with tailored health advice — all saved to a local file.

---

## ✨ Features

| Feature                     | Details                                                                   |
| --------------------------- | ------------------------------------------------------------------------- |
| 🧠 Mental Health Assessment | 9-question quiz or choose your own status (Depressed / Stressed / Normal) |
| ⚖️ BMI Calculator           | Calculated from weight (kg) + height (cm), categorized per WHO criteria   |
| 🏋️ Workout Plan            | 7-day schedule adjusted by mental status + BMI + goal                     |
| 💡 Health Advice            | Recommendations across Mindset, BMI, Goals                                |
| 💾 File Save / Load         | Records saved to `health_records.txt`                                     |
| 🎨 ANSI UI                  | Colored terminal with styled output                                       |

---

## 🏗️ OOP Concepts

```
HolisticHealthSystem.java
│
├── InvalidBMIInputException
├── InvalidMenuChoiceException
├── Ansi
├── MentalStatus (enum)
├── Goal (enum)
├── HealthPlan (interface)
├── HealthProfile (abstract)
│   └── UserProfile
├── HealthRecordStore<T>
├── FileUtil
└── HolisticHealthSystem (main)
```

---

## 📁 Project Structure

```
src/
├── core/
│   ├── HealthRecordStore.java
│   └── HolisticHealthSystem.java
├── exceptions/
│   ├── InvalidBMIInputException.java
│   └── InvalidMenuChoiceException.java
├── models/
│   ├── FileUtil.java
│   ├── Goal.java
│   ├── HealthPlan.java
│   ├── HealthProfile.java
│   ├── MentalStatus.java
│   └── UserProfile.java
├── utils/
│   ├── Ansi.java
│   └── FileUtil.java
└── README.md
```

---

## 🚀 How to Run

### Prerequisites

* Java JDK 8+

### Compile & Run

```bash
javac src/core/HolisticHealthSystem.java -d out
java -cp out HolisticHealthSystem
```

---

## 📋 Usage

```
╔══════════════════════════════════════════════╗
║        🌿  HOLISTIC HEALTH SYSTEM  🌿       ║
╠══════════════════════════════════════════════╣
║  1. Start Health Assessment                  ║
║  2. View Saved Records                       ║
║  3. Exit                                     ║
╚══════════════════════════════════════════════╝
```

### Assessment Steps

* Enter your name
* Step 1 – Mental Health: Take the 9-question quiz or select your status
* Step 2 – BMI: Enter weight (kg) and height (cm)
* Step 3 – Goal: Choose Lose Weight / Build Muscle / Maintain Weight
* System generates a personalized plan and saves to `health_records.txt`

---

## 🧪 BMI Categories (WHO)

| BMI         | Category    |
| ----------- | ----------- |
| < 18.5      | Underweight |
| 18.5 – 24.9 | Normal      |
| 25.0 – 29.9 | Overweight  |
| ≥ 30.0      | Obese       |

---

## 📊 Mental Health Quiz

| Score   | Result       |
| ------- | ------------ |
| 0 – 4   | NORMAL 😊    |
| 5 – 10  | STRESSED 😰  |
| 11 – 18 | DEPRESSED 😔 |

---

## 📝 License

MIT License — Free to use and modify.

