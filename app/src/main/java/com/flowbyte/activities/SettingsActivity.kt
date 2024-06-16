package com.flowbyte.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.flowbyte.R
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception
import java.util.UUID

class SettingsActivity : AppCompatActivity() {
    private var fileUri: Uri? = null
    private lateinit var profileImage: CircleImageView
    private lateinit var editIcon: ImageView
    private lateinit var progress: CircularProgressIndicator
    private lateinit var name: TextView
    private lateinit var email: TextView
    private var user: FirebaseUser? = null

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            fileUri = uri
            if (fileUri != null) {
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
//                    profileImage.setImageBitmap(bitmap)
                    uploadImage()
                } catch (e: Exception) {
                    Log.e("Exception", "Error: $e")
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        user = Firebase.auth.currentUser

        name = findViewById(R.id.profileName)
        email = findViewById(R.id.profileEmail)
        editIcon = findViewById(R.id.editIcon)
        profileImage = findViewById(R.id.profileImage)
        progress = findViewById(R.id.progressCircular)

        name.text = user?.displayName
        email.text = user?.email

        Log.d("Image User: ", user?.photoUrl.toString())
        if (user?.photoUrl != null) {
            Glide.with(this).load(user?.photoUrl).into(profileImage)
        }

        editIcon.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }
    }

    private fun uploadImage() {
        if (fileUri == null) return

        progress.isIndeterminate = true

        val ref: StorageReference =
            FirebaseStorage.getInstance().getReference().child(user?.uid.toString())

        ref.putFile(fileUri!!).addOnSuccessListener {
            progress.isIndeterminate = false
            Toast.makeText(applicationContext, "File Uploaded Successfully", Toast.LENGTH_SHORT)
                .show()
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "File Upload Failed...", Toast.LENGTH_SHORT)
                .show()
        }.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                Log.d("Download URL: ", downloadUri.toString())
                user?.updateProfile(userProfileChangeRequest {
                    photoUri = downloadUri
                })

                Glide.with(this).load(downloadUri).into(profileImage)
            }
        }
    }

    private fun getCapturedImage(selectedPhotoUri: Uri): Bitmap? {
        return when {
            Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                this.contentResolver,
                selectedPhotoUri
            )

            else -> {
                val source = ImageDecoder.createSource(this.contentResolver, selectedPhotoUri)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}