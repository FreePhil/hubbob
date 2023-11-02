package com.henge.filter


class LameSecurityInterceptor {

    LameSecurityInterceptor() {
        matchAll()
    }

    boolean before() {
        println "before ...."
        true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
