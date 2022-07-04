package org.guohai.iot;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.guohai.iot.event.MainEventProducer;
import org.guohai.iot.handler.DecoderHandler;
import org.guohai.iot.handler.EncoderHandler;
import org.guohai.iot.handler.IdleCheckHandler;
import org.guohai.iot.handler.StatusPringHandler;
import org.guohai.iot.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

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
	 * 状态 handler
	 */
	@Autowired
	StatusPringHandler statusPringHandler;

	/**
	 * 会话管理
	 */
	@Autowired
	SessionManager sessionManager;


	@Autowired
	IdleCheckHandler idleCheckHandler;

	@Autowired
	MainEventProducer mainEventProducer;



	public static void main(String[] args) {
		SpringApplication.run(IotApplication.class, args);
	}

	/**
	 * 实现自定义的run方法
	 * @param args 输入的参数
	 * @throws Exception 抛出异常
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
						public void initChannel(SocketChannel ch) {
							ch.pipeline()
									// 链路 head <=> IdleState <=> decoder <=> idleCheck <=> tail
									// 入站 从head到tail ，出站 从tail到head
									// 增加空闲检查器，规定读写各30秒没操作时触发
									.addLast("IdleState", new IdleStateHandler(30,30,0))
									// netty要求ChannelHandler是每个线程一份的，就算指定bean的scope是原型也无效。
									// 这里有三种解决方案
									// 1. 每次都是new的，但把需要依赖spring完成初始化的传参进去
									// 2. 使用一个ApplicationContextHolder工具类，在handler中通过applicationContext.getBean来获取
									// 3. 如果能保证线程安全的情况下 给ChannelHandler增加@Sharable注解
									// 增加一个json解码的
									.addLast("decoder", new DecoderHandler(sessionManager, mainEventProducer))
									// 编码器
									.addLast("encoder", new EncoderHandler())
									//自定义实现的空闲处理
									.addLast("idleCheck", idleCheckHandler);
						}
					});

			// 为worker组设置一个定时器
			workerGroup.next().scheduleAtFixedRate(statusPringHandler,1, 60, TimeUnit.SECONDS);

			// 绑定端口
			ChannelFuture channelFuture = bootstrap.bind(SERVER_PORT).sync();
			logger.info("Server start listen port :" + SERVER_PORT);
			channelFuture.channel().closeFuture().sync();
		}finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}

		// 在应用结束的时候，我们同时还要结束掉 worker和boos两个事件循环
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				logger.info("程序结束，准备结束两个event loop");
				workerGroup.shutdownGracefully();
				bossGroup.shutdownGracefully();
			}
		));

	}
}
