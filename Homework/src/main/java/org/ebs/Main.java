package org.ebs;

import org.ebs.data.Publication;
import org.ebs.data.Subscription;
import org.ebs.field.DateField;
import org.ebs.field.DoubleField;
import org.ebs.field.Field;
import org.ebs.field.StringField;
import org.ebs.generator.Generator;
import org.ebs.util.FieldParams;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Main {
//    public static void main(String[] args) {
//        List<Field<?>> subscriptionFields = Arrays.asList(
//                new StringField("company", Arrays.asList("Google", "Apple", "Microsoft")),
//                new DoubleField("value", 0, 100),
//                new DoubleField("variation", 0, 1)
//        );
//
//        int numPublications = 5;
//        int numSubscriptions = 5;
//
//        Publication[] publications = new Publication[numPublications];
//        Subscription[] subscriptions = new Subscription[numSubscriptions];
//
//        for (int i = 0; i < numPublications; i++) {
//            publications[i] = new Publication(new StringField("company", Arrays.asList("Google", "Apple", "Microsoft")),
//                    new DoubleField("value", 0, 100),
//                    new DoubleField("drop", 0, 100),
//                    new DoubleField("variation", 0, 1),
//                    new DateField("date", Arrays.asList(
//                            LocalDate.of(2022, 1, 1),
//                            LocalDate.of(2022, 2, 2),
//                            LocalDate.of(2022, 3, 3)
//                    )));
//        }
//
//        for (int i = 0; i < numSubscriptions; i++) {
//            subscriptions[i] = new Subscription(subscriptionFields);
//        }
//
//        System.out.println("Publications:");
//        for (Publication publication : publications) {
//            publication.generate();
//            System.out.println(publication);
//        }
//
//        System.out.println("\nSubscriptions:");
//        for (Subscription subscription : subscriptions) {
//            subscription.generate();
//            System.out.println(subscription);
//        }
//    }

    public static void main(String[] args) {
        initializeFieldParams();

        Generator generator = new Generator(1, 10, 10);
        generator.generate();

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
