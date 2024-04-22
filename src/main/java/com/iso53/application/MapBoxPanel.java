package com.iso53.application;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import com.iso53.algorithm.Event;
import com.iso53.algorithm.Incident;
import com.iso53.algorithm.ProblemData;
import org.iso53.InteractiveImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class MapBoxPanel extends InteractiveImagePanel {

    private final String BASE_URL;
    private final String ACCESS_TOKEN;
    private final ArrayList<Overlay> overlays;
    private final String RED = "ff0000";
    private final String GREEN = "006400";
    private final String BLUE = "0000ff";
    private final String ORANGE = "ff9800";

    public MapBoxPanel() {
        this.ACCESS_TOKEN = "ACCESS_TOKEN";
        this.BASE_URL = "https://api.mapbox.com/styles/v1/mapbox/dark-v11/static/";
        this.overlays = new ArrayList<>();

        for (Incident incident : ProblemData.INCIDENTS) {
            overlays.add(new Marker(incident.getLat(), incident.getLon(), RED, "i"));
        }
        overlays.add(new Marker(ProblemData.HEADQUARTER.lat, ProblemData.HEADQUARTER.lon, GREEN, "h"));
    }

    public void setUnitSolution(LinkedList<Event> events) {
        double[][] locations = new double[events.size()][2];

        for (int i = 0; i < locations.length; i++) {
            int unitLocIndex = events.get(i).getUnitIndex();
            if (unitLocIndex == ProblemData.INCIDENTS.length) {
                locations[i][0] = ProblemData.HEADQUARTER.lat;
                locations[i][1] = ProblemData.HEADQUARTER.lon;
            } else {
                locations[i][0] = ProblemData.INCIDENTS[unitLocIndex].getLat();
                locations[i][1] = ProblemData.INCIDENTS[unitLocIndex].getLon();
            }
        }

        overlays.add(new Path(ORANGE, locations));
    }

    private BufferedImage getMapImage() throws IOException {
        URL url = new URL(buildRequestString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStream in = conn.getInputStream();
        return ImageIO.read(in);
    }

    public void setImage() {
        SwingWorker<BufferedImage, Void> worker = new SwingWorker<>() {
            @Override
            protected BufferedImage doInBackground() throws Exception {
                return getMapImage();
            }

            @Override
            protected void done() {
                try {
                    BufferedImage img = get();
                    MapBoxPanel.this.setImage(img);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    private String buildRequestString() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        return String.format(
                Locale.US,
                "%s%s/auto/%dx%d@2x?access_token=%s",
                BASE_URL,
                overlays.stream()
                        .map(Overlay::getAsString)
                        .collect(Collectors.joining(",")),
                (int) Math.max(Math.min(screenSize.getWidth(), 1280), 1),
                (int) Math.max(Math.min(screenSize.getHeight(), 1280), 1),
                ACCESS_TOKEN);
    }

    private abstract static class Overlay {
        abstract String getAsString();
    }

    private class Marker extends Overlay {
        private final double latitude;
        private final double longitude;
        private final String color;
        private final String label;

        public Marker(double latitude, double longitude, String color, String label) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.color = color;
            this.label = label;
        }

        @Override
        String getAsString() {
            return String.format(Locale.US, "pin-s-%s+%s(%.4f,%.4f)", label, color, longitude, latitude);
        }
    }

    private class Path extends Overlay {
        private final String color;
        private final String encodedPolyline;

        public Path(String color, double[][] locations) {
            this.color = color;

            ArrayList<LatLng> path = new ArrayList<>();
            for (double[] location : locations) {
                path.add(new LatLng(location[0], location[1]));
            }
            this.encodedPolyline = PolylineEncoding.encode(path);
        }

        @Override
        String getAsString() {
            return String.format(Locale.US, "path-2+%s(%s)", color, encodedPolyline);
        }
    }
}
