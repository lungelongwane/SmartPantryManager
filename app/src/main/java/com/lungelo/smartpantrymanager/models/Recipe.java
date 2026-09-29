package com.lungelo.smartpantrymanager.models;

public class Recipe {
    private int id;
    private String name;
    private String instructions;
    public Recipe() {}
    public Recipe(int id,String name,String instructions){this.id=id;this.name=name;this.instructions=instructions;}
    public Recipe(String name,String instructions){this.name=name;this.instructions=instructions;}
    public int getId(){return id;} public void setId(int id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getInstructions(){return instructions;} public void setInstructions(String instructions){this.instructions=instructions;}
}
