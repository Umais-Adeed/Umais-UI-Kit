package com.zafbush.umais_ui_kit.shaders

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShaderLinesApp()
        }
    }
}

@Composable
fun ShaderLinesApp() {
    Box(modifier = Modifier.fillMaxSize()) {
        ShaderLinesGL()

    }
}

@Composable
fun ShaderLinesGL() {
    val renderer = remember { ShaderRenderer() }

    AndroidView(
        factory = { context ->
            GLSurfaceView(context).apply {
                setEGLContextClientVersion(2)
                setRenderer(renderer)
                renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

class ShaderRenderer : GLSurfaceView.Renderer {
    private var program = 0
    private var timeUniformLocation = 0
    private var resolutionUniformLocation = 0
    private var time = 0f

    companion object {
        const val VERTEX_SHADER_CODE = """
            attribute vec4 aPosition;
            void main() {
                gl_Position = aPosition;
            }
        """

        const val FRAGMENT_SHADER_CODE = """
            #ifdef GL_FRAGMENT_PRECISION_HIGH
            precision highp float;
            #else
            precision mediump float;
            #endif
            
            uniform vec2 resolution;
            uniform float time;
            
            float random(in float x) {
                return fract(sin(x) * 1e4);
            }
            
            float random(vec2 st) {
                return fract(sin(dot(st.xy, vec2(12.9898, 78.233))) * 43758.5453123);
            }
            
            void main(void) {
                vec2 uv = (gl_FragCoord.xy * 2.0 - resolution.xy) / min(resolution.x, resolution.y);
                
                vec2 fMosaicScal = vec2(4.0, 2.0);
                vec2 vScreenSize = vec2(256.0, 256.0);
                uv.x = floor(uv.x * vScreenSize.x / fMosaicScal.x) / (vScreenSize.x / fMosaicScal.x);
                uv.y = floor(uv.y * vScreenSize.y / fMosaicScal.y) / (vScreenSize.y / fMosaicScal.y);       
                
                float t = time * 0.06 + random(uv.x) * 0.4;
                float lineWidth = 0.0008;
                vec3 color = vec3(0.0);
                
                for(int j = 0; j < 3; j++) {
                    for(int i = 0; i < 5; i++) {
                        color[j] += lineWidth * float(i * i) / 
                                   abs(fract(t - 0.01 * float(j) + float(i) * 0.01) * 1.0 - length(uv));        
                    }
                }
                
                gl_FragColor = vec4(color[2], color[1], color[0], 1.0);
            }
        """
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, VERTEX_SHADER_CODE)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_CODE)

        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }

        timeUniformLocation = GLES20.glGetUniformLocation(program, "time")
        resolutionUniformLocation = GLES20.glGetUniformLocation(program, "resolution")
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        GLES20.glUseProgram(program)
        GLES20.glUniform2f(resolutionUniformLocation, width.toFloat(), height.toFloat())
    }

    override fun onDrawFrame(gl: GL10?) {
        time += 0.05f
        GLES20.glUseProgram(program)
        GLES20.glUniform1f(timeUniformLocation, time)

        // Draw full-screen quad
        val vertices = floatArrayOf(
            -1f, -1f,  // bottom left
            1f, -1f,  // bottom right
            -1f,  1f,  // top left
            1f,  1f   // top right
        )

        val vertexBuffer = java.nio.ByteBuffer.allocateDirect(vertices.size * 4)
            .order(java.nio.ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply { put(vertices); position(0) }

        val positionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(positionHandle)
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }
}

@Preview
@Composable
fun ShaderLinesAppPreview() {
    ShaderLinesApp()
}