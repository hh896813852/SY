package com.edusoho.yunketang.bean.lesson;

import java.util.List;

/**
 * Created by DF on 2016/12/19.
 */

public class StudentList {

    private CodeBean code;
    private String message;
    private List<PreviousBean> previous;

    public CodeBean getCode() {
        return code;
    }

    public void setCode(CodeBean code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PreviousBean> getPrevious() {
        return previous;
    }

    public void setPrevious(List<PreviousBean> previous) {
        this.previous = previous;
    }

    public static class CodeBean {

        private AttributesBean attributes;
        private RequestBean request;
        private QueryBean query;
        private ServerBean server;
        private FilesBean files;
        private CookiesBean cookies;
        private HeadersBean headers;

        public AttributesBean getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesBean attributes) {
            this.attributes = attributes;
        }

        public RequestBean getRequest() {
            return request;
        }

        public void setRequest(RequestBean request) {
            this.request = request;
        }

        public QueryBean getQuery() {
            return query;
        }

        public void setQuery(QueryBean query) {
            this.query = query;
        }

        public ServerBean getServer() {
            return server;
        }

        public void setServer(ServerBean server) {
            this.server = server;
        }

        public FilesBean getFiles() {
            return files;
        }

        public void setFiles(FilesBean files) {
            this.files = files;
        }

        public CookiesBean getCookies() {
            return cookies;
        }

        public void setCookies(CookiesBean cookies) {
            this.cookies = cookies;
        }

        public HeadersBean getHeaders() {
            return headers;
        }

        public void setHeaders(HeadersBean headers) {
            this.headers = headers;
        }

        public static class AttributesBean {
        }

        public static class RequestBean {
        }

        public static class QueryBean {
        }

        public static class ServerBean {
        }

        public static class FilesBean {
        }

        public static class CookiesBean {
        }

        public static class HeadersBean {
        }
    }

    public static class PreviousBean {

        private String message;
        private List<String> trace;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<String> getTrace() {
            return trace;
        }

        public void setTrace(List<String> trace) {
            this.trace = trace;
        }
    }
}
