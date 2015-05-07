/**
 * 
 */
package net.freecoder.restdemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.freecoder.restdemo.constant.SystemModules;

/**
 * This annotation should be added before to Controller declaration, to declare
 * a controller needs a module privilege.
 * 
 * @author JiTing
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface WlsModule {
	SystemModules value();

}
