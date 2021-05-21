package ru.kinghp.userdownloader.service.impl;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kinghp.userdownloader.dto.UserDto;
import ru.kinghp.userdownloader.service.UserDownloaderException;
import ru.kinghp.userdownloader.service.randuserdata.AllRandomUsers;
import ru.kinghp.userdownloader.models.User;
import ru.kinghp.userdownloader.repository.UserRepository;
import ru.kinghp.userdownloader.service.UserService;
import ru.kinghp.userdownloader.service.randuserdata.RandomUser;
import ru.kinghp.userdownloader.service.randuserdata.RandomUserName;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Value("${resourceUrl}")
    String resourceUrl;

    @Override
    public List<UserDto> downloadUsers(Integer vol) {

        AllRandomUsers randomUsers = getAllRandomUsers(vol);
        List<User> users = getUsersByRU(randomUsers);
        List<User> creatingUsers = (List<User>) userRepository.saveAll(users);
        List<UserDto> dtos = creatingUsers.stream().map(this::convertUserToDto).collect(Collectors.toList());
        return dtos;
    }

    @Override
    @Transactional(readOnly = true)
    public void exportFile(Integer vol, HttpServletResponse response){

        try {

            exportCsvFile(vol, response);

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new UserDownloaderException();
        }

    }

    private void exportCsvFile(Integer vol, HttpServletResponse response) throws Exception {

        String filename = getCsvFileName();

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

        StatefulBeanToCsv<UserDto> writer = new StatefulBeanToCsvBuilder<UserDto>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        List<User> users = userRepository.findAll(PageRequest.of(0, vol)).getContent();
        List<UserDto> dtos = users.stream().map(this::convertUserToDto).collect(Collectors.toList());

        writer.write(dtos);
    }


    private String getCsvFileName(){
        LocalDateTime now = LocalDateTime.now();
        return now.toLocalDate() + "(" + now.getHour() + "_" + now.getMinute() + "_" + now.getSecond() + ")" + ".csv";
    }

    private List<User> getUsersByRU(AllRandomUsers randomUsers){
        List<User> users = new ArrayList<>();

        for (RandomUser rdUser : randomUsers.getResults()){
            RandomUserName name = rdUser.getName();
            User user = new User();
            user.setFirstName(name.getFirst());
            user.setLastName(name.getLast());
            users.add(user);
        }

        return users;
    }

    private AllRandomUsers getAllRandomUsers(Integer vol){
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(resourceUrl).
                queryParam("results", vol).
                queryParam("inc", "name,dob").
                queryParam("noinfo");

        ResponseEntity<AllRandomUsers> response = restTemplate.getForEntity(builder.build().encode().toUri(), AllRandomUsers.class);
        if (response.getStatusCode() == HttpStatus.OK){
            return response.getBody();
        }else {
            throw new UserDownloaderException();
        }
    }

    private UserDto convertUserToDto(User user){;
        return modelMapper.map(user, UserDto.class);
    }

}
