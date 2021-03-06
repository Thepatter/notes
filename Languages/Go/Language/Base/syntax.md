###  Go 基础语法

#### 安装运行

##### 安装配置

###### Linux 下安装

* 下载源码

* 配置相关变量

    ```shell
    # 设置 Go 环境变量
    export GOROOT=$HOME/go
    # 相关文件在文件系统的任何地方都能被调用
    export PATH=$PATH:$GOROOT/bin
    # 保存你的工作目录
    export GOPATH=$HOME/Applications/Go
    ```

* 安装 C 工具

    ```shell
    sudo apt-get install bison ed gawk gcc libc6-dev make
    ```

* 安装目录清单

    *目录结构*
    
    |    目录    |              内容               |
    | :--------: | :-----------------------------: |
    |   `/bin`   | 包含可执行文件：编译器，Go 工具 |
    |   `/doc`   |       示例程序，本地文档        |
    |   `/lib`   |               库                |
    |  `/misc`   |  支持 Go 编译器有关的配置文件   |
    | `/os_arch` |    包含标准库的包的对象文件     |
    |   `/src`   |              源码               |
    | `/src/cmd` |   Go 和 C 编译器和命令行脚步    |

###### 配置

Go 开发环境依赖于一些操作系统环境变量

*环境变量*

|   变量    |                             描述                             |
| :-------: | :----------------------------------------------------------: |
| `$GOROOT` |          表示 Go 在主机的安装位置，一般 `$HOME/go`           |
| `$GOARCH` |                   表示目标机器的处理器架构                   |
|  `$GOOS`  |                    表示目标机器的操作系统                    |
| `$GOBIN`  |      表示编译器和链接器的安装位置，默认是 `$GOROOT/bin`      |
| `$GOPATH` | 默认 `$GOROOT` 一样，1.1 版本开始，必须修改为其他路径。它可以包含多个包含 Go 语言源码文件，包文件和可执行文件的路径 |

##### 运行 go 代码

在开发中，你既可以使用 `go run` 也可以使用 `go build`，但正式部署代码时，应该通过 `go build` 生成二进制文件并执行它。在 go 中程序入口必须是 `main` 函数，并且在 `main` 包内。

* 运行

    ```shell
    go run main.go
    ```

* 编译

    ```shell
    go build main.go
    ```

#### 数据类型

*   值类型

    基本类型属于值类型，直接指向存在内存中的值，数值和结构这些复合类型也是值类型，当使用 `=` 将一个变量的值赋值给另一个变量时，在内存中对值进行了拷贝。值类型的变量的值存储在栈中

*   引用类型

    指针，`slices`，`maps`，`channel` 属于引用类型，被引用的变量会存储在堆中，以便进行垃圾回收

##### 基本数据类型

###### bool

布尔类型的值是常量 true 或 false。在格式化输出时，可以使用 `%t` 来表示要输出得值为布尔型

两个类型相同的值可以使用相等 `==` 或 `!=` 运算符来进行比较并获得一个布尔型的值，当运算符两边的值是完全相同的值的时候会返回 true，否则返回 false，并且只有在两个的值的类型相同的情况下才可以使用。

go 对于值之间的比较有非常严格的限制，只有两个类型相同的值才可以进行比较，如果值的类型是接口，它们也必须都实现了相同的接口。如果其中一个值是常量，那么另外一个值的类型必须和该常量类型相兼容。如果以上条件都不满足，则其中一个值的类型必须在被转换为和另外一个值的类型相同后才可以进行比较。

###### int

`int`、`uint`、`uintptr` 长度是根据运行程序所在操作系统决定：

*   `int` 和 `uint` 在 32 位操作系统上，均使用 32 位（4 个字节），在 64 位操作系统上，均使用 64 位（8个字节）
*   `uintptr` 得长度被设定为足够存放一个指针即可

go 中不允许不同类型之间的混合使用，在格式化字符串里，`%d` 用于格式化整数，`%x` 和 `%X` 用于格式化 16 进制表示的数字

*与操作系统架构无关类型都有固定的大小*

|   整数   |                    范围                    |
| :------: | :----------------------------------------: |
|  `int8`  |                 -128 ~ 127                 |
| `int16`  |               -32768 ~ 32767               |
| `int32`  |          -2147483648 ~ 2147483647          |
| `int64`  | -9223372036854775808 ~ 9223372036854775807 |
| `uint8`  |                  0 ~ 255                   |
| `uint16` |                 0 ~ 65535                  |
| `uint32` |               0 ~ 4294967295               |
| `uint64` |          0 ~ 18446744073709551615          |

###### float

Go 语言中没有 float 类型（Go 语言中只有 `float32` 和 `float64`）没有 `double` 类型。`%g` 用于格式化浮点型，`%f` 输出浮点数，`%e` 输出科学计数表示法，`%0d` 输出定长的整数（开头的数字 0 是必须的）`%n.mg` 用于表示数字 n 并精确到小数点后 m 位，除了使用 g 之外，还可以使用 e 或者 f。

*   `float32 ` 精确到小数点后 7 位
*   `float64` 精确到小数点后 15 位。应该尽可能使用 `float64`，`math` 包中所有有关数学运算的函数都会要求接收这个类型

###### byte

字符只是整数的特殊用例。`byte` 类型是 `uint8` 的别名

###### string

字符串是 UTF-8 字符的一个序列（当字符为  ASCII 码时则占用 1 个字节，其他字符根据需要占用 2 - 4 个字节）。字符串是一种值类型，值不可变，是字节的定长数组，Go 支持以下 2 种形式的字面值：

*   解释字符串：

    该类字符串使用双引号括起来，其中的相关的转义字符被替换，转义字符包括：`\n`、`\r`，`\t`，`\u`，`\\`

*   非解释字符串：

    该类字符串使用反引号括起来，支持换行

