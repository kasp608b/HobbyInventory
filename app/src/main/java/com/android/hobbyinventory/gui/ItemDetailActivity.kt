package com.android.hobbyinventory.gui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.android.hobbyinventory.R
import kotlinx.android.synthetic.main.activity_item_detail.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ItemDetailActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 1
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE = 101
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 102
    val permissions = mutableListOf<String>()
    val TAG = "xyz"

    var mFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)
        checkPermissions()

        if(intent.extras != null) {
            val extras: Bundle = intent.extras!!
        }


    }

    //Checks if permission is granted to use the various required parts.
    private fun isPermissionGiven(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return permissions.all { p -> checkSelfPermission(p) == PackageManager.PERMISSION_GRANTED}
        }
        return true
    }

    //Checks all permissions.
    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        if ( ! isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) ) permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if ( ! isGranted(Manifest.permission.CAMERA) ) permissions.add(Manifest.permission.CAMERA)
        if ( ! isGranted(Manifest.permission.ACCESS_FINE_LOCATION) ) permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        if ( ! isGranted(Manifest.permission.ACCESS_COARSE_LOCATION) ) permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permissions.size > 0)
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)

    }

    //Checks if the permissions are granted.
    private fun isGranted(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    // show the image [bmap] in the imageview [img] - and put meta data in [txt]
    private fun showImageFromBitmap(img: ImageView, bmap: Bitmap) {
        img.setImageBitmap(bmap)
        //img.setLayoutParams(RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        //img.setBackgroundColor(Color.RED)


    }

    //Grabs the new image and inputs it into the app.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE ->
                if (resultCode == RESULT_OK)
                    showImageFromFile(imgItem, mFile!!)
                else handleOther(resultCode)

            CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP ->
                if (resultCode == RESULT_OK) {
                    val extras = data!!.extras
                    val imageBitmap = extras!!["data"] as Bitmap
                    showImageFromBitmap(imgItem, imageBitmap)
                } else handleOther(resultCode)
        }
    }

    //Handles cancelling the camera or if the image is not taken.
    private fun handleOther(resultCode: Int) {
        if (resultCode == RESULT_CANCELED)
            Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show()
        else Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show()
    }

    // show the image allocated in [f] in imageview [img]. Show meta data in [txt]
    private fun showImageFromFile(img: ImageView,  f: File) {
        img.setImageURI(Uri.fromFile(f))
        img.setBackgroundColor(Color.RED)
        //mImage.setRotation(90);

    }

    //Creates a file to save an image, checks whether or not it can.
    fun onTakeByFile(view: View) {
        mFile = getOutputMediaFile("Camera01") // create a file to save the image

        if (mFile == null) {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show()
            return
        }


        // create Intent to take a picture
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val applicationId = "com.android.hobbyinventory"
        intent.putExtra(
            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
            this,
            "${applicationId}.provider",  //use your app signature + ".provider"
            mFile!!))

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_FILE)
        } else Log.d(TAG, "camera app could NOT be started")

    }

    // return a new file with a timestamp name in a folder named [folder] in
    // the external directory for pictures.
    // Return null if the file cannot be created
    private fun getOutputMediaFile(folder: String): File? {
        // in an emulated device you can see the external files in /sdcard/Android/data/<your app>.
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), folder)
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory")
                return null
            }
        }

        // Create a media file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val postfix = "jpg"
        val prefix = "IMG"
        return File(mediaStorageDir.path +
                File.separator + prefix +
                "_" + timeStamp + "." + postfix)
    }
}