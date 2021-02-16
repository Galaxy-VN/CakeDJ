package me.JustAPie.CakeDJ;

import net.dv8tion.jda.api.Permission;

import java.util.List;

public interface ICommand {
    void exec(CommandContext ctx);
    String getName();
    String getHelp();
    String getCategory();
    String getUsage();
    default int argsLength() { return 0; }
    default boolean isOwnerCommand() { return false; }
    default List<String> getAliases() { return List.of(); }
    default List<Permission> getUserPermissions() { return List.of(); }
    default List<Permission> getBotPermission() { return List.of(); }
    default boolean needConnectedVoice() { return false; }
    default boolean sameVoice() { return false; }
    default boolean activeQueue() { return false; }
    default boolean activePlayer() { return false; }
}
