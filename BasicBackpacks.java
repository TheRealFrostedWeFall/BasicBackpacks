package FrostedWeFall.BasicBackpacks;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;
 
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import FrostedWeFall.BasicBackpacks.BasicBackpacks;

public class BasicBackpacks extends JavaPlugin implements Listener {
       
        private HashMap<UUID, Inventory> BasicBackpacks = new HashMap<UUID, Inventory>();
       
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e) {
                Inventory inv = Bukkit.getServer().createInventory(e.getPlayer(), InventoryType.CHEST, "Your Backpack");
               
                if (getConfig().contains("BasicBackpacks." + e.getPlayer().getUniqueId())) {
                        for (String item : getConfig().getConfigurationSection("BasicBackpacks." + e.getPlayer().getUniqueId()).getKeys(false)) {
                                inv.addItem(loadItem(getConfig().getConfigurationSection("BasicBackpacks." + e.getPlayer().getUniqueId() + "." + item)));
                        }
                }
               
                BasicBackpacks.put(e.getPlayer().getUniqueId(), inv);
        }
       
        @EventHandler
        public void onPlayerLeave(PlayerQuitEvent e) {
                if (!getConfig().contains("BasicBackpacks." + e.getPlayer().getUniqueId())) {
                        getConfig().createSection("backpacks." + e.getPlayer().getUniqueId());
                }
               
                char c = 'a';
                for (ItemStack itemStack : BasicBackpacks.get(e.getPlayer().getUniqueId())) {
                        if (itemStack != null) {
                                saveItem(getConfig().createSection("BasicBackpacks." + e.getPlayer().getUniqueId() + "." + c++), itemStack);
                        }
                }
               
                saveConfig();
        }
       
        public void onEnable() {
                Bukkit.getServer().getPluginManager().registerEvents(this, this);
                getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\nBasicBackpacks V0.4 Has been succesfully enabled!\n\n");
        }
       
        public void onDisable() {
        	getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\nBasicBackpacks V0.4 Has been succesfully disabled!\n\n");
                for (Entry<UUID, Inventory> entry : BasicBackpacks.entrySet()) {
                        if (!getConfig().contains("BasicBackpacks." + entry.getKey())) {
                                getConfig().createSection("BasicBackpacks." + entry.getKey());
                        }
                       
                        char c = 'a';
                        for (ItemStack itemStack : entry.getValue()) {
                                if (itemStack != null) {
                                        saveItem(getConfig().createSection("BasicBackpacks." + entry.getKey() + "." + c++), itemStack);
                                }
                        }
                       
                        saveConfig();
                }
        }
       
        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
                if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "The console cannot have an backpack, you must be a player to activate this command");
                        return true;
                }
               
                Player p = (Player) sender;
               
                if (cmd.getName().equalsIgnoreCase("backpack")) {
                        p.openInventory(BasicBackpacks.get(p.getUniqueId()));
                        p.sendMessage(ChatColor.GRAY + "Opening your personal backpack!");
                }
                if (cmd.getName().equalsIgnoreCase("bbp")) {
                		p.sendMessage(ChatColor.GRAY + "BasicBackpacksBETA Created By FrostedWeFall");
                		p.sendMessage(ChatColor.GRAY + "Please report any issues at https://github.com/TheRealFrostedWeFall/BasicBackpacks/issues");
                }
                if (cmd.getName().equalsIgnoreCase("bbhelp")) {
                		p.sendMessage(ChatColor.GRAY + "-----+--------------------+-----");
                		p.sendMessage(ChatColor.AQUA + "BasicBackpacks V0.4 BETA By FrostedWeFall");
                		p.sendMessage(ChatColor.GRAY + "/backpack -" + ChatColor.AQUA + " Opens your personal backpack!");
                		p.sendMessage(ChatColor.GRAY + "/bbp -" + ChatColor.AQUA + " Info menu about the BasicBackpacks plugin!");
                		p.sendMessage(ChatColor.GRAY + "-----+--------------------+-----");
                }
                return true;
                	
        }
       
        private void saveItem(ConfigurationSection section, ItemStack itemStack) {
                section.set("type", itemStack.getType().name());
                section.set("amount", itemStack.getAmount());
                // Save more information.
        }
       
        private ItemStack loadItem(ConfigurationSection section) {
                return new ItemStack(Material.valueOf(section.getString("type")), section.getInt("amount"));
                // Load more information.
        }
}
        
        
        
