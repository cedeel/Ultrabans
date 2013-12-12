Ultrabans
===
Administration system for Minecraft Bukkit Servers

Want to contribute?
---
If you have an idea that you think Ultrabans needs, and I'm not responding (for whatever reason), feel free to submit a pull request!

Ultrabans uses Maven to manage its dependencies, and is dependent on the Java 5 JDK or later. I use 4 spaces for indentation. When committing changes to your local branch, try to keep each feature in its own commit.

    git clone https://github.com/cedee;/Ultrabans.git
    cd Ultrabans
    mvn -U clean package

Features
----
* MySQL and SQLite support
* Configurable messages
* Chat scanning for IPs, spamming, and word filtering with configurable result
* Jail, muting and more admin functions incorporated
* (Optional) fining players for infractions
* Server Lockdown (temporary whitelist)

![Build Status](https://api.travis-ci.org/cedeel/Ultrabans.png)

Warning: Builds may pass, this only means that it compiled without errors, this does not indicate a fully functional build.

Licensing
---
![Open Source](http://www.gnu.org/graphics/gplv3-127x51.png)
To view a full copy of the license see http://www.gnu.org/licenses/gpl-3.0.txt
