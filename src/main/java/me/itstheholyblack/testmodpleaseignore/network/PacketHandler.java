package me.itstheholyblack.testmodpleaseignore.network;

import me.itstheholyblack.testmodpleaseignore.Reference;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketHandler {
	private static final SimpleNetworkWrapper HANDLER = new SimpleNetworkWrapper(Reference.NETWORK_CHANNEL);
	public static void sendToServer(IMessage msg) {
		HANDLER.sendToServer(msg);
	}
}
