package com.tiza.client.handler;

import com.tiza.db.DealInsertSQL;
import com.tiza.db.DealUpdateSQL;
import com.tiza.util.config.Constant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * Description: ServerHandler
 * Author: DIYILIU
 * Update: 2016-03-22 16:34
 */

@Component
@ChannelHandler.Sharable
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
        logger.info("收到SQL: {}", sql);

        int sqlId = getSQLID(sql);

        if (sqlId == Constant.SQL.INSERT){
            logger.info("收到INSERT SQL: {}", sql);
            DealInsertSQL.putSQL(sql);
        }else if (sqlId == Constant.SQL.UPDATE){
            logger.info("收到UPDATE SQL: {}", sql);
            DealUpdateSQL.putSQL(sql);
        }else {
            logger.warn("无法处理SQL:[{}]", sql);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("服务器异常...{}", cause.getStackTrace());
        ctx.close();
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

    private int getSQLID(String sql){

        if (sql.toUpperCase().contains("INSERT")){
            return Constant.SQL.INSERT;
        }

        if (sql.toUpperCase().contains("UPDATE")){
            return Constant.SQL.UPDATE;
        }

        return -1;
    }

}
