/* COPYRIGHT (c) 2013 Deathmarine (Joshua McCurry)
 * This file is part of Ultrabans.
 * Ultrabans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Ultrabans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Ultrabans.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.modcrafting.ultrabans;

import com.modcrafting.ultrabans.commands.*;
import com.modcrafting.ultrabans.db.Database;
import com.modcrafting.ultrabans.db.SQL;
import com.modcrafting.ultrabans.db.SQLite;
import com.modcrafting.ultrabans.listeners.UltraBanBlockListener;
import com.modcrafting.ultrabans.listeners.UltraBanPlayerListener;
import com.modcrafting.ultrabans.util.BanInfo;
import com.modcrafting.ultrabans.util.Jailtools;
import net.h31ix.updater.Updater;
import net.h31ix.updater.Updater.UpdateResult;
import net.h31ix.updater.Updater.UpdateType;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Ultrabans extends JavaPlugin {
    public static String DEFAULT_ADMIN;
    public static String DEFAULT_REASON;
    public static String DEFAULT_DENY_MESSAGE;
    public HashSet<String> muted = new HashSet<String>();
    public Map<String, List<BanInfo>> cache = new HashMap<String, List<BanInfo>>();
    public Map<String, List<BanInfo>> cacheIP = new HashMap<String, List<BanInfo>>();
    public Jailtools jail = new Jailtools(this);
    private UltrabansAPI api = new UltrabansAPI(this);
    private YamlConfiguration lang;
    private Database db;
    private boolean log;

    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        cache.clear();
        cacheIP.clear();
        muted.clear();
    }

    public void onEnable() {
        long time = System.currentTimeMillis();
        this.getDataFolder().mkdir();
        this.saveDefaultConfig();
        FileConfiguration config = getConfig();
        log = config.getBoolean("Log.Enabled", true);

        DEFAULT_ADMIN = config.getString("Label.Console", "Server");
        DEFAULT_REASON = config.getString("Label.Reason", "Unsure");
        DEFAULT_DENY_MESSAGE = ChatColor.RED + this.getString(Language.PERMISSION);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new UltraBanPlayerListener(this), this);
        pm.registerEvents(new UltraBanBlockListener(this), this);

        loadCommands();

        //Storage
        if (config.getString("Database").equalsIgnoreCase("mysql")) {
            db = new SQL(this);
        } else {
            db = new SQLite(this);
        }
        db.load();

        if (config.getBoolean("AutoUpdater.Enabled", true))
            // loadUpdater();
        // Disabled since project is not on Bukkit dev

        this.getLogger().info("Loaded. " + ((System.currentTimeMillis() - time) / 1000) + " secs.");
    }

    private void initLang(FileConfiguration config) {
        String language = config.getString("Language", "en-us");
        File langFolder = new File(this.getDataFolder(), "lang");
        langFolder.mkdir();
        File langFile = new File(langFolder, language + ".yml");
        if (!langFile.exists()) {
            try {
                langFile.createNewFile();
                BufferedInputStream in = new BufferedInputStream(this.getResource(language + ".yml"));
                FileOutputStream fileOutputStream = new FileOutputStream(langFile);
                byte[] data = new byte[1024];
                int c;
                while ((c = in.read(data, 0, 1024)) != -1)
                    fileOutputStream.write(data, 0, c);
                in.close();
                fileOutputStream.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    private void loadUpdater() {
        Updater updater = new Updater(this, 10, this.getFile(), UpdateType.DEFAULT, true);
        if (!updater.getResult().equals(UpdateResult.SUCCESS)) {
            if (updater.getResult().equals(UpdateResult.FAIL_NOVERSION)) {
                this.getLogger().info("Unable to connect to dev.bukkit.org.");
            } else {
                this.getLogger().info("No Updates found on dev.bukkit.org.");
            }
        } else {
            this.getLogger().info("Update " + updater.getLatestName() + " found please restart your server.");
        }
    }

    private void loadCommands() {
        getCommand("ban").setExecutor(new Ban(this));
        getCommand("checkban").setExecutor(new Checkban(this));
        getCommand("checkip").setExecutor(new CheckIP(this));
        getCommand("dupeip").setExecutor(new DupeIP(this));
        getCommand("importbans").setExecutor(new Import(this));
        getCommand("exportbans").setExecutor(new Export(this));
        getCommand("uhelp").setExecutor(new Help(this));
        getCommand("ipban").setExecutor(new Ipban(this));
        getCommand("kick").setExecutor(new Kick(this));
        getCommand("ureload").setExecutor(new Reload(this));
        getCommand("forcespawn").setExecutor(new Spawn(this));
        getCommand("starve").setExecutor(new Starve(this));
        getCommand("tempban").setExecutor(new Tempban(this));
        getCommand("tempipban").setExecutor(new Tempipban(this));
        getCommand("unban").setExecutor(new Unban(this));
        getCommand("uversion").setExecutor(new Version(this));
        getCommand("warn").setExecutor(new Warn(this));
        getCommand("jail").setExecutor(new Jail(this));
        getCommand("tempjail").setExecutor(new Tempjail(this));
        getCommand("permaban").setExecutor(new Perma(this));
        getCommand("lockdown").setExecutor(new Lockdown(this));
        getCommand("umute").setExecutor(new Mute(this));
        getCommand("history").setExecutor(new History(this));
        getCommand("pardon").setExecutor(new Pardon(this));
        getCommand("invof").setExecutor(new Inventory(this));
        getCommand("ustatus").setExecutor(new Status(this));
        getCommand("uclean").setExecutor(new Clean(this));
        getCommand("uping").setExecutor(new Ping(this));
    }

    public Database getUBDatabase() {
        return db;
    }

    public void log(String s) {
        if (log) {
            getLogger().info(ChatColor.stripColor(s));
        }
    }

    public String getString(Language piece) {
        if(lang == null)
            initLang(getConfig());
        return lang.getString(piece.getLocation());
    }

    public UltrabansAPI getAPI() {
        return api;
    }

}



