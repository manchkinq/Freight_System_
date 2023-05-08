package model;

import javafx.collections.FXCollections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class Forum {

    private int id;
    private String title;
   // private List<Comment> commentList;

    public Forum(String title) {
        this.title = title;
        //this.commentList = FXCollections.observableArrayList();
    }


    @Override
    public String toString() {
        return "Title: " + title;
    }
}
