package com.tiza.client;

import com.tiza.client.handler.ServerDecoder;
import com.tiza.client.handler.ServerEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Description: ServerClient
 * Author: DIYILIU
 * Update: 2016-03-22 16:32
 */

public class ServerClient extends Thread implements IClient {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private int port = 8088;

    @Resource
    private ChannelInboundHandler serverHandler;

    @Override
    public void init() {

        this.start();
    }

    @Override
    public void run() {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1000)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast(new ServerDecoder())
                                    .addLast(new ServerEncoder())
                                    .addLast(new IdleStateHandler(60, 0, 0))
                                    .addLast(serverHandler);
                        }
                    });
            ChannelFuture f = b.bind(port).sync();
            logger.info("DBP服务器启动...");

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }


    }

    public void setPort(int port) {
        this.port = port;
    }
}
