/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package org.buer.taskcli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;

import static org.buer.taskcli.Color.*;

/**
 *
 * @author ADVAN
 */
public class TaskCli {
    
    public static final String FILE_NAME = "src/main/java/org/buer/taskcli/tasks.csv";
    public static final ArrayList<String> TASKS = new ArrayList<>();

    void main() {
        initFile();
        loadData();
        menu();
    }
    
    private static void menu() {
        
        try (Scanner scanner = new Scanner(System.in)) {
            label:
            while(true) {
            System.out.println(PURPLE + """
                               Menu
                               [1]. Add tasks
                               [2]. Read Tasks
                               [3]. Update Tasks
                               [4]. Delete Tasks
                               [5]. Exit
                               """.stripIndent() + RESET);
            try {
                String input;
                input = scanner.nextLine();
                if (input.isBlank()) {
                System.out.println(RED + "Please choose between the menu above!!" + RESET);
            }
                switch (input) {
                    case "1":
                        System.out.println("Write new task: ");
                        String taskName = scanner.nextLine();
                        if (taskName == null || taskName.isBlank()) {
                            System.err.println(RED + "Value cannot be null!" + RESET);
                            break;
                        }
                        saveTask(taskName);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String now = LocalDateTime.now().format(formatter);
                        TASKS.add(taskName + " (Created at: " + now + ")");
                        break;
                    case "2":
                        System.out.println("\nTasks List: ");
                        for (int i = 0; i < TASKS.size(); i++) {
                            System.out.printf("[%d]. %s%n", i + 1, TASKS.get(i));
                        }
                        break;
                    case "3": {
                        System.out.println("Choose id number you wanna update: ");
                        int index = Integer.parseInt(scanner.nextLine()) - 1;
                        if (index >= 0 && index < TASKS.size()) {
                            System.out.println("Input task's new name: ");
                            String newName = scanner.nextLine();
                            TASKS.set(index, newName);
                            refreshCSV();
                        } else {
                            System.out.println(RED + "Id is not exist." + RESET);
                        }
                        break;
                    }
                    case "4": {
                        System.out.println("Choose id number you wanna delete: ");
                        if (TASKS.isEmpty()) {
                            System.err.println(RED + "Tasks is empty!!" + RESET);
                        }
                        int index = Integer.parseInt(scanner.nextLine()) - 1;
                        if (index >= 0 && index < TASKS.size()) {
                            TASKS.remove(index);
                            refreshCSV();
                            System.out.println(GREEN + "Successfully delete data" + RESET);
                        }

                        break;
                    }
                    case "5":
                        break label;
                }
            } catch (NumberFormatException _) {
                System.err.println(RED + "[!] ERROR: Please input number instead of letter" + RESET);
            } catch (NullPointerException ex) {
                System.err.println(RED + "[!] WARNING: " + ex.getMessage() + RESET);
            } catch (Exception _) {
                System.out.println(YELLOW + "Unknown Error" + RESET);
            }
        }
        } catch (Exception _) {
        System.out.println(YELLOW+ "Unknown Error" + RESET);
    }
}
    
    private static void saveTask(String taskName) {
        try (FileWriter write = new FileWriter(FILE_NAME, true)) {
            int id = TASKS.size() + 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = LocalDateTime.now().format(formatter);
            write.append(String.valueOf(id))
                    .append(", ")
                    .append(taskName)
                    .append(",")
                    .append(formattedDate)
                    .append("\n");
            System.out.println(GREEN + "Successfully insert task to CSV" + RESET);
        } catch (IOException ex) {
            System.err.println(RED + "[!] ERROR: " + ex.getMessage() + RESET);
        }
    }
    
    private static void initFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("id,task,timestamp");
                System.out.println("Csv file successfully created!");
            } catch (IOException | NullPointerException ex) {
                System.out.println(RED + "Error: " + ex.getMessage() + RESET);
            } catch (Exception _) {
                System.out.println(RED + "Failed to create new file" + RESET);
            }
        }
    }
    
    private static void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                
                if (data.length > 2) {
                    TASKS.add(data[1] + "(Created at: " + data[2] + ")");
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println(RED + "[!] ERROR: " + ex.getMessage() + RESET);
        } catch (IOException ex) {
            System.out.println(RED + "[!] Failed To Load Data: " + ex.getMessage() + RESET);
        }
    }
    
    private static void refreshCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            writer.println("id,task,timestamp");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String now = LocalDateTime.now().format(formatter);
            
            for (int i = 0; i < TASKS.size(); i++) {
                writer.println((i + 1) + "," + TASKS.get(i) + "," + now);
            }
        } catch (IOException ex) {
            System.err.println(RED + "[!] FAILED TO LOAD FILE: " + ex.getMessage() + RESET);
        }
    }
}
