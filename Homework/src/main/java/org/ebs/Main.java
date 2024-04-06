package org.ebs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ebs.constant.EqOperatorFrequencyMap;
import org.ebs.constant.FieldFrequencyMap;
import org.ebs.data.Publication;
import org.ebs.data.Subscription;
import org.ebs.field.DateField;
import org.ebs.field.DoubleField;
import org.ebs.field.Field;
import org.ebs.field.StringField;
import org.ebs.file.FileManager;
import org.ebs.generator.Generator;
import org.ebs.util.FieldParams;
import org.ebs.util.Timer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static boolean DEBUG = false;
    public static int numberOfThreads;
    public static int numberOfPublications;
    public static int numberOfSubscriptions;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("JSON file not specified.");
            System.exit(1);
        }
        String jsonFilePath = args[0];

        if (args.length < 2) {
            System.err.println("No dir to write publications/subscriptions to specified.");
            System.exit(1);
        }
        String baseDirPath = args[1];
        FileManager fileManager;
        try {
            fileManager = new FileManager(baseDirPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initializeParameters(jsonFilePath);

        Generator generator = new Generator(numberOfThreads, numberOfPublications, numberOfSubscriptions, fileManager);
        Timer timer = Timer.getInstance();
        timer.start();
        generator.generate();
        timer.stop();
        timer.showTimeTaken();

        generator.writePublicationsToFile();
        generator.writeSubscriptionsToFile();

        try {
            fileManager.closeFileWriters();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (DEBUG) {
            List<Publication> publications = generator.getAllPublications();
            System.out.println("Generated Publications:");
            for (Publication publication : publications) {
                System.out.println(publication);
            }

            List<Subscription> subscriptions = generator.getAllSubscriptions();
            System.out.println("\nGenerated Subscriptions:");
            for (Subscription subscription : subscriptions) {
                System.out.println(subscription);
            }
        }
    }

    private static void initializeParameters(String jsonFilePath) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            ObjectNode node = mapper.readValue(new File(jsonFilePath), ObjectNode.class);

            FieldParams.companyPossibleValues = getListFromNode(mapper, node, "companyPossibleValues");
            FieldParams.valueMin = node.get("valueMin").asDouble();
            FieldParams.valueMax = node.get("valueMax").asDouble();
            FieldParams.dropMin = node.get("dropMin").asDouble();
            FieldParams.dropMax = node.get("dropMax").asDouble();
            FieldParams.variationMin = node.get("variationMin").asDouble();
            FieldParams.variationMax = node.get("variationMax").asDouble();
            FieldParams.datePossibleValues = getListFromNode(mapper, node, "datePossibleValues").stream()
                    .map(LocalDate::parse)
                    .collect(Collectors.toList());

            FieldFrequencyMap.fieldFrequencyMap.put("company", node.get("companyFrequency").asDouble());
            FieldFrequencyMap.fieldFrequencyMap.put("value", node.get("valueFrequency").asDouble());
            FieldFrequencyMap.fieldFrequencyMap.put("drop", node.get("dropFrequency").asDouble());
            FieldFrequencyMap.fieldFrequencyMap.put("variation", node.get("variationFrequency").asDouble());
            FieldFrequencyMap.fieldFrequencyMap.put("date", node.get("dateFrequency").asDouble());

            EqOperatorFrequencyMap.eqOperatorFrequencyMap.put("company", node.get("companyOperatorFrequency").asDouble());
            EqOperatorFrequencyMap.eqOperatorFrequencyMap.put("value", node.get("valueOperatorFrequency").asDouble());
            EqOperatorFrequencyMap.eqOperatorFrequencyMap.put("drop", node.get("dropOperatorFrequency").asDouble());
            EqOperatorFrequencyMap.eqOperatorFrequencyMap.put("variation", node.get("variationOperatorFrequency").asDouble());
            EqOperatorFrequencyMap.eqOperatorFrequencyMap.put("date", node.get("dateOperatorFrequency").asDouble());

            Main.numberOfThreads = node.get("numberOfThreads").asInt();
            Main.numberOfPublications = node.get("numberOfPublications").asInt();
            Main.numberOfSubscriptions = node.get("numberOfSubscriptions").asInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getListFromNode(ObjectMapper mapper, ObjectNode node, String fieldName) {
        return mapper.convertValue(node.get(fieldName), mapper.getTypeFactory().constructCollectionType(List.class, String.class));
    }

    private static void initializeFieldParams() {
        FieldParams.companyPossibleValues = List.of("Company1", "Company2", "Company3");
        FieldParams.valueMin = 0.0;
        FieldParams.valueMax = 100.0;
        FieldParams.dropMin = 0.0;
        FieldParams.dropMax = 50.0;
        FieldParams.variationMin = 0.0;
        FieldParams.variationMax = 1.0;
        FieldParams.datePossibleValues = List.of(LocalDate.now(), LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
    }
}
