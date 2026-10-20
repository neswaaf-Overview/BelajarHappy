package com.example.belajar

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import java.util.Locale

data class Item(val simbol: String, val kata: String, val emoji: String)

class MainActivity : AppCompatActivity() {

    private val huruf = listOf(
        Item("A", "Apel", "🍎"), Item("B", "Bola", "⚽"),
        Item("C", "Cicak", "🦎"), Item("D", "Domba", "🐑"),
        Item("E", "Elang", "🦅"), Item("F", "Foto", "📷"),
        Item("G", "Gajah", "🐘"), Item("H", "Harimau", "🐯"),
        Item("I", "Ikan", "🐟"), Item("J", "Jeruk", "🍊"),
        Item("K", "Kucing", "🐱"), Item("L", "Lebah", "🐝"),
        Item("M", "Mobil", "🚗"), Item("N", "Nanas", "🍍"),
        Item("O", "Ombak", "🌊"), Item("P", "Pisang", "🍌"),
        Item("Q", "Quiz", "❓"), Item("R", "Rusa", "🦌"),
        Item("S", "Sapi", "🐮"), Item("T", "Tomat", "🍅"),
        Item("U", "Ular", "🐍"), Item("V", "Vespa", "🛵"),
        Item("W", "Wortel", "🥕"), Item("X", "Xilofon", "🎹"),
        Item("Y", "Yoyo", "🪀"), Item("Z", "Zebra", "🦓")
    )

    private val namaAngka = listOf(
        "Nol", "Satu", "Dua", "Tiga", "Empat",
        "Lima", "Enam", "Tujuh", "Delapan", "Sembilan"
    )

    private val angka = namaAngka.mapIndexed { i, nama ->
        Item(i.toString(), nama, if (i == 0) "🚫" else "⭐".repeat(i))
    }

    private val warna = listOf(
        "#E91E63", "#2196F3", "#4CAF50", "#FF9800",
        "#9C27B0", "#F44336", "#009688", "#3F51B5"
    )

    private var daftar = huruf
    private var indeks = 0
    private var tts: TextToSpeech? = null

    private lateinit var tvIsi: TextView
    private lateinit var tvEmoji: TextView
    private lateinit var tvKata: TextView
    private lateinit var btnHuruf: MaterialButton
    private lateinit var btnAngka: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvIsi = findViewById(R.id.tvIsi)
        tvEmoji = findViewById(R.id.tvEmoji)
        tvKata = findViewById(R.id.tvKata)
        btnHuruf = findViewById(R.id.btnHuruf)
        btnAngka = findViewById(R.id.btnAngka)
        val btnPrev = findViewById<MaterialButton>(R.id.btnPrev)
        val btnNext = findViewById<MaterialButton>(R.id.btnNext)
        val btnSuara = findViewById<MaterialButton>(R.id.btnSuara)

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale("id", "ID")
            }
        }

        btnHuruf.setOnClickListener {
            daftar = huruf
            indeks = 0
            tampilkan()
        }

        btnAngka.setOnClickListener {
            daftar = angka
            indeks = 0
            tampilkan()
        }

        btnNext.setOnClickListener {
            indeks = (indeks + 1) % daftar.size
            tampilkan()
        }

        btnPrev.setOnClickListener {
            indeks = (indeks - 1 + daftar.size) % daftar.size
            tampilkan()
        }

        btnSuara.setOnClickListener {
            val item = daftar[indeks]
            val ucapan = if (daftar === huruf) {
                "${item.simbol}. ${item.kata}"
            } else {
                item.kata
            }
            tts?.speak(ucapan, TextToSpeech.QUEUE_FLUSH, null, "ucap")
        }

        tampilkan()
    }

    private fun tampilkan() {
        val item = daftar[indeks]
        tvIsi.text = item.simbol
        tvIsi.setTextColor(Color.parseColor(warna[indeks % warna.size]))
        tvEmoji.text = item.emoji
        tvKata.text = item.kata

        val aktif = ColorStateList.valueOf(Color.parseColor("#E91E63"))
        val mati = ColorStateList.valueOf(Color.parseColor("#BDBDBD"))
        btnHuruf.backgroundTintList = if (daftar === huruf) aktif else mati
        btnAngka.backgroundTintList = if (daftar === angka) aktif else mati
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}