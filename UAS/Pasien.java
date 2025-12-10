// Kelas Pasien
class Pasien {
    String idPasien;
    String nama;
    String alamat;
    String tglLahir;

    public Pasien(String idPasien, String nama, String alamat, String tglLahir) {
        this.idPasien = idPasien;
        this.nama = nama;
        this.alamat = alamat;
        this.tglLahir = tglLahir;
    }
    // Getter methods (dapat ditambahkan untuk kepraktisan)
    // ...
}

// Kelas Poli dan Dokter (untuk data master)
class Poli {
    String namaPoli;
    String namaDokter;

    public Poli(String namaPoli, String namaDokter) {
        this.namaPoli = namaPoli;
        this.namaDokter = namaDokter;
    }

    @Override
    public String toString() {
        return namaPoli + " (Dr. " + namaDokter + ")";
    }
}

// Kelas Antrian (untuk menyimpan status antrian)
class Antrian {
    int nomorAntrian;
    Pasien pasien;
    Poli poliTujuan;
    long timestamp; // Waktu pendaftaran

    public Antrian(int nomorAntrian, Pasien pasien, Poli poliTujuan) {
        this.nomorAntrian = nomorAntrian;
        this.pasien = pasien;
        this.poliTujuan = poliTujuan;
        this.timestamp = System.currentTimeMillis();
    }
    // Getter methods (dapat ditambahkan)
    // ...
}