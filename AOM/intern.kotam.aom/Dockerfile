<<<<<<< HEAD
# Java 21 için Eclipse Temurin (resmi ve kararlı)
FROM eclipse-temurin:21-jdk

# Uygulama klasörü
WORKDIR /app

# Maven ile build edilmiş JAR dosyasını kopyala
COPY target/aom-*.jar app.jar

# Uygulamanın çalışacağı port (application.properties dosyasına göre ayarla)
EXPOSE 8080

# JAR dosyasını başlat
=======
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/kotam.aom-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

>>>>>>> 239ece6 (Dokümantasyon ve Dockerfile eklendi)
ENTRYPOINT ["java", "-jar", "app.jar"]
