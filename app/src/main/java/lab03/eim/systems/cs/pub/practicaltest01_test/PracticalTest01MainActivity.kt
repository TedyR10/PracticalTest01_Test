package lab03.eim.systems.cs.pub.practicaltest01_test

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PracticalTest01MainActivity : AppCompatActivity() {

    private lateinit var broadcastReceiver: BroadcastReceiver

    companion object {
        const val COUNTER_THRESHOLD = 10
    }

    private var leftCounter = 0
    private var rightCounter = 0

    private lateinit var secondaryActivityLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test01_main)

        val leftTextView: TextView = findViewById(R.id.leftCounter)
        val rightTextView: TextView = findViewById(R.id.rightCounter)
        val buttonLeft: Button = findViewById(R.id.buttonLeft)
        val buttonRight: Button = findViewById(R.id.buttonRight)
        val launchSecondaryActivityButton: Button = findViewById(R.id.launchSecondaryActivityButton)

        buttonLeft.setOnClickListener {
            leftCounter++
            leftTextView.text = leftCounter.toString()
        }

        buttonRight.setOnClickListener {
            rightCounter++
            rightTextView.text = rightCounter.toString()
        }

        savedInstanceState?.let {
            leftCounter = it.getInt("leftCounter", 0)
            rightCounter = it.getInt("rightCounter", 0)
            leftTextView.text = leftCounter.toString()
            rightTextView.text = rightCounter.toString()
        }

        secondaryActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val message = when (result.resultCode) {
                Activity.RESULT_OK -> "Okay, sefule"
                Activity.RESULT_CANCELED -> "Cancel, puiu"
                else -> "No result"
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        launchSecondaryActivityButton.setOnClickListener {
            val totalClicks = leftCounter + rightCounter
            val intent = Intent(this, PracticalTest01SecondaryActivity::class.java)
            intent.putExtra("totalClicks", totalClicks)
            secondaryActivityLauncher.launch(intent)

            if (totalClicks > COUNTER_THRESHOLD) {
                val serviceIntent = Intent(this, PracticalTest01Service::class.java)
                serviceIntent.putExtra("leftCounter", leftCounter)
                serviceIntent.putExtra("rightCounter", rightCounter)
                startService(serviceIntent)
                Log.d("PracticalTest01Service", "Service intent started")
            }
        }

        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    val arithmeticMean = it.getDoubleExtra("arithmeticMean", 0.0)
                    val geometricMean = it.getDoubleExtra("geometricMean", 0.0)
                    val timestamp = it.getStringExtra("timestamp")

                    Log.d("PracticalTest01Service", "Received broadcast - Arithmetic Mean: $arithmeticMean, Geometric Mean: $geometricMean, Timestamp: $timestamp")
                }
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("leftCounter", leftCounter)
        outState.putInt("rightCounter", rightCounter)
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter().apply {
            addAction("ACTION_ONE")
            addAction("ACTION_TWO")
            addAction("ACTION_THREE")
        }
        ContextCompat.registerReceiver(this, broadcastReceiver, filter, ContextCompat.RECEIVER_NOT_EXPORTED)
    }


    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, PracticalTest01Service::class.java)) // Oprim serviciul la distrugerea activității
    }
}


