package com.bukkit.timberjaw.spawncontrol;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

import com.nijikokun.bukkit.Permissions.Permissions;

/**
 * Handle events for all Player related events
 * @author Timberjaw
 */
public class SCPlayerListener extends PlayerListener {
    private final SpawnControl plugin;
    

    public SCPlayerListener(SpawnControl instance) {
        plugin = instance;
    }

    public void onPlayerCommand(PlayerChatEvent e)
    {
    	// Split the command in case it has parameters
    	String[] cmd = e.getMessage().split(" ");
    	
    	// Sethome
    	if(plugin.getSetting("enable_home") == SpawnControl.Settings.YES && cmd[0].equalsIgnoreCase("/sethome"))
    	{
    		String setter = e.getPlayer().getName();
    		String homeowner = setter;
    		Location l = e.getPlayer().getLocation();
    		
    		if(cmd.length > 1 && !Permissions.Security.permission(e.getPlayer(), "SpawnControl.sethome.proxy"))
    		{
    			// User is trying to set home for another user but they don't have permission
    			e.getPlayer().sendMessage("You don't have permission to do that.");
    		}
    		else if(!Permissions.Security.permission(e.getPlayer(), "SpawnControl.sethome.basic"))
    		{
    			// User is trying to set home but they don't have permission
    			e.getPlayer().sendMessage("You don't have permission to do that.");
    		}
    		else
    		{
    			if(cmd.length > 1)
    			{
    				// Setting home for different player
    				homeowner = cmd[1];
    			}
    			
	    		if(plugin.setHome(homeowner, l, setter))
	    		{
	    			e.getPlayer().sendMessage("Home set successfully!");
	    		}
	    		else
	    		{
	    			e.getPlayer().sendMessage("Could not set Home!");
	    		}
    		}
    		
    		e.setCancelled(true);
    	}
    	
    	// Home
    	if(plugin.getSetting("enable_home") == SpawnControl.Settings.YES && cmd[0].equalsIgnoreCase("/home"))
    	{
    		// Send player home
    		if(!Permissions.Security.permission(e.getPlayer(), "SpawnControl.home.basic"))
    		{
    			// User doesn't have access to this command
    			e.getPlayer().sendMessage("You don't have permission to do that.");
    		}
    		else
    		{
	    		SpawnControl.log.info("[SpawnControl] Attempting to send player "+e.getPlayer().getName()+" to home.");
	        	plugin.sendHome(e.getPlayer());
    		}
        	e.setCancelled(true);
    	}
    	
    	// Spawn (globalspawn)
    	if(plugin.getSetting("enable_globalspawn") == SpawnControl.Settings.YES && (cmd[0].equalsIgnoreCase("/spawn") || cmd[0].equalsIgnoreCase("/globalspawn")))
    	{
    		// Send player to spawn
    		if(!Permissions.Security.permission(e.getPlayer(), "SpawnControl.spawn.use"))
    		{
    			// User doesn't have access to this command
    			e.getPlayer().sendMessage("You don't have permission to do that.");
    		}
    		else
    		{
	    		SpawnControl.log.info("[SpawnControl] Attempting to send player "+e.getPlayer().getName()+" to spawn.");
	        	plugin.sendToSpawn(e.getPlayer());
    		}
        	e.setCancelled(true);
    	}
    	
    	// Set spawn (globalspawn)
    	if(plugin.getSetting("enable_globalspawn") == SpawnControl.Settings.YES && (cmd[0].equalsIgnoreCase("/setspawn") || cmd[0].equalsIgnoreCase("/setglobalspawn")))
    	{
    		// Set global spawn
    		if(!Permissions.Security.permission(e.getPlayer(), "SpawnControl.spawn.set"))
    		{
    			// User doesn't have access to this command
    			e.getPlayer().sendMessage("You don't have permission to do that.");
    		}
    		else
    		{
	    		SpawnControl.log.info("[SpawnControl] Attempting to set global spawn.");
	        	if(plugin.setSpawn(e.getPlayer().getLocation(), e.getPlayer().getName()))
	        	{
	        		e.getPlayer().sendMessage("Global spawn set successfully!");
	        	}
	        	else
	        	{
	        		e.getPlayer().sendMessage("Could not set global spawn.");
	        	}
    		}
        	e.setCancelled(true);
    	}
    	
    	// Setgroupspawn
    	if(plugin.getSetting("enable_groupspawn") == SpawnControl.Settings.YES && (cmd[0].equalsIgnoreCase("/setgroupspawn") || cmd[0].equalsIgnoreCase("/setgroupspawn")))
    	{
    		String group = null;
    		
    		// Set group spawn
    		if(!Permissions.Security.permission(e.getPlayer(), "SpawnControl.groupspawn.set"))
    		{
    			// User doesn't have access to this command
    			e.getPlayer().sendMessage("You don't have permission to do that.");
    		}
    		else if(!(cmd.length > 1))
    		{
    			// User didn't specify a group
    			e.getPlayer().sendMessage("Command format: /setgroupspawn [group]");
    		}
    		else
    		{
    			group = cmd[1];
	    		SpawnControl.log.info("[SpawnControl] Setting group spawn for '"+group+"'.");
	        	if(plugin.setGroupSpawn(group, e.getPlayer().getLocation(), e.getPlayer().getName()))
	        	{
	        		e.getPlayer().sendMessage("Group spawn for "+group+" set successfully!");
	        	}
	        	else
	        	{
	        		e.getPlayer().sendMessage("Could not set group spawn for "+group+".");
	        	}
    		}
        	e.setCancelled(true);
    	}
    	
    	// Groupspawn
    	if(plugin.getSetting("enable_groupspawn") == SpawnControl.Settings.YES && (cmd[0].equalsIgnoreCase("/groupspawn") || cmd[0].equalsIgnoreCase("/groupspawn")))
    	{
    		// Send player to group spawn
    		if(!Permissions.Security.permission(e.getPlayer(), "SpawnControl.groupspawn.use"))
    		{
    			// User doesn't have access to this command
    			e.getPlayer().sendMessage("You don't have permission to do that.");
    		}
    		else
    		{
    			// Get group spawn for player
    			String group = Permissions.Security.getGroup(e.getPlayer().getName());
	    		SpawnControl.log.info("[SpawnControl] Attempting to send player "+e.getPlayer().getName()+" to group spawn.");
	        	plugin.sendToGroupSpawn(group, e.getPlayer());
    		}
        	e.setCancelled(true);
    	}
    	
    	// Check settings
    	
    	// Set setting
    	if(cmd[0].equalsIgnoreCase("/sc_config") && Permissions.Security.permission(e.getPlayer(), "SpawnControl.config"))
    	{
    		if(cmd.length < 3)
    		{
    			// Command format is wrong
    			e.getPlayer().sendMessage("Command format: /sc_config [setting] [value]");
    		}
    		else
    		{
	    		// Verify setting
	    		if(plugin.getSetting(cmd[1]) < 0)
	    		{
	    			// Bad setting key
	    			e.getPlayer().sendMessage("Unknown configuration value.");
	    		}
	    		else
	    		{
	    			// Parse value
	    			try
	    			{
	    				int tmpval = Integer.parseInt(cmd[2]);
	    				
	    				if(tmpval < 0)
	    				{
	    					e.getPlayer().sendMessage("Value must be >= 0.");
	    				}
	    				else
	    				{
	    					// Save
	    					if(!plugin.setSetting(cmd[1], tmpval))
	    					{
	    						e.getPlayer().sendMessage("Could not save value for '"+cmd[1]+"'!");
	    					}
	    					else
	    					{
	    						e.getPlayer().sendMessage("Saved value for '"+cmd[1]+"'.");
	    					}
	    				}
	    			}
	    			catch(Exception ex)
	    			{
	    				// Bad number
	    				e.getPlayer().sendMessage("Couldn't read value.");
	    			}
	    		}
    		}
    		e.setCancelled(true);
    	}
    	
    	// Import config
    	if(cmd[0].equalsIgnoreCase("/scimportconfig") && Permissions.Security.permission(e.getPlayer(), "SpawnControl.import"))
    	{
    		SpawnControl.log.info("[SpawnControl] Attempting to import player configuration file.");
    		plugin.importConfig();
    		e.setCancelled(true);
    	}
    	
    	// Import group config
    	if(cmd[0].equalsIgnoreCase("/scimportgroupconfig") && Permissions.Security.permission(e.getPlayer(), "SpawnControl.import"))
    	{
    		SpawnControl.log.info("[SpawnControl] Attempting to import group configuration file.");
    		plugin.importGroupConfig();
    		e.setCancelled(true);
    	}
    }
    
