import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeController implements ChangeListener, ActionListener {

    // create timer
    int counter = 0;
    public Timer mazeTimer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            counter++;
            mazeView.timeLabel.setText("Time: " + counter);
        }
    });

    // create private instances of model and view to control
    private MazeModel mazeModel;
    private MazeView mazeView;

    // boolean to see which version is running
    public boolean isGenerating = false;
    public boolean isSolving = false;

    public MazeController(MazeModel model, MazeView view) {
        mazeModel = model;
        mazeView = view;
    }

    // slider listeners
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            if (source == mazeView.speedSlider) {
                mazeModel.genTimer.setDelay(source.getValue());
                mazeModel.solveTimer.setDelay(source.getValue());
            } else if (source == mazeView.rowsSlider) {
                reset(source.getValue(), mazeModel.cols);
                mazeView.rowsLabel.setText("Rows: " + Integer.toString(source.getValue()));
            } else if (source == mazeView.colsSlider) {
                reset(mazeModel.rows, source.getValue());
                mazeView.colsLabel.setText("Columns: " + Integer.toString(source.getValue()));
            }
        }
    }

    // button listeners
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mazeView.generateButton) {
            if (!mazeView.showGen.isSelected()) {
                if (!mazeModel.isDoneGenerating && !isSolving && !isGenerating) {
                    mazeModel.dfsGenNoTimer();
                    mazeTimer.start();
                    mazeView.percentVisited.setText("Percent Visited: 100%");
                    mazeView.statusLabel.setText("Maze Generated");
                }
            } else {
                if (!mazeModel.isDoneGenerating && !isSolving && !isGenerating) {
                    mazeView.statusLabel.setText("Generating...");
                    mazeModel.initialSpeed = mazeView.speedSlider.getValue();
                    mazeModel.dfsGenerate(mazeView, this);
                    mazeTimer.start();
                    isGenerating = true;
                }
            }
        } else if (e.getSource() == mazeView.solveButton) {
            if (!mazeView.showSolve.isSelected()) {
                if (!mazeModel.isDoneSolving && mazeModel.isDoneGenerating && !isSolving) {
                    mazeModel.dfsSolveNoTimer(mazeView, this);
                    mazeView.statusLabel.setText("Maze Solved!");
                }
            } else {
                if (!mazeModel.isDoneSolving && !isGenerating && !isSolving && mazeModel.isDoneGenerating) {
                    mazeView.statusLabel.setText("Solving...");
                    mazeModel.initialSpeed = mazeView.speedSlider.getValue();
                    mazeModel.dfsSolve(mazeView, this);
                    isSolving = true;
                }
            }
        } else if (e.getSource() == mazeView.stopButton) {
            if (isGenerating) {
                if (!mazeModel.isStopped) {
                    mazeModel.genTimer.stop();
                    mazeTimer.stop();
                    mazeView.statusLabel.setText("Paused");
                    mazeModel.isStopped = true;
                } else if (mazeModel.isStopped && !mazeModel.isDoneGenerating) {
                    mazeModel.genTimer.start();
                    mazeTimer.start();
                    mazeView.statusLabel.setText("Generating...");
                    mazeModel.isStopped = false;
                }
            } else if (isSolving) {
                if (!mazeModel.isStopped) {
                    mazeModel.solveTimer.stop();
                    mazeTimer.stop();
                    mazeView.statusLabel.setText("Paused");
                    mazeModel.isStopped = true;
                } else if (mazeModel.isStopped && !mazeModel.isDoneSolving) {
                    mazeModel.solveTimer.start();
                    mazeTimer.start();
                    mazeView.statusLabel.setText("Solving...");
                    mazeModel.isStopped = false;
                }
            }
        }
    }



    // reset the frame when rows or cols are changed
    public void reset(int rows, int cols) {
        mazeView.westPanel.removeAll();
        mazeView.percentVisited.setText("Percent Visited: 0.00%");
        mazeView.timeLabel.setText("Time: 0");
        mazeTimer.stop();
        counter = 0;
        mazeView.statusLabel.setText("Waiting...");
        mazeModel = new MazeModel(rows, cols);
        mazeView.westPanel.add(mazeModel);
        mazeView.revalidate();
        mazeView.repaint();
    }
}
