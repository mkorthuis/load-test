package com.candescent.selenium;

public class Arguments {

	public Arguments() { 
		
	}
	
	public enum Environment {
		PRODUCTION("prod", Constants.PROD_BASE_URL), 
		TEST("test", Constants.TEST_BASE_URL);
		
		private String name;
		private String url;
		
		Environment(String name, String url) {
			this.name = name;
			this.url = url;
		}
		
		public String getUrl() {
			return url;
		}
		
		public static Environment find(String name) {
			for (Environment env : Environment.values()) {
		      if (env.name.equalsIgnoreCase(name)) {
		        return env;
		      }
		    }
		    return null;
		}
	};
	
	private Integer users;
	private String username;
	private String password; 
	private Environment environment;
	private Boolean valid;
	private Integer lengthSeconds;

	public Integer getUsers() {
		return users;
	}

	public void setUsers(Integer users) {
		this.users = users;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Boolean isValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Integer getLengthSeconds() {
		return lengthSeconds;
	}

	public void setLengthSeconds(Integer lengthSeconds) {
		this.lengthSeconds = lengthSeconds;
	}

}
