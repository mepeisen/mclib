package de.minigameslib.mclib.test.impl;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.objects.McPlayerInterface;
import de.minigameslib.mclib.api.objects.ObjectServiceInterface;

public class MyCommandHandler {
	
	public static boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		final McPlayerInterface player = ObjectServiceInterface.instance().getPlayer((Player) sender);
		try
		{
			player.openAnvilGui(new MyAnvilGui());
		}
		catch (McException ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

}
