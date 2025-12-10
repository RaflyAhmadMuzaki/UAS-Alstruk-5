import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class AntrianPasien {
    // Data Master dan Transaksi
    private static HashMap<String, Pasien> dataPasien = new HashMap<>(); // ID Pasien -> Pasien
    private static HashMap<String, Poli> dataPoli = new HashMap<>();     // Nama Poli -> Poli
    private static Queue<Antrian> antrianSaatIni = new LinkedList<>();    // Antrian aktif
    private static List<Antrian> riwayatKunjungan = new ArrayList<>();   // Riwayat yang sudah dipanggil

    private static int counterAntrian = 1;

    public static void main(String[] args) {
        // Inisialisasi data awal (contoh)
        inisialisasiData();

        Scanner scanner = new Scanner(System.in);
        int pilihan;

        do {
            tampilkanMenu();
            System.out.print("Masukkan pilihan Anda (1-6): ");
            try {
                pilihan = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                pilihan = 0; // Pilihan tidak valid
            }

            switch (pilihan) {
                case 1:
                    pendaftaranPasien(scanner);
                    break;
                case 2:
                    tampilkanDaftarAntrian();
                    break;
                case 3:
                    panggilAntrian();
                    break;
                case 4:
                    riwayatKunjunganPasien(scanner);
                    break;
                case 5:
                    kelolaData(scanner);
                    break;
                case 6:
                    System.out.println("Terima kasih, program diakhiri.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
            System.out.println("------------------------------------------");

        } while (pilihan != 6);

        scanner.close();
    }

    // --- Fungsi Bantuan ---

    private static void inisialisasiData() {
        // Poli Awal
        dataPoli.put("Umum", new Poli("Umum", "Budi Santoso"));
        dataPoli.put("Gigi", new Poli("Gigi", "Citra Dewi"));
        dataPoli.put("Anak", new Poli("Anak", "Dani Kurniawan"));
    }

    private static void tampilkanMenu() {
        System.out.println("========== SISTEM ANTRIAN PUSKESMAS ==========");
        System.out.println("1. Pendaftaran Pasien (Ambil Antrian)");
        System.out.println("2. Daftar Antrian Saat Ini");
        System.out.println("3. Panggil Antrian Selanjutnya");
        System.out.println("4. Riwayat Kunjungan Pasien");
        System.out.println("5. Kelola Data (Poli & Dokter)");
        System.out.println("6. Keluar");
        System.out.println("===========================================");
    }

    // --- Implementasi Menu ---

// 1. Pendaftaran Pasien (Ambil Antrian) - HANYA UNTUK PASIEN BARU
    private static void pendaftaranPasien(Scanner scanner) {
        System.out.println("\n--- Pendaftaran Pasien Baru ---");

        // Menghasilkan ID Pasien Baru
        String newId = "P" + String.format("%03d", dataPasien.size() + 1);

        System.out.print("Masukkan Nama Pasien: ");
        String nama = scanner.nextLine();
        System.out.print("Masukkan Alamat Pasien: ");
        String alamat = scanner.nextLine();
        System.out.print("Masukkan Tanggal Lahir (dd/mm/yyyy): ");
        String tglLahir = scanner.nextLine();

        // Membuat objek Pasien Baru dan menyimpannya
        Pasien pasien = new Pasien(newId, nama, alamat, tglLahir);
        dataPasien.put(newId, pasien);
        
        System.out.println(">> Pasien berhasil didaftarkan dengan ID: " + newId);

        // 2. Pilih Poli Tujuan
        System.out.println("\nPilih Poli Tujuan:");
        List<String> listPoli = new ArrayList<>(dataPoli.keySet());
        for (int i = 0; i < listPoli.size(); i++) {
            Poli poli = dataPoli.get(listPoli.get(i));
            System.out.println((i + 1) + ". " + poli.namaPoli + " (Dr. " + poli.namaDokter + ")");
        }

        System.out.print("Masukkan nomor poli: ");
        try {
            int poliPilihan = Integer.parseInt(scanner.nextLine());
            if (poliPilihan > 0 && poliPilihan <= listPoli.size()) {
                Poli poli = dataPoli.get(listPoli.get(poliPilihan - 1));

                // 3. Tambahkan ke antrian
                Antrian antrianBaru = new Antrian(counterAntrian++, pasien, poli);
                antrianSaatIni.add(antrianBaru);

                System.out.println("\n--- PENDAFTARAN BERHASIL ---");
                System.out.println("No. Antrian Anda: **" + antrianBaru.nomorAntrian + "**");
                System.out.println("Pasien: " + pasien.nama + " | ID: " + newId);
                System.out.println("Tujuan: " + poli.toString());
            } else {
                System.out.println("!! Nomor poli tidak valid. Pendaftaran dibatalkan.");
            }
        } catch (NumberFormatException e) {
            System.out.println("!! Input harus berupa angka. Pendaftaran dibatalkan.");
        }
    }

    // 2. Daftar Antrian Saat Ini
    private static void tampilkanDaftarAntrian() {
        System.out.println("\n--- Daftar Antrian Saat Ini ---");
        if (antrianSaatIni.isEmpty()) {
            System.out.println(">> Tidak ada antrian saat ini.");
            return;
        }

        for (Antrian a : antrianSaatIni) {
            System.out.println("No. Antrian: " + a.nomorAntrian +
                               ", Pasien: " + a.pasien.nama +
                               ", Poli: " + a.poliTujuan.namaPoli);
        }
    }

    // 3. Panggil Antrian
    private static void panggilAntrian() {
        System.out.println("\n--- Panggil Antrian Selanjutnya ---");
        if (antrianSaatIni.isEmpty()) {
            System.out.println(">> Tidak ada antrian yang harus dipanggil.");
            return;
        }

        Antrian antrianDipanggil = antrianSaatIni.poll(); // Ambil dan hapus dari antrian
        riwayatKunjungan.add(antrianDipanggil);          // Tambahkan ke riwayat

        System.out.println(">> MEMANGGIL ANTRIAN: **" + antrianDipanggil.nomorAntrian + "**");
        System.out.println("Nama Pasien: " + antrianDipanggil.pasien.nama);
        System.out.println("Poli Tujuan: " + antrianDipanggil.poliTujuan.namaPoli +
                           " (Dr. " + antrianDipanggil.poliTujuan.namaDokter + ")");

        if (!antrianSaatIni.isEmpty()) {
            System.out.println("\nAntrian Selanjutnya: " + antrianSaatIni.peek().nomorAntrian +
                               " (" + antrianSaatIni.peek().pasien.nama + ")");
        }
    }

    // 4. Riwayat Kunjungan Pasien
    private static void riwayatKunjunganPasien(Scanner scanner) {
        System.out.println("\n--- Riwayat Kunjungan Pasien ---");
        System.out.print("Masukkan ID Pasien yang ingin dicari (misal: P001): ");
        String idCari = scanner.nextLine().toUpperCase();

        if (!dataPasien.containsKey(idCari)) {
            System.out.println("!! ID Pasien tidak ditemukan.");
            return;
        }

        Pasien pasien = dataPasien.get(idCari);
        System.out.println("\nRiwayat Kunjungan untuk: " + pasien.nama + " (ID: " + pasien.idPasien + ")");

        boolean found = false;
        for (Antrian a : riwayatKunjungan) {
            if (a.pasien.idPasien.equals(idCari)) {
                System.out.println("- Poli: " + a.poliTujuan.namaPoli +
                                   ", Tanggal Kunjungan: " + new java.util.Date(a.timestamp));
                found = true;
            }
        }

        if (!found) {
            System.out.println(">> Belum ada riwayat kunjungan yang tercatat untuk pasien ini.");
        }
    }

    // 5. Kelola Data (Poli & Dokter)
    private static void kelolaData(Scanner scanner) {
        System.out.println("\n--- Kelola Data Poli & Dokter ---");
        System.out.println("Poli yang ada saat ini:");
        List<String> listPoli = new ArrayList<>(dataPoli.keySet());
        for (int i = 0; i < listPoli.size(); i++) {
            Poli poli = dataPoli.get(listPoli.get(i));
            System.out.println((i + 1) + ". " + poli.namaPoli + " (Dr. " + poli.namaDokter + ")");
        }

        System.out.print("\nMasukkan nomor poli yang ingin diubah/dihapus, atau ketik 'BARU' untuk tambah: ");
        String input = scanner.nextLine().toUpperCase();

        if (input.equals("BARU")) {
            System.out.print("Nama Poli Baru: ");
            String namaBaru = scanner.nextLine();
            System.out.print("Nama Dokter yang bertugas: ");
            String dokterBaru = scanner.nextLine();
            dataPoli.put(namaBaru, new Poli(namaBaru, dokterBaru));
            System.out.println(">> Poli " + namaBaru + " berhasil ditambahkan.");
            return;
        }

        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx >= 0 && idx < listPoli.size()) {
                String namaPoliLama = listPoli.get(idx);
                Poli poliLama = dataPoli.get(namaPoliLama);

                System.out.println("\nAnda akan mengubah data untuk: " + poliLama);
                System.out.println("1. Ubah Nama Dokter");
                System.out.println("2. Hapus Poli");
                System.out.print("Pilih opsi (1/2): ");
                String opsi = scanner.nextLine();

                if (opsi.equals("1")) {
                    System.out.print("Masukkan Nama Dokter Baru: ");
                    String dokterBaru = scanner.nextLine();
                    poliLama.namaDokter = dokterBaru; // Ubah nama dokter di objek yang sama
                    System.out.println(">> Data Poli " + namaPoliLama + " berhasil diperbarui. Dokter: " + dokterBaru);
                } else if (opsi.equals("2")) {
                    dataPoli.remove(namaPoliLama);
                    System.out.println("!! Poli " + namaPoliLama + " berhasil dihapus.");
                } else {
                    System.out.println("!! Opsi tidak valid.");
                }

            } else {
                System.out.println("!! Nomor poli tidak valid.");
            }
        } catch (NumberFormatException e) {
            System.out.println("!! Input tidak valid.");
        }
    }
}