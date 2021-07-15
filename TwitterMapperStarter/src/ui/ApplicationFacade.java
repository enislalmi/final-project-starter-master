package ui;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.tilesources.BingAerialTileSource;
import query.Query;
import twitter.LiveTwitterSource;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * The Twitter viewer application
 * Derived from a JMapViewer demo program written by Jan Peter Stotz
 */
public class ApplicationFacade extends JFrame {
    // The content panel, which contains the entire UI
    private final ContentPanel contentPanel;
    // The provider of the tiles for the map, we use the Bing source
    private BingAerialTileSource bing;
    // All of the active queries
    private List<Query> queries = new ArrayList<>();
    // The source of tweets, a TwitterSource, either live or playback
    private LiveTwitterSource twitterSubject;

    public ApplicationFacade() {
        super("Twitter content viewer");
        contentPanel = new ContentPanel(this);
    }

    private void initializeApp() {
        twitterSubject = new LiveTwitterSource();
        queries = new ArrayList<>();

        initializeUI();
        initializeMapSettings();

    }

    public void addQuery(Query twitterObserver) {
        queries.add(twitterObserver);
        Set<String> allterms = getQueryTerms();
        twitterSubject.setFilterTerms(allterms);
        contentPanel.addQuery(twitterObserver);

        twitterSubject.addObserver(twitterObserver);
    }

    private Set<String> getQueryTerms() {
        Set<String> ans = new HashSet<>();
        for (Query q : queries) {
            ans.addAll(q.getFilter().terms());
        }
        return ans;
    }

    private void initializeMapSettings() {

        map().setMapMarkerVisible(true);

        map().setZoomContolsVisible(true);
        map().setScrollWrapEnabled(true);

        map().setTileSource(bing);

    }

    private void initializeUI() {
        setSize(300, 300);

        setLayout(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        bing = new BingAerialTileSource();

    }

    public void startApp() {

        initializeApp();
        Coordinate coord = new Coordinate(0, 0);
        Timer bingTimer = new Timer();
        bingTimer.schedule(doApplicationTimer(coord, bingTimer), 100, 200);
        map().addMouseMotionListener(getMouseAdapter());
        this.setVisible(true);

    }

    private void getMouseMoved(Point p, ICoordinate pos) {

        new ToolTipBuilder.Builder()
                .addPosition(pos)
                .addMap(map())
                .addPoint(p)
                .addQueries(queries)
                .build();
    }

    private MouseAdapter getMouseAdapter() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                ICoordinate pos = map().getPosition(p);
                getMouseMoved(p, pos);
            }
        };
        return mouseAdapter;
    }

    private TimerTask doApplicationTimer(Coordinate coord, Timer bingTimer) {
        TimerTask bingAttributionCheck = new TimerTask() {

            @Override
            public void run() {
                if (!bing.getAttributionText(0, coord, coord).equals("Error loading Bing attribution data")) {
                    map().setZoom(2);
                    bingTimer.cancel();
                }
            }
        };
        return bingAttributionCheck;
    }


    public JMapViewer map() {
        return contentPanel.getViewer();
    }

    // Update which queries are visible after any checkBox has been changed
    public void updateVisibility() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Recomputing visible queries");
                for (Query q : queries) {
                    JCheckBox box = q.getCheckBox();
                    Boolean state = box.isSelected();
                    q.setVisible(state);
                }
                map().repaint();
            }
        });
    }

    public void terminateQuery(Query twitterObserver) {
        queries.remove(twitterObserver);
        Set<String> allterms = getQueryTerms();
        twitterSubject.setFilterTerms(allterms);
        twitterSubject.deleteObserver(twitterObserver);
    }
}
