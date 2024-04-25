# TrexBot
TrexBot is a versatile, general-purpose Discord bot designed for personal and friends' servers. While its primary feature is music playback support from platforms like YouTube, SoundCloud, and Bandcamp, it's also capable of a wide range of other functions. This README outlines the prerequisites, installation, and setup instructions for running TrexBot.

## What's Coming?

- Features are currently being planned.
- If you have any suggestions, feel free to open an issue or submit a pull request.

## Prerequisites:

### API Keys
To function correctly, TrexBot requires API keys from the following services:

* Discord - Obtainable at [Discord Developer Portal](https://discord.com/developers)
* YouTube API
* Discord Webhook for `QuoteCommand.java` (Optional)

These keys should be entered into a `.env` file at the root of the project.

### Requirements

#### Software Requirements

* JDK 11 (Earlier versions like JDK 17 might work, but JDK 11 is was used for testing)
* Gradle (6.5 or higher recommended)
* dos2unix (Optional, for Linux users running specific scripts)

#### Installation Instructions

**For Linux:**

- JDK 11: `sudo apt install openjdk-11-jdk`
- Gradle: Instructions for installing Gradle can be found at [Gradle's Installation Guide](https://docs.gradle.org/current/userguide/installation.html).
- Dos2Unix: `sudo apt install dos2unix` (useful for script conversion if needed)

**For Windows:**

- Instructions for installing JDK 11 and Gradle on Windows can be found on their respective official websites. Ensure environment variables are set correctly for both tools.

### Building and Installing

1. **Building:** The project can be built using the provided `setup.sh` script. This script automates the process of setting up your environment and compiling the bot.
   
2. **Self-Hosting (Systemd):**
   - Download the latest release from the repository or the zip file if a release isn't available.
   - Run `setup.sh` to prepare the bot for operation.
   - Create a systemd service file for TrexBot by editing `/etc/systemd/system/trexbot.service`. Sample configurations and service files can be found in the `/config` directory.

### Multi-Purpose Functionality
TrexBot is more than just a music bot. It's designed to be a multi-purpose tool that can enhance your Discord server's functionality. Whether you're looking to play music, manage server tasks, or integrate with other APIs, TrexBot aims to provide a comprehensive solution. We're always looking to expand its capabilities, so your suggestions and contributions are welcome.

### Contributing
We welcome contributions and suggestions! If you have ideas for new features or improvements, please open an issue or submit a pull request on [GitHub](https://github.com/Terell-Davis/TrexBot).

### License
Specify your project's license here.
