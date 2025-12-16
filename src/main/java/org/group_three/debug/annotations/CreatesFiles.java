package org.group_three.debug.annotations;


import java.lang.annotation.*;

/**
 * Indicates that this method creates files.
 * @author Luca
 * */
@Documented
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface CreatesFiles {
}
