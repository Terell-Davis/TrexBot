# Trexbot
A general purpose discord bot I use in personal/friends servers.
Also has music support for Youtube, SoundCloud, Bandcamp.


<h3>What's Coming?</h1>

- TBD
- If you have any suggestions

## Prerequisites:

#### API Keys
* Discord - https://discord.com/developers
* Youtube api
* Discord Webhook for ```QuoteCommand.java```(Optional) 

Keys must be entered into ```.env```

#### Requirements

* Installation of Jdk 1.8_342 (higher might work but I have not tested)
* Gradle 
* 

Install dependancies:

For Linux:
* Jdk 8: `sudo apt install openjdk-8-jdk`
* Gradle 6.5 or higher: https://docs.gradle.org/current/userguide/installation.html
* Dos2Unix: `sudo apt isntall dos2unix`(if using [script name])

For Windows:
* TBD

### Building
- setup.sh

### Installing - Self hosting (Systemd)

1. Download release if available, alternatively download repository zip
2. Run `setup.sh`
3. `nano /etc/systemd/trexbot.service`
4. See configuration options in /config/config.py
