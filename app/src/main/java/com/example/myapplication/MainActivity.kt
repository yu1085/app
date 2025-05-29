package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.databinding.ActivityMainBinding
import org.webrtc.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var eglBaseContext: EglBase.Context
    private lateinit var peerConnectionFactory: PeerConnectionFactory
    private lateinit var localVideoTrack: VideoTrack
    private lateinit var localAudioTrack: AudioTrack
    private lateinit var localVideoSource: VideoSource
    private lateinit var localAudioSource: AudioSource
    private lateinit var cameraVideoCapturer: CameraVideoCapturer
    private var peerConnection: PeerConnection? = null

    private val requiredPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(this, requiredPermissions, PERMISSION_REQUEST_CODE)
        } else {
            initializeWebRTC()
        }

        setupUI()
    }

    private fun setupUI() {
        binding.btnToggleCamera.setOnClickListener {
            cameraVideoCapturer.switchCamera(null)
        }

        binding.btnToggleMic.setOnClickListener {
            localAudioTrack.setEnabled(!localAudioTrack.enabled())
        }

        binding.btnEndCall.setOnClickListener {
            finish()
        }
    }

    private fun initializeWebRTC() {
        val eglBase = EglBase.create()
        eglBaseContext = eglBase.eglBaseContext

        // Initialize PeerConnectionFactory
        val options = PeerConnectionFactory.InitializationOptions.builder(this)
            .setEnableInternalTracer(true)
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)

        val builder = PeerConnectionFactory.builder()
        peerConnectionFactory = builder.createPeerConnectionFactory()

        // Create video capturer
        cameraVideoCapturer = createCameraCapturer()
        localVideoSource = peerConnectionFactory.createVideoSource(cameraVideoCapturer.isScreencast)
        cameraVideoCapturer.initialize(
            SurfaceTextureHelper.create("CaptureThread", eglBaseContext),
            this,
            localVideoSource.capturerObserver
        )

        // Create audio source
        val audioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
        localAudioSource = audioSource
        localAudioTrack = peerConnectionFactory.createAudioTrack("audio_track", localAudioSource)

        // Create video track
        localVideoTrack = peerConnectionFactory.createVideoTrack("video_track", localVideoSource)

        // Initialize views
        binding.localView.init(eglBaseContext, null)
        binding.remoteView.init(eglBaseContext, null)

        // Start capturing
        cameraVideoCapturer.startCapture(1280, 720, 30)
        localVideoTrack.addSink(binding.localView)
    }

    private fun createCameraCapturer(): CameraVideoCapturer {
        val cameraEnumerator = Camera2Enumerator(this)
        val deviceNames = cameraEnumerator.deviceNames

        // Try to find front facing camera
        for (deviceName in deviceNames) {
            if (cameraEnumerator.isFrontFacing(deviceName)) {
                return cameraEnumerator.createCapturer(deviceName, null)
            }
        }

        // If front facing camera not found, try back facing camera
        for (deviceName in deviceNames) {
            if (cameraEnumerator.isBackFacing(deviceName)) {
                return cameraEnumerator.createCapturer(deviceName, null)
            }
        }

        throw RuntimeException("No camera found")
    }

    private fun hasPermissions(): Boolean {
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                initializeWebRTC()
            } else {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraVideoCapturer.dispose()
        localVideoTrack.dispose()
        localAudioTrack.dispose()
        localVideoSource.dispose()
        localAudioSource.dispose()
        peerConnectionFactory.dispose()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }
}