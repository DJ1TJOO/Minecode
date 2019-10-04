package me.DJ1TJOO.minecode;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class Config {
	 
    Config() { }
   
    static Config instance = new Config();
   
    public static Config getInstance() {
        return instance;
    }
   
    Plugin p;
   
    FileConfiguration config;
    File cfile;
   
    public void setup(Plugin p) {
            cfile = new File(p.getDataFolder(), "config.yml");
            config = p.getConfig();
           
            if (!p.getDataFolder().exists()) {
                    p.getDataFolder().mkdir();
            }
    }
   
    public PluginDescriptionFile getDesc() {
            return p.getDescription();
    }
    
    //config
	    public FileConfiguration getConfig() {
	        return config;
		}
		
		public void saveConfig() {
		        try {
		                config.save(cfile);
		        }
		        catch (IOException e) {
		                Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save config.yml!");
		        }
		}
		
		public void reloadConfig() {
		        config = YamlConfiguration.loadConfiguration(cfile);
		}
	//end

}
