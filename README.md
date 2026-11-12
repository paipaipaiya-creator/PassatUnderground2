# Passat Underground 2 — Android prototype

Лёгкий 2D аркадный гоночный прототип в альбомной ориентации: ночной город, стилизованный Volkswagen Passat B4, трафик, руление, газ, тормоз, нитро и гараж со сменой цвета.

- Минимальная версия: Android 6.0 (API 23)
- Целевая версия: Android 15 (API 35)
- Сборка: Gradle 8.9 + Android Gradle Plugin 8.7.3 + Java 17

## Как получить APK через GitHub с телефона

1. Распакуйте ZIP на телефоне.
2. Откройте репозиторий `https://github.com/paipaipaiya-creator/PassatUnderground2` в браузере.
3. Нажмите **Add file → Upload files**.
4. Выберите файлы и папки из распакованного проекта, включая скрытую папку `.github/workflows` (если файловый менеджер её не показывает, включите показ скрытых файлов).
5. Нажмите **Commit changes**.
6. Откройте вкладку **Actions** и дождитесь зелёной галочки у `Build Passat Underground 2 APK`.
7. Откройте успешный запуск, прокрутите вниз до **Artifacts** и скачайте `PassatUnderground2-debug-apk`.
8. Распакуйте скачанный архив и установите `app-debug.apk`. Android может попросить разрешить установку из этого источника.

Это 2D прототип, а не полноценная 3D-игра уровня Need for Speed Underground 2. APK ещё не собран и должен быть проверен GitHub Actions.
