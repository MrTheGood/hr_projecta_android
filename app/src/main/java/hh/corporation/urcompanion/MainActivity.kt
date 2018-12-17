package hh.corporation.urcompanion

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import processing.android.PFragment

class MainActivity : AppCompatActivity() {
    /**
     * You can ignore this file. This is just Android specific stuff.
     */

    private val sketch by lazy {
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        Sketch(width, height)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PFragment(sketch).setView(container, this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        sketch.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    public override fun onNewIntent(intent: Intent) {
        sketch.onNewIntent(intent)
    }

    override fun onBackPressed() {
        sketch.onBackPressed()
    }
}
