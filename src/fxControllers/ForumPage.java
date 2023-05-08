
package fxControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.*;
import utils.DbUtils;
import utils.FXMLUtils;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class ForumPage implements Initializable {
    @FXML
    public ListView<Forum> forumList;
    @FXML
    public TreeView<Comment> commentTree;
    @FXML
    public TextArea commentBody;
    @FXML
    public MenuItem updateTitleForumField;
    @FXML
    public MenuItem addTitleForumField;
    @FXML
    public MenuItem deleteTitleForumField;


    public void addTitleForum() {
        try {
            Connection connection = DbUtils.connectToDb();
            String insertForum = "INSERT INTO Forum(title) VALUES (?)";

            PreparedStatement preparedStatement = connection.prepareStatement(insertForum);
            Forum forum = new Forum(commentBody.getText());
            preparedStatement.setString(1, forum.getTitle());
            preparedStatement.execute();
            DbUtils.disconnect(connection, preparedStatement);
            forumList.setItems(DbUtils.getDataForum());

        } catch (Exception e) {
            FXMLUtils.alertDialog("Data format", "Warning", "Input correct data");
        }
    }

    public void updateTitleForum() {

        Connection connection = DbUtils.connectToDb();
        Forum selectedForum = forumList.getSelectionModel().getSelectedItem();
        if (selectedForum != null) {
            try {
                String UPDATE_FORUM = "UPDATE Forum SET title = ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_FORUM);
                preparedStatement.setString(1, commentBody.getText());
                preparedStatement.setInt(2, selectedForum.getId());
                preparedStatement.executeUpdate();
                DbUtils.disconnect(connection, preparedStatement);
                FXMLUtils.throwAlert("Updating forum title", " Successfully updated");
                forumList.setItems(DbUtils.getDataForum());
            } catch (Exception e) {
                FXMLUtils.alertDialog("Data format", "Warning", "Input correct data");
            }
        }
    }

    public void deleteTitleForum() throws SQLException {

        Connection connection = DbUtils.connectToDb();
        Forum selectedForum = forumList.getSelectionModel().getSelectedItem();

        String DELETE_FORUM = "DELETE FROM Forum WHERE Forum.id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FORUM);
        preparedStatement.setInt(1, selectedForum.getId());
        preparedStatement.executeUpdate();

        PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM comments WHERE forum_id = ?");
        preparedStatement1.setInt(1, selectedForum.getId());
        preparedStatement1.executeUpdate();

        DbUtils.disconnect(connection, preparedStatement);
        forumList.setItems(DbUtils.getDataForum());
        refreshCommentTree(null);
    }

    public void goToComments() throws SQLException {
        Forum selectedForum = forumList.getSelectionModel().getSelectedItem();
        refreshCommentTree(selectedForum);
    }

    public void createComment() {
        try {
            Forum selectedForum = forumList.getSelectionModel().getSelectedItem();
            if (selectedForum == null) {
                FXMLUtils.alertDialog("Select forum title", "Warning", null);
            }
            TreeItem<Comment> selectedCommentTreeItem = commentTree.getSelectionModel().getSelectedItem();
            Connection connection = DbUtils.connectToDb();
            String INSERT_COMMENT = "INSERT INTO comments(commentText, date_created, forum_id, isReply, comment_id) VALUES (?,?,?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COMMENT);

            if (selectedCommentTreeItem != null) {
                Comment reply = new Comment(commentBody.getText());
                selectedCommentTreeItem.getChildren().add(new TreeItem<>(reply));
                preparedStatement.setBoolean(4, true);
                preparedStatement.setInt(5, selectedCommentTreeItem.getValue().getId());
            } else {
                preparedStatement.setBoolean(4, false);
                preparedStatement.setInt(5, 0);
            }
            Comment comment = new Comment(commentBody.getText());
            preparedStatement.setString(1, comment.getCommentText());
            preparedStatement.setDate(2, Date.valueOf(LocalDate.now()));
            preparedStatement.setInt(3, selectedForum.getId());
            preparedStatement.execute();
            DbUtils.disconnect(connection, preparedStatement);

            commentTree.setRoot(DbUtils.getDataComment(selectedForum));
            refreshCommentTree(selectedForum);
        } catch (Exception e) {
            FXMLUtils.alertDialog("Data format", "Warning", "Input correct data");
            e.printStackTrace();
        }
    }


    private void refreshCommentTree(Forum forum) throws SQLException {

        Connection connection = DbUtils.connectToDb();
        String forumTitle = forum.getTitle();
        String rootCommentQuery = "SELECT * FROM comments WHERE forum_id = ? AND isReply = 0";
        PreparedStatement rootCommentStmt = connection.prepareStatement(rootCommentQuery);
        rootCommentStmt.setInt(1, forum.getId());
        ResultSet rootCommentResult = rootCommentStmt.executeQuery();
        TreeItem<Comment> root = new TreeItem<>(new Comment("Comments for " + forumTitle));
        while (rootCommentResult.next()) {
            int rootCommentId = rootCommentResult.getInt("id");
            String rootCommentText = rootCommentResult.getString("commentText");
            Comment rootComment = new Comment(rootCommentId, rootCommentText);
            TreeItem<Comment> rootCommentNode = new TreeItem<>(rootComment);
            String childCommentQuery = "SELECT * FROM comments WHERE comment_id = ?";
            PreparedStatement childCommentStmt = connection.prepareStatement(childCommentQuery);
            childCommentStmt.setInt(1, rootCommentId);
            ResultSet childCommentResult = childCommentStmt.executeQuery();
            while (childCommentResult.next()) {
                int childCommentId = childCommentResult.getInt("id");
                String childCommentText = childCommentResult.getString("commentText");
                Comment childComment = new Comment(childCommentId, childCommentText);
                TreeItem<Comment> childCommentNode = new TreeItem<>(childComment);
                rootCommentNode.getChildren().add(childCommentNode);
            }
            root.getChildren().add(rootCommentNode);
        }
        commentTree.setRoot(root);

    }

    public void updateComment() throws SQLException {
        TreeItem<Comment> selectedCommentTreeItem = commentTree.getSelectionModel().getSelectedItem();
        if (selectedCommentTreeItem != null) {
            Connection connection = DbUtils.connectToDb();
            PreparedStatement preparedStatement = connection.prepareStatement("update comments set commentText = ? where id = ?");
            preparedStatement.setString(1, commentBody.getText());
            preparedStatement.setInt(2, selectedCommentTreeItem.getValue().getId());
            preparedStatement.executeUpdate();
            DbUtils.disconnect(connection, preparedStatement);
            commentTree.refresh();
        }
    }

    public void deleteComment() throws SQLException {

        TreeItem<Comment> selectedCommentTreeItem = commentTree.getSelectionModel().getSelectedItem();
        if (selectedCommentTreeItem == null) {
            return;
        }
        Forum selectedForum = forumList.getSelectionModel().getSelectedItem();

        Connection conn = DbUtils.connectToDb();
        PreparedStatement psDeleteComment = conn.prepareStatement("DELETE FROM comments WHERE id = ?");
        psDeleteComment.setInt(1, selectedCommentTreeItem.getValue().getId());
        psDeleteComment.execute();
        DbUtils.disconnect(conn, psDeleteComment);

        refreshCommentTree(selectedForum);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        forumList.setItems(DbUtils.getDataForum());
    }
}


