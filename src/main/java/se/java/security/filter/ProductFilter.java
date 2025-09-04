package se.java.security.filter;

import se.java.security.models.Product;

import java.util.function.Predicate;

/**
 * FUNCTIONAL INTERFACE for creating product filters
 * *
 * PURPOSE: This interface defines a contract for creating filter
 * predicates that can be applied to products.
 * *
 * FUNCTIONAL INTERFACE EXPLANATION:
 * - @FunctionalInterface means this interface has exactly one abstract method
 * - Can be implemented using lambda expressions
 * - Used to create reusable filter logic
 * *
 * GENERIC TYPE:
 * - <T extends Product>: Works with any Product subclass
 * *
 * METHOD SIGNATURE:
 * - apply(String value): Takes a filter value (e.g., "M" for size, "leather" for material)
 * - Returns Predicate<T>: A function that tests if a product matches the filter criteria
 * *
 * EXAMPLE USAGE:
 * ProductFilter<Collar> sizeFilter = value -> collar -> collar.getSize().equals(value);
 * Predicate<Collar> mediumSizeFilter = sizeFilter.apply("M");
 * boolean matches = mediumSizeFilter.test(someCollar);
 */
@FunctionalInterface
public interface ProductFilter<T extends Product> {
    Predicate<T> apply(String value);
}