package application;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.Array;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class Main extends Application implements EventHandler<ActionEvent> {
	private GridPane grid;
	private Model chatModel;
	private Button[][] arrays;
	private Label feedback;
	@Override
	public void start(Stage primaryStage) {
		try {
			chatModel = new Model();
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,1000,800);
			feedback = new Label(chatModel.getFeedback());
			root.setTop(feedback);
			grid = new GridPane();
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle(chatModel.getTitle());
			root.setCenter(grid);
			
			arrays = new Button[23][];


			for (int i = 0; i <= 10; i++)
			{	
				arrays[i] = new Button[i+1];
				for (int j = 0; j <= i; j++)
				{	
					arrays[i][j] = new Button();
					arrays[i][j].setPrefWidth(80);
					arrays[i][j].setOnAction(this); //e -> {chatModel.updateBoard(i,j);} );
					//if (i%2==0)
						grid.add(arrays[i][j], (11-i)+j*2, i, 2, 1);
					//else
						//grid.add(arrays[i][j], (11-i*2)+j+1, i, 2, 1);
				}
			}


			for (int i = 9; i >= 0; i--) 
			{ 
				arrays[20 - i] = new Button[i + 1];
				for (int j = 0; j <= i; j++) 
				{
					arrays[i][j] = new Button();
					arrays[i][j].setPrefWidth(80);
					arrays[i][j].setOnAction(this);
					//if (i%2==0)
						grid.add(arrays[i][j], (11-i)+j*2, 21-i, 20, 1);
					//else
						//grid.add(arrays[i][j], j*2+1, 21-i, 2, 1);
				}
			}


		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}


	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("")) 
		{
		}
		if (evt.getPropertyName().equals("")) 
		{
		}
		if (evt.getPropertyName().equals("")) 
		{
		}

	}

		@Override
		public void handle(ActionEvent event) {
			System.out.println(event.getSource());

//			for (int i = 0; i <= 10; i++)
//			{	
//				for (int j = 0; j <= i; j++)
//				{	
//					if (event.getSource() == arrays[i][j])
//					{
//						System.out.println("Button" + i + j);
//						//chatModel.updateBoard(i, j);
//					}
//				}
//			}
			
			for (int i = 0; i <= 10; i++)
			{	
				
				for (int j = 0; j <= i; j++)
				{	
					if (event.getSource() == arrays[i][j])
					{
						System.out.println("Button" + i + j);
					}
				}
			}
			
			
			int column = 0;
			for (int i = 9; i >= 0; i--) 
			{ 
				
				for (int j = 0; j <= i; j++) 
				{
					if (event.getSource() == arrays[20-i][j])
					{	column = 20-i;
						System.out.println("Button" + column + j);
					}	//chatModel.updateBoard(20-1, j);
				}
			}
		}}




