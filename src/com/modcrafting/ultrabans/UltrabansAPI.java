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

import java.util.ArrayList;
import java.util.List;

import com.modcrafting.ultrabans.util.BanInfo;
import com.modcrafting.ultrabans.util.BanType;

public class UltrabansAPI {
    private Ultrabans plugin;

    public UltrabansAPI(Ultrabans instance) {
        plugin = instance;
    }

    public void addPlayer(final String playerName, final String reason, final String admin, final long time, final BanType type) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getUBDatabase().addPlayer(playerName, reason, admin, time, type.getId());
            }
        });
    }

    public void banPlayer(final String playerName, final String reason, final String admin) {
        List<BanInfo> list = new ArrayList<BanInfo>();
        if (plugin.cache.containsKey(playerName))
            list = plugin.cache.get(playerName);
        list.add(new BanInfo(playerName, reason, admin, 0, BanType.BAN));
        plugin.cache.put(playerName.toLowerCase(), list);
        addPlayer(playerName, reason, admin, 0, BanType.BAN);
    }

    public void ipbanPlayer(final String playerName, final String ip, final String reason, final String admin) {
        BanInfo info = new BanInfo(playerName, reason, admin, 0, BanType.IPBAN);
        List<BanInfo> list = new ArrayList<BanInfo>();
        if (plugin.cacheIP.containsKey(playerName))
            list = plugin.cache.get(playerName);
        if (plugin.cacheIP.containsKey(playerName))
            list = plugin.cache.get(playerName);
        list.add(info);
        plugin.cache.put(playerName.toLowerCase(), list);
        plugin.cacheIP.put(ip, list);
        addPlayer(playerName, reason, admin, 0, BanType.IPBAN);
    }

    public void jailPlayer(final String playerName, final String reason, final String admin) {
        List<BanInfo> list = new ArrayList<BanInfo>();
        if (plugin.cache.containsKey(playerName))
            list = plugin.cache.get(playerName);
        list.add(new BanInfo(playerName, reason, admin, 0, BanType.JAIL));
        plugin.cache.put(playerName.toLowerCase(), list);
        addPlayer(playerName, reason, admin, 0, BanType.JAIL);
    }

    public void warnPlayer(String playerName, String reason, String admin) {
        addPlayer(playerName, reason, admin, 0, BanType.WARN);
    }

    public void pardonPlayer(final String playerName, final String admin) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getUBDatabase().removeFromJaillist(playerName);
            }
        });
        addPlayer(playerName, "Released From Jail", admin, 0, BanType.INFO);
    }

    public void mutePlayer(final String playerName, final String reason, final String admin) {
        plugin.muted.add(playerName.toLowerCase());
        addPlayer(playerName, reason, admin, 0, BanType.MUTE);
    }

    public void kickPlayer(final String playerName, final String reason, final String admin) {
        addPlayer(playerName, reason, admin, 0, BanType.KICK);
    }

    public void permabanPlayer(final String playerName, final String reason, final String admin) {
        List<BanInfo> list = new ArrayList<BanInfo>();
        if (plugin.cache.containsKey(playerName))
            list = plugin.cache.get(playerName);
        list.add(new BanInfo(playerName, reason, admin, 0, BanType.PERMA));
        plugin.cache.put(playerName.toLowerCase(), list);
        addPlayer(playerName, reason, admin, 0, BanType.PERMA);
    }

    public void tempbanPlayer(final String playerName, final String reason, final long temp, final String admin) {
        List<BanInfo> list = new ArrayList<BanInfo>();
        if (plugin.cache.containsKey(playerName))
            list = plugin.cache.get(playerName);
        list.add(new BanInfo(playerName, reason, admin, 0, BanType.TEMPBAN));
        plugin.cache.put(playerName.toLowerCase(), list);
        addPlayer(playerName, reason, admin, temp, BanType.TEMPBAN);
    }

    public void tempipbanPlayer(final String playerName, final String ip, final String reason, final long temp, final String admin) {
        BanInfo info = new BanInfo(playerName, reason, admin, 0, BanType.TEMPIPBAN);
        List<BanInfo> list = new ArrayList<BanInfo>();
        if (plugin.cacheIP.containsKey(playerName))
            list = plugin.cache.get(playerName);
        if (plugin.cacheIP.containsKey(playerName))
            list = plugin.cache.get(playerName);
        list.add(info);
        plugin.cache.put(playerName.toLowerCase(), list);
        plugin.cacheIP.put(ip, list);
        addPlayer(playerName, reason, admin, temp, BanType.TEMPIPBAN);
    }

    public void tempjailPlayer(final String playerName, final String reason, final long temp, final String admin) {
        List<BanInfo> list = new ArrayList<BanInfo>();
        if (plugin.cache.containsKey(playerName))
            list = plugin.cache.get(playerName);
        list.add(new BanInfo(playerName, reason, admin, 0, BanType.TEMPJAIL));
        addPlayer(playerName, reason, admin, temp, BanType.TEMPJAIL);
    }

    public void clearWarn(final String playerName) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getUBDatabase().clearWarns(playerName);
            }
        });
    }
}
