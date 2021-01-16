package com.example.pra05_anonymoussns

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.card_background.view.*
import kotlinx.android.synthetic.main.card_post.view.*
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Minutes
import java.text.SimpleDateFormat
import java.util.*
import java.util.prefs.Preferences

class MainActivity : AppCompatActivity() {

    // 글 목록을 저장하는 변수
    val posts: MutableList<Post> = mutableListOf()

    lateinit var preferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "글목록"

        floatingActionButton.setOnClickListener {
            val intent = Intent(this@MainActivity, WriteActivity::class.java)
            startActivity(intent)
        }

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("userId", getMyId())
        editor.apply()

        // RecyclerView 에 LayoutManager 설정
        val layoutManager = LinearLayoutManager(this@MainActivity)

        // 리사이클러뷰 아이템 역순으로 정렬
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MyAdapter()

        // Firebase 에서 Post 데이터를 가져온 후 posts 변수에 저장
        FirebaseDatabase.getInstance().getReference("/Posts")
                .orderByChild("writeTime").addChildEventListener(object : ChildEventListener {
                    // 글이 추가된 경우
                    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                        snapshot?.let {
                            // snapshop 의 데이트럴 Post 객체로 가조오기
                            val post = snapshot.getValue(Post::class.java)
                            post?.let {
                                // 새 글이 마지막 부분에 추가된 경우
                                if (previousChildName == null) {
                                    // 글 목록을 저장하는 변수에 post 객체 추가
                                    posts.add(it)
                                    recyclerView.adapter?.notifyItemInserted(posts.size -1) // 데이터 추가했어요~ 알리기
                                } else {
                                    // 글이 중간에 삽입된 경우 previousChildName로 한단계 앞으 데이터의 위치를 찾은 뒤 데이터 추가
                                    val prevIndex = posts.map { it.postId }.indexOf(previousChildName)
                                    posts.add(prevIndex + 1, post)
                                    recyclerView.adapter?.notifyItemInserted(prevIndex + 1)
                                }
                            }
                        }
                    }

                    // 글이 변경된 경우
                    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                        snapshot?.let { snapshot ->
                            // snapshow 의 데이터를 Post 객체로 가져오기
                            val post = snapshot.getValue(Post::class.java)
                            post?.let { post ->
                                // 글이 변경된 경우 글의 앞의 데이터 인덱스에 데이터를 변경
                                val prevIndex = posts.map { it.postId }.indexOf(previousChildName)
                                posts[prevIndex + 1] = post
                                recyclerView.adapter?.notifyItemChanged(prevIndex + 1)
                            }
                        }
                    }

                    // 글이 삭제된 경우
                    override fun onChildRemoved(snapshot: DataSnapshot) {
                        snapshot?.let {
                            // snapshow 의 데이터를 Post 객체로 가져오기
                            val post = snapshot.getValue(Post::class.java)

                            post?.let { post ->
                                // 기존의 인덱스를 찾아 해당 인덱스의 데이터를 삭제
                                val existIndex = posts.map { it.postId }.indexOf(post.postId)
                                // 기존 데이터 지우기
                                posts.removeAt(existIndex)
                                recyclerView.adapter?.notifyItemRemoved(existIndex)
                            }
                        }
                    }

                    // 글의 순서가 이동된 경우
                    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                        snapshot?.let {
                            // snapshow 의 데이터를 Post 객체로 가져오기
                            val post = snapshot.getValue(Post::class.java)

                            post?.let { post ->
                                // 기존의 인덱스 구하기
                                val existIndex = posts.map { it.postId }.indexOf(post.postId)
                                // 기존 데이터 지우기
                                posts.removeAt(existIndex)
                                recyclerView.adapter?.notifyItemRemoved(existIndex)

                                // previousChildName 이 없는 경우 맨마지막으로 이동 됨
                                if (previousChildName == null) {
                                    posts.add(post)
                                    recyclerView.adapter?.notifyItemChanged(posts.size - 1)
                                } else {
                                    // previousChildName 다음 글로 추가
                                    val prevIndex = posts.map { it.postId }.indexOf(previousChildName)
                                    posts.add(prevIndex + 1, post)
                                    recyclerView.adapter?.notifyItemChanged(prevIndex + 1)
                                }
                            }
                        }
                    }

                    // 취소된 경우
                    override fun onCancelled(error: DatabaseError) {
                        error?.toException()?.printStackTrace()
                        Log.e("TAG", error.message.toString())
                    }

                })

    }

    // RecyclerView 에서 사용하는 View 홀더 클래스스
    inner class MyViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.imageView1
        val contentsText: TextView = itemView.contentsText
        val timeTextView: TextView = itemView.timeTextView
        val commentCountText: TextView = itemView.commentCountTextView
    }

    // RecyclerView 의 어댑터 클래스
    inner class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
        // RecyclerView 에서 각 행에서 그릴 ViewHolder 를 생성할때 부름
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            // RecyclerView에서 사용하는 ViewHolder 클래스를 card_background 레이아웃 리소르 파일을 사용
            return MyViewHolder(LayoutInflater.from(this@MainActivity).inflate(R.layout.card_post, parent, false))
        }

        // 각 생이 포지션에 그려야 할 viewHolder UI 에 데이터를 적용하는 메소드
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val post = posts[position]
            // 배경 이미지 설정
            Picasso.get().load(Uri.parse(post.bgUrl)).fit().centerCrop().into(holder.imageView)
            // 카드에 글 쓰기
            holder.contentsText.text = post.message
            holder.timeTextView.text = getDiffTimeText(post.writeTime as Long)
            holder.commentCountText.text = post.commentCount.toString()
            Log.e("TEST", post.commentCount.toString())

            // 카드가 클릭된 경우
            holder.itemView.setOnClickListener {
                // 상세화면을 호출할 Intent 를 생성
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("postId", post.postId)
                startActivity(intent)
            }
        }

        // RecyclerView 에서 몇개의 행을 그릴지
        override fun getItemCount(): Int {
            return posts.size
        }

    }

    // 글이 쓰여진 시간을  여러 포맷으로 반환해주는 메소드
    fun getDiffTimeText(targetTime: Long): String {
        val curDateTime = DateTime()
        val targetDateTime = DateTime().withMillis(targetTime)

        val diffDay = Days.daysBetween(curDateTime, targetDateTime).days
        val diffHours = Hours.hoursBetween(targetDateTime, curDateTime).hours
        val diffMinutes = Minutes.minutesBetween(targetDateTime, curDateTime).minutes
        if(diffDay == 0) {
            if (diffHours ==0 && diffMinutes == 0){
                return "방금 전"
            }
            else if (diffHours > 0) {
                return ""+ diffHours+"시간 전"
            } else return "" + diffMinutes + "분 전"
        }
        else {
            val format = SimpleDateFormat("yyyy년 MM월 DD일 HH:mm")
            return format.format(Date(targetTime))
        }
    }

    // 디바이스 아이디 반환
    private fun getMyId(): String {
        return Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
    }

}