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
package de.uniluebeck.itm.netty.handlerstack.isenseotap;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniluebeck.itm.netty.handlerstack.isenseotap.generatedmessages.MacroFabricSerializer;
import de.uniluebeck.itm.netty.handlerstack.isenseotap.generatedmessages.OtapInitReply;
import de.uniluebeck.itm.netty.handlerstack.isenseotap.generatedmessages.OtapInitRequest;
import de.uniluebeck.itm.netty.handlerstack.isenseotap.generatedmessages.OtapProgramReply;
import de.uniluebeck.itm.netty.handlerstack.isenseotap.generatedmessages.OtapProgramRequest;
import de.uniluebeck.itm.netty.handlerstack.isenseotap.generatedmessages.PresenceDetectReply;
import de.uniluebeck.itm.netty.handlerstack.isenseotap.generatedmessages.PresenceDetectRequest;

public class ISenseOtapPacketEncoder extends OneToOneEncoder {

    private static final Logger log = LoggerFactory.getLogger(ISenseOtapPacketEncoder.class);

    /**
     * Package-private constructor for creation via factory only
     */
    ISenseOtapPacketEncoder() {
    }

    @Override
    protected Object encode(final ChannelHandlerContext ctx, final Channel channel, final Object msg) throws Exception {
        byte[] bytes = null;

        if (msg instanceof OtapInitReply)
            bytes = MacroFabricSerializer.serialize((OtapInitReply) msg);
        else if (msg instanceof OtapInitRequest)
            bytes = MacroFabricSerializer.serialize((OtapInitRequest) msg);
        else if (msg instanceof OtapProgramReply)
            bytes = MacroFabricSerializer.serialize((OtapProgramReply) msg);
        else if (msg instanceof OtapProgramRequest)
            bytes = MacroFabricSerializer.serialize((OtapProgramRequest) msg);
        else if (msg instanceof PresenceDetectRequest)
            bytes = MacroFabricSerializer.serialize((PresenceDetectRequest) msg);
        else if (msg instanceof PresenceDetectReply)
            bytes = MacroFabricSerializer.serialize((PresenceDetectReply) msg);

        if (bytes != null) {
            ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(bytes);
            log.trace("[{}] Encoded PresenceDetectRequestPacket: {}", ctx.getName(), buffer);
            return buffer;

        } else {
            log.warn("Could not encode packet {}, returning it.", msg);
            return msg;
        }

    }
}
