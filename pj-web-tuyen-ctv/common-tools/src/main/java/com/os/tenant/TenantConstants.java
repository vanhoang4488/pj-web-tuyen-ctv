package com.os.tenant;

public abstract class TenantConstants {

    public static final String TENANT_MSG_TAG = "@tenantId=";

    public static final ThreadLocal<String> TENANT_THREAD_LOCAL = new ThreadLocal();

    public static final String TENANT_ID_NAME = "tenantId";
}
