package cn.lzp.shortcutmanager

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.lzp.shortcutmanager.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.createShortcut.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
                val shortcutManager = this.getSystemService(ShortcutManager::class.java)

                val shortcut = ShortcutInfo.Builder(this, "id1")
                    .setShortLabel("Website")
                    .setLongLabel("Open the website")
                    .setIcon(Icon.createWithResource(this, R.drawable.compose_icon))
                    .setIntent(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.mysite.example.com/")
                        )
                    )
                    .build()

                shortcutManager!!.dynamicShortcuts = Arrays.asList(shortcut)
            }
        }

        mBinding.createPinnShortcut.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val shortcutManager = getSystemService(ShortcutManager::class.java)

                if (shortcutManager!!.isRequestPinShortcutSupported) {
                    // Assumes there's already a shortcut with the ID "my-shortcut".
                    // The shortcut must be enabled.
                    val pinShortcutInfo = ShortcutInfo.Builder(this, "my-shortcut")
                        .setIcon(Icon.createWithResource(this, R.drawable.compose_icon))
                        .setShortLabel("ShortcutActivity")
                        .setIntent(Intent("cn.lzp.shortcutmanager.ShortcutActivity"))
                        .build()

                    // Create the PendingIntent object only if your app needs to be notified
                    // that the user allowed the shortcut to be pinned. Note that, if the
                    // pinning operation fails, your app isn't notified. We assume here that the
                    // app has implemented a method called createShortcutResultIntent() that
                    // returns a broadcast intent.
                    val pinnedShortcutCallbackIntent =
                        shortcutManager.createShortcutResultIntent(pinShortcutInfo)

                    // Configure the intent so that your app's broadcast receiver gets
                    // the callback successfully.For details, see PendingIntent.getBroadcast().
                    val successCallback = PendingIntent.getBroadcast(
                        this, /* request code */ 0,
                        pinnedShortcutCallbackIntent, /* flags */ 0
                    )

                    shortcutManager.requestPinShortcut(
                        pinShortcutInfo,
                        successCallback.intentSender
                    )
                }
            }

        }

        mBinding.button.setOnClickListener {
            startActivity(Intent("cn.lzp.shortcutmanager.ShortcutActivity"))

        }
    }
}