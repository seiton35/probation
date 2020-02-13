package com.company;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import support.Task;
import support.Timer;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

    static Task[] task = new Task[20];

    JsonFactory jsonFactory = new JsonFactory();


    private static Timer timer;
    private Scanner in;
    private boolean quit;

    public boolean getQuit() {
        return quit;
    }

    public void setQuit(){
        this.quit = true;
    }

    static String help = "list - list tasks\n" +
            "new - new task\n" +
            "start - start timer \n" +
            "stop - stop timer\n" +
            "comp - mark as completed\n" +
            "del - delete task\n" +
            "quit - quit task manager";

    public static void main(String[] args) throws JsonProcessingException {
        Main main = new Main();

        task[0] = new Task(1,"eat","eating", false);
        task[1] = new Task(2,"eadfst","ppping", true);
        task[2] = new Task(3,"edsfsdft","sing", false);

        main.saveTasks();

        ObjectMapper ObjMapper = new ObjectMapper();
        String allTasksString = ObjMapper.writeValueAsString(task);
        System.out.println(allTasksString);


        main.in = new Scanner(System.in);

        System.out.println(help);
        while (!main.getQuit())
            main.inputCommand();
    }

    private void saveTasks(){
        try (JsonGenerator jsonGenerator = jsonFactory.createGenerator(
                new File("activeTasks.json"), JsonEncoding.UTF8)){
            jsonGenerator.writeStartArray();
            for (Task tasy : task){
                if (tasy != null){
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeNumberField("num", tasy.getNum());
                    jsonGenerator.writeStringField("name", tasy.getName());
                    jsonGenerator.writeStringField("backlog", tasy.getBacklog());
                    jsonGenerator.writeBooleanField("complete", tasy.getComplete());
                    jsonGenerator.writeEndObject();
                }
            }
            jsonGenerator.writeEndArray();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()) .log(Level.SEVERE, null, ex);
        }
    }

    private int getLastTaskNum() {
        int lastTaskNum = 0;
        for (Task tasy : task) {
            if (tasy != null) {
                lastTaskNum++;
            }else{
                break;
            }
        }
        return lastTaskNum;
    }

    private boolean hasTask(){
        boolean hasTask = false;
        for (Task tasy : task) {
            if (tasy != null) {
                hasTask = true;
            }
        }
        return hasTask;
    }

    private void printTasks() {
        if (hasTask()){
            for (Task tasy : task) {
                if (tasy != null) {
                    System.out.println(tasy);
                }

            }
        }else {
            System.out.println("no tasks!");
        }
    }

    private int InputNumOfaTask() {
        System.out.print("Input a number of task: ");
        int numberOfTask = 0;
        if (in.hasNextInt()){
            numberOfTask = in.nextInt();
        }
        return numberOfTask;
    }

    private void completeTask() {
        if (hasTask()) {
            int numOfTask = InputNumOfaTask();
            if (task[numOfTask - 1] != null) {
                if (!task[numOfTask - 1].getComplete()){
                    task[numOfTask - 1].setComplete();
                    System.out.println("task has been completed!");
                }
                else {
                    System.out.println("task already completed!");
                }
            }
            else
                System.out.println("no task with this number!");
        }
        else {
            System.out.println("no tasks!");
        }
    }

    private void deleteTask() {
        if (hasTask()) {
            int numOfTask = InputNumOfaTask();
            if (task[numOfTask - 1] != null) {
                task[numOfTask - 1] = null;
                System.out.println("task has been deleted!");
            }
            else
                System.out.println("no task with this number!");
        }
        else {
            System.out.println("no tasks!");
        }
    }

    private void startTimer() {
        this.timer = new Timer();
        timer.start();
    }

    private void stopTimer() {
        timer.setStop();
    }

    private void newTask() {
        System.out.print("Input a name of task: ");
        String name = in.nextLine();
        String steps = "";
        int lastTask = getLastTaskNum();
        if (name.length() != 0) {
            System.out.print("Input a steps: ");
            steps = in.nextLine();
            if (steps.length() == 0) {
                System.out.println("must be at least one step!");
                return;
            }
        } else {
            System.out.println("name is too short!");
            return;
        }
        task[lastTask] = new Task(lastTask + 1, name, steps, false);
        System.out.println("task has been added!");

    }

    private void inputCommand() {
        switch (in.nextLine()) {
            case ("help"):
                System.out.println(help);
                break;
            case ("start"):
                startTimer();
                break;
            case ("stop"):
                stopTimer();
                break;
            case ("comp"):
                completeTask();
                break;
            case ("del"):
                deleteTask();
                break;
            case ("new"):
                newTask();
                break;
            case ("list"):
                printTasks();
                break;
            case ("quit"):
                setQuit();
                break;
            default:
                System.out.println("unknown command");
                break;
        }
    }
}
