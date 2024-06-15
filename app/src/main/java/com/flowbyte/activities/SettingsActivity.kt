package com.flowbyte.activities

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.flowbyte.R
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.flowbyte.databinding.ProfileHeaderBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.lang.Exception
import java.util.UUID

class SettingsActivity : AppCompatActivity() {
    private lateinit var _binding: ProfileHeaderBinding
    var fileUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        _binding = ProfileHeaderBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.editIcon.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "choose image to upload"), 0)
            if (fileUri != null) {
                UploadImage()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Please select image to upload",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
                _binding.profileImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Log.e("Exception", "Error: " + e)
            }
        }
    }

    fun UploadImage() {
        if (fileUri != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading image...")
            progressDialog.setMessage("Processing...")
            progressDialog.show()

            val ref: StorageReference =
                FirebaseStorage.getInstance().getReference().child(UUID.randomUUID().toString())
            ref.putFile(fileUri!!).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "File Uploaded Successfully", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(applicationContext, "File Upload Failed...", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}