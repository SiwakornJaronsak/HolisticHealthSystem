# 🌿 Holistic Health System

ระบบประเมินสุขภาพแบบองค์รวม (Mind + Body) ที่สร้างแผนออกกำลังกายและคำแนะนำเฉพาะบุคคล  
เขียนด้วยภาษา **Java** แบบ Single-file โดยใช้หลักการ OOP ครบถ้วน

---

## ✨ Features

| ฟีเจอร์ | รายละเอียด |
|---------|-----------|
| 🧠 Mental Health Assessment | Quiz 5 ข้อ หรือเลือกสถานะเองได้ (Depressed / Stressed / Normal) |
| ⚖️ BMI Calculator | คำนวณจากน้ำหนัก (kg) + ส่วนสูง (cm) พร้อมจัดหมวดหมู่ตามเกณฑ์ WHO |
| 🏋️ Personalized Workout Plan | ตาราง 7 วัน ปรับตาม mental status + BMI + goal |
| 💡 Health Advice | คำแนะนำเฉพาะบุคคลใน 3 มิติ: จิตใจ, BMI, เป้าหมาย |
| 💾 File Save/Load | บันทึก records ลง `health_records.txt` และอ่านกลับมาได้ |
| 🎨 ANSI Color UI | ตกแต่ง terminal ด้วยสีและ style สวยงาม |

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

---

## 🚀 วิธีรันโปรแกรม

### Prerequisites
- Java JDK 8 ขึ้นไป

### Compile & Run

```bash
# Compile
javac src/HolisticHealthSystem.java -d out

# Run
java -cp out HolisticHealthSystem
```

หรือถ้าอยู่ใน directory เดียวกับไฟล์:

```bash
javac HolisticHealthSystem.java
java HolisticHealthSystem
```

---

## 📋 การใช้งาน

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

### ขั้นตอน Assessment
1. **กรอกชื่อ**
2. **Step 1 – Mental Health**: เลือกทำ quiz 5 ข้อ หรือบอกสถานะเอง
3. **Step 2 – BMI**: กรอกน้ำหนัก (kg) และส่วนสูง (cm)
4. **Step 3 – Goal**: เลือกเป้าหมาย (ลดน้ำหนัก / เพิ่มกล้ามเนื้อ / รักษาน้ำหนัก)
5. ระบบสร้าง Workout Plan + คำแนะนำ และบันทึกลง `health_records.txt` อัตโนมัติ

---

## 📁 Project Structure

```
HolisticHealthSystem/
├── src/
│   └── HolisticHealthSystem.java   ← Source code หลัก
├── health_records.txt              ← ไฟล์บันทึก (สร้างอัตโนมัติตอนรัน)
├── .gitignore
└── README.md
```

---

## 🧪 BMI Categories (WHO Standard)

| BMI | หมวดหมู่ |
|-----|---------|
| < 18.5 | Underweight |
| 18.5 – 24.9 | Normal |
| 25.0 – 29.9 | Overweight |
| ≥ 30.0 | Obese |

---

## 📊 Mental Health Quiz Scoring

| คะแนน | ผลลัพธ์ |
|-------|--------|
| 7 – 10 | DEPRESSED |
| 3 – 6 | STRESSED |
| 0 – 2 | NORMAL |

แต่ละข้อ: A = 2 คะแนน | B = 1 คะแนน | C = 0 คะแนน

---

## 📝 License

MIT License — Free to use and modify.
