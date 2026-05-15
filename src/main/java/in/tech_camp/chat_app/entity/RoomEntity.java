package in.tech_camp.chat_app.entity;

import java.util.List;

import lombok.Data;

@Data
public class RoomEntity {

  private Integer id;
  private String name;

  //1つのRoomEntityからは複数のRoomUserEntityに紐づくのでRoomUserEntity型のリストを用意
  private List<RoomUserEntity> roomUsers;
}