`string` 类型的零值为长度为零的字符串，即空字符串 `""`，一般的比较运算符通过在内存中按字节比较来实现字符串的对比。使用全局函数 `len()` 来获取字符串所占的字节长度

对于纯 ASCII 码的字符串，可以使用索引访问内容，索引从 0 开始计数。**获取字符串中某个字节的地址的行为是非法的**，使用 `+` 拼接字符串

###### pointer

不能进行指针运算。程序在内存中存储它的值，取地址符是 `&`，放到一个变量前使用就会返回相应变量的内存地址，这个地址可以存储在一个叫做指针的特殊数据类型中

指针的格式化标识符是 `%p`，一个指针变量可以指向任何一个值的内存地址，在 32 位机器上占用 4 个字节，在 64 位机器上占用 8 个字节，并且与它所指向的值的大小无关。可以声明指针指向任何类型的值来表明它的原始性或结构性；

可以在指针类型前加上 `*` 前缀来获取指针所指向的内容，这里的 `*` 号是一个类型更改器，使用一个指针引用一个值被称为间接引用。对于一个空指针的反向引用是不合法的，并且会使程序崩溃

当一个指针被定义后没有分配到任何变量时，它的值为 `nil`，不能得到一个字面量或常量的地址

指针可以指向另一个指针，并且可以进行任意深度的嵌套，可以有多级的间接引用，但在大多数情况下这会使代码结构不清晰。

##### 复合类型

###### 函数

函数是一个类型，支持多个返回值

```go
// 声明函数类型，单个 int 返回值
type Add func(a int, b int) int
// 使用该函数类型
func process(adder Add) int {
    return adder(1, 2)
}
```

###### 结构

go 并非面向对象，具有结构体，可以将一些方法和结构体管理。字段可以是任何类型

```go
// 定义结构体
type Saiyan struct {
    Name string
    Power int
}
// 方法关联在结构体上
func (s *Saiyan) Super() {
    s.Power += 10000
}
// *Saiyan 类型是 Super 方法的接受者，调用 Super 方法
goku := &Saiyan{"Goku", 9001}
goku.Super()
fmt.Println(goku.Power)
// 初始化
goku := Saiyan {
	Name: "Goku",
	Power: 900
}
goku1 := Saiyan{}
goku1.Power = 9000
gok2 := Saiyan {"Goke", 9000}
// 内置 new 函数，使用它来分配类型所需要的内存。new(x) 与 &x{} 相同
goku := new(Saiyan)
// 等价
goku := &Saiyan{}
```

可以使用 `&` 来获取指针，并使用指针修改原始内存地址值，使用 `*`  来声明指针参数。结构体没有构造器，可以创建一个返回所期望类型的实例函数（类似于工厂）

```go
// 返回结构体指针
func NewSaiyan(name string, power int) *Saiyan {
	return &Saiyan {
		Name: name,
		Power: power,
	}
}
// 返回结构体
func NewSaiyan(name string, power int) Saiyan {
	return Saiyan {
		Name: name,
		Power: power,
	}
}
```

###### 组合

```go
type Person struct {
	Name string
}
func (p *Person) Introduce() {
	fmt.Printf("Hi, I'm %s\n", p,Name)
}
type Saiyan struct {
	*Person
	Power int
}
// 使用组合
goku := &Saiyun {
    Person: &Person{"Goku"},
    Power: 9001,
}
goku.Introduct()
```

###### 数组

go 中，数组长度是固定的，在声明一个数组时需要指定它的长度。索引从 0 开始。数组越界访问会报错。

```go
scores := [4]int{9001, 9002, 9999, 10000}
// 等价于
var scores [4]int
scores[0] = 900
// 遍历
for index, value := range scores {
    
}
```

###### 切片

在 go 语言中，很少直接使用数组。取而代之的是使用切片。切片是轻量的包含并表示数组的一部分结构。

```go
// 创建切片
scores := []int{1, 4, 2, 5, 3}
// 创建长度是 10，容量是 10 的切片
scores := make([]int, 10)
// 长度是 0， 容量是 10
scores := make([]int, 0, 10)
```

长度是切片的长度，容量是底层数组的长度，在使用 `make` 创建切片时，可以分别指定切片的长度和容量。使用 `append` 扩充切片，会自动扩展底层数组。

```go
scores := make([]int, 0, 10)
// 扩展切片
scores = append(scores, 5)
```

`[x:]` 是从 X 到结尾的简写，`[:x]` 是从开始到 X 的简写，Go 不支持负数索引。

###### 映射

类似 `hashtable`，可以获取，设置和删除其中的值，映射和切片一样，使用 `make` 方法来创建。

```go
func main() {
    lookup := make(map[string]int)
    lookup["goku"] = 9001
    power, exists := lookup["vegeta"]
    # 等价于
    lookup := map[string]int{
        "goku": 9001,
        "gohan": 2044
    }
    fmt.Println(power, exists)
}
// 获取映射的键的数量
total := len(lookup)
// 删除键对应的值
delete(lookup, "goku")
```

映射是动态变化的，如果事先知道映射会有多少键值，定义一个初始大小将会帮助改善性能，迭代映射是没有顺序的，每次迭代查找会随机返回键值对

```go
// 指定初始大小
lookup := make(map[string]int, 100)
// 迭代映射
for key, value := range lookup {
    
}
```

#### 语法

源文件由小写字母组成，使用下划线分隔，不支持空格和其他特殊字符，以 `.go` 为后缀名，源文件包含任意多行代码，区分大小写，有效的标识符必须以字符（UTF8 或 _）开始；程序中可能会使用这些分隔符： `()`，`[]`，`{}`；程序中可能会使用这些标点符号：`.`，`,`，`;`，`:`，`...`；每个语句不需要以 `;` 结尾，如果将多个语句写在同一行，必须使用 `;` 区分

