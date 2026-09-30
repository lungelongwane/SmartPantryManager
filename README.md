# Smart Pantry Manager

Java Android application developed for the Mobile App Development 700
assignment.

## Purpose

Smart Pantry Manager allows users to manage ingredients stored in their
pantry and receive recipe suggestions based on the ingredients and quantities
they currently have available.

A recipe is suggested only when all of its required ingredients are available
in the pantry in sufficient quantities.

## Technology

- Java
- Android Studio
- Android Activities and Intents
- SQLite using SQLiteOpenHelper
- RecyclerView
- Custom RecyclerView adapters
- Material Design components
- XML layouts

## Main Screens

The application contains the following screens:

1. Home
2. My Pantry
3. Add/Edit Ingredient
4. Suggested Recipes
5. Recipe Details
6. Settings

## Pantry Management

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

Pantry data is stored locally using SQLite and remains available after the
application is closed and reopened.

## Recipe Suggestions

The application contains 18 seeded recipes.

Recipe suggestions use strict ingredient and quantity matching.

A recipe is displayed only when:

- Every required ingredient is available.
- The available quantity is equal to or greater than the required quantity.
- Compatible units can be converted where supported.

The matching system also normalizes common singular and plural ingredient
names.

For example, a recipe requiring:

    chicken: 250 g

will only be suggested when at least 250 g of chicken is available.

If a required ingredient is missing or there is not enough of it, the recipe
is not displayed.

## Recipe Database

The local SQLite database contains three main tables:

- `ingredients`
- `recipes`
- `recipe_ingredients`

The `recipes` and `recipe_ingredients` tables are populated with the seeded
recipe data when the database is created.

## Database Persistence

Pantry ingredients are stored in the local SQLite database.

This means that ingredients remain stored when the user:

- Leaves the pantry screen
- Navigates between screens
- Closes the application
- Reopens the application

## Settings

The Settings screen provides an expiry-alert preference.

The preference is stored using Android SharedPreferences so that the selected
setting can be retained.

## Navigation

The application uses Android Activities and Intents for navigation between
the main application screens.

Toolbar navigation and the application's menu are used to move between
screens.

## Project Structure

Important application components include:

- `MainActivity` - application home screen
- `PantryActivity` - pantry ingredient list
- `AddEditIngredientActivity` - add and edit ingredients
- `SuggestedRecipesActivity` - displays matching recipes
- `RecipeDetailActivity` - displays recipe information
- `SettingsActivity` - application settings
- `DatabaseHelper` - SQLite database management
- `RecipeMatcher` - strict recipe matching logic
- `PantryAdapter` - RecyclerView adapter for pantry ingredients
- `RecipeAdapter` - RecyclerView adapter for recipes

## Running the Application

1. Open the project in Android Studio.
2. Allow Gradle synchronization to complete.
3. Make sure an Android emulator or physical Android device is available.
4. Select the device from Android Studio.
5. Press **Run**.
6. The application will build and launch on the selected device.

## GitHub Development

The project is maintained in a Git repository and developed through
incremental commits.

Each commit represents a genuine development change to the application,
such as functionality, validation, UI improvements, database improvements,
or bug fixes.

## Academic Note

This project was developed for the Mobile App Development 700 assignment.
The implementation, Git history, demonstration, and written documentation
should reflect the actual functionality and development process of the
application.

