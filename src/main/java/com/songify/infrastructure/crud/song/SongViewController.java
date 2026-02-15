package com.songify.infrastructure.crud.song;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class SongViewController {

    private Map<Integer, String> database = new HashMap<Integer, String>();

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/view/songs")
    public String songs(Model model) {
        database.put(1, "Shawn Mendes song");
        database.put(2, "Ariana Grande song");
        database.put(3, "ACDC song");
        database.put(4, "u2 song");

        model.addAttribute("songMap", database);

        return "songs";
    }

}