##### 保留字

###### 关键字

|  break   |   default   |  func  | interface | select |
| :------: | :---------: | :----: | :-------: | :----: |
|   case   |    defer    |   go   |    map    | struct |
|   chan   |    else     |  goto  |  package  | switch |
|  const   | fallthrough |   if   |   range   |  type  |
| continue |     for     | import |  return   |  var   |

###### 预定义标识符

|  append   |    bool    |  byte  |  cap  |  close  | complex |
| :-------: | :--------: | :----: | :---: | :-----: | :-----: |
| complex64 | complex128 | uint16 | copy  |  false  | float32 |
|  float64  |    imag    |  int   | int8  |  int16  | uint32  |
|   int32   |   int64    |  iota  |  len  |  make   |   new   |
|    nil    |   panic    | uint64 | print | println |  real   |
|  recover  |   string   |  true  | uint  |  uint8  | uintptr |

##### 变量和常量

###### 变量

声明变量一般形式是使用 `var` 关键字，当一个变量被声明后，系统自动赋予它该类型的零值：`int` 为 0，`float` 为 `0.0`，`bool` 为  false，string 为 `""`，指针为 `nil`。

```go
var identifier type
```

变量的命名规则采用小驼峰如。在相同作用域下，相同的变量不能被声明两次，Go 不允许程序拥有未使用的变量

*   全局变量

    变量在函数体外声明，如果需要被外部包所使用，则需要将首个单词的首字母也大写。

*   局部变量

    函数体内声明的变量，作用域只在函数体内，参数和返回值变量也是局部变量

*   块级变量

    在 `if` 和 `for` 这些控制结构中声明的变量作用域只在相应的代码块内。一般情况下，局部变量的作用域可以通过代码块判断

```go
// 声明包级别的全局变量
var (
   // 一般情况下，当变量 `a` 和变量 `b` 类型相同时，才能进行 `a = b` 的赋值，声明与赋值语句可以组合使用
    HOME = os.Getenv("HOME")
    USER = os.Getenv("USER")
    GOROOT = os.Getenv("GOROOT")
)
// 声明与赋值，声明 var NAME TYPE 
var power int = 9000
// 声明及赋值 NAME := VALUE，变量类型推断
power := 9000
// 多个变量同时赋值
name, power := "GoKu", 9000
// 字符串和字节数组紧密相关，可以轻松转换，字符串是不可变的，转换实际上创建了数据的副本。
stra := "the spice must flow"
byts := []byte(stra)
strb := string(byts)
```

###### 常量

常量使用关键字 `const` 定义，用于存储不会改变的数据，存储在常量中的数据类型只可以是基本类型，可以省略类型说明符 `[type]`，编译器可以根据值和上下文来推断其类型

```go
const identifier [type] = value
```

常量的值必须是能够在编译时就能够确定的，可以在其赋值表达式中涉及计算过程，但所有用于计算的值必须在编译期间就能获得。在编译期间自定义函数均属于未知，因此无法用于常量的赋值，但内置函数可以使用。

数字型的常量是没有大小和符号的，并且可以使用任何精度而不会导致溢出，当常量赋值给一个精度过小的数字型变量时，可能会因为无法正确表达常量所代表的数值而导致溢出，这会在编译期间引发错误

```go
// 常量运行使用并行赋值形式
const beef, two, c = "eat", 2, "veg"
const Monday, Tuesday, Wednesday, Thursday, Friday, Saturday = 1, 2, 3, 4, 5, 6
const (
    Monday, Tuesday, Wednesday = 1, 2, 3
    Thursday, Friday, Saturday = 4, 5, 6
)
```

##### 包管理

go install 是 Go 中自动包安装工具：如需要将包安装到本地它会从远端仓库下载包：检出、编译和安装一气呵成。在包安装前的先决条件是要自动处理包自身依赖关系的安装。被依赖的包也会安装到子目录下，但是没有文档和示例。`go install` 使用了 GOPATH 变量

###### 导入

go 有很多内建函数，可以在没有引用的情况下直接使用。`import` 关键字被用于声明文件中代码要使用的包

```go
import (
  "fmt",
  "os"
)
// 或者
import "fmt"
import "os"
```

###### 包

在 Go 中，包名遵循 Go 项目的目录结构。命名一个包时，可以通过 `package` 关键字，提供一个值，而不是完整的层次结构。当导入一个包时，需要指定完整路径。导入包不能形成循环导入。Go 用了一个简单的规则定义什么类型和函数包外可见：**如果类型或函数名称以一个大写字母开始，它就具有了包外可见性。如果以一个小写字母开始，它就不可以，对结构体一样，如果一个字段名以一个小写字母开始，只有包内的代码可以访问它们**。可以使用 `models.func` 访问包内函数。

可以使用 `get` 子命令来获取第三方库。`go get` 支持各种协议，`go get` 获取远端的文件并把它们存储在工作区中。如果在一个项目内使用 `go get`，它将浏览所有文件，查找 `imports` 的第三方库然后下载它们。使用 `go get -u` 更新所有包，或 `go get -u FULL_PACKAGE_NAME` 更新一个具体的包。

##### 接口

接口定义了合约但并没有实现的类型

```go
type Logger interface {
	Log(message string)
}
// 在结构中使用
type Server struct {
    logger Logger
}
// 函数参数
func process(logger Logger) {
    logger.log("hello!")
}
```

内部有一个没有任何方法的空接口：`interface{}`。空接口时隐式实现的，因此每种类型都满足空接口契约。为了将一个接口变量转化为一个显式的类型，可以用 `.(TYPE)`

```go
func add(a interface{}, b interface{}) interface{} {
    return a.(int) + b.(int)
}
```

##### 句法

###### 代码风格

在一个项目内的时候，可以运用格式化规则到这个项目及其子项目

```go
go fmt ./..
```

###### 流程控制

