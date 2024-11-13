package lab03.eim.systems.cs.pub.practicaltest01_test

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class PracticalTest01SecondaryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test01_secondary)

        val totalCountText: TextView = findViewById(R.id.totalCountText)
        val okButton: Button = findViewById(R.id.okButton)
        val cancelButton: Button = findViewById(R.id.cancelButton)

        val totalClicks = intent.getIntExtra("totalClicks", 0)
        totalCountText.text = "Total Clicks: $totalClicks"

        okButton.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}
