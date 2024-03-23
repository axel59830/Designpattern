// TodoManager.java

package com.fges.todoapp;

import java.io.IOException;
import java.util.List;

public class TodoManager {
    private final TodoFileHandler fileHandler;

    public TodoManager(String fileName) throws IOException {
        this.fileHandler = new TodoFileHandler(fileName);
    }

    public void executeCommand(String command, List<String> positionalArgs, boolean showDone) throws IOException {
        if (command.equals("insert")) {
            if (positionalArgs.size() < 2) {
                System.err.println("Missing TODO name");
            } else {
                String todo = positionalArgs.get(1);
                if (fileHandler.getFilePath().toString().endsWith(".json")) {
                    fileHandler.insertTodo(todo);
                } else if (fileHandler.getFilePath().toString().endsWith(".csv")) {
                    fileHandler.insertTodoCSV(todo);
                } else {
                    System.err.println("Unsupported file format for inserting todos");
                }
            }
        }

        if (command.equals("list")) {
            if (showDone) {
                fileHandler.listDoneTodos();
            } else {
                fileHandler.listTodos();
            }
        }
    }

    public void migrateTodos(String outputFileName) throws IOException {
        fileHandler.migrateTodos(outputFileName);
    }
}
