package com.exchangerates.createdb;

import com.exchangerates.dto.ExchangeRate;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class XMLReader {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss Z", Locale.ROOT);

    public static List<ExchangeRate> getCurrencyData() {

        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try{
            String pre_apiURL = "https://www.bank.lv/vk/ecb_rss.xml";
            System.out.println("url "+ pre_apiURL);
            URL url = new URL(pre_apiURL);

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(url.openStream());

            NodeList items = doc.getElementsByTagName("item");

            for(int i = 0; i < items.getLength(); i++){
                Element item = (Element) items.item(i);

                Element description = (Element) item.getElementsByTagName("description").item(0);
                Element date = (Element) item.getElementsByTagName("pubDate").item(0);

                String[] descriptionArr = getCharacterDataFromElement(description).split(" ");


                String dateStr = getCharacterDataFromElement(date);
                LocalDateTime parsedDate = LocalDateTime.parse(dateStr, formatter);


                for(int j = 0; j < descriptionArr.length -1; j += 2){

                    exchangeRates.add(new ExchangeRate(
                                    descriptionArr[j],
                                    parsedDate,
                                    Float.parseFloat(descriptionArr[j+1])
                            )
                    );
                }
            }

        }catch(Exception e){
            System.out.println(e);
            System.out.println("ERROR");
        }

        return exchangeRates;
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }
}