    public void onPlayerJoin(PlayerEvent e)
    {
    	SpawnControl.log.info("[SpawnControl] Player " + e.getPlayer().getName() + " joined with " + e.getPlayer().getHealth() + " health.");
    	if(Permissions.Security.getGroup(e.getPlayer().getName()).equalsIgnoreCase("default") && plugin.getHome(e.getPlayer().getName()) == null)
    	{
    		// Probably a new player
    		SpawnControl.log.info("[SpawnControl] Sending new player " + e.getPlayer().getName() + " to global spawn.");
    		
    		// Send player to global spawn
    		plugin.sendToSpawn(e.getPlayer());
    		
    		// Set home for player
    		plugin.setHome(e.getPlayer().getName(), plugin.getSpawn(), "SpawnControl");
    	}
    	
    	int jb = plugin.getSetting("behavior_join");
    	if(jb != SpawnControl.Settings.JOIN_NONE)
    	{
	    	// Get player
	    	Player p = e.getPlayer();
	    	
	    	// Check for home
	    	SpawnControl.log.info("[SpawnControl] Attempting to respawn player "+p.getName()+" (joining).");
	    	
	    	switch(jb)
	    	{
	    		case SpawnControl.Settings.JOIN_HOME:
	    			plugin.sendHome(p);
	    			break;
	    		case SpawnControl.Settings.JOIN_GROUPSPAWN:
	    			plugin.sendToGroupSpawn(Permissions.Security.getGroup(p.getName()), p);
	    			break;
	    		case SpawnControl.Settings.JOIN_GLOBALSPAWN:
	    		default:
	    			plugin.sendToSpawn(p);
	    			break;
	    	}
    	}
    }
}