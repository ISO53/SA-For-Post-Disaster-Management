package com.iso53.application;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import com.iso53.algorithm.Event;
import com.iso53.algorithm.Incident;
import com.iso53.algorithm.ProblemData;
import org.iso53.InteractiveImagePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final String YELLOW = "ffff00";
    private final String ORANGE = "ff9800";

    public MapBoxPanel() {
        // DON'T PUSH THIS LINE TO GITHUB !!!
        this.ACCESS_TOKEN = "pk.eyJ1IjoiaXNvNTMiLCJhIjoiY2x2NndpMzIyMDM3NTJrcWltcmt2bWVhciJ9.lHDfJfK_XCnRHkWqtyeRLw";
        this.BASE_URL = "https://api.mapbox.com/styles/v1/mapbox/dark-v11/static/";
        this.overlays = new ArrayList<>();

        for (Incident incident : ProblemData.INCIDENTS) {
            overlays.add(new Marker(incident.getLat(), incident.getLon(), RED, "i"));
        }
        overlays.add(new Marker(ProblemData.HEADQUARTER.lat, ProblemData.HEADQUARTER.lon, GREEN, "h"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Cast Graphics to Graphics2D for better rendering
        Graphics2D g2d = (Graphics2D) g;

        // Define the radius and initial position for the oval
        int radius = 15;
        int initialX = 10;
        int initialY = 10;

        // Define the strings to be displayed
        String[] strings = { "Starting Path", "Path", "End Path" };
        Color[] colors = { Color.decode("#" + RED), Color.decode("#" + ORANGE), Color.decode("#" + YELLOW) };

        // Draw the strings and the ovals
        for (int i = 0; i < strings.length; i++) {
            g2d.setColor(colors[i]);
            g2d.fillOval(initialX, initialY + i * (radius + 20), radius, radius);
            g2d.setColor(Color.WHITE);
            g2d.drawString(strings[i], initialX + radius + 10, initialY + i * (radius + 20) + 10);
        }
    }

    public void setUnitSolution(LinkedList<Event> events) {
        double[][] locations = new double[events.size() + 1][2];
        System.out.println(locations.length);

        for (int i = 0; i < locations.length; i++) {
            // Return back to HEADQUARTER once finished
            if (i == locations.length - 1) {
                locations[i][0] = ProblemData.HEADQUARTER.lat;
                locations[i][1] = ProblemData.HEADQUARTER.lon;
                break;
            }

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
        overlays.add(new Path(RED, Arrays.copyOfRange(locations, 0, 2)));
        overlays.add(new Path(YELLOW, Arrays.copyOfRange(locations, locations.length - 2, locations.length)));
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
