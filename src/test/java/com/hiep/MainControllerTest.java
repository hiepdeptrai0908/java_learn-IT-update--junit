package com.hiep;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.hiep.controller.MainController;
import com.hiep.entity.AppUserEntity;
import com.hiep.mapper.UserMapper;
import com.hiep.message.ErMessage;
import com.hiep.model.UserModel;

import mockit.Deencapsulation;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = SpringSecutityApplication.class)
public class MainControllerTest {

	/**
	 * MockMvcオブジェクト
	 */
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext wac;

	/**
	 * 前処理(各テストケースを実行する前に行われる処理)
	 */

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	private UserMapper userMapper = new UserMapperImp();

	private PasswordEncoder passwordEncoder = new PasswordEncoderImp();

	MainController target1 = new MainController();

	class PasswordEncoderImp implements PasswordEncoder {

		@Override
		public String encode(CharSequence rawPassword) {
			// TODO Auto-generated method stub
			return "hiepPassword001";
		}

		@Override
		public boolean matches(CharSequence rawPassword, String encodedPassword) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	class UserMapperImp implements UserMapper {

		@Override
		public List<AppUserEntity> selectByEmail(UserModel userModel) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int createUser(UserModel userModel) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int checkEmail(String getEmail) {
			if ("hhhhhh@email.com".equals(getEmail)) {
				return 1;
			}
			return 0;
		}

	}

	@Test
	public void Testcase1a() throws Exception {

		mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"));
	}

	@Test
	public void Testcase1b() throws Exception {

		mockMvc.perform(get("/login-fail")).andExpect(status().isOk()).andExpect(view().name("login"))
				.andExpect(model().attribute("errorMessengeLogin", "メールアドレスまたはパスワードが正しくありません！"));
		;

	}

	@Test
	public void Testcase1c() throws Exception {

		mockMvc.perform(get("/create-user")).andExpect(status().isOk()).andExpect(view().name("create-user"));

	}

	@Test
	public void Testcase1() throws Exception {

		Deencapsulation.setField(target1, "userMapper", userMapper);
		Deencapsulation.setField(target1, "passwordEncoder", passwordEncoder);

		try {
			UserModel userModel = new UserModel();
			userModel.setEmail("qwer1234@email.com");
			userModel.setPassword("Qq1234567");

			Method method = MainController.class.getDeclaredMethod("doCreateUser", UserModel.class, ErMessage.class);
			method.setAccessible(true);
			ErMessage erMessage = new ErMessage();
			method.invoke(target1, userModel, erMessage);
			Iterator<String> i = erMessage.err.iterator();

			if (i.hasNext()) {
				fail();

			} else {
				// ノーメッセージ
			}

		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void Testcase2() throws Exception {

		Deencapsulation.setField(target1, "userMapper", userMapper);
		Deencapsulation.setField(target1, "passwordEncoder", passwordEncoder);

		try {
			UserModel userModel = new UserModel();
			userModel.setEmail("hhhhhh@email.com");
			userModel.setPassword("123");

			Method method = MainController.class.getDeclaredMethod("doCreateUser", UserModel.class, ErMessage.class);
			method.setAccessible(true);
			ErMessage erMessage = new ErMessage();
			method.invoke(target1, userModel, erMessage);
			int i = erMessage.err.size();
			if (i > 0) {

				assertEquals("パスワードの定型フォーマットが正しくない！", erMessage.err.get(0));
				assertEquals("8桁以上の大文字と小文字と数字を入力してください！", erMessage.err.get(1));
				assertEquals("このメールアドレスはすでに使いました！", erMessage.err.get(2));
				assertEquals(3, i);

			} else {
				fail();
			}

		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void Testcase4() throws Exception {

		Deencapsulation.setField(target1, "userMapper", userMapper);
		Deencapsulation.setField(target1, "passwordEncoder", passwordEncoder);

		try {
			UserModel userModel = new UserModel();
			userModel.setEmail("hhhhhh");
			userModel.setPassword("Qq1234567");

			Method method = MainController.class.getDeclaredMethod("doCreateUser", UserModel.class, ErMessage.class);
			method.setAccessible(true);
			ErMessage erMessage = new ErMessage();
			method.invoke(target1, userModel, erMessage);
			int i = erMessage.err.size();
			if (i > 0) {
				assertEquals("メールの定型フォーマットが正しくない！", erMessage.err.get(0));
				assertEquals(1, i);

			} else {
				fail();
			}

		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void Testcase3() throws Exception {

		Deencapsulation.setField(target1, "userMapper", userMapper);
		Deencapsulation.setField(target1, "passwordEncoder", passwordEncoder);

		try {
			UserModel userModel = new UserModel();
			userModel.setEmail("hhhhhh");
			userModel.setPassword("123");

			Method method = MainController.class.getDeclaredMethod("doCreateUser", UserModel.class, ErMessage.class);
			method.setAccessible(true);
			ErMessage erMessage = new ErMessage();
			method.invoke(target1, userModel, erMessage);
			int i = erMessage.err.size();
			if (i > 0) {
				assertEquals("メールの定型フォーマットが正しくない！", erMessage.err.get(0));
				assertEquals("パスワードの定型フォーマットが正しくない！", erMessage.err.get(1));
				assertEquals("8桁以上の大文字と小文字と数字を入力してください！", erMessage.err.get(2));
				assertEquals(3, i);

			} else {
				fail();
			}

		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void Testcase5() throws Exception {

		Deencapsulation.setField(target1, "userMapper", userMapper);
		Deencapsulation.setField(target1, "passwordEncoder", passwordEncoder);

		try {
			UserModel userModel = new UserModel();
			userModel.setEmail("hhhhhh@email.com");
			userModel.setPassword("Qq1234567");

			Method method = MainController.class.getDeclaredMethod("doCreateUser", UserModel.class, ErMessage.class);
			method.setAccessible(true);
			ErMessage erMessage = new ErMessage();
			method.invoke(target1, userModel, erMessage);
			int i = erMessage.err.size();
			if (i > 0) {

				assertEquals("このメールアドレスはすでに使いました！", erMessage.err.get(0));
				assertEquals(1, i);

			} else {
				fail();
			}

		} catch (SecurityException e) {
			e.printStackTrace();
			fail();
		}
	}

}
