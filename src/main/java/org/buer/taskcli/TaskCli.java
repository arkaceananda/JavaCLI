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
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author ADVAN
 */
public class TaskCli {
    
    public static final String FILE_NAME = "src/main/java/org/buer/taskcli/tasks.csv";
    public static final ArrayList<String> TASKS = new ArrayList<>();

    public static void main(String[] args) {
        initFile();
        loadData();
        menu();
    }
    
    private static void menu() 
    {
        
        try (Scanner scanner = new Scanner(System.in))
        {
            while(true) 
        {
            System.out.println("""
                               Menu
                               [1]. Add tasks
                               [2]. Read Tasks
                               [3]. Delete Tasks
                               [4]. Exit
                               """.stripIndent());
            try
            {
                String input;
                input = scanner.nextLine();
                if (input.isEmpty() || input.isBlank())
            {
                System.out.println("Please choose between the menu above!!");
            }
                if ("1".equals(input)) 
                {
                    System.out.println("Write new task: ");
                    String taskName = scanner.nextLine();
                    saveTask(taskName);
                    TASKS.add(taskName);
                } else if ("2".equals(input))
                {
                    System.out.println("\nTasks List: ");
                    for (int i = 0; i < TASKS.size(); i++)
                    {
                        System.out.printf("[%d]. %s%n", i + 1, TASKS.get(i));
                    }
                } else if ("3".equals(input)) 
                {
                    System.out.println("Choose id number you wanna delete: ");
                    if (TASKS.isEmpty())
                    {
                        System.err.println("Tasks is empty!!");
                    }
                    int index = Integer.parseInt(scanner.nextLine()) - 1;
                    if (index >= 0 && index < TASKS.size()) 
                    {
                        TASKS.remove(index);
                        refreshCSV();
                        System.out.println("Successfully delete data");
                    }
                    
                } else if ("4".equals(input))
                {
                    break;
                }
            } catch (NumberFormatException _)
            {
                System.err.println("[!] ERROR: Please input number instead of letter");
            } catch (NullPointerException npe)
            {
                System.err.println("[!] WARNING: " + npe.getMessage());
            } catch (Exception _)
            {
                System.out.println("Unknown Error");
            }
        }
    } catch (Exception e)
    {
        System.out.println("Unknown Error");
    }
}
    
    private static void saveTask(String taskName)
    {
        try (FileWriter write = new FileWriter(FILE_NAME, true))
        {
            int id = TASKS.size() + 1;
            write.append(id + ", " + taskName + "\n");
            System.out.println("Successfully insert task to CSV");
        } catch (IOException e)
        {
            System.err.println("[!] ERROR: " + e.getMessage());
        }
    }
    
    private static void initFile() 
    {
        File file = new File(FILE_NAME);
        if(!file.exists())
        {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) 
            {
                writer.println("id,task");
                System.out.println("Csv file successfully created!");
            } catch (IOException | NullPointerException e) 
            {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) 
            {
                System.out.println("Failed to create new file");
            }
        }
    }
    
    private static void loadData()
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME)))
        {
            String line;
            reader.readLine();
            
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                
                if (data.length > 1) {
                    TASKS.add(data[1]);
                }
            }
        } catch (FileNotFoundException ex) {
            System.err.println("[!] ERROR: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("[!] Failed To Load Data: " + ex.getMessage());
        }
    }
    
    private static void refreshCSV() 
    {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME)))
        {
            writer.println("id,task");
            
            for (int i = 0; i < TASKS.size(); i++) 
            {
                writer.println((i + 1) + "," + TASKS.get(i));
            }
        } catch (IOException ex) 
        {
            System.err.println("[!] FAILED TO LOAD FILE: " + ex.getMessage());
        }
    }
}
