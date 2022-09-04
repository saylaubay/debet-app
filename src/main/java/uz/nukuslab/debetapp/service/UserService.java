package uz.nukuslab.debetapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.nukuslab.debetapp.DebetAppApplication;
import uz.nukuslab.debetapp.entity.Company;
import uz.nukuslab.debetapp.entity.User;
import uz.nukuslab.debetapp.entity.enums.RoleName;
import uz.nukuslab.debetapp.payload.ApiResponse;
import uz.nukuslab.debetapp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    DebetAppApplication debetAppApplication;

    public ApiResponse getUsersMyCompany(User user) {
        Company company = user.getCompany();
        Long compId = company.getId();

        List<User> userList = userRepository.findByCompany_IdAndRole_RoleName(compId, RoleName.USER);

        return new ApiResponse("Userler listi", true, userList);
    }

    public ApiResponse blockUser(User user, Long id) {
        User usr = userRepository.findByIdAndCompany_IdAndRole_RoleNameAndCompany_Active(id, user.getCompany().getId(), RoleName.USER, true);
        usr.setEnabled(false);
        try {
            userRepository.save(usr);
            return new ApiResponse("User bloklandi!!!", true);
        }catch (Exception e) {
            return new ApiResponse("Qa'telik", false);
        }
    }

    public ApiResponse blockAdmin(Long id) {
        Optional<User> byAdmin = userRepository.findByIdAndRole_RoleName(id, RoleName.ADMIN);
        if (!byAdmin.isPresent()){
            return new ApiResponse("Bunday admin id tabilmadi!!!", false);
        }
        User user = byAdmin.get();
        user.setEnabled(false);
        userRepository.save(user);
        return new ApiResponse("Admin bloklandi!!!", true);
    }

    public ApiResponse unBlockUser(User user, Long id) {
        User usr = userRepository.findByIdAndCompany_IdAndRole_RoleNameAndCompany_Active(id, user.getCompany().getId(), RoleName.USER, true);
        usr.setEnabled(true);
        try {
            userRepository.save(usr);
            return new ApiResponse("User bloktan ASHILDI!!!", true);
        }catch (Exception e) {
            return new ApiResponse("Qa'telik", false);
        }
    }

    public ApiResponse unBlockAdmin(Long id) {
        Optional<User> byAdmin = userRepository.findByIdAndRole_RoleName(id, RoleName.ADMIN);
        if (!byAdmin.isPresent()){
            return new ApiResponse("Bunday admin id tabilmadi!!!", false);
        }
        User user = byAdmin.get();
        user.setEnabled(true);
        userRepository.save(user);
        return new ApiResponse("Admin bloktan SHIG'ARILDI!!!", true);
    }

    public ApiResponse addBalance(Long workerId, double summa) {
        Optional<User> byWorker = userRepository.findByIdAndCompanyActive(workerId, true);
        if (!byWorker.isPresent()){
            return new ApiResponse("Bunday id li user tabilmadi!!!", false);
        }
        User user = byWorker.get();
        user.setBalance(user.getBalance() + summa);
        userRepository.save(user);
check(user);

        return new ApiResponse("Summa balance qa qosildi!!!", true);
    }

    public void check(User user){
        if (!user.isAccountNonExpired()) {
            user.setAccountNonExpired(true);
            user.setBalance(user.getBalance() - 50);
            userRepository.save(user);
        }

    }

}