Go 提供了下面这些条件结构和分支结构：

* `if-else` 结构

    ```go
    if condition {
        
    } else if condition1 {
        
    } else {
        
    }
    ```

* `switch` 结构

    ```go
    switch var1 {
        case varl:
        case var2:
        default:
    }
    ```

    变量 `var1` 可以是任何类型，而 `var1` 和 `var2` 则可以是同类型的任意值，类型不被局限于常量或整数，但必须是相同的类型；或者最终结果为相同类型的表达式

    可以同时测试多个可能符合条件的值，使用逗号分隔

    ```go
    case var1, var2, var3
    ```

    不需要使用 `break` 语句来表示结束，因此，程序也不会自动地去执行下一个分支的代码，如果在执行完每个分支的代码后，还希望继续执行后续分支的代码，可以使用 `fallthrough` 关键字来达到目的

    任何支持进行相等判断的类型都可以作为测试表达式的条件，包括 `int`，`string`，指针等

    ```go
    // switch 语句包含一个初始化语句
    switch a, b := x[i], y[j]; {
        case a < b: t = -1
        case a == b: t = 0
        case a > b: t = 1
    }
    ```

* `select` 结构，用于 `channel` 的选择

* for 结构

    ```go
    // 基于计数器的迭代
    for i :=0; i < 5; i++ {
        ...
    }
    // 基于条件判断的迭代
    for i >= 0 {
        
    }
    // 无限循环
    for {
        
    }
    // for-range for ix, val := range col {}
    for pos, char := range str {
        
    }
    ```

    for-range 是 go 特有的一种迭代结构，它可以迭代任何一个集合，`val` 始终为集合中对应索引的值拷贝，对它所做的任何修改都不会影响到集合中原有的值（如果 `val` 为指针，则会产生指针的拷贝，依旧可以修改集合中的原值）

    使用 `break` 退出循环，一个 `break` 的作用范围为该语句出现后的最内部的结构，它可以被用于任何形式的 for 循环（计数器，条件判断）。在 `switch` 或 `select` 语句中，`break` 语句的作用结果是跳过整个代码块，执行后续代码

    关键字 continue 只能被用于 for 循环中

Go 完全省略了 `if`，`switch`，`for` 结构中条件语句两侧的括号。`for`，`switch`，`select` 语句都可以配合标签形式的标识符使用（即某一行第一个以冒号 `:` 结尾的单词，标签是大小写敏感的，一般建议使用全部大写字母

#### 并发

##### go 协程

使用 `go` 关键字然后使用想要执行的函数，也可以之间使用匿名函数

```go
go func() {
    fmt.Println("processing")
}
func process() {
    fmt.Println("Processing")
}
go process()
```

协程易于创建且开销很小，多个协程将会在同一个线程上运行。主进程在退出前协程才会有机会运行（主进程在退出前不会等待全部协程执行完毕）

##### 同步

常规操作是使用互斥量（`sync.Mutex`），互斥量序列化会锁住锁下的代码访问。通道在共享不相关数据的情况下，让并发编程变得更健壮。通道是协程之间用于传递数据的共享管道。一个协程可以通过一个通道向另外一个协程传递数据。在任意时间点，只有一个协程可以访问数据

一个通道，和其他任何变量一样，都有一个类型。这个类型是在通道中传递的数据的类型

```go
// 创建一个通道用于传递一个整数
c := make(chan int)
// 使用
func worker(c chan int) {}
```

通道只支持两个操作：接收和发送。接收和发送操作是阻塞的。当我们从一个通道接收的时候，`goroutine` 将会直到数据可用才会继续执行。

```go
// 向通道发送数据
CHANNEL <- DATA
// 从通道接收数据
VAR := <-CHANNEL
```

如果没有 worker 可用，想去临时存储数据在某些队列中。通道内建这种缓冲容量，使用 `make` 创建通道时，可以设置通道的长度

```go
c := make(chan int, 100)
```

使用 `select` ，可以提供当通道不能发送数据的时候处理代码。

```go
for {
    select {
        case c <- rand.Int():
        // 可选代码
        default:
        // 可以留空静默删除数据
    }
    time.Sleep(time.Millisecond * 50)
}
```

`select` 的主要目的是管理多个通道，`select` 将阻塞直到第一个通道可用。如果没有通道可用，如果提供了`default` ，将会执行。如果多个通道都可用，随机挑选一个，如果没有 default，select 将会阻塞

```go
for {
    select {
        case c <- rand.Int():
        case <-time.After(time.Millisecond * 100):
        fmt.Println("timed out")
    }
    time.Sleep(time.Millisecond * 50)
}
```

`time.After` 返回了一个通道，这个通道可以在指定时间之后被写入

#### 程序结构

##### 函数

函数是 Go 里面的基本代码块，Go 是编译型语言，函数编写顺序无关紧要，鉴于可读性的需求，最好把 `main()` 函数写在文件的前面，其他函数按顺序进行编写。函数执行到最后一行或 `return` 语句时会退出，`return` 语句可以带有零个或多个参数，作为返回值。简单的 return 语句可以用来结束 for 循环或结束一个协程

```go
// 函数被调用基本格式
pack1.Function(arg1, arg2,...argn)
```

Go 支持三种类型的函数：

* 普通的带有名字的函数
* 匿名函数或 lambda 函数
* 方法

除了 `main()`，`init()` 函数外，其他所有类型的函数都可以有参数或返回值。参数、返回值及它们的类型被统称为函数签名。函数可以将其他函数调用作为它的参数，只要这个被调用函数的返回值个数、返回值类型和返回值的顺序与调用函数所需求的实参是一致的。在 Go 里**函数不支持重载**，会导致编译错误。

```go
// 声明外部定义函数
func flushICache(begin, end uitptr)
// 以声明的方式被使用，作为一个函数类型
type binOp func(int, int) int
add := binOp
```

