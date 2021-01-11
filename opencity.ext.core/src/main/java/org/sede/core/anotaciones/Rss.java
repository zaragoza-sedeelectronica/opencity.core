package org.sede.core.anotaciones;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Rss {

	String title() default "";
	String link() default "";
	
	String description() default "";
	String pubDate() default "";
	
	
}
