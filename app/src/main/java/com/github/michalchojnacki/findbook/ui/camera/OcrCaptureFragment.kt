@file:Suppress("DEPRECATION")

package com.github.michalchojnacki.findbook.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.github.michalchojnacki.findbook.R
import com.github.michalchojnacki.findbook.ui.common.BaseFragment
import com.github.michalchojnacki.findbook.ui.common.EventObserver
import com.github.michalchojnacki.findbook.ui.di.ViewModelFactoryExtensions.activityViewModel
import com.github.michalchojnacki.findbook.ui.di.ViewModelFactoryExtensions.viewModel
import com.github.michalchojnacki.findbook.ui.di.injector
import com.github.michalchojnacki.findbook.ui.navigation.MainNavigationViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.ocr_capture_fragment.*
import java.io.IOException

private const val RC_HANDLE_GMS = 9001
private const val RC_HANDLE_CAMERA_PERM = 2
private const val PREVIEW_OPTIMAL_WIDTH = 1280
private const val PREVIEW_OPTIMAL_HEIGHT = 1024
private const val PREVIEW_OPTIMAL_FPS = 2.0f

class OcrCaptureFragment : BaseFragment() {
    private var cameraSource: CameraSource? = null
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    @Suppress("UNCHECKED_CAST")
    private val graphicOverlay: GraphicOverlay<OcrGraphic>
        get() = scannerOverlay as GraphicOverlay<OcrGraphic>

    private val viewModel: OcrCaptureViewModel by viewModel { injector.ocrCaptureViewModel }
    private val navigationViewModel: MainNavigationViewModel by activityViewModel { injector.mainNavigationViewModel }

    companion object {
        fun newInstance() = OcrCaptureFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.ocr_capture_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())

        val context = this.context ?: return
        val rc = ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource()
        } else {
            requestCameraPermission()
        }

        view.setOnTouchListener { _, event -> scaleGestureDetector.onTouchEvent(event) }
        scannerGoToSearchTextBtn.setOnClickListener { navigationViewModel.showTypingSearch() }
        viewModel.onValidatedTextLiveData.observe(this, EventObserver { query ->
            navigationViewModel.showBookList(query)
        })
    }

    /**
     * Handles the requesting of the camera permission.
     */
    private fun requestCameraPermission() {
        val activity = this.activity ?: return
        val permissions = arrayOf(Manifest.permission.CAMERA)

        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.CAMERA
            )
        ) {
            requestPermissions(permissions, RC_HANDLE_CAMERA_PERM)
            return
        }

        AlertDialog.Builder(context).setTitle(R.string.no_camera_permission_title)
            .setMessage(R.string.permission_camera_rationale)
            .setPositiveButton(R.string.ok) { _, _ ->
                requestPermissions(permissions, RC_HANDLE_CAMERA_PERM)
            }.show()
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the ocr detector to detect small text samples
     * at long distances.
     *
     *
     * Suppressing InlinedApi since there is a check that the minimum version is met before using
     * the constant.
     */
    @SuppressLint("InlinedApi", "UsableSpace")
    private fun createCameraSource(autoFocus: Boolean = true) {
        val context = context?.applicationContext ?: return

        val textRecognizer = TextRecognizer.Builder(context).build()
        textRecognizer.setProcessor(OcrDetectorProcessor(graphicOverlay, OcrOnTextDetectedListener {
            viewModel.onTextDetected(it)
        }))

        if (!textRecognizer.isOperational) {
            val hasLowStorage =
                context.cacheDir?.let { cacheDir -> cacheDir.usableSpace * 100 / cacheDir.totalSpace <= 10 }
                    ?: false
            if (hasLowStorage) {
                Toast.makeText(context, R.string.low_storage_error, Toast.LENGTH_LONG).show()
            }
        }

        cameraSource = CameraSource.Builder(context, textRecognizer)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedPreviewSize(PREVIEW_OPTIMAL_WIDTH, PREVIEW_OPTIMAL_HEIGHT)
            .setRequestedFps(PREVIEW_OPTIMAL_FPS)
            .setFocusMode(if (autoFocus) Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO else null)
            .build()
    }

    /**
     * Restarts the camera.
     */
    override fun onResume() {
        super.onResume()
        startCameraSource()
    }

    /**
     * Stops the camera.
     */
    override fun onPause() {
        super.onPause()
        scannerPreview?.stop()
    }

    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    override fun onDestroy() {
        super.onDestroy()
        scannerPreview?.release()
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on [.requestPermissions].
     *
     *
     * **Note:** It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     *
     *
     * @param requestCode  The request code passed in [.requestPermissions].
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     * which is either [PackageManager.PERMISSION_GRANTED]
     * or [PackageManager.PERMISSION_DENIED]. Never null.
     * @see .requestPermissions
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createCameraSource()
            return
        }

        AlertDialog.Builder(context).setTitle(R.string.no_camera_permission_title)
            .setMessage(R.string.no_camera_permission_msg)
            .setPositiveButton(R.string.ok) { _, _ ->
                navigationViewModel.showTypingSearch(becauseOfNoCameraPermission = true)
            }.show()
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    @Throws(SecurityException::class)
    private fun startCameraSource() {
        // check that the device has play services available.
        val activity = this.activity ?: return

        val code = GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(activity.applicationContext)
        if (code != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(activity, code, RC_HANDLE_GMS).show()
        }

        if (cameraSource != null) {
            try {
                scannerPreview?.start(cameraSource, graphicOverlay)
            } catch (e: IOException) {
                cameraSource?.release()
                cameraSource = null
            }

        }
    }

    private inner class ScaleListener : ScaleGestureDetector.OnScaleGestureListener {

        /**
         * Responds to scaling events for a gesture in progress.
         * Reported by pointer motion.
         *
         * @param detector The detector reporting the event - use this to
         * retrieve extended info about event state.
         * @return Whether or not the detector should consider this event
         * as handled. If an event was not handled, the detector
         * will continue to accumulate movement until an event is
         * handled. This can be useful if an application, for example,
         * only wants to update scaling factors if the change is
         * greater than 0.01.
         */
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            return false
        }

        /**
         * Responds to the beginning of a scaling gesture. Reported by
         * new pointers going down.
         *
         * @param detector The detector reporting the event - use this to
         * retrieve extended info about event state.
         * @return Whether or not the detector should continue recognizing
         * this gesture. For example, if a gesture is beginning
         * with a focal point outside of a region where it makes
         * sense, onScaleBegin() may return false to ignore the
         * rest of the gesture.
         */
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            return true
        }

        /**
         * Responds to the end of a scale gesture. Reported by existing
         * pointers going up.
         *
         *
         * Once a scale has ended, [ScaleGestureDetector.getFocusX]
         * and [ScaleGestureDetector.getFocusY] will return focal point
         * of the pointers remaining on the screen.
         *
         * @param detector The detector reporting the event - use this to
         * retrieve extended info about event state.
         */
        override fun onScaleEnd(detector: ScaleGestureDetector) {
            cameraSource?.doZoom(detector.scaleFactor)
        }
    }
}
