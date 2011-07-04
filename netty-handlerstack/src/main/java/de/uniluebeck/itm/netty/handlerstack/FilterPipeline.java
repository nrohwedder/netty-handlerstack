package de.uniluebeck.itm.netty.handlerstack;

import de.uniluebeck.itm.tr.util.Tuple;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandler;

import java.net.SocketAddress;
import java.util.List;

public interface FilterPipeline {

	public interface DownstreamOutputListener {
		void receiveDownstreamOutput(ChannelBuffer message, SocketAddress targetAddress);
	}

	public interface UpstreamOutputListener {
		void receiveUpstreamOutput(ChannelBuffer message, SocketAddress sourceAddress);
	}

	void sendDownstream(ChannelBuffer message, SocketAddress targetAddress);

	void sendUpstream(ChannelBuffer message, SocketAddress sourceAddress);

	void setChannelPipeline(List<Tuple<String, ChannelHandler>> channelPipeline);

	void addListener(final DownstreamOutputListener listener);

	void addListener(final UpstreamOutputListener listener);

	void removeListener(final DownstreamOutputListener listener);

	void removeListener(final UpstreamOutputListener listener);

}