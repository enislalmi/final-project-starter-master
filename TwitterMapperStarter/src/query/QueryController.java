package query;

import twitter.TwitterSource;
import twitter4j.Twitter;
import ui.ContentPanel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class QueryController {

    private final List<Query> LIST_QUERIES;
    private final TwitterSource SOURCE;
    private static QueryController controller;


    public QueryController(TwitterSource source) {
        LIST_QUERIES = new ArrayList<>();
        SOURCE = source;
    }

    public static QueryController getController(TwitterSource source)
    {
        if (controller == null)
        {
            return new QueryController(source);
        }
        return controller;
    }

    public List<Query> getLIST_QUERIES() {return LIST_QUERIES;}
    public TwitterSource getSource() {
        return SOURCE;
    }

    public void addQuery(Query query) {
        LIST_QUERIES.add(query);
        SOURCE.setFilterTerms(getTerms());
        SOURCE.addObserver(query);
    }

    public void addQueryToPanel(Query query, ContentPanel contentPanel){ contentPanel.addQueryToContentPanel(query); }

    private Collection<String> getTerms() {
        Collection<String> terms = new HashSet<>();

        LIST_QUERIES.stream().map(q -> q.getFilter().terms()).forEach(terms::addAll);

        return terms;
    }

    public void terminateQuery(Query query) {
        query.terminate();
        SOURCE.deleteObserver(query);

        LIST_QUERIES.remove(query);
        SOURCE.setFilterTerms(getTerms());
    }

}
