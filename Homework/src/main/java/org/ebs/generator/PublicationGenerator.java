package org.ebs.generator;

import lombok.Getter;
import org.ebs.data.Publication;
import org.ebs.field.DateField;
import org.ebs.field.DoubleField;
import org.ebs.field.StringField;
import org.ebs.file.FileManager;
import org.ebs.util.FieldParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Getter
public class PublicationGenerator implements Callable<List<Publication>> {
    private int numberOfPublications;
    private int numberOfThreads;

    private List<Publication> publications;

    private FileManager fileManager;

    public PublicationGenerator(int numberOfPublications, int numberOfThreads, FileManager fileManager) {
        this.numberOfPublications = numberOfPublications;
        this.numberOfThreads = numberOfThreads;

        this.fileManager = fileManager;

        this.publications = new ArrayList<>();
    }

    @Override
    public List<Publication> call() {
        for (int i = 0; i < numberOfPublications / numberOfThreads; i++) {
            StringField companyField = new StringField("company", FieldParams.companyPossibleValues);
            DoubleField valueField = new DoubleField("value", FieldParams.valueMin, FieldParams.valueMax);
            DoubleField dropField = new DoubleField("drop", FieldParams.dropMin, FieldParams.dropMax);
            DoubleField variationField = new DoubleField("variation", FieldParams.variationMin, FieldParams.variationMax);
            DateField dateField = new DateField("date", FieldParams.datePossibleValues);

            Publication publication = new Publication(companyField, valueField, dropField, variationField, dateField);
            publication.generate();

            publications.add(publication);
        }
        return publications;
    }
}
