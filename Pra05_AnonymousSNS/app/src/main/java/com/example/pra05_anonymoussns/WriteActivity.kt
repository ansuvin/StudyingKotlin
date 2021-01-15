package com.example.pra05_anonymoussns

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.card_background.view.*
import com.example.pra05_anonymoussns.WriteActivity.MyViewHolder as MyViewHolder

class WriteActivity : AppCompatActivity() {

    // 배경 리스트 데이터 res/drawable 에 있는 배경 이미지를 url 주소로 사용
    // url 주소로 사용하면 추후 웹에 있는 이미지 URL도 바로 사용 가능
    val bgList = mutableListOf(
        "android.resource://com.example.pra05_anonymoussns/drawable/default_bg",
        "android.resource://com.example.pra05_anonymoussns/drawable/bg2",
        "android.resource://com.example.pra05_anonymoussns/drawable/bg3",
        "android.resource://com.example.pra05_anonymoussns/drawable/bg4",
        "android.resource://com.example.pra05_anonymoussns/drawable/bg5",
        "android.resource://com.example.pra05_anonymoussns/drawable/bg6",
        "android.resource://com.example.pra05_anonymoussns/drawable/bg7",
        "android.resource://com.example.pra05_anonymoussns/drawable/bg8",
        "android.resource://com.example.pra05_anonymoussns/drawable/bg9"
    )

    // 현재 선택된 배경이미지의 포지션 저장
    var currentBgPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)
        
        supportActionBar?.title = "글쓰기"
        
        // recyclerView 에서 사용할 레이아웃 메니저 생성
        val layoutManager = LinearLayoutManager(this@WriteActivity)
        // recyclerView 의 스크롤 방향(layoutManager)을 HORIZONTAL 로 설정
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MyAdapter()

        // 공유하기 버튼
        sendButton.setOnClickListener {
            // 메세지가 없는 경우
            if (TextUtils.isEmpty(input.text)){
                Toast.makeText(applicationContext, "메세지를 입력사헤요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Post 객체 생성
            val post = Post()
            // Firebase 의 Posts 참조에서 객체를 저장하기 위한 새로운 카를 생성하고 참조를 newRef 에 저장
            // push()를 사용하면 유니크한 키값이 생성되는 것을 보장함.
            val newRef = FirebaseDatabase.getInstance().getReference("Posts").push()
            // 글이 쓰여진 시간은 Firebase 서버의 시간으로 설정
            // ServerValue.TIMESTAMP 는 데이터가 저장되는 시점에 서버 시간을 저장함.
            post.writeTime = ServerValue.TIMESTAMP
            // 배경 Uri 주소를 현재 선택된 배경의 주소로 할당
            post.bgUrl = bgList[currentBgPosition]
            // 메세지는 input EditText 의 텍스트 내용
            post.message = input.text.toString()
            // 글쓴 사람의 ID는 디바이스 아이디로 할당
            post.writeId = getMyId()
            // 글의 ID는 새로 생성된 파이어베이스 참조키로 할당
            post.postId = newRef.key.toString()
            // Post 객체를 새로 생성한 참조에 저장
            newRef.setValue(post)
            // 저장 성공 -> Activity 종료
            Toast.makeText(applicationContext, "공유되었습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // 디바이스 아이디 반환
    private fun getMyId(): String {
        return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
    }

    // RecyclerView 에서 사용하는 View 홀더 클래스스
    inner class MyViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView = itemView.imageView1
    }

    // RecyclerView 의 어댑터 클래스
    inner class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
        // RecyclerView 에서 각 행에서 그릴 ViewHolder 를 생성할때 부름
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            // RecyclerView에서 사용하는 ViewHolder 클래스를 card_background 레이아웃 리소르 파일을 사용
            return MyViewHolder(LayoutInflater.from(this@WriteActivity).inflate(R.layout.card_background, parent, false))
        }

        // 각 생이 포지션에 그려야 할 viewHolder UI 에 데이터를 적용하는 메소드
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            // 이미지 로딩 라이브러리인 피카소 객체로 뷰홀더에 존재하는 imageView 에 이미지 로딩
            Picasso.get()
                .load(Uri.parse(bgList[position]))
                .fit()
                .centerCrop()
                .into(holder.imageView)

            // 각 배경화면 행이 클릭된 경우 (이벤트 리스너)
            holder.itemView.setOnClickListener{
                // 선택된 배경의 포지션을 currentBgPosition 에 저장
                currentBgPosition = position
                // 피카소 객체로 뷰홀더에 존재하는 글쓰기 배경 이미지뷰에 이미지 로딩
                Picasso.get()
                    .load(Uri.parse(bgList[position]))
                    .fit()
                    .centerCrop()
                    .into(writeBackground)
            }
        }

        // RecyclerView 에서 몇개의 행을 그릴지
        override fun getItemCount(): Int {
            return bgList.size
        }

    }
}