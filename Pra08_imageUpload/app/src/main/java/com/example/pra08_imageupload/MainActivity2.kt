package com.example.pra08_imageupload

import android.Manifest.permission.*
import android.annotation.TargetApi
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*


class MainActivity2 : AppCompatActivity() {

    var apiService: ApiService? = null
    var picUri: Uri? = null
    private var permissionsToRequest: ArrayList<String>? = null
    private val permissionsRejected: ArrayList<String> = ArrayList()
    private val permissions: ArrayList<String> = ArrayList()
    private val ALL_PERMISSIONS_RESULT = 107
    private val IMAGE_RESULT = 200
    var mBitmap: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 퍼미션 요청
        askPermissions()
        // 레트로핏 설정
        initRetrofitClient()

        getImage()

        // 이미지 받아오기 버튼
        getImgBtn.setOnClickListener {
            startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT)
        }

        // 이미지 업로드하기 버튼
        uploadImgBtn.setOnClickListener {
            if (mBitmap != null) {
                multipartImageUpload()
            } else {
                Toast.makeText(applicationContext, "비트맵 비어있음", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun getImage() {
        Picasso.get().load("http://f0f3e680fa1c.ngrok.io/uploads/359cc2d83bd7eecabec16e64a2690efd.jpg").into(imageView)
        Glide.with(applicationContext).load("http://f0f3e680fa1c.ngrok.io/uploads/359cc2d83bd7eecabec16e64a2690efd.jpg").into(imageView2)
    }

    // 퍼미션 요청
    private fun askPermissions() {
        // 카메라
        //permissions.add(CAMERA)
        // 외부 저장소 쓰기/읽기
        permissions.add(WRITE_EXTERNAL_STORAGE)
        permissions.add(READ_EXTERNAL_STORAGE)
        // 요청하지 않은 퍼미션 찾아내기
        permissionsToRequest = findUnAskedPermissions(permissions)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest!!.size > 0) requestPermissions(
                    permissionsToRequest!!.toArray(
                            arrayOfNulls<String>(permissionsToRequest!!.size)
                    ), ALL_PERMISSIONS_RESULT
                    // 요청하기
            )
        }
    }

    // 레드로핏 설정
    private fun initRetrofitClient() {
        val client = OkHttpClient.Builder().build()
        apiService =
            Retrofit.Builder().baseUrl("http://f0f3e680fa1c.ngrok.io").client(client).build().create(
                    ApiService::class.java
            )
    }

    // 이미지 선택하기 (가져오기)
    fun getPickImageChooserIntent(): Intent? {
        val outputFileUri: Uri = getCaptureImageOutputUri()!!
        val allIntents: MutableList<Intent> = ArrayList()
        val packageManager = packageManager
        // 이미지 선택 Intent
        val captureIntent = Intent(Intent.ACTION_GET_CONTENT)
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            val intent = Intent(captureIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            }
            allIntents.add(intent)
        }
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)
        for (res in listGallery) {
            val intent = Intent(galleryIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            allIntents.add(intent)
        }
        var mainIntent = allIntents[allIntents.size - 1]
        for (intent in allIntents) {
            if (intent.component!!.className == "com.android.documentsui.DocumentsActivity") {
                mainIntent = intent
                break
            }
        }
        allIntents.remove(mainIntent)
        val chooserIntent = Intent.createChooser(mainIntent, "Select source")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray())
        return chooserIntent
    }

    // 이미지 Uri 가져오기
    private fun getCaptureImageOutputUri(): Uri? {
        var outputFileUri: Uri? = null
        val getImage: File? = getExternalFilesDir("")
        if (getImage != null) {
            outputFileUri = Uri.fromFile(File(getImage.path, "profile.png"))
        }
        return outputFileUri
    }

    // startActivityForResult 끝나고
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_RESULT) {
                // 파일 경로 가져오기
                val filePath = getImageFilePath(data)
                if (filePath != null) {
                    // 파일 비트맵으로 만들기
                    mBitmap = BitmapFactory.decodeFile(filePath)
                    // 이미지 뷰에 보이기
                    imageView.setImageBitmap(mBitmap)
                }
            }
        }
    }

    // 파일경로를 사용하여 이미지 가져오기
    private fun getImageFromFilePath(data: Intent?): String? {
        val isCamera = data == null || data.data == null
        return if (isCamera) getCaptureImageOutputUri()!!.path else getPathFromURI(data!!.data)
    }

    // 이미지 경로 가져오기
    fun getImageFilePath(data: Intent?): String? {
        return getImageFromFilePath(data)
    }

    // URI를 이용하여 경로 가져오기
    private fun getPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Audio.Media.DATA)
        val cursor: Cursor? = contentResolver.query(contentUri!!, proj, null, null, null)
        val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("pic_uri", picUri)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri")
    }

    // 요청하지 않은 퍼미션 찾아내기
    private fun findUnAskedPermissions(wanted: ArrayList<String>): ArrayList<String>? {
        val result = ArrayList<String>()
        for (perm in wanted) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }
        return result
    }

    // 퍼미션이 있는지
    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun canMakeSmores(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    @TargetApi(Build.VERSION_CODES.M)

    // 드디어 올린다 서버에
    private fun multipartImageUpload() {
        try {
            val filesDir: File = applicationContext.filesDir
            val file = File(filesDir, "image" + ".png")
            val bos = ByteArrayOutputStream()
            mBitmap!!.compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmapdata: ByteArray = bos.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            val body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile)
            val name = RequestBody.create(MediaType.parse("text/plain"), "upload")
            val req: Call<ResponseBody> = apiService!!.postImage(body, name)
            req.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                ) {
                    if (response.code() == 200) {
                        textView.text = "Uploaded Successfully!";
                        textView.setTextColor(Color.BLUE);
                    }

                    Toast.makeText(applicationContext, response.code().toString(), Toast.LENGTH_SHORT).show();
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    textView.text = "Uploaded Failed!";
                    textView.setTextColor(Color.RED);
                    Toast.makeText(applicationContext, "Request failed", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }

            })
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            ALL_PERMISSIONS_RESULT -> {
                for (perms in permissionsToRequest!!) {
                    if (!hasPermission(perms!!)) {
                        permissionsRejected.add(perms!!)
                    }
                }
                if (permissionsRejected.size > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected[0])) {
                            showMessageOKCancel(
                                    "These permissions are mandatory for the application. Please allow access."
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

}