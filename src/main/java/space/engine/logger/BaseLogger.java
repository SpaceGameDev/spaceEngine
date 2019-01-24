package space.engine.logger;

import org.jetbrains.annotations.NotNull;

public class BaseLogger extends AbstractLogger {
	
	//	DependencyEventCreator<Prefix> handler = new DependencyEventBuilderSinglethread<>();
//	DependencyEventCreator<BiConsumer<LogMessage, CharSequence2D>> printer = new DependencyEventBuilderSinglethread<>();
	public String prefixMessageSeparator;
	
	public BaseLogger() {
		this(": ");
	}
	
	public BaseLogger(String prefixMessageSeparator) {
		this.prefixMessageSeparator = prefixMessageSeparator;
	}
	
	//subLogger
	@NotNull
	@Override
	public String name() {
		return "root";
	}
	
	@Override
	public Logger parentLogger() {
		return null;
	}
	
	@NotNull
	@Override
	public Logger subLogger(String name) {
		return new SubLogger(this, name);
	}
	
	//log
	@Override
	public void logDirect0(LogMessage msg) {
//		try {
//			handler.execute(consumer -> consumer.accept(msg)).await();
//			printer.execute(new TypeBiConsumer<>(msg, new CharBufferBuilder2D<>().append(msg.prefix).append(prefixMessageSeparator).append(msg.msg).toString2D())).await();
//		} catch (InterruptedException e) {
//			Thread.currentThread().interrupt();
//		}
	}
	
	//utility
	
	/**
	 * adds a {@link java.util.Date} (formatted with HH:mm:ss), {@link Thread}, {@link LogLevel} and {@link SubLogger} prefix to the {@link BaseLogger}
	 */
	public static BaseLogger defaultHandler(BaseLogger logger) {
//		logger.handler.addHook(DependencyEventEntry.fromFunction(new TimePrefix(new SimpleDateFormat("HH:mm:ss")), "time", 0));
//		logger.handler.addHook(DependencyEventEntry.fromFunction(new ThreadPrefix(), "thread", 1));
//		logger.handler.addHook(DependencyEventEntry.fromFunction(new LogLevelPrefix(), "loglevel", 2));
//		logger.handler.addHook(DependencyEventEntry.fromFunction(new SubLoggerPrefix(), "sublogger", 3));
		return logger;
	}
	
	/**
	 * adds the default System.out and System.err {@link java.io.PrintStream}s as Printer to the BaseLogger
	 */
	public static BaseLogger defaultPrinter(BaseLogger logger) {
//		logger.printer.addHook(DependencyEventEntry.fromFunction(new SeparatedPrinter(new SimpleStringPrinter(System.out), new SimpleStringPrinter(System.err)), "system"));
		return logger;
	}
}