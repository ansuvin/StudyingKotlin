package com.example.pra11_mvvm.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pra11_mvvm.R
import com.example.pra11_mvvm.base.BaseActivity
import com.example.pra11_mvvm.databinding.ActivityMainBinding
import com.example.pra11_mvvm.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val resource: Int
        get() = R.layout.activity_main
    override val viewModel: MainViewModel
        get() = getViewModel(MainViewModel::class)

    override fun init() {

    }

    override fun observerViewModel() {
        with(viewModel) {
            onSuccessEvent.observe(this@MainActivity, {
                Toast.makeText(this@MainActivity, "성공", Toast.LENGTH_SHORT).show()
            })

            onFailEvent.observe(this@MainActivity, {
                Toast.makeText(this@MainActivity, "실패", Toast.LENGTH_SHORT).show()
            })

            btn2Event.observe(this@MainActivity, {
                val intent = Intent(this@MainActivity, SubActivity::class.java)
                startActivity(intent)
                finish()
            })
        }
    }
}