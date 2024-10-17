package com.example.boardgame.controllers;

import com.example.boardgame.beans.BoardGame;
import com.example.boardgame.beans.Review;
import com.example.boardgame.database.DatabaseAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    DatabaseAccess databaseAccess;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @GetMapping("/newUser")
    public String newUser(Model model) {
        List<String> authorities = databaseAccess.getAuthorities();
        model.addAttribute("authorities", authorities);
        return "new-user";
    }

    @PostMapping("/addUser")
    public String addUser(@RequestParam String userName, @RequestParam String password,
                          @RequestParam String[] authorities, Model model, RedirectAttributes redirectAttrs) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (String authority : authorities) {
            authorityList.add(new SimpleGrantedAuthority(authority));
        }
        String encodedPassword = passwordEncoder.encode(password);
        // check existing user
        if (jdbcUserDetailsManager.userExists(userName)) {
            model.addAttribute("errorMsg", "User name already Exists. Try a different user name.");
            model.addAttribute("authorities", authorityList);
            return "new-user";
        } else {
            User user = new User(userName, encodedPassword, authorityList);
            jdbcUserDetailsManager.createUser(user);
            redirectAttrs.addFlashAttribute("userAddedMsg", "User succesfully added!");
            return "redirect:/";
        }
    }

    @GetMapping("/")
    public String goHome(Model model) {
        List<BoardGame> boardgames = databaseAccess.getBoardGames();
        model.addAttribute("boardgames", boardgames);
        return "index";
    }

    @GetMapping("/{id}")
    public String getBoardgameDetail(@PathVariable Long id, Model model) {
        model.addAttribute("boardgame", databaseAccess.getBoardGame(id));
        return "boardgame";
    }

    @GetMapping("/{id}/reviews")
    public String getReviews(@PathVariable Long id, Model model) {
        model.addAttribute("boardgame", databaseAccess.getBoardGame(id));
        model.addAttribute("reviews", databaseAccess.getReviews(id));
        return "review";
    }

    @GetMapping("/secured/addReview/{id}")
    public String addReview(@PathVariable Long id, Model model) {
        model.addAttribute("boardgame", databaseAccess.getBoardGame(id));
        model.addAttribute("review", new Review());
        return "secured/addReview";
    }

    @GetMapping("/{gameId}/reviews/{id}")
    public String editReview(@PathVariable Long gameId, @PathVariable Long id, Model model) {
        Review review = databaseAccess.getReview(id);
        model.addAttribute("review", review);
        model.addAttribute("boardgame", databaseAccess.getBoardGame(gameId));
        return "secured/addReview";
    }

    @GetMapping("/secured/addBoardGame")
    public String addBoardGame(Model model) {
        model.addAttribute("boardgame", new BoardGame());
        return "secured/addBoardGame";
    }

    @PostMapping("/boardgameAdded")
    public String boardgameAdded(@ModelAttribute BoardGame boardgame) {
        Long returnValue = databaseAccess.addBoardGame(boardgame);
        System.out.println("return value is: " + returnValue);
        return "redirect:/";
    }

    @PostMapping("/reviewAdded")
    public String reviewAdded(@ModelAttribute Review review) {
        int returnValue;
        // if id exists, edit
        if (review.getId() != null) {
            returnValue = databaseAccess.editReview(review);
        } else {
            // if id not exists, add
            returnValue = databaseAccess.addReview(review);
        }
        System.out.println("return value is: " + returnValue);
        return "redirect:/" + review.getGameId() +
                "/reviews";
    }

    @GetMapping("/deleteReview/{id}")
    public String deleteReview(@PathVariable Long id) {
        Long gameId = databaseAccess.getReview(id).getGameId();
        int returnValue = databaseAccess.deleteReview(id);
        System.out.println("return value is: " + returnValue);
        return "redirect:/" + gameId + "/reviews";
    }

    @GetMapping("/user")
    public String goToUserSecured() {
        return "secured/user/index";
    }

    @GetMapping("/manager")
    public String goToManagerSecured() {
        return "secured/manager/index";
    }

    @GetMapping("/secured")
    public String goToSecured() {
        return "secured/gateway";
    }

    @GetMapping("/login")
    public String goToLogin() {
        return "login";
    }

    @GetMapping("/permission-denied")
    public String goToDenied() {
        return "error/permission-denied";
    }
}
