/*
 * Copyright (c) 2008 Aduna and Deutsches Forschungszentrum fuer Kuenstliche Intelligenz DFKI GmbH.
 * All rights reserved.
 * 
 * Licensed under the Aperture BSD-style license.
 */
package org.semanticdesktop.aperture.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
 
/**
 * Hack to read windows registry values via command line reg query util.
 * At the moment it only supports reading the "my documents" folder,
 * and the other shell folders.
 * but looking at the code, you will be easily able to do more with it.
 * @author grimnes
 */
public class RegistryReader {
    /**
	 * String value
	 */
	private static final String REGSTR_TOKEN = "REG_SZ";
 
	/**
	 * DWord value
	 */
	private static final String REGDWORD_TOKEN = "REG_DWORD";
 
	private static final String REGQUERY_UTIL="Reg Query"+" ";

	private static final String REGKEY_SHELLFOLDERS = "\"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\"
        + "Explorer\\Shell Folders\"";
	
	/**
	 *    My Documents folder key
	 */
	private static final String PERSONAL_FOLDER_CMD = REGQUERY_UTIL + REGKEY_SHELLFOLDERS + " /v Personal";

    private static final String APP_DATA_CMD = REGQUERY_UTIL + REGKEY_SHELLFOLDERS + " /v AppData";

    private static final String LOCALSETTINGS_CMD = REGQUERY_UTIL + REGKEY_SHELLFOLDERS
            + " /v \"Local Settings\"";

    private static final String LOCALAPPDATA_CMD = REGQUERY_UTIL + REGKEY_SHELLFOLDERS + " /v \"Local AppData\"";
	 
	public static String getCurrentUserPersonalFolderPath() {
	    return runCommand(PERSONAL_FOLDER_CMD);
	}
	public static String getCurrentUserAppDataFolderPath() {
	    return runCommand(APP_DATA_CMD);
	}
	public static String getCurrentUserLocalSettingsFolderPath() {
	    return runCommand(LOCALSETTINGS_CMD);
	}
	public static String getCurrentUserLocalAppDataFolderPath() {
	    return runCommand(LOCALAPPDATA_CMD);
	}
	
	/**
	 * run the given command, and if on windows, return the return value
	 * @param command
	 * @return null or the result
	 */
	private static String runCommand(String command) {
	    if   (!System.getProperty("os.name").toLowerCase().contains("windows")) return null;
        
        try {
            Process process = Runtime.getRuntime().exec(command);
            StreamReader reader = new StreamReader(process.getInputStream());
 
            reader.start();
            process.waitFor();
            reader.join();
 
            String result = reader.getResult();
            int p = result.indexOf(REGSTR_TOKEN);
            if (p == -1)
                return null;
 
            return result.substring(p + REGSTR_TOKEN.length()).trim();
        } catch (Exception e) {
            return null;
        }
	}
 
	static class StreamReader extends Thread {
		private InputStream is;
 
		private StringWriter sw;
 
		StreamReader(InputStream is) {
			this.is = is;
			sw = new StringWriter();
		}
 
		public void run() {
			try {
				int c;
				while ((c = is.read()) != -1)
					sw.write(c);
			} catch (IOException e) {
			}
		}
 
		String getResult() {
			return sw.toString();
		}
	}
 
	public static void main(String[] args) {
		System.out.println("Your My Documents Folder is In: "+ getCurrentUserPersonalFolderPath());
		System.out.println("Your AppData Folder is In: "+ getCurrentUserAppDataFolderPath());
		System.out.println("Your Local Settings Folder is In: "+ getCurrentUserLocalSettingsFolderPath());
		System.out.println("Your Local App Data Folder is In: "+ getCurrentUserLocalAppDataFolderPath());
	}
}