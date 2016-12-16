package com.syful.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //can use in method only.
public @interface WebTest {

    /**
     * Links TestNG test case to TestRail case instance
     */
    public String id() default "";

    /**
     * Unique identifier for the location. Responsible for communication with DB.
     * Should be the same as in [Identifier] column for the Location table
     * */
    public String locationAccountName() default "";

    /**
     * Feature level for new location.
     * Available option:
     * "Booker Express Direct", "Booker Express Partner", "Booker Kickstart",
     * "Classes & Practices Kickstart", "Enterprise", "FD - Basic", "GoBook",
     * "GoPromote", "HR - Basic", "HR - Standard", "Lite", "Professional",
     * "ProfessionalPlus", "Transactional", "Unlimited"
     * */
    public String locationFeatureLevel() default "";

    /**
     * Flag that indicates if test requires a new location.
     * If it set to true new location will be created with Feature level that specified in {@link #locationFeatureLevel}.
     * Otherwise test will use location that specified in {@link #locationAccountName}
     * */
    public boolean isRequireNewLocation() default false;

    /**
     * Payment processor name.
     * */
    public String paymentProcessor() default "";
}
