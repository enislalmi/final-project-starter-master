package filters;

import twitter4j.Status;

import java.util.List;

public class AndFilter implements Filter {
    private final Filter left_side;
    private final Filter right_side;

    public AndFilter(Filter left_side, Filter right_side) {
        this.left_side = left_side;
        this.right_side = right_side;
    }


    @Override
    public boolean matches(Status status) {
        return left_side.matches(status) && right_side.matches(status);
    }

    @Override
    public List<String> terms() {
        List<String> terms = left_side.terms();
        terms().addAll(right_side.terms());
        return terms;
    }

    @Override
    public String toString()
    {
        String string = "(" + left_side.toString() + "and" + right_side.toString() + ")";
        return string;
    }
}
