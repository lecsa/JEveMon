/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import data.EVECharacter;
import data.Item;
import data.JournalElement;
import data.Skill;
import data.SkillInTraining;
import data.Station;
import db.DBHandler;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
    //  cache/accounts
    //  cache/char
/**
 *
 * @author lecsa
 */
public class APIHandler {
    
    
    public APIHandler(){
        
    }
    public static void createdirs(){
        File f = new File("cache/account");
        f.mkdirs();
        f = new File("cache/img");
        f.mkdirs();
        f = new File("cache/char");
        f.mkdirs();
        f = new File("cache/static");
        f.mkdirs();
    }
    
    public static boolean isCacheNeeded(File f){
    boolean needed = false;
        FileInputStream fis = null;
        
        if( f.exists() && f.isFile() ){
            try{
                fis = new FileInputStream(f);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(f);

                NodeList list = doc.getElementsByTagName("cachedUntil");
                Node n = list.item(0);
                Element e = (Element)n;
                String dateString = (String)e.getTextContent();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date cached =  df.parse(dateString);
                Date now = df.parse(df.format(new Date()));
                needed = now.after(cached);
                
            }catch(SAXException ex){
                System.out.println("SAXE: "+ex.getMessage());
            }catch(ParserConfigurationException ex){
                System.out.println("PCE: "+ex.getMessage());
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }catch(ParseException ex){
                System.out.println("PEX: "+ex.getMessage());
            }finally{
                try{
                    fis.close();
                }catch(IOException ex){
                    System.out.println("IOE: "+ex);
                }
            }
        }else{
            needed = true;
        }
    return needed;
    }
    
