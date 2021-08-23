package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Mouse;

public class GuiInventoryCreative extends GuiInventory {
	InventorySelect inv1;
	IInventory inv2;
	public GuiInventoryCreative(IInventory inv) {
		super(inv,new ItemStack[4]);
		inv1=new InventorySelect();
		inv2=inv;
		inventorySlots.clear();
		xSize=198;
		ySize=222;
		int j = 37;
		for(int l = 0; l < 3; l++)
        {
            for(int k1 = 0; k1 < 9; k1++)
            {
                inventorySlots.add(new SlotInventory(this, inv, k1 + (l + 1) * 9, 8 + k1 * 18+22, 103 + l * 18 + j));
            }

        }

        for(int i1 = 0; i1 < 9; i1++)
        {
            inventorySlots.add(new SlotInventory(this, inv, i1, 8 + i1 * 18+22, 161 + j));
        }
        for(int k = 0; k < 4; k++)
        {
            int j1 = k;
            inventorySlots.add(new SlotArmor(this, this, inv, inv.getSizeInventory() - 1 - k, 8, (k+5) * 18, j1));
        }
        for(int k = 0; k < 6; k++)
        {
            for(int j1 = 0; j1 < 9; j1++)
            {
                inventorySlots.add(new SlotInventory(this, inv1, j1 + k * 9, 8 + j1 * 18+22, 18 + k * 18));
            }

        }
	}
	protected void drawGuiContainerForegroundLayer()
    {
    	fontRenderer.drawString(inv1.getInvName(), 30, 6, 0x404040);
        fontRenderer.drawString(inv2.getInvName(), 30, (ySize - 96) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/gui/creative.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }
    public void handleMouseInput() {
    	super.handleMouseInput();
    	int wheel = Mouse.getDWheel();
    	if (wheel==0) return;
    	int offset = wheel < 0 ? 9 : -9;
    	if(inv1.offset+offset>=0&&inv1.sortedItems[inv1.offset+offset]!=null) inv1.offset+=offset;
    }
}