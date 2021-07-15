package query;

import filters.Filter;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.MapMarkerCircle;
import twitter4j.Status;
import twitter4j.User;
import ui.custom.CustomizedMap;
import ui.custom.CustomizedMap;
import util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * A query over the twitter stream.
 */
public class Query implements Observer {
    private final JMapViewer map;
    private Layer layer;
    private final Color color;
    private final String queryString;
    private final Filter filter;
    private JCheckBox checkBox;

    private List<MapMarkerCircle> customMapMarkerList;

    /*
     * JUST FOR TESTING
     * */
    public Query() {
        this("not a test", Color.BLACK, new JMapViewer());
    }

    public Query(String queryString, Color color, JMapViewer map) {
        this.customMapMarkerList = new ArrayList<>();
        this.queryString = queryString;
        this.filter = Filter.parse(queryString);
        this.color = color;
        this.layer = new Layer(queryString);
        this.map = map;
    }

    public Color getColor() {
        return color;
    }

    public String getQueryString() {
        return queryString;
    }

    public Filter getFilter() {
        return filter;
    }

    public Layer getLayer() {
        return layer;
    }

    public JCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public void setVisible(boolean visible) {
        layer.setVisible(visible);
    }

    public boolean getVisible() { return layer.isVisible(); }

    @Override
    public String toString() {
        return "Query: " + queryString;
    }

    public void terminate() {

        customMapMarkerList.forEach(map::removeMapMarker);
    }

    @Override
    public void update(Observable observable, Object obj) {
        Status status = (Status) obj;
        if (filter.matches(status)) {
            MapMarkerCircle mapMarker = getCustomMapMarker(status);
            customMapMarkerList.add(mapMarker);
            map.addMapMarker(mapMarker);
        }
    }

    private MapMarkerCircle getCustomMapMarker(Status status) {
        Coordinate coordinate = Util.statusCoordinate(status);
        User user = status.getUser();
        String profileImageURL = user.getProfileImageURL();
        return new CustomizedMap(getLayer(), coordinate, getColor(), profileImageURL, status.getText());
    }

    public void terminateQuery() {
        layer.setVisible(false);
        customMapMarkerList.forEach(map::removeMapMarker);
    }
}
