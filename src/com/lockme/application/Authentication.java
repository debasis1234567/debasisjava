package com.lockme.application;

import com.lockme.model.UserCredentials;
import com.lockme.model.Users;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authentication {
	//input data
	private static Scanner keyInput;
	private static Scanner input;
	private static Scanner lockerInput;
	//output data 
	private static PrintWriter output;
	private static PrintWriter lockerOutput;
	//model to store data.
	private static Users users;
	private static UserCredentials userCredentials;
	
	private static String password_regex = "^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@#$%^&+=])"
            + "(?=\\S+$).{8,20}$";
	
	
	public static void main(String[] args) {
		intializeApplication();
		signInOptions();

	}
	public static void signInOptions() {
		int option;
		do {
			welcomeScreen();
			System.out.println("1 . Registration ");
			System.out.println("2 . Login ");
			System.out.println("3 . Exit ");
			option = keyInput.nextInt();
			switch (option) {
			case 1:
				registerUser();
				break;
			case 2:
				loginUser();
				break;
			default:
				System.out.println("Please select 1 to Register, 2 to Login and 3 to Exit");
				break;
			}
		} while (option != 3);
		keyInput.close();
		input.close();
	}
	
	public static void lockerOptions(String inpUsername) {
		System.out.println("1 . FETCH ALL STORED CREDENTIALS ");
		System.out.println("2 . STORE CREDENTIALS ");
		int option = keyInput.nextInt();
		switch(option) {
			case 1 : 
				fetchCredentials(inpUsername);
				break;
			case 2 :
				storeCredentials(inpUsername);
				break;
			default :
				System.out.println("Please select 1 Or 2");
				break;
		}
		lockerInput.close();
	}
	
	public static void registerUser() {
		System.out.println("==========================================");
		System.out.println("*					*");
		System.out.println("*   WELCOME TO REGISTRATION PAGE	*");
		System.out.println("*					*");
		System.out.println("==========================================");
		
		System.out.println("Enter Username :");
		String username = keyInput.next();
		users.setUsername(username);
		boolean found = false;
		while (input.hasNext() && !found) {
			if (input.next().equals(username)) {
				System.out.println("User Name already exists!");
				found = true;
				break;
			}
		}
        if(!found){
        	System.out.println("Enter Password :");
        	String password = keyInput.next();
        	Pattern p = Pattern.compile(password_regex);
        	Matcher m = p.matcher(password); 
			if(!m.matches()){
				System.out.println("Password should be at least 8 characters and must contain atleast one special character and number!");	
			} else {
				users.setPassword(password);
				output.println(users.getUsername());
				output.println(users.getPassword());
				System.out.println("User Registration Suscessful !");
				output.close();
			}
			
        }
		
	}
	public static void loginUser() {
		System.out.println("==========================================");
		System.out.println("*					*");
		System.out.println("*   WELCOME TO LOGIN PAGE	 *");
		System.out.println("*					*");
		System.out.println("==========================================");
		System.out.println("Enter Username :");
		String inpUsername = keyInput.next();
		boolean found = false;
		while(input.hasNext() && !found) {
			if(input.next().equals(inpUsername)) {
				System.out.println("Enter Password :");
				String inpPassword = keyInput.next();
				if(input.next().equals(inpPassword)) {
					System.out.println("Login Successful ! 200OK");
					found = true;
					lockerOptions(inpUsername);
					break;
				}
			}
		}
		if(!found) {
			System.out.println("User Not Found : Login Failure : 404");
		}
		
	}
	
	public static void welcomeScreen() {
		System.out.println("==========================================");
		System.out.println("*					*");
		System.out.println("*   Welcome To LockMe!		*");
		System.out.println("*   Your Personal Digital Locker	*");
		System.out.println("*					*");
		System.out.println("==========================================");
		
	}
	//store credentails
	public static void storeCredentials(String loggedInUser) {
		System.out.println("==========================================");
		System.out.println("*					*");
		System.out.println("*   WELCOME TO DIGITAL LOCKER STORE YOUR CREDS HERE	 *");
		System.out.println("*					*");
		System.out.println("==========================================");
		
		userCredentials.setLoggedInUser(loggedInUser);
		
		System.out.println("Enter Site Name :");
		String siteName = keyInput.next();
		userCredentials.setSiteName(siteName);
		
		System.out.println("Enter Username :");
		String username = keyInput.next();
		userCredentials.setUsername(username);
		
		System.out.println("Enter Password :");
		String password = keyInput.next();
		userCredentials.setPassword(password);
		
		lockerOutput.println(userCredentials.getLoggedInUser());
		lockerOutput.println(userCredentials.getSiteName());
		lockerOutput.println(userCredentials.getUsername());
		lockerOutput.println(userCredentials.getPassword());
		
		System.out.println("YOUR CREDS ARE STORED AND SECURED!");
		lockerOutput.close();		
	}
	
	//fetch credentials
	public static void fetchCredentials(String inpUsername) {
		System.out.println("==========================================");
		System.out.println("*					*");
		System.out.println("*   WELCOME TO DIGITAL LOCKER 	 *");
		System.out.println("*   YOUR CREDS ARE 	 *");
		System.out.println("*					*");
		System.out.println("==========================================");
		System.out.println(inpUsername);
		
		
		while(lockerInput.hasNext()) {
//			System.out.println(lockerInput.hasNext());
			if(lockerInput.next().equals(inpUsername)) {
				System.out.println("Site Name: "+lockerInput.next());
				System.out.println("User Name: "+lockerInput.next());
				System.out.println("User Password: "+lockerInput.next());
			}
		}
		
	}
	
	public static void intializeApplication() {

		File  dbFile = new File("credentialmanagerdatabase.txt");
		File  lockerFile = new File("locker-file.txt");
		
		try {
			//read data from db file
			input = new Scanner(dbFile);
			
			//red data from locker file
			lockerInput = new Scanner(lockerFile);
			
			//read data from keyboard
			keyInput = new Scanner(System.in);
			
			//out put 
			output = new PrintWriter( new FileWriter(dbFile,true));
			lockerOutput = new PrintWriter( new FileWriter(lockerFile,true));
			
			users = new Users();
			userCredentials  = new UserCredentials();
			
			
		} catch (IOException e) {
			System.out.println("404 : File Not Found ");
		}
		
	}

}
