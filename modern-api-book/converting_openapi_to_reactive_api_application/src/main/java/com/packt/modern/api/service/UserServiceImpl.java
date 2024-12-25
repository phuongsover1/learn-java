package com.packt.modern.api.service;

import com.packt.modern.api.entity.AddressEntity;
import com.packt.modern.api.entity.CardEntity;
import com.packt.modern.api.entity.UserEntity;
import com.packt.modern.api.exceptions.AddressNotFoundException;
import com.packt.modern.api.exceptions.CustomerNotFoundException;
import com.packt.modern.api.exceptions.ErrorCode;
import com.packt.modern.api.exceptions.UserNotInAdress;
import com.packt.modern.api.model.User;
import com.packt.modern.api.repository.AddressRepository;
import com.packt.modern.api.repository.CardRepository;
import com.packt.modern.api.repository.UserAddressRepository;
import com.packt.modern.api.repository.UserRepository;
import java.util.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final CardRepository cRepo;
  private final UserAddressRepository UARepo;
  private final AddressRepository ARepo;

  public UserServiceImpl(
      UserRepository userRepository,
      CardRepository cRepo,
      UserAddressRepository uaRepo,
      AddressRepository aRepo) {
    this.userRepository = userRepository;
    this.cRepo = cRepo;
    UARepo = uaRepo;
    ARepo = aRepo;
  }

  @Override
  public Mono<Void> deleteCustomerById(String id) {
    return userRepository.deleteById(UUID.fromString(id));
  }

  @Override
  public Flux<AddressEntity> getAddressesByCustomerId(String id) {
    return userRepository.getAddressesByCustomerId(UUID.fromString(id));
  }

  @Override
  public Flux<UserEntity> getAllCustomers() {
    return userRepository.findAll();
  }

  @Override
  public Mono<CardEntity> getCardByCustomerId(String id) {
    return userRepository
        .findById(UUID.fromString(id))
        .switchIfEmpty(Mono.error(new CustomerNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND)))
        .flatMap(u -> cRepo.findByUserId(u.getId()));
  }

  @Override
  public Mono<UserEntity> getCustomerById(String id) {
    return userRepository.findById(UUID.fromString(id));
  }

  public User toModel(UserEntity entity) {
    User t = new User();
    BeanUtils.copyProperties(entity, t);
    t.setFirstName(entity.getFirstName());
    t.setLastName(entity.getLastName());
    t.setUserStatus(entity.getUserStatus());
    return t;
  }

  // Đang làm chức năng lưu địa chỉ có hoặc không kèm theo user  -> DONE
  // TODO: xoá địa chỉ thì xoá trong bảng user_adress trước -> cần id địa chỉ + id user
  // FIXME: Nếu address cần xoá mà không có add User nào thì xoá bình thường

  public UserEntity toEntity(User model) {
    UserEntity t = new UserEntity();
    BeanUtils.copyProperties(model, t);
    t.setFirstName(model.getFirstName());
    t.setLastName(model.getLastName());
    t.setUserStatus(model.getUserStatus());
    return t;
  }

  @Override
  public Mono<Void> deleteAddressOfCustomer(String customerId, String addressId) {
    Mono<UserEntity> uMono = userRepository.findById(UUID.fromString(customerId));
    Mono<AddressEntity> aMono = ARepo.findById(UUID.fromString(addressId));
    return uMono
        .switchIfEmpty(Mono.error(new CustomerNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND)))
        .flatMap(
            u -> {
              return aMono
                  .switchIfEmpty(
                      Mono.error(new AddressNotFoundException(ErrorCode.ADDRESS_NOT_FOUND)))
                  .flatMap(
                      a -> {
                        return UARepo.findByUserIdAndAddressId(u.getId(), a.getId())
                            .switchIfEmpty(
                                Mono.error(new UserNotInAdress(ErrorCode.USER_NOT_IN_ADDRESS)))
                            .flatMap(
                                UA -> {
                                  System.out.println(UA.getUserId());
                                  return UARepo.deleteAddressOfUser(
                                      UA.getUserId(), UA.getAddressId());
                                });
                      });
            });
  }
}
