public class Maze {

    public static void main(String[] args) {
        // create model object
        MazeModel mazeModel = new MazeModel(40, 40);
        MazeView mazeView = new MazeView(mazeModel);
        MazeController mazeController = new MazeController(mazeModel, mazeView);

        // register listeners
        mazeView.registerListeners(mazeController);
    }
}
