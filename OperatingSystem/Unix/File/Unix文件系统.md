## IO

### 输入和输出

#### 文件描述符

文件描述符是一个小的非负整数，内核用以标识一个特定进程正在存访的文件，当内核打开一个现存文件或创建一个新文件时，返回一个文件描述符。读写文件时，可以使用它。

每当运行一个新程序时，所有的 s h e l l都为其打开三个文件描述符：标准输入、标 准输出以及标准出错

函数 `open`、`read`、`write`、`lseek`、`close` 提供了不带缓存的 I / O 

### 标准 I/O

标准 I/O 函数为那些不带缓冲的 I/O 函数提供了一个带缓冲的接口。使用标准 I/O 函数无需担心如何选取最佳的缓冲区大小，并简化了对输入行的处理

