package com.tj.common.db.rwsplit;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface RWSplit {
    RWSplitDataSourceType value() default RWSplitDataSourceType.WRITE;
}