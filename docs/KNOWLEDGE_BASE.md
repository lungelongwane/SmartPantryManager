# Smart Pantry Manager — Project Knowledge Base

> **Purpose:** This document is the working knowledge base for the SmartPantryManager Android assignment. It records the actual project structure, requirements, business rules, database design, troubleshooting information, testing checklist, and development history so the project can be understood without re-reading the whole codebase.

**Repository:** https://github.com/lungelongwane/SmartPantryManager  
**Package:** `com.lungelo.smartpantrymanager`  
**Application:** Smart Pantry Manager  
**Module:** Mobile App Development 700  
**Language:** Java  
**Current branch:** `main`

---

## 1. Project Purpose

Smart Pantry Manager is a Java Android application for the Mobile App Development 700 practical assignment.

The application lets a user record ingredients they already have in their pantry and receive recipe suggestions based on those ingredients.

The central business rule is **strict recipe matching**:

> A recipe may be suggested only when every required ingredient is present in the pantry in at least the required quantity.

Partial matches must not appear in the main Suggested Recipes list.

The assignment describes the purpose as reducing food waste by helping users cook with ingredients they already have rather than requiring a shopping trip.

---

## 2. Assignment Requirements

The supplied Mobile App Development 700 assignment specifies the following core functionality:

### Pantry management
The user must be able to:
- Add pantry ingredients.
- View pantry ingredients.
- Edit pantry ingredients.
- Delete pantry ingredients.
- Record ingredient name, quantity, unit and optionally expiry date.

### Pantry list
The pantry must be displayed using a RecyclerView/ListView connected to persistent data storage.

### Recipe collection
The application must contain at least 15–20 pre-loaded/seeded recipes. Each recipe needs:
- A recipe name.
- Required ingredients.
- Preparation instructions.

### Suggested Recipes
The app must evaluate the user's current pantry using the strict-matching rule and display only recipes the user can make immediately.

### Recipe details
Selecting a suggested recipe must open a detail screen showing:
- Full ingredient requirements.
- Preparation method/instructions.

### Settings/Profile
A settings or profile screen is required. The assignment gives examples such as:
- Expiring-soon alerts.
- Units preference.

The current implementation uses an expiry-alert setting.

### Zero-match feedback
When no recipes qualify, the app must show clear feedback rather than a blank or broken screen.

### Technical restrictions
The assignment requires the application to be built entirely in Java using Android Studio. It also explicitly prohibits Google Maps, mapping SDKs and device location/GPS features because the scope is the user's own pantry and recipe matching.

---

## 3. Current Application Screens

The repository currently contains these main screens:

| Screen | Class | Purpose |
|---|---|---|
| Home | `MainActivity` | Application landing/home screen |
| My Pantry | `PantryActivity` | Displays and manages pantry ingredients |
| Add/Edit Ingredient | `AddEditIngredientActivity` | Creates or edits pantry records |
| Suggested Recipes | `SuggestedRecipesActivity` | Displays recipes that strictly match the pantry |
| Recipe Details | `RecipeDetailActivity` | Displays recipe ingredients and instructions |
| Settings | `SettingsActivity` | Stores the expiry-alert preference |

Navigation is handled through Android Activities, Intents and the shared `Navigation` helper.

---

## 4. Project Architecture

The project follows a straightforward Android structure:

```
com.lungelo.smartpantrymanager
├── MainActivity.java
├── Navigation.java
├── PantryActivity.java
├── AddEditIngredientActivity.java
├── SuggestedRecipesActivity.java
├── RecipeDetailActivity.java
├── SettingsActivity.java
│
├── adapters
│   ├── PantryAdapter.java
│   └── RecipeAdapter.java
│
├── database
│   └── DatabaseHelper.java
│
├── models
│   ├── Ingredient.java
│   ├── Recipe.java
│   └── RecipeIngredient.java
│
└── utils
    └── RecipeMatcher.java
```

### Responsibilities

**Activities**
- Control screens and user interaction.
- Start other Activities with Intents.
- Reload data when returning to a screen.

**Models**
- Represent pantry ingredients, recipes and recipe ingredient requirements.

**Adapters**
- Bind pantry and recipe objects to RecyclerView rows.

**DatabaseHelper**
- Creates and manages the SQLite database.
- Performs pantry CRUD operations.
- Reads recipes and recipe ingredients.
- Seeds the recipe collection.

