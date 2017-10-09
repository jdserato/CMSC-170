package src.Maze;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Serato, Jay Vince; Acal, Winnah Gwen; and Hernandez, Danielle on September 24, 2017.
 */
public class Main {
    private static Node[][] maze;
    private static ArrayList<Node> openList = new ArrayList<>();
    private static ArrayList<Node> closedList = new ArrayList<>();
    static Scanner sc = new Scanner(System.in); // The scanner "sc" will ask the user for input from the System console.
    static int row = 0, col = 0;

    public static void main(String[] args) {
        Node current = null; // The "current" will be used as a pointer in the maze
        ArrayList<Node> goals = new ArrayList<>(); // The "goals" are a collection of the pointer of the goals.
        BufferedReader br; // The buffered reader "br" will read the contents of the file.
        int part; // The variable "part" will identify which part of the problem shall be solved;
        int specPart; // the "specPart" will specify which maze or search will be executed.
        int method; // The method will identify to either use Manhattan Distance or Straight Line Distance will be used to calculate H.




        /**
         * The following code will ask the user for "part", "specPart", and method.
         */
        System.out.println("Which part would you want me to solve:" +
                "\n\t1. Part 1: Basic Path Finding" +
                "\n\t2. Part 2: Search with Multiple Goals");
        part = sc.nextInt();
        switch (part) {
            case 1:
                System.out.println("Which of the following mazes would you want me to solve:" +
                        "\n\t1. Tiny Maze [7 x 7]" +
                        "\n\t2. Small Maze [10 x 22]" +
                        "\n\t3. Medium Maze [18 x 36]"+
                        "\n\t4. Open Maze [37 x 23]"+
                        "\n\t5. Big Maze [37 x 37]");
                specPart = sc.nextInt();
                break;
            case 2:
                System.out.println("Which of the following searches would you want me to solve:" +
                        "\n\t1. Small Search [5 x 20]" +
                        "\n\t2. Tricky Search [7 x 20]" +
                        "\n\t3. Medium Search [8 x 31]" +
                        "\n\t4. Big Search [15 x 31]");
                specPart = sc.nextInt();
                break;
            default:
                System.out.println("Error!");
                return;
        }
        System.out.println("Method of Solving the maze:" +
                "\n\t1. Part 1: Manhattan Distance" +
                "\n\t2. Part 2: Straight Line Distance");
        method = sc.nextInt();

        /**
         * The following code will set the filenames of the maze or search to be executed.
         */
        String filename = "";
        try {
            if (specPart == 1 && part == 1) {
                filename = "tinyMaze.lay.txt";
                row = 7;
                col = 7;
            } else if (specPart == 2 && part == 1){
                filename = "smallMaze.lay.txt";
                row = 10;
                col = 22;
            } else if (specPart == 3 && part == 1){
                filename = "mediumMaze.lay.txt";
                row = 18;
                col = 36;
            } else if (specPart == 4 && part == 1) {
                filename = "openMaze.lay.txt";
                row = 23;
                col = 37;
            } else if (specPart == 5 && part == 1){
                filename = "bigMaze.lay.txt";
                row = 37;
                col = 37;
            } else if (specPart == 1 && part == 2){
                filename = "smallSearch.lay.txt";
                row = 5;
                col = 20;
            } else if (specPart == 2 && part == 2){
                filename = "trickySearch.lay.txt";
                row = 7;
                col = 20;
            } else if (specPart == 3 && part == 2){
                filename = "mediumSearch.lay.txt";
                row = 8;
                col = 31;
            } else if (specPart == 4 && part == 2){
                filename = "bigSearch.lay.txt";
                row = 15;
                col = 31;
            } else {
                System.err.println("Invalid input.");
            }

            maze = new Node[row][col]; // This will instantiate the maze.
            br = new BufferedReader(new FileReader("Maze\\" + filename)); // This will read the file.
            int l = 0, w = 0; // The rows ("l") and the columns ("w") of the nodes (or cells) shall be identified and is initially set to zero.

            String sCurrLine;
            while ((sCurrLine = br.readLine()) != null) { // The result of (sCurrLine = br.readLine()) is null when it reaches the end of file (EOF).
                for (char c : sCurrLine.toCharArray()) { //The sCurrLine (a String type) has been extracted to produce a character array to easily get the characters one by one.
                    if (c == '%') { // "%" indicates a wall.
                        maze[l][w] = new Wall(c);
                    } else {
                        maze[l][w] = new Node(c, l, w);
                    }

                    if (c == 'P'){ // "P" indicates Pacman, the starting point, thus the current.
                        current = maze[l][w];
                    } else if (c == '.') { // "." indicates the goal/s.
                        goals.add(maze[l][w]);
                    }
                    w++;
                }
                w = 0;
                l++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Node goal = null;
        if (part == 1) { // Part 1 is defined as Basic Path Finding.
            goal = goals.remove(0); // There is only one goal in Part 1, thus only extracting the index 0 of goals.
            while (current != goal) {
                if (current.getContent() != 'P') {
                    current.setVisited(true);
                }
                current = adjacencySearch(current, goal, method);
            }

            /**
             * The following code lines will clean up the maze to only reflect the path instead of the closed list.
             */
            while (current != null && current.getContent() != 'P') { // This code will backtrack from the goal to the starting point using each node's parent as guide.
                current.setContent('.');
                current = current.getParent();
            }
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (maze[i][j].getContent() == '?') {
                        maze[i][j].setContent(' ');
                    }
                }
            }


        } else { // Part 2 is defined as Search with Multiple Goals.
            int goalNum = 0;
            while (!goals.isEmpty()) {
                if (goal == null) {
                    current.setContent('P');
                    // The code will find the nearest goal.
                    for (Node g : goals) {
                        if (method == 1) {
                            g.setH(Math.abs(current.getRow() - g.getRow()) + Math.abs(current.getCol() - g.getCol()));
                        } else {
                            g.setH(Math.max((Math.abs(current.getRow() - g.getRow())), Math.abs(current.getCol() - g.getCol())));
                        }
                    }
                    int nearestH = goals.get(0).getH();
                    Node nearest = goals.get(0);
                    for (Node g : goals) {
                        if (g.getH() < nearestH) {
                            nearestH = g.getH();
                            nearest = g;
                        }
                    }
                    goal = nearest;
                    goals.remove(goal);
                    openList = new ArrayList<>();
                    closedList = new ArrayList<>();
                    for (int i = 0; i < row; i++) {
                        for (int j = 0; j < col; j++) {
                            maze[i][j].setVisited(false);
                            maze[i][j].setParent(null);
                        }
                    }
                }

                while (current != goal) {
                    current = adjacencySearch(current, goal, method);
                    printMaze(true);
                    System.out.println("goal is [" + goal.getRow() + ", " + goal.getCol() + "].");
                }

                /**
                 * The following code lines will clean up the maze to only reflect the path instead of the closed list.
                 */
                while (current != null) { // This code will backtrack from the goal to the starting point using each node's parent as guide.
                    current.setContent('*');
                    if (current.getParent() != null) {
                        System.out.println(current.getParent().getRow() + " | " + current.getParent().getCol());
                    }
                    current = current.getParent();
                }
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        if (maze[i][j].getContent() == '?') {
                            maze[i][j].setContent(' ');
                        }
                    }
                }
                goal.setContent('.');
                printMaze(true);
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        if (maze[i][j].getContent() == 'P' || maze[i][j].getContent() == '*') {
                            maze[i][j].setContent(' ');
                        }
                    }
                }
                for (Node g : goals) {
                    g.setContent('.');
                }
                current = goal;
                goal = null;
            }
        }
        printMaze(false);
    }

    /**
     * The function adjacencySearch will not only find and include the "current" node's adjacent node to the open list
     * but also provides the next current node based on the least F in the open list and move it to the closed list.
     * @param current is the current node to be inspected.
     * @param goal is a pointer to the goal.
     * @param method is defined to either use the Manhattan Distance (1) or the Straight Line Distance (2) as to find H.
     * @return the next current Node.
     */
    static private Node adjacencySearch(Node current, Node goal, int method) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if ( (i == 0 && j != 0) || (i != 0 && j == 0)) {
                    Node inspectedNode = maze[current.getRow() + i][current.getCol() + j];
                    if (!inspectedNode.isVisited() && !(inspectedNode instanceof Wall) && !openList.contains(inspectedNode) && inspectedNode != goal) { // If the adjacent node ("inspectedNode") is not the goal...
                        inspectedNode.setG(current.getG() + 1); // G is defined as the distance or cost from the starting point ("P") to the node. For easier referencing, we use the current's G and add it to the unit cost (1).
                        if (method == 1) { // if Manhattan Distance should be used...
                            inspectedNode.setH(Math.abs(inspectedNode.getRow() - goal.getRow()) + Math.abs(inspectedNode.getCol() - goal.getCol()));
                        } else { // if Straight Line Distance should be used...
                            inspectedNode.setH(Math.max((Math.abs(inspectedNode.getRow() - goal.getRow())), Math.abs(inspectedNode.getCol() - goal.getCol())));
                        }
                        openList.add(inspectedNode);
                        if (current.getContent() != 'P') {
                            inspectedNode.setParent(current);
                        }
                    } else if (inspectedNode == goal) { // If the adjacent node ("inspectedNode") is the goal...
                        inspectedNode.setParent(current);
                        return goal;
                    }
                }
            }
        }

        /**
         * The following code finds the next "current".
         */
        Node candidateNode;
        int F = openList.get(0).getF(); // The next two lines make use of the first item in the list as reference to the least F (or candidate node).
        candidateNode = openList.get(0);
        for (Node n : openList) { // Whenever we find a smaller F, we change the candidate node accordingly.
            if (n.getF() < F && n.getContent() != '?') {
                candidateNode = n;
                F = n.getF();
            }
        }

        openList.remove(candidateNode);
        closedList.add(candidateNode);
        candidateNode.setVisited(true);

        return candidateNode;
    }

    /**
     * This function will print the maze.
     * @param wait to visualize the step-by-step methods.
     */
    private static void printMaze(boolean wait) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
        if (wait) {
            sc.nextLine();
        }
    }
}