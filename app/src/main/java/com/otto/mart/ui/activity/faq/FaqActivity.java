package com.otto.mart.ui.activity.faq;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.otto.mart.R;
import com.otto.mart.model.localmodel.ui.Faqmodel;
import com.otto.mart.ui.Partials.adapter.FaqAdapter;

import java.util.ArrayList;
import java.util.List;

import app.beelabs.com.codebase.base.BaseActivity;

public class FaqActivity extends BaseActivity implements IFaq {

    private Fragment toolbarFragment;
    //    private WebView wv;
    List<Faqmodel> faqmodel;

    private RecyclerView rv;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        initComponent();
        initContent();
        initFragment();
    }


    private void initComponent() {
        rv = findViewById(R.id.faq_rv);
    }

    private void initContent() {

        loadPlaceholderData();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv.setLayoutManager(linearLayoutManager);
        FaqAdapter adapter = new FaqAdapter(faqmodel, R.layout.item_faq);
        rv.setAdapter(adapter);

//        wv.loadUrl("https://ottopay.id/helps?id=1#faq");
    }

    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(R.anim.fast_slide_in_left, R.anim.fast_slide_out_right)
                .add(R.id.toolbar_container, toolbarFragment).commit();
    }

    @Override
    public void finishme() {
        finish();
    }

    private void loadPlaceholderData() {
        faqmodel = new ArrayList<>();

        Faqmodel q1 = new Faqmodel();
        q1.setQuestion("Apa itu OttoPay?");
        q1.setAnswer("OttoPay adalah aplikasi yang menerima pembayaran via kode QR dari berbagai e-money yang sudah bekerjasama dengan OttoPay. \n\n");
        faqmodel.add(q1);

        Faqmodel q2 = new Faqmodel();
        q2.setQuestion("Bagaimana cara saya mengunduh aplikasi OttoPay?");
        q2.setAnswer("Aplikasi OttoPay dapat digunakan untuk handphone Android Kitkat 4.4. ke atas.  \n\n");
        faqmodel.add(q2);

        Faqmodel q3 = new Faqmodel();
        q3.setQuestion("Apa itu Kode QR OttoPay?");
        q3.setAnswer("Toko yang sudah bergabung bersama OttoPay akan mendapatkan satu kode QR sebagai identitas toko sekaligus alat penerima pembayaran dari pembeli. Toko akan mendapat stiker QR OttoPay atau hanya perlu menunjukkan kode QR di HP kepada pembeli untuk di-scan.  Jadi, pembeli hanya perlu scan kode QR toko ketika membayar dan uangnya otomatis akan masuk ke Omzet toko.  \n\n");
        faqmodel.add(q3);

        Faqmodel q4 = new Faqmodel();
        q4.setQuestion("Apa itu Omzet?");
        q4.setAnswer("Omzet adalah saldo uang elektronik hasil penjualan barang dagangan untuk semua jenis transaksi yang diterima. Omzet harus ditransfer/ dipindahkan ke Dompet terlebih dahulu untuk bisa digunakan dalam bertransaksi.  \n\n");
        faqmodel.add(q4);

        Faqmodel q5 = new Faqmodel();
        q5.setQuestion("Apa itu Dompet?");
        q5.setAnswer("Dompet adalah tempat saldo uang elektronik yang didaftarkan untuk bertransaksi. Saldo uang elektronik yang ada di Dompet bisa langsung dipergunakan untuk bertransaksi. / dipindahkan ke Dompet terlebih dahulu untuk bisa digunakan dalam bertransaksi.  \n\n");
        faqmodel.add(q5);

        Faqmodel q6 = new Faqmodel();
        q6.setQuestion("Apa itu Mitra OttoPay?");
        q6.setAnswer("Mitra OttoPay adalah toko yang telah mengunduh dan mendaftarkan diri di Aplikasi OttoPay. ");
        faqmodel.add(q6);

        Faqmodel q7 = new Faqmodel();
        q7.setQuestion("Apa yang saya dapatkan dengan bergabung sebagai Mitra OttoPay?");
        q7.setAnswer("Kamu akan mendapat banyak keuntungan dengan gabung sebagai Mitra OttoPay, antara lain:\n" +
                "\t•\tTidak perlu repot menerima uang tunai atau menyiapkan uang kembalian karena semua sudah masuk ke Aplikasi OttoPay kamu\n" +
                "\t•\tMembantu monitor penerimaan karena OttoPay mencatat semua transaksi kamu yang dilakukan lewat OttoPay\n" +
                "\t•\tBisa menggaet lebih banyak pembeli karena sudah bisa menerima pembayaran elektronik dari berbagai bank\n" +
                "\t•\tBisa meningkatkan pendapatan kamu lewat jualan tagihan (pulsa, paket data, listrik, tagihan air, asuransi, dan pajak) ");
        faqmodel.add(q7);

        Faqmodel q8 = new Faqmodel();
        q8.setQuestion("Aplikasi pembayaran apa saja yang bisa diterima di Mitra OttoPay?");
        q8.setAnswer("Saat ini, OttoPay sudah bisa menerima pembayaran dari aplikasi e-money pede, JakOne, dan BNI Yap!. Ke depannya, OttoPay akan bekerjasama dengan lebih banyak lagi aplikasi untuk memudahkan kamu menerima semakin banyak sumber pembayaran!");
        faqmodel.add(q8);

        Faqmodel q9 = new Faqmodel();
        q9.setQuestion("Transaksi apa saja yang bisa dilakukan lewat OttoPay?");
        q9.setAnswer("OttoPay bisa digunakan untuk menerima transaksi QR dan jual-beli tagihan (pulsa, paket data, listrik, tagihan air, asuransi, dan pajak)");
        faqmodel.add(q9);

        Faqmodel q10 = new Faqmodel();
        q10.setQuestion("Bagaimana cara menerima pembayaran lewat OttoPay?");
        q10.setAnswer("Kamu bisa menerima pembayaran lewat OttoPay dengan cara:\n" +
                "\t•\tTunjukkan kode QR kamu ke pembeli:\n" +
                "\t•\tKlik \"Terima Pembayaran QR\"\n" +
                "\t•\tTunjukkan kode QR yang tertera di layar Aplikasi OttoPay kamu, atau masukkan jumlah pembayaran dan klik \"Perbarui Kode QR\"\n" +
                "\t•\tTunjukkan kode QR untuk di-scan oleh pembeli\n" +
                "\t•\tMinta pembeli untuk mengirimkan stiker QR yang ditempel di etalase toko kamu dan masukkan nominal pembelian.\n" +
                "\n" +
                "\t•\tBagaimana cara bertransaksi tagihan dengan OttoPay?\n" +
                "\t•\tPilih menu tagihan yang ingin dibayar\n" +
                "\t•\tMasukkan identitas pelanggan (nomor HP/ nomor anggota) dan pilih denom yang ingin dibeli\n" +
                "\t•\tPilih metode pembayaran pembeli kamu (pembayaran tunai/ kode QR)\n" +
                "\t•\tTransaksi selesai\n" +
                "\n" +
                "\t•\tBagaimana cara bertransaksi toko online dengan OttoPay?\n" +
                "Saat ini fitur belum tersedia");
        faqmodel.add(q10);

        Faqmodel q11 = new Faqmodel();
        q11.setQuestion("Bagaimana cara saya transfer Omzet ke Dompet?");
        q11.setAnswer("\t•\tPilih menu \"Transfer Pendapatan\"\n" +
                "\t•\tPilih menu \"Transfer ke Dompet\" \n" +
                "\t•\tMasukkan nominal yang mau kamu transfer, lalu klik \"Kirim\"\n" +
                "\t•\tAplikasi OttoPay akan memunculkan detail transaksi. Klik \"Ya\" jika ingin melanjutkan\n" +
                "\t•\tMasukkan kode PIN kamu\n" +
                "\t•\tTransfer selesai");
        faqmodel.add(q11);

        Faqmodel q12 = new Faqmodel();
        q12.setQuestion("Bagaimana cara saya transfer Omzet ke Rekening Bank?");
        q12.setAnswer("\t•\tPilih menu \"Transfer Pendapatan\"\n" +
                "\t•\tPilih menu \"Transfer ke Rekening Bank\" \n" +
                "\t•\tMasukkan nominal yang mau kamu transfer, lalu klik \"Kirim\"\n" +
                "\t•\tAplikasi OttoPay akan memunculkan detail transaksi. Klik \"Ya\" jika ingin melanjutkan\n" +
                "\t•\tMasukkan kode PIN kamu\n" +
                "\t•\tTransfer selesai");
        faqmodel.add(q12);

        Faqmodel q13 = new Faqmodel();
        q13.setQuestion("Bagaimana cara saya transfer Saldo ke Rekening Bank?");
        q13.setAnswer("\t•\tPilih menu \"Dompet\" yang ada di kanan bawah layar Aplikasi OttoPay\n" +
                "\t•\tPilih rekening yang ingin kamu transfer, lalu klik \"Transfer Saldo\"\n" +
                "\t•\tPilih \"Transfer ke Rekening Bank\", lalu pilih rekening bank yang sudah terdaftar\n" +
                "\t•\tMasukkan nominal transfer, lalu klik \"Transfer\"\n" +
                "\t•\tAplikasi OttoPay akan memunculkan detail transaksi. Klik \"Ya\" jika ingin melanjutkan\n" +
                "\t•\tMasukkan kode PIN kamu\n" +
                "\t•\tTransfer selesai");
        faqmodel.add(q13);

        Faqmodel q14 = new Faqmodel();
        q14.setQuestion("Apa saya bisa transfer Saldo ke Rekening e-money lain?");
        q14.setAnswer("\t•\tPilih menu \"Dompet\" yang ada di kanan bawah layar Aplikasi OttoPay\n" +
                "\t•\tPilih rekening yang ingin kamu transfer, lalu klik \"Transfer Saldo\"\n" +
                "\t•\tPilih \"Transfer ke E-money\", lalu pilih jenis e-money tujuan\n" +
                "\t•\tMasukkan nomor HP tujuan dan nominal transfer, lalu klik \"Transfer\"\n" +
                "\t•\tAplikasi OttoPay akan memunculkan detail transaksi. Klik \"Ya\" jika ingin melanjutkan\n" +
                "\t•\tMasukkan kode PIN kamu\n" +
                "\t•\tTransfer selesai");
        faqmodel.add(q14);

        Faqmodel q15 = new Faqmodel();
        q15.setQuestion("Di mana saya bisa top up akun OttoPay saya?");
        q15.setAnswer("Kamu bisa top up akun OttoPay kamu lewat ATM Bersama dan mobile banking.");
        faqmodel.add(q15);

        Faqmodel q16 = new Faqmodel();
        q16.setQuestion("Bagaimana cara topup?");
        q16.setAnswer("Via Internet Banking/ Mobile Banking:\n" +
                "\t•\tBuka internet/ mobile banking yang telah tersedia di HP atau layar browser kamu\n" +
                "\t•\tPilih menu transfer antar bank/ bank lain\n" +
                "\t•\tPilih Bank tujuan transfer: BANK INA PERDANA (kode bank : 513)\n" +
                "\t•\tMasukkan nomor rekening berupa nomor virtual account kamu (99002XXXXXXXXXXXX)\n" +
                "\t•\tMasukkan nominal yang diinginkan\n" +
                "\t•\tTransaksi berhasil\n" +
                "Via ATM Bersama:\n" +
                "\t•\tMasukkan kartu ATM dan PIN kamu\n" +
                "\t•\tPilih menu Transfer, lalu pilih Bank Lain\n" +
                "\t•\tMasukkan kode BANK INA PERDANA (kode bank : 513)\n" +
                "\t•\tMasukkan nomor rekening berupa nomor virtual account kamu:  (99002 + nomor HP kamu (contoh: 99002XXXXXXXXXXXX)\n" +
                "\t•\tMasukkan nominal yang diinginkan\n" +
                "\t•\tTransaksi berhasil");
        faqmodel.add(q16);

        Faqmodel q17 = new Faqmodel();
        q17.setQuestion("Bagaimana cara tarik tunai?");
        q17.setAnswer("Saat ini kamu belum bisa melakukan tarik tunai. Tapi kamu sudah bisa mentransfer Saldo kamu ke Rekening Bank dan melakukan tarik tunai dari ATM bank kamu.");
        faqmodel.add(q17);

        Faqmodel q18 = new Faqmodel();
        q18.setQuestion("Bagaimana cara saya mendaftar OttoPay?");
        q18.setAnswer("\t•\tTim sales OttoPay akan mendatangi toko kamu\n" +
                "\t•\tKamu download OttoPay dan buka aplikasinya\n" +
                "\t•\tScan stiker QR yang diberikan oleh tim sales OttoPay, lalu masukkan nomor HP kamu\n" +
                "\t•\tMasukkan PIN dan jawab pertanyaan keamanan yang tertera di layar Aplikasi OttoPay\n" +
                "\t•\tMasukkan kode OTP yang sudah di-SMS ke HP kamu\n" +
                "\t•\tAkun kamu sudah berhasil dibuat! Silakan login.");
        faqmodel.add(q18);

        Faqmodel q19 = new Faqmodel();
        q19.setQuestion("Apakah saya bisa mendaftar sendiri tanpa bantuan sales?");
        q19.setAnswer("Tidak bisa");
        faqmodel.add(q19);

        Faqmodel q20 = new Faqmodel();
        q20.setQuestion("Bagaimana jika saya mau toko saya didatangi oleh Tim Sales OttoPay?");
        q20.setAnswer("Silakan WhatsApp kami di 0813-1542-2822 dan sampaikan informasi nama & alamat toko kamu. Kami akan segera memproses permintaan kamu. ");
        faqmodel.add(q20);

        Faqmodel q21 = new Faqmodel();
        q21.setQuestion("Bagaimana cara saya masuk ke akun OttoPay saya?");
        q21.setAnswer("Masukkan nomor HP yang kamu daftarkan atau scan stiker QR toko kamu, lalu masukkan PIN. ");
        faqmodel.add(q21);

        Faqmodel q22 = new Faqmodel();
        q22.setQuestion("Bagaimana jika saya ingin mengubah kode merchant saya?");
        q22.setAnswer("Kamu tidak bisa mengubah kode merchant kamu. ");
        faqmodel.add(q22);

        Faqmodel q23 = new Faqmodel();
        q23.setQuestion("Apakah saya bisa mempunyai lebih dari satu akun OttoPay?");
        q23.setAnswer("Tidak bisa. ");
        faqmodel.add(q23);

        Faqmodel q24 = new Faqmodel();
        q24.setQuestion("Apa saya bisa mengedit akun saya?");
        q24.setAnswer("Ya.");
        faqmodel.add(q24);

        Faqmodel q25 = new Faqmodel();
        q25.setQuestion("Bagaimana jika saya lupa PIN akun saya?");
        q25.setAnswer("\t•\tKlik “Lupa PIN”\n" +
                "\t•\tMasukkan nomor HP kamu\n" +
                "\t•\tMasukkan kode OTP yang di-SMS ke HP kamu\n" +
                "\t•\tJawab pertanyaan keamanan \n" +
                "\t•\tBuat kode PIN baru\n" +
                "\t•\tSilakan login dengan kode PIN baru kamu");
        faqmodel.add(q25);

        Faqmodel q26 = new Faqmodel();
        q26.setQuestion("Bagaimana jika HP saya hilang?");
        q26.setAnswer("Silakan hubungi call center kami di (021) 1500-749 atau WhatsApp kami di 0813-1542-2822. Tim kami akan segera memproses akun kamu.");
        faqmodel.add(q26);

        Faqmodel q27 = new Faqmodel();
        q27.setQuestion("Ke manakah saya bisa menghubungi jika saya ada keluhan dan pertanyaan lainnya");
        q27.setAnswer("Silakan hubungi call center kami di (021) 1500-749/ WhatsApp 0813-1542-2822/ email ke halo@OttoPay.id");
        faqmodel.add(q27);

    }


}
