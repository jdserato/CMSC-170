package src.Maze;

/**
 * Created by Serato, Jay Vince on September 24, 2017.
 */

public class Node {
    private boolean visited = false;
    private char content;
    private int row, col;
    private int H, G;
    private Node parent;

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public char getContent() {
        return content;
    }

    public void setContent(char content) {
        this.content = content;
    }


    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        if (this.visited && !visited) {
            content = ' ';
        }
        this.visited = visited;
        if (visited) {
            if (content != 'P') {
                content = '?';
            }
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }


    public int getH() {
        return H;
    }

    public void setH(int h) {
        H = h;
    }

    public int getG() {
        return G;
    }

    public void setG(int g) {
        G = g;
    }

    public int getF() {
        return G + H;
    }


    public Node(char c, int row, int col) {
        content = c;
        this.row = row;
        this.col = col;
    }



    @Override
    public String toString() {
        return content + "";
    }
}
