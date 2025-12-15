package org.group_three.debug.annotations;

import java.lang.annotation.*;

/**
 * Indicates that this class only uses Static methods and is not to be instantiated.
 * @author Luca
 * */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface StaticClass { }
