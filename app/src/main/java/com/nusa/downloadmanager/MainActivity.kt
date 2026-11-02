package com.nusa.downloadmanager

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*

class MainActivity : Activity() {
    private lateinit var urlInput: EditText
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUi()
        if (intent?.action == android.content.Intent.ACTION_SEND) {
            val shared = intent.getStringExtra(android.content.Intent.EXTRA_TEXT)
            if (!shared.isNullOrBlank()) urlInput.setText(shared.trim())
        }
    }

    private fun buildUi() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 32, 24, 24)
            gravity = Gravity.TOP
        }
        layout.addView(TextView(this).apply {
            text = "Nusa Download Manager"
            textSize = 24f
        })
        layout.addView(TextView(this).apply {
            text = "Tempel URL video atau file yang memang diizinkan untuk diunduh."
            textSize = 14f
            setPadding(0, 12, 0, 18)
        })
        urlInput = EditText(this).apply {
            hint = "https://contoh.com/video.mp4"
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_URI
            minLines = 2
        }
        layout.addView(urlInput, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val detect = Button(this).apply {
            text = "Periksa jenis stream"
            setOnClickListener { inspectUrl() }
        }
        layout.addView(detect)

        val download = Button(this).apply {
            text = "Download file langsung"
            setOnClickListener { downloadDirect() }
        }
        layout.addView(download)

        status = TextView(this).apply {
            text = "Status: siap"
            textSize = 14f
            setPadding(0, 18, 0, 0)
        }
        layout.addView(status)
        layout.addView(TextView(this).apply {
            text = "\nVersi awal 0.1.0\nHTTP/HTTPS file langsung: tersedia.\nHLS/DASH: deteksi URL dasar; mesin unduh stream akan ditambahkan pada tahap berikutnya."
            textSize = 13f
        })
        setContentView(ScrollView(this).apply { addView(layout) })
    }

    private fun normalizedUrl(): String? {
        val raw = urlInput.text.toString().trim().substringBefore("\n")
        if (raw.isBlank()) {
            status.text = "Masukkan URL terlebih dahulu."
            return null
        }
        val uri = runCatching { Uri.parse(raw) }.getOrNull()
        if (uri == null || (uri.scheme != "http" && uri.scheme != "https") || uri.host.isNullOrBlank()) {
            status.text = "URL tidak valid. Gunakan alamat http:// atau https://."
            return null
        }
        return raw
    }

    private fun inspectUrl() {
        val url = normalizedUrl() ?: return
        val lower = url.lowercase()
        val type = when {
            ".m3u8" in lower -> "HLS (.m3u8)"
            ".mpd" in lower -> "MPEG-DASH (.mpd)"
            ".mp4" in lower -> "MP4"
            ".mp3" in lower || ".m4a" in lower -> "Audio"
            else -> "URL web/file (format belum diketahui)"
        }
        status.text = "Terdeteksi: $type\nCatatan: URL manifest HLS/DASH belum bisa diunduh oleh tombol download langsung."
    }

    private fun downloadDirect() {
        val url = normalizedUrl() ?: return
        val lower = url.lowercase()
        if (".m3u8" in lower || ".mpd" in lower) {
            status.text = "Ini manifest HLS/DASH. Gunakan mesin stream pada tahap berikutnya; DownloadManager Android tidak menggabungkan segmennya."
            return
        }
        try {
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle("Nusa Download")
                .setDescription("Mengunduh file...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(false)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName(url))
            val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager.enqueue(request)
            status.text = "Download dimulai. Periksa notifikasi atau folder Download."
        } catch (e: Exception) {
            status.text = "Gagal memulai download: ${e.message}"
        }
    }

    private fun fileName(url: String): String {
        val last = Uri.parse(url).lastPathSegment?.takeIf { it.isNotBlank() } ?: "nusa-download.bin"
        return last.substringAfterLast('/').take(100)
    }
}
