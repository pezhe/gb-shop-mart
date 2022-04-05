docker build --build-arg JAR_FILE=build/libs/config-server-0.0.1-SNAPSHOT.jar -t config-server-mart .

docker run -p [host]:[inside docker] --name [container name] -e GIT_URI= -v [host_path]:[путь из environments] -d  [имя image]

-d скрыть вывод контейнера (скрыть вывод приложения и перевести его в фоновый режим)
вместо
-it интерактивный режим без перевода в фон

docker stop [имя контейнера/хэш контейнера]
docker start [имя контейнера/хэш контейнера]
docker ps  - посмотреть запущенные контейнеры
docker images - посмотреть образы
docker logs --tail 200