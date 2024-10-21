package com.example.boardgame.controllers;

import com.example.boardgame.beans.BoardGame;
import com.example.boardgame.beans.ErrorMessage;
import com.example.boardgame.database.DatabaseAccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/boardgames")
public class BoardGameController {

    private DatabaseAccess databaseAccess;

    public BoardGameController(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    @GetMapping
    public List<BoardGame> getBoardGames() {
        return databaseAccess.getBoardGames();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBoardGame(@PathVariable Long id) {
        BoardGame boardGame = databaseAccess.getBoardGame(id);
        if (boardGame != null) {
            return ResponseEntity.ok(boardGame);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage("No such record"));
        }
    }
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> postBoardGame(@RequestBody BoardGame boardGame) {
        try {
            Long id = databaseAccess.addBoardGame(boardGame);
            boardGame.setId(id);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
            return ResponseEntity.created(location).body(boardGame);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage("Name already exists."));
        }
    }
}
