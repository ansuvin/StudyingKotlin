package com.example.pra10_databinding

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pra10_databinding.databinding.RcvListItmeBinding

class ProfileAdapter(private val context: Context): RecyclerView.Adapter<ProfileAdapter.ProfileVH>() {
    var data = listOf<ProfileData>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileVH {
        val binding = RcvListItmeBinding.inflate(LayoutInflater.from(context), parent, false)

        return ProfileVH(binding)
    }

    override fun onBindViewHolder(holder: ProfileVH, position: Int) {
        holder.onBind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    // RchListItmeBinding = rcv_list_itme.xml (파스칼 표기법으로 바뀐것)
    class ProfileVH(val binding: RcvListItmeBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: ProfileData) {
            binding.user = data
        }
    }

}