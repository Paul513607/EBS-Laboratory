package org.ebs.file;

import lombok.Getter;
import lombok.Setter;
import org.ebs.data.Publication;
import org.ebs.data.Subscription;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Getter
@Setter
public class FileManager {
    private String baseDirPath;
    private File publicationFile;
    private File subscriptionFile;

    private BufferedWriter publicationFileWriter;
    private BufferedWriter subscriptionFileWriter;

    private int fileIncrement;

    public FileManager(String baseDirPath) throws IOException {
        this.baseDirPath = baseDirPath;

        fileIncrement = Stream.of(Objects.requireNonNull(new File(baseDirPath).listFiles()))
                .filter(file -> !file.isDirectory())
                .map(file -> {
                    String fileName = file.getName();
                    String numberStr = fileName.replaceAll("[^0-9]", "");
                    return numberStr.isEmpty() ? 1 : Integer.parseInt(numberStr) + 1;
                })
                .max(Integer::compareTo)
                .orElse(1);


        publicationFile = new File(baseDirPath + "/publications" + fileIncrement + ".txt");
        subscriptionFile = new File(baseDirPath + "/subscriptions" + fileIncrement + ".txt");

        publicationFile.createNewFile();
        subscriptionFile.createNewFile();

        publicationFileWriter = new BufferedWriter(new FileWriter(publicationFile, true));
        subscriptionFileWriter = new BufferedWriter(new FileWriter(subscriptionFile, true));
    }

    public synchronized void writePublicationToFile(Publication publication) throws IOException {
        publicationFileWriter.write(publication.toString() + "\n");
        publicationFileWriter.flush();
    }

    public synchronized void writeSubscriptionToFile(Subscription subscription) throws IOException {
        subscriptionFileWriter.write(subscription.toString() + "\n");
        subscriptionFileWriter.flush();
    }

    public synchronized void writePublicationToFile(List<Publication> publicationList) throws IOException {
        publicationList.forEach(publication -> {
            try {
                publicationFileWriter.write(publication.toString() + "\n");
                publicationFileWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public synchronized void writeSubscriptionToFile(List<Subscription> subscriptionList) throws IOException {
        subscriptionList.forEach(subscription -> {
            try {
                subscriptionFileWriter.write(subscription.toString() + "\n");
                subscriptionFileWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void closeFileWriters() throws IOException {
        publicationFileWriter.close();
        subscriptionFileWriter.close();
    }
}
