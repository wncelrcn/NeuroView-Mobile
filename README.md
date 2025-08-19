# NeuroView-Mobile 🧠

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)

**Smart MRI analysis. Fast results. Anywhere you need it.**

NeuroView is an AI-powered Android application designed for brain tumor classification using MRI images. The app provides healthcare professionals and researchers with a convenient tool to analyze brain scans and get detailed information about different types of brain tumors.

## 📱 Features

### 🎯 Core Functionality

- **📤 MRI Image Upload**: Upload brain MRI images for analysis
- **🤖 AI-Powered Classification**: Get accurate tumor type predictions using machine learning
- **📊 Detailed Results**: View confidence scores and probability distributions
- **📋 Past Records**: Access history of previous analyses
- **📚 Tumor Information**: Comprehensive details about different tumor types

### 🧬 Supported Tumor Types

- **Glioma**: Most common primary brain tumor
- **Meningioma**: Tumor arising from brain's protective layers
- **Pituitary**: Tumor in the pituitary gland

### 🎨 User Experience

- **Modern UI**: Built with Jetpack Compose for smooth, native Android experience
- **Intuitive Navigation**: Easy-to-use interface with bottom navigation
- **Responsive Design**: Optimized for various screen sizes
- **Real-time Results**: Fast processing and immediate feedback

## 🛠️ Technology Stack

### Frontend (Android)

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 36
- **Architecture**: Modern Android development practices

### Key Dependencies

- **Jetpack Compose BOM**: Latest stable Compose libraries
- **Navigation Compose**: For seamless screen transitions
- **Material 3**: Modern Material Design components
- **Ktor Client**: HTTP client for API communication
- **Kotlinx Serialization**: JSON serialization/deserialization
- **Coil**: Efficient image loading library
- **Accompanist Pager**: Enhanced pager components

### Backend Integration

- **API**: REST API hosted on Render
- **Base URL**: `https://neuroview-backend.onrender.com/api/auto`
- **Format**: JSON communication
- **Features**: Image upload, classification, history retrieval

## 📱 Screenshots & App Flow

### Main Screens

1. **Home Screen**: Welcome screen with app branding and call-to-action
2. **Dashboard**: Main navigation hub with quick access to features
3. **Upload Screen**: Image selection and upload interface
4. **Results Screen**: AI analysis results with confidence scores
5. **Tumor Details**: Comprehensive information about detected tumor types
6. **Past Records**: History of previous analyses

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Android SDK with API level 24+
- Kotlin 2.0.21+
- Gradle 8.11.1+

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/yourusername/NeuroView-Exam.git
   cd NeuroView-Exam
   ```

2. **Open in Android Studio**

   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory
   - Wait for Gradle sync to complete

3. **Build and Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or use the Run button in Android Studio

### Configuration

The app is pre-configured to work with the hosted backend. No additional setup is required for basic functionality.

#### Network Configuration

- Internet permission is required for API communication
- Network security configuration allows HTTP traffic for development
- HTTPS is used for production API endpoints

## 📁 Project Structure

```
app/src/main/java/com/example/neuroviewexam/
├── activities/              # Activity classes
│   ├── DashboardActivity.kt
│   ├── PastRecordsActivity.kt
│   ├── ResultActivity.kt
│   ├── TumorDetailActivity.kt
│   └── UploadActivity.kt
├── components/              # Reusable UI components
│   ├── BottomNavigationBar.kt
│   └── TopAppBar.kt
├── data/                    # Data models and tumor information
│   └── TumorData.kt
├── network/                 # API service and networking
│   └── ApiService.kt
├── ui/theme/               # App theming and styling
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
└── MainActivity.kt         # Entry point activity
```

## 🔧 API Integration

### Endpoints Used

- **POST** `/api/auto` - Upload image and get classification
- **GET** `/api/auto` - Retrieve past analysis records

### Response Format

```json
{
  "success": true,
  "prediction": {
    "tumor_type": "Glioma",
    "confidence": 0.95,
    "probabilities": [0.95, 0.03, 0.02],
    "class_probabilities": {
      "Glioma": 0.95,
      "Meningioma": 0.03,
      "Pituitary": 0.02
    }
  }
}
```

## 🧬 Tumor Information Database

The app includes comprehensive information about each tumor type:

### Glioma

- **Description**: Most common primary brain tumor arising from glial cells
- **Symptoms**: Headaches, seizures, vision problems, speech difficulties
- **Treatment**: Surgery, radiation therapy, chemotherapy

### Meningioma

- **Description**: Tumor from protective brain membranes
- **Symptoms**: Gradual headaches, vision changes, hearing loss
- **Treatment**: Surgical removal, radiation therapy for inoperable cases

### Pituitary

- **Description**: Tumor in the hormone-controlling pituitary gland
- **Symptoms**: Vision problems, hormonal imbalances, headaches
- **Treatment**: Medication, surgery, radiation therapy

## 🎨 Design & UI

### Theme

- **Color Scheme**: Modern medical-inspired palette
- **Typography**: Funnel Display font for enhanced readability
- **Icons**: Custom medical icons and Material Design icons
- **Layout**: Responsive design with consistent spacing

### Components

- Custom top app bar with branding
- Bottom navigation for main sections
- Material 3 cards and buttons
- Smooth transitions and animations

## 🔒 Permissions

### Required Permissions

- `INTERNET`: For API communication
- `READ_EXTERNAL_STORAGE`: For accessing images from device storage

### Security

- Network security configuration for HTTPS enforcement
- Input validation for uploaded images
- Secure API communication with error handling

## 🚀 Building for Production

### Release Build

```bash
./gradlew assembleRelease
```

### Optimization

- ProGuard rules configured for release builds
- APK optimization enabled
- Unused resources removed

## 🤝 Contributing

We welcome contributions to improve NeuroView-Exam! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Development Guidelines

- Follow Kotlin coding conventions
- Use Jetpack Compose best practices
- Add appropriate comments and documentation
- Test thoroughly on multiple devices

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For support, questions, or feedback:

- Open an issue on GitHub
- Contact the development team
- Check the documentation

**Made with ❤️ for advancing medical technology**

_NeuroView-Mobile - Making brain tumor analysis accessible, accurate, and efficient._
