package de.uniluebeck.itm.tlspeerverficication;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.ssl.SslHandler;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class TlsPeerVerificationHandler extends SimpleChannelUpstreamHandler {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(TlsPeerVerificationHandler.class);

	public TlsPeerVerificationHandler() {
	}

	@Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        log.debug("Connected with {}, starting handshake", e.getChannel().getRemoteAddress());

        // Get the SslHandler from the pipeline
        // which were added in SecureChatPipelineFactory.
        SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);
        Preconditions.checkNotNull(sslHandler, "No SSL handler in the current pipeline");

        // Begin handshake
        sslHandler.handshake().addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.debug("Handshake complete @ {} with remote {}", future.getChannel().getLocalAddress(), future
                            .getChannel().getRemoteAddress());

                    // future.getChannel().write("lkjlkjl");
                } else {
                    log.debug("Handshake failed @ {} with remote {} ({}): {}", new Object[] { future.getChannel().getLocalAddress(), future
                            .getChannel().getRemoteAddress(), future.getCause(), future.getCause().getMessage() });
                    
                }
            }
        });

        super.channelConnected(ctx, e);
    }

}
