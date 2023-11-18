package vanhoang.project.utils;

import vanhoang.project.entity.UserEntity;

public abstract class CheckEntity {
    public static String checkUser (UserEntity userEntity) {
        if (!StringUtils.isNoneEmpty(userEntity.getLoginName()))
            return "user.loginName.empty";
        else if (!StringUtils.isNoneEmpty(userEntity.getPassword()))
            return "user.password.empty";
        else if (userEntity.getPassword().length() < 8 || userEntity.getPassword().length() > 16)
            return "user.password.lengthSize.wrong";
        else if (!StringUtils.isNoneEmpty(userEntity.getFullName()))
            return "user.fullName.empty";
        else if (userEntity.getBirthDay() == null)
            return "user.birthDay.null";
        else if (userEntity.getGender() != 0 && userEntity.getGender() != 1)
            return "user.gender.wrong";
        else if (!StringUtils.isNoneEmpty(userEntity.getEmail()))
            return "user.email.empty";
        else if (StringUtils.checkEmail(userEntity.getEmail()))
            return "user.email.pattern.wrong";
        else
            return null;
    }
}
