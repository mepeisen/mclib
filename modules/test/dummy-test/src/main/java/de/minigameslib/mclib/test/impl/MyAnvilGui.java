package de.minigameslib.mclib.test.impl;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.AnvilGuiId;
import de.minigameslib.mclib.api.gui.AnvilGuiInterface;

final class MyAnvilGui implements AnvilGuiInterface {
	@Override
	public void onInput(String input) throws McException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AnvilGuiId getUniqueId() {
		return GuiIds.Sample;
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.NAME_TAG);
	}
}