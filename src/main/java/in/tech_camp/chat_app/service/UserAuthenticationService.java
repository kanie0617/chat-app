package in.tech_camp.chat_app.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.tech_camp.chat_app.custom_user.CustomUserDetail;
import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
//UserDetailsServiceを実装するクラスを定義
public class UserAuthenticationService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {
    //emailを受け取り、DBを検索しCustomUserDetailのインスタンスとして返す
    UserEntity userEntity = userRepository.findByEmail(email);

    //不正なメールアドレスが与えられたときの例外
    if (userEntity == null) {
      throw new UsernameNotFoundException("User not found with email: " + email);
    }
    return new CustomUserDetail(userEntity);
  }

}
