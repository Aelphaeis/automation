package com.cruat.automation.capture.predicates;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.cruat.automation.AutomationException;

public class TimedPredicateIterator<T> implements PredicateIterator<T> {
	
	public static int DEFAULT_TIMEOUT = 5000;
	public static int DEFAULT_INTERVAL = 500;

	private final Predicate<T> predicate;
	private final int interval;
	private final int timeout;

	public TimedPredicateIterator(TimedPredicateIterator.Builder<T> builder) {
		this.predicate = Objects.requireNonNull(builder.getPredicate());
		this.interval = builder.getInterval();
		this.timeout = builder.getTimeout();
	}

	public boolean iterativeTest(Supplier<T> supplier) {
		try {
			long end = System.currentTimeMillis() + getTimeout();
			for (long i = 0; i < end; i = System.currentTimeMillis()) {
				if (test(supplier.get())) {
					return true;
				}
				Thread.sleep(getInterval());
			}
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new AutomationException(e);
		}
		return false;
	}

	@Override
	public boolean test(T t) {
		return getPredicate().test(t);
	}

	public int getTimeout() {
		return timeout;
	}

	public int getInterval() {
		return interval;
	}

	public Predicate<T> getPredicate() {
		return predicate;
	}

	public static class Builder<T> {

		private Predicate<T> predicate;
		private int timeout;
		private int interval;
		
		public Builder() {
			this.timeout = DEFAULT_TIMEOUT;
			this.interval = DEFAULT_INTERVAL;
		}

		public Predicate<T> getPredicate() {
			return predicate;
		}

		public Builder<T> setPredicate(Predicate<T> predicate) {
			this.predicate = predicate;
			return this;
		}

		public int getTimeout() {
			return timeout;
		}

		public Builder<T> setTimeout(int timeout) {
			this.timeout = timeout;
			return this;
		}

		public int getInterval() {
			return interval;
		}

		public Builder<T> setDelay(int interval) {
			this.interval = interval;
			return this;
		}

		public TimedPredicateIterator<T> build() {
			return new TimedPredicateIterator<>(this);
		}
	}
}
