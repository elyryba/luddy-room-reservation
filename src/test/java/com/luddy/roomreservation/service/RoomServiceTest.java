package com.luddy.roomreservation.service;

import com.luddy.roomreservation.model.Room;
import com.luddy.roomreservation.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    private Room testRoom;
    private Room anotherRoom;

    @BeforeEach
    void setup() {
        // create some test rooms
        testRoom = new Room("101", 1, 20);
        testRoom.setId(1L);
        testRoom.setHasWhiteboard(true);
        testRoom.setElevatorAccess(true);
        
        anotherRoom = new Room("201", 2, 40);
        anotherRoom.setId(2L);
        anotherRoom.setHasProjector(true);
    }

    @Test
    void getAllRooms_ShouldReturnAllRooms() {
        // setup
        when(roomRepository.findAll()).thenReturn(Arrays.asList(testRoom, anotherRoom));
        
        // execute
        List<Room> result = roomService.getAllRooms();
        
        // verify
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(roomRepository, times(1)).findAll();
    }

    @Test
    void getRoomById_WhenRoomExists_ShouldReturnRoom() {
        when(roomRepository.findById(1L)).thenReturn(Optional.of(testRoom));
        
        Optional<Room> result = roomService.getRoomById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("101", result.get().getRoomNumber());
        assertEquals(1, result.get().getFloor());
    }

    @Test
    void getRoomById_WhenRoomDoesNotExist_ShouldReturnEmpty() {
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());
        
        Optional<Room> result = roomService.getRoomById(999L);
        
        assertFalse(result.isPresent());
    }

    @Test
    void saveRoom_ShouldCallRepository() {
        when(roomRepository.save(any(Room.class))).thenReturn(testRoom);
        
        Room saved = roomService.saveRoom(testRoom);
        
        assertNotNull(saved);
        assertEquals("101", saved.getRoomNumber());
        verify(roomRepository, times(1)).save(testRoom);
    }

    @Test
    void filterRooms_ByCapacity_ShouldReturnMatchingRooms() {
        Room smallRoom = new Room("102", 1, 4);
        Room mediumRoom = new Room("103", 1, 20);
        Room largeRoom = new Room("104", 1, 50);
        
        when(roomRepository.findAll()).thenReturn(
            Arrays.asList(smallRoom, mediumRoom, largeRoom)
        );
        
        List<Room> result = roomService.filterRooms(20, null, null, null, null, null);
        
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(r -> r.getCapacity() >= 20));
    }

    @Test  
    void filterRooms_ByWhiteboard_ShouldReturnRoomsWithWhiteboard() {
        Room roomWithWhiteboard = new Room("105", 1, 10);
        roomWithWhiteboard.setHasWhiteboard(true);
        
        Room roomWithoutWhiteboard = new Room("106", 1, 10);
        roomWithoutWhiteboard.setHasWhiteboard(false);
        
        when(roomRepository.findAll()).thenReturn(
            Arrays.asList(roomWithWhiteboard, roomWithoutWhiteboard)
        );
        
        List<Room> result = roomService.filterRooms(null, true, null, null, null, null);
        
        assertEquals(1, result.size());
        assertTrue(result.get(0).getHasWhiteboard());
    }

    @Test
    void filterRooms_MultipleFilters_ShouldReturnMatchingRooms() {
        Room perfectMatch = new Room("107", 2, 25);
        perfectMatch.setHasWhiteboard(true);
        perfectMatch.setElevatorAccess(true);
        
        Room wrongFloor = new Room("108", 1, 30);
        wrongFloor.setHasWhiteboard(true);
        wrongFloor.setElevatorAccess(true);
        
        when(roomRepository.findAll()).thenReturn(
            Arrays.asList(perfectMatch, wrongFloor)
        );
        
        List<Room> result = roomService.filterRooms(20, true, null, null, true, 2);
        
        assertEquals(1, result.size());
        assertEquals("107", result.get(0).getRoomNumber());
    }

    @Test
    void deleteRoom_ShouldCallRepository() {
        roomService.deleteRoom(1L);
        
        verify(roomRepository, times(1)).deleteById(1L);
    }
}
