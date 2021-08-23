package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class EntityPlayerCreative extends EntityPlayerSP {
	public boolean flying = false;
    long space=0;
	public EntityPlayerCreative(Minecraft minecraft, World world, Session session, int i) {
		super(minecraft,world,session,i);
        field_9343_G=false;
	}
	public void damageEntity(int i) {} // 模拟创造模式不受伤害
	// 飞行部分
    public void func_460_a(int i, boolean flag) {
        super.func_460_a(i,flag);
        if(flag&&i==Keyboard.KEY_SPACE) {
            if(System.currentTimeMillis()-space<500L) {
                flying=!flying;
            } 
            space=System.currentTimeMillis();
        }
    }
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
    {
        super.writeEntityToNBT(nbttagcompound);
        nbttagcompound.setBoolean("Flying", flying); // 写入飞行状态
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        super.readEntityFromNBT(nbttagcompound);
        flying = nbttagcompound.getBoolean("Flying"); // 读取飞行状态
    }
    public void moveEntity(double x,double y,double z) {
        if (flying) {
            x*=1.5d;
            z*=1.5d;
        }
        super.moveEntity(x,y,z);
    }
	protected void fall(float f)
    {
    }

    public void func_435_b(float f, float f1)
    {
    	if(!flying) { // 如果没启用飞行模式，则调用父类
    		super.func_435_b(f,f1);
    		return;
    	}
        
    	// 没有电脑，请自行调整该参数
    	if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
    		motionY+=0.1d;
    	}
    	if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
    		motionY-=0.1d;
    	}
        if(handleWaterMovement())
        {
            func_351_a(f, f1, 0.02F);
            moveEntity(motionX, motionY, motionZ);
            motionX *= 0.80000001192092896D;
            motionY *= 0.80000001192092896D;
            motionZ *= 0.80000001192092896D;
        } else
        if(handleLavaMovement())
        {
            func_351_a(f, f1, 0.02F);
            moveEntity(motionX, motionY, motionZ);
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        } else
        {
            float f2 = 0.91F;
            if(onGround)
            {
                f2 = 0.5460001F;
                int i = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
                if(i > 0)
                {
                    f2 = Block.blocksList[i].slipperiness * 0.91F;
                }
            }
            float f3 = 0.1627714F / (f2 * f2 * f2);
            func_351_a(f, f1, onGround ? 0.1F * f3 : 0.02F);
            f2 = 0.91F;
            if(onGround)
            {
                f2 = 0.5460001F;
                int j = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
                if(j > 0)
                {
                    f2 = Block.blocksList[j].slipperiness * 0.91F;
                }
            }
            moveEntity(motionX, motionY, motionZ);
            motionX *= f2;
            motionY *= f2;
            motionZ *= f2;
        }
        field_705_Q = field_704_R;
        double d = posX - prevPosX;
        double d1 = posZ - prevPosZ;
        float f4 = MathHelper.sqrt_double(d * d + d1 * d1) * 4F;
        if(f4 > 1.0F)
        {
            f4 = 1.0F;
        }
        field_704_R += (f4 - field_704_R) * 0.4F;
        field_703_S += field_704_R;
    }

    public boolean isOnLadder()
    {
        return flying ? false : super.isOnLadder(); // 如果没启用飞行模式，则调用父类
    }
}