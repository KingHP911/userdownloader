package ru.kinghp.userdownloader;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import ru.kinghp.userdownloader.dto.UserDto;
import ru.kinghp.userdownloader.repository.UserRepository;
import ru.kinghp.userdownloader.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserdownloaderApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;

	@Test
	void whenDownloadedUsersInServiceLayerSize_10() {

		List<UserDto> users = userService.downloadUsers(10);
		assertEquals(users.size(), 10);

	}

	@Test
	void whenDownloadedUsersInControllerSize_20(){

		Map<String, String> urlVariables = new HashMap<>();
		urlVariables.put("vol", "20");

		ResponseEntity<List<UserDto>> response  =
				testRestTemplate.exchange("/download/{vol}", HttpMethod.POST, null, new ParameterizedTypeReference<List<UserDto>>(){}, urlVariables);

		assertTrue(response.getStatusCode() == HttpStatus.OK);
		List<UserDto> dtos = response.getBody();
		assertTrue(dtos.size() == 20);


//		вариант через заголовки
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("vol", "20");
//
//		ResponseEntity<List<UserDto>> response  =
//				testRestTemplate.exchange("/download", HttpMethod.POST, new HttpEntity<>(headers), new ParameterizedTypeReference<List<UserDto>>(){});

	}

	@Test
	void downloadCsvFile_inResponseExpect10Strings(){

		userService.downloadUsers(10);

		Map<String, String> urlVariables = new HashMap<>();
		urlVariables.put("vol", "10");

		ResponseEntity response  =
				testRestTemplate.exchange("/export/{vol}", HttpMethod.GET, null, String.class, urlVariables);

		String body = (String) response.getBody();
		String[] lines = body.split("\r\n|\r|\n");

		assertTrue(lines.length == 10);

	}



}
