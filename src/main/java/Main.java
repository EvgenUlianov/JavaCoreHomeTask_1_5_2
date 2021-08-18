import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {

    private static final String FILE_XML = "data.xml";
    private static final String FILE_JSON = "data.gson";

    public static void main(String[] args) {
        System.out.println("Задача 2: XML - JSON парсер");
        makeDataXML();
        List<Employee> list;
        try {
            list = parseXML();
        }catch (Exception e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }
        // check the process
        list.forEach(System.out::println);

        String json = listToJson(list);
        System.out.println(json);

        saveTextFile(json);


    }

    private static String listToJson(List<Employee> list) {

        // function 'setPrettyPrinting' makes returned String pretty)))
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        return gson.toJson(list);
    }

    private static List<Employee> parseXML() throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(FILE_XML));

        // hard code)))
        Node root = doc.getDocumentElement();
        NodeList levelOneNodes = root.getChildNodes();
        for (int i = 0; i < levelOneNodes.getLength(); i++) {
            Node levelOneNode = levelOneNodes.item(i);
            if (Node.ELEMENT_NODE == levelOneNode.getNodeType()) {
                Employee oneEmployee = new Employee();
                NodeList levelTwoNodes = levelOneNode.getChildNodes();
                for (int j = 0; j < levelTwoNodes.getLength(); j++) {
                    Node levelThreeNode = levelTwoNodes.item(j);
                    if (Node.ELEMENT_NODE == levelThreeNode.getNodeType()) {
                        String fieldName = levelThreeNode.getNodeName();
                        String currentValue = levelThreeNode.getTextContent();

                        //it is new syntaxis for me...
                        switch (fieldName) {
                            case "id" -> oneEmployee.setId(Long.parseLong(currentValue));
                            case "age" -> oneEmployee.setAge(Integer.parseInt(currentValue));
                            case "firstName" -> oneEmployee.setFirstName(currentValue);
                            case "country" -> oneEmployee.setCountry(currentValue);
                            case "lastName" -> oneEmployee.setLastName(currentValue);
                        }
                    }
                }
                list.add(oneEmployee);
            }
        }
        return list;
    }

    private static void makeDataXML() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("staff");
            document.appendChild(root);

            List<Employee> list = new ArrayList<>();
            list.add(new Employee(1,"John", "Smith", "USA", 25));
            list.add(new Employee(2,"Inav", "Petrov", "RU", 23));
            list.add(new Employee(3,"Irina", "Smelova", "UA", 21));

            for (Employee emploee: list) {
                Element employeeElement = document.createElement("employee");
                root.appendChild(employeeElement);

                Element id = document.createElement("id");
                employeeElement.appendChild(id);
                Text idText = document.createTextNode(Long.toString(emploee.getId()));
                id.appendChild(idText);

                Element firstName = document.createElement("firstName");
                employeeElement.appendChild(firstName);
                Text firstNameText = document.createTextNode(emploee.getFirstName());
                firstName.appendChild(firstNameText);

                Element lastName = document.createElement("lastName");
                employeeElement.appendChild(lastName);
                Text lastNameText = document.createTextNode(emploee.getLastName());
                lastName.appendChild(lastNameText);

                Element country = document.createElement("country");
                employeeElement.appendChild(country);
                Text countryText = document.createTextNode(emploee.getCountry());
                country.appendChild(countryText);

                Element age = document.createElement("age");
                employeeElement.appendChild(age);
                Text ageText = document.createTextNode(Integer.toString(emploee.getAge()));
                age.appendChild(ageText);

            }

            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(FILE_XML));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, streamResult);

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static void saveTextFile(String extForSaving) {
        if (!extForSaving.isEmpty()) {
            try (FileWriter writer = new FileWriter(Main.FILE_JSON, false)) {
                // запись всей строки
                writer.write(extForSaving);
                // дозаписываем и очищаем буфер
                writer.flush();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
