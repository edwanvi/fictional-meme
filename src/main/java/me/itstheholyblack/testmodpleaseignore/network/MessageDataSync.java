package me.itstheholyblack.testmodpleaseignore.network;

import io.netty.buffer.ByteBuf;
import me.itstheholyblack.testmodpleaseignore.core.PlayerDataMan;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageDataSync implements IMessage {
	// A default constructor is always required
	public MessageDataSync(){}

	private double toSend;
	public MessageDataSync(double toSend) {
	    this.toSend = toSend;
	}
	@Override
	public void fromBytes(ByteBuf buf) {
		toSend = buf.readDouble();
	}
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(toSend);
	}
	public static class Handler implements IMessageHandler<MessageDataSync, IMessage> {
		@Override
		public IMessage onMessage(MessageDataSync message, MessageContext ctx) {
			// sync from server -> client
			if (ctx != null && ctx.side.isClient()) {
				EntityPlayerSP player = Minecraft.getMinecraft().player;
				NBTTagCompound data = player.getEntityData();
				NBTTagCompound persist = data.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
				double value = message.toSend;
				persist.setDouble(PlayerDataMan.ManaPool, value);
			}
			return null;
		}	
	}
}
