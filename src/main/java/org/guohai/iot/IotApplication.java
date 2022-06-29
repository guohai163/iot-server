package org.guohai.iot;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.guohai.iot.handler.DecoderHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 引导程序，同时我们要实现CommandLineRunner接口。
 * 同时实现run方法，会在所有 Spring Beans 都初始化之后，SpringApplication.run() 之前执行
 * @author guohai
 */
@SpringBootApplication
public class IotApplication implements CommandLineRunner {

	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(IotApplication.class);

	/**
	 * 主事件，负责连接。单一线程就行
	 */
	private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	/**
	 * 负责处理业务,不设置线程数时为CPU核心*2.如果运行在容器状态下会不准，建议手动设置
	 */
	private final EventLoopGroup workerGroup = new NioEventLoopGroup(2);
	/**
	 * 服务监听的端口
	 */
	private final static int SERVER_PORT = 4100;

	/**
	 * 解码器
	 */
	@Autowired
	DecoderHandler decoderHandler;

	public static void main(String[] args) {
		SpringApplication.run(IotApplication.class, args);
	}

	/**
	 * 实现自定义的run方法
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(String... args) throws Exception {
		try{
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap
					.group(bossGroup, workerGroup)
					// 这里还可以支持其他的实现，
					// 比如在Linux下可以用基于EpollServerSocketChannel
					// 在mac下可以使用KQueueServerSocketChannel
					// 在这里我们用比较通用的NioServerSocketChannel实现
					.channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline()
									.addLast(decoderHandler);
						}
					});
			// 绑定端口
			ChannelFuture channelFuture = bootstrap.bind(SERVER_PORT).sync();
			logger.info("Server start listen port :" + SERVER_PORT);
			channelFuture.channel().closeFuture().sync();
		}finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}

		// 在应用结束的时候，我们同时还要结束掉 worker和boos两个事件循环
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				workerGroup.shutdownGracefully();
				bossGroup.shutdownGracefully();
			}
		});

	}
}
