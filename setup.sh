#!/bin/bash
if [ -f .env ]; then
  echo ".env file found. Continuing..."
else
  echo "Copying env-example to .env..."
  cp env-template .env
fi

echo "Building Trexbot"

chmod +x ./gradlew

./gradlew clean installShadowDist
rm -rf bin
mv build/install/TrexBot-shadow/bin bin
rm -rf lib
mv build/install/TrexBot-shadow/lib lib

cp .env bin/.env
echo "Done building Trexbot"