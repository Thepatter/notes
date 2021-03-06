## 所有对象的通用方法

### equals

不覆盖 `equals` 方法，在这种情况下，类的每个实例都只与它自身相等。如果满足了以下任何一个条件，即使期望的结果 

* **类的每个实例本质上都是唯一的**

  对于代表活动实体而不是值的类来说确实如此。

* **类没有必要提供逻辑相等的测试功能**

  如 `java.util.regex.Pattern` 可以覆盖 equals ，以检查两个 Pattern 实例是否代表同一个正则表达式。在这类情况下，从 Object 继承得到的 equals 实现已经足够

* **超类以及覆盖了 equals ，超类的行为对于这个类也是合适的**

  大多数的 Set、List、Map 实现都从 `Abstract` 类继承 `equals` 实现

* **类时私有的，或包级私有的，可以确定它的 equals 方法永远不会被调用**

如果类具有自己特有的 ”逻辑相等“ 概念（不等于对象等同的概念），而且超类还没有覆盖 `equals` 。这通常属于 ”值类” 的情形。值类仅仅是一个表示值的类，如 `Integer` 或 `String`。程序员在利用 `equals` 方法来比较值对象的引用时，希望直到它们在逻辑上是否相等，而不是想了解它们是否指向同一个对象。这样做也使得这个类的实例可以被用作映射表（map）的键（key），或者 Set 的元素，使映射或集合符合预期的行为。

实例受控确保每个值至多只存在一个对象的类，不需要覆盖 equals 方法，如枚举类。对于这种类型，逻辑相同和对象等同是同一件事。

#### 覆盖 `equals` 方法的通用约定

* **自反性（reflexive）**

  对于任何非 null 的引用值 x，`x.equlas(x)` 必须返回 true

* **对称性（symmetric）**

  对于任何非 null 的引用值 x 和 y，当且仅当 `y.equals(x)` 返回 true 时，`x.quals(y)` 必须返回 true

* **传递性（transitive）**

  对于任何非 null 的引用值 x，y，z，如果 `x.equals(y)` 返回 true，并且 `y.equals(z)` 也返回 `true`，那么 `x.quals(z)` 也必须返回 true

* **一致性（consistent）**

  对于任何非 null 的引用值 x 和 y，只要 `equals` 的比较操作在对象中所用的信息没有被修改，多次调用 `x.equals(y)`，就会一致的返回 true 或 false

* 对于任何非 null 的引用值 x，`x.equals(null)` 必须返回 false

使用 `==` 操作符检查“参数是否为这个对象的引用”。如果是，返回 true。这是一种性能优化，如果比较操作由可能很昂贵，就值得这么做

使用 `instanceof` 操作符检查“参数是否为正确的类型”。如果不是，则返回 false。一般来说：正确的类型是指 equals 方法所在的那个类。某些情况下，是指该类所实现的某个接口。如果类实现的接口改进了 `equals` 约定，允许在实现该接口的类之间进行比较，那么就使用接口。集合接口 `Set`、`List`、`Map`、`Map.Entry` 具有这样的特性

把参数转换成正确的类型，因为转换之前进行过 `instanceof` 测试，所以确保会成功

对于该类中每个“关键”（significant）域，检查参数中的域是否与该对象中对应的域相匹配。如果这些测试全部成功，则返回 true；否则返回 false。如果类型是个接口，就必须通过接口方法访问参数中的域；取决于它们的可访问性

对于既不是 float 也不是 double 类型的基本类型域，可以使用 == 操作符进行比较；对于对象引用域，可以递归地调用 `equals` 方法；对于 float，使用静态 `Float.compare(float, float)`；对于 double，使用 `Double.compare(double，double)`，对 float 和 double 域进行特殊处理是必要的。

有些对象引用域包含 null 可能是合法的，所以，为了避免 NullPointerException 异常，则使用静态方法 `Objects.equals(Object, Object)` 来检查这类域的等同性。

域的比较顺序可能会影响 equals 方法的性能。应该最先比较最有可能不一致的域，或开销最低的域，不应该比较那些不属于对象逻辑状态的域（同步操作的 Lock 域，衍生域）

* 覆盖 equals 时总要覆盖 hashCode
* 不要企图让 equals 方法过于智能
* 不要将 equlas 声明中 Object 对象替换为其他的类型

不要轻易覆盖 equals 方法，如果覆盖 equlas，一定要比较这个类的所有关键域

#### 覆盖 equals 时总要覆盖 hashCode

在每个覆盖 equals 方法的类中，都必须覆盖 hashCode 方法，如果不这样做，就会违反 hashCode 的通用约定，从而导致该类无法结合所有基于散列的集合一起正常运作。Object 规范：

* 在应用程序的执行期间，只要对象的 equals 方法的比较操作所用到的信息没有被修改，那么对同一个对象的多次调用，hashCode 方法都必须始终返回同一个值。在一个应用程序域另一个程序的执行过程中，执行 hashCode 方法所返回的值可以不一致
* 如果两个对象根据 `equals(Object)` 方法比较是相等的，那么调用这两个对象中的 hashCode 方法都必须产生同样的整数结果
* 如果两个对象根据 `equals(Object)` 方法比较是不相等的，那么调用这两个对象中的 hashCode 方法，则不一定要求 hashCode 方法必须产生不同的结果。

不要试图从散列表计算中排除掉一个对象的关键域来提高性能；不要对 hashCode 方法的返回值做出具体决定，因此客户端无法理所当然的依赖它，这样可以为修改提供灵活性