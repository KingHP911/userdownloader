package ru.kinghp.userdownloader.controllers;

import lombok.Data;
import org.springframework.web.bind.annotation.*;
import ru.kinghp.userdownloader.dto.UserDto;
import ru.kinghp.userdownloader.service.UserDownloaderException;
import ru.kinghp.userdownloader.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Data
@RestController
public class DownloaderController {

    private final UserService userService;

    @PostMapping("/download/{vol}")
    @ResponseBody
    @ExceptionHandler(UserDownloaderException.class)
    public List<UserDto> download(@PathVariable("vol") Integer vol){
        List<UserDto> downloadedUsers = userService.downloadUsers(vol);
        return downloadedUsers;
    }

    @GetMapping("/export/{vol}")
    @ResponseBody
    @ExceptionHandler(UserDownloaderException.class)
    public void export(@PathVariable("vol") Integer vol, HttpServletResponse response){
        userService.exportFile(vol, response);
    }


//		вариант через заголовки
//    @PostMapping("/download")
//    @ResponseBody
//    @ExceptionHandler(UserDownloaderException.class)
//    public List<UserDto> download(@RequestHeader(name = "vol") Integer vol){
//        List<UserDto> downloadedUsers = userService.downloadUsers(vol);
//        return downloadedUsers;
//    }

}
