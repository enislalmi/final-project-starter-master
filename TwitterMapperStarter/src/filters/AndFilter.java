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
            add(listToString(first_child.terms())+ " " + listToString(second_child.terms()));
        }};
    }

    private String listToString(List<String> stringList)
    {
        StringBuilder listString = new StringBuilder();

        for(String string: stringList)
        {
            listString.append(string);
        }
        return listString.toString();
    }
    public String toString() {
        return "(" + first_child.toString() + " and " + second_child.toString() + ")";
    }
}
