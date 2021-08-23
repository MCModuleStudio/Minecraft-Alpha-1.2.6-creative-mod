package net.minecraft.src;
// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

import java.util.List;
import net.minecraft.client.Minecraft;

public class PlayerControllerTest extends PlayerController
{
    int field_1068_i=0;
    public PlayerControllerTest(Minecraft minecraft)
    {
        super(minecraft);
        field_1064_b = false; // 瞬间破坏方块，notch把这部分的代码删除了
    }

    public void func_6473_b(EntityPlayer entityplayer)
    {
        for(int i = 0; i < 9; i++)
        {
            if(entityplayer.inventory.mainInventory[i] == null)
            {
                mc.thePlayer.inventory.mainInventory[i] = new ItemStack(((Block)Session.registeredBlocksList.get(i)).blockID);
            } else
            {
                mc.thePlayer.inventory.mainInventory[i].stackSize = 1;
            }
        }

    }

    public void sendBlockRemoving(int i, int j, int k, int l)
    {
        if(field_1068_i > 0)
        {
            field_1068_i--;
            return;
        }
        sendBlockRemoved(i, j, k, l);
        field_1068_i = 5;
    }

    public boolean func_6469_d()
    {
        return false;
    }

    public void func_717_a(World world)
    {
        super.func_717_a(world);
    }

    public void func_6474_c()
    {
    }
    public EntityPlayer func_4087_b(World world)
    {
        return new EntityPlayerCreative(mc, world, mc.field_6320_i, world.worldProvider.field_4218_e);
    }
}
