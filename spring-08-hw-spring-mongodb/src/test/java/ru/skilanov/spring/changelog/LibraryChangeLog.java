package ru.skilanov.spring.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

@ChangeLog
public class LibraryChangeLog {
    @ChangeSet(order = "001", id = "dropDb", author = "kilanov", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertTolstoy", author = "kilanov")
    public void insertTolstoy(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("authors");
        var doc = new Document().append("name", "Tolstoy");
        myCollection.insertOne(doc);
    }

    @ChangeSet(order = "003", id = "insertGogol", author = "kilanov")
    public void insertGogol(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("authors");
        var doc = new Document().append("name", "Gogol");
        myCollection.insertOne(doc);
    }

    @ChangeSet(order = "004", id = "insertDrama", author = "kilanov")
    public void insertDrama(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("genres");
        var doc = new Document().append("name", "Drama");
        myCollection.insertOne(doc);
    }

    @ChangeSet(order = "005", id = "insertHorror", author = "kilanov")
    public void insertHorror(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("genres");
        var doc = new Document().append("name", "Horror");
        myCollection.insertOne(doc);
    }

    @ChangeSet(order = "006", id = "insertBook", author = "kilanov")
    public void insertBook(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("books");
        var doc = new Document().append("title", "voina i mir");
        myCollection.insertOne(doc);
    }

    @ChangeSet(order = "007", id = "insertComment", author = "kilanov")
    public void insertComment(MongoDatabase db) {
        MongoCollection<Document> myCollection = db.getCollection("comments");
        var doc = new Document().append("description", "awesome");
        myCollection.insertOne(doc);
    }
}