    public static void cache(File cache, URL url){
        BufferedReader br = null;
        FileOutputStream fos = null;
        String xml = "";
        try{

            URLConnection conn = url.openConnection();
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                xml += inputLine;
            }

            if( cache.exists() ){
                cache.delete();
            }

            cache.createNewFile();
            fos = new FileOutputStream(cache);
            fos.write(xml.getBytes());

        }catch(IOException ex){
            System.out.println("IOE: "+ex.getMessage());
        }finally{
            try{
                br.close();
                fos.close();
            }catch(Exception ex){
            }
        }
    }
    
    private static void cacheCharacterIMG(int characterID){
        File f = new File("cache/img/"+Integer.toString(characterID)+"_128.jpg");
        if( !f.exists() ){//cache
            FileOutputStream fos = null;
            try{
                URL website = new URL("http://image.eveonline.com/character/"+Integer.toString(characterID)+"_128.jpg");
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                fos = new FileOutputStream(f);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
                
            }finally{
                try{
                    fos.close();
                }catch(IOException ex){
                    System.out.println("IOE: "+ex.getMessage());
                }
            }
        }
    }
    
    public static Station getStationByID(long stationID){
    Station h=new Station(stationID,"Unknown station: "+stationID);
    File cache = new File("cache/static/outposts.xml");
        if( isCacheNeeded(cache) ){
            try{
                URL url = new URL("https://"+Defaults.SERVER+"/eve/ConquerableStationList.xml.aspx");
                cache(cache, url);
            }catch(MalformedURLException ex){
                System.out.println("MUE: "+ex.getMessage());
            }
        }
        if( cache.exists() && cache.isFile() ){
            try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(cache);
            NodeList rows = doc.getElementsByTagName("row");
            for(int i=0;i<rows.getLength();i++){
                Element row = (Element)rows.item(i);
                if(Long.parseLong(row.getAttribute("stationID"))==stationID){
                    h = new Station(stationID, row.getAttribute("stationName"));
                }
            }
            }catch(SAXException ex){
                System.out.println("SAXE: "+ex.getMessage());
            }catch(ParserConfigurationException ex){
                System.out.println("PCE: "+ex.getMessage());
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }    
        }
    return h;
    }
    
    public static JLabel getCharacterIMG(int characterID){
    File f = new File("cache/img/"+Integer.toString(characterID)+"_128.jpg");
    JLabel retval = new JLabel("IMG unavailable");
    if( f.exists() ){
        try{
            BufferedImage img = ImageIO.read(f);
            retval = new JLabel(new ImageIcon(img));

        }catch(IOException ex){
            System.out.println("IOE: "+ex.getMessage());
        }
    }
    return retval;
    }
    
    private static void initRefTypes(){
        File cache = new File("cache/eve/reftypes.xml");
        if( isCacheNeeded(cache) ){
            try{
                URL url = new URL("https://"+Defaults.SERVER+"/eve/RefTypes.xml.aspx");
                cache(cache, url);
            }catch(MalformedURLException ex){
                System.out.println("MUE: "+ex.getMessage());
            }
        }
        if( cache.exists() && cache.isFile() ){
            try{
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(cache);
                NodeList rows = doc.getElementsByTagName("row");
                for(int i=0;i<rows.getLength();i++){
                    if( rows.item(i).getNodeType() != Node.TEXT_NODE ){
                        Element e = (Element)rows.item(i);
                        try{
                            int id = Integer.parseInt(e.getAttribute("refTypeID").toString());
                            String name = e.getAttribute("refTypeName").toString();
                            JournalElement.refTypes.put(new Integer(id), name);
                        }catch(NumberFormatException ex){
                            System.out.println("NEX: "+ex.getMessage());
                        }
                    }
                }
            }catch(SAXException ex){
                System.out.println("SAXE: "+ex.getMessage());
            }catch(ParserConfigurationException ex){
                System.out.println("PCE: "+ex.getMessage());
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }
        }
    }
    
    public static void fillCharacterWalletJournal(EVECharacter c){
        if( JournalElement.refTypes == null ){
            initRefTypes();
        }
        File cache = new File("cache/char/journal_"+c.id+".xml");
        if( isCacheNeeded(cache) ){
            try{
                URL url = new URL(c.key.getURL("char", "WalletJournal.xml.aspx")+"&characterID="+c.id);
                cache(cache, url);
            }catch(MalformedURLException ex){
                System.out.println("MUE: "+ex.getMessage());
            }
        }
        if( cache.exists() && cache.isFile() ){
            try{
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(cache);
            }catch(SAXException ex){
                System.out.println("SAXE: "+ex.getMessage());
            }catch(ParserConfigurationException ex){
                System.out.println("PCE: "+ex.getMessage());
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }
        }
    }
    
    public static Element getTransactionElement(EVECharacter c, long refID){
    Element el = null;
        File cache = new File("cache/char/transactions_"+c.id+".xml");
        if( isCacheNeeded(cache) ){
            try{
                URL url = new URL(c.key.getURL("char", "WalletTransactions.xml.aspx")+"&characterID="+c.id);
                cache(cache, url);
            }catch(MalformedURLException ex){
                System.out.println("MUE: "+ex.getMessage());
            }
        }
        if( cache.exists() && cache.isFile() ){
            try{
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(cache);
            }catch(SAXException ex){
                System.out.println("SAXE: "+ex.getMessage());
            }catch(ParserConfigurationException ex){
                System.out.println("PCE: "+ex.getMessage());
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }
        }
    return el;
    }
    
    public static EVECharacter fillCharacterData(EVECharacter c){
        File cache = new File("cache/char/sheet_"+c.id+".xml");
        if( isCacheNeeded(cache) ){
            try{
                URL url = new URL(c.key.getURL("char", "CharacterSheet.xml.aspx")+"&characterID="+c.id);
                cache(cache, url);
            }catch(MalformedURLException ex){
                System.out.println("MUE: "+ex.getMessage());
            }
        }
        if( cache.exists() && cache.isFile() ){
            try{
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(cache);
                try{
                    Element e = (Element)(doc.getElementsByTagName("balance").item(0));
                    c.balance       = Double.parseDouble(e.getTextContent());
                    
                    c.corpID        = Integer.parseInt( ((Element)(doc.getElementsByTagName("corporationID").item(0))).getTextContent() );
                    c.intelligence  = Integer.parseInt( ((Element)(doc.getElementsByTagName("intelligence").item(0))).getTextContent() );
                    c.memory        = Integer.parseInt( ((Element)(doc.getElementsByTagName("memory").item(0))).getTextContent() );
                    c.charisma      = Integer.parseInt( ((Element)(doc.getElementsByTagName("charisma").item(0))).getTextContent() );
                    c.perception    = Integer.parseInt( ((Element)(doc.getElementsByTagName("perception").item(0))).getTextContent() );
                    c.willpower     = Integer.parseInt( ((Element)(doc.getElementsByTagName("willpower").item(0))).getTextContent() );
                    c.cloneSkillpoints = Integer.parseInt( ((Element)(doc.getElementsByTagName("cloneSkillPoints").item(0))).getTextContent() );
                }catch(NumberFormatException ex){
                    System.out.println("NFE: "+ex.getMessage());
                }
                c.cloneName     = ((Element)(doc.getElementsByTagName("cloneName").item(0))).getTextContent();
                c.dayOfBirth    = ((Element)(doc.getElementsByTagName("DoB").item(0))).getTextContent();
                //----- SKILLS ------
                //get all rowset nodes
                NodeList rowsets = doc.getElementsByTagName("rowset");
                boolean foundSkills = false;
                Node skillNode = null;
                for(int i=0;i<rowsets.getLength() && ! foundSkills;i++){
                    if( rowsets.item(i).hasAttributes() ){
                        Element currentRowset = (Element)rowsets.item(i);
                        if( currentRowset.getAttribute("name").toString().equals("skills") ){
                            skillNode = rowsets.item(i);
                            foundSkills = true;
                        }
                    }
                }
                if( foundSkills ){
                    NodeList rows = skillNode.getChildNodes();
                    DBHandler db = new DBHandler();
                    c.skillpoints = 0;
                    for( int i=0;i<rows.getLength();i++ ){
                        if( rows.item(i).getNodeType() != Node.TEXT_NODE ){
                            Element e = (Element)rows.item(i);
                            try{
                                int typeID      = Integer.parseInt(e.getAttribute("typeID").toString());
                                int skillpoint  = Integer.parseInt(e.getAttribute("skillpoints").toString());
                                int level       = Integer.parseInt(e.getAttribute("level").toString());
                                c.skills.add(new Skill(db.getTypeByID(typeID), skillpoint, level));
                                c.skillpoints+=skillpoint;
                            }catch(NumberFormatException ex){
                                System.out.println("NEX: "+ex.getMessage());
                            }
                        }
                    }
                }
            }catch(SAXException ex){
                System.out.println("SAXE: "+ex.getMessage());
            }catch(ParserConfigurationException ex){
                System.out.println("PCE: "+ex.getMessage());
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }
        
        }
        c = fillSkillQueue(c);
        c.isTraining = isCharacterTraining(c);
    return c;
    }
    
    public static EVECharacter fillSkillQueue(EVECharacter c){
        File cache = new File("cache/char/queue_"+c.id+".xml");
        if( isCacheNeeded(cache) ){
            try{
                URL url = new URL(c.key.getURL("char", "SkillQueue.xml.aspx")+"&characterID="+c.id);
                cache(cache, url);
            }catch(MalformedURLException ex){
                System.out.println("MUE: "+ex.getMessage());
            }
        }
        if( cache.exists() && cache.isFile() ){
            try{
                DBHandler db = new DBHandler();
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(cache);
                NodeList rows = doc.getElementsByTagName("row");
                for(int i=0;i<rows.getLength();i++){
                    if( rows.item(i).getNodeType() != Node.TEXT_NODE ){
                        try{
                            int typeID  = Integer.parseInt( ((Element)rows.item(i)).getAttribute("typeID").toString() );
                            int level   = Integer.parseInt( ((Element)rows.item(i)).getAttribute("level").toString() );
                            int startsp = Integer.parseInt( ((Element)rows.item(i)).getAttribute("startSP").toString() );
                            int endsp   = Integer.parseInt( ((Element)rows.item(i)).getAttribute("endSP").toString() );
                            int skillpoints = endsp-startsp;
                            String starttime = ((Element)rows.item(i)).getAttribute("startTime").toString();
                            String endtime = ((Element)rows.item(i)).getAttribute("endTime").toString();
                            c.trainingQueue.add(new SkillInTraining(db.getTypeByID(typeID), skillpoints, level, starttime, endtime, startsp, endsp));
                        }catch(NumberFormatException ex){
                            System.out.println("NFE: "+ex.getMessage());
                        }
                    }
                }
            }catch(SAXException ex){
                System.out.println("SAXE: "+ex.getMessage());
            }catch(ParserConfigurationException ex){
                System.out.println("PCE: "+ex.getMessage());
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }
        }
        return c;
    }
    
    public static boolean isCharacterTraining(EVECharacter c){
    boolean isTraining = false;
    File cache = new File("cache/char/training_"+c.id+".xml");
        if( isCacheNeeded(cache) ){
            try{
                URL url = new URL(c.key.getURL("char", "SkillInTraining.xml.aspx")+"&characterID="+c.id);
                cache(cache, url);
            }catch(MalformedURLException ex){
                System.out.println("MUE: "+ex.getMessage());
            }
        }
        if( cache.exists() && cache.isFile() ){
            try{
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(cache);
                Element skillInTraining = (Element)doc.getElementsByTagName("skillInTraining").item(0);
                isTraining = skillInTraining.getTextContent().toString().equals("1");
            }catch(SAXException ex){
                System.out.println("SAXE: "+ex.getMessage());
            }catch(ParserConfigurationException ex){
                System.out.println("PCE: "+ex.getMessage());
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }
        }
    return isTraining;
    }
    
    
    public static EVECharacter[] getCharacters(APIKey key){
        EVECharacter[] chars = new EVECharacter[3];
        // get URL content
        
        File f = new File("cache/account/"+key.getName()+".xml");
        if( isCacheNeeded(f) ){
            try{
                URL url = new URL(key.getURL("account", "Characters.xml.aspx"));
                cache(f, url);
                
            }catch(MalformedURLException ex){
                System.out.println("MUE: "+ex.getMessage());
            }
        }
        if( f.exists() && f.isFile() ){
            try{
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(f);

                NodeList list = doc.getElementsByTagName("row");
                    for(int i=0;i<list.getLength() && i< chars.length;i++){
                        Node n = list.item(i);
                        Element e = (Element)n;
                        String charName = (String)e.getAttribute("name");
                        String corpName = (String)e.getAttribute("corporationName");
                        String strID = (String)e.getAttribute("characterID");
                        int charID = -1;
                        try{
                            charID = Integer.parseInt(strID);
                        }catch(NumberFormatException ex){
                            System.out.println("NEX: "+ex.getMessage());
                        }
                        if( charID != -1 ){
                            chars[i] = new EVECharacter(charID, charName, key);
                            chars[i].corpName = corpName;
                            cacheCharacterIMG(charID);
                        }
                        
                    }
            }catch(SAXException ex){
                System.out.println("SAXE: "+ex.getMessage());
            }catch(ParserConfigurationException ex){
                System.out.println("PCE: "+ex.getMessage());
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }finally{
            }
        }
        
        return chars;
    }
    
    public static void fillCharacterAssets(EVECharacter c){
        File cache = new File("cache/char/assets_"+c.id+".xml");
        if( isCacheNeeded(cache) ){
            try{
                URL url = new URL(c.key.getURL("char", "AssetList.xml.aspx")+"&characterID="+c.id);
                cache(cache, url);
            }catch(MalformedURLException ex){
                System.out.println("MUE: "+ex.getMessage());
            }
        }
        if( cache.exists() && cache.isFile() ){
            try{
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(cache);
                NodeList allRows = doc.getElementsByTagName("row");
                ArrayList<Element> locRows = new ArrayList();
                
                for(int i=0;i<allRows.getLength();i++){
                    if( allRows.item(i).getNodeType() != Node.TEXT_NODE ){
                        if( allRows.item(i).hasAttributes() ){
                            Element e = (Element)allRows.item(i);
                            if( e.hasAttribute("locationID") ){
                                locRows.add(e);
                            }
                        }
                    }
                }
                DBHandler db = new DBHandler();
                for(int i=0;i<locRows.size();i++){
                    try{
                    long locationID = Long.parseLong(locRows.get(i).getAttribute("locationID"));
                    int typeID = Integer.parseInt(locRows.get(i).getAttribute("typeID"));
                    int quantity = Integer.parseInt(locRows.get(i).getAttribute("quantity"));
                    Item parent = new Item(db.getTypeByID(typeID), quantity);
                    
                    if( locRows.get(i).hasChildNodes() ){
                        NodeList childRows = locRows.get(i).getElementsByTagName("row");
                        for(int n=0;n<childRows.getLength();n++){
                            Element e = (Element)childRows.item(n);
                            int childTypeID = Integer.parseInt(e.getAttribute("typeID"));
                            int childQuantity = Integer.parseInt(e.getAttribute("quantity"));
                            Item child = new Item(db.getTypeByID(childTypeID),childQuantity);
                            parent.containedItems.add(child);
                        }
                    }
                    c.addAsset(parent, locationID);
                    }catch(NumberFormatException ex){
                        System.out.println("NFE: "+ex.getMessage());
                    }
                }
//                System.out.println("asset size: "+c.assets.size());
            }catch(SAXException ex){
                System.out.println("SAXE: "+ex.getMessage());
            }catch(ParserConfigurationException ex){
                System.out.println("PCE: "+ex.getMessage());
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }finally{
            }
        }
    }
}
