package com.example.pra05_anonymoussns

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    }

    // RecyclerView 에서 사용하는 View 홀더 클래스스
    inner class MyViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val imageView = itemView.imageView
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