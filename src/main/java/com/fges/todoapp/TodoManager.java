package com.fges.todoapp;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TodoManager {
    private final TodoFileHandler fileHandler;

    public TodoManager(String fileName) throws IOException {
        this.fileHandler = new TodoFileHandler(fileName);
    }

    public void executeCommand(String command, List<String> positionalArgs) throws IOException {
        if (command.equals("insert")) {
            if (positionalArgs.size() < 2) {
                System.err.println("Missing TODO name");
            } else {
                String todo = positionalArgs.get(1);
                fileHandler.insertTodo(todo);
            }
        }

        if (command.equals("list")) {
            fileHandler.listTodos();
        }
    }
}
