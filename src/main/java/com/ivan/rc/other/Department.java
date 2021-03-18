package com.ivan.rc.other;

import java.util.HashSet;
import java.util.Set;

public class Department {
    private String name;
    private int id;
    private int parent;
    private Set<Department> child = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public Department(int id, int parent, String name) {
        this.name = name;
        this.id = id;
        this.parent = parent;
    }

    public Set<Department> getChild() {
        return child;
    }

    public void setChild(Set<Department> child) {
        this.child = child;
    }

}
