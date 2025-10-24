package com.luddy.roomreservation.controller;

import com.luddy.roomreservation.model.Room;
import com.luddy.roomreservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RoomController {
    
    @Autowired
    private RoomService roomService;
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/rooms")
    public String searchRooms(
            @RequestParam(required = false) Integer minCapacity,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Boolean whiteboard,
            @RequestParam(required = false) Boolean projector,
            @RequestParam(required = false) Boolean computer,
            @RequestParam(required = false) Boolean tv,
            @RequestParam(required = false) Boolean wheelchair,
            Model model) {
        
        List<Room> rooms = roomService.filterRooms(
            minCapacity, floor, whiteboard, projector, computer, tv, wheelchair);
        
        model.addAttribute("rooms", rooms);
        return "rooms";
    }
    
    @GetMapping("/room/{id}")
    public String roomDetails(@PathVariable Long id, Model model) {
        roomService.getAllRooms().stream()
            .filter(r -> r.getId().equals(id))
            .findFirst()
            .ifPresent(room -> model.addAttribute("room", room));
        return "room-details";
    }
}
