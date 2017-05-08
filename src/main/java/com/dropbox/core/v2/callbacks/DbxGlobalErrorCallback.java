package com.dropbox.core.v2.callbacks;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows for consistent, global handling of route-specific error types, as well as general network errors.
 * Normally, error handling is done on a request-by-request basis. However, it is convenient to handle some
 * error behavior consistently, regardless of the request or the endpoint. For implementing global
 * error handling for general networking errors like an auth error, one of the `registerNetworkErrorCallback`
 * methods should be used. For implementing global error handling for route-specific errors, one of the
 * `registerRouteErrorCallback` methods should be used.
 */

public class DbxGlobalErrorCallback {
    private static Map<Class<?>, DbxRouteErrorCallback<?>> routeErrorClassToCallback = new HashMap<Class<?>, DbxRouteErrorCallback<?>>();
    private static DbxNetworkErrorCallback networkErrorCallback = null;

    public static <T> DbxRouteErrorCallback<T> getRouteErrorCallback(Class<T> routeErrorClass) {
        if (routeErrorClassToCallback.containsKey(routeErrorClass)) {
            return (DbxRouteErrorCallback<T>)routeErrorClassToCallback.get(routeErrorClass);
        }
        return null;
    }

    public static void setRouteErrorCallback(DbxRouteErrorCallback<?> callback) {
        routeErrorClassToCallback.put(callback.getRouteError().getClass(), callback);
    }

    public static DbxNetworkErrorCallback getNetworkErrorCallback() {
        return networkErrorCallback;
    }

    public static void setNetworkErrorCallback(DbxNetworkErrorCallback networkErrorCallback) {
        DbxGlobalErrorCallback.networkErrorCallback = networkErrorCallback;
    }
}
