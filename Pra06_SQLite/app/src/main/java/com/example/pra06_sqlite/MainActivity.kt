package com.example.pra06_sqlite

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card.view.*

class MainActivity : AppCompatActivity() {

    // 데이터베이스에서 읽은 데이터를 맵의 리스트 형태로 저장
    val dataList = mutableListOf<MutableMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter()

        fab.setOnClickListener {
            // WriteDialog
            val dialog = WriteDialog()
            // 다이얼로그 완료 리스너
            dialog.listener = {title, post ->
                // 데이터베이스 저장
                saveData(title, post)
                // recyclerView 업데이트
                updateRecyclerView()
            }
            dialog.show(supportFragmentManager, "dialog")
        }

        // 최초 실행 시에 recyclerView를 업데이트
        updateRecyclerView()
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val titleTextView: TextView
        val postTextView: TextView
        val timeTextView: TextView
        val editButton: ImageButton
        val deleteButton: ImageButton

        init {
            titleTextView = itemView.titleTextView
            postTextView = itemView.postTextView
            timeTextView = itemView.timeTextView
            editButton = itemView.editBtn
            deleteButton = itemView.deleteBtn
        }
    }

    inner class MyAdapter: RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(
                LayoutInflater.from(this@MainActivity).inflate(R.layout.card, parent, false)
            )
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.titleTextView.text = dataList[position].get("title").toString()
            holder.postTextView.text = dataList[position].get("post").toString()
            holder.timeTextView.text = dataList[position].get("time").toString()

            holder.deleteButton.setOnClickListener {
                removeData(dataList[position].get("id").toString())
                updateRecyclerView()
            }

            holder.editButton.setOnClickListener {
                val dialog = WriteDialog()
                dialog.title = dataList[position].get("title").toString()
                dialog.post = dataList[position].get("post").toString()
                dialog.listener = {title, post ->
                    // 데이터베이스에 저장
                    editData(dataList[position].get("id").toString(), title, post)
                    updateRecyclerView()
                }
                dialog.show(supportFragmentManager, "dialog")
            }
        }

        override fun getItemCount(): Int {
            return dataList.size
        }

    }

    // 데이터베이스의 데이터와 동기화하여 업데이트
    fun updateRecyclerView() {
        // 지우고
        dataList.clear()
        // 다 긁어 오고
        dataList.addAll(readAllData())
        // 바뀐거 알리기
        recyclerView.adapter?.notifyDataSetChanged()
    }

    // 데이터베이스에 데이터 저장
    fun saveData(title: String, post: String){
        val sql = "INSERT INTO post (title, post) values('$title', '$post')"
        val dbHelper = PostDbHelper(applicationContext)
        dbHelper.writableDatabase.execSQL(sql)
    }

    // post 테이블의 모든 데이터를 읽고 맵의 리스트 형태로 변환
    fun readAllData(): MutableList<MutableMap<String, String>> {
        val dbHelper = PostDbHelper(applicationContext)
        val resultList = mutableListOf<MutableMap<String, String>>()
        val cursor = dbHelper.readableDatabase.rawQuery("SELECT * FROM post", null)
        if(cursor.moveToFirst()){
            do {
                val map = mutableMapOf<String, String>()
                map["id"] = cursor.getString(cursor.getColumnIndex("id"))
                map["title"] = cursor.getString(cursor.getColumnIndex("title"))
                map["post"] = cursor.getString(cursor.getColumnIndex("post"))
                map["time"] = cursor.getString(cursor.getColumnIndex("time"))
                resultList.add(map)
            } while (cursor.moveToNext())
        }
        return resultList
    }

    // 데이터 삭제
    fun removeData(id: String) {
        val dbHelper = PostDbHelper(applicationContext)
        val sql = "DELETE FROM post where id = $id"
        dbHelper.writableDatabase.execSQL(sql)
    }

    // 데이터 수정
    fun editData(id: String, title: String, post: String) {
        val dbHelper = PostDbHelper(applicationContext)
        val sql = "UPDATE post set title = '$title', post = '$post', time = CURRENT_TIMESTAMP where id = $id"
        dbHelper.writableDatabase.execSQL(sql)
    }

}