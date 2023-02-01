#!/bin/bash
# The check isnt working I will fix this later...... maybe....
#echo "Checking if Gradle and dos2unix are installed..."
#if ! command -v gradle &> /dev/null; then
#    echo "Gradle not found, please install it and try again"
#    exit 1
#fi

#if ! command -v dos2unix &> /dev/null; then
#    echo "dos2unix not found, please install it and try again"
#    exit 1
#fi

if [ -f .env ]; then
  echo ".env file found. Updating..."
else
  echo "Copying env-example to .env..."
  cp env-example .env
fi

echo "Discord API Key (press enter to skip): "
read discord_api_key
if [ -n "$discord_api_key" ]; then
  sed -i "s/discordkey/$discord_api_key/g" .env
fi

echo "Youtube API Key (press enter to skip): "
read youtube_api_key
if [ -n "$youtube_api_key" ]; then
  sed -i "s/youtubeapikey/$youtube_api_key/g" .env
fi

echo "Discord Webhook URL (press enter to skip): "
read webhook_url
if [ -n "$webhook_url" ]; then
  sed -i "s/quotechannel/$webhook_url/g" .env
fi

echo "Bot Owner's Discord ID (press enter to skip): "
read owner_id
if [ -n "$owner_id" ]; then
  sed -i "s/ownerid/$owner_id/g" .env
fi

echo "Do you want to change the prefix? (y/n): "
read change_prefix
if [ "$change_prefix" == "y" ]; then
  echo "What prefix do you want to use?"
  read new_prefix
  sed -i "s/t-/$new_prefix/g" .env
fi

echo "Building Trexbot"
sudo chmod +x ./gradlew
if ! command -v dos2unix &> /dev/null; then
  echo "dos2unix not found. Exiting..."
  exit 1
fi

dos2unix ./gradlew
./gradlew clean installShadowDist
mv build/install/TrexBot-shadow/bin bin
rm -rf lib
mv build/install/TrexBot-shadow/lib lib
mv .env bin/.env
echo "Done building Trexbot"