函数值之间可以相互比较，如果它们引用的是相同的函数或者都是 nil 的话，则认为它们是相同的函数。函数不能在其他函数里面声明（不能嵌套），可以使用匿名函数。

Go 没有泛型概念，不支持多种类型的函数。在大部分情况下可以通过接口，特别是空接口与类型选择或通过使用反射来实现相似功能，但会导致复杂与降低性能，最后为每一个类型单独创建一个函数，而且代码可读性更强

###### 参数与返回值

默认按值传递参数，在函数调用时，像切片（slice）、字典（map）、接口（interface）、通道（channel）这样的引用类型都是默认使用引用传递（即使没有显式的指出指针）。函数定义时，形参一般是有名字的，也可以定义没有形参名的函数，只有相应的形参类型

```go
func f(int, int, float64)
```

如果函数的最后一个参数是采用 `...type` 的形式，这个函数就可以处理一个变长的参数，这个长度可以为 0

```go
// 该函数接受一个类似某个类型的 slice 的参数，该参数可以通过 for 循环结构迭代
func myFunc(a, b, arg ...int) {}
```

如果参数被存储在一个 slice 类型的变量 slice 中，则可以通过 `slice...` 的形式来传递参数调用变参函数

函数可以作为其他函数的参数进行传递，然后在其他函数内调用执行

```go
func main() {
    callback(1, Add)
}
func Add(a, b int) {
    fmt.Print("The sum of %d and %d is: %d\n", a, b, a + b)
}
func callback(y int, f func(int, int)) {
    f(y, 2)
}
```

函数支持返回多个值

* 非命名返回值，当需要返回多个非命名返回值时，需要使用 `()` 把它们括起来

    ```go
    func getX2And3X(input int) (int, int) {
        return 2 * input, 3 * inptu
    }
    ```

* 命名返回值作为结果形参被初始化为相应类型的零值，当需要返回的时候，只需要一条简单的不带参数的 return 语句，即使只有一个命名返回值，也需要使用 `()` 括起来

    ```go
    func getX2AndX32(input int) (x2 int, x3 int) {
        x2 = 2 * input
        x3 = 3 * input
        return
    }
    ```

    即使函数使用了命名返回值，依旧可以无视而返回明确的值，任何一个非命名返回值在 return 语句里都要明确指出包含返回值的变量或是一个可计算的值，尽量使用命名返回值

空白符 `_` 用来匹配一些不需要的值，然后丢弃掉。

###### defer 和追踪

`defer` 关键字允许推迟到函数返回之前（或任意位置执行 return 语句之后）才执行某个语句或函数。类似于面向对象中的 `finally` 语句块，一般用于释放某些已分配的资源。当有多个 defer 行为被注册时，它们会以逆序执行（类似栈，后进先出）

```go
// 关闭文件流
defer file.Close()
// 解锁资源
mu.Lock()
defer mu.Unlock()
```

关键字 `defer` 经常配合匿名函数使用，可以用于改变函数的命名返回值

###### 闭包

当我们不希望给函数起名字的时候，可以使用匿名函数**表示参数列表的第一对括号必须紧挨着关键字 `func`**

```go
fplus := func(x, y int) int {
    return x + y
}
fplus(3, 4)
```

这样的函数不能够独立存在，但可以被赋值给某个变量，即保存函数的地址到变量中，然后通过变量名对函数进行调用，可以直接在函数体后跟 `()` 对匿名函数进行调用

```go
// 直接调用函数
func(x, y int) int {
    return x + y
}(3, 4)
// 将函数作为返回值
func Adder(a int) func(b int) int {
    return func(b int) int {
        return a + b
    }
}
```

闭包函数保存并积累其中的变量的值，不管外部函数退出与否，它都能继续操作外部函数中的局部变量，在闭包中使用的变量可以是在闭包函数体内声明的，也可以在外部函数声明的

```go
// 工厂函数
func MakeAddSuffix(suffix string) func(string) string {
    return func(name string) string {
        if !strings.HasSuffix(name, suffix) {
            return name + suffix
        }
        return name
    }
}
```

可以返回其它函数得函数和接受其他函数作为参数得函数为高阶函数，是函数式语言得特点，Go 语言具有一些函数式语言得特性，闭包在 Go 语言中非常常见，常用于 `goroutine` 和管道操作

##### 数组

数组是具有相同**唯一类型**的一组已编号且长度固定的数据项序列，如果想让数组元素类型为任意类型的话可以使用空接口作为类型，当使用值时做类型判断。数组长度最大为 2Gb

```go
var identifier [len]type
var arr1 [5]int
for i := 0; i < len(arr1); i++ {
    arr[i] = i * 2
}
// 使用 for-range 遍历
for index, value := range arr1
```

数组是一种值类型（不像 C/C++ 中是指向首元素的指针），可以通过 `new()` 来创建

```go
// *[5]int
var arr1 = new([5]int)
// [5]int
var arr2 [5]int
// func1(arr2) 不会修改原始的数组 arr2
func1(arr2)
```

如果数组值已经提前知道了，可以通过数组常量的方法来初始化数组，而不用以此使用 `[]=` 方法

```go
var arrLazy = []int{5, 6, 7, 8, 9}
// 有 10 个元素的数组，除前三个元素外其他元素都为 0
var a10 = [10]int {1, 2, 3}
// 只有索引 3 和 4 被赋予实际值，其他元素为空字符串
var arrKeyValue = [5]string{3: "Chris", 4: "Ron"}
```

go 语言的多维数组是矩形式的

```go
const WIDTH = 1929
const HEIGHT = 1080
type pixel int
var screen [WIDTH][HEIGHT]pixel
```

###### 切片（slice）

