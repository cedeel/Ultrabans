package com.modcrafting.ultrabans;

/**
 * Created by cedeel on 09/12/13.
 */
public enum Language {

    // Permission
    PERMISSION("Permission"),

    // Ban
    BAN_ARGUMENTS("Ban.Arguments"),
    BAN_FAILED("Ban.Failed"),
    BAN_DENIED("Ban.Denied"),
    BAN_EMO("Ban.Emo"),
    BAN_MSGTOVICTIM("Ban.MsgToVictim"),
    BAN_MSGTOBROADCAST("Ban.MsgToBroadcast"),
    BAN_LOGIN("Ban.Login"),

    // CheckBan
    CHECKBAN_ARGUMENTS("CheckBan.Arguments"),
    CHECKBAN_HEADER("CheckBan.Header"),
    CHECKBAN_NONE("CheckBan.None"),

    // CheckIP
    CHECKIP_ARGUMENTS("CheckIP.Arguments"),
    CHECKIP_NOPLAYER("CheckIP.NoPlayer"),
    CHECKIP_MSG1("CheckIP.MSG1"),
    CHECKIP_MSG2("CheckIP.MSG2"),
    CHECKIP_EXCEPTION("CheckIP.Exception"),

    // Clean
    CLEAN_COMPLETE("Clean.Complete"),

    // DupeIP
    DUPEIP_ARGUMENTS("DupeIP.Arguments"),
    DUPEIP_NOPLAYER("DupeIP.NoPlayer"),
    DUPEIP_HEADER("DupeIP.Header"),
    DUPEIP_COMPLETED("DupeIP.Completed"),

    // Export
    EXPORT_FAILED("Export.Failed"),
    EXPORT_COMPLETED("Export.Completed"),

    // History
    HISTORY_FAILED("History.Failed"),
    HISTORY_HEADER("History.Header"),

    // Import
    IMPORT_LOADING("Import.Loading"),
    IMPORT_FAILED("Import.Failed"),
    IMPORT_COMPLETED("Import.Completed"),

    // InvOf
    INVOF_ARGUMENTS("InvOf.Arguments"),
    INVOF_CONSOLE("InvOf.Console"),
    INVOF_FAILED("InvOf.Failed"),

    // IPBan
    IPBAN_ARGUMENTS("IPBan.Arguments"),
    IPBAN_FAILED("IPBan.Failed"),
    IPBAN_DENIED("IPBan.Denied"),
    IPBAN_EMO("IPBan.Emo"),
    IPBAN_IPNOTFOUND("IPBan.IPNotFound"),
    IPBAN_MSGTOVICTIM("IPBan.MsgToVictim"),
    IPBAN_MSGTOBROADCAST("IPBan.MsgToBroadcast"),
    IPBAN_LOGIN("IPBan.Login"),

    // Jail
    JAIL_ARGUMENTS("Jail.Arguments"),
    JAIL_SETFAIL("Jail.SetFail"),
    JAIL_SETJAIL("Jail.SetJail"),
    JAIL_SETRELEASE("Jail.SetRelease"),
    JAIL_MSGTOVICTIM("Jail.MsgToVictim"),
    JAIL_MSGTOBROADCAST("Jail.MsgToBroadcast"),
    JAIL_FAILED("Jail.Failed"),
    JAIL_ONLINE("Jail.Online"),
    JAIL_DENIED("Jail.Denied"),
    JAIL_EMO("Jail.Emo"),
    JAIL_PLACEMSG("Jail.PlaceMsg"),
    JAIL_BREAKMSG("Jail.BreakMsg"),

    // Kick
    KICK_ARGUMENTS("Kick.Arguments"),
    KICK_MSGTOALL("Kick.MsgToAll"),
    KICK_MSGTOVICTIM("Kick.MsgToVictim"),
    KICK_MSGTOBROADCAST("Kick.MsgToBroadcast"),
    KICK_ONLINE("Kick.Online"),
    KICK_DENIED("Kick.Denied"),
    KICK_EMO("Kick.Emo"),

    // Lockdown
    LOCKDOWN_ARGUMENTS("Lockdown.Arguments"),
    LOCKDOWN_START("Lockdown.Start"),
    LOCKDOWN_END("Lockdown.End"),
    LOCKDOWN_STATUS("Lockdown.Status"),
    LOCKDOWN_LOGINMSG("Lockdown.LoginMsg"),

