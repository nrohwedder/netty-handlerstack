/**
 * Copyright (c) 2010, Daniel Bimschas and Dennis Pfisterer, Institute of Telematics, University of Luebeck
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.coalesenses.isense.ishell.interpreter;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.LoggerFactory;

import de.uniluebeck.itm.netty.handlerstack.isense.ISensePacket;
import de.uniluebeck.itm.netty.handlerstack.isense.ISensePacketType;
import de.uniluebeck.itm.netty.handlerstack.util.HandlerTools;

public class IShellInterpreterHandler extends SimpleChannelHandler {
    private final org.slf4j.Logger log;

    private ChannelHandlerContext context;

    public IShellInterpreterHandler() {
        this(null);
    }

    public IShellInterpreterHandler(String instanceName) {
        log = LoggerFactory.getLogger(instanceName != null ? instanceName : IShellInterpreterHandler.class.getName());
    }

    @Override
    public void channelDisconnected(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
        context = null;
        super.channelDisconnected(ctx, e);
    }

    @Override
    public void channelConnected(final ChannelHandlerContext ctx, final ChannelStateEvent e) throws Exception {
        context = ctx;
        super.channelConnected(ctx, e);
    }

    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        if (e.getMessage() instanceof IShellInterpreterSetChannelMessage) {
            IShellInterpreterSetChannelMessage msg = (IShellInterpreterSetChannelMessage) e.getMessage();
            setChannel(msg.getChannel());
        } else {
            super.writeRequested(ctx, e);
        }

    }

    public void setChannel(byte channelNumber) {
        if (this.context == null) {
            throw new RuntimeException("Channel not yet connected");
        }

        ChannelBuffer buffer = ChannelBuffers.buffer(3);
        buffer.writeByte(ISensePacketType.CODE.getValue());
        buffer.writeByte(IShellInterpreterPacketTypes.COMMAND_SET_CHANNEL.getValue());
        buffer.writeByte(channelNumber);

        log.debug("Setting channel to {}", channelNumber);
        HandlerTools.sendDownstream(new ISensePacket(buffer), context);
    }

}
