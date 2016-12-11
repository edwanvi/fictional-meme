package me.itstheholyblack.testmodpleaseignore.network;

import io.netty.buffer.ByteBuf;
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
		// TODO Auto-generated method stub
		toSend = buf.readDouble();
	}
	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		buf.writeDouble(toSend);
	}
	public static class Handler implements IMessageHandler<MessageDataSync, IMessage> {
		@Override
		public IMessage onMessage(MessageDataSync message, MessageContext ctx) {
			// sync from server -> client
			if (ctx != null && ctx.side.isClient()) {
				
			}
			return null;
		}	
	}
}
