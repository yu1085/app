package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.webrtc.*

class LocalCameraActivity : AppCompatActivity() {
    private lateinit var localView: SurfaceViewRenderer
    private lateinit var eglBase: EglBase
    private lateinit var videoCapturer: VideoCapturer
    private lateinit var videoSource: VideoSource
    private lateinit var videoTrack: VideoTrack
    private lateinit var peerConnectionFactory: PeerConnectionFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_camera)

        // 权限检查
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 1)
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        localView = findViewById(R.id.localView)
        eglBase = EglBase.create()
        localView.init(eglBase.eglBaseContext, null)
        localView.setMirror(true)

        // 初始化 PeerConnectionFactory
        val options = PeerConnectionFactory.InitializationOptions.builder(this)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)
        peerConnectionFactory = PeerConnectionFactory.builder().createPeerConnectionFactory()

        // 创建视频采集器
        videoCapturer = createCameraCapturer()
        videoSource = peerConnectionFactory.createVideoSource(false)
        videoCapturer.initialize(
            SurfaceTextureHelper.create("CaptureThread", eglBase.eglBaseContext),
            this,
            videoSource.capturerObserver
        )
        videoCapturer.startCapture(640, 480, 30)

        // 创建视频轨道并渲染
        videoTrack = peerConnectionFactory.createVideoTrack("100", videoSource)
        videoTrack.addSink(localView)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            startCamera()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::videoCapturer.isInitialized) {
            try { videoCapturer.stopCapture() } catch (_: Exception) {}
            videoCapturer.dispose()
        }
        if (::videoSource.isInitialized) videoSource.dispose()
        if (::localView.isInitialized) localView.release()
        if (::eglBase.isInitialized) eglBase.release()
        if (::peerConnectionFactory.isInitialized) peerConnectionFactory.dispose()
    }

    private fun createCameraCapturer(): VideoCapturer {
        val enumerator = Camera2Enumerator(this)
        for (deviceName in enumerator.deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                return enumerator.createCapturer(deviceName, null)
            }
        }
        throw RuntimeException("No front camera found!")
    }
} 