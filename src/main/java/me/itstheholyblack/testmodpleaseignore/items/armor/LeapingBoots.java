package me.itstheholyblack.testmodpleaseignore.items.armor;

import java.util.UUID;

import me.itstheholyblack.testmodpleaseignore.Reference;
import me.itstheholyblack.testmodpleaseignore.network.PacketHandler;
import me.itstheholyblack.testmodpleaseignore.network.PacketJump;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LeapingBoots extends ItemArmor {
	
	private static int timesJumped;
	private static boolean jumpDown;
	
	public LeapingBoots(EntityEquipmentSlot type, String name) {
		this(type, name, ArmorTypes.ENDER_CLOTH_MAT);
	}
	public LeapingBoots(EntityEquipmentSlot type, String name, ArmorMaterial mat){
		super(mat, 0, type);
		setUnlocalizedName(Reference.MODID + "." + "leapingboots");
		setRegistryName("leapingboots");
		GameRegistry.register(this);
	}
	@Override
	// @SideOnly(Side.CLIENT)
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		if(player instanceof EntityPlayerSP && player == Minecraft.getMinecraft().thePlayer) {
			EntityPlayerSP playerSp = (EntityPlayerSP) player;
			UUID uuid = playerSp.getUniqueID();

			if(playerSp.onGround)
				timesJumped = 0;
			else {
				if(playerSp.movementInput.jump) {
					if(!jumpDown && timesJumped < getMaxAllowedJumps()) {
						System.out.println("Double Jump!");
						playerSp.jump();
						PacketHandler.sendToServer(new PacketJump());
						timesJumped++;
					}
					jumpDown = true;
				} else jumpDown = false;
			}
		}
	}
	public int getMaxAllowedJumps() {
		return 5;
	}
}
