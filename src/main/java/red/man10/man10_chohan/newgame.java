package red.man10.man10_chohan;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

import static red.man10.man10_chohan.Man10_Chohan.*;

public class newgame {

    private Man10_Chohan plugin;

    public newgame(Man10_Chohan plugin){
        this.plugin = plugin;
    }

    public boolean Start(Player p){
        plugin.timer = plugin.config.getInt("Waittime");
        Bukkit.getServer().broadcastMessage(plugin.prefex + p.getDisplayName() + "§fさんによって §6§l" + plugin.jpnBalForm(plugin.bal) + "円§a§l丁半が始められました!§f§l[/mc]");
        onStartTimer();
        return true;
    }


    public void onStartTimer(){

        new BukkitRunnable(){
            @Override
            public void run() {
                if(setup == false){
                    cancel();
                    return;
                }
                //    セットアップがされていたら
                //          if (setup == true) {


                if (plugin.timer == 0){
                    if (chou.size() == 0|| han.size() == 0){
                        Bukkit.getServer().broadcastMessage(plugin.prefex +"§4§l人数が集まらなかったため中止しました");
                        plugin.gameclear();
                        cancel();
                        return;
                    }else {
                        Bukkit.broadcastMessage(plugin.prefex + "§e§lサイコロを振っています…  §f§l§k123");

                    }
                }
                //    ２秒間をあける
                else if (plugin.timer >= 0) {
                    if (plugin.timer % 10 == 0 || plugin.timer <= 5) {
                        Bukkit.getServer().broadcastMessage(plugin.prefex + "§aBET受付終了まであと§f§l" + plugin.timer + "秒");
                    }
                }
                //  ゲーム終了
                else if (plugin.timer == -3) {
                    cancel();
                    Game();
                    return;
                }
                plugin.timer--;
            }
        }.runTaskTimer(plugin,0,20);
    }

    public void Game(){

        /*OfflinePlayer[] chouplayer = new OfflinePlayer[chou.size()];
        OfflinePlayer[] hanplayer = new OfflinePlayer[han.size()];*/

        double payout;

        Random rdice = new Random();
        int dice = rdice.nextInt(6) + 1;
        String chouhan;

        if (dice % 2 == 0){
            chouhan = "丁";
            payout = plugin.totalbal / chou.size();
        }else {
            chouhan = "半";
            payout = plugin.totalbal / han.size();
        }

        if (chouhan == "丁") {
            Bukkit.broadcastMessage(plugin.prefex + "§f結果:§e§l" + dice +" §f§l/ §c§l" + chouhan + "の勝利！");
            for (int i = 0; i < chou.size(); i++) {
                OfflinePlayer p = Bukkit.getOfflinePlayer(chou.get(i));
                if (p == null){
                    Bukkit.getLogger().info(chou.get(i).toString()+"は見つからない");
                    continue;
                }

                plugin.val.deposit(chou.get(i), payout);

                Bukkit.broadcastMessage(plugin.prefex + "§c§l" + p.getName() + "さん§a§l勝利！ §f" + plugin.jpnBalForm(plugin.bal) + "円§f§l⇒§6§l" + plugin.jpnBalForm(payout) + "円");

            }

        }else {

            Bukkit.broadcastMessage(plugin.prefex + "§f結果: §e§l" + dice +" §f§l/ §b§l" + chouhan + "の勝利！");

            for (int i = 0; i < han.size(); i++) {
                OfflinePlayer p = Bukkit.getOfflinePlayer(han.get(i));

                if (p == null){
                    Bukkit.getLogger().info(han.get(i).toString()+"は見つからない");
                    continue;
                }

                plugin.val.deposit(han.get(i), payout);
                Bukkit.broadcastMessage(plugin.prefex + "§b§l" + p.getName() + "さん§a§l勝利！ §f" + plugin.jpnBalForm(plugin.bal) + "円§f§l⇒§6§l" + plugin.jpnBalForm(payout) + "円");

            }
        }
        plugin.gamefinish();
        return;
    }

}
