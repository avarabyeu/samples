package com.github.avarabyeu.samples.guiceng;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Andrei Varabyeu
 */
@Retention(RUNTIME)
@Target({ElementType.TYPE})
public @interface UseParentInjector {
    boolean cached() default true;
}
