package com.meryemefe.basic_spring_boot_test;

import org.junit.jupiter.api.DisplayName;
import org.junit.platform.commons.support.AnnotationSupport;
import org.junit.platform.engine.TestSource;
import org.junit.platform.engine.support.descriptor.MethodSource;

import java.lang.reflect.Method;
import java.util.Optional;

public class DisplayNameGenerator implements org.junit.jupiter.api.DisplayNameGenerator {

	@Override
	public String generateDisplayNameForClass(Class<?> testClass) {
		Optional<DisplayName> displayNameAnnotation = AnnotationSupport.findAnnotation(testClass, DisplayName.class);
		if (displayNameAnnotation.isPresent()) {
			return displayNameAnnotation.get().value();
		}
		return testClass.getSimpleName();
	}

	@Override
	public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
		Optional<DisplayName> displayNameAnnotation = AnnotationSupport.findAnnotation(nestedClass, DisplayName.class);
		if (displayNameAnnotation.isPresent()) {
			return displayNameAnnotation.get().value();
		}
		return nestedClass.getSimpleName();
	}

	@Override
	public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
		Optional<DisplayName> displayNameAnnotation = AnnotationSupport.findAnnotation(testMethod, DisplayName.class);
		if (displayNameAnnotation.isPresent()) {
			return displayNameAnnotation.get().value();
		}
		return testMethod.getName();
	}
}

