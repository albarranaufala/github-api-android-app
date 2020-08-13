package com.example.githubuserapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.githubuserapi.fragment.MyPreferenceFragment

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferenceFragment()).commit()

        supportActionBar?.let {
            it.title = resources.getString(R.string.setting)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}