package com.example.todolistapp;

import org.threeten.bp.LocalDate;

public class Task implements Comparable<Task> {
    private String title;
    private LocalDate dueDate;
    private boolean isCompleted;

    public Task(String title, LocalDate dueDate) {
        this.title = title;
        this.dueDate = dueDate;
        this.isCompleted = false;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public int compareTo(Task other) {
        // Earlier due dates come first (higher priority)
        return this.dueDate.compareTo(other.dueDate);
    }
}
