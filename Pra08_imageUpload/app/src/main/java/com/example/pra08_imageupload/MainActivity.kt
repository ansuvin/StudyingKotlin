package com.example.pra08_imageupload

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.TargetApi
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*


class MainActivity : AppCompatActivity() {

    lateinit var apiService: ApiService

    val ALL_PERMISSIONS_RESULT = 107;
    val IMAGE_RESULT = 200;
    val REQUEST_CODE = 1;

    var user_id = ""
    var permissionsToRequest = arrayListOf<String>()
    var permissionsRejected= arrayListOf<String>()
    var permissions= arrayListOf<String>()
    var mBitmap= arrayListOf<Bitmap>()

    var picUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        askPermission();
        initRetrofitClient()

        getImgBtn.setOnClickListener {
            getPickImageChooserIntent()
        }

        uploadImgBtn.setOnClickListener {
            if (mBitmap != null) {
                for (i: Int in 0..mBitmap.size) {
                    Log.e("TAG", "비트맵 있음")
                    multipartImageUpload(i)
                }
            } else {
                Toast.makeText(applicationContext, "비트맵 없음 다시시도 바람", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun askPermission() {
        permissions.add(WRITE_EXTERNAL_STORAGE)
        permissions.add(READ_EXTERNAL_STORAGE)
        permissionsToRequest = findUnAskedPermissions(permissions)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size > 0)
                requestPermissions(
                    permissionsToRequest.toArray(
                        arrayOfNulls<String>(
                            permissionsToRequest.size
                        )
                    ), ALL_PERMISSIONS_RESULT
                )
        }
    }

    fun initRetrofitClient() {
        val client = OkHttpClient.Builder().build()

        apiService = Retrofit.Builder().baseUrl("http://localhost:3000/").client(client).build().create(
            ApiService::class.java
        )
    }

    fun getPickImageChooserIntent() {
        val allIntents = arrayListOf<Intent>()
        val packageManager = packageManager
        val images = arrayListOf<Image>()
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "이미지 다중 선택"), REQUEST_CODE);
    }

    fun getCaptureImageOutputUri(): Uri {
        var outputFileUri:Uri? = null
        val getImage = getExternalFilesDir("")
        if (getImage !=null) {
            outputFileUri = Uri.fromFile(File(getImage.path, "profile.png"))
        }
        return outputFileUri!!
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var imagePath = ""
        val imageListUri = arrayListOf<String>()
        val realUri = arrayListOf<Uri>()

        if (requestCode == REQUEST_CODE) {
            Toast.makeText(applicationContext, "여기 드가욤", Toast.LENGTH_SHORT).show()

            if (data!!.clipData == null) {
                Toast.makeText(applicationContext, "다중선택이 불가능한 기기입니다.", Toast.LENGTH_SHORT).show()
            } else {
                val clipData = data!!.clipData
                Log.e("clipData.itemCount", clipData!!.itemCount.toString())

                if (clipData!!.itemCount > 5) {
                    Toast.makeText(applicationContext, "사진은 5장만 가능이야", Toast.LENGTH_SHORT).show()
                } else if (clipData.itemCount == 5) {
                    var tempUri: Uri
                    tempUri = clipData.getItemAt(0).uri
                    imagePath = tempUri.toString()
                    Log.e("imagePath", imagePath)

                    mBitmap.add(BitmapFactory.decodeFile(imagePath))
                    imageView.setImageBitmap(mBitmap[0])
                } else if ((clipData.itemCount > 1) && (clipData.itemCount <= 5)) {
                    Toast.makeText(applicationContext, "하기싫어", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getImageFromFilePath(data: Intent):String {
        return getPathFromUri(data.data!!)
    }

    fun getImageFilePath(data: Intent): String {
        return getImageFromFilePath(data)
    }

    fun getPathFromUri(contentUri: Uri) : String {
        val proj = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor = contentResolver.query(contentUri, proj, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        outState.putParcelable("pic_uri", picUri)
    }

    fun findUnAskedPermissions(wanted: ArrayList<String>): ArrayList<String> {
        val result = arrayListOf<String>()
        for (perm: String in wanted) {
            if (!hasPermissions(perm)) {
                result.add(perm)
            }
        }
        return result
    }

    fun hasPermissions(permissions: String) : Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permissions) == PackageManager.PERMISSION_GRANTED)
            }
        }
        return true
    }

    fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    fun canMakeSmores(): Boolean {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            ALL_PERMISSIONS_RESULT -> {
                for (perms: String in permissionsToRequest) {
                    if (!hasPermissions(perms)) {
                        permissionsRejected.add(perms)
                    }
                }
                if (permissionsRejected.size > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected[0])) {
                            showMessageOKCancel(
                                "These permissions are mandatory for the application. Please allow access"
                            ) { dialog, which ->
                                requestPermissions(
                                    permissionsRejected.toTypedArray(),
                                    ALL_PERMISSIONS_RESULT
                                )
                            }
                            return
                        }
                    }
                }
            }
        }
    }

    // 실제 이미지를 업로드 하는 부분
    fun multipartImageUpload(index: Int) {
        try {
            val filesDir = applicationContext.filesDir
            val file = File(filesDir, filesDir.name+".png")

            val bos = ByteArrayOutputStream()
            mBitmap[index].compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmapdata = bos.toByteArray()
            Log.e("bitmapData", bitmapdata.toString())

            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()

            val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
            val body = MultipartBody.Part.createFormData("upload", file.name, reqFile)
            val name = RequestBody.create(MediaType.parse("text/plain"), "upload")

            Log.e("fileName", name.toString())

            val req = apiService.postImage(body, name)
            req.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.code() == 200) {
                        textView.text = "uploaded success"
                        textView.setTextColor(Color.BLUE)
                    }

                    Toast.makeText(applicationContext, "${response.code()} ", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    textView.text = "uploaded fail"
                    Log.e("TAG", t.message.toString())
                }

            })
        } catch (e: FileNotFoundException) { e.printStackTrace()}
        catch (e: IOException) { e.printStackTrace()}
    }
}