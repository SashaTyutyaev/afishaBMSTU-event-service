package ru.afishaBMSTU.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.afishaBMSTU.dto.user.UserFullDto;
import ru.afishaBMSTU.dto.user.UserRegisterRequest;
import ru.afishaBMSTU.dto.user.UserShortDto;
import ru.afishaBMSTU.model.email.Email;
import ru.afishaBMSTU.model.phone.Phone;
import ru.afishaBMSTU.model.user.User;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "emails", source = "emails", qualifiedByName = "mapEmails")
    @Mapping(target = "phones", source = "phones", qualifiedByName = "mapPhones")
    UserFullDto toUserFullDto(User user);

    @Named("mapEmails")
    default Set<String> mapEmails(Set<Email> emails) {
        if (emails == null) {
            return null;
        }
        return emails.stream()
                .map(Email::getEmail)
                .collect(Collectors.toSet());
    }

    @Named("mapPhones")
    default Set<String> mapPhones(Set<Phone> phones) {
        if (phones == null) {
            return null;
        }
        return phones.stream()
                .map(Phone::getPhone)
                .collect(Collectors.toSet());
    }

    @Mapping(target = "password", ignore = true)
    User toUser(UserRegisterRequest userRegisterRequest);

    UserShortDto toUserShortDto(User user);
}
