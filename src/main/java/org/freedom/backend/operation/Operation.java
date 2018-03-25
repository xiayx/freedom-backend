package org.freedom.backend.operation;

/**
 * 操作，通常表示用户在页面上点击按钮后，客户端向后台发起请求
 *
 * @author xiayx
 */
public class Operation {

    private String code;
    private String name;
    private String url;

    public Operation() {
    }

    public Operation(String code, String name, String url) {
        this.code = code;
        this.name = name;
        this.url = url;
    }

    @Override
    public String toString() {
        return "{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
