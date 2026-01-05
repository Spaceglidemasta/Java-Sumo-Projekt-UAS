package org.group_three.debug.annotations;



import org.group_three.constants.enums.style.AttributeStyle;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.RECORD_COMPONENT;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * This annotation allows for Stylisation of Lists as Record-Components
 * @see AttributeStyle
 * @author Luca
 * */
@Retention(RUNTIME)
@Target(RECORD_COMPONENT)
public @interface PrintStyle {
    AttributeStyle value();
}
