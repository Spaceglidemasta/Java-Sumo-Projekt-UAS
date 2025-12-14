package org.group_three.debug.annotations;

import java.lang.annotation.*;

/**
 * Indicates that this method may return null
 * @author Luca
 * */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface MayReturnNull { }
