🌿 Holistic Health System
A holistic health assessment system (mind + body) that creates personalized exercise plans and advice
Java OOP MIT License
📋 Overview
Holistic Health System is a Java-based terminal application that integrates physical and mental health assessment into a single platform. It evaluates the user's mental status through a 9-question quiz, calculates BMI, and generates a fully personalized 7-day workout plan along with tailored health advice — all saved to a local file for future reference.

✨ Features
Feature	Details
🧠 Mental Health Assessment	9-question quiz or choose your own status (Depressed / Stressed / Normal)
⚖️ BMI Calculator	Calculated from weight (kg) + height (cm), categorized per WHO criteria
🏋️ Personalized Workout Plan	7-day schedule adjusted by mental status + BMI + goal
💡 Health Advice	Personalized recommendations across 3 dimensions: Mindset, BMI, Goals
💾 File Save / Load	Records saved to health_records.txt, viewable by name or all
🎨 ANSI Color UI	Terminal decorated with colors, box borders, and styled output
🏗️ OOP Concepts
HolisticHealthSystem.java
│
├── InvalidBMIInputException      ← Custom Exception
├── InvalidMenuChoiceException   ← Custom Exception
├── Ansi                           ← ANSI Color Constants
├── MentalStatus (enum)           ← DEPRESSED / STRESSED / NORMAL
├── Goal (enum)                   ← LOSE_WEIGHT / BUILD_MUSCLE / MAINTAIN_WEIGHT
├── HealthPlan (interface)         ← generateWorkoutPlan() / getAdvice()
├── HealthProfile (abstract)       ← Base Class
│   └── UserProfile               ← Inheritance + implements HealthPlan
├── HealthRecordStore<T>          ← Generic Collection Class
├── FileUtil                       ← File I/O Utility
└── HolisticHealthSystem (main)  ← Entry Point
📁 Project Structure
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

health_records.txt    ← auto-created at runtime
🚀 How to Run
Prerequisites
Java JDK 8 or higher
Compile & Run
If files are organized in packages:

javac src/core/HolisticHealthSystem.java -d out
java -cp out HolisticHealthSystem
If all files are in the same directory:

javac HolisticHealthSystem.java
java HolisticHealthSystem
📋 Usage
  ╔══════════════════════════════════════════════╗
  ║        🌿  HOLISTIC HEALTH SYSTEM  🌿        ║
  ╠══════════════════════════════════════════════╣
  ║  1.  Start Health Assessment                 ║
  ║  2.  View Saved Records (from file)          ║
  ║  3.  Exit                                    ║
  ╚══════════════════════════════════════════════╝
Assessment Steps
Enter your name
Step 1 – Mental Health: Take the 9-question quiz or manually select your status
Step 2 – BMI: Enter your weight (kg) and height (cm)
Step 3 – Goal: Choose Lose Weight / Build Muscle / Maintain Weight
The system generates a personalized workout plan and health advice, then saves to health_records.txt
🧪 BMI Categories (WHO Standard)
BMI	Category
< 18.5	Underweight
18.5 – 24.9	Normal
25.0 – 29.9	Overweight
≥ 30.0	Obese
📊 Mental Health Quiz Scoring
The quiz consists of 9 questions. Each answer is scored as:

A = 2 points  (most severe)
B = 1 point
C = 0 points  (fine)
Total Score	Result
0 – 4	NORMAL 😊
5 – 10	STRESSED 😰
11 – 18	DEPRESSED 😔
Maximum possible score: 18 (9 questions × 2 points each)

📝 License
MIT License — Free to use and modify.
