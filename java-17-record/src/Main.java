import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Main {
  public static void main(String[] args) {
    BiFunction<Integer, String, Alien> createAlienFunc = Alien::new;
//    Function<Alien, String > returnAlienName = Alien::getName;

    Alien a1 = createAlienFunc.apply(1, "Navin");
    Alien a2 = createAlienFunc.apply(0, "Navin");
    System.out.println(a1.equals(a2)); // true
    System.out.println(a1); // Alien[id=1, name=Navin]
    System.out.println(a2);// Alien[id=1, name=Navin]


  }
}

// Class Alien ta chỉ dùng chỉ để chứa dữ liệu khi mà object được tạo ra
// (không thay đổi dữ liệu ở trong object đó dù ở bất kì trường hợp nào)
// -> Tạo ra quá nhiều function chỉ để đọc và đảm bảo dữ liệu đúng nếu như 2 object mang cùng một dữ liệu
// Giải pháp: Dùng "record" chỉ cần 1 dòng
//class Alien {
//  private final int id;
//
//  private final String name;
//
//  Alien(int id, String name) {
//    this.id = id;
//    this.name = name;
//  }
//
//  public int getId() {
//    return id;
//  }
//
//  public String getName() {
//    return name;
//  }
//
//  @Override
//  public String toString() {
//    return "Alien{" +
//        "id=" + id +
//        ", name='" + name + '\'' +
//        '}';
//  }
//
//  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//    if (o == null || getClass() != o.getClass()) return false;
//    Alien alien = (Alien) o;
//    return id == alien.id && Objects.equals(name, alien.name);
//  }
//
//  @Override
//  public int hashCode() {
//    return Objects.hash(id, name);
//  }
//}

record Alien(int id, String name) {

  // Kiểm tra khi tạo object từ record
  public Alien(int id, String name) {
    if (id == 0)
      throw new IllegalArgumentException("Id cannot be zero");
    this.id = id;
    this.name = name;
  }
}