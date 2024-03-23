// App.java

package com.fges.todoapp;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {
        System.exit(exec(args));
    }

    public static int exec(String[] args) throws IOException {
        Options cliOptions = new Options();
        CommandLineParser parser = new DefaultParser();

        cliOptions.addRequiredOption("s", "source", true, "File containing the todos");
        cliOptions.addOption("d", "done", false, "Display only done todos");
        cliOptions.addOption("o", "output", true, "Output file for migrate command");

        CommandLine cmd;
        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.err.println("Fail to parse arguments: " + ex.getMessage());
            return 1;
        }

        List<String> positionalArgs = cmd.getArgList();
        if (positionalArgs.isEmpty()) {
            System.err.println("Missing Command");
            return 1;
        }

        String command = positionalArgs.get(0);

        if (command.equals("migrate")) {
            MigrateCommand.execute(cmd);
        } else {
            executeOtherCommands(cmd, positionalArgs);
        }

        System.err.println("Done.");
        return 0;
    }

    private static void executeOtherCommands(CommandLine cmd, List<String> positionalArgs) throws IOException {
        String fileName = cmd.getOptionValue("s");
        boolean showDone = cmd.hasOption("d");

        String command = positionalArgs.get(0);

        TodoManager todoManager = new TodoManager(fileName);
        todoManager.executeCommand(command, positionalArgs, showDone);
    }
}
