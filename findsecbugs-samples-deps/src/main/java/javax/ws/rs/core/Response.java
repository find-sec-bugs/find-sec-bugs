package javax.ws.rs.core;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class Response {

    protected Response() {}

    public static ResponseBuilder status(Status status) {
        ResponseBuilder b = ResponseBuilder.newInstance();
        b.status(status);
        return b;
    }

    public static ResponseBuilder status(int status) {
        ResponseBuilder b = ResponseBuilder.newInstance();
        b.status(status);
        return b;
    }

    public static ResponseBuilder ok() {
        ResponseBuilder b = status(Status.OK);
        return b;
    }

    public static ResponseBuilder ok(Object entity) {
        ResponseBuilder b = ok();
        b.entity(entity);
        return b;
    }

    public static ResponseBuilder ok(Object entity, MediaType type) {
        ResponseBuilder b = ok();
        b.entity(entity);
        b.type(type);
        return b;
    }

    public static ResponseBuilder ok(Object entity, String type) {
        ResponseBuilder b = ok();
        b.entity(entity);
        b.type(type);
        return b;
    }

    public static ResponseBuilder ok(Object entity, Variant variant) {
        ResponseBuilder b = ok();
        b.entity(entity);
        b.variant(variant);
        return b;
    }

    public static ResponseBuilder serverError() {
        ResponseBuilder b = status(Status.INTERNAL_SERVER_ERROR);
        return b;
    }

    public static ResponseBuilder created(URI location) {
        ResponseBuilder b = status(Status.CREATED).location(location);
        return b;
    }

    public static ResponseBuilder noContent() {
        ResponseBuilder b = status(Status.NO_CONTENT);
        return b;
    }

    public static ResponseBuilder notModified() {
        ResponseBuilder b = status(Status.NOT_MODIFIED);
        return b;
    }

    public static ResponseBuilder notModified(EntityTag tag) {
        ResponseBuilder b = notModified();
        b.tag(tag);
        return b;
    }

    public static ResponseBuilder notModified(String tag) {
        ResponseBuilder b = notModified();
        b.tag(tag);
        return b;
    }

    public static ResponseBuilder seeOther(URI location) {
        ResponseBuilder b = status(Status.SEE_OTHER).location(location);
        return b;
    }

    public static ResponseBuilder temporaryRedirect(URI location) {
        ResponseBuilder b = status(Status.TEMPORARY_REDIRECT).location(location);
        return b;
    }

    public static ResponseBuilder notAcceptable(List<Variant> variants) {
        ResponseBuilder b = status(Status.NOT_ACCEPTABLE).variants(variants);
        return b;
    }

    public static abstract class ResponseBuilder {

        protected ResponseBuilder() {}

        protected static ResponseBuilder newInstance() {
            return null;
        }

        public abstract Response build();

        @Override
        public abstract ResponseBuilder clone();

        public abstract ResponseBuilder status(int status);

        public ResponseBuilder status(Status status) {
            return null;
        };

        public abstract ResponseBuilder entity(Object entity);

        public abstract ResponseBuilder type(MediaType type);

        public abstract ResponseBuilder type(String type);

        public abstract ResponseBuilder variant(Variant variant);

        public abstract ResponseBuilder variants(List<Variant> variants);

        public abstract ResponseBuilder language(String language);

        public abstract ResponseBuilder language(Locale language);

        public abstract ResponseBuilder location(URI location);

        public abstract ResponseBuilder contentLocation(URI location);

        public abstract ResponseBuilder tag(EntityTag tag);

        public abstract ResponseBuilder tag(String tag);

        public abstract ResponseBuilder lastModified(Date lastModified);

        public abstract ResponseBuilder cacheControl(CacheControl cacheControl);

        public abstract ResponseBuilder expires(Date expires);

        public abstract ResponseBuilder header(String name, Object value);

        public abstract ResponseBuilder cookie(NewCookie... cookies);
    }

    public enum Status {
        OK(200, "OK"),
        CREATED(201, "Created"),
        ACCEPTED(202, "Accepted"),
        NO_CONTENT(204, "No Content"),
        MOVED_PERMANENTLY(301, "Moved Permanently"),
        SEE_OTHER(303, "See Other"),
        NOT_MODIFIED(304, "Not Modified"),
        TEMPORARY_REDIRECT(307, "Temporary Redirect"),
        BAD_REQUEST(400, "Bad Request"),
        UNAUTHORIZED(401, "Unauthorized"),
        FORBIDDEN(403, "Forbidden"),
        NOT_FOUND(404, "Not Found"),
        NOT_ACCEPTABLE(406, "Not Acceptable"),
        CONFLICT(409, "Conflict"),
        GONE(410, "Gone"),

        PRECONDITION_FAILED(412, "Precondition Failed"),

        UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),

        INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

        SERVICE_UNAVAILABLE(503, "Service Unavailable");

        private final int code;
        private final String reason;
        private Family family;

        public enum Family {INFORMATIONAL, SUCCESSFUL, REDIRECTION, CLIENT_ERROR, SERVER_ERROR, OTHER}

        ;

        Status(final int statusCode, final String reasonPhrase) {
            this.code = statusCode;
            this.reason = reasonPhrase;
            switch (code / 100) {
                case 1:
                    this.family = Family.INFORMATIONAL;
                    break;
                case 2:
                    this.family = Family.SUCCESSFUL;
                    break;
                case 3:
                    this.family = Family.REDIRECTION;
                    break;
                case 4:
                    this.family = Family.CLIENT_ERROR;
                    break;
                case 5:
                    this.family = Family.SERVER_ERROR;
                    break;
                default:
                    this.family = Family.OTHER;
                    break;
            }
        }

        public Family getFamily() {
            return family;
        }

        public int getStatusCode() {
            return code;
        }

        @Override
        public String toString() {
            return reason;
        }

        public static Status fromStatusCode(final int statusCode) {
            for (Status s : Status.values()) {
                if (s.code == statusCode) {
                    return s;
                }
            }
            return null;
        }
    }
}
