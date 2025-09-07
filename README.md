# Umais' UI Kit 🎨

Welcome! This repository is the official home for my **"Weekend UI Showcase,"** a weekly series where I design, build, and share stunning, modern user interfaces. Each project is a deep dive into a specific UI concept, complete with source code and assets.

Your go-to source for UI/UX inspiration. This repo contains a weekly LinkedIn series showcasing stunning UI designs, complete with source code and assets to help you learn and build beautiful, modern applications.

---

## Featured Project: Generative Shader Lines

This week's project is a mesmerizing, generative animation created entirely with GLSL shaders running within a Jetpack Compose application. It renders a dynamic, mosaic-like pattern of flowing lines, perfect for an eye-catching background or a standalone piece of digital art.

<!-- Add a GIF or video of the animation here! -->
`![Shader Lines Animation](assets/gifweek1.mp4)`

### ✨ Tech Stack
*   **Framework:** Jetpack Compose
*   **Language:** Kotlin
*   **Graphics:** OpenGL ES 2.0
*   **Shaders:** GLSL (OpenGL Shading Language)

### How It Works

This effect is achieved by embedding a `GLSurfaceView` within the Compose UI using an `AndroidView`. The core logic resides in `ShaderRenderer.kt`:

1.  **Vertex Shader:** A minimal shader responsible for positioning a full-screen quad.
2.  **Fragment Shader:** This is where the magic happens. It calculates pixel colors based on screen coordinates (`uv`), time, and a series of mathematical functions to generate the flowing, multi-colored lines.
3.  **Jetpack Compose:** Hosts the `GLSurfaceView` and manages its lifecycle, seamlessly integrating the raw OpenGL rendering into the modern declarative UI framework.

### 📂 File Structure

All the code for this animation is self-contained in a single file:

```
app/src/main/java/com/zafbush/umais_ui_kit/shaders/shadinglines.kt
```

### 🚀 How to Run This Project

To see the animation in action, you need to set `ShaderLinesApp()` as the main content view.

1.  Open `MainActivity.kt`.
2.  Replace the existing `setContent` block with the following code:

```kotlin
// In MainActivity.kt

import com.zafbush.umais_ui_kit.shaders.ShaderLinesApp // <-- Add this import

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Replace the default theme with our shader app
            ShaderLinesApp()
        }
    }
}
```
3.  Run the app, and you will see the full-screen shader animation.

---

### About The Series

I'm Umais, and I'm passionate about crafting beautiful and performant user experiences. Follow my journey and get weekly inspiration on [**LinkedIn**](https://www.linkedin.com/in/your-profile-url/).

### License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
