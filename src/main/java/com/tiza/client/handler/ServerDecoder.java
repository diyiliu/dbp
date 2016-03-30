package com.tiza.client.handler;

import com.tiza.util.Common;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Description: ServerDecoder
 * Author: DIYILIU
 * Update: 2016-03-22 16:41
 */

public class ServerDecoder extends ByteToMessageDecoder {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (in.readableBytes() < 3) {
            return;
        }

        in.markReaderIndex();

        int length = in.readUnsignedShort();
        int cmd = in.readByte();

        if (cmd == 0x00) {
            //logger.info("收到心跳...");
            // 心跳内容为空
        } else if (cmd == 0x01) {

            if (in.readableBytes() < length - 3) {
                in.resetReaderIndex();
                return;
            }

            in.resetReaderIndex();

            byte[] bytes = new byte[length];
            in.readBytes(bytes);

            out.add(Unpooled.copiedBuffer(bytes));
        } else {
            logger.error("命令异常：[{}]", Common.toHex(cmd));
            ctx.close();
        }
    }
}
