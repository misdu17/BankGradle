package com.syful.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Apitest {

    /**
     * Links TestNG test case to TestRail case instance
     */
    public String id() default "";

    /**
     * Unique identifier for the location. Responsible for communication with DB.
     * Should be the same as in [Identifier] column for the Location table
     * */
    public String locationAccountName() default "";

    public String paymentProcessor() default "";

    public String defect() default "";

    public String story() default "";
}