**RecipeMatcher**
- Implements the strict recipe-matching business logic.
- Normalizes ingredient names.
- Converts compatible units before quantity comparison.

---

## 5. Database Design

The application uses SQLite through Android's `SQLiteOpenHelper`.

Database:
```
smart_pantry.db
```

Current database version:
```
1
```

### Table: ingredients

Stores the user's pantry records.

Fields:
- `id` — INTEGER PRIMARY KEY AUTOINCREMENT
- `name` — TEXT NOT NULL
- `quantity` — REAL NOT NULL
- `unit` — TEXT NOT NULL
- `expiry_date` — TEXT, optional

### Table: recipes

Stores recipe definitions.

Fields:
- `id` — INTEGER PRIMARY KEY AUTOINCREMENT
- `name` — TEXT NOT NULL
- `instructions` — TEXT NOT NULL

### Table: recipe_ingredients

Stores the ingredients required by each recipe.

Fields:
- `id` — INTEGER PRIMARY KEY AUTOINCREMENT
- `recipe_id` — INTEGER NOT NULL
- `ingredient_name` — TEXT NOT NULL
- `required_quantity` — REAL NOT NULL
- `unit` — TEXT NOT NULL

Relationship:
```
recipes 1 ──────── many recipe_ingredients
```

The foreign key uses `ON DELETE CASCADE`.

Foreign-key enforcement is enabled in `DatabaseHelper.onConfigure()`.

---

## 6. Recipe Seed Data

The current source contains **18 seeded recipes**, satisfying the assignment's 15–20 recipe requirement.

Recipes currently defined include:

1. Chicken Fried Rice
2. Tomato Pasta
3. Vegetable Omelette
4. Chicken Sandwich
5. Tuna Pasta
6. Potato Egg Hash
7. Chicken Curry
8. Bean Wrap
9. Cheese Toastie
10. Vegetable Rice
11. Greek Salad
12. Garlic Butter Pasta
13. Banana Pancakes
14. Chicken Salad
15. Lentil Soup
16. Egg Fried Rice
17. Tomato Egg Scramble
18. Cheesy Potato Bake

Recipes are seeded in `DatabaseHelper.seedRecipes()` when the database is first created.

---

## 7. Strict Recipe Matching Logic

The strict matcher is implemented in:

```
app/src/main/java/com/lungelo/smartpantrymanager/utils/RecipeMatcher.java
```

### Rule

For every recipe:

1. Read all required ingredients.
2. Find pantry items with the same normalized ingredient name.
3. Convert compatible quantities to a common base unit.
4. Add compatible quantities where multiple pantry records have the same ingredient.
5. Compare available quantity with required quantity.
6. If any required ingredient is missing or insufficient, reject the entire recipe.
7. Only recipes that pass every requirement are returned.

### Example

If Tomato Pasta requires:

```
pasta: 200 g
tomato: 3 pieces
onion: 1 piece
garlic: 2 cloves
olive oil: 15 ml
```

then all five requirements must be satisfied.

Having:
- pasta
- tomato
- onion
- garlic

but no olive oil means **Tomato Pasta must not be suggested**.

### Quantity rule

```
available quantity >= required quantity
```

### Ingredient-name normalization

The matcher handles common singular/plural differences, for example:
- tomato → tomato
- tomatoes → tomato
- egg → egg
- eggs → egg
- clove → clove
- cloves → clove

It also handles common `ies` and `oes` plural forms.

This is specifically important because the assignment says a naive exact-string comparison such as “tomato” vs “tomatoes” should not be used.

---

## 8. Unit Conversion

`RecipeMatcher` supports compatible unit conversion.

### Weight
Base unit: grams.

Supported:
- g / gram / grams
- kg / kilogram / kilograms

Conversion:
```
1 kg = 1000 g
```

### Volume
Base unit: millilitres.

Supported:
- ml
- l
- cup

Conversion:
```
1 l = 1000 ml
1 cup = 240 ml
```

### Count units

Supported count types are kept distinct:
- piece
- slice
- clove
- can

A clove is not treated as a generic piece, and a slice is not treated as a can.

This avoids unsafe assumptions when comparing quantities.

---

## 9. Pantry CRUD

`PantryActivity` uses `DatabaseHelper` and `PantryAdapter`.

### Add
The user opens `AddEditIngredientActivity`, enters the ingredient details and saves the record.

