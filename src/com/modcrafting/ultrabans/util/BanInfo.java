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
package com.modcrafting.ultrabans.util;

public class BanInfo {
    private String name;
    private String reason;
    private String admin;
    private long endTime;
    private BanType type;

    public BanInfo(String name, String reason, String admin, long endTime, BanType type) {
        this.name = name;
        this.reason = reason;
        this.admin = admin;
        this.endTime = endTime;
        this.type = type;
    }

    @Deprecated
    public BanInfo(String name, String reason, String admin, long endTime, int type) {
        this(name, reason, admin, endTime, BanType.fromID(type));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public BanType getType() {
        return type;
    }

    public void setType(BanType type) {
        this.type = type;
    }
}