是对数组一个连续片段的引用（该数组称之为相关数组，通常是匿名的），切片是一个引用类型（类似于 C/C++ 中的数组类型，或者 Python 中的 list 类型）。这个片段可以是整个数组，或者由起始或终止索引标识的一些项的子集。**终止索引标识的项不包括在切片内**，切片提供了一个相关数组的动态窗口

切片是可索引的，并且可以由 `len()` 函数获取长度，切片的长度可以在运行时修改，最小为 0 最大为相关数组的长度

切片提供了计算容量的函数 `cap()` 可以测量切片最长可以达到多少：它等于切片从第一个元素开始，到相关数组末尾的元素个数。如果 s 是一个切片，`cap(s)` 就是从 `s[0]` 到数组末尾的数组长度。切片的长度永远不会超过它的容量，即 `0 <= len(s) <= cap(s)`

多个切片如果表示同一个数组的片段，它们可以共享数据；因此一个切片和相关数组的其他切片是共享存储的，相反，不同的数组总是代表不同的存储，数组实际上是切片的构建块

```go
// 声明切片，不需要说明长度
var identifier []type
// 切片的初始化格式,由数组 arr1 从 start 索引到 end -1 索引之间的元素构成的子集
var slice1 []type = arr1[start:end]
// 初始化切片(创建了长度为 5 的数组且创建了一个相关切片)
var x = []int{2,3,5,7,11}
// slice1 等于完整数组
var slice1 []type = arr1[:]
// 由数字 1，2，3 组成的切片
s := []int{1,2,3}
// 用切片组成切片,拥有相同的元素，但仍然指向相同的相关数组
s2 := s[:]
// 扩展 s 到大小上限
s = s[:cap(s)]
```

一个切片在未初始化之前默认未 nil，长度为 0

切片在内存中的组织方式实际上是一个有 3 个域的结构体：指向相关数组的指针，切片长度以及切片容量

当相关数组还未定义时，可以使用 `make()` 函数来创建一个切片同时创建相关数组

```go
var slice1 []type = make([]type, len)
// 简写, len 即是数组的长度也是 slice 的初始长度
slice1 := make([]type, len)
```

切片通常也是一维的，但是也可以由一维组合成高维。通过分片的分片（或者切片的数组）长度可以任意动态变化，Go 语言的多维切片可以任意切分，而内层的切片必须单独分配（通过make函数）

```go
// start_length 为切片初始长度，capacity 作为相关数组长度
slice1 := make([]type, start_length, capacity)
// 重组,end 是新的末尾索引
slice1 = slice1[0:end]
// 切片扩展一位
sl = sl[0:len(sl)+1]
```

如果想增加切片的容量，必须创建一个新的更大的切片并把原分配的内容都拷贝过去，使用切片拷贝函数 `copy` 和追加新元素的 `append` 函数

切片的底层指向一个数组，该数组的实际容量可能要大于切片所定义的容量，只有在没有任何切片指向的时候，底层的数组内存才会被释放，这种特性有时会导致程序占用多余的内存

##### Map

一种元素对（pair）的无序集合，`pair` 的一个元素是 `key`，对应的另一个元素是 `value`，map 是引用类型

```go
// keytype 和 valuetype 之间允许有空格
var map1 map[keytype]valuetype
var map1 map[string]int
// 初始化
var map1 = make(map[keytype]valuetype)
map1 := make(map[keytype]valuetype)
map1 = map[string]int{"one": 1, "two": 2}
// 指定长度
mp2 := make(map[keytype]valuetype, cap)
map2 := make(map[string]float32, 100)
// 赋值及访问使用 []
map1[key1] = val1
// 将 key1 对应的值赋值为 v，如果 map 中没有 key1 存在，v 将被赋值为 map1 的值类型的空值
v := map1[key1]
// 用切片作为 map 的值
mp1 := make(map[int][]int)
mp2 := make(map[int]*[]int)
```

在声明的时候不需要知道 map 的长度，map 是可动态增长的，可以指定 map 的初始容量，当 map 增长到容量上限的时候，如果再增加新的 key-value 对，map 的大小会自动加 1，出于性能考虑，对于大的 map 或者会快速扩张的 map，即使只是大概知道容量，也最好先编码

未初始化的 map 的值是 nil，key 可以是任意可以用 `==` 或 `!=` 操作符比较的类型（string，int，float，指针，接口）。数组，切片和结构体不能作为 key，如果要用结构体作为 key 可以提供 `key()` 和 `hash()` 方法，这样可以通过结构体的域计算出唯一的数字或字符串的 key。`value` 可以是任意类型的；通过使用空接口类型，可以存储任意值，但是使用这种类型作为值时需要先做一次类型断言

```go
// 测定是否存在 key1, isPresent 返回一个 bool 值，如果 key1 存在于 map1，va11 就是 key1 对应的 value 值，isPresent 为 true，如果 key1 不存在，val1 是一个控制，isPresent 为 false
val1, isPresent = map1[key1]
// 与 if 混用
if _, ok := map1[key1]; ok {
    
}
// 删除 map1 中的 key1
delete(map1, key1)
// for 循环 map
for key, value := range map1 {
    
}
```

map 类型为非线程安全的，当并行访问一个共享的 map 类型的数据，map 数据将会出错

##### 结构

Go 通过类型别名和结构体的形式支持用户自定义类型。结构体是复合类型，当需要定义一个类型，它由一系列属性组成，每个属性都有自己的类型和值。结构体是值类型，可以通过 `new` 函数来创建，组成结构体类型的那些数据为字段。每个字段都有一个类型和一个名字，在一个结构体中，字段名字必须是唯一的

```go
type identifier struct {
    field1 type1
}
type T struct {a, b int}
// 使用 new 给结构体分配内存，返回指向已分配内存的指针
var t *T = new(T)
// t 指向 T 的指针，结构体字段的值是它们所属类型的零值
t := new(T)
// 结构体字段赋值
structname.fieldname = value
// 初始, 底层仍然调用 new()
ms := &structname{10, 15.5, "Chris"}
```

