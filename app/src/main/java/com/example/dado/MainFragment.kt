package com.example.dado

import android.hardware.SensorEventListener
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import kotlin.math.sqrt
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient

class MainFragment : Fragment(), SensorEventListener {
    private var isSoundOn = true
    private lateinit var diceRollsAdapter: DiceRollsAdapter
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var currentAcceleration: Float = 0f
    private var lastAcceleration: Float = 0f
    private var isAnimating = false
    private var menu: Menu? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object{
        val diceRolls = mutableListOf<DiceRoll>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        lastAcceleration = SensorManager.GRAVITY_EARTH
        currentAcceleration = SensorManager.GRAVITY_EARTH

        fusedLocationClient = FusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        diceRollsAdapter = DiceRollsAdapter(diceRolls)

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupShowResultsButton()
        setupSoundButton()
        setupRollButton()
    }

    private fun setupSoundButton() {
        val fabSound: FloatingActionButton? = view?.findViewById(R.id.fabSound)
        fabSound?.setOnClickListener{
            isSoundOn = !isSoundOn
            if (isSoundOn){
                fabSound.setImageResource(R.drawable.ic_sonido_on)
                Toast.makeText(context, "Sonido activado", Toast.LENGTH_SHORT).show()
            }else{
                fabSound.setImageResource(R.drawable.ic_sonido_off)
                Toast.makeText(context, "Sonido desactivado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupShowResultsButton() {
        val fabShowResults: FloatingActionButton? = view?.findViewById(R.id.fabShowResults)
        fabShowResults?.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_diceRollsFragment)
        }
    }


    private fun setupRollButton() {
        val btnLanzar: Button? = view?.findViewById(R.id.btnLanzar)
        btnLanzar?.setOnClickListener{

            if(!isAnimating)
            {
                tiempo()

                if (isSoundOn){
                    val mp: MediaPlayer = MediaPlayer.create(context, R.raw.sonido)
                    mp.start()
                }
            }
        }
    }

    private fun animateDice(){
        val numero: Int = lanzar(6)
        val imgDado: ImageView? = view?.findViewById(R.id.imgDado)
        imgDado?.setImageResource(
            when(numero){
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                else -> R.drawable.dice_6
            }
        )
    }

    private fun rollDice(){
        val numero: Int = lanzar(6)
        diceRolls.add(DiceRoll(numero))
        diceRollsAdapter?.notifyDataSetChanged()

        val imgDado: ImageView? = view?.findViewById(R.id.imgDado)
        imgDado?.setImageResource(
            when(numero){
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                else -> R.drawable.dice_6
            }
        )
    }

    private fun lanzar(max: Int): Int{
        return (1..max).random()
    }

    private fun tiempo(){
        if(!isAnimating)
        {
            isAnimating = true
            if(isSoundOn){
                val mp: MediaPlayer = MediaPlayer.create(context, R.raw.sonido)
                mp.start()
            }
            object: CountDownTimer(4000, 100){
                override fun onTick(millisUntilFinished: Long) {
                    animateDice()
                }

                override fun onFinish() {
                    rollDice()
                    isAnimating = false
                }
            }.start()
        }

    }

    override fun onResume() {
        super.onResume()
        requireActivity().invalidateOptionsMenu()
        accelerometer?.also { accelerometer ->
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        requireActivity().invalidateOptionsMenu()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        lastAcceleration = currentAcceleration
        currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val delta = currentAcceleration - lastAcceleration

        if (delta > 15 && !isAnimating) {
            tiempo()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }
}