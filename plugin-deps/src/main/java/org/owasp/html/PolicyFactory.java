package org.owasp.html;

import com.sun.istack.internal.Nullable;

public final class PolicyFactory {

    public <CTX> String sanitize(@Nullable String html,
            @Nullable HtmlChangeListener<CTX> listener, @Nullable CTX context) {
        return "";
    }
}
