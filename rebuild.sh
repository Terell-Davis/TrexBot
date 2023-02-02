#!/bin/bash
echo "Building Trexbot"
sudo chmod +x ./gradlew
if ! command -v dos2unix &> /dev/null; then
  echo "dos2unix not found. Exiting..."
  exit 1
fi

dos2unix ./gradlew
./gradlew clean installShadowDist
rm -rf bin
mv build/install/TrexBot-shadow/bin bin
rm -rf lib
mv build/install/TrexBot-shadow/lib lib

cp .env bin/.env
echo "Done building Trexbot"