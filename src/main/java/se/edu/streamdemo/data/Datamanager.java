package se.edu.streamdemo.data;

import se.edu.streamdemo.task.Deadline;
import se.edu.streamdemo.task.Event;
import se.edu.streamdemo.task.Task;
import se.edu.streamdemo.task.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

public class Datamanager {
    public static final String TODO = "T";
    public static final String DEADLINE = "D";
    public static final String EVENT = "E";
    private File dataFile;

    public File getDataFile() {
        return dataFile;
    }

    public Datamanager(String fileName) {
        dataFile = new File(fileName);
    }

    public void createFile() {
        try {
            if (dataFile.exists()) {
                System.out.println("file exists");
                return;
            }
            if (!dataFile.getParentFile().exists()) {
                dataFile.getParentFile().mkdirs();
            }
            dataFile.createNewFile();
        } catch (IOException e) {
            System.out.println("Cannot create file; reason: " + e.getMessage());
        }
    }

    private ArrayList readFile() throws IOException {
        if (!dataFile.exists()) {
            throw new FileNotFoundException();
        }
        if (dataFile.length() == 0) {
            System.out.println("empty file");
            throw new IOException();
        }
        ArrayList<String> dataItems = (ArrayList) Files.readAllLines(dataFile.toPath(),
                Charset.defaultCharset());

        return dataItems;
    }

    public ArrayList<Task> loadData() {
        ArrayList<Task> taskList = null;
        try {
            ArrayList<String> dataItems = readFile();
            taskList = parse(dataItems);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return taskList;
    }

    private ArrayList<Task> parse(ArrayList<String> dataItems) {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (String line : dataItems) {
            String taskDescription = getTaskDescription(line);
            String taskType = getTaskType(line);
            switch (taskType) {
            case TODO:
                Todo todo = new Todo(taskDescription);
                allTasks.add(todo);
                break;
            case DEADLINE:
                Deadline deadline = new Deadline(taskDescription);
                allTasks.add(deadline);
                break;
            case EVENT:
                Event event = new Event(taskDescription);
                allTasks.add(event);
                break;
            default:
                System.out.println("Unknown task encountered. Skipping");
                break;
            }
        }
        return allTasks;
    }

    private static String getTaskType(String inputLine) {
        String taskType = inputLine.substring(0, 2);
        taskType = taskType.replace("[", "");
        taskType = taskType.replace("]", "");
        return taskType;
    }

    private static String getTaskDescription(String inputLine) {
        String taskDescription = inputLine.substring(4);
        return taskDescription;
    }
}