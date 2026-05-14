package in.tech_camp.chat_app.entity;

import java.util.List;

import lombok.Data;

@Data
public class UserEntity {
  private Integer id;
  private String name;
  private String email;
  private String password;

  //1つのUserEntityからは複数のRoomUserEntityに紐づくのでRoomUserEntity型のリストを用意  
  private List<RoomUserEntity> roomUsers;
}