结构体的字段可以是任何类型，甚至是结构体本身。`var t T` 也会给 `t` 分配内存，并零值化内存，但这个时候 `t` 是类型 T。

结构体和它所包含的数据在内存中是以连续块的形式存在的，即使结构体中嵌套有其他的结构体，这在性能上带来了很大优势。结构体类型可以通过引用自身来定义（这在定义链表或二叉树的元素时特别有用，此时节点包含指向临近节点的链接）

```go
type Node struct {
    pr *Node
    data float64
    su *Node
}
type Tree struct {
    le *Tree
    data float64
    ri *Tree
}
```

当为结构体定义了一个 alias 类型时，此结构体类型和它的 alias 类型都有相同的底层类型。结构体中的字段除了有名字和类型外，还可以有一个可选的标签（tag）：它是一个附属于字段的字符串，可以是文档或其他的重要标记。标签的内容不可以在一般的编程中使用，只有包 `reflect` 能获取它

```go
type TagType struct {
    field1 bool "An important answer"
    field2 string "The name of the thing"
}
func refTag(tt TagType, ix int) {
    ttType := reflect.TypeOf(tt)
    ixField := ttType.Field(ix)
    fmt.Printf("%v\n", ixField.Tag)
}
```

结构体可以包含一个或多个匿名（内嵌）字段，这些字段没有显示的名字，只有字段的类型是必须的，此时类型就是字段的名字。匿名字段本身可以是一个结构体类型，即结构体可以包含内嵌结构体。**在一个结构体中对于每一种数据类型只能有一个匿名字段**

```go
type innerS struct {
    in1 int
    in2 int
}
type outerS struct {
    b    int
    c    float32
    int  // anonymous field
    innerS //anonymous field
}
func main() {
    outer := new(outerS)
    outer.b = 6
    outer.c = 7.5
    outer.int = 60
    outer.in1 = 5 // 外层结构体直接进入内存结构体的字段，内嵌结构体可以来自其他包
    outer.in2 = 10
    // 使用结构体字面量
    outer2 := outerS{6, 7.5, 60, innerS{5, 10}}
    fmt.Println("outer2 is:", outer2)
}
```

当两个字段拥有相同的名字时：

* 外层名字会覆盖内层名字（但是两者的内存空间都保留），这提供了一种重载字段或方法的方式
* 如果相同的名字在同一级别出现了两次，如果这个名字被程序使用了，将会引发一个错误（不使用没关系），没有办法来解决这种问题引起的二义性，必须由程序员自己修正

###### 方法

在 Go 中，它和方法有着同样的名字，并且大体上意思相同：Go 方法是作用在接收者上的一个函数，接收者是某种类型的变量。因此方法是一种特殊类型的函数

接收者类型可以是任何类型，不仅仅是结构体类型：任何类型都可以有方法，甚至是函数类型，可以是 `int`，`bool`，`string` 或数组的别名类型。但接收者不能是一个接口类型也不能是一个指针类型，但可以是任何其他允许类型的指针

一个类型加上它的方法等价于面向对象中的一个类，一个重要的区别是：在 Go 中，类型的代码和绑定在它上面的方法的代码可以不放置在一起，它们可以存在在不同的源文件，它们必须是同一个包的

方法是函数，不允许方法重载，对于一个类型只能有一个给定名称的方法。但是如果基于接收者类型，是有重载的：具有相同名字的方法可以多个不同的接收者类型上存在

```go
// 方法基于接受者重载
func (a *denseMatrix) Add(b Matrix) Matrix
func (a *sparseMatrix) Add(b Matrix) Matrix
```

别名类型不能有它原始类型上已经定义过的方法

```go
// 方法名之前，func 关键字之后的括号中指定 receiver
func (recv receiver_type) methodName(parameter_list)(return_value_list) {
    ...
}
// 结构体类型上方法
type TwoInts struct {
    a int
    b int
}
func (tn *TwoInts) AddThem() int {
    return tn.a + tn.b
}
// 非结构体类型上方法
type IntVector []int
func (v IntVecotr) Sum() (s int) {
    for _, x := range v {
        s += x
    }
    return
}
```

接收者类型和作用在它上面定义的方法必须在同一个包里定义，不能在 `int`、`float` 或类似这些的类型上定义方法，接收者类型关联的方法不写在类型结构里面，类型和方法之间的关联由接收者来建立

```go
// 使用别名间接定义
type myTime struct {
	time.Time
}
func (t myTime) formatYYYYMMDDHHIISS() string {
    return t.Time.String()[0:19]
}
```

函数将变量作为参数：`Function1(recv)`

方法在变量上被调用：`recv,Method1()`

在接收者是指针时，方法可以改变接收者的值（或状态），当参数作为指针传递，函数也可以改变参数的状态。出于性能的原因，`recv` 最常见的是一个指向 `receiver_type` 的指针，如果想要方法改变接收者的数据，就在接收者的指针类型上定义该方法。否则，就在普通的值类型上定义该方法

在值和指针上调用方法：

* 可以有连接到类型的方法，也可以有连接到类型指针的方法
* 对于类型 T，如果在 `*T` 上存在方法 `Meth()`，并且 `t` 是这个类型的变量，那么 `t.Meth()` 会被自动转换为 `(&t).Meth()`
* 指针方法和值方法都可以在指针或非指针上被调用
* 对于其他包中类型（Person）被明确导出了，但它的字段没有被导出，在其他包中访问`p.firstName` 就是错误的。可以通过在类型上使用 `getter` 和 `setter` 方法

对象的字段不应该由 2 个或 2 个以上的不同线程在同一时间去改变，可以使用包 `sync` 中的方法，或 `goroutines` 和 `channels`

