package de.fau.osr.gui.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import de.fau.osr.util.AppProperties;

/**
 * A utility class to cater to file read and write for various needs
 * @author Gayathery
 *
 */
public class FileUtil {

    /**
     * @param configInfo
     * @return a bool to notify whether write was successful or not
     * This method writes the configuration information to config file
     */
    public Boolean writeConfigFile(List<String> configInfo){
        String lineSeparator = System.getProperty("line.separator");
        String userHomePath = System.getProperty("user.home");
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(userHomePath,AppProperties.GetValue("DefaultConfigFilePath"))), "utf-8"))) {
         writer.write(String.join(lineSeparator, configInfo));
         writer.close();
         return true;
      } catch (UnsupportedEncodingException e1) {
        e1.printStackTrace();
        return false;
        } catch (FileNotFoundException e1) {
        e1.printStackTrace();
        return false;
        } catch (IOException e1) {
        
        e1.printStackTrace();
        return false;
        }
    }
    
    /**
     * This method cleans the config file when the user rejects storing of data during login
     * @return a Boolean value "True" - Config file was cleaned properly. "False" Some problem occured while cleaning the file
     */
    public Boolean cleanConfigFile(){
        
        String userHomePath = System.getProperty("user.home");
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(new File(userHomePath,AppProperties.GetValue("DefaultConfigFilePath"))), "utf-8"))) {         
         writer.close();
         return true;
      } catch (UnsupportedEncodingException e1) {
        e1.printStackTrace();
        return false;
        } catch (FileNotFoundException e1) {
        e1.printStackTrace();
        return false;
        } catch (IOException e1) {        
        e1.printStackTrace();
        return false;
        }
    }
    
    /**
     * @return list of string which contains the configuration information
     * This method reads the configuration information from the  config file
     */
    public List<String> readConfigFile(){
        String userHomePath = System.getProperty("user.home");
 
       
       try {
           Path configFilePath = Paths.get(userHomePath, AppProperties.GetValue("DefaultConfigFilePath"));
           List<String> configList = Files.readAllLines(configFilePath);
           if(configList.isEmpty() || configList.size()  != Integer.valueOf(AppProperties.GetValue("ConfigCount"))){
               return new ArrayList<String>();
           }
           return configList;
    } catch (Exception e) {
        
        e.printStackTrace();
        return new ArrayList<String>();
    }
       

    }
}
