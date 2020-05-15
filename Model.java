/**
 * The back end part of the Chat Noir game. Keeps track of positions, what is legal and what isn't,
 * and triggers game end.
 * @author Aidan St. George and Tyler Graffam
 * @version 1.0 - 5/11/2020
 */

package application;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Random;

public class Model {

	/** A helper object to handle observer pattern behavior */
	protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	//TODO javadoc
	private final int[] rowDiff = {-1, -1, 0, 0, 1, 1};
	private final int[] upperColDiff = {-1, 0, -1, 1, 0, 1};
	private final int[] middleColDiff = {-1, 0, -1, 1, 0, -1};
	private final int[] lowerColDiff = {1, 0, -1, 1, 0, -1};
	private int startingBlocked = 11;
	
	/** Boolean that stores who's turn it is */
	private boolean catsTurn;
	
	/** Boolean that triggers the end game */
	private boolean gameOver;
	
	/** Storage unit for the graph of vertices */
	private Vertex[][] vertices;
	
	/** Current position of cat */
	private Vertex catPosition;
	
	/**
	 * Initializes a new model. Saves view from having to create new Model each game.
	 * - Sets up the graph of vertices
	 * - generates and checks random blocked vertices
	 * - Initializes catPosition, catsTurn, and gameOver
	 */
	public void initialize() {
		catsTurn = false;
		gameOver = false;
		vertices = new Vertex[21][];
		
		// Set up ragged matrix of vertices
		for (int i = 0; i <= 10; i++) { // First half
			vertices[i] = new Vertex[i + 1];
			for (int j = 0; j <= i; j++) {
				vertices[i][j] = new Vertex(i, j);
			}
		}
		
		for (int i = 9; i >= 0; i--) { // Second half
			vertices[20 - i] = new Vertex[i + 1];
			for (int j = 0; j <= i; j++) {
				vertices[20 - i][j] = new Vertex(20 - i, j);
			}
		}
		
		// Initialize borderVertex and adjacentVertices for each vertex
		for (int i = 0; i <= 9; i++) { // Fist half
			for (int j = 0; j <= i; j++) {
				if (j == 0 || j == i)
					vertices[i][j].borderVertex = true;
				// Adjacent vertices
				for (int k = 0; k < 6; k++) {
					try {
						vertices[i][j].adjacentVertices.add(
								vertices[i + rowDiff[k]][j + upperColDiff[k]]);
					} catch (IndexOutOfBoundsException e) {
						// Don't add invalid vertices
					}
				}
			}
		}
		
		for (int j = 0; j <= 10; j++) { // Middle row
			if (j == 0 || j == 10)
				vertices[10][j].borderVertex = true;
			for (int k = 0; k < 6; k++) {
				try {
					vertices[10][j].adjacentVertices.add(
							vertices[10 + rowDiff[k]][j + middleColDiff[k]]);
				} catch (IndexOutOfBoundsException e) {
					
				}
			}
		}

		for (int i = 9; i >= 0; i--) { // Second half
			for (int j = 0; j <= i; j++) {
				if (j == 0 || j == i)
					vertices[20 - i][j].borderVertex = true;
				for (int k = 0; k < 6; k++) {
					try {
						vertices[20 - i][j].adjacentVertices.add(
								vertices[20 - i + rowDiff[k]][j + lowerColDiff[k]]);
					} catch (IndexOutOfBoundsException e) {
						
					}
				}
			}
		}
		
	
		// Generate random blocked vertices
		Random rand = new Random();
		do {
			for (int i = 0; i < startingBlocked; i++) {
				int randCol, randRow;
				do {
					randRow = rand.nextInt(21);
					randCol = rand.nextInt(11);
				} while (randCol >= vertices[randRow].length || vertices[randRow][randCol].blocked ||
						vertices[randRow][randCol] == catPosition); // check validity of vertex to block
				vertices[randRow][randCol].blocked = true;
				pcs.firePropertyChange("blocked", randRow, randCol - 1);
			}
		} while (!canCatEscape());
		
		catPosition = vertices[10][5];
		pcs.firePropertyChange("yes cat", 10, 5);
		
	}
	
