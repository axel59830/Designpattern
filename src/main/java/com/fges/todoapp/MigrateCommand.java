// MigrateCommand.java

package com.fges.todoapp;

import org.apache.commons.cli.CommandLine;
import java.io.IOException;

public class MigrateCommand {


    public static void execute(CommandLine cmd) throws IOException {
        String sourceFileName = cmd.getOptionValue("s");
        String outputFileName = cmd.getOptionValue("o");

        if (sourceFileName == null || outputFileName == null) {
            System.err.println("Missing required arguments: --source and --output");
            return;
        }

        TodoFileHandler sourceFileHandler = new TodoFileHandler(sourceFileName);
        sourceFileHandler.migrateTodos(outputFileName);

        System.err.println("Migration completed.");
    }
}
