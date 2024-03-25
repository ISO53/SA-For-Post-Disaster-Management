package com.iso53.application;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerIJTheme;
import com.iso53.algorithm.ProblemData;
import com.iso53.algorithm.SimulatedAnnealing;
import com.iso53.algorithm.Solution;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.concurrent.ExecutionException;

public class App {
    private JPanel jpnl_backPanel;
    private JPanel leftPanel;
    private JPanel jpnl_rightPanel;
    private JButton jbtn_loadDistanceMatrixButton;
    private JButton jbtn_loadIncidentsButton;
    private JButton jbtn_loadProcessTimesButton;
    private JButton jbtn_loadUnitsButton;
    private JPanel jpnl_graphPanel;
    private JButton jbtn_startButton;
    private JProgressBar jprgrsbr_progressBar;
    private JLabel jlbl_distanceMatrixLoaded;
    private JLabel jlbl_incidentsLoaded;
    private JLabel jlbl_processTimesLoaded;
    private JLabel jlbl_unitsLoaded;

    private File distanceMatrixFile;
    private File incidentsFile;
    private File processTimesFile;
    private File unitsFile;

    private final Color ACCENT_COLOR = new Color(255, 152, 0);

    public App() {
        JFrame frame = new JFrame("Post Disaster Management");
        frame.setContentPane(jpnl_backPanel);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);

        buttonListeners();
    }

    public void checkDataLoadings() {
        if (jlbl_distanceMatrixLoaded.getText().equals("✔") && jlbl_incidentsLoaded.getText().equals("✔") && jlbl_unitsLoaded.getText().equals("✔") && jlbl_processTimesLoaded.getText().equals("✔")) {
            jbtn_startButton.setEnabled(true);
        }
    }

    public void buttonListeners() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON files", "json"));
        fileChooser.setCurrentDirectory(new File("C:\\Users\\termi\\Documents\\Projects\\SchoolProjects\\SA-For-Post" +
                "-Disaster-Management\\src\\main\\resources"));

        jbtn_loadDistanceMatrixButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    distanceMatrixFile = fileChooser.getSelectedFile();
                    jlbl_distanceMatrixLoaded.setText("✔");
                    jlbl_distanceMatrixLoaded.setForeground(ACCENT_COLOR);
                }

                checkDataLoadings();
            }
        });

        jbtn_loadIncidentsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    incidentsFile = fileChooser.getSelectedFile();
                    jlbl_incidentsLoaded.setText("✔");
                    jlbl_incidentsLoaded.setForeground(ACCENT_COLOR);
                }

                checkDataLoadings();
            }
        });

        jbtn_loadProcessTimesButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    processTimesFile = fileChooser.getSelectedFile();
                    jlbl_processTimesLoaded.setText("✔");
                    jlbl_processTimesLoaded.setForeground(ACCENT_COLOR);
                }

                checkDataLoadings();
            }
        });

        jbtn_loadUnitsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    unitsFile = fileChooser.getSelectedFile();
                    jlbl_unitsLoaded.setText("✔");
                    jlbl_unitsLoaded.setForeground(ACCENT_COLOR);
                }

                checkDataLoadings();
            }
        });

        jbtn_startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (!jbtn_startButton.isEnabled()) {
                    return;
                }

                ProblemData.init(processTimesFile, incidentsFile, unitsFile, distanceMatrixFile);

                SolutionBarChart solutionBarChart = new SolutionBarChart(ProblemData.UNITS, ProblemData.INCIDENTS);
                jpnl_graphPanel.setLayout(new BorderLayout());
                jpnl_graphPanel.add(solutionBarChart, BorderLayout.CENTER);
                solutionBarChart.revalidate();

                jprgrsbr_progressBar.setIndeterminate(true);

                SwingWorker<Solution, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Solution doInBackground() {
                        SimulatedAnnealing SA = new SimulatedAnnealing(1000, 0.0003, ProblemData.INCIDENTS);
                        return SA.run();
                    }

                    @Override
                    protected void done() {
                        try {
                            Solution solution = get();
                            solution.print();
                            jprgrsbr_progressBar.setIndeterminate(false);
                            solutionBarChart.setSolution(solution);
                        } catch (InterruptedException | ExecutionException ex) {
                            ex.printStackTrace();
                        }
                    }
                };

                worker.execute();
            }
        });
    }

    public static void main(String[] args) {
        FlatMaterialDarkerIJTheme.setup();
        SwingUtilities.invokeLater(App::new);
    }
}