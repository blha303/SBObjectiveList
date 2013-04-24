package me.blha303.objectivelist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.ChatPaginator.ChatPage;

public class SBObjectiveList extends JavaPlugin {
    Scoreboard sb;

    @Override
    public void onEnable() {
        sb = getServer().getScoreboardManager().getMainScoreboard();
    }

    public boolean onCommand(CommandSender sender, Command command, String labl, String[] args) {
        if (sender.hasPermission("sbobjlist.use")) {
        int pgnum = 1;
        boolean paginate = true;
        Set<Objective> set = sb.getObjectives();
        if (args.length >= 1) {
            try {
                pgnum = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
//                Objective obj = sb.getObjective(args[0]);
//                if (obj != null) {
//                    set.add(obj);
//                } else {
//                    sender.sendMessage(ChatColor.RED + args[0] + " isn't a valid objective.");
//                    return true;
//                }
                return false;
            }
        } else {
            paginate = false;
        }
//        if (set.isEmpty()) set = sb.getObjectives();
        List<String> output = new ArrayList<String>();
        output.add(ChatColor.GOLD + "Objective list (default)");
        if (!set.isEmpty()) {
            for (Objective obj : set) {
                String label;
                if (obj.getDisplayName().equals(obj.getName())) {
                    label = obj.getDisplayName();
                } else {
                    label = String.format("%s (%s)", obj.getDisplayName(), obj.getName());
                }
                Map<String, Integer> scores = new HashMap<String, Integer>();
                for (OfflinePlayer p : getServer().getOfflinePlayers()) {
                    if (obj.getScore(p).getScore() != 0)
                        scores.put(p.getName(), obj.getScore(p).getScore());
                }
                output.add(String.format(ChatColor.AQUA + "%s " + ChatColor.GRAY + ": " + ChatColor.BLUE + "%s", label, obj.getCriteria()));
                if (!scores.isEmpty()) {
                    for (Entry<String, Integer> e : scores.entrySet()) {
                        output.add(String.format(ChatColor.LIGHT_PURPLE + "  %s" + ChatColor.GRAY + ": " + ChatColor.RED + "%s", e.getKey(), e.getValue()));
                    }
                } else {
                    output.add(ChatColor.LIGHT_PURPLE + "  There are no player scores in this objective.");
                }
            }
        } else {
            output.add(ChatColor.AQUA + "There are no objectives in this scoreboard.");
        }
        if (paginate) {
            int x = 0;
            String out = "";
            for (String s : output) {
                if (x == 0) {
                    out = s;
                } else {
                    out = out + "\n" + s;
                }
                x++;
            }
            ChatPage page = ChatPaginator.paginate(out, pgnum);
            for (String s : page.getLines()) {
                sender.sendMessage(s);
            }
        } else {
            for (String s : output) {
                sender.sendMessage(s);
            }
        }
        } else {
            sender.sendMessage(ChatColor.RED + "You can't use this command.");
            return true;
        }
        return true;
    }
}
