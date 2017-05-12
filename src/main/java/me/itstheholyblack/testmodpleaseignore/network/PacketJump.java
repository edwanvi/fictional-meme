package me.itstheholyblack.testmodpleaseignore.network;

import io.netty.buffer.ByteBuf;
import me.itstheholyblack.testmodpleaseignore.items.armor.LeapingBoots;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Class to make the player jump (for double jumping things.)
 *
 * @author Vazkii
 */
public class PacketJump implements IMessage {
	// we don't need to define these
	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	// subclasses, joy
	public static class Handler implements IMessageHandler<PacketJump, IMessage> {
		// What is done when we recieve this message
		@Override
		public IMessage onMessage(PacketJump message, MessageContext ctx) {
			// Get the player so we can mess them up
			EntityPlayerMP player = ctx.getServerHandler().player;
			player.mcServer.addScheduledTask(() -> {
				if (player.inventory.armorItemInSlot(0) != null
						&& player.inventory.armorItemInSlot(0).getItem() instanceof LeapingBoots) {
					player.addExhaustion(0.3F);
					player.fallDistance = 0;

					// if(belt != null && belt.getItem() instanceof
					// ItemTravelBelt)
					player.fallDistance = -((LeapingBoots) player.inventory.armorItemInSlot(0).getItem())
							.getMaxAllowedJumps();
				}
			});
			return null;
		}
	}
}
