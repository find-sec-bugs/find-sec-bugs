package org.springframework.web.client;

interface RestOperationsExtensionsKt {
        <T> T getForObject(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object... c);
        <T> T getForObject(org.springframework.web.client.RestOperations a, java.lang.String b, java.util.Map<java.lang.String, ? extends java.lang.Object> d) ;
        <T> T getForObject(org.springframework.web.client.RestOperations a, java.net.URI b) ;
        <T> org.springframework.http.ResponseEntity<T> getForEntity(org.springframework.web.client.RestOperations a, java.net.URI b) ;
        <T> org.springframework.http.ResponseEntity<T> getForEntity(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object... c) ;
        <T> org.springframework.http.ResponseEntity<T> getForEntity(org.springframework.web.client.RestOperations a, java.lang.String b, java.util.Map<java.lang.String, ?> c) ;
        <T> T patchForObject(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object c, java.lang.Object... d) ;
        java.lang.Object patchForObject$default(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object c, java.lang.Object[] d, int e, java.lang.Object f) ;
        <T> T patchForObject(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object c, java.util.Map<java.lang.String, ?> d) ;
        java.lang.Object patchForObject$default(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object c, java.util.Map d, int e, java.lang.Object f) ;
        <T> T patchForObject(org.springframework.web.client.RestOperations a, java.net.URI b, java.lang.Object c) ;
        java.lang.Object patchForObject$default(org.springframework.web.client.RestOperations a, java.net.URI b, java.lang.Object c, int d, java.lang.Object e) ;
        <T> T postForObject(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object c, java.lang.Object... d) ;
        java.lang.Object postForObject$default(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object c, java.lang.Object[] d, int e, java.lang.Object f) ;
        <T> T postForObject(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object c, java.util.Map<java.lang.String, ?> d) ;
        java.lang.Object postForObject$default(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object c, java.util.Map d, int e, java.lang.Object f) ;
        <T> T postForObject(org.springframework.web.client.RestOperations a, java.net.URI b, java.lang.Object c) ;
        java.lang.Object postForObject$default(org.springframework.web.client.RestOperations a, java.net.URI b, java.lang.Object c, int d, java.lang.Object e) ;
        <T> org.springframework.http.ResponseEntity<T> postForEntity(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object c, java.lang.Object... d) ;
        org.springframework.http.ResponseEntity postForEntity$default(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object c, java.lang.Object[] d, int e, java.lang.Object f) ;
        <T> org.springframework.http.ResponseEntity<T> postForEntity(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object c, java.util.Map<java.lang.String, ?> d) ;
        org.springframework.http.ResponseEntity postForEntity$default(org.springframework.web.client.RestOperations a, java.lang.String b, java.lang.Object c, java.util.Map d, int e, java.lang.Object f) ;
        <T> org.springframework.http.ResponseEntity<T> postForEntity(org.springframework.web.client.RestOperations a, java.net.URI b, java.lang.Object c) ;
        org.springframework.http.ResponseEntity postForEntity$default(org.springframework.web.client.RestOperations a, java.net.URI b, java.lang.Object c, int d, java.lang.Object e) ;
        <T> org.springframework.http.ResponseEntity<T> exchange(org.springframework.web.client.RestOperations a, java.lang.String b, org.springframework.http.HttpMethod c, org.springframework.http.HttpEntity<?> d, java.lang.Object... e) ;
        org.springframework.http.ResponseEntity exchange$default(org.springframework.web.client.RestOperations a, java.lang.String b, org.springframework.http.HttpMethod c, org.springframework.http.HttpEntity d, java.lang.Object[] e, int f, java.lang.Object g) ;
        <T> org.springframework.http.ResponseEntity<T> exchange(org.springframework.web.client.RestOperations a, java.lang.String b, org.springframework.http.HttpMethod c, org.springframework.http.HttpEntity<?> d, java.util.Map<java.lang.String, ?> e) ;
        org.springframework.http.ResponseEntity exchange$default(org.springframework.web.client.RestOperations a, java.lang.String b, org.springframework.http.HttpMethod c, org.springframework.http.HttpEntity d, java.util.Map e, int f, java.lang.Object g) ;
        <T> org.springframework.http.ResponseEntity<T> exchange(org.springframework.web.client.RestOperations a, java.net.URI b, org.springframework.http.HttpMethod c, org.springframework.http.HttpEntity<?> d) ;
        org.springframework.http.ResponseEntity exchange$default(org.springframework.web.client.RestOperations a, java.net.URI b, org.springframework.http.HttpMethod c, org.springframework.http.HttpEntity d, int e, java.lang.Object f) ;
}