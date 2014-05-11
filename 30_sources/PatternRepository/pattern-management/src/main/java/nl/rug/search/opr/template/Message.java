package nl.rug.search.opr.template;

import java.util.ArrayList;
import java.util.List;

public class Message {

    private int code;
    private List<String> details = new ArrayList<String>(2);
    private String type;

    public int getCode() {
        return code;
    }

    public Message setCode(int code) {
        this.code = code;
        return this;
    }

    public Object[] getDetails() {
        return details.toArray();
    }

    public Message addDetail(String detail) {
        this.details.add(detail);
        return this;
    }

    public String getType() {
        return type;
    }

    public Message setType(String type) {
        this.type = type;
        return this;
    }
}
