package com.tiza.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * Description: ServerHandler
 * Author: DIYILIU
 * Update: 2016-03-22 16:34
 */

public class ServerHandler extends ChannelInboundHandlerAdapter{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String key = ctx.channel().remoteAddress().toString().trim().replaceFirst("/", "");

        logger.info("[{}]建立连接...", key);

        ctx.channel().closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isDone()) {
                    logger.info("[{}]关闭连接...", key);
                }
            }
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;

        int length = buf.readUnsignedShort();
        int cmd = buf.readByte();

        if (cmd != 0x01){
            return;
        }

        byte[] content = new byte[length - 3];
        buf.readBytes(content);

        String sql = new String(content, Charset.forName("UTF-8"));

        logger.info("收到消息: {}", sql);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        String key = ctx.channel().remoteAddress().toString().trim().replaceFirst("/", "");

        // 心跳处理
        if (evt instanceof IdleStateEvent) {

            IdleStateEvent event = (IdleStateEvent) evt;

            if (IdleState.READER_IDLE == event.state()) {
                logger.info("读超时...[{}], 断开连接！", key);
                // 超时断开
                ctx.close();
            } else if (IdleState.WRITER_IDLE == event.state()) {
                //logger.warn("写超时...");

            } else if (IdleState.ALL_IDLE == event.state()) {
                //logger.warn("读/写超时...");
            }
        }
    }
}
