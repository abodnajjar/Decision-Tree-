package application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.*;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
public class Main extends Application {
	//this method to convert root node to tree 
	private TreeItem<String> convertToTreeItem(Node node) {
		if (node == null) {
			return null;
		}
		TreeItem<String> treeItem = new TreeItem<>(String.valueOf(node.attribute));
		if (node.isLeaf) {
			// If it's a leaf, add the result as a child
			boolean result = node.classification; // Assuming getResult() gives "Yes" or "No"
			treeItem.getChildren().add(new TreeItem<>("Result: " + result));
		} else {
			// Recursively add children if not a leaf
			for (Node child : node.children) {
				treeItem.getChildren().add(convertToTreeItem(child));
			}
		}
		return treeItem;
	}
	// the main Stage (select the train data split)
	@Override
	public void start(Stage primaryStage) {
		 Label label = new Label("Select the training data size :  ");
		 label.setFont(Font.font(20));
		  StackPane stackPane1 = new StackPane();
	        stackPane1.setStyle("-fx-background-color: #40E0D0;");; // Blue background
	        stackPane1.getChildren().add(label);
	        // Create ComboBox
	        ComboBox<String> comboBox = new ComboBox<>();
	        comboBox.setStyle("-fx-alignment: CENTER;");
	        comboBox.getItems().addAll("20%", "40%", "60%","70%","80%","90%");
	        comboBox.setValue("70%");
	       // comboBox.setPrefWidth(100); // Optional: Set a preferred width for better UI
	        comboBox.setPrefWidth(200);
	        comboBox.setPrefHeight(40);

	        // Place Label and ComboBox in an HBox
	        HBox hBox = new HBox(20); // Add spacing between elements
	        hBox.getChildren().addAll(stackPane1, comboBox);

	        // Create Button
	        Button showTreeButton = new Button("Show Tree");
	        showTreeButton.setPrefSize(200, 30);
	        showTreeButton.setOnAction(event -> {
	            String selectedValue = comboBox.getValue(); // Get selected ComboBox value
	            double x = 0.0;
	            if (selectedValue != null) {
	                switch (selectedValue) {
	                    case "20%":
	                        x = 0.2;
	                        Stage d=D(x,primaryStage);
	                        primaryStage.close();
	                        d.show();
	                        break;
	                    case "40%":
	                        x = 0.4;
	                        Stage g=D(x,primaryStage);
	                        primaryStage.close();
	                        g.show();
	                        break;
	                    case "60%":
	                        x = 0.6;
	                        Stage t=D(x,primaryStage);
	                        primaryStage.close();
	                        t.show();
	                        break;
	                    case "70%":
	                        x = 0.7;
	                        Stage a=D(x,primaryStage);
	                        primaryStage.close();
	                        a.show();
	                        break;
	                    case "80%":
	                        x = 0.8;
	                        Stage b=D(x,primaryStage);
	                        primaryStage.close();
	                        b.show();
	                        break;
	                    case "90%":
	                        x = 0.9;
	                        Stage n=D(x,primaryStage);
	                        primaryStage.close();
	                        n.show();
	                        break;
	                    default:
	                        System.out.println("Invalid selection");
	                        return;
	                }
	            } 
	        });
	        // Place HBox and Button in a VBox
	        VBox vBox = new VBox(70); // Add spacing between elements
	        vBox.getChildren().addAll(hBox, showTreeButton);
	        vBox.setStyle("-fx-background-color: #D3D3D3;-fx-padding: 20; -fx-alignment: center;");
	        // Set VBox alignment
	       // vBox.setStyle("");

	        // Add VBox to the Scene and Stage
	        Scene scene = new Scene(vBox, 600, 400);
	        primaryStage.setTitle("Training Data Selector");
	        primaryStage.setScene(scene);
	        primaryStage.show();
	}
	
