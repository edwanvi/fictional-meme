package me.itstheholyblack.testmodpleaseignore.network;

import me.itstheholyblack.testmodpleaseignore.Reference;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	private static final SimpleNetworkWrapper HANDLER = new SimpleNetworkWrapper(Reference.NETWORK_CHANNEL);
	public static void init() {
		int id = 0; 
		HANDLER.registerMessage(PacketJump.Handler.class, PacketJump.class, id++, Side.SERVER);
		HANDLER.registerMessage(MessageDataSync.Handler.class, MessageDataSync.class, id++, Side.CLIENT);
	}
	public static void sendToServer(IMessage msg) {
		HANDLER.sendToServer(msg);
	}
	public static void sendToPlayer(IMessage msg, EntityPlayerMP player) {
		HANDLER.sendTo(msg, player);
	}
}
