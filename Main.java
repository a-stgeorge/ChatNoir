/**
 * The front end part of the Chat Noir game. Runs the user interface, controls view and input.
 * @author Aidan St. George and Tyler Graffam
 * @version 1.1 - 5/12/2020
 */

package application;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;


public class Main extends Application implements EventHandler<ActionEvent>, PropertyChangeListener {
	private GridPane grid;
	private Model chatModel;
	private Button[][] arrays;
	private Label feedback;
	private Button Reset;

	@Override
	public void start(Stage primaryStage) {
		try {
			chatModel = new Model();
			chatModel.addPropertyChangeListener(this);
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 670,600);
			feedback = new Label();
			root.setTop(feedback);
			grid = new GridPane();
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle(chatModel.getTitle());
			root.setCenter(grid);
			Reset = new Button();
			Reset.setOnAction(this);
			//root.setBottom(Reset);
			Reset.setText("Reset");
			arrays = new Button[21][];
			grid.getColumnConstraints().add(new ColumnConstraints(40)); // Fix issue with first column having width 0
			grid.add(Reset,  20,  0, 2, 1);
			for (int i = 0; i <= 10; i++)
			{	
				arrays[i] = new Button[i + 1];
				for (int j = 0; j <= i; j++)
				{	
					arrays[i][j] = new Button();
					arrays[i][j].setPrefWidth(80);
					arrays[i][j].setOnAction(this);
					arrays[i][j].setStyle("-fx-border-color: #ff0000; -fx-background-color: #ffffff");
					grid.add(arrays[i][j], (10-i)+j*2, i, 2, 1);
				}
			}


			for (int i = 9; i >= 0; i--) 
			{ 
				arrays[20 - i] = new Button[i + 1];
				for (int j = 0; j <= i; j++) 
				{
					arrays[20 - i][j] = new Button();
					arrays[20 - i][j].setPrefWidth(80);
					arrays[20 - i][j].setOnAction(this);
					arrays[20 - i][j].setStyle("-fx-border-color: #ff0000; -fx-background-color: #ffffff");
					grid.add(arrays[20 - i][j], (10-i)+j*2, 21-i, 2, 1);
				}
			}

			chatModel.initialize(); // Set up graph and set border and blocked vertices
			feedback.setText(chatModel.getFeedback());; // Must display label after model is initialized


		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("blocked")) 
		{
			arrays[(int) ((Point)evt.getNewValue()).getX()][(int) ((Point)evt.getNewValue()).getY()]
					.setStyle("-fx-border-color: #ff0000; -fx-background-color: #ff0000");
		}
		if (evt.getPropertyName().contains("cat")) // Remove old cat (.contains() to catch both "cat left" and "cat right")
		{
			if (evt.getOldValue() != null) { // Erase old position
				arrays[(int) ((Point)evt.getOldValue()).getX()][(int) ((Point)evt.getOldValue()).getY()]
						.setStyle("-fx-border-color: #ff0000; -fx-background-color: #ffffff");
				arrays[(int) ((Point)evt.getOldValue()).getX()][(int) ((Point)evt.getOldValue()).getY()].setText("");
			}
			arrays[(int) ((Point)evt.getNewValue()).getX()][(int) ((Point)evt.getNewValue()).getY()]
					.setStyle("-fx-text-fill: #ffffff; -fx-border-color: #ff0000;"
							+ "-fx-background-color: #000000");

			if (evt.getPropertyName().equals("cat left")) // Change text direction
				arrays[(int) ((Point)evt.getNewValue()).getX()][(int) ((Point)evt.getNewValue()).getY()]
						.setText("(^••^)~");
			else
				arrays[(int) ((Point)evt.getNewValue()).getX()][(int) ((Point)evt.getNewValue()).getY()]
						.setText("~(^••^)");
		}
		if (evt.getPropertyName().equals("feedback")) // Add new cat
		{
			feedback.setText(chatModel.getFeedback());
		}
		if (evt.getPropertyName().equals("reset")) {
			for (int i = 0; i < arrays.length; i++) {
				for (int j = 0; j < arrays[i].length; j++) {
					arrays[i][j].setStyle("-fx-border-color: #ff0000; -fx-background-color: #ffffff");
					arrays[i][j].setText("");
				}
			}
		}
	}

	@Override
	public void handle(ActionEvent event) {

		if (event.getSource() != Reset)
		{
			for (int i = 0; i < arrays.length; i++) {
				for (int j = 0; j < arrays[i].length; j++) {
					if (event.getSource() == arrays[i][j]) {
						chatModel.updateBoard(i, j);	
					}
				}
			}
		}

		else
		{
			chatModel.reset();
		}
	}
}