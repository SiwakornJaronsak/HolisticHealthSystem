# 🌿 Holistic Health System

A holistic health assessment system (mind + body) that creates personalized exercise plans and advice.
Written in **Java** as a single application file, using full object-oriented programming (OOP) principles

---

## ✨ Features

| Features | details |
|---------|-----------|
| 🧠 Mental Health Assessment | 5-question quiz or choose your own status. (Depressed / Stressed / Normal) |
| ⚖️ BMI Calculator | Calculated from weight (kg) + height (cm), and categorized according to WHO criteria |
| 🏋️ Personalized Workout Plan | 7-day schedule adjusted according to mental status + BMI + goal |
| 💡 Health Advice | Personalized recommendations in 3 dimensions: Mindset, BMI, Goals |
| 💾 File Save/Load | Records are saved to `health_records.txt` and can be read back |
| 🎨 ANSI Color UI | Decorate the terminal with beautiful colors and style |

---

## 🏗️ OOP Concepts

```
HolisticHealthSystem.java
│
├── InvalidBMIInputException      ← Custom Exception
├── InvalidMenuChoiceException    ← Custom Exception
├── Ansi                          ← ANSI Color Constants
├── MentalStatus (enum)           ← DEPRESSED / STRESSED / NORMAL
├── Goal (enum)                   ← LOSE_WEIGHT / BUILD_MUSCLE / MAINTAIN_WEIGHT
├── HealthPlan (interface)        ← generateWorkoutPlan() / getAdvice()
├── HealthProfile (abstract)      ← Base Class
│   └── UserProfile               ← Inheritance + implements HealthPlan
├── HealthRecordStore<T>          ← Generic Collection Class
├── FileUtil                      ← File I/O Utility
└── HolisticHealthSystem (main)   ← Entry Point
```

## 🚀 How to run the program

### Prerequisites
- Java JDK 8 ขึ้นไป

### Compile & Run

```bash
# Compile
javac src/HolisticHealthSystem.java -d out

# Run
java -cp out HolisticHealthSystem
```

Or if it's in the same directory as the file:

```bash
javac HolisticHealthSystem.java
java HolisticHealthSystem
```

---

## 📋 Usage

```
  ============================================
         HOLISTIC HEALTH SYSTEM
     Your Mind + Body Wellness Guide
  ============================================

  MAIN MENU:
  1. Start Health Assessment
  2. View Saved Records (from file)
  3. Exit
```

### Assessment steps
1. **Enter name**
2. **Step 1 – Mental Health**: Choose to answer a 5-question quiz or state your status yourself
3. **Step 2 – BMI**: Enter your weight (kg) and height (cm)
4. **Step 3 – Goal**: Choose a goal (weight loss / muscle gain / weight maintenance)
5. The system automatically generates a workout plan and instructions and saves them to `health_records.txt`

---

## 📁 Project Structure

```
HolisticHealthSystem/
├── src/
│   └── HolisticHealthSystem.java   ← Main source code
├── health_records.txt              ← Log file (automatically created during runtime)
├── .gitignore
└── README.md
```

---

## 🧪 BMI Categories (WHO Standard)

| BMI | Category |
|-----|---------|
| < 18.5 | Underweight |
| 18.5 – 24.9 | Normal |
| 25.0 – 29.9 | Overweight |
| ≥ 30.0 | Obese |

---

## 📊 Mental Health Quiz Scoring

| score | result |
|-------|--------|
| 7 – 10 | DEPRESSED |
| 3 – 6 | STRESSED |
| 0 – 2 | NORMAL |

แต่ละข้อ: A = 2 คะแนน | B = 1 คะแนน | C = 0 คะแนน

---

## 📝 License

MIT License — Free to use and modify.
