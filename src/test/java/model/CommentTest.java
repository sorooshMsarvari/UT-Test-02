package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CommentTest {
    private Comment aComment;

    @BeforeEach
    void setup() {
        aComment = new Comment(
                1,
                "folan@folan.com",
                "folani",
                1,
                "folanText"
        );
    }

    @Test
    void testLikeDislikeWorksInNormalUsage() {
        aComment.addUserVote("bahmani", "like");
        aComment.addUserVote("bahmani2", "dislike");
        assertThat(aComment.getLike()).isEqualTo(1);
        assertThat(aComment.getDislike()).isEqualTo(1);
    }

    @Test
    void testUserVoteIsOverwritten() {
        aComment.addUserVote("bahmani", "like");
        aComment.addUserVote("bahmani", "dislike");
        assertThat(aComment.getLike()).isEqualTo(0);
        assertThat(aComment.getDislike()).isEqualTo(1);
    }

    @Test
    void testOnlyLikeAndDislikeStringsAreValid() {
        aComment.addUserVote("bahmani", "invalid vote");
        assertThat(aComment.getLike()).isEqualTo(0);
        assertThat(aComment.getDislike()).isEqualTo(0);
    }
}