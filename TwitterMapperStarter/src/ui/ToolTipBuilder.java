package ui;

import query.Query;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.Layer;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;
import ui.custom.CustomizedMap;
import util.SphericalGeometry;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ToolTipBuilder {

    private JMapViewer map;
    private ICoordinate pos;
    private Point point;
    private List<Query> queries;


    public static class Builder {
        private static JMapViewer map;
        private ICoordinate pos;
        private Point point;
        private static List<Query> queries;

        public Builder() {
        }

        public Builder addMap(JMapViewer map) {

            this.map = map;
            return this;
        }

        public Builder addQueries(List<Query> queries) {

            this.queries = queries;
            return this;
        }

        public Builder addPosition(ICoordinate pos) {

            this.pos = pos;
            return this;
        }


        public Builder addPoint(Point point) {

            this.point = point;
            return this;

        }

        public void build() {

            ToolTipBuilder toolTipBuilder = new ToolTipBuilder(this);

            toolTipBuilder.map = this.map;
            toolTipBuilder.pos = this.pos;
            toolTipBuilder.point = this.point;
            toolTipBuilder.queries = this.queries;

            for (MapMarker m : getMarkersCovering(pos, pixelWidth(point))) {

                CustomizedMap marker = (CustomizedMap) m;

                String txt = "<html> <p>" + marker.getName() + " </p> " +
                        "<p> <img width= 75 height=75 src="
                        + marker.getProfilePhotoURL() + "></p><p>"
                        + marker.getTweet() + "</p></html>";

                map.setToolTipText(txt);

                UIManager.put("ToolTip.background", marker.getColor());
                UIManager.put("ToolTip.font", new Font("Arial", Font.BOLD, 12));

            }

        }

        private static double pixelWidth(Point p) {
            ICoordinate center = map.getPosition(p);
            ICoordinate edge = map.getPosition(new Point(p.x + 1, p.y));
            return SphericalGeometry.distanceBetween(center, edge);
        }

        private static Set<Layer> getVisibleLayers() {
            Set<Layer> ans = new HashSet<>();
            for (Query q : queries) {
                if (q.getVisible()) {
                    ans.add(q.getLayer());
                }
            }
            return ans;
        }

        private static List<MapMarker> getMarkersCovering(ICoordinate pos, double pixelWidth) {

            List<MapMarker> ans = new ArrayList<>();
            Set<Layer> visibleLayers = getVisibleLayers();
            for (MapMarker m : map.getMapMarkerList()) {
                if (!visibleLayers.contains(m.getLayer())) continue;
                double distance = SphericalGeometry.distanceBetween(m.getCoordinate(), pos);
                if (distance < m.getRadius() * pixelWidth) {
                    ans.add(m);
                }
            }
            return ans;
        }
    }


    private ToolTipBuilder(Builder builder) {

        this.map = builder.map;
        this.point = builder.point;
        this.pos = builder.pos;
        this.queries = builder.queries;
    }

    public JMapViewer getMap() {
        return map;
    }

    public Point getPoint() {
        return point;
    }


    public ICoordinate getPosition() {
        return pos;
    }


    public List<Query> getQueries() {
        return queries;
    }
}
