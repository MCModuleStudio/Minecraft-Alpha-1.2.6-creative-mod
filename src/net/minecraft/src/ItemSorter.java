package net.minecraft.src;

import java.util.Comparator;

public class ItemSorter implements Comparator<Item> {
	public int compare(Item i,Item j) {
		if (i==null&&j==null) {
			return 0;
		}
		if (i==null&&j!=null) {
			return 1;
		}
		if (i!=null&&j==null) {
			return -1;
		}
		return j.shiftedIndex<i.shiftedIndex ? 1 : -1;
	}
}