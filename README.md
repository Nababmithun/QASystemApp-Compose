# QASystemApp-Compose
# 📋 Q&A Survey Android App (XML Version)

An interactive survey/questionnaire Android app built with
**Kotlin**,
**Room**,
**Retrofit**,
**Compose**.
The app fetches dynamic questions from a remote JSON source and allows users to answer or skip. At the end, all responses are displayed.

---

## ✨ Features

- ✅ Dynamic questions loaded from **JSONBin API**
- ✅ Supports multiple question types (Text input, MCQ, Dropdown, etc.)
- ✅ **Room database** for storing answers locally
- ✅ Skip & navigate between questions
- ✅ Show all submitted answers in a scrollable **RecyclerView**
- ✅ Option to **restart** the survey

---

## 🛠️ Tech Stack

- **Kotlin**
- **Retrofit2** (API)
- **Gson** (JSON parsing)
- **Room** (Database)
- **Coroutines**
- **Compose**
- **MVVM Pattern**

---

## 📦 API Source

Questions are fetched from:

https://api.jsonbin.io/v3/b/687374506063391d31aca23a

The JSON contains a dynamic question flow including:
- Question ID
- Type (e.g., multipleChoice, numberInput)
- Options
- Validation rules
- Navigation (`referTo`, `skip`)

---
