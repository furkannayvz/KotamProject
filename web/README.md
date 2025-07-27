# KOTAM Frontend Web Uygulaması

Bu proje, KOTAM kullanıcılarının mobil hat kullanım detaylarını (internet, dakika, SMS) takip edebildikleri, sisteme kayıt olup giriş yapabildikleri React tabanlı web arayüzüdür.

---

### Gereksinimler

Projeyi çalıştırabilmek için bilgisayarınızda aşağıdakilerin kurulu olması gerekmektedir:

* **Node.js** (v18 veya daha güncel bir sürüm önerilir)
* **npm** (Node.js ile birlikte otomatik olarak yüklenir)

---

### Kurulum

1.  Projeyi bilgisayarınıza klonladıktan sonra, bir terminal açarak projenin ana klasörüne gidin.

2.  Gerekli tüm paketleri yüklemek için aşağıdaki komutu çalıştırın:
    ```bash
    npm install
    ```

---

### Uygulamayı Çalıştırma

1.  Kurulum tamamlandıktan sonra, geliştirme sunucusunu başlatmak için aşağıdaki komutu çalıştırın:
    ```bash
    npm start
    ```

2.  Bu komut, uygulamayı derleyecek ve varsayılan tarayıcınızda **`http://localhost:3000`** adresinde otomatik olarak açacaktır.

---

### ⚠️ Önemli Not

Bu frontend uygulamasının tek başına bir anlamı yoktur. Tüm özelliklerinin çalışabilmesi için **KOTAM Backend Servisi'nin** `http://localhost:8080` adresinde çalışır durumda olması **ZORUNLUDUR**.

Eğer backend servisi çalışmıyorsa, uygulama veri çekemez ve hata verir.