当一个匿名类型被内嵌在结构体中时，匿名类型的可见方法也同样被内嵌，这在效果上等同于外层类型继承了这些方法，将父类型放在子类型中来实现亚型。

```go
type Point struct {
    x, y float64
}
func (p *Point) Abs() float64 {
    return math.Sqrt(p.x*p.x + p.y*p.y)
}
type NamedPoint struct {
    Point
    name string
}
n := &NamedPoint{Point(3, 4), "Pythagoras"}
fmt.Println(n.Abs())
```

内嵌将一个已存在类型的字段和方法注入到另一个类型里：匿名字段上的方法“晋升”成外层类型上的方法。可以覆写方法，和内嵌类型方法具有同样名字的外层类型的方法会覆写内嵌类型对应的方法。结构体内嵌和自己在同一包中的结构体时，可以彼此访问对方所有的字段和方法

有两种方法来实现在类型中嵌入功能：

* 聚合（组合）：包含一个所需功能类型的具名字段
* 内嵌（匿名的）所需功能类型，如果内嵌类型潜入了其他类型，也是可以的，那些类型的方法可以直接在外层类型中使用，可以内嵌多个父类型来实现类似多重继承功能

如果类型定义了 `String()` 方法，它会被用在 `fmt.Printf()`、`fmt.Print()`、`fmt.Println()`  中生成默认的输出，等同于使用格式化描述符 `%v` 产生的输出。当广泛使用自定义类型时，最好为它定义 `String()` 方法，不要在 `String()` 方法里调用涉及 `String()` 方法的方法，它会导致意料之外的错误

##### 接口

接口定义了一组方法，但是这些方法不包含实现代码，它们没有被实现，接口里也不能包含变量，**约定只包含一个方法的接口的名字由方法名加 `[e]r` 后缀组成，当 后缀 er 不合适时，以 able 结尾，或者以 I 开头**

在 Go 语言中接口可以有值，一个接口类型的变量或一个接口值 `var ai Namer`，`ai` 是一个多字数据结构，它的值是 nil，本质上是一个指针，虽然不完全是一回事，指向接口值得指针是非法得，它们会导致代码错误

* 类型（结构体）实现接口方法，每个方法的实现说明了此方法是如何作用于该类型得，同时方法集也构成了该类型的接口。
* 实现了 `Namer` 接口类型的变量可以赋值给 `ai` （接收者值），此时方法表中的指针会指向被实现的接口方法。当然如果另一个类型（也实现了该接口）的变量被赋值给 `ai`，这二者也会随之改变

* 类型不需要显式声明它实现了某个接口：接口被隐式地实现，多个类型可以实现同一个接口，实现某个接口的类型（除了实现接口方法外）可以有其他的方法。
* 一个类型可以实现多个接口，接口类型可以包含一个实例的引用，该实例的类型实现了此接口（接口是动态类型）

* 即使接口在类型之后才定义，二者处于不同的包中，被单独编译：只要类型实现了接口中的方法，它就实现了接口

一个接口可以包含一个或多个其他的接口，这相当于直接将这些内嵌接口的方法列举在外层接口中一样

```go
type ReadWrite interface {
    Read(b Buffer) bool
    Write(b Buffer) bool
}
type Lock interface {
    Lock()
    Unlock()
}
type File interface {
    ReadWrite
    Lock
    Close()
}
```

一个接口类型的变量 `varI` 中可以包含任何类型的值，必须有一种方式来检测它的动态类型，即运行时在变量中存储的值的实际类型，在执行过程中动态类型可能会有所不同，但是它总是可以分配给接口变量本身的类型。通常可以使用类型断言来测试在某个时刻 `varI` 是否包含类型 `T` 的值

```go
// varl 必须是一个接口变量
v := varI.(T)
// 安全的类型断言，如果转换合法，v 是 varI 转换到类型 T 的值，ok 会是 true，否则，v 是类型 T 的零值，ok 是 false，也没有运行时错误发生
if v, ok := varI.(T); ok {
    Process(v)
    return
}
// 使用 switch 结构检测
switch t := areaIntf.(type) {
    case *Square:
    	fmt.Printf("Type Square %T with value %v\n", t, t)
    case *Circle:
    	fmt.Printf("Type Circle %T with value %v\n", t, t)
	case nil:
    	fmt.Printf("nil value: nothing to check?\n")
	default:
    	fmt.Printf("Unexpected type %T\n", t)
}
```

在接口上调用方法时，必须有和方法定义时相同的接收者类型或者是可以从具体类型 `P` 直接可以辨识的：

* 指针方法可以通过指针调用
* 值方法可以通过值调用
* 接收者是值得方法可以通过指针调用，因为指针会首先被解引用
* 接收者是指针得方法不可以通过值调用，因为存储在接口中得值没有地址

将一个值赋值给一个接口时，编译器会确保所有可能得接口方法都可以在此值上被调用，因此不正确得赋值在编译期就会失败，Go 语言规范定义了接口方法集得调用规则：

* 类型 T 的可调用方法集包含接受者为 T 或 T 的所有方法集
* 类型 T 的可调用方法集包含接收者为 T 的所有方法
* 类型 T 的可调用方法集不包含接受者为 *T 的方法

空接口或最小接口不包含任何方法，它对实现不做任何要求，任何其他类型都实现了空接口

```go
type Any interface {}
// 给空接口类型的变量赋任何类型的值
var val interface {}
```

每个 `interface{}` 变量在内存中占据两个字长：一个用来存储它包含的类型，另一个用来存储它包含的数据或者指向数据的指针

一个接口的值可以赋值给另一个接口变量，只要底层类型实现了必要的方法。这个转换是在运行时进行检查的，转换失败会导致一个运行时的错误

当一个类型包含（内嵌）另一个类型（实现了一个或多个接口）的指针时，这个类型就可以使用（另一个类型）所有的接口方法

