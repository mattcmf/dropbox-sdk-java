package com.dropbox.core.v2.callbacks;

/**
 * Abstract class for route error callback.
 */
public abstract class DbxRouteErrorCallback<TError> implements Runnable {
    private TError routeError;

    public TError getRouteError() {
        return routeError;
    }

    public void setRouteError(TError routeError) {
        this.routeError = routeError;
    }

    public DbxRouteErrorCallback(TError routeError) {
        this.routeError = routeError;
    }
}
