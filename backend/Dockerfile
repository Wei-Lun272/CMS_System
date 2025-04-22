# 使用官方 Java 17 映像作為基底
FROM openjdk:17-jdk-slim

# 設定容器內的工作目錄
WORKDIR /app

# 複製打包好的 JAR 檔案到容器中
COPY target/Construction_Management_System-0.0.1-SNAPSHOT.jar app.jar

# 曝露應用程式使用的 port（Spring Boot 預設是 8080）
EXPOSE 8080

# 啟動指令
ENTRYPOINT ["java", "-jar", "app.jar"]
