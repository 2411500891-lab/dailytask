# DailyTask+

## Deskripsi Aplikasi

DailyTask+ merupakan aplikasi Android yang dikembangkan untuk membantu mahasiswa dalam mengelola tugas akademik, deadline, dan aktivitas sehari-hari secara lebih terstruktur.

Aplikasi ini menyediakan fitur pencatatan tugas, pengelompokan kategori, penentuan prioritas, serta monitoring tugas yang belum selesai. Data disimpan pada database MySQL dan diakses melalui REST API berbasis PHP menggunakan Retrofit.

### Tujuan Aplikasi

* Membantu mahasiswa mengelola tugas secara terstruktur.
* Mengurangi risiko lupa deadline tugas.
* Meningkatkan produktivitas belajar.
* Mempermudah pencatatan dan pemantauan tugas harian.

---

# Fitur Utama

## Authentication

* Registrasi akun pengguna
* Login akun
* Session login menggunakan SharedPreferences
* Logout akun

## Task Management

* Menambah tugas baru
* Melihat daftar tugas
* Mengubah data tugas
* Menghapus tugas

## Kategori Tugas

* Kuliah
* Tugas
* Praktikum
* Organisasi
* Pribadi
* Umum

## Prioritas Tugas

* Low
* Medium
* High

## Deadline Management

* Pemilihan tanggal menggunakan DatePickerDialog
* Pemilihan waktu menggunakan TimePickerDialog

## Status Tugas

* Belum selesai
* Selesai

## Monitoring Tugas

* Menampilkan jumlah tugas yang belum selesai pada halaman utama

---

# Activity

## LoginActivity

Fungsi:

* Halaman login pengguna
* Validasi email dan password
* Menyimpan session login menggunakan SharedPreferences
* Mengarahkan pengguna ke MainActivity

## RegisterActivity

Fungsi:

* Registrasi pengguna baru
* Validasi nama, email, dan password
* Mengirim data ke REST API

## MainActivity

Fungsi:

* Menampilkan daftar tugas menggunakan RecyclerView
* Menampilkan jumlah tugas aktif
* Menambahkan tugas baru
* Mengubah status tugas
* Menghapus tugas
* Logout akun

## AddEditTaskActivity

Fungsi:

* Menambah tugas baru
* Mengubah data tugas
* Memilih kategori
* Memilih prioritas
* Memilih tanggal dan waktu deadline

---

# Intent yang Digunakan

## LoginActivity → RegisterActivity

Digunakan saat pengguna memilih menu registrasi.

```java
startActivity(new Intent(this, RegisterActivity.class));
```

## LoginActivity → MainActivity

Digunakan setelah login berhasil.

```java
startActivity(new Intent(this, MainActivity.class));
```

## MainActivity → AddEditTaskActivity

Digunakan untuk menambah tugas baru.

```java
startActivity(new Intent(this, AddEditTaskActivity.class));
```

## MainActivity → LoginActivity

Digunakan saat proses logout.

```java
startActivity(new Intent(this, LoginActivity.class));
```

## Explicit Intent dengan Extra Data

Digunakan saat mengedit tugas.

Data yang dikirim:

* id
* title
* description
* category
* priority
* due_date
* due_time
* is_done

---

# Widget Android yang Digunakan

| Widget                       | Fungsi                                         |
| ---------------------------- | ---------------------------------------------- |
| EditText                     | Input data login, registrasi, dan tugas        |
| Button                       | Menjalankan aksi aplikasi                      |
| TextView                     | Menampilkan informasi                          |
| RecyclerView                 | Menampilkan daftar tugas                       |
| Spinner                      | Memilih kategori dan prioritas                 |
| CheckBox                     | Menandai status tugas                          |
| FloatingActionButton         | Menambah tugas                                 |
| ExtendedFloatingActionButton | Menambah tugas dengan tampilan Material Design |
| DatePickerDialog             | Memilih tanggal deadline                       |
| TimePickerDialog             | Memilih waktu deadline                         |
| AlertDialog                  | Konfirmasi penghapusan data                    |
| Toast                        | Menampilkan notifikasi                         |