	//TODO fix fire property changes
	/**
	 * Public method for View to call to update the model based on whose turn and validity of move.
	 * Also updates turn based on success of move
	 * @param i row of vertex to update
	 * @param j column of vertex to update
	 */
	public void updateBoard(int i, int j) {
		if (catsTurn) {
			pcs.firePropertyChange("no cat", catPosition.row, catPosition.col - 1);
			moveCat(vertices[i][j]);
			pcs.firePropertyChange("yes cat", i, j - 1);
		} else {
			setBlocked(vertices[i][j]);
			pcs.firePropertyChange("blocked", i, j - 1);
		}
		
		catsTurn = !catsTurn;
		pcs.firePropertyChange("feedback", null, null);
	}
	
	/**
	 * If v is a valid vertex that is not already blocked and doesn't contain the cat. Fires a 
	 * property change if successful. Calls can Escape to check if cat can still escape.
	 * @param v vertex to block
	 * @return success of change
	 */
	private boolean setBlocked(Vertex v) {
		v.blocked = true;
		return false;
	}
	
	/**
	 * If v is a valid vertex bordering the cat, model will update cat's position and fire a
	 * Property change.
	 * @param v vertex to block
	 * @return success of change
	 */
	private boolean moveCat(Vertex v) {
		catPosition = v;
		return false;
	}
	
	/**
	 * Method that uses Djikstra's algorithm to check if the cat has a route to an edge
	 * @return true if cat can escape, false otherwise.
	 */
	private boolean canCatEscape() {
		return true;
	}
	
	/**
	 * Getter for the state of the game. If the game is not over, method returns whose turn it is.
	 * If the game is over, method returns who won.
	 * @return feedback string to display
	 */
	public String getFeedback() {
		if (!gameOver) {
			if (catsTurn)
				return "It's the cat's turn to move!";
			else
				return "It's the blocker's turn to move!";
		} else {
			if (canCatEscape()) 
				return "Game over. The cat wins!";
			else
				return "Game over. The blocker wins!";
		}
	}
	
	/**
	 * Getter for the title of the game. To keep loose coupling
	 * @return the string "Chat Noir", the title of the game.
	 */
	public String getTitle() {
		return "Chat Noir";
	}
	
	//TODO Remove
	/**
	 * Method for testing purposes. Not used in view class.
	 * @return vertices
	 */
	public void printVertices() {
		
		for (int i = 0; i < vertices.length; i++) {
			for (int j = 0; j < vertices[i].length; j++) {
				String value = "0";
				if (vertices[i][j].blocked == true)
					value = "X";
				else if (vertices[i][j] == catPosition)
					value = "C";
				else if (vertices[i][j].borderVertex == true)
					value = "1";
				
				System.out.print(value + " ");
			}
			System.out.println();
		}
	}
	public void testAdjacencies(int i, int j) {
		Vertex v = vertices[i][j];
		for (int k = 0; k < v.adjacentVertices.size(); k++) {
			v.adjacentVertices.get(k).blocked = true;
			pcs.firePropertyChange("block", i, j);
		}
	}
	
	/**
	 * Private Vertex class. Stores data for a single vertex: whether its on the edge, which vertices are
	 * bordering it, and whether or not it has been blocked.
	 * @author Aidan St. George
	 * @version 1.0 - 5/11/2020
	 */
	private class Vertex {
		
		/** Int values that store the vertexes position in the storage matrix */
		int col, row;
		
		/** Boolean used in model's canEscape() algorithm to keep track of visited vertices */
		boolean visited;
		
		/** Boolean to keep track of which vertices have been blocked out */
		boolean blocked;
		
		/** Boolean to keep track of which vertices border the edge of the map */
		boolean borderVertex;
		
		/** List of all 2-6 vertices bordering this vertex */
		ArrayList<Vertex> adjacentVertices;
		
		public Vertex(int i, int j) {
			adjacentVertices = new ArrayList<Vertex>();
			row = i;
			col = j;
		}
		
	}
	
	/**
	 * Don't forget to create a way for Observers to subscribe to this
	 * @param listener
	 */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /**
     * And Observers probably want to be able to unsubscribe as well
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
	
}