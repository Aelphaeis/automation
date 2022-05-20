package com.cruat.automation.capture.predicates;

import java.util.function.Predicate;
import java.util.function.Supplier;

public interface PredicateIterator<T> extends Predicate<T> {

	public boolean iterativeTest(Supplier<T> supplier);
}
