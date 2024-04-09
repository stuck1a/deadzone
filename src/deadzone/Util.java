package deadzone;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Support class which provides some smart general utility functions
 */
public class Util {
  
  /** Stores the absolute path of the applications root folder */
  private static final String absRootDir;
  
  /** Stores the absolute path to the library folder */
  private static final String absLibrariesDir;
  
  /** Stores the absolute path to the ressources folder */
  private static final String absResourcesDir;
  
  /** Stores the absolute path to the library folder */
  private static final String absShadersDir;
  
  /** Stores the absolute path of the assets folder */
  private static final String absAssetsDir;
  
  
  static {
    // Initialize fixed paths
    String separator = System.getProperty("file.separator");
    absRootDir = System.getProperty("user.dir") + separator;
    absLibrariesDir = absRootDir + "libs" + separator;
    
    absResourcesDir = absRootDir + "res" + separator;
    absShadersDir = absResourcesDir + "shaders" + separator;
    
    absAssetsDir = absRootDir + "assets" + separator;
  }
  
  
  /**
   * @return Application root path
   */
  public static String getRootDir()  {
    return absRootDir;
  }
  
  /**
   * @return Libraries folder path
   */
  public static String getResourcesDir()  {
    return absResourcesDir;
  }
  
  /**
   * @return Resources folder path
   */
  public static String getShadersDir()  {
    return absShadersDir;
  }
  
  /**
   * @return Shaders folder path
   */
  public static String getLibrariesDir()  {
    return absLibrariesDir;
  }
  
  /**
   * @return Assets folder path
   */
  public static String getAssetsDir()  {
    return absAssetsDir;
  }
  
  
  public static String readFullFile(String path) {
    byte[] encoded;
    try {
      encoded = Files.readAllBytes(Paths.get(path));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new String(encoded, StandardCharsets.UTF_8);
  }
  
  
  /**
   * Converts a given width in pixel into a value between 0 and 1, where
   * 0 means 0% of the current window width and 1 means 100% of the current window width
   */
  public static float normalizePixelWidth(int pixelWidth) {
    int totalWidth = Deadzone.getApplication().getWindow().getPixelWidth();
    return  (float)(pixelWidth) / (float)(totalWidth);
  }
  
  
  /**
   * Converts a given height in pixel into a value between 0 and 1, where
   * 0 means 0% of the current window height and 1 means 100% of the current window height
   */
  public static float normalizePixelHeight(int pixelHeight) {
    int totalHeight = Deadzone.getApplication().getWindow().getPixelHeight();
    return (float)(pixelHeight) / (float)(totalHeight);
  }
  
  
  /**
   * Loads a given XML file and returns its data
   * Example usage to access data in the returned document object:
   * document.getElementsByTagName("user").item(0).getTextContent()
   */
  public static Document parseXML(String path) {
    File file = new File(path);
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder;
    Document document;
    try {
      documentBuilder = documentBuilderFactory.newDocumentBuilder();
      document = documentBuilder.parse(file);
    } catch (ParserConfigurationException e) {
      System.err.println("Failed to load XML file: \"" + path + "\"");
      return null;
    } catch (IOException | SAXException e) {
      System.err.println("Failed to parse XML file: \"" + path + "\"");
      return null;
    }
    return document;
  }
  
  
}
