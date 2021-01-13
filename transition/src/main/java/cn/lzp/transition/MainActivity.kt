package cn.lzp.transition

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.lzp.transition.fragment.ListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, ListFragment())
            .commit()
    }
}