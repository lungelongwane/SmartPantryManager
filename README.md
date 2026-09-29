# Smart Pantry Manager

Java Android application for Mobile App Development 700.

## Purpose
The app helps users manage ingredients they already have and suggests recipes only when every required ingredient is available in the pantry in a sufficient quantity.

## Technology
- Java
- Android Studio
- SQLite using SQLiteOpenHelper
- RecyclerView with custom adapters
- Android Activities and Intents

## Main screens
1. Home
2. My Pantry
3. Add/Edit Ingredient
4. Suggested Recipes
5. Recipe Details
6. Settings

## Database
The local SQLite database contains:
- `ingredients`
- `recipes`
- `recipe_ingredients`

Recipes are seeded automatically on the first database creation.

## Strict matching
A recipe is displayed only when every required ingredient is found in the pantry and the available quantity is sufficient. Ingredient names are normalized for common singular/plural differences and common compatible units are converted.

## Running
Open the project in Android Studio, allow Gradle synchronization to complete, select an emulator or physical Android device, and press Run.

## Academic note
The project should be developed and committed incrementally in the student's own Git repository, with meaningful commit messages matching the actual development process.
