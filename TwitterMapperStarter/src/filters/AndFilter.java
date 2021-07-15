package filters;

import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

public class AndFilter implements Filter {

    private final Filter first_child;
    private final Filter second_child;

    public AndFilter(Filter first_child, Filter second_child) {
        this.first_child = first_child;
        this.second_child = second_child;
    }

    //changed s to status
    @Override
    public boolean matches(Status status) {
        return first_child.matches(status) && second_child.matches(status);
    }

    @Override
    public List<String> terms() {
        return new ArrayList<String>() {{
            addAll(first_child.terms());
            addAll(second_child.terms());
        }};
    }

    public String toString() {
        return "(" + first_child.toString() + " and " + second_child.toString() + ")";
    }
}
