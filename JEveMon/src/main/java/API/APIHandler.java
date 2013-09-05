/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package API;

import UI.Msg;
import data.account.Account;
import data.character.EVECharacter;
import data.type.Item;
import data.journal.JournalElement;
import data.skill.Skill;
import data.skill.SkillInTraining;
import data.location.Station;
import data.journal.TransactionElement;
import data.character.Attributes;
import data.type.Ship;
import db.DBHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

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
    
    private static boolean msgApiServerOffline = true;
    
    
    public static void createdirs(){
        File f = new File("cache/account");
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
                needed = true;
            }catch(ParserConfigurationException ex){
                System.out.println("PCE: "+ex.getMessage());
                needed = true;
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
                needed = true;
            }catch(ParseException ex){
                System.out.println("PEX: "+ex.getMessage());
                needed = true;
            }finally{
                try{
                    fis.close();
                }catch(Exception ex){
                    
                }
            }
        }else{
            needed = true;
        }
    return needed;
    }
    
    public static boolean cache(File cache, URL url){
        boolean success = false;
        if(isURLexists(url)){
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
                success = true;
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }finally{
                try{
                    br.close();
                    fos.close();
                }catch(Exception ex){
                }
            }
        }else{
            System.out.println("API server unavailable.");
            if( msgApiServerOffline ){
                Msg.errorMsg("<html>API server unreachable. Working with cached data.<br />This message won't appear again until you restart the application.</html>");
                msgApiServerOffline = false;
            }
        }
        return success;
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
    
    
    private static void initRefTypes(){
        JournalElement.refTypes = new HashMap();
        File cache = new File("cache/static/reftypes.xml");
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
        File cache = new File("cache/char/journal_"+c.getId()+".xml");
        if( isCacheNeeded(cache) ){
            try{
                URL url = new URL(c.getKey().getURL("char", "WalletJournal.xml.aspx")+"&characterID="+c.getId());
                cache(cache, url);
            }catch(MalformedURLException ex){
                System.out.println("MUE: "+ex.getMessage());
            }
        }
        File cache2 = new File("cache/char/transactions_"+c.getId()+".xml");
        if( isCacheNeeded(cache2) ){
            try{
                URL url = new URL(c.getKey().getURL("char", "WalletTransactions.xml.aspx")+"&characterID="+c.getId());
                cache(cache2, url);
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
                            long refID          = Long.parseLong(e.getAttribute("refID").toString());
                            double iskAmount      = Double.parseDouble(e.getAttribute("amount").toString());
                            long balance        = (long)Double.parseDouble(e.getAttribute("balance").toString());
                            int refTypeID       = Integer.parseInt(e.getAttribute("refTypeID").toString());
                            String date         = e.getAttribute("date").toString();
                            String owner1Name   = e.getAttribute("ownerName1").toString();
                            String owner2Name   = e.getAttribute("ownerName2").toString();
                            String reason       = e.getAttribute("reason").toString();
                            String refTypeName  = JournalElement.refTypes.get(new Integer(refTypeID));
                            JournalElement je = new JournalElement(date, refID, refTypeID, refTypeName, owner1Name, owner2Name, iskAmount, balance, reason);
                            if( refTypeID == 42 ){//market escrow
                                Element domElement = getTransactionDOMElement(c, refID);
                                double prize            = Double.parseDouble(domElement.getAttribute("price").toString());
                                int quantity            = Integer.parseInt(domElement.getAttribute("quantity").toString());
                                String typeName         = domElement.getAttribute("typeName").toString();
                                String clientName       = domElement.getAttribute("clientName").toString();
                                String stationName      = domElement.getAttribute("stationName").toString();
                                String transactionType  = domElement.getAttribute("transactionType").toString();
                                String transactionFor   = domElement.getAttribute("transactionFor").toString();
                                je.transaction = new TransactionElement(typeName, prize, clientName, stationName, transactionType, quantity, transactionFor);
                            }else if( refTypeID == 10 || refTypeID == 37 ){
                                je.transaction.clientName = je.owner2Name;
                                je.transaction.transactionType = "donation";
                            }
                            
                            c.getWalletJournal().add(je);
                        }catch(NumberFormatException ex){
                            System.out.println("NEX: "+ex.getMessage());
                        }
                    }
                }//for rows
            }catch(SAXException ex){
                System.out.println("SAXE: "+ex.getMessage());
            }catch(ParserConfigurationException ex){
                System.out.println("PCE: "+ex.getMessage());
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }
        }
    }
    
    private static boolean isURLexists(URL url){
        boolean result = false;
            try {
                InputStream input = url.openStream();
                result = true;
            } catch (Exception ex) {
                System.out.println("EX: "+ex.getMessage());
            }
        return result;
    }
    
    public static Element getTransactionDOMElement(EVECharacter c, long refID){
        Element el = null;
        File cache = new File("cache/char/transactions_"+c.getId()+".xml");
        if( cache.exists() && cache.isFile() ){
            try{
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(cache);
                NodeList rows = doc.getElementsByTagName("row");
                for(int i=0;i<rows.getLength() && el == null;i++){
                    if( rows.item(i).getNodeType() != Node.TEXT_NODE ){
                        Element e = (Element)rows.item(i);
                        try{
                            long id = Long.parseLong(e.getAttribute("journalTransactionID").toString());
                            if( id == refID ){
                                el = e;
                            }
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
        return el;
    }
    
    public static EVECharacter fillCharacterData(EVECharacter c){
        File cache = new File("cache/char/sheet_"+c.getId()+".xml");
        if( isCacheNeeded(cache) ){
            try{
                URL url = new URL(c.getKey().getURL("char", "CharacterSheet.xml.aspx")+"&characterID="+c.getId());
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
                    c.setBalance( Double.parseDouble(e.getTextContent()) );
                    c.setCorpID( Integer.parseInt( ((Element)(doc.getElementsByTagName("corporationID").item(0))).getTextContent()) );
                    c.getAttributes().setRaw( Attributes.Attribute.INTELLIGENCE , Integer.parseInt( ((Element)(doc.getElementsByTagName("intelligence").item(0))).getTextContent() ) );
                    c.getAttributes().setRaw( Attributes.Attribute.MEMORY , Integer.parseInt( ((Element)(doc.getElementsByTagName("memory").item(0))).getTextContent() ) );
                    c.getAttributes().setRaw( Attributes.Attribute.PERCEPTION, Integer.parseInt( ((Element)(doc.getElementsByTagName("perception").item(0))).getTextContent() ) );
                    c.getAttributes().setRaw( Attributes.Attribute.WILLPOWER, Integer.parseInt( ((Element)(doc.getElementsByTagName("willpower").item(0))).getTextContent() ) );
                    c.getAttributes().setRaw( Attributes.Attribute.CHARISMA , Integer.parseInt( ((Element)(doc.getElementsByTagName("charisma").item(0))).getTextContent() ) );
                    /** 
                     * TODO: Implants Import
                     */
                    c.setCloneSkillpoints( Integer.parseInt(((Element)(doc.getElementsByTagName("cloneSkillPoints").item(0))).getTextContent()) );
                }catch(NumberFormatException ex){
                    System.out.println("NFE: "+ex.getMessage());
                }
                c.setCloneName( ((Element)(doc.getElementsByTagName("cloneName").item(0))).getTextContent() );
                c.setDayOfBirth( ((Element)(doc.getElementsByTagName("DoB").item(0))).getTextContent() );
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
                    c.setSkillpoints(0);
                    for( int i=0;i<rows.getLength();i++ ){
                        if( rows.item(i).getNodeType() != Node.TEXT_NODE ){
                            Element e = (Element)rows.item(i);
                            try{
                                int typeID      = Integer.parseInt(e.getAttribute("typeID").toString());
                                int skillpoint  = Integer.parseInt(e.getAttribute("skillpoints").toString());
                                int level       = Integer.parseInt(e.getAttribute("level").toString());
                                c.getSkills().add(new Skill(db.getTypeByID(typeID), skillpoint, level));
                                c.setSkillpoints(c.getSkillpoints()+skillpoint);
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
        c.setIsTraining(isCharacterTraining(c));
    return c;
    }
    
    public static EVECharacter fillSkillQueue(EVECharacter c){
        File cache = new File("cache/char/queue_"+c.getId()+".xml");
        if( isCacheNeeded(cache) ){
            try{
                URL url = new URL(c.getKey().getURL("char", "SkillQueue.xml.aspx")+"&characterID="+c.getId());
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
                            c.getTrainingQueue().add(new SkillInTraining(db.getTypeByID(typeID), skillpoints, level, starttime, endtime, startsp, endsp));
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
    File cache = new File("cache/char/training_"+c.getId()+".xml");
        if( isCacheNeeded(cache) ){
            try{
                URL url = new URL(c.getKey().getURL("char", "SkillInTraining.xml.aspx")+"&characterID="+c.getId());
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
    
    public static Account getAccount(APIKey key){
    Account acc = null;
        File f = new File("cache/account/"+key.getName()+"_status.xml");
        if( isCacheNeeded(f) ){
            try{
                URL url = new URL(key.getURL("account", "AccountStatus.xml.aspx"));
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
                Node paidUntilNode = doc.getElementsByTagName("paidUntil").item(0);
                acc = new Account(paidUntilNode.getTextContent().toString());
                }catch(SAXException ex){
                System.out.println("SAXE: "+ex.getMessage());
            }catch(ParserConfigurationException ex){
                System.out.println("PCE: "+ex.getMessage());
            }catch(IOException ex){
                System.out.println("IOE: "+ex.getMessage());
            }finally{
            }
        }
        
    return acc;
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
                            chars[i].setCorpName(corpName);
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
        File cache = new File("cache/char/assets_"+c.getId()+".xml");
        if( isCacheNeeded(cache) ){
            try{
                URL url = new URL(c.getKey().getURL("char", "AssetList.xml.aspx")+"&characterID="+c.getId());
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
                    int flag = Integer.parseInt(locRows.get(i).getAttribute("flag"));
                    Item parent = new Item(db.getTypeByID(typeID), quantity, flag);//can be ship
                    Ship parentShip = new Ship(parent, quantity, flag);
                    boolean parentIsShip = db.isShip(typeID);
                    if( locRows.get(i).hasChildNodes() ){
                        NodeList childRows = locRows.get(i).getElementsByTagName("row");
                        for(int n=0;n<childRows.getLength();n++){
                            Element e = (Element)childRows.item(n);
                            int childTypeID = Integer.parseInt(e.getAttribute("typeID"));
                            int childQuantity = Integer.parseInt(e.getAttribute("quantity"));
                            int childFlag = Integer.parseInt(e.getAttribute("flag"));
                            Item child = new Item(db.getTypeByID(childTypeID),childQuantity,childFlag);
                            if( parentIsShip ){
                                if(child.isFitted()){
                                    parentShip.getFittedItems().add(child);
                                }else if(child.isInDroneBay()){
                                    parentShip.getDroneBay().add(child);
                                }else if(child.isInCargoHold()){
                                    parentShip.getCargoHold().add(child);
                                }else{
                                    parentShip.getContainedItems().add(child);
                                }
                            }else{
                                parent.getContainedItems().add(child);
                            }
                        }
                    }
                    if( !parentIsShip ){
                        c.addAsset(parent, locationID);
                    }else{
                        c.addAsset(parentShip, locationID);
                    }
                    }catch(NumberFormatException ex){
                        System.out.println("NFE: "+ex.getMessage());
                    }
                }
                Collections.sort(c.getAssets());
                for(int i=0;i<c.getAssets().size();i++){
                    Collections.sort(c.getAssets().get(i).getItems());
                }
//                System.out.println("asset size: "+c.getAssets().size());
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
