package ru.kinghp.userdownloader.service;

import ru.kinghp.userdownloader.dto.UserDto;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {

    List<UserDto> downloadUsers(Integer vol);

    void exportFile(Integer vol, HttpServletResponse response);

}
