package org.jxxy.debug.barcode;

/** @author asus */
public class CustomException extends Exception {

    // 无参构造方法
    public CustomException() {

        super();
    }

    // 有参的构造方法
    public CustomException(String message) {
        super(message);
    }

    // 用指定的详细信息和原因构造一个新的异常
    public CustomException(String message, Throwable cause) {

        super(message, cause);
    }

    // 用指定原因构造一个新的异常
    public CustomException(Throwable cause) {

        super(cause);
    }
}

// 备注： 这些方法怎么来的？ 重写父类Exception的方法，那么如何查看Exception具有哪些API，快捷键：选中Exception, command+单击。windows系统
// ：选中Exception, control+单击。
