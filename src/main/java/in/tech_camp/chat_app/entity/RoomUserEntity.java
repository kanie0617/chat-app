package in.tech_camp.chat_app.entity;

import lombok.Data;

@Data
public class RoomUserEntity {
  // private Integer id;
  // private Integer userId;
  // private Integer roomId;

  private Long id;
  //外部キーに紐づけされたユーザーの情報を取得するためにUserEntity型userのフィールドを用意する。これでルームを作成したユーザー情報をuserに格納することができる
  private UserEntity user;
  // 外部キーに紐付けられたルームの情報を取得するためにRoomEntity型roomのフィールドを用意する。これで作成されたルームの情報をroomに格納することができる
  private RoomEntity room;
}
