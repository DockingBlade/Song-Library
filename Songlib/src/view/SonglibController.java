package view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SonglibController {
	@FXML ListView<String> listView;
	@FXML Text displayName;
	@FXML Text displayArtist;
	@FXML Text displayYear;
	@FXML Text displayAlbum;
	
	@FXML Button add;
	@FXML TextField addName;
	@FXML TextField addArtist;
	@FXML TextField addYear;
	@FXML TextField addAlbum;
	
	@FXML Button edit;
	@FXML TextField editOGName;
	@FXML TextField editOGArtist;
	@FXML TextField editName;
	@FXML TextField editArtist;
	@FXML TextField editYear;
	@FXML TextField editAlbum;
	
	@FXML Button delete;
	private ObservableList<String> obsList;
	BufferedReader in;
	HashMap<String,String[]> map;
	Stage primaryStage;
	
	public void start(Stage mainStage) {
		primaryStage = mainStage; //sets up the Stage so Alerts can pop up correctly
		obsList = FXCollections.observableArrayList(); //sets up list for List view
		File file = new File("SongList.txt"); //This file object is used to check if the SongList.txt file exists and creates it if it does not
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not create file");
				return;
			}
		}
		
		try {
			in = new BufferedReader(new FileReader("SongList.txt")); //this try catch loop opens the SongList.txt file or returns if it cannot 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		map = new HashMap<>(); //Initializes the map, maps Strings in the observable list to an array of Strings that contains the Name, Artist, Year, and Album
		String line;
		try {
			while((line=in.readLine()) != null ) { //reads in each line of the Songlib.txt file
				if(line.equals("")) {
					break;
				}
				String [] list = line.split("\\|"); //creates a String array of name, artist, year, and album to map to 
				String key = list[0] + " by " +list[1]; //creates String of Name and Artist to Display on the GUI 
				map.put(key.toLowerCase(), list); //maps the displayed name to the array containing the rest of the information 
				obsList.add(key); //adds the key string to the display, we assume the file is already sorted 
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		
		 
		

		listView.setItems(obsList); 
		
		listView    //creates listener which monitors which item is selected
		.getSelectionModel()
		.selectedIndexProperty()
		.addListener(
				(obs, oldVal, newVal) -> 
				display());  //calls the display function everytime an item is selected 
		

		// select the first item
		listView.getSelectionModel().select(0);
		if(obsList.size() >=1) { //displays the first item's Name, Artist, Year (Optional). and Album (Optional)
			String[] setDisplay = map.get(obsList.get(0).toLowerCase());
			displayName.setText(setDisplay[0]);
			displayArtist.setText(setDisplay[1]);
			if(setDisplay.length >= 3 && !setDisplay[2].equals("null")) {
				displayYear.setText(setDisplay[2]);
			}
			if(setDisplay.length >= 4 && !setDisplay[3].equals("null")) {
				displayAlbum.setText(setDisplay[3]);
			}
			
		}
			
		
		
	}
	public void display () {
		int index = listView.getSelectionModel().getSelectedIndex(); //gets the index of the selected item 
		if(index < 0) {
			displayName.setText("");
			displayArtist.setText("");
			displayYear.setText("");
			displayAlbum.setText("");
			return;
		}
		String [] setDisplay = map.get(obsList.get(index).toLowerCase()); //Obtains the array of Strings corresponding to the selected item 
		displayName.setText(setDisplay[0]); //Displays Name
		displayArtist.setText(setDisplay[1]); //Displays Artist 
		if(setDisplay.length >= 3 && !setDisplay[2].equals("null")) { //If Year is not null displays Year
			displayYear.setText(setDisplay[2]);
		}
		else {
			displayYear.setText("");
		}
		if(setDisplay.length >= 4 && !setDisplay[3].equals("null")) { //If Album is not null displays Album
			displayAlbum.setText(setDisplay[3]);
		}
		else {
			displayAlbum.setText("");
		}
		
	
	}
	
	public void add() {
		String name = addName.getText(); //Initializes Strings to the fields corresponding to the Add button
		String artist =addArtist.getText();
		String year = addYear.getText();
		String album = addAlbum.getText();
		
		if(name == null || name.equals("") || artist == null || artist.equals("")) { //if the name or artist is empty creates an alert and then returns 
			Alert info = new Alert(AlertType.INFORMATION);
			info.initOwner(primaryStage);
			info.setHeaderText("Error Invalid Arguements");
			info.setContentText("The Name field or Artist field is blank");
			info.showAndWait();
			return;
					
		}
		
		
		
		if(year == null || year.equals("")) { //if the Year field is empty sets it to the "null" String
			year = new String ("null");
		}
		else {
			try {
				int yearInt = Integer.parseInt(year);
				if(yearInt < 0) {
					Alert info = new Alert(AlertType.INFORMATION);
					info.initOwner(primaryStage);
					info.setHeaderText("Error Invalid Arguements");
					info.setContentText("The year is less than 0, this is invalid");
					info.showAndWait();
					return;
				}
			}
			catch(Exception e) {
				Alert info = new Alert(AlertType.INFORMATION);
				info.initOwner(primaryStage);
				info.setHeaderText("Error Invalid Arguements");
				info.setContentText("The year is not in a numermic or Integer format this is invalid");
				info.showAndWait();
				return;
			}
		}
		if(album == null || album.equals("")) { // if the Year field is empty sets it to the "null" String
			album = new String ("null");
		}
		
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to add this song", ButtonType.YES, ButtonType.NO); //creates an Alert 
		alert.initOwner(primaryStage);
		Optional<ButtonType> result = alert.showAndWait();
		if(!result.isPresent() || alert.getResult() == ButtonType.NO) { //ends the process if the user press No or cancels out the window (X's out)
			return;
		}
		
		String listingName = name + " by " +artist; //creates a key to map to, and to display on the GUI
		String [] list = new String [4]; //creates a list containing all the information for a Name and Artist pair
		list[0] =name;
		list[1] = artist;
		list[2] = year;
		list[3] = album;
		
		if(obsList.size() == 0) { //if the list is empty, adds the item to the list and displays it 
			obsList.add(listingName);
			map.put(listingName.toLowerCase(), list);
			listView.getSelectionModel().select(0);
			display();
			return;
		}
		for(int i = 0; i < obsList.size(); i++) { //searches for the first string greater than the listingName in the list and inserts it at that index  
			if(obsList.get(i).toLowerCase().equals(listingName.toLowerCase())) { //if the name-artist pair exists in the table opens an alert and ends the program 
				Alert info = new Alert(AlertType.INFORMATION);
				info.initOwner(primaryStage);
				info.setHeaderText("Error Invalid Arguements");
				info.setContentText("This song has already been entered");
				info.showAndWait();
				return;
			}
			if(listingName.toLowerCase().compareTo(obsList.get(i).toLowerCase()) < 0) {
				obsList.add(i,listingName);
				map.put(listingName.toLowerCase(), list);
				listView.getSelectionModel().select(i);
				display();
				return;
			}
			
		}
		//if the lisitingName is greater lexicographically than all Strings in the list the listing is added at the end of the list. 
		obsList.add(listingName);
		map.put(listingName.toLowerCase(), list);
		listView.getSelectionModel().select(obsList.size()-1);
		display();
		return;
		
		
		
	}
	public void add(String name, String artist, String year, String album, String OGString) {
		
		if(name == null || name.equals("") || artist == null || artist.equals("")) { //if the name or artist is empty creates an alert and then returns 
			Alert info = new Alert(AlertType.INFORMATION);
			info.initOwner(primaryStage);
			info.setHeaderText("Error Invalid Arguements");
			info.setContentText("The Name field or Artist field is blank");
			info.showAndWait();
			return;
					
		}
		if(year == null || year.equals("")) { //if the Year field is empty sets it to the "null" String
			year = new String ("null");
		}
		
		else {
			try {
				int yearInt = Integer.parseInt(year);
				if(yearInt < 0) {
					Alert info = new Alert(AlertType.INFORMATION);
					info.initOwner(primaryStage);
					info.setHeaderText("Error Invalid Arguements");
					info.setContentText("The year is less than 0, this is invalid");
					info.showAndWait();
					return;
				}
			}
			catch(Exception e) {
				Alert info = new Alert(AlertType.INFORMATION);
				info.initOwner(primaryStage);
				info.setHeaderText("Error Invalid Arguements");
				info.setContentText("The year is not in a numermic or Integer format this is invalid");
				info.showAndWait();
				return;
			}
		}
		if(album == null || album.equals("")) { // if the Year field is empty sets it to the "null" String
			album = new String ("null");
		}
		
		map.remove(OGString.toLowerCase());
		for(int i= 0; i<obsList.size();i++) {
			if(obsList.get(i).toLowerCase().equals(OGString.toLowerCase())) {
				obsList.remove(i);
			}
		}
		
		//obsList.remove(OGString);
		
		String listingName = name + " by " +artist; //creates a key to map to, and to display on the GUI
		String [] list = new String [4]; //creates a list containing all the information for a Name and Artist pair
		list[0] =name;
		list[1] = artist;
		list[2] = year;
		list[3] = album;
		
		if(obsList.size() == 0) { //if the list is empty, adds the item to the list and displays it 
			obsList.add(listingName);
			map.put(listingName.toLowerCase(), list);
			listView.getSelectionModel().select(0);
			display();
			return;
		}
		for(int i = 0; i < obsList.size(); i++) { //searches for the first string greater than the listingName in the list and inserts it at that index  
			if(obsList.get(i).toLowerCase().equals(listingName.toLowerCase())) { //if the name-artist pair exists in the table opens an alert and ends the program 
				Alert info = new Alert(AlertType.INFORMATION);
				info.initOwner(primaryStage);
				info.setHeaderText("Error Invalid Arguements");
				info.setContentText("This song has already been entered");
				info.showAndWait();
				return;
			}
			if(listingName.toLowerCase().compareTo(obsList.get(i).toLowerCase()) < 0) {
				obsList.add(i,listingName);
				map.put(listingName.toLowerCase(), list);
				listView.getSelectionModel().select(i);
				display();
				return;
			}
			
		}
		//if the lisitingName is greater lexicographically than all Strings in the list the listing is added at the end of the list. 
		obsList.add(listingName);
		map.put(listingName.toLowerCase(), list);
		listView.getSelectionModel().select(obsList.size()-1);
		display();
		return;
		
		
		
	}
	
	
	
	public void edit() {
		
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to edit this song", ButtonType.YES, ButtonType.NO); //creates an Alert 
		alert.initOwner(primaryStage);
		Optional<ButtonType> result = alert.showAndWait();
		if(!result.isPresent() || alert.getResult() == ButtonType.NO) { //ends the process if the user press No or cancels out the window (X's out)
			return;
		}
		
		
		
		
		String OGname = editOGName.getText();
		String OGartist = editOGArtist.getText();
		String name = editName.getText(); //Initializes Strings to the fields corresponding to the Add button
		String artist =editArtist.getText();
		String year = editYear.getText();
		String album = editAlbum.getText();
		
		String OGString = OGname + " by " + OGartist;
		if(!map.containsKey(OGString.toLowerCase())) {
			Alert info = new Alert(AlertType.INFORMATION);
			info.initOwner(primaryStage);
			info.setHeaderText("Error Invalid Arguements");
			info.setContentText("There is no Song with the name and artist specified");
			info.showAndWait();
			return;
		}
		
		String newString = name + " by " +artist;
		if(map.containsKey(newString.toLowerCase())) {
			Alert info = new Alert(AlertType.INFORMATION);
			info.initOwner(primaryStage);
			info.setHeaderText("Error Invalid Arguements");
			info.setContentText("There is already a Song with the name and artist specified");
			info.showAndWait();
			return;
		}
		
		
		
		add(name,artist,year,album,OGString);
	}
	
	public void end() {
		File file = new File("SongList.txt"); //this file item is used to check if the file exists and creating it if it doesn't
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Could not create file");
				return;
			}
		}
		
		FileWriter fileToWrite;
		try {
			fileToWrite = new FileWriter("SongList.txt");
			BufferedWriter output = new BufferedWriter(fileToWrite); //opens the file for writing 
			//Set <String> keyes = map.keySet();
			//List<String> sortedKeyes = new ArrayList<String>(keyes); //takes all the keys in the map and sorts them in a ArrayList
			//Collections.sort(sortedKeyes);
			for(String key : obsList) { //loops through the arrayList and creates a String to write to the Songlib.txt
				String [] args = map.get(key.toLowerCase());
				String toWrite = null;
				if(args.length == 4) {
					toWrite = args[0] + "|" + args[1] + "|" +args[2] +"|" +args[3] + "\n";
				}
				else if(args.length == 3) {
					toWrite = args[0] + "|" + args[1] + "|" +args[2] + "\n";
				}
				else {
					toWrite = args[0] + "|" + args[1] + "\n";
				}
				if(toWrite != null) {
					output.write(toWrite);
				}
			}
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		
	}
	public void delete() {
		Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete the selected song", ButtonType.YES, ButtonType.NO); //creates an Alert 
		alert.initOwner(primaryStage);
		Optional<ButtonType> result = alert.showAndWait();
		if(!result.isPresent() || alert.getResult() == ButtonType.NO) { //ends the process if the user press No or cancels out the window (X's out)
			return;
		}
		
		int index = listView.getSelectionModel().getSelectedIndex(); //gets the index of the selected item 
		if(index < 0) {
			return;
		}
		if(obsList.size() == 1) {
			map.remove(obsList.get(0).toLowerCase());
			obsList.remove(0);
			displayName.setText("");
			displayArtist.setText("");
			displayYear.setText("");
			displayAlbum.setText("");
			return;
		}
		if(index == obsList.size()-1) {
			map.remove(obsList.get(index).toLowerCase());
			obsList.remove(index);
			listView.getSelectionModel().select(obsList.size()-1);
			display();
			return;
		}
		
		map.remove(obsList.get(index).toLowerCase());
		obsList.remove(index);
		listView.getSelectionModel().select(index);
		display();
		
		
	}
}