### View
Pantry records are loaded from SQLite and displayed in a RecyclerView.

### Edit
Selecting edit passes the ingredient ID to `AddEditIngredientActivity`, which loads and updates the existing record.

### Delete
Delete displays a confirmation dialog before removing the database record.

### Persistence
Pantry data is stored in SQLite and therefore remains available when navigating away from the pantry and reopening the application.

---

## 10. Input Validation

The Add/Edit screen validates pantry data.

The current project is intended to enforce:
- Ingredient name is not empty.
- Quantity is greater than zero.
- Expiry date is valid where supplied.

Validation should be demonstrated in the assignment video/report with actual screenshots of validation feedback.

---

## 11. Suggested Recipes Flow

The flow is:

```
User opens Suggested Recipes
        ↓
DatabaseHelper.getAllRecipes()
        ↓
DatabaseHelper.getAllIngredients()
        ↓
RecipeMatcher.getStrictMatches()
        ↓
Qualified recipes returned
        ↓
RecipeAdapter displays them
        ↓
User selects a recipe
        ↓
RecipeDetailActivity opens with recipeId
```

When no recipe qualifies:
- The RecyclerView is hidden.
- The empty recipe state is displayed.
- The screen provides an action to add ingredients.

This satisfies the assignment's requirement for clear zero-match feedback.

---

## 12. Recipe Details

`RecipeDetailActivity` receives a recipe ID through the Intent:

```
recipeId
```

It then obtains:
- Recipe name.
- Required ingredients.
- Instructions.

The detail screen is intended to show the complete recipe information rather than only the recipe title.

---

## 13. Settings

`SettingsActivity` contains an expiry-alert switch.

The setting is stored using:

```
SharedPreferences
```

Preference key:

```
expiry_alerts
```

Default:
```
true
```

This means the user's selected preference can survive navigation and application restarts.

---

## 14. Database Upgrade Safety

The current `DatabaseHelper.onUpgrade()` does **not** drop and recreate the database.

This is intentional.

Older implementations that use:

```
DROP TABLE
```

during every upgrade can destroy user pantry data.

The current implementation preserves existing records and leaves a clear location for explicit migrations if the schema is changed in a future version.

### Important

The current database version is still 1 because no schema change requiring a version bump has been introduced.

---

## 15. Important Database/Recipe Troubleshooting

Recipe seed data is inserted in `onCreate()`.

That means the 18 recipes are seeded when the SQLite database is initially created.

If a device/emulator already has an older `smart_pantry.db` created before the recipe seed logic was added, the database may not automatically receive the seed records because `onCreate()` is not called again for an existing database.

### Diagnostic sequence if Suggested Recipes shows no recipes unexpectedly

1. Confirm pantry ingredients satisfy a known recipe exactly.
2. Confirm the database contains recipes.
3. Check Logcat using the tag:
   ```
   RECIPE_MATCHER
   ```
4. Check whether `getAllRecipes()` returns records.
5. If the local database was created before recipe seeding existed, recreate the app's local database/device data as a development diagnostic.
6. Do not add a destructive production upgrade unless it is intentionally implemented as a migration.

---

## 16. Known Test Case: Tomato Pasta

A useful end-to-end test is:

Add these pantry records:

| Ingredient | Quantity | Unit |
|---|---:|---|
| tomato | 3 | piece |
| onion | 1 | piece |
| pasta | 200 | g |
| garlic | 2 | clove |
| olive oil | 15 | ml |

Expected result:

**Tomato Pasta appears in Suggested Recipes.**

Then open it and verify:
- Recipe name is displayed.
- All five ingredients are displayed.
- Instructions are displayed.

### Negative test

Remove or reduce any one required ingredient.

Expected result:

**Tomato Pasta must not appear.**

This is especially important because strict matching is the core business rule of the assignment.

---

## 17. Full Functional Test Checklist

### Home/navigation
- [ ] App launches without crashing.
- [ ] Home screen loads.
- [ ] My Pantry navigation works.
- [ ] Suggested Recipes navigation works.
- [ ] Settings navigation works.
- [ ] Toolbar/back navigation works.

### Pantry
- [ ] Add ingredient.
- [ ] Ingredient appears in list.
- [ ] Edit ingredient.
- [ ] Edited values persist.
- [ ] Delete ingredient.
- [ ] Delete confirmation appears.
- [ ] Empty pantry state appears when no ingredients exist.

