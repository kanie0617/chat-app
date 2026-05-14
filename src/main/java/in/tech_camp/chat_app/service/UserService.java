package in.tech_camp.chat_app.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.chat_app.entity.UserEntity;
import in.tech_camp.chat_app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  public void createUserWithEncryptedPassword (UserEntity userEntity) {
    String encodedPassword = encodedPassword(userEntity.getPassword());//1.引数で受け取ったエンティティからPWを取り出してencoded()を呼び出して暗号化
    userEntity.setPassword(encodedPassword);//3.変換後の文字列をエンティティに上書きする
    userRepository.insert(userEntity);//4.リポジトリのinsertでDBに保存
  }

  private String encodedPassword (String password) {
    return passwordEncoder.encode(password);//2.暗号化するメソッド
  }
}
