// TodoFileHandler.java

package com.fges.todoapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TodoFileHandler {
    private final Path filePath;
    private String fileContent;

    public TodoFileHandler(String fileName) throws IOException {
        this.filePath = Path.of(fileName);
        this.fileContent = Files.exists(filePath) ? Files.readString(filePath) : "";
    }

    public void insertTodo(String todo) throws IOException {
        if (filePath.toString().endsWith(".json")) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(fileContent);
            if (actualObj instanceof MissingNode) {
                actualObj = JsonNodeFactory.instance.arrayNode();
            }

            if (actualObj instanceof ArrayNode arrayNode) {
                ObjectNode todoNode = mapper.createObjectNode();
                todoNode.put("todo", todo);
                boolean isDone = true;
                todoNode.put("done", isDone);
                arrayNode.add(todoNode);
            }

            Files.writeString(filePath, actualObj.toString());
        } else {
            System.err.println("Unsupported file format for inserting todos");
        }
    }

    public void insertTodoCSV(String todo) throws IOException {
        if (filePath.toString().endsWith(".csv")) {
            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)) {
                writer.write(todo);
                writer.newLine();
            }
        } else {
            System.err.println("Unsupported file format for inserting todos");
        }
    }

    public void listTodos() throws JsonProcessingException {
        if (filePath.toString().endsWith(".json")) {
            // Affichage des TODOS JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(fileContent);
            if (actualObj instanceof MissingNode) {
                actualObj = JsonNodeFactory.instance.arrayNode();
            }

            if (actualObj instanceof ArrayNode arrayNode) {
                arrayNode.forEach(node -> System.out.println("- " + node.toString()));
            }
        } else if (filePath.toString().endsWith(".csv")) {
            // Affichage des TODOS CSV
            System.out.println(Arrays.stream(fileContent.split("\n"))
                    .map(todo -> "- " + todo)
                    .collect(Collectors.joining("\n"))
            );
        }
    }

    public void listDoneTodos() throws IOException {
        if (filePath.toString().endsWith(".json")) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(fileContent);
            if (actualObj instanceof MissingNode) {
                actualObj = JsonNodeFactory.instance.arrayNode();
            }

            if (actualObj instanceof ArrayNode arrayNode) {
                arrayNode.forEach(node -> {
                    if (node.has("done") && node.get("done").asBoolean()) {
                        System.out.println("- " + node.toString());
                    }
                });
            }
        } else {
            System.err.println("Unsupported file format for listing done todos");
        }
    }

    public void migrateTodos(String outputFileName) throws IOException {
        // Assuming migration means creating a CSV file with the same content
        if (filePath.toString().endsWith(".json")) {
            String[] lines = fileContent.split("\n");
            Path outputPath = Path.of(outputFileName);
            try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            System.out.println("Migration completed.");
        } else {
            System.err.println("Unsupported file format for migration");
        }
    }

    public Path getFilePath() {
        return filePath;
    }
}
