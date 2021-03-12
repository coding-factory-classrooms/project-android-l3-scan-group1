package com.example.projetphoto.takepictures

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.projetphoto.databinding.ActivityTakepicturesBinding
import java.io.File
import java.io.FileOutputStream


private val cameraRequestId  = 1222
private const val TAG = "TakePicturesActivity"
private val PERMISSION_CODE = 1000;

class TakePicturesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTakepicturesBinding


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTakepicturesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED) {
            demandPermissions()

        }
        binding.cameraBtn.setOnClickListener {
            val cameraInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraInt, cameraRequestId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun demandPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA),
            cameraRequestId
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestId){
            val image = data?.extras?.get("data") as Bitmap
            binding.myImage.setImageBitmap(image)
            val name = saveImage(image, "test2")
            val returnIntent = Intent(this, TakePicturesActivity::class.java)
            returnIntent.putExtra("result", name)
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }
    private fun saveImage(finalBitmap: Bitmap, image_name: String): String? {
        
        val root: String = getExternalFilesDir(image_name).toString()
        val myDir = File(root)
        myDir.mkdirs()
        val fname = "Image-$image_name.jpg"
        val file = File(myDir, fname)

        val fullpath = "${getExternalFilesDir(image_name)}/${fname}"

        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            Log.i(TAG, "saveImage: $fullpath")

            return fullpath;

        } catch (e: Exception) {
            Log.e(TAG, "saveImage: ", e)
        }

        return null
    }

}