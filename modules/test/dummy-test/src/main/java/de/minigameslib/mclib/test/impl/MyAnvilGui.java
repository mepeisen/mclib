package de.minigameslib.mclib.test.impl;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.minigameslib.mclib.api.McException;
import de.minigameslib.mclib.api.gui.AnvilGuiId;
import de.minigameslib.mclib.api.gui.AnvilGuiInterface;
import de.minigameslib.mclib.api.util.function.McConsumer;

final class MyAnvilGui implements AnvilGuiInterface {
    
    McConsumer<String> consumer;
    
	/**
     * @param consumer
     */
    public MyAnvilGui(McConsumer<String> consumer)
    {
        super();
        this.consumer = consumer;
    }

    @Override
	public void onInput(String input) throws McException {
		this.consumer.accept(input);
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
		final ItemStack stack = new ItemStack(Material.NAME_TAG);
		final ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§1FOO§2BAR");
		meta.setLore(Arrays.asList("FOO", "BAR"));
		stack.setItemMeta(meta);
		return stack;
	}
}