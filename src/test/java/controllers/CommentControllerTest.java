package controllers;

import exceptions.NotExistentComment;
import model.Comment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import service.Baloot;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {
    @Mock
    Baloot baloot;
    @InjectMocks
    CommentController commentController;

    @Test
    void likeCommentSuccessfully() throws NotExistentComment {
        String username = "folan";
        Map<String, String> input = new HashMap<>();
        input.put("username", username);
        when(baloot.getCommentById(1)).thenReturn(new Comment());
        assertThat(commentController.likeComment("1", input).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void likeFailedBecauseNotFound() throws NotExistentComment {
        String username = "folan";
        String password = "bahman";
        Map input = new HashMap();
        input.put("username", username);
        input.put("password", password);
        doThrow(NotExistentComment.class).when(baloot).getCommentById(1);
        assertThat(commentController.likeComment("1", input).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test
    void dislikeCommentSuccessfully() throws NotExistentComment {
        String username = "folan";
        Map input = new HashMap();
        input.put("username", username);
        when(baloot.getCommentById(1)).thenReturn(new Comment());
        assertThat(commentController.dislikeComment("1", input).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void dislikeFailedBecauseNotFound() throws NotExistentComment {
        String username = "folan";
        String password = "bahman";
        Map input = new HashMap();
        input.put("username", username);
        input.put("password", password);
        doThrow(NotExistentComment.class).when(baloot).getCommentById(1);
        assertThat(commentController.dislikeComment("1", input).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