### Validation
- [ ] Empty ingredient name rejected.
- [ ] Zero quantity rejected.
- [ ] Negative quantity rejected.
- [ ] Invalid expiry date rejected where applicable.

### Recipes
- [ ] Recipe data exists.
- [ ] Suggested Recipes screen loads.
- [ ] Complete-match recipe appears.
- [ ] Missing ingredient excludes recipe.
- [ ] Insufficient quantity excludes recipe.
- [ ] Singular/plural matching works.
- [ ] Compatible unit conversion works.
- [ ] No-match feedback appears.
- [ ] Recipe selection opens Recipe Details.
- [ ] Recipe ingredients and instructions display correctly.

### Settings
- [ ] Expiry-alert switch loads.
- [ ] Switch can be changed.
- [ ] Preference persists after leaving/reopening Settings.
- [ ] Preference persists after application restart.

### Persistence
- [ ] Pantry data survives screen navigation.
- [ ] Pantry data survives closing/reopening the app.
- [ ] Recipe data remains available.

---

## 18. Assignment Evidence Checklist

The supplied assignment requires a portfolio of evidence, not just source code.

The written report should include evidence such as:

### System design
- Screen-flow/wireframe diagram.
- Database/data-model or ER diagram.

### Screenshots
Actual screenshots from the running application showing:
- Every screen.
- Add function.
- Edit function.
- Delete function.
- List view.
- Validation error.
- Suggested recipes.
- Recipe details.
- Settings.
- Empty/no-match state.

Do not use placeholders or stock images instead of screenshots of the actual application.

### Key code snippets
The assignment asks for 3–5 short, well-selected code snippets with explanations.

Useful snippets for this project:
1. Pantry CRUD/database insertion.
2. Strict recipe matching.
3. Unit conversion/normalization.
4. Recipe navigation using Intent.
5. SharedPreferences settings persistence.

### Challenges and solutions
Document 2–3 real problems encountered during development.

Actual project examples include:
- Suggested recipe matching/debugging.
- Gradle/Android Gradle Plugin compatibility.
- Protecting pantry data from destructive database upgrades.

### Conclusion/reflection
Explain:
- What was learned.
- What could be improved with more time.

---

## 19. Marking Rubric Reference

The supplied assignment uses a 100-mark rubric:

| Criterion | Marks |
|---|---:|
| App Functionality & Requirements | 20 |
| Database Implementation | 15 |
| Code Quality & Java Fundamentals | 25 |
| UI/UX Design | 10 |
| GitHub Version Control | 10 |
| Video Demonstration | 10 |
| Written Report | 10 |
| **Total** | **100** |

### GitHub requirement

The rubric specifically expects:
- Regular meaningful commits.
- Incremental progress.
- Complete push history.
- README present.

The current repository has a README and a history of incremental commits.

---

## 20. Video Demonstration Requirements

The assignment specifies a **5–7 minute** demonstration.

The video should show:
- GitHub history.
- The working application.
- Clear verbal explanation of the concepts used.

A sensible demonstration sequence is:

1. Briefly introduce Smart Pantry Manager.
2. Show GitHub repository and commit history.
3. Launch the application.
4. Demonstrate adding an ingredient.
5. Demonstrate editing it.
6. Demonstrate deleting it.
7. Add ingredients that satisfy a complete recipe.
8. Open Suggested Recipes.
9. Open Recipe Details.
10. Remove/reduce one required ingredient and show the strict recipe disappears.
11. Show Settings and expiry-alert preference.
12. Briefly explain SQLite, Activities, Intents, RecyclerView and RecipeMatcher.

---

## 21. Submission Requirements

The supplied assignment states that the final submission is a **single ZIP file** containing:
- Complete Android Studio source project.
- Compressed 5–7 minute demonstration video.
- Written report as a PDF or Word document.

The assignment states the ZIP must not exceed **50 MB**.

The report must include a working GitHub repository link.

The assignment also states that build/.gradle folders may be excluded from the source archive to reduce size.

---

## 22. Git Development History

Known meaningful commits:

| Commit | Message | Purpose |
|---|---|---|
| `f28e365` | Initial project setup | Initial repository setup |
| `add623d` | Implement Smart Pantry Manager core application | Core application functionality |
| `a0c7bc8` | Fix compatible recipe quantity matching | Improved strict recipe quantity/unit matching |
| `5c61345` | Align Gradle wrapper with Android Gradle plugin | Changed wrapper to Gradle 8.7 for project compatibility |
| `51f4e78` | Preserve pantry data during database upgrades | Removed destructive upgrade behaviour |

