package com.iso53.application;

import com.iso53.algorithm.Incident;
import com.iso53.algorithm.ProblemData;
import com.iso53.algorithm.Solution;
import com.iso53.algorithm.Unit;

import javax.swing.*;
import java.awt.*;

public class SolutionBarChart extends JPanel {

    private final int MARGIN = 10;
    private final int SMALL_MARGIN = 5;
    private final int GRAPH_MARGIN = 30;
    private final int SHORT_LINE_LENGTH = 5;
    private final int UNIT_INFO_SPACE = 60;
    private final int TIME_INFO_SPACE = 10;
    private final int TIME_LINE_COUNT = 15;

    private final Unit[] units;
    private final Incident[] incidents;
    private Solution solution;

    private double solutionMaxLength;

    public SolutionBarChart(Unit[] units, Incident[] incidents) {
        this.units = units;
        this.incidents = incidents;
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
        this.solutionMaxLength = solution.getMaxLineLength();
    }

    // be aware, only god knows the calculations beyond this point
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Get height and weight
        int height = getHeight();
        int width = getWidth();

        double minScl = ProblemData.MIN_SCALE_FACTOR;
        double maxScl = ProblemData.MAX_SCALE_FACTOR;
        double range = maxScl - minScl;

        // Set thickness
        g2.setStroke(new BasicStroke(2));

        // Draw y-axis
        g2.drawLine(
                MARGIN + UNIT_INFO_SPACE + SMALL_MARGIN + SHORT_LINE_LENGTH,
                MARGIN,
                MARGIN + UNIT_INFO_SPACE + SMALL_MARGIN + SHORT_LINE_LENGTH,
                height - (MARGIN + TIME_INFO_SPACE + SMALL_MARGIN + SHORT_LINE_LENGTH));

        // Draw x-axis
        g2.drawLine(
                MARGIN + UNIT_INFO_SPACE + SMALL_MARGIN + SHORT_LINE_LENGTH,
                height - (MARGIN + TIME_INFO_SPACE + SMALL_MARGIN + SHORT_LINE_LENGTH),
                width - MARGIN,
                height - (MARGIN + TIME_INFO_SPACE + SMALL_MARGIN + SHORT_LINE_LENGTH));

        // Draw units to y-axis
        if (units != null) {
            for (int i = 0; i < units.length; i++) {
                // Draw short lines
                int x1 = MARGIN + UNIT_INFO_SPACE + SMALL_MARGIN;
                int x2 = x1 + SHORT_LINE_LENGTH;
                int y = MARGIN + GRAPH_MARGIN + (height - ((MARGIN + GRAPH_MARGIN) + (MARGIN + TIME_INFO_SPACE + SMALL_MARGIN + SHORT_LINE_LENGTH + GRAPH_MARGIN))) / (units.length - 1) * i;
                g2.drawLine(x1, y, x2, y);

                // Draw unit information
                g2.drawString(units[i].getType(), x1 - UNIT_INFO_SPACE - SMALL_MARGIN, y);
                g2.drawString(
                        units[i].getName()
                                .replace(", ", "")
                                .replace("~", ""),
                        x1 - UNIT_INFO_SPACE - SMALL_MARGIN, y + g2.getFontMetrics().getHeight());
            }
        }

        // scale process and distance time bars based on width
        double scale =
                (width - (2 * MARGIN + GRAPH_MARGIN + UNIT_INFO_SPACE + SMALL_MARGIN + SHORT_LINE_LENGTH)) / ((solutionMaxLength - minScl) / range);

        // Draw times to x-axis
        for (int i = 0; i < TIME_LINE_COUNT; i++) {
            // Draw line
            int y1 = height - (MARGIN + TIME_INFO_SPACE + SMALL_MARGIN + SHORT_LINE_LENGTH);
            int y2 = y1 + SHORT_LINE_LENGTH;
            int x = MARGIN + UNIT_INFO_SPACE + SHORT_LINE_LENGTH + SMALL_MARGIN + (width - (2 * MARGIN + UNIT_INFO_SPACE + SHORT_LINE_LENGTH + SMALL_MARGIN)) / TIME_LINE_COUNT * i;
            if (i != 0) {
                g2.drawLine(x, y1, x, y2);
            }

            // Draw time value
            if (solution != null) {
                double timeValue = solutionMaxLength / TIME_LINE_COUNT * i;
                String timeValueStr = String.format("%.2f", timeValue);
                g2.drawString(timeValueStr, x - g2.getFontMetrics().stringWidth(timeValueStr) / 2, y1 + GRAPH_MARGIN);
            }
        }

        // Draw process and distance bars
        if (solution != null) {
            for (int i = 0; i < solution.getSolution().size(); i++) {
                double totalBar = 0;

                for (int j = 0; j < solution.getSolution().get(i).size(); j++) {
                    totalBar += ((solution.getSolution().get(i).get(j).getDistanceTime()) * scale)
                            + ((solution.getSolution().get(i).get(j).getProcessTime()) * scale);
                }

                g2.setStroke(new BasicStroke(2));
                g2.setColor(new Color(190, 190, 190));
                int x1 = (int) (MARGIN + UNIT_INFO_SPACE + SMALL_MARGIN + SHORT_LINE_LENGTH);
                int x2 = (int) (x1 + totalBar);
                int y = MARGIN + GRAPH_MARGIN + (height - ((MARGIN + GRAPH_MARGIN) + (MARGIN + TIME_INFO_SPACE + SMALL_MARGIN + SHORT_LINE_LENGTH + GRAPH_MARGIN))) / (units.length - 1) * i;
                g2.fillRect(x1, y - 10, x2 - x1, 20);

                totalBar = 0;
                for (int j = 0; j < solution.getSolution().get(i).size(); j++) {
                    double distanceBar =
                            (solution.getSolution().get(i).get(j).getDistanceTime()) * scale;
                    double processBar =
                            (solution.getSolution().get(i).get(j).getProcessTime()) * scale;

                    g2.setColor(new Color(190, 190, 190));
                    x1 = (int) (MARGIN + UNIT_INFO_SPACE + SMALL_MARGIN + SHORT_LINE_LENGTH + totalBar);
                    x2 = (int) (x1 + distanceBar);
                    g2.fillRect(x1, y - 10, x2 - x1, 20);
                    totalBar += distanceBar;

                    g2.setColor(new Color(100, 100, 100));
                    x1 = (int) (MARGIN + UNIT_INFO_SPACE + SMALL_MARGIN + SHORT_LINE_LENGTH + totalBar);
                    x2 = (int) (x1 + processBar);
                    g2.fillRect(x1, y - 10, x2 - x1, 20);
                    totalBar += processBar;
                }
            }
        }
    }
}
