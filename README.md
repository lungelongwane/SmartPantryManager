# Smart Pantry Manager

> A Java-based Android application for managing pantry ingredients, storing data locally with SQLite, and generating recipe suggestions from available ingredients.

## 📱 Project Overview

Smart Pantry Manager is an Android application developed for the **Mobile App Development 700** assignment.

The application was built to provide a practical pantry-management workflow while demonstrating Android application development, local database persistence, user input validation, navigation between activities, and recipe-matching logic.

## Background & Motivation

As part of my mobile application development studies, I built this project to strengthen my practical understanding of Android application development and database-driven mobile applications.

The application focuses on practical pantry management functionality, including adding, editing, and deleting ingredients, maintaining pantry data, and using stored ingredients to support recipe suggestions. The project also provided hands-on experience with Android UI design, application navigation, local data persistence, and implementing application logic in Java.

## ✨ Key Features

- **Pantry management** — add, view, edit, and delete ingredients.
- **Quantity and unit tracking** — store the amount and measurement unit for each ingredient.
- **Expiry-date handling** — support optional expiry dates and validation.
- **Local persistence** — store pantry information in a local SQLite database.
- **Recipe database** — maintain seeded recipes and their required ingredients.
- **Recipe matching** — compare pantry ingredients and quantities against recipe requirements.
- **Suggested recipes** — display recipes that satisfy the application's matching rules.
- **Recipe details** — view information for a selected recipe.
- **Settings** — store the application's expiry-alert preference using SharedPreferences.
- **Material UI** — Android layouts and Material Design components.
- **Activity-based navigation** — navigate between the application's main screens using Android Activities and Intents.

## 🛠️ Technology Stack

| Technology | Use |
|---|---|
| **Java** | Application logic |
| **Android Studio** | Android development environment |
| **SQLite / SQLiteOpenHelper** | Local data persistence |
| **RecyclerView** | Pantry and recipe lists |
| **Material Design** | User interface components |
| **XML** | Android layouts |
| **SharedPreferences** | Settings persistence |
| **Gradle** | Project build system |

## 🧩 Application Structure

The project is organised into separate areas for activities, adapters, database access, models and utilities.

```text
SmartPantryManager/
├── app/
│   └── src/main/
│       ├── java/com/lungelo/smartpantrymanager/
│       │   ├── MainActivity.java
│       │   ├── PantryActivity.java
│       │   ├── AddEditIngredientActivity.java
│       │   ├── SuggestedRecipesActivity.java
│       │   ├── RecipeDetailActivity.java
│       │   ├── SettingsActivity.java
│       │   ├── Navigation.java
│       │   ├── adapters/
│       │   ├── database/
│       │   ├── models/
│       │   └── utils/
│       └── res/
├── docs/
│   └── KNOWLEDGE_BASE.md
├── build.gradle
├── settings.gradle
└── README.md
```

## 🏠 Main Application Screens

The application includes the following main areas:

1. **Home**
2. **My Pantry**
3. **Add / Edit Ingredient**
4. **Suggested Recipes**
5. **Recipe Details**
6. **Settings**

## 🥫 Pantry Management

Users can:

- Add ingredients
- View pantry ingredients
- Edit existing ingredients
- Delete ingredients
- Store ingredient quantity and unit
- Store an optional expiry date
- Validate ingredient names
- Validate that quantities are greater than zero
- Validate expiry dates

Pantry data is stored locally using SQLite and remains available after the application is closed and reopened.

## 🍳 Recipe Suggestions

The application contains **18 seeded recipes**.

Recipe suggestions use ingredient and quantity matching. A recipe is displayed only when:

- Every required ingredient is available.
- The available quantity is equal to or greater than the required quantity.
- Compatible units can be converted where supported.

The matching system also normalizes common singular and plural ingredient names.

For example, a recipe requiring:

```text
chicken: 250 g
```

requires at least **250 g of chicken** to be available in the pantry.

## 🗄️ Database

The local SQLite database contains three main tables:

- `ingredients`
- `recipes`
- `recipe_ingredients`

The recipe tables are populated with seeded recipe data when the database is created.

## 📚 Project Knowledge Base

A detailed project knowledge base is included in:

**[`docs/KNOWLEDGE_BASE.md`](docs/KNOWLEDGE_BASE.md)**

It documents the application's architecture, database design, major components, navigation, recipe-matching flow, validation, and development considerations.

## ▶️ Running the Application

1. Open the project in **Android Studio**.
2. Allow Gradle synchronization to complete.
3. Start an Android emulator or connect a physical Android device.
4. Select the device in Android Studio.
5. Press **Run**.
6. Build and launch the application.

## 🧪 Development & Validation

The project has been developed through incremental Git commits so that changes to functionality, validation, UI, database behaviour and bug fixes can be tracked separately.

The repository documentation is intended to reflect the actual implementation rather than an idealised or hypothetical application.

## 📸 Screenshots

Screenshots of the running application can be added here as project evidence, particularly for:

- Home screen
- My Pantry
- Add Ingredient
- Edit Ingredient
- Suggested Recipes
- Recipe Details
- Settings

Only screenshots from the actual application should be included.

## 🎓 Academic Context

This project was developed for the **Mobile App Development 700** assignment.

The repository contains the Android source code, project configuration, documentation and knowledge base used to explain the implementation.

---

**Project:** Smart Pantry Manager  
**Platform:** Android  
**Language:** Java  
**Database:** SQLite  
**Development Environment:** Android Studio
