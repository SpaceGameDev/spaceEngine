package space.engine.event;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import space.engine.Side;
import space.engine.key.attribute.AttributeList;
import space.engine.key.attribute.AttributeListModification;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static space.engine.Side.*;

public class EventTest {
	
	final int eventInput = 42;
	AtomicInteger callCounter = new AtomicInteger();
	
	EventEntry<Consumer<Integer>> accept0 = new EventEntry<>(createAcceptFunction(0));
	EventEntry<Consumer<Integer>> accept1 = new EventEntry<>(createAcceptFunction(1), accept0);
	EventEntry<Consumer<Integer>> accept234_1 = new EventEntry<>(createAcceptFunction(2, 4), accept1);
	EventEntry<Consumer<Integer>> accept234_2 = new EventEntry<>(createAcceptFunction(2, 4), accept1);
	EventEntry<Consumer<Integer>> accept234_3 = new EventEntry<>(createAcceptFunction(2, 4), accept1);
	EventEntry<Consumer<Integer>> accept56_1 = new EventEntry<>(createAcceptFunction(5, 6), accept234_1, accept234_2, accept234_3);
	EventEntry<Consumer<Integer>> accept56_2 = new EventEntry<>(createAcceptFunction(5, 6), accept234_1, accept234_2, accept234_3);
	EventEntry<Consumer<Integer>> accept7 = new EventEntry<>(createAcceptFunction(7), accept56_2, accept56_1);
	EventEntry<Consumer<Integer>> accept9 = new EventEntry<>(createAcceptFunction(9));
	EventEntry<Consumer<Integer>> accept8 = new EventEntry<>(createAcceptFunction(8), new EventEntry[] {accept7}, new EventEntry[] {accept9});
	
	@SuppressWarnings("unchecked")
	EventEntry<Consumer<Integer>>[] acceptAll = new EventEntry[] {
			accept0, accept1, accept234_1, accept234_2, accept234_3, accept56_1, accept56_2, accept7, accept8, accept9
	};
	
	private Consumer<Integer> createAcceptFunction(int callId) {
		return integer -> {
			assertEquals(integer.intValue(), eventInput);
			assertEquals(callCounter.getAndIncrement(), callId);
		};
	}
	
	@SuppressWarnings("SameParameterValue")
	private Consumer<Integer> createAcceptFunction(int callIdFrom, int callIdTo) {
		return integer -> {
			assertEquals(integer.intValue(), eventInput);
			Assert.assertThat(callCounter.getAndIncrement(), allOf(greaterThanOrEqualTo(callIdFrom), lessThanOrEqualTo(callIdTo)));
		};
	}
	
	public void testEvent(Event<Consumer<Integer>> eventImpl) throws InterruptedException {
		Arrays.stream(acceptAll).forEach(eventImpl::addHook);
		eventImpl.submit(func -> func.accept(eventInput)).await();
		assertEquals(callCounter.get(), acceptAll.length);
	}
	
	@Before
	public void before() {
		AttributeList<Side> side = side();
		AttributeListModification<Side> modify = side.createModify();
		modify.put(EXECUTOR_POOL, Runnable::run);
		side.apply(modify);
	}
	
	@Test
	public void testEventBuilderSinglethread() throws InterruptedException {
		testEvent(new SequentialEventBuilder<>());
	}
	
	@Test
	public void testEventBuilderMultithreaded() throws InterruptedException {
		testEvent(new ParallelEventBuilder<>());
	}
}