    // Mute
    MUTE_ARGUMENTS("Mute.Arguments"),
    MUTE_MUTEMSGTOSENDER("Mute.MuteMsgToSender"),
    MUTE_MUTEMSGTOVICTIM("Mute.MuteMsgToVictim"),
    MUTE_UNMUTEMSGTOSENDER("Mute.UnmuteMsgToSender"),
    MUTE_UNMUTEMSGTOVICTIM("Mute.UnmuteMsgToVictim"),
    MUTE_DENIED("Mute.Denied"),
    MUTE_EMO("Mute.Emo"),
    MUTE_FAILED("Mute.Failed"),
    MUTE_CHAT("Mute.Chat"),

    // Pardon
    PARDON_ARGUMENTS("Pardon.Arguments"),
    PARDON_MSG("Pardon.Msg"),
    PARDON_FAILED("Pardon.Failed"),

    // PermaBan
    PERMABAN_ARGUMENTS("PermaBan.Arguments"),
    PERMABAN_MSGTOVICTIM("PermaBan.MsgToVictim"),
    PERMABAN_MSGTOBROADCAST("PermaBan.MsgToBroadcast"),
    PERMABAN_FAILED("PermaBan.Failed"),
    PERMABAN_ONLINE("PermaBan.Online"),
    PERMABAN_DENIED("PermaBan.Denied"),
    PERMABAN_EMO("PermaBan.Emo"),

    // Ping
    PING_FAILED("Ping.Failed"),
    PING_PERSONAL("Ping.Personal"),
    PING_OTHER("Ping.Other"),

    // Reload
    RELOAD("Reload"),

    // Spawn
    SPAWN_ARGUMENTS("Spawn.Arguments"),
    SPAWN_MSGTOVICTIM("Spawn.MsgToVictim"),
    SPAWN_MSGTOSENDER("Spawn.MsgToSender"),
    SPAWN_FAILED("Spawn.Failed"),

    // Starve
    STARVE_ARGUMENTS("Starve.Arguments"),
    STARVE_MSGTOVICTIM("Starve.MsgToVictim"),
    STARVE_MSGTOSENDER("Starve.MsgToSender"),
    STARVE_FAILED("Starve.Failed"),

    // Status
    STATUS_USAGE("Status.Usage"),
    STATUS_CACHEHEADER("Status.CacheHeader"),
    STATUS_CACHEBANS("Status.CacheBans"),
    STATUS_CACHEIPBANS("Status.CacheIPBans"),

    // Tempban
    TEMPBAN_ARGUMENTS("Tempban.Arguments"),
    TEMPBAN_MSGTOVICTIM("Tempban.MsgToVictim"),
    TEMPBAN_MSGTOBROADCAST("Tempban.MsgToBroadcast"),
    TEMPBAN_FAILED("Tempban.Failed"),
    TEMPBAN_DENIED("Tempban.Denied"),
    TEMPBAN_TIMEFAIL("Tempban.TimeFail"),
    TEMPBAN_EMO("Tempban.Emo"),
    TEMPBAN_LOGIN("Tempban.Login"),

    // TempIpBan
    TEMPIPBAN_ARGUMENTS("TempIpBan.Arguments"),
    TEMPIPBAN_MSGTOVICTIM("TempIpBan.MsgToVictim"),
    TEMPIPBAN_MSGTOBROADCAST("TempIpBan.MsgToBroadcast"),
    TEMPIPBAN_FAILED("TempIpBan.Failed"),
    TEMPIPBAN_DENIED("TempIpBan.Denied"),
    TEMPIPBAN_EMO("TempIpBan.Emo"),
    TEMPIPBAN_IPNOTFOUND("TempIpBan.IPNotFound"),

    // TempJail
    TEMPJAIL_ARGUMENTS("TempJail.Arguments"),
    TEMPJAIL_MSGTOVICTIM("TempJail.MsgToVictim"),
    TEMPJAIL_MSGTOBROADCAST("TempJail.MsgToBroadcast"),
    TEMPJAIL_FAILED("TempJail.Failed"),
    TEMPJAIL_DENIED("TempJail.Denied"),
    TEMPJAIL_EMO("TempJail.Emo"),

    // Unban
    UNBAN_ARGUMENTS("Unban.Arguments"),
    UNBAN_MSGTOBROADCAST("Unban.MsgToBroadcast"),
    UNBAN_PERMABANNED("Unban.PermaBanned"),
    UNBAN_FAILED("Unban.Failed"),

    // Warn
    WARN_ARGUMENTS("Warn.Arguments"),
    WARN_MSGTOVICTIM("Warn.MsgToVictim"),
    WARN_MSGTOBROADCAST("Warn.MsgToBroadcast"),
    WARN_DENIED("Warn.Denied"),
    WARN_EMO("Warn.Emo"),

    // MaxWarn
    MAXWARN_MSGTOBROADCAST("MaxWarn.MsgToBroadcast");

    private String location;

    private Language(String loc) {
        location = loc;
    }

    public String getLocation() {
        return location;
    }
}
