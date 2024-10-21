package com.example.boardgame;

import com.example.boardgame.beans.BoardGame;
import com.example.boardgame.database.DatabaseAccess;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
class TestDatabase {

    private DatabaseAccess da;

    @Autowired
    public void setDatabase(DatabaseAccess da) {
        this.da = da;
    }

    @Test
    public void testDatabaseAddBoardGame() {
        BoardGame boardGame = new BoardGame();
        boardGame.setName("onecard");
        boardGame.setLevel(1);
        boardGame.setMinPlayers(2);
        boardGame.setMaxPlayers("+");
        boardGame.setGameType("Party Game");
        int originalSize = da.getBoardGames().size();
        da.addBoardGame(boardGame);
        int newSize = da.getBoardGames().size();
        assertEquals(newSize, originalSize + 1);
    }
}
