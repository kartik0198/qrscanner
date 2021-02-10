package com.kartik.barcode

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.Barcode
import com.kartik.barcode.databinding.ActivityMainBinding
import com.kartik.scanner.camera.CameraManager
import com.kartik.scanner.detection.BarcodeScannerProcessor

/**
 * Created by Kartik Singhal on October 06, 2020.
 */

class MainActivity : AppCompatActivity(), BarcodeScannerProcessor.BarcodeResultListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var cameraManager: CameraManager

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        createCameraManager()
        binding.apply {
            lifecycleOwner = this@MainActivity
        }
        if (allPermissionsGranted()) {
            cameraManager.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults:
            IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraManager.startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                        .show()
                //finish()
            }
        }
    }

    private fun createCameraManager() {
        cameraManager = CameraManager(
                this,
                binding.previewViewFinder,
                this,
                binding.graphicOverlayFinder,
                this
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onScanned(barcode: Barcode) {
        cameraManager.stopCamera()
//        navigateToDestination(
//                R.id.barcodeResultFragment,
//                R.id.action_barcodeSearchFragment_to_barcodeResultFragment,
//                bundleOf(BarcodeResultFragment.KEY_BARCODE_RAW_VALUE to barcode.rawValue)
//        )
    }

    override fun onScanError(errorMessage: String?) {
//        context?.showShortToast("Error occurred. Please try again")
//        Timber.d(errorMessage)
    }
}