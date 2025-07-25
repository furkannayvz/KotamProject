# Java 21 için Eclipse Temurin (resmi ve kararlı)
FROM eclipse-temurin:21-jdk

# Uygulama klasörü
WORKDIR /app

# Maven ile build edilmiş JAR dosyasını kopyala
COPY target/aom-*.jar app.jar

# Uygulamanın çalışacağı port (application.properties dosyasına göre ayarla)
EXPOSE 8080

# JAR dosyasını başlat
ENTRYPOINT ["java", "-jar", "app.jar"]
