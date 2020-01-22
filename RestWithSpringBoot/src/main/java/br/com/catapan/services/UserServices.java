package br.com.catapan.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.catapan.converter.DozerConverter;
import br.com.catapan.data.model.User;
import br.com.catapan.data.vo.v1.UserVO;
import br.com.catapan.exception.ResourceNotFoundException;
import br.com.catapan.repository.UserRepository;

@Service
public class UserServices implements UserDetailsService {

	@Autowired
	UserRepository repository;

	public UserServices(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = repository.findByUsername(username);
		if (user != null) {
			return user;
		} else {
			throw new UsernameNotFoundException("Username " + username + " not found");
		}
	}
	
	private String getCryptPasswordEncoder(String password) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(16);
		return bCryptPasswordEncoder.encode(password);
	}
	
	public UserVO create(UserVO user) {
		user.setPassword(getCryptPasswordEncoder(user.getPassword()));
		var entity = DozerConverter.parseObject(user, User.class);
		var vo = DozerConverter.parseObject(repository.save(entity), UserVO.class);
		return vo;
	}

	public List<UserVO> findAll() {
		return DozerConverter.parseListObjects(repository.findAll(), UserVO.class);
	}

	public UserVO findById(Long id) {
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		return DozerConverter.parseObject(entity, UserVO.class);
	}

	public void delete(Long id) {
		User entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		repository.delete(entity);
	}

}