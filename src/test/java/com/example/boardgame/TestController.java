package com.example.boardgame;

import com.example.boardgame.beans.BoardGame;
import com.example.boardgame.beans.Review;
import com.example.boardgame.database.DatabaseAccess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TestController {

    private DatabaseAccess databaseAccess;
    private MockMvc mockMvc;

    @Autowired
    public void setDatabase(DatabaseAccess da) {
        this.databaseAccess = da;
    }

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    public void testRoot() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void testAddBoardGame() throws Exception {
        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("name", "onecard");
        requestParams.add("level", "1");
        requestParams.add("minPlayers", "2");
        requestParams.add("maxPlayers", "+");
        requestParams.add("gameType", "Party Game");
        int origSize = databaseAccess.getBoardGames().size();
        mockMvc.perform(post("/boardgameAdded").params(requestParams))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andDo(print());
        int newSize = databaseAccess.getBoardGames().size();
        assertEquals(newSize, origSize + 1);
    }

    @Test
    public void testEditReview() throws Exception {
        List<BoardGame> boardGames = databaseAccess.getBoardGames();
        Long boardgameId = boardGames.get(0).getId();
        List<Review> reviews = databaseAccess.getReviews(boardgameId);
        Review review = reviews.get(0);
        Long reviewId = review.getId();
        review.setText("Edited text");
        mockMvc.perform(post("/reviewAdded").flashAttr("review", review))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/" + review.getGameId() + "/reviews"));
        review = databaseAccess.getReview(reviewId);
        assertEquals(review.getText(), "Edited text");
    }

    @Test
    public void testDeleteReview() throws Exception {
        List<BoardGame> boardGames = databaseAccess.getBoardGames();
        Long boardgameId = boardGames.get(0).getId();
        List<Review> reviews = databaseAccess.getReviews(boardgameId);
        Long reviewId = reviews.get(0).getId();
        int origSize = reviews.size();
        mockMvc.perform(get("/deleteReview/{id}", reviewId))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/" + boardgameId + "/reviews"));
        int newSize = databaseAccess.getReviews(boardgameId).size();
        assertEquals(newSize, origSize - 1);
    }
}