---

# Library yang Digunakan

## Retrofit 2

```gradle
implementation("com.squareup.retrofit2:retrofit:2.9.0")
```

Fungsi:

* Menghubungkan aplikasi Android dengan REST API PHP.

Alasan Penggunaan:

* Mempermudah komunikasi HTTP antara aplikasi dan server.

## Gson Converter

```gradle
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
```

Fungsi:

* Mengubah data JSON menjadi object Java dan sebaliknya.

Alasan Penggunaan:

* Mempermudah proses parsing data dari API.

## RecyclerView

```gradle
implementation("androidx.recyclerview:recyclerview:1.3.2")
```

Fungsi:

* Menampilkan daftar tugas secara efisien.

Alasan Penggunaan:

* Lebih optimal dibandingkan ListView.

## CircleImageView

```gradle
implementation("de.hdodenhof:circleimageview:3.1.0")
```

Fungsi:

* Menampilkan gambar profil berbentuk lingkaran.

Alasan Penggunaan:

* Membuat tampilan aplikasi lebih modern.

## Material Design Components

```gradle
implementation(libs.material)
```

Fungsi:

* Menyediakan komponen antarmuka modern Android.

Alasan Penggunaan:

* Mengikuti standar desain Material Design.

---

# REST API

Aplikasi menggunakan backend PHP dengan endpoint:

```text
http://YOUR_IP_ADDRESS:8080/dailytask_api/
```

Service yang digunakan:

* Login
* Register
* Get Tasks
* Add Task
* Update Task
* Delete Task

---

# Database

Database menggunakan MySQL.

## Tabel Users

| Field    | Tipe Data |
| -------- | --------- |
| id       | INT (PK)  |
| name     | VARCHAR   |
| email    | VARCHAR   |
| password | VARCHAR   |

## Tabel Tasks

| Field       | Tipe Data |
| ----------- | --------- |
| id          | INT (PK)  |
| user_id     | INT (FK)  |
| title       | VARCHAR   |
| description | TEXT      |
| category    | VARCHAR   |
| priority    | VARCHAR   |
| due_date    | DATE      |
| due_time    | TIME      |
| is_done     | TINYINT   |

---

# Entity Relationship Diagram (ERD)

```text
+----------------+
|     USERS      |
+----------------+
| PK id          |
| name           |
| email          |
| password       |
+----------------+
         |
         | 1
         |
         | N
         |
+----------------------+
|       TASKS          |
+----------------------+
| PK id                |
| FK user_id           |
| title                |
| description          |
| category             |
| priority             |
| due_date             |
| due_time             |
| is_done              |
+----------------------+
```

Keterangan:

* Satu user dapat memiliki banyak tugas (One-to-Many).
* Setiap tugas hanya dimiliki oleh satu user.

---

# Teknologi yang Digunakan

## Frontend

* Android Studio
* Java
* XML

## Backend

* PHP REST API

## Database

* MySQL

## Networking

* Retrofit
* Gson

## Session Management

* SharedPreferences

---

# Struktur Project

```text
com.example.dailytask
│
├── LoginActivity.java
├── RegisterActivity.java
├── MainActivity.java
├── ApiClient.java
├── ApiResponse.java
├── TaskAdapter.java
├── User.java
│
└── com.example.dailytaskplus
    │
    ├── AddEditTaskActivity.java
    │
    ├── api
    │   └── ApiService.java
    │
    └── model
        └── Task.java
```

---

# Cara Menjalankan Project

1. Clone repository.

```bash
git clone https://github.com/username/dailytask.git
```

2. Buka project menggunakan Android Studio.

3. Jalankan database MySQL.

4. Jalankan backend PHP.

5. Ubah BASE_URL pada file ApiClient.java sesuai alamat server.

6. Jalankan aplikasi pada emulator atau perangkat Android.

---

# Pengembang

Project Mobile Programming

Program Studi Teknik Informatika

Universitas Budi Luhur
