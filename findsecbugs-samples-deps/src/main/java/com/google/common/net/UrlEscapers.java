package com.google.common.net;

import com.google.common.escape.Escaper;

public final class UrlEscapers {
    public static Escaper urlPathSegmentEscaper() {
        return new PercentEscaper();
    }
}
