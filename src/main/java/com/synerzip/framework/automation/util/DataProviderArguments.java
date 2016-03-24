/*
 * Copyright 2016 Synerzip Softech. All Rights Reserved.
 */

package com.synerzip.framework.automation.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The Interface DataProviderArguments.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DataProviderArguments {
	String path();
}
