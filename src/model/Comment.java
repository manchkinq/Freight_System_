package model;

import javafx.collections.FXCollections;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private int id;
    private String commentText;
    private User user;
    private LocalDate dateCreated;
    private LocalDate dateModified;
    private List <Comment> replies;



    public Comment(String commentText) {
        this.commentText = commentText;
        //this.replies = FXCollections.observableArrayList();
    }

    public Comment(int id, String commentText, LocalDate dateCreated) {
        this.id = id;
        this.commentText = commentText;
        this.dateCreated = dateCreated;
    }

    public Comment(int rootCommentId, String rootCommentText) {
        this.id = rootCommentId;
        this.commentText = rootCommentText;
    }


    public void addReplies(Comment reply) {
        replies.add(reply);
    }

    public List<Comment> getReplies() {
        return replies;
    }

    @Override
    public String toString() {
        return "Comment: " + commentText;
    }



}
