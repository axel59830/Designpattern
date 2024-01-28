package com.fges.todoapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TodoFileHandler {
    private final Path filePath;
    private String fileContent;

    public TodoFileHandler(String fileName) throws IOException {
        this.filePath = Paths.get(fileName);
        this.fileContent = Files.exists(filePath) ? Files.readString(filePath) : "";
    }

    public void insertTodo(String todo) throws IOException {
        if (filePath.toString().endsWith(".json")) {
            // Gestion des TODOS JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(fileContent);
            if (actualObj instanceof MissingNode) {
                actualObj = JsonNodeFactory.instance.arrayNode();
            }

            if (actualObj instanceof ArrayNode arrayNode) {
                arrayNode.add(todo);
            }

            Files.writeString(filePath, actualObj.toString());
        } else if (filePath.toString().endsWith(".csv")) {
            // Gestion des TODOS CSV
            if (!fileContent.endsWith("\n") && !fileContent.isEmpty()) {
                fileContent += "\n";
            }
            fileContent += todo;
            Files.writeString(filePath, fileContent);
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
}
