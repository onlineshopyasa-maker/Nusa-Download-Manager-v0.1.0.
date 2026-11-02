# Nusa Download Manager

Starter project Android untuk downloader pribadi.

## Build lewat GitHub Actions
1. Buat repository GitHub baru, misalnya `NusaDownloadManager`.
2. Upload seluruh isi folder ZIP ini ke root repository.
3. Buka tab **Actions** dan jalankan workflow **Android Build (Nusa Download Manager)**.
4. Setelah selesai, unduh APK dari bagian **Artifacts**.

## Status fitur 0.1.0
- Android minimum: Android 10 (API 29).
- Menerima URL melalui menu Share dari aplikasi lain.
- Deteksi sederhana ekstensi MP4, M3U8, MPD, dan audio.
- Download HTTP/HTTPS langsung memakai Android DownloadManager.
- HLS/DASH baru dideteksi, belum diunduh/digabung.

## Tahap berikutnya
- Browser internal dan deteksi URL stream dari halaman yang dibuka.
- HLS/DASH downloader berbasis Media3/FFmpeg untuk stream non-DRM.
- Pilihan kualitas, antrian, pause/resume sesuai dukungan server, dan pengelola file.
- Signed release APK.

Gunakan hanya untuk konten yang kamu miliki atau yang memang diizinkan untuk diunduh. DRM tidak didukung dan tidak akan dilewati.
