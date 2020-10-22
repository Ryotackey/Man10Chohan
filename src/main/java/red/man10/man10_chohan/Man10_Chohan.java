package red.man10.man10_chohan;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Man10_Chohan extends JavaPlugin {

    public static boolean setup;

    public static ArrayList<UUID> chou = new ArrayList<>();
    public static ArrayList<UUID> han = new ArrayList<>();

    public FileConfiguration config;

    public double bal;

    public double totalbal;

    public VaultManager val = null;

    public newgame New;

    public int timer;

    public final String prefex = "§f§l[§d§lMa§f§ln§a§l10§c§l丁§b§l半§f§l]";

    public boolean onnoff = true;

    @Override
    public void onEnable() {
        getCommand("mc").setExecutor(this);
        saveDefaultConfig();
        config = getConfig();
        val = new VaultManager(this);
        New = new newgame(this);
    }

    @Override
    public void onDisable() {
        if (setup == true){
            gameclear();
        }
    }

    public void sendtoMember(String text){

        for (int i = 0; i < chou.size(); i++){
            OfflinePlayer p = Bukkit.getOfflinePlayer(chou.get(i));
            if (p == null){
                getLogger().info(chou.get(i).toString()+"は見つからない");
                continue;
            }
            p.getPlayer().sendMessage(text);
        }

        for (int i = 0; i < han.size(); i++){
            OfflinePlayer p = Bukkit.getOfflinePlayer(han.get(i));
            if (p == null){
                getLogger().info(han.get(i).toString()+"は見つからない");
                continue;
            }
            p.getPlayer().sendMessage(text);
        }

    }

    public void mcList(Player p){

        String[] chouplayer = new String[chou.size()];

        String[] hanplayer = new String[han.size()];

        if (setup == true) {
            p.sendMessage(prefex + "§a§l" + jpnBalForm(bal) + "円丁半");
            p.sendMessage("§c§l～丁～");
            for (int i = 0; i < chou.size(); i++) {

                chouplayer[i] = Bukkit.getPlayer(chou.get(i)).getDisplayName();

                p.sendMessage( chouplayer[i]);
            }

            p.sendMessage("§b§l～半～");

            for (int i = 0; i < han.size(); i++) {

                hanplayer[i] = Bukkit.getPlayer(han.get(i)).getDisplayName();

                p.sendMessage(hanplayer[i]);
            }
            return;
        }else {
            p.sendMessage(prefex + "§4§l丁半は開始されてません");
            return;
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player p = (Player) sender;

        UUID playeruuid = p.getUniqueId();

        int MaxPlayer = config.getInt("MaxPlayer");

        switch (args.length) {
            case 0:
                p.sendMessage("§e§l------------§0§l[§d§lMa§f§ln§a§l10§c§l丁§b§l半§0§l]§e§l------------");
                p.sendMessage("§e現在の状況 : §f(§c丁" + chou.size() + "人§f/§b半" + han.size() + "人§f)§6合計" + jpnBalForm(totalbal) + "円");
                mcList(p);
                p.sendMessage("§e§l----------------------------------");
                p.sendMessage("§c/mc c§f : §c丁(偶数に賭ける)");
                p.sendMessage("§b/mc h§f : §b半(奇数に賭ける)");
                p.sendMessage("§6/mc new [金額]§f : §6[金額]で丁半ゲームを開催する");

                break;

            case 1:

                double playerbet = val.getBalance(playeruuid);

                if (args[0].equalsIgnoreCase("c")) {
                    if (setup == true) {
                            if (chou.size() < MaxPlayer) {

                                for (int i = 0; i < chou.size(); i++) {

                                    if (chou.get(i) == playeruuid) {
                                        p.sendMessage("§e§l[Man10丁半]§4§lもうすでに丁に賭けています");
                                        return true;
                                    }
                                }

                                for (int i = 0; i < han.size(); i++) {

                                    if (han.get(i) == playeruuid) {
                                        han.remove(i);
                                        chou.add(playeruuid);
                                        sendtoMember(prefex + "§c§l" + p.getDisplayName() +"§cさんが丁に賭けました§f§l(§c§l丁" + chou.size() + "人§f§l/§b§l半" + han.size() + "人§f§l)§6§l合計" + jpnBalForm(totalbal) + "円");
                                        mcList(p);
                                        return true;
                                    }
                                }
                                if (playerbet < bal) {
                                    p.sendMessage("§e§l[Man10丁半]§4§lお金が足りません");
                                    return false;
                                }else {
                                    chou.add(playeruuid);
                                    val.withdraw(playeruuid, bal);
                                    totalbal += bal;
                                    sendtoMember(prefex + "§c§l" + p.getDisplayName() + "§cさんが丁に賭けました§f§l(§c§l丁" + chou.size() + "人§f§l/§b§l半" + han.size() + "人§f§l)§6§l合計" + jpnBalForm(totalbal) + "円");
                                    mcList(p);

                                    return true;
                                }
                            } else {
                                p.sendMessage(prefex + "§4§lもう定員です");
                                return true;
                            }
                    } else {
                        p.sendMessage(prefex + "§4§l丁半は開始されてません");
                        return false;

                    }
                }

                if (args[0].equalsIgnoreCase("h")) {
                    if (setup == true) {
                            if (han.size() < MaxPlayer) {
                                for (int i = 0; i < han.size(); i++) {

                                    if (han.get(i) == playeruuid) {
                                        p.sendMessage(prefex + "§4§lもうすでに半に賭けています");
                                        return true;
                                    }
                                }

                                for (int i = 0; i < chou.size(); i++) {

                                    if (chou.get(i) == playeruuid) {
                                        chou.remove(i);
                                        han.add(playeruuid);
                                        sendtoMember(prefex + "§b" + p.getDisplayName() +"§bさんが半に賭けました§f§l(§c§l丁" + chou.size() + "人§f§l/§b§l半" + han.size() + "人§f§l)§6§l合計" + jpnBalForm(totalbal) + "円");
                                        mcList(p);
                                        return true;
                                    }
                                }
                                if (playerbet < bal) {
                                    p.sendMessage(prefex + "§4§lお金が足りません");
                                    return false;
                                }else {
                                    han.add(playeruuid);
                                    val.withdraw(playeruuid, bal);
                                    totalbal += bal;
                                    sendtoMember(prefex + "§b" + p.getDisplayName() + "§bさんが半に賭けました§f§l(§c§l丁" + chou.size() + "人§f§l/§b§l半" + han.size() + "人§f§l)§6§l合計" + jpnBalForm(totalbal) + "円");
                                    mcList(p);

                                    return true;
                                }
                            } else {
                                p.sendMessage(prefex + "§4§lもう定員です");
                                return true;
                            }

                    } else {
                        p.sendMessage(prefex + "§4§l丁半は開始されてません");
                        return false;
                    }
                }

                if (args[0].equalsIgnoreCase("cancel")){
                    if (setup == true){
                        if(p.hasPermission("man10chohan.mc.cancel")){

                            gameclear();

                            Bukkit.getServer().broadcastMessage(prefex + "§a§l丁半がキャンセルされました");

                        }else {
                            p.sendMessage(prefex + "§4§l権限がないのでキャンセルできません");
                            return false;
                        }
                    }else {
                        p.sendMessage(prefex + "§4§l丁半は開始されてません");
                        return false;
                    }
                }

                if (args[0].equalsIgnoreCase("reload")){
                    if (p.hasPermission("man10chohan.mc.cancel")) {

                        reloadConfig();
                        config = getConfig();
                        p.sendMessage(prefex + "§aReload complete");
                        return true;
                    }else {
                        p.sendMessage(prefex + "§4§l権限がないのでreloadできません");
                        return false;
                    }

                }

                if (args[0].equalsIgnoreCase("on")){
                    if (p.hasPermission("man10chohan.mc.switch")) {
                        onnoff = true;
                        p.sendMessage(prefex + "§lオンにしました");
                    }else {
                        p.sendMessage(prefex + "§4§l権限がありません");
                    }

                }

                if (args[0].equalsIgnoreCase("off")){
                    if (p.hasPermission("man10chohan.mc.switch")) {
                        onnoff = false;
                        p.sendMessage(prefex + "§lオフにしました");
                    }else {
                        p.sendMessage(prefex + "§4§l権限がありません");
                    }

                }
                break;

            case 2:
                if (args[0].equalsIgnoreCase("new")) {
                    if (onnoff == true) {
                        if (setup == true) {

                            p.sendMessage(prefex + "§4§l丁半はすでに開始されています");
                            return true;

                        } else {
                            try {
                                bal = Double.parseDouble(args[1]);
                                if (10000 > bal) {
                                    p.sendMessage(prefex + "§4§l最低金額は1万円です");
                                    return false;
                                }

                                if (bal > 1000000000) {
                                    p.sendMessage(prefex + "§4§l最高金額は10億円です");
                                    return false;
                                }

                            } catch (NumberFormatException mc) {
                                sender.sendMessage(prefex + "§4§l金額を指定してください.");
                                return false;

                            }

                            setup = New.Start(p);
                        }
                    }else {
                        p.sendMessage(prefex + "§4§l丁半はオフになってます");
                        return false;
                    }
                }

                if (args[0].equalsIgnoreCase("time")){

                    int timeset;

                    try {
                        timeset = Integer.parseInt(args[1]);
                    }catch (NumberFormatException mc){
                        sender.sendMessage(prefex + "§4§l時間を指定してください.");
                        return false;
                    }

                    config.set("Waittime", timeset);
                    saveConfig();
                    sender.sendMessage(prefex + "§a時間を§6" + args[1] + "§aに設定しました");

                }
                break;
        }
        return false;
    }

    String jpnBalForm(double val){
        long val2 = (long) val;

        String addition = "";
        String form = "万";
        long man = val2/10000;
        if(val >= 100000000){
            man = val2/100000000;
            form = "億";
            long mann = (val2 - man * 100000000) / 10000;
            addition = mann + "万";
        }
        return man + form + addition;
    }

    public void gameclear(){

        for (int i = 0; i < chou.size(); i++) {
            val.deposit(chou.get(i), bal);
        }

        for (int i = 0; i < han.size(); i++) {
            val.deposit(han.get(i), bal);
        }

        chou.clear();
        han.clear();

        totalbal = 0;
        setup = false;
        bal = 0;
        timer = 0;
        return;
    }

    public void gamefinish(){
        chou.clear();
        han.clear();

        totalbal = 0;
        setup = false;
        bal = 0;
        timer = 0;
        return;

    }

}

