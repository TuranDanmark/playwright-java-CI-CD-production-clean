# Используем официальный образ Playwright с уже установленными браузерами
FROM mcr.microsoft.com/playwright/java:v1.42.0-jammy

WORKDIR /app

# Копируем pom.xml и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем весь проект
COPY src ./src

# По умолчанию запускаем тесты
CMD ["sh", "-c", "mvn test -Dheadless=true && mvn allure:report"]