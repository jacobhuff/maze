import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class MazeElement extends JPanel {

    // create element properties
    private MazeElement north, south, east, west;
    private boolean isNorth = true;
    private boolean isSouth = true;
    private boolean isEast = true;
    private boolean isWest = true;
    private boolean isVisited = false;
    private boolean isBacktracked = false;
    private boolean isUsed = false;

    private int northCode = 1;
    private int southCode = 1;
    private int eastCode = 1;
    private int westCode = 1;

    // Constructor
    public MazeElement(int size) {
        setPreferredSize(new Dimension(size, size));
        setBorder(new MatteBorder(1,1,1,1,Color.white));
        setBackground(Color.black);
    }

    // Constructor for start and end cells
    public MazeElement(String type, int size) {
        setPreferredSize(new Dimension(size, size));
        setBorder(new MatteBorder(1,1,1,1,Color.white));
        if (type.equals("start")) {
            setBackground(Color.green);
        } else if (type.equals("end")) {
            setBackground(Color.RED);
        }
    }

    // draws the cell in its current state
    public void drawSelf() {
        if (!isNorth) { northCode = 0; }
        if (!isSouth) { southCode = 0; }
        if (!isEast) { eastCode = 0; }
        if (!isWest) { westCode = 0; }

        setBorder(new MatteBorder(northCode, westCode, southCode, eastCode, Color.white));
        if (isUsed && !isBacktracked) { setBackground(Color.blue); }
        if (isBacktracked) { setBackground(Color.darkGray); }
    }

    // accessors and mutators for backtracking and solving
    public void setBacktracked() { isBacktracked = true; }
    public void setUsed() { isUsed = true; }
    public boolean getUsed() { return isUsed; }

    // accessors for wall codes
    public int getNorthCode() { return northCode; }
    public int getSouthCode() { return southCode; }
    public int getEastCode() { return eastCode; }
    public int getWestCode() { return westCode; }
    public int[] getWalls() {
        int[] walls = {getNorthCode(), getSouthCode(), getEastCode(), getWestCode()};
        return walls;
    }

    // Accessors and mutators for neighbors
    public void setNorth(MazeElement m) { this.north = m; }
    public void setSouth(MazeElement m) { this.south = m; }
    public void setEast (MazeElement m) { this.east = m; }
    public void setWest (MazeElement m) { this.west = m; }
    public MazeElement getNorth() { return north; }
    public MazeElement getSouth() { return south; }
    public MazeElement getEast() { return east; }
    public MazeElement getWest() { return west; }

    // returns all neighbors of current element
    public MazeElement[] getNeighbors() {
        MazeElement[] neighbors = {getNorth(), getSouth(), getWest(), getEast()};
        return neighbors;
    }

    // checks if element is at dead end
    public boolean isDeadEnd() {
        int[] walls =  getWalls();
        int counter = 0;
        for (int i = 0; i < walls.length; i++) {
            if (walls[i] == 1) { counter++; }
        }
        if (counter > 2) { return true; }
        else { return false; }
    }

    // Accessors and mutators for walls
    public void setIsNorth(boolean bool) { isNorth = bool; }
    public void setIsSouth(boolean bool) { isSouth = bool; }
    public void setIsEast(boolean bool) { isEast = bool; }
    public void setIsWest(boolean bool) { isWest = bool; }

    // Accessors and mutators for visited
    public void setVisited(boolean visited) { isVisited = visited; }
    public boolean getVisited() { return isVisited; }
    public boolean isAllVisited() {
        if (getNorth() != null) {
            if (!getNorth().getVisited()) { return false; }
        }
        if (getSouth() != null) {
            if (!getSouth().getVisited()) { return false; }
        }
        if (getEast() != null) {
            if (!getEast().getVisited()) { return false; }
        }
        if (getWest() != null) {
            if (!getWest().getVisited()) { return false; }
        }
        return true;
    }
    public String isPath() {
        if (getNorth() != null) {
            if (!getNorth().isUsed && getNorthCode() == 0) { return "north"; }
        }
        if (getSouth() != null) {
            if (!getSouth().isUsed && getSouthCode() == 0) { return "south"; }
        }
        if (getEast() != null) {
            if (!getEast().isUsed && getEastCode() == 0) { return "east"; }
        }
        if (getWest() != null) {
            if (!getWest().isUsed && getWestCode() == 0) { return "west"; }
        }
        return "none";
    }
}
