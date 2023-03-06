package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class EntityPlayerCreative extends EntityPlayerSP {
	public boolean flying = false;
	long space = 0;

	public EntityPlayerCreative(Minecraft minecraft, World world, Session session, int i) {
		super(minecraft, world, session, i);
		field_9343_G = false;
	}

	public void damageEntity(int i) {
	}

	public void func_460_a(int i, boolean flag) {
		super.func_460_a(i, flag);
		if (flag && i == Keyboard.KEY_SPACE) {
			if (System.currentTimeMillis() - space < 500L) {
				flying = !flying;
			}
			space = System.currentTimeMillis();
		}
	}

	public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
		super.writeEntityToNBT(nbttagcompound);
		nbttagcompound.setBoolean("Flying", flying);
	}

	public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
		super.readEntityFromNBT(nbttagcompound);
		flying = nbttagcompound.getBoolean("Flying");
	}

	// Optinal 
	public void moveEntity(double x, double y, double z) {
		if(flying) {
			x *= 2.5d;
			z *= 2.5d;
			if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				x *= 2d;
				z *= 2d;
			}
		}
		super.moveEntity(x, y, z);
	}

	protected void fall(float f) {
	}

	public void func_435_b(float f, float f1) {
		if (!flying) { 
			super.func_435_b(f, f1);
			return;
		}

		if (this.field_787_a.field_1176_d) { // Jump
			motionY += 0.42d;
		}
		if (this.field_787_a.field_1175_e) { // Sneak
			motionY -= 0.42d;
		}
		if (handleWaterMovement()) {
			func_351_a(f, f1, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.80000001192092896D;
			motionY *= 0.80000001192092896D;
			motionZ *= 0.80000001192092896D;
		} else if (handleLavaMovement()) {
			func_351_a(f, f1, 0.02F);
			moveEntity(motionX, motionY, motionZ);
			motionX *= 0.5D;
			motionY *= 0.5D;
			motionZ *= 0.5D;
		} else {
			float f2 = 0.91F;
			if (onGround) {
				f2 = 0.5460001F;
				int i = worldObj.getBlockId(MathHelper.floor_double(posX),
						MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
				if (i > 0) {
					f2 = Block.blocksList[i].slipperiness * 0.91F;
				}
			}
			float f3 = 0.1627714F / (f2 * f2 * f2);
			func_351_a(f, f1, 0.1F * f3);
			f2 = 0.91F;
			if (onGround) {
				f2 = 0.5460001F;
				int j = worldObj.getBlockId(MathHelper.floor_double(posX),
						MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));
				if (j > 0) {
					f2 = Block.blocksList[j].slipperiness * 0.91F;
				}
			}
			moveEntity(motionX, motionY, motionZ);
			motionX *= f2;
			motionY *= 0.05;
			motionZ *= f2;
		}
		field_705_Q = field_704_R;
		double d = posX - prevPosX;
		double d1 = posZ - prevPosZ;
		float f4 = MathHelper.sqrt_double(d * d + d1 * d1) * 4F;
		if (f4 > 1.0F) {
			f4 = 1.0F;
		}
		field_704_R += (f4 - field_704_R) * 0.4F;
		field_703_S += field_704_R;
		
		flying &= !this.onGround;
	}

	public boolean isOnLadder() {
		return flying ? false : super.isOnLadder();
	}
}