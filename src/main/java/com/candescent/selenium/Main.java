package com.candescent.selenium;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.candescent.selenium.Arguments.Environment;
import com.candescent.selenium.util.Output;

public class Main 
{
	

	
	public static void main( String[] args ) throws InterruptedException
    {
		
		Output.getInstance().writeLine("Starting Application");
		
		//Process command line arguments
		Arguments arguments = handleArguments(args);
		if(!arguments.isValid()) {
			return;
		}
		
		//Spin up all of the "users"
		ExecutorService executorService = Executors.newCachedThreadPool();
		for(int i=0;i<arguments.getUsers();i++) {
			final int index = i;
			Output.getInstance().writeLine("Starting Thread: " + index);
			executorService.execute(new Runnable() {
				@Override 
		    	public void run() {
					try {
						new SingleRadEmulator(arguments, index).run();
					} catch (InterruptedException e) {
						Output.getInstance().writeLine("Error! Thread: " + index + " : " + e.getMessage());
					}	
	        	}
			});	
		}
		
		//Sleep while the "users" run.
		Thread.sleep(arguments.getLengthSeconds()*1000);
		
		//Shutdown all users once the test is complete.
		executorService.shutdownNow();
		Output.getInstance().close();
		
    }
	
	private static Arguments handleArguments(String[] args) {
		Arguments arguments = new Arguments();
		
		if(args.length != 5) {
			System.out.println("Invalid Arguments.  Need load size, username, password, environment(prod|test) and test length");
    		arguments.setValid(Boolean.FALSE);
			return arguments;
		}
		
		arguments.setUsers(Integer.parseInt(args[0]));
		arguments.setUsername(args[1]);
		arguments.setPassword(args[2]);
		
		Environment env = Arguments.Environment.find(args[3]);
		if(env == null) {
			System.out.println("Invalid Arguments.  Environment must be either prod or test");
			arguments.setValid(Boolean.FALSE);
			return arguments;
		}
		arguments.setEnvironment(env);
		arguments.setLengthSeconds(Integer.parseInt(args[4]));
		
		arguments.setValid(Boolean.TRUE);
		return arguments;
	}
	
	
}
