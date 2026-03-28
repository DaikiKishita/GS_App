apt-get update && \
    apt-get install -y curl unzip && \
    rm -rf /var/lib/apt/lists/*

curl -fsSL https://start.spring.io/starter.zip \
    -o starter.zip

unzip starter.zip -d app && \
    cd app && \
    ./gradlew clean build -x test

docker compose up -d --build