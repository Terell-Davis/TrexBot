# TrexBot 3.0 (beta)
TrexBot is a general purpose Discord bot designed for personal and friends' servers.
- If you have any suggestions, feel free to open an issue or submit a pull request.

### API Keys

To function correctly, TrexBot requires API keys from the following services:

* Discord - Obtainable at [Discord Developer Portal](https://discord.com/developers)
* YouTube API
* Discord Webhook for `QuoteCommand.java` (Optional)

These keys should be entered into a `.env` file at the root of the project.

#### Software Requirements

* JDK 21 (Later versions might work but I only tested with Java 21)
* Gradle (9.0 or higher recommended)
* dos2unix (Optional, for Linux users running specific scripts)

#### Installation Instructions

**For Linux:**

- JDK 21: `sudo apt install openjdk-21-jdk`
- Gradle: Instructions for installing Gradle can be found at [Gradle's Installation Guide](https://docs.gradle.org/current/userguide/installation.html).
- Dos2Unix: `sudo apt install dos2unix` (useful for script conversion if needed)
- {Add more Later}
**For Windows:**

- Instructions for installing JDK 21 and Gradle on Windows can be found on their respective official websites. 
- Ensure environment variables are set correctly for both tools.
- {Add more later}

### Building and Installing

1. **Building:** The project can be built using the provided `setup.sh` script. This script automates the process of setting up your environment and compiling the bot.
   - This might not work right now. Need to fix this later
   
2. **Self-Hosting (Systemd):**
   - Download the latest release from the repository or the zip file if a release isn't available.
   - Run `setup.sh` to prepare the bot for operation.
   - Create a systemd service file for TrexBot by editing `/etc/systemd/system/trexbot.service`.

3. **Docker**
   - Coming Soon...
   - 
### License
(TODO)
