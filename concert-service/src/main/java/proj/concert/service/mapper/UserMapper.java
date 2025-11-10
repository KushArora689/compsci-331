package proj.concert.service.mapper;
import proj.concert.common.dto.UserDTO;
import proj.concert.service.domain.User;

public class UserMapper {
    public static User toDomainObject(UserDTO userdto) {
        User user = new User(
                userdto.getUsername(),
                userdto.getPassword());
        return user;
    }

    public static UserDTO toDTO(User user) {
        UserDTO userdto = new UserDTO(
                user.getUsername(),
                user.getPassword());
        return userdto;
    }

}
