package MazeSearchHooray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Serato, Jay Vince on September 24, 2017.
 */
public class Main {
    private static Node[][] maze;
    private static ArrayList<Node> openList = new ArrayList<>();
    private static ArrayList<Node> closedList = new ArrayList<>();

    public static void main(String[] args) {
        Node current = null;
        Node goal = null;
        BufferedReader br;
        BufferedWriter bw;
        String sCurrLine;
        Scanner sc = new Scanner(System.in);
        int part, specPart, method, row = 0, col = 0;

        System.out.println("Which part would you want me to solve:" +
                "\n\t1. Part 1: Basic Pathfinding" +
                "\n\t2. Part 2: Search with Multiple Goals");
        part = sc.nextInt();
        switch (part) {
            case 1:
                System.out.println("Which of the following mazes would you want me to solve:" +
                    "\n\t1. Tiny Maze [7 x 7]" +
                    "\n\t2. Small Maze [10 x 22]" +
                    "\n\t3. Medium Maze [18 x 36]" +
                    "\n\t4. Big Maze [37 x 37]");
                    specPart = sc.nextInt();
                break;
            case 2:
                System.out.println("Which of the following searches would you want me to solve:" +
                        "\n\t1. Small Search [7 x 7]" +
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
                "\n\t1. Part 1: Manhattan" +
                "\n\t2. Part 2: Straighforward");
        method = sc.nextInt();

        String filename = "";
        try {
            if(specPart == 1 && part == 1) {
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
            } else if (specPart == 4 && part == 1){
                filename = "bigMaze.lay.txt";
                row = 37;
                col = 37;
            } else if (specPart == 1 && part == 2){
                filename = "smallSearch.lay.txt";
                row = 7;
                col = 7;
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
            }

            maze = new Node[row][col];
            br = new BufferedReader(new FileReader("Maze\\" + filename));
            int l = 0, w = 0;
            while ((sCurrLine = br.readLine()) != null) {
                for (char c : sCurrLine.toCharArray()) {
                    if (c == '%') {
                        maze[l][w] = new Wall(c);
                    } else {
                        maze[l][w] = new Node(c, l, w);
                    }

                    if (c == 'P'){
                        current = maze[l][w];
                    } else if (c == '.') {
                        goal = maze[l][w];
                    }
                    w++;
                }
                w = 0;
                l++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }

        while (current != goal) {
            if (!(current.isVisited()) && current.getContent() != 'P') {
                current.setVisited(true);
            }
            current = adjacencySearch(current, goal);
        }

        System.out.println(current + "is current");
        //TODO clean up mode
        while (current.toString().equals(".") || current.toString().equals("?")) {
            current.setContent('.');
            current = current.getParent();
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    System.out.print(maze[i][j]);
                }
                System.out.println();
            }
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (maze[i][j].getContent() == '?') {
                    maze[i][j].setContent(' ');
                }
            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
    }

    static private Node adjacencySearch(Node current, Node goal) {
        Node candidateNode;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                Node inspectedNode = maze[current.getRow() + i][current.getCol() + j];
                if (inspectedNode.getContent() == ' ' && (i == 0 || j == 0)) {
                    inspectedNode.setG(current.getG() + 10);
                    inspectedNode.setH((Math.abs(inspectedNode.getRow() - goal.getRow()) + Math.abs(inspectedNode.getCol() - goal.getCol())) * 10);
                    inspectedNode.setParent(current);
                    openList.add(inspectedNode);
                    System.out.println(inspectedNode.getParent());
                    System.out.println(" F = " + inspectedNode.getF());
                } else if (inspectedNode == goal) {
                    goal.setParent(current);
                    openList.add(goal);
                    return goal;
                }
            }
        }

        int F = openList.get(0).getF();
        candidateNode = openList.get(0);
        for (Node n : openList) {
            if (n.getF() < F) {
                candidateNode = n;
                F = n.getF();
            }
        }
        openList.remove(candidateNode);
        closedList.add(candidateNode);
        candidateNode.setVisited(true);
        System.out.println("candidate node's parent is " + candidateNode.getParent());

        return candidateNode;

    }
}
