import javax.swing.*;
import java.awt.*;

public class MazeView extends JFrame {

    public MazeModel mazeModel;

    // Allocate Components
    public JPanel westPanel, eastPanel, statusPanel;
    public JButton generateButton, solveButton, stopButton;
    public JCheckBox showGen, showSolve;
    public JSlider speedSlider, rowsSlider, colsSlider;
    public JLabel timeLabel, percentVisited, statusLabel, speedLabel, rowsLabel, colsLabel;
    private FlowLayout westPanelLayout;

    public MazeView(MazeModel model) {
        // get pane
        Container c = getContentPane();

        // create maze model
        mazeModel = model;

        // set up west panel
        westPanel = new JPanel(new FlowLayout());
        westPanelLayout = (FlowLayout)westPanel.getLayout();
        westPanelLayout.setVgap(1);
        westPanel.setPreferredSize(new Dimension(756, 756));

        // setup east panel
        eastPanel = new JPanel(new FlowLayout());
        FlowLayout eastPanelLayout = (FlowLayout)eastPanel.getLayout();
        eastPanelLayout.setVgap(15);
        eastPanel.setPreferredSize(new Dimension(300, 756));

        // setup buttons
        generateButton = new JButton("Generate");
        generateButton.setPreferredSize(new Dimension(125,35));
        generateButton.setFocusPainted(false);
        solveButton = new JButton("Solve");
        solveButton.setPreferredSize(new Dimension(125,35));
        solveButton.setFocusPainted(false);
        stopButton = new JButton("Start/Stop");
        stopButton.setPreferredSize(new Dimension(250, 50));
        stopButton.setFocusPainted(false);

        // setup check boxes
        showGen = new JCheckBox("Show Generation");
        showGen.setPreferredSize(new Dimension(125, 35));
        showGen.setFocusPainted(false);
        showSolve = new JCheckBox("Show Solver");
        showSolve.setPreferredSize(new Dimension(125, 35));
        showSolve.setFocusPainted(false);

        // setup sliders
        speedSlider = new JSlider(JSlider.HORIZONTAL, 0, 300, model.initialSpeed);
        speedSlider.setName("speed");
        rowsSlider = new JSlider(JSlider.HORIZONTAL, 10, 60, model.rows);
        rowsSlider.setName("rows");
        colsSlider = new JSlider(JSlider.HORIZONTAL, 10, 60, model.rows);
        colsSlider.setName("cols");

        // setup JLabels
        timeLabel = new JLabel("Time: 0");
        timeLabel.setPreferredSize(new Dimension(225, 25));
        timeLabel.setForeground(Color.white);
        percentVisited = new JLabel("Percent Visited: 0.00%");
        percentVisited.setPreferredSize(new Dimension(225, 25));
        percentVisited.setForeground(Color.white);
        statusLabel = new JLabel("Waiting...");
        statusLabel.setPreferredSize(new Dimension(225, 25));
        statusLabel.setForeground(Color.white);
        speedLabel = new JLabel("Speed");
        speedLabel.setPreferredSize(new Dimension(200, 25));
        rowsLabel = new JLabel("Rows: " + rowsSlider.getValue());
        rowsLabel.setPreferredSize(new Dimension(200, 25));
        colsLabel = new JLabel("Columns: " + colsSlider.getValue());
        colsLabel.setPreferredSize(new Dimension(200, 25));

        // create status panel
        statusPanel = new JPanel(new FlowLayout());
        FlowLayout statusPanelLayout = (FlowLayout)statusPanel.getLayout();
        statusPanelLayout.setVgap(60);
        statusPanel.setPreferredSize(new Dimension(300, 325));
        statusPanel.setBackground(Color.BLUE);
        statusPanel.add(statusLabel);
        statusPanel.add(timeLabel);
        statusPanel.add(percentVisited);

        // add contents to east panel
        eastPanel.add(generateButton);
        eastPanel.add(solveButton);
        eastPanel.add(showGen);
        eastPanel.add(showSolve);
        eastPanel.add(speedLabel);
        eastPanel.add(speedSlider);
        eastPanel.add(rowsLabel);
        eastPanel.add(rowsSlider);
        eastPanel.add(colsLabel);
        eastPanel.add(colsSlider);
        eastPanel.add(stopButton);
        eastPanel.add(statusPanel);

        // add panels to pane
        westPanel.add(mazeModel);
        c.add(westPanel, BorderLayout.WEST);
        c.add(eastPanel, BorderLayout.EAST);

        // set the frame to visible
        setSize(1056, 756);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void registerListeners(MazeController listener) {
        generateButton.addActionListener(listener);
        solveButton.addActionListener(listener);
        showGen.addActionListener(listener);
        showSolve.addActionListener(listener);
        speedSlider.addChangeListener(listener);
        rowsSlider.addChangeListener(listener);
        colsSlider.addChangeListener(listener);
        stopButton.addActionListener(listener);
    }
}