	public Stage D(double x,Stage z) {
		Stage primaryStage=new Stage();
		try {
			DecisionTree tree = new DecisionTree();
			List<String[]> data = tree.load_Data();
			tree.load_Train_Data(data,x);
			List<String[]> trani=tree.trainData;
			List<String[]> test=tree.testDat;
			String[] st=tree.load_attributes();
			Node c=tree.buildTree(trani,st);
			tree.predict(c, test);
			Label l1=new Label("The Decision Tree :-");
			l1.setFont(Font.font(25));
			  StackPane stackPane1 = new StackPane();
		        stackPane1.setStyle("-fx-background-color: #40E0D0;");; // Blue background
		        stackPane1.getChildren().add(l1);
			TreeItem<String> rootItem = convertToTreeItem(c);
			rootItem.setExpanded(true); // Expand the root node
			TreeView<String> treeView = new TreeView<>(rootItem);
			treeView.setPrefWidth(450);
			treeView.setPrefHeight(350);
			//StackPane rootPane = new StackPane(treeView);
			int true_positive = tree.true_positive;
			int  true_neagtive = tree.true_neagtive;
			int fasle_positive= tree.fasle_positive;
			int false_neagtive = tree.false_neagtive;

			int  total = true_positive + true_neagtive + fasle_positive + false_neagtive;
			double accuracy = ((double) (true_positive + true_neagtive) / total*100);
			double precision = (double) true_positive / (true_positive + fasle_positive);
			double recall = (double) true_positive / (true_positive + false_neagtive);
			double fScore = 2 * (precision * recall) / (precision + recall);
			String s=String.format("%.4f",accuracy);
			
			//String total_string = String.valueOf(total);
			 String accuracy_string = s+"%";
			 String precision_string = String.format("%.4f",precision);
			 String recall_string = String.format("%.4f",recall);
			 String fScore_string = String.format("%.4f",fScore );

			String true_positive_string = String.valueOf(tree.true_positive);
			String  true_neagtive_string = String.valueOf(tree.true_neagtive);
			String false_positive_string = String.valueOf(tree.fasle_positive);
			String false_negative_string = String.valueOf(tree.false_neagtive);
			Label l2=new Label("The classification Details :-");
			l2.setFont(Font.font(25));
			 StackPane stackPane2 = new StackPane();
		        stackPane2.setStyle("-fx-background-color: #40E0D0;"); // Blue background
		        stackPane2.getChildren().add(l2);
			GridPane gridPane=new GridPane();
			Label label11=new Label("True Positive =");
			label11.setFont(Font.font(20));
			Label label12=new Label(true_positive_string);
			label12.setFont(Font.font(20));
			gridPane.add(label11, 1, 0);
			gridPane.add(label12,2,0);
			Label label21=new Label("True Neagtive =");
			label21.setFont(Font.font(20));
			Label label22=new Label(true_neagtive_string);
			label22.setFont(Font.font(20));
			gridPane.add(label21, 1, 1);
			gridPane.add(label22,2, 1);
			Label label31=new Label("False Positive =");
			label31.setFont(Font.font(20));
			Label label32=new Label(false_positive_string);
			label32.setFont(Font.font(20));
			gridPane.add( label31, 1, 2);
			gridPane.add( label32,2, 2);
			Label label41=new Label("False Neagtive =");
			label41.setFont(Font.font(20));
			Label label42=new Label(false_negative_string);
			label42.setFont(Font.font(20));
			gridPane.add(label41, 1, 3);
			gridPane.add(label42,2, 3);
			Label label51=new Label("Accuracy  =");
			label51.setFont(Font.font(20));
			Label label52=new Label(accuracy_string);
			label52.setFont(Font.font(20));
			gridPane.add(label51, 1, 4);
			gridPane.add(label52,2, 4);
			Label label61=new Label("Precision =");
			label61.setFont(Font.font(20));
			Label label62=new Label(precision_string);
			label62.setFont(Font.font(20));
			gridPane.add(label61, 1, 5);
			gridPane.add(label62,2,5);
			Label label71=new Label("Recall =");
			label71.setFont(Font.font(20));
			Label label72=new Label(recall_string);
			label72.setFont(Font.font(20));
			gridPane.add(label71,1,6);
			gridPane.add(label72,2,6);
			Label label81=new Label("FScore =");
			label81.setFont(Font.font(20));
			Label label82=new Label(fScore_string);
			label82.setFont(Font.font(20));
			gridPane.add(label81,1,7);
			gridPane.add(label82,2,7);
			Button Back=new Button("Go Back");
			Back.setPrefWidth(200);
			Back.setPrefHeight(40);
			Back.setOnAction(e->{
				primaryStage.close();
				z.show();
			});
			Label la1=new Label("");
			Label la2=new Label("");
			gridPane.add(la1,1,8);
			gridPane.add(la2,2,8);
			Label la3=new Label("");
			Label la4=new Label("");
			gridPane.add(la3,1,9);
			gridPane.add(la4,2,9);
			gridPane.add(Back,1,10);
			
			HBox hb1=new HBox(250,stackPane1,stackPane2);
			hb1.setAlignment(Pos.CENTER);
			HBox hb2=new HBox(100,treeView,gridPane);
			hb2.setAlignment(Pos.CENTER);
			VBox vb=new VBox(20,hb1,hb2);
			vb.setAlignment(Pos.CENTER);
			vb.setStyle("-fx-background-color: #D3D3D3;");
			Scene scene = new Scene(vb,1000,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Decision Tree Project :-");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
