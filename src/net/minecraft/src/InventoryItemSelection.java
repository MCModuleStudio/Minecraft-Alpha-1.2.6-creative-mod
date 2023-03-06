package net.minecraft.src;

import java.util.Arrays;

import org.lwjgl.input.Keyboard;

public class InventoryItemSelection implements IInventory {
	public int offset = 0;
	public static final Item[] sortedItems;

	public int getSizeInventory() {
		return 54;
	}

	public ItemStack getStackInSlot(int i) {
		if (sortedItems[offset + i] == null) {
			return null;
		}
		return decrStackSize(i, Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? sortedItems[offset + i].getItemStackLimit() : 1);
	}

	public ItemStack decrStackSize(int i, int j) {
		if (sortedItems[offset + i] == null) {
			return null;
		}
		return new ItemStack(sortedItems[offset + i], j);
	}

	public void setInventorySlotContents(int i, ItemStack itemstack) {
	}

	public String getInvName() {
		return "Item Selection";
	}

	public int getInventoryStackLimit() {
		return 64;
	}

	public void onInventoryChanged() {
	}

	static {
		sortedItems = Arrays.copyOf(Item.itemsList, Item.itemsList.length);
		Arrays.sort(sortedItems, new ItemSorter());
	}
}