Current repository HEAD is `51f4e78`.

---

## 23. Build Configuration

Current application configuration includes:

- Android Gradle Plugin: 8.6.1
- Gradle wrapper: 8.7
- compileSdk: 35
- targetSdk: 35
- minSdk: 24
- Java/Android implementation language: Java
- Application ID: `com.lungelo.smartpantrymanager`

Dependencies currently include:
- AndroidX AppCompat 1.7.0
- AndroidX RecyclerView 1.3.2
- Material Components 1.12.0

The development environment previously used JDK 17 (Temurin 17.0.20).

---

## 24. Current Project Status

### Implemented in the repository
- Java Android application.
- Home screen.
- Pantry screen.
- Pantry Add/Edit/Delete.
- RecyclerView pantry list.
- SQLite database.
- 18 seeded recipes in source.
- Recipe/recipe-ingredient database model.
- Strict recipe matching.
- Quantity checking.
- Weight and volume conversion.
- Singular/plural normalization.
- Suggested Recipes screen.
- Zero-match state.
- Recipe Details screen.
- Settings screen.
- SharedPreferences setting.
- Activity/Intent navigation.
- README.
- Incremental Git commits.
- Safer database upgrade handling.
- Gradle wrapper alignment.

### Still requires runtime verification
The repository has been inspected and updated, but the application has **not been runtime-verified in an Android emulator in this environment**.

The following should therefore be tested in Android Studio before treating them as confirmed:
- Clean build.
- Application launch.
- Recipe seed data on the actual device/database.
- Tomato Pasta positive match.
- Strict negative match.
- Recipe Details.
- Settings persistence.
- Full navigation.
- Persistence after restart.
- Absence of crashes.

---

## 25. Developer Quick Reference

### Main files to inspect

**Database**
```
app/src/main/java/com/lungelo/smartpantrymanager/database/DatabaseHelper.java
```

**Recipe matching**
```
app/src/main/java/com/lungelo/smartpantrymanager/utils/RecipeMatcher.java
```

**Pantry**
```
app/src/main/java/com/lungelo/smartpantrymanager/PantryActivity.java
app/src/main/java/com/lungelo/smartpantrymanager/AddEditIngredientActivity.java
```

**Recipe suggestions**
```
app/src/main/java/com/lungelo/smartpantrymanager/SuggestedRecipesActivity.java
```

**Recipe details**
```
app/src/main/java/com/lungelo/smartpantrymanager/RecipeDetailActivity.java
```

**Settings**
```
app/src/main/java/com/lungelo/smartpantrymanager/SettingsActivity.java
```

**Build**
```
app/build.gradle
build.gradle
gradle/wrapper/gradle-wrapper.properties
```

---

## 26. Troubleshooting Decision Tree

### Problem: "No recipes found"

Check in this order:

1. Is the pantry populated?
2. Does the pantry contain every ingredient for a known recipe?
3. Are quantities sufficient?
4. Are units compatible?
5. Are ingredient names normalized/matching?
6. Does `DatabaseHelper.getAllRecipes()` return the seeded recipes?
7. Was the app's local database created before the seed data was added?
8. What does Logcat show under `RECIPE_MATCHER`?

### Problem: Build/Gradle error

Check:
1. JDK 17 selected.
2. Gradle wrapper is 8.7.
3. Android Studio has completed Gradle sync.
4. SDK 35 is installed.
5. Run a clean/rebuild after sync.

### Problem: Pantry data disappears

Check:
1. Database version.
2. `DatabaseHelper.onUpgrade()`.
3. Whether the app data/database was manually cleared.
4. Whether a schema migration was introduced without preserving existing records.

---

## 27. Knowledge Base Maintenance Rules

When the project changes, update this document whenever a change affects:
- Assignment requirements.
- Screen/functionality.
- Database schema.
- Recipe matching behaviour.
- Build configuration.
- Troubleshooting.
- Testing.
- Git history.
- Submission requirements.

Do not document functionality as working until it has been tested.

For future changes, record:
1. What changed.
2. Why it changed.
3. Which files were affected.
4. How it was tested.
5. The commit SHA.

This keeps the knowledge base aligned with the actual repository rather than becoming outdated project notes.
