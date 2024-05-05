package fr.inovancy.main;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R1.block.CraftBlock;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Command implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command cmd, @NotNull String msg, @NotNull String[] args) {
        if(cmd.getName().equalsIgnoreCase("utprenderer")){
            if (sender.hasPermission("utp.manage.map")) {
                if (sender instanceof Player) {
                    Player p = (Player) sender;
                    try {
                        World world = UTPMapRenderer.getInstance().getWorldEdit().getSession(p).getSelectionWorld();
                        if (world == null) {
                            p.sendMessage("First select both ends of your map with WorldEdit.");
                            return false;
                        }
                        Region region = UTPMapRenderer.getInstance().getWorldEdit().getSession(p).getSelection(world);
                        int Xa = region.getMinimumPoint().getBlockX();
                        int Ya = region.getMinimumPoint().getBlockZ();

                        int Xb = region.getMaximumPoint().getBlockX();
                        int Yb = region.getMaximumPoint().getBlockZ();
                        p.sendMessage("Generation in progress, please wait...");

                        BufferedImage bufferedImage = new BufferedImage(region.getWidth(), region.getLength(), 1);
                        Graphics2D graphics2D = bufferedImage.createGraphics();
                        for (int x = Xa; x < Xb; x++) {
                            for (int z = Ya; z < Yb; z++) {
                                Location loc = new Location(p.getWorld(), x, (p.getLocation().getBlockY() - 1), z);
                                Block block = p.getWorld().getHighestBlockAt(loc);
                                Color c = new Color((((CraftBlock) block).getNMS().b().s()).ak);
                                graphics2D.setColor(c);
                                graphics2D.fillRect(Xb - x, Yb - z, 1, 1);
                            }
                        }
                        graphics2D.dispose();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HHmmss");
                        LocalDateTime now = LocalDateTime.now();
                        String name = formatter.format(now);
                        File file = new File(UTPMapRenderer.getInstance().getDataFolder(), "map-"+name+".png");
                        UTPMapRenderer.getInstance().saveConfig();
                        ImageIO.write(bufferedImage, "png", file);
                        p.sendMessage("The map was successfully generated.");
                        p.sendMessage("§7- Xa: " + Xa + " | Ya: " + Ya);
                        p.sendMessage("§7- Xb: " + Xb + " | Yb: " + Yb);
                        return true;
                    } catch (IncompleteRegionException | IOException e) {
                        sender.sendMessage("An error has occurred during map generation.");
                        return false;
                    }
                } else {
                    sender.sendMessage("Only a player can run this command.");
                    return false;
                }
            } else {
                sender.sendMessage("You do not have permission to execute this command.");
                return false;
            }
        }
        return false;
    }
}
