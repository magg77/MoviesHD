# 📱 Proyecto de Aplicación Android

# Screenshots
![Test Image 2](https://github.com/magg77/MoviesHD/blob/develop/screens/App-MovieHD.jpg)

# 🛠 Requisitos
Movie app developed in Kotlin to be run on Android mobile devices. Before running the project, make sure you have the following installed:

- Android Studio Ladybug Feature Drop | 2024.2.2
- Minimum version 23
- Maximum version 35
- JDK 11 or higher = JvmTarget = "11"
- Kotlin compiler version = "2.0.21"
- Uses AndroidX libraries and Jetpack components
- Internet connection (to download dependencies and consume external APIs, if applicable)

# 🚀 Instrucciones para ejecutar el proyecto
- git clone https://github.com/magg77/MoviesHD.git
- cd tu-repositorio
- Abrir el proyecto con el ide android studio
- sync dependencies with gradle
- ./gradlew build

# 📦 Technologies and libraries used in the MovieHD App
This is a clean architecture app  built with

- Kotlin: Main development language.
- MVVM
- Coroutines
- Extension Functions
- Dagger Hilt
- Retrofit
- Room
- Navigation Components
- Cache Strategy (Repository Pattern)
- Coroutines Flow
- ViewBinding
- Livedata
- Material design theming
- Support for dark and light mode
- AndroidX and Jetpack Components: Modern architecture and backward compatibility.
- tom.libs: dependency management

# 🏗️ Technical Decisions
1️⃣ Project Architecture
- MVVM (Model-View-ViewModel) has been implemented to ensure separation of responsibilities and better maintainability.

2️⃣ Dependency Management
- tom.libs makes dependency management easier by centralizing versions and references into a single file, reducing redundancy and avoiding conflicts between libraries. This improves project maintainability and consistency.

3️⃣ Dependency Injection
- Hilt is used for dependency injection, which makes the code easier to scale.

4️⃣ Api
-For the development of this test, the TMDb movie API was used. It is ideal for technical testing on Android due to its free API, real data and clear documentation, allowing the evaluation of REST consumption, UI management and architecture.

![Test Image 1](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)


# Support 😄

Follow me on Github [User :>](https://github.com/magg77/MoviesHD)  ⭐



