package com.example.pra05_anonymoussns

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.activity_write.recyclerView
import kotlinx.android.synthetic.main.card_post.*
import kotlinx.android.synthetic.main.card_post.contentsText

class DetailActivity : AppCompatActivity() {

    val TAG = "DetailActivity"

    val commentList = mutableListOf<Comment>()

    var postId = ""

    lateinit var preferences : SharedPreferences

    var curWriteId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        postId = intent.getStringExtra("postId")!!

        val layoutManager = LinearLayoutManager(this@DetailActivity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MyAdapter()

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE)

        getPostWriteId()

        floatingActionButton2.setOnClickListener {
            val intent = Intent(this@DetailActivity, WriteActivity::class.java)
            intent.putExtra("mode", "comment")
            intent.putExtra("postId", postId)
            startActivity(intent)
        }

        floatingActionButton3.setOnClickListener {
            if (preferences.getString("userId", "").equals(curWriteId)){
                val intent = Intent(this@DetailActivity, ModifyActivity::class.java)
                intent.putExtra("postId", postId)
                intent.putExtra("modeM", "post")
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "게시글을 수정할 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 게시글의 ID 로 게시글의 데이터로 바로 접근
        FirebaseDatabase.getInstance().getReference("/Posts/$postId")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.let {
                        val post = it.getValue(Post::class.java)
                        post?.let {
                            Picasso.get().load(post.bgUrl)
                                    .fit()
                                    .centerCrop()
                                    .into(detailBackground)
                            contentsText.text = post.message
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("err", error.message)
                }

            })

        // 게시글의 ID로 댓글 목록에 ChildEventListener를 등록
        FirebaseDatabase.getInstance().getReference("/Comments/$postId")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot?.let {
                        val comment = snapshot.getValue(Comment::class.java)
                        comment?.let { comment ->
                            val prevIndex = commentList.map { it.commentId }.indexOf(previousChildName)
                            commentList.add(prevIndex + 1, comment)
                            recyclerView.adapter?.notifyItemInserted(prevIndex + 1)
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot?.let { snapshot ->
                        val comment = snapshot.getValue(Comment::class.java)
                        comment?.let { comment ->
                            // 글이 변경된 경우 글의 앞의 데이터 인덱스에 데이터를 변경
                            val prevIndex = commentList.map { it.commentId }.indexOf(previousChildName)
                            commentList[prevIndex + 1] = comment
                            recyclerView.adapter?.notifyItemChanged(prevIndex + 1)
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    snapshot?.let {
                        val comment = snapshot.getValue(Comment::class.java)
                        comment?.let { comment ->
                            val existIndex = commentList.map { it.commentId }.indexOf(comment.commentId)
                            commentList.removeAt(existIndex)
                            recyclerView.adapter?.notifyItemRemoved(existIndex)
                        }
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot !=null){
                        val comment = snapshot.getValue(Comment::class.java)
                        comment?.let {
                            // 기존의 인덱스 구하기
                            val existIndex = commentList.map { it.commentId }.indexOf(it.commentId)
                            // 기존 데이터 지우기
                            commentList.removeAt(existIndex)
                            recyclerView.adapter?.notifyItemRemoved(existIndex)

                            // previousChildName 다음 글로 추가
                            val prevIndex = commentList.map { it.commentId }.indexOf(previousChildName)
                            commentList.add(prevIndex + 1, it)
                            recyclerView.adapter?.notifyItemInserted(prevIndex + 1)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("err", error.message)
                }

            })
    }

    fun getPostWriteId() {
        val database = FirebaseDatabase.getInstance().reference
        database.child("Posts").child(postId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val writeId = snapshot.child("writeId").getValue(String::class.java)
                curWriteId = writeId.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAGDetailActivity", "err")
            }
        })
    }

    inner class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(LayoutInflater.from(this@DetailActivity).inflate(R.layout.card_comment, parent, false))
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val comment = commentList[position]
            comment?.let {
                Picasso.get()
                    .load(Uri.parse(comment.bgUrl))
                    .fit()
                    .centerCrop()
                    .into(holder.imageView)
                holder.commentText.text = comment.message
            }

            holder.itemView.setOnClickListener {
                if (preferences.getString("userId", "").equals(comment.writerId)){
                    val intent = Intent(this@DetailActivity, ModifyActivity::class.java)
                    intent.putExtra("commentId", comment.commentId)
                    intent.putExtra("postId", postId)
                    intent.putExtra("modeM", "comment")
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "댓글을 수정할 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun getItemCount(): Int {
            return commentList.size
        }

    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.background)
        val commentText = itemView.findViewById<TextView>(R.id.commentText)
    }
}