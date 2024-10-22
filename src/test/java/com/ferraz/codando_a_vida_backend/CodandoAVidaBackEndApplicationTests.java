package com.ferraz.codando_a_vida_backend;

import com.ferraz.codando_a_vida_backend.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CodandoAVidaBackEndApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
		assertThat(userRepository).isNotNull();
	}

}
