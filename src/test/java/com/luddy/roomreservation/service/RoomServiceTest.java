package com.luddy.roomreservation.service;

import com.luddy.roomreservation.model.Room;
import com.luddy.roomreservation.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoomServiceTest {
    
    @Mock
    private RoomRepository roomRepository;
    
    @InjectMocks
    private RoomService roomService;
    
    private Room room1, room2, room3;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // setup test rooms
        room1 = new Room();
        room1.setId(1L);
        room1.setRoomNumber("101");
        room1.setFloor(1);
        room1.setCapacity(20);
        room1.setHasWhiteboard(true);
        room1.setHasProjector(true);
        
        room2 = new Room();
        room2.setId(2L);
        room2.setRoomNumber("201");
        room2.setFloor(2);
        room2.setCapacity(40);
        room2.setHasProjector(true);
        room2.setWheelchairAccessible(true);
        
        room3 = new Room();
        room3.setId(3L);
        room3.setRoomNumber("102");
        room3.setFloor(1);
        room3.setCapacity(10);
        room3.setHasComputer(true);
    }
    
    @Test
    void testGetAllRooms() {
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2, room3));
        List<Room> rooms = roomService.getAllRooms();
        assertEquals(3, rooms.size());
    }
    
    @Test
    void testGetRoomsByFloor() {
        when(roomRepository.findByFloor(1)).thenReturn(Arrays.asList(room1, room3));
        List<Room> rooms = roomService.getRoomsByFloor(1);
        assertEquals(2, rooms.size());
    }
    
    @Test
    void testFilterByCapacity() {
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2, room3));
        List<Room> rooms = roomService.filterRooms(20, null, null, null, null, null, null);
        assertEquals(2, rooms.size());
    }
    
    @Test
    void testFilterByFloor() {
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2, room3));
        List<Room> rooms = roomService.filterRooms(null, 2, null, null, null, null, null);
        assertEquals(1, rooms.size());
        assertEquals("201", rooms.get(0).getRoomNumber());
    }
    
    @Test
    void testFilterByProjector() {
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2, room3));
        List<Room> rooms = roomService.filterRooms(null, null, null, true, null, null, null);
        assertEquals(2, rooms.size());
    }
    
    @Test
    void testFilterMultipleCriteria() {
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room1, room2, room3));
        // looking for floor 2 with min 30 people and projector
        List<Room> rooms = roomService.filterRooms(30, 2, null, true, null, null, null);
        assertEquals(1, rooms.size());
        assertEquals("201", rooms.get(0).getRoomNumber());
    }
    
    @Test
    void testSaveRoom() {
        when(roomRepository.save(room1)).thenReturn(room1);
        Room saved = roomService.saveRoom(room1);
        assertNotNull(saved);
        verify(roomRepository, times(1)).save(room1);
    }
    
    @Test
    void testDeleteRoom() {
        roomService.deleteRoom(1L);
        verify(roomRepository, times(1)).deleteById(1L);
    }
}
