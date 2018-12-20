import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MazeModel extends JPanel {

    // create variables for dfs algorithms
    private MazeElement currElement, randNeighbor, nextElement;
    private Stack<MazeElement> stack;
    private Queue<MazeElement> Q;
    public boolean isEmpty;
    private boolean isBacktracking = false;
    private boolean isFirstIt = true;
    private String direction;
    public boolean isDoneGenerating = false;
    public boolean isDoneSolving = false;
    public boolean isStopped = false;
    public double percentVisited = 0.0;

    // Create grid
    MazeElement[][] grid;

    // create rows and cols
    public int rows, cols, size;
    public int initialSpeed = 100;

    // create Timer for speed control for generation and solving
    public Timer genTimer = new Timer(initialSpeed, null);
    public Timer solveTimer = new Timer(initialSpeed, null);

    // Constructor
    public MazeModel(int numRows, int numCols) {
        // initialize grid with dynamic size
        rows = numRows;
        cols = numCols;
        if (rows > 50 || cols > 50) {
            size = 12;
        } else if (rows >= 40 || cols >= 40) {
            size = 14;
        } else if (rows >= 25 || cols >= 25) {
            size = 18;
        } else {
            size = 28;
        }

        // set panel properties
        setPreferredSize(new Dimension(cols * size, rows * size));
        setBackground(Color.black);
        setLayout(new FlowLayout());
        FlowLayout thisLayout = (FlowLayout) getLayout();
        thisLayout.setHgap(0);
        thisLayout.setVgap(0);

        grid = new MazeElement[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0 && j == 0) {
                    grid[i][j] = new MazeElement("start", size);
                } else if (i == rows - 1 && j == cols - 1) {
                    grid[i][j] = new MazeElement("end", size);
                } else {
                    grid[i][j] = new MazeElement(size);
                }
                add(grid[i][j]);
            }
        }
        // set neighbors for each cell
        assignNeighbors();
    }

    // generate maze with a timer
    public void dfsGenerate(MazeView mazeView, MazeController mazeController) {
        // create queue
        Q = new LinkedList<>();
        isEmpty = false;

        // set starting cell to random element
        currElement = grid[0][0];

        genTimer = new Timer(initialSpeed, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isEmpty || isStopped) {
                    ((Timer) e.getSource()).stop();
                } else {
                    // push tile onto Q and mark currElement as visited
                    ((LinkedList<MazeElement>) Q).push(currElement);
                    currElement.setVisited(true);

                    // if all neighbors are visited
                    while (currElement.isAllVisited()) {

                        // Find a node with non visited neighbor
                        if (Q.size() != 0) {
                            currElement = ((LinkedList<MazeElement>) Q).pop();
                        } else {
                            isDoneGenerating = true;
                            isEmpty = true;
                            mazeController.isGenerating = false;
                            mazeView.percentVisited.setText("Percent Visited: 100%");
                            mazeView.statusLabel.setText("Maze Generated");
                            break;
                        }
                    }

                    // check if no nodes exist
                    if (!isEmpty) {
                        // Randomly select a neighbor of that node that has not been visited
                        randNeighbor = getRandomNeighbor(currElement);
                        while (randNeighbor.getVisited()) {
                            randNeighbor = getRandomNeighbor(currElement);
                        }

                        if (randNeighbor == currElement.getNorth()) {
                            currElement.setIsNorth(false);
                            randNeighbor.setIsSouth(false);
                        } else if (randNeighbor == currElement.getSouth()) {
                            currElement.setIsSouth(false);
                            randNeighbor.setIsNorth(false);
                        } else if (randNeighbor == currElement.getEast()) {
                            currElement.setIsEast(false);
                            randNeighbor.setIsWest(false);
                        } else if (randNeighbor == currElement.getWest()) {
                            currElement.setIsWest(false);
                            randNeighbor.setIsEast(false);
                        }

                        // update Maze Elements
                        currElement.drawSelf();
                        randNeighbor.drawSelf();

                        // get percent visited and update label
                        percentVisited = getPercentVisited();
                        mazeView.percentVisited.setText("Percent Visited: " + percentVisited + "%");

                        // set currElement to randNeighbor and repeat
                        currElement = randNeighbor;
                    }
                }
            }
        });
        genTimer.start();
    }

    // generate maze with no timer
    public void dfsGenNoTimer() {
        // create queue
        Q = new LinkedList<>();
        isEmpty = false;

        // set starting cell to random element
        currElement = grid[0][0];

        while (!isEmpty) {
            // push tile onto Q and mark currElement as visited
            ((LinkedList<MazeElement>) Q).push(currElement);
            currElement.setVisited(true);

            // if all neighbors are visited
            while (currElement.isAllVisited()) {

                // Find a node with non visited neighbor
                if (Q.size() != 0) {
                    currElement = ((LinkedList<MazeElement>) Q).pop();
                } else {
                    isDoneGenerating = true;
                    isEmpty = true;
                    break;
                }
            }

            // check if no nodes exist
            if (!isEmpty) {
                // Randomly select a neighbor of that node that has not been visited
                randNeighbor = getRandomNeighbor(currElement);
                while (randNeighbor.getVisited()) {
                    randNeighbor = getRandomNeighbor(currElement);
                }

                if (randNeighbor == currElement.getNorth()) {
                    currElement.setIsNorth(false);
                    randNeighbor.setIsSouth(false);
                } else if (randNeighbor == currElement.getSouth()) {
                    currElement.setIsSouth(false);
                    randNeighbor.setIsNorth(false);
                } else if (randNeighbor == currElement.getEast()) {
                    currElement.setIsEast(false);
                    randNeighbor.setIsWest(false);
                } else if (randNeighbor == currElement.getWest()) {
                    currElement.setIsWest(false);
                    randNeighbor.setIsEast(false);
                }

                // update Maze Elements
                currElement.drawSelf();
                randNeighbor.drawSelf();

                // set currElement to randNeighbor and repeat
                currElement = randNeighbor;
            }
        }
    }

    // get a random neighbor with the current element
    public MazeElement getRandomNeighbor(MazeElement currElement) {
        MazeElement[] neighbors = currElement.getNeighbors();

        int randIndex = (int)(Math.random() * neighbors.length);
        while (neighbors[randIndex] == null) {
            randIndex = (int)(Math.random() * neighbors.length);
        }
        MazeElement neighbor = neighbors[randIndex];
        return neighbor;
    }

    // Assign neighbors to all cells
    public void assignNeighbors() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isInGrid(i-1,j)) { grid[i][j].setNorth(grid[i-1][j]); }
                if (isInGrid(i, j+1)) { grid[i][j].setEast(grid[i][j+1]); }
                if (isInGrid(i+1,j)) { grid[i][j].setSouth(grid[i+1][j]); }
                if (isInGrid(i, j-1)) { grid[i][j].setWest(grid[i][j-1]); }
            }
        }
    }

    // helper method to check if cell is valid
    public boolean isInGrid(int x, int y) {
        if (x >= 0 && y >= 0 && x < rows && y < cols) { return true; }
        else { return false; }
    }

    // returns the current percent of visited cells during generation
    public double getPercentVisited() {
        double numVisited = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j].getVisited()) {
                    numVisited++;
                }
            }
        }

        double percentage = 100 * (numVisited / (rows * cols));
        new DecimalFormat("#.##").format(percentage);
        return Math.floor(percentage * 100) / 100;
    }

    // returns the current percent of used cells during solving
    public double getPercentUsed() {
        double numUsed = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j].getUsed()) {
                    numUsed++;
                }
            }
        }

        double percentage = 100 * (numUsed / (rows * cols));
        new DecimalFormat("#.##").format(percentage);
        return Math.floor(percentage * 100) / 100;
    }

    // solve over time
    public void dfsSolve(MazeView mazeView, MazeController mazeController) {
        // create queue
        stack = new Stack<>();
        isEmpty = false;
        direction = "south";

        // set starting cell to current element
        currElement = grid[0][0];

        solveTimer = new Timer(initialSpeed, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (isEmpty || isStopped) {
                    ((Timer) e.getSource()).stop();
                } else {
                    // check for backtracking
                    if (isBacktracking) {

                        if (isFirstIt) {
                            currElement.setBacktracked();
                            currElement.drawSelf();
                            currElement = stack.pop();
                            isFirstIt = false;
                        }

                        if (currElement.isPath().equals("none")) {
                            currElement.setBacktracked();
                            currElement.drawSelf();
                            currElement = stack.pop();
                        }

                        if (!currElement.isPath().equals("none")) {
                            direction = currElement.isPath();
                            currElement.setBacktracked();
                            currElement.drawSelf();
                            isFirstIt = true;
                            isBacktracking = false;
                        }
                    } else {
                        // push tile onto Q and mark currElement as visited
                        stack.push(currElement);
                        currElement.setUsed();

                        // check if we have reached the end
                        if (grid[rows-1][cols-1] == currElement) {
                            isEmpty = true;
                            isStopped = true;
                            isDoneSolving = true;
                            mazeController.isSolving = false;
                            mazeController.mazeTimer.stop();
                            mazeView.statusLabel.setText("Solved!");
                        }

                        // if we haven't
                        if (!isEmpty) {
                            if (direction.equals("south")) {
                                if (currElement.getSouthCode() != 1) {
                                    nextElement = currElement.getSouth();
                                } else if (currElement.getWestCode() != 1) {
                                    direction = "west";
                                } else if (currElement.getEastCode() != 1) {
                                    direction = "east";
                                } else  {
                                    direction = "north";
                                    isBacktracking = true;
                                }
                            }
                            if (direction.equals("east")) {
                                if (currElement.getEastCode() != 1) {
                                    nextElement = currElement.getEast();
                                } else if (currElement.getSouthCode() != 1) {
                                    direction = "south";
                                    nextElement = currElement.getSouth();
                                } else if (currElement.getNorthCode() != 1) {
                                    direction = "north";
                                } else {
                                    direction = "west";
                                    isBacktracking = true;
                                }

                            }
                            if (direction.equals("north")) {
                                if (currElement.getNorthCode() != 1) {
                                    nextElement = currElement.getNorth();
                                } else if (currElement.getEastCode() != 1) {
                                    direction = "east";
                                    nextElement = currElement.getEast();
                                } else if (currElement.getWestCode() != 1) {
                                    direction = "west";
                                } else {
                                    direction = "south";
                                    isBacktracking = true;
                                }
                            }
                            if (direction.equals("west")) {
                                if (currElement.getWestCode() != 1) {
                                    nextElement = currElement.getWest();
                                } else if (currElement.getNorthCode() != 1) {
                                    direction = "north";
                                    nextElement = currElement.getNorth();
                                } else if (currElement.getSouthCode() != 1) {
                                    direction = "south";
                                    nextElement = currElement.getSouth();
                                } else {
                                    direction = "east";
                                    isBacktracking = true;
                                }
                            }
                        }
                        if (!isBacktracking) {
                            currElement.drawSelf();
                            currElement = nextElement;
                        }
                    }

                    // get percent visited and update label
                    percentVisited = getPercentUsed();
                    mazeView.percentVisited.setText("Percent Visited: " + percentVisited + "%");
                }
            }
        });
        solveTimer.start();
    }

    // Solve instantly
    public void dfsSolveNoTimer(MazeView mazeView, MazeController mazeController) {
        // create queue
        stack = new Stack<>();
        isEmpty = false;
        direction = "south";

        // set starting cell to current element
        currElement = grid[0][0];

        while (!isEmpty) {
                // push tile onto Q and mark currElement as visited
                stack.push(currElement);
                currElement.setUsed();

                // check if we have reached the end
                if (grid[rows-1][cols-1] == currElement) {
                    isDoneSolving = true;
                    isEmpty = true;
                    isStopped = true;
                    mazeController.isSolving = false;
                    mazeController.mazeTimer.stop();
                    mazeView.statusLabel.setText("Solved!");
                }

                // if we haven't
                if (!isEmpty) {
                    if (direction.equals("south")) {
                        if (currElement.getSouthCode() != 1) {
                            nextElement = currElement.getSouth();
                        } else if (currElement.getWestCode() != 1) {
                            direction = "west";
                        } else if (currElement.getEastCode() != 1) {
                            direction = "east";
                        } else  {
                            direction = "north";
                            isBacktracking = true;
                        }
                    }
                    if (direction.equals("east")) {
                        if (currElement.getEastCode() != 1) {
                            nextElement = currElement.getEast();
                        } else if (currElement.getSouthCode() != 1) {
                            direction = "south";
                            nextElement = currElement.getSouth();
                        } else if (currElement.getNorthCode() != 1) {
                            direction = "north";
                        } else {
                            direction = "west";
                            isBacktracking = true;
                        }

                    }
                    if (direction.equals("north")) {
                        if (currElement.getNorthCode() != 1) {
                            nextElement = currElement.getNorth();
                        } else if (currElement.getEastCode() != 1) {
                            direction = "east";
                            nextElement = currElement.getEast();
                        } else if (currElement.getWestCode() != 1) {
                            direction = "west";
                        } else {
                            direction = "south";
                            isBacktracking = true;
                        }
                    }
                    if (direction.equals("west")) {
                        if (currElement.getWestCode() != 1) {
                            nextElement = currElement.getWest();
                        } else if (currElement.getNorthCode() != 1) {
                            direction = "north";
                            nextElement = currElement.getNorth();
                        } else if (currElement.getSouthCode() != 1) {
                            direction = "south";
                            nextElement = currElement.getSouth();
                        } else {
                            direction = "east";
                            isBacktracking = true;
                        }
                    }

                    // if we are backtracking, continue popping off stack
                    if (isBacktracking) {
                        currElement.setBacktracked();
                        currElement.drawSelf();
                        currElement = stack.pop();

                        while (currElement.isPath().equals("none")) {
                            currElement.setBacktracked();
                            currElement.drawSelf();
                            currElement = stack.pop();
                        }
                        direction = currElement.isPath();
                        currElement.setBacktracked();
                        currElement.drawSelf();
                        isBacktracking = false;
                    } else {
                        currElement.drawSelf();
                        currElement = nextElement;
                    }
                }

                // get percent visited and update label
                percentVisited = getPercentUsed();
                mazeView.percentVisited.setText("Percent Visited: " + percentVisited + "%");
        }
    }
}
