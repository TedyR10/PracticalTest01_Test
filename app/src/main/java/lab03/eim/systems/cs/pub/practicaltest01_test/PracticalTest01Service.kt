package lab03.eim.systems.cs.pub.practicaltest01_test

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import java.util.*
import kotlin.math.sqrt

class PracticalTest01Service : Service() {

    private val handler = Handler()
    private lateinit var runnable: Runnable

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.w("PracticalTest01Service", "Service started")

        val leftCounter = intent?.getIntExtra("leftCounter", 0) ?: 0
        val rightCounter = intent?.getIntExtra("rightCounter", 0) ?: 0

        runnable = Runnable {
            val arithmeticMean = (leftCounter + rightCounter) / 2.0
            val geometricMean = sqrt(leftCounter.toDouble() * rightCounter)

            val broadcastIntent = Intent()
            val actions = listOf("ACTION_ONE", "ACTION_TWO", "ACTION_THREE")
            broadcastIntent.action = actions.random()
            broadcastIntent.putExtra("arithmeticMean", arithmeticMean)
            broadcastIntent.putExtra("geometricMean", geometricMean)
            broadcastIntent.putExtra("timestamp", Date().toString())

            Log.i("ViewRootImpl@631db0[PracticalTest01MainActivity]", "Arithmetic Mean: $arithmeticMean, Geometric Mean: $geometricMean, Timestamp: ${Date().toString()}")
            sendBroadcast(broadcastIntent)

            handler.postDelayed(runnable, 10000)
        }

        handler.post(runnable)
        return START_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable) // Oprim mesageria când serviciul e distrus
        super.onDestroy()
        Log.w("PracticalTest01Service", "Service stopped")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
