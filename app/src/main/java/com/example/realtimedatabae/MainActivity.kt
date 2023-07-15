package com.example.realtimedatabae

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.realtimedatabae.databinding.ActivityMainBinding
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference//bu qaysinga murojat qile deb aytadigon yoli boladi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("my_switch")

        // bu ringtongni olib kelib beryapti shu muzika qoyib beryapti
        val thePlayer = MediaPlayer.create(
            applicationContext,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        )

        var ch = false
        binding.sswitchBoss.setOnClickListener {
            reference.setValue(binding.sswitchBoss.isChecked)
            ch = binding.sswitchBoss.isChecked
        }

        // reference.addListenerForSingleValueEvent() bir marotaba ishledi halos
        //reference.addValueEventListener() bu esa naryogada ozgarish bolishi bn ozgarada
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // snapshot realtime ni malumotllarini olib keladi
                //bu yerda malumot ozgarishi
                // snapshot.value diganimizda true false ni olib keladi
                //snapshot.key diganimizda uning reference = firebaseDatabase.getReference("my_switch") shu kodda my_switch
                // deganimiz uchun my_switch keladi key deganimizda
                val checked = snapshot.value as Boolean
                if (ch != checked) {
                    binding.sswitchBoss.isChecked = checked
                } else {
                    //bu yerda true bolsa muzik qoyilsin false bolsa muzika cholmasin
                    if (checked) {
                        thePlayer.start()
                    } else {
                        thePlayer.pause()
                    }
                    thePlayer.start()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //malumot qaytishi
            }
        })
    }
}