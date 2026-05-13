package in.tech_camp.chat_app.custom_user;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.tech_camp.chat_app.entity.UserEntity;
import lombok.Data;

@Data
public class CustomUserDetail implements UserDetails{

  private final UserEntity user;

  //コンストラクタでUserEntityを受け取り、基本的にはゲッター（get〇〇()）で各フィールドの値を返したり、デフォルト値を返すようにする
  public CustomUserDetail(UserEntity user){
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  // @Override
  public Integer getId() {
    return user.getId();
  }

  // @Override
  public String getName() {
    return user.getName();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();//getUsernameでemailのフィールドを返す